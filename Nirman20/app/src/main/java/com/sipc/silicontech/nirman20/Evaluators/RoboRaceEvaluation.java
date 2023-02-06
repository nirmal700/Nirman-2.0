package com.sipc.silicontech.nirman20.Evaluators;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sipc.silicontech.nirman20.R;
import com.sipc.silicontech.nirman20.Users.Suggestion;

import java.util.Objects;

public class RoboRaceEvaluation extends AppCompatActivity {
    Button start, stop, reset, inc1, dec1, inc2, dec2, mTimeOut, mSubmit, inc3, dec3;
    TextInputLayout et_teamName, et_collegeName, et_suggestion;
    TextView disp1, disp2, disp3;

    int count1, count2, count3;
    int sec, min, miliSec;
    ProgressDialog progressDialog;
    String mTeamName, mCollegeName;
    DocumentReference mDocumentReference;
    private int mTechTimeOut = 0;
    private boolean mTechTimeTaken = false;
    private Chronometer chronometer, mChronoTimer;
    private long pauseOffset;
    private boolean running, isResume;
    private Handler handler;
    private long tMiliSec, tStart, tBuff, tUpdate = 0L;
    public Runnable runnable = new Runnable() {
        @Override
        public void run() {
            tMiliSec = SystemClock.uptimeMillis() - tStart;
            tUpdate = tBuff + tMiliSec;
            sec = (int) (tUpdate / 1000);
            min = sec / 60;
            sec = sec % 60;
            miliSec = (int) (tUpdate % 100);
            mChronoTimer.setText(String.format("%02d", min) + ":" + String.format("%02d", sec) + ":" + String.format("%02d", miliSec));
            handler.postDelayed(this, 60);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.robo_race_evaluation);
        start = findViewById(R.id.start);
        stop = findViewById(R.id.stop);
        reset = findViewById(R.id.reset);
        mChronoTimer = findViewById(R.id.timer);
        inc1 = findViewById(R.id.incrementer1);
        inc2 = findViewById(R.id.incrementer2);
        dec1 = findViewById(R.id.decrementer1);
        dec2 = findViewById(R.id.decrementer2);
        inc3 = findViewById(R.id.incrementer3);
        dec3 = findViewById(R.id.decrementer3);
        disp1 = findViewById(R.id.disp1);
        disp2 = findViewById(R.id.disp2);
        disp3 = findViewById(R.id.disp3);
        mSubmit = findViewById(R.id.btn_submit);
        mTimeOut = findViewById(R.id.btn_timeout);
        et_teamName = findViewById(R.id.et_teamName);
        et_collegeName = findViewById(R.id.et_collegeName);
        et_suggestion = findViewById(R.id.et_suggestion);

        mTeamName = getIntent().getStringExtra("mTeamName");
        mCollegeName = getIntent().getStringExtra("mCollegeName");

        Objects.requireNonNull(et_teamName.getEditText()).setText("" + mTeamName);
        Objects.requireNonNull(et_collegeName.getEditText()).setText("" + mCollegeName);
        et_teamName.setEnabled(false);
        et_collegeName.setEnabled(false);

        progressDialog = new ProgressDialog(RoboRaceEvaluation.this);
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.dismiss();


        handler = new Handler();


        start.setOnClickListener(view -> startTimer());
        stop.setOnClickListener(view -> stopTimer());
        reset.setOnClickListener(view -> resetTimer());


        inc1.setOnClickListener(v -> {
            count1++;
            disp1.setText(String.format("%d", count1));
        });
        dec1.setOnClickListener(v -> {
            if (count1 <= 0) count1 = 0;
            else count1--;
            disp1.setText(String.format("%d", count1));
        });

        inc2.setOnClickListener(v -> {
            count2++;
            disp2.setText(String.format("%d", count2));
        });
        dec2.setOnClickListener(v -> {
            if (count2 <= 0) count2 = 0;
            else count2--;
            disp2.setText(String.format("%d", count2));
        });


        inc3.setOnClickListener(v -> {
            count3++;
            disp3.setText(String.format("%d", count3));
        });
        dec3.setOnClickListener(v -> {
            if (count3 <= 0) count3 = 0;
            else count3--;
            disp3.setText(String.format("%d", count3));
        });


        chronometer = findViewById(R.id.chronometer);
        chronometer.setFormat("Time: %s");
        chronometer.setBase(SystemClock.elapsedRealtime());
        mTimeOut.setOnClickListener(view -> {
            mTimeOut.setClickable(false);
            mTechTimeOut++;
            if (mTechTimeOut <= 1) {
                stopTimer();
                startChronometer(view);
                start.setVisibility(View.INVISIBLE);
                stop.setVisibility(View.INVISIBLE);
                reset.setVisibility(View.INVISIBLE);
                mTechTimeTaken = true;
            } else {
                Toast.makeText(RoboRaceEvaluation.this, "No More Technical Timeout!", Toast.LENGTH_SHORT).show();
            }
        });

        chronometer.setOnChronometerTickListener(chronometer -> {
            if ((SystemClock.elapsedRealtime() - chronometer.getBase()) >= 10000) {
                chronometer.setBase(SystemClock.elapsedRealtime());
                Toast.makeText(RoboRaceEvaluation.this, "Bing! Time Over!", Toast.LENGTH_SHORT).show();
                chronometer.stop();
                running = false;
                start.setVisibility(View.VISIBLE);
                stop.setVisibility(View.VISIBLE);
                reset.setVisibility(View.VISIBLE);
                mTimeOut.setClickable(true);
                startTimer();
            }
        });

        mSubmit.setOnClickListener(view -> {
            progressDialog.show();
            String mTeamName, mSuggestion;
            long mTotalTime, mCheckPoints, mHandTouches;
            mTeamName = et_teamName.getEditText().getText().toString();


            String[] timeArray = mChronoTimer.getText().toString().split(":");
            int minutes = Integer.parseInt(timeArray[0]);
            int seconds = Integer.parseInt(timeArray[1]);
            mTotalTime = (minutes * 60L) + seconds;

            mCheckPoints = Long.parseLong(disp1.getText().toString());
            mHandTouches = Long.parseLong(disp2.getText().toString());
            long mBonus = Long.parseLong(disp3.getText().toString());
            mSuggestion = Objects.requireNonNull(et_suggestion.getEditText()).getText().toString();
            DatabaseReference mSugDB = FirebaseDatabase.getInstance().getReference("Suggestions_Team").child("Robo Race").child(mTeamName).child("Suggestions");
            String id = mSugDB.push().getKey();
            if (mSuggestion.length() > 0 & id != null) {
                Suggestion suggestion = new Suggestion(mTeamName, mCollegeName, mSuggestion, id, true, false, 0L);
                mSugDB.child(id).setValue(suggestion);
            }
            mDocumentReference = FirebaseFirestore.getInstance().collection("Robo Race").document(mTeamName);
            mDocumentReference.update("mCheckPointCleared", mCheckPoints, "mHandTouches", mHandTouches, "mTimeOutTaken", mTechTimeTaken, "mTotalTimeTaken", mTotalTime, "mBonus", mBonus).addOnCompleteListener(task -> {
                startActivity(new Intent(getApplicationContext(), EvaluatorDashboard.class));
                finish();
            }).addOnSuccessListener(unused -> Toast.makeText(RoboRaceEvaluation.this, "Completed!!", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(RoboRaceEvaluation.this, "Failed!!", Toast.LENGTH_SHORT).show());
        });


    }

    private void resetTimer() {
        tMiliSec = 0L;
        tStart = 0L;
        tBuff = 0L;
        tUpdate = 0L;
        sec = 0;
        min = 0;
        miliSec = 0;
        mChronoTimer.setText("00:00:00");
    }

    private void stopTimer() {

        if (isResume) {
            tBuff += tMiliSec;
            handler.removeCallbacks(runnable);
            mChronoTimer.stop();
            isResume = false;
            stop.setVisibility(View.INVISIBLE);
            start.setVisibility(View.VISIBLE);
        }

    }

    private void startTimer() {

        if (!isResume) {
            tStart = SystemClock.uptimeMillis();
            handler.postDelayed(runnable, 0);
            mChronoTimer.start();
            isResume = true;
            start.setVisibility(View.INVISIBLE);
            stop.setVisibility(View.VISIBLE);
        }

    }

    public void startChronometer(View v) {
        if (!running) {
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            chronometer.start();
            running = true;
        }
    }

    public void pauseChronometer(View v) {
        if (running) {
            chronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
            running = false;
            Log.e("TAG768", "onCreate: " + chronometer.getText().toString());
        }
    }

    public void resetChronometer(View v) {
        chronometer.setBase(SystemClock.elapsedRealtime());
        pauseOffset = 0;
        Log.e("TAG768", "onCreate: " + chronometer.getText().toString());
    }
}
