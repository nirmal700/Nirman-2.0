package com.sipc.silicontech.nirman20.Evaluators;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
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
import com.sipc.silicontech.nirman20.Users.UserDashBoard;

import java.util.Objects;

public class RoboRaceEvaluation extends AppCompatActivity {
    Button start, stop, reset, inc10, dec10, inc20, dec20, mTimeOut, mSubmit, inc30, dec30,mHandIncrement,mHandDecrement,mBonusIncrement,mBonusDecrement;
    TextInputLayout et_teamName, et_collegeName, et_suggestion;
    TextView disp10, disp20, disp30,mHandTouch,mBonus;

    int count1, count2, count3,countHand,countBonus;
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
    ImageView btn_backToSd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.robo_race_evaluation);
        btn_backToSd = findViewById(R.id.btn_backToSd);
        start = findViewById(R.id.start);
        stop = findViewById(R.id.stop);
        reset = findViewById(R.id.reset);
        mChronoTimer = findViewById(R.id.timer);
        inc10 = findViewById(R.id.incrementer10);
        inc20= findViewById(R.id.incrementer20);
        dec10 = findViewById(R.id.decrementer10);
        dec20 = findViewById(R.id.decrementer20);
        inc30 = findViewById(R.id.incrementer30);
        dec30 = findViewById(R.id.decrementer30);
        disp10 = findViewById(R.id.disp10);
        disp20 = findViewById(R.id.disp20);
        disp30 = findViewById(R.id.disp30);
        mBonusIncrement = findViewById(R.id.mBonusIncrement);
        mBonusDecrement = findViewById(R.id.mBonusDecrement);
        mHandIncrement = findViewById(R.id.mHandIncrement);
        mHandDecrement = findViewById(R.id.mHandDecrement);
        mBonus = findViewById(R.id.mBonus);
        mHandTouch = findViewById(R.id.mHandTouch);
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

        btn_backToSd.setOnClickListener(v -> {
            startActivity(new Intent(RoboRaceEvaluation.this, EvaluatorDashboard.class));
            finishAffinity();
        });


        handler = new Handler();

        if (!isConnected(RoboRaceEvaluation.this)) {
            showCustomDialog();
        }


        start.setOnClickListener(view -> startTimer());
        stop.setOnClickListener(view -> stopTimer());
        reset.setOnClickListener(view -> resetTimer());


        inc10.setOnClickListener(v -> {
            count1++;
            disp10.setText(String.format("%d", count1));
        });
        dec10.setOnClickListener(v -> {
            if (count1 <= 0) count1 = 0;
            else count1--;
            disp10.setText(String.format("%d", count1));
        });

        inc20.setOnClickListener(v -> {
            count2++;
            disp20.setText(String.format("%d", count2));
        });
        dec20.setOnClickListener(v -> {
            if (count2 <= 0) count2 = 0;
            else count2--;
            disp20.setText(String.format("%d", count2));
        });


        inc30.setOnClickListener(v -> {
            count3++;
            disp30.setText(String.format("%d", count3));
        });
        dec30.setOnClickListener(v -> {
            if (count3 <= 0) count3 = 0;
            else count3--;
            disp30.setText(String.format("%d", count3));
        });
        mHandIncrement.setOnClickListener(v -> {
            countHand++;
            mHandTouch.setText(String.format("%d", countHand));
        });
        mHandDecrement.setOnClickListener(v -> {
            if (countHand <= 0) countHand = 0;
            else countHand--;
            mHandTouch.setText(String.format("%d", countHand));
        });

        mBonusIncrement.setOnClickListener(v -> {
            countBonus++;
            mBonus.setText(String.format("%d", countBonus));
        });
        mBonusDecrement.setOnClickListener(v -> {
            if (countBonus <= 0) countBonus = 0;
            else countBonus--;
            mBonus.setText(String.format("%d", countBonus));
        });


        chronometer = findViewById(R.id.chronometer);
        chronometer.setFormat("Time: %s");
        chronometer.setBase(SystemClock.elapsedRealtime());
        mTimeOut.setOnClickListener(view -> {
            mTechTimeOut++;
            if (mTechTimeOut <= 1) {
                stopTimer();
                startChronometer(view);
                start.setVisibility(View.INVISIBLE);
                stop.setVisibility(View.INVISIBLE);
                reset.setVisibility(View.INVISIBLE);
                mTechTimeTaken = true;
            } else if (running == true) {
                chronometer.setBase(SystemClock.elapsedRealtime());
                Toast.makeText(RoboRaceEvaluation.this, "Bing! Time Over!", Toast.LENGTH_SHORT).show();
                chronometer.stop();
                running = false;
                start.setVisibility(View.VISIBLE);
                stop.setVisibility(View.VISIBLE);
                reset.setVisibility(View.VISIBLE);
                startTimer();
            } else {
                Toast.makeText(RoboRaceEvaluation.this, "No More Technical Timeout!", Toast.LENGTH_SHORT).show();
            }
        });

        chronometer.setOnChronometerTickListener(chronometer -> {
            if ((SystemClock.elapsedRealtime() - chronometer.getBase()) >= 30000) {
                chronometer.setBase(SystemClock.elapsedRealtime());
                Toast.makeText(RoboRaceEvaluation.this, "Bing! Time Over!", Toast.LENGTH_SHORT).show();
                chronometer.stop();
                running = false;
                start.setVisibility(View.VISIBLE);
                stop.setVisibility(View.VISIBLE);
                reset.setVisibility(View.VISIBLE);
                startTimer();
            }
        });

        mSubmit.setOnClickListener(view -> {
            progressDialog.show();
            String mTeamName, mSuggestion;
            long mTotalTime, mCheckPoints, mHandTouches,mBonusPoint,mTotal;
            mTeamName = et_teamName.getEditText().getText().toString();


            String[] timeArray = mChronoTimer.getText().toString().split(":");
            int minutes = Integer.parseInt(timeArray[0]);
            int seconds = Integer.parseInt(timeArray[1]);
            mTotalTime = (minutes * 60L) + seconds;

            mCheckPoints = Long.parseLong(disp10.getText().toString()) + Long.parseLong(disp20.getText().toString()) +Long.parseLong(disp30.getText().toString());

            mHandTouches = Long.parseLong(mHandTouch.getText().toString());
            mBonusPoint = Long.parseLong(mBonus.getText().toString());
            mSuggestion = Objects.requireNonNull(et_suggestion.getEditText()).getText().toString();

            mTotal = (10*Long.parseLong(disp10.getText().toString())) + (20*Long.parseLong(disp20.getText().toString())) + (30*Long.parseLong(disp30.getText().toString()));
            if(mHandTouches>5)
            {
                mTotal =mTotal -  5*(mHandTouches - 5);
            }
            if(mBonusPoint>0)
            {
                mTotal = mTotal + 5*mBonusPoint;
            }
            DatabaseReference mSugDB = FirebaseDatabase.getInstance().getReference("Suggestions_Team").child("Robo Race").child(mTeamName).child("Suggestions");
            String id = mSugDB.push().getKey();
            if (mSuggestion.length() > 0 & id != null) {
                Suggestion suggestion = new Suggestion(mTeamName, mCollegeName, mSuggestion, id, true, false, 0L);
                mSugDB.child(id).setValue(suggestion);
            }
            mDocumentReference = FirebaseFirestore.getInstance().collection("Robo Race").document(mTeamName);
            mDocumentReference.update("mCheckPointCleared", mCheckPoints, "mHandTouches", mHandTouches, "mTimeOutTaken", mTechTimeTaken, "mTotalTimeTaken", mTotalTime, "mBonus", mBonusPoint,"mTotal",mTotal).addOnCompleteListener(task -> {
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
            reset.setVisibility(View.VISIBLE);
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
            reset.setVisibility(View.INVISIBLE);
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
        }
    }

    public void resetChronometer(View v) {
        chronometer.setBase(SystemClock.elapsedRealtime());
        pauseOffset = 0;
    }

    private void showCustomDialog() {

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(RoboRaceEvaluation.this);
        builder.setMessage("Please connect to the internet")
                //.setCancelable(false)
                .setPositiveButton("Connect", (dialog, which) -> startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS))).setNegativeButton("Cancel", (dialog, which) -> {
                    startActivity(new Intent(getApplicationContext(), RoboRaceEvaluation.class));
                    finish();
                });
        android.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    //--------------- Check Internet Is Connected -----------
    private boolean isConnected(RoboRaceEvaluation userLogin) {

        ConnectivityManager connectivityManager = (ConnectivityManager) userLogin.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo bluetoothConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_BLUETOOTH);

        return (wifiConn != null && wifiConn.isConnected()) || (mobileConn != null && mobileConn.isConnected() || (bluetoothConn != null && bluetoothConn.isConnected())); // if true ,  else false

    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), EvaluatorDashboard.class));
        super.onBackPressed();
    }
}
