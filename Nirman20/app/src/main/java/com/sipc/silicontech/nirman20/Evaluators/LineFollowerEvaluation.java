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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sipc.silicontech.nirman20.R;
import com.sipc.silicontech.nirman20.Users.Suggestion;
import com.sipc.silicontech.nirman20.Users.UserDashBoard;

import java.util.Objects;

public class LineFollowerEvaluation extends AppCompatActivity {
    Button start, stop, reset, inc1, dec1, inc2, dec2, mTimeOut, mSubmit;
    TextInputLayout et_teamName, et_collegeName, et_suggestion;
    TextView disp1, disp2;

    int count1, count2;
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
        setContentView(R.layout.line_follower_evaluation);
        btn_backToSd = findViewById(R.id.btn_backToSd);

        start = findViewById(R.id.start);
        stop = findViewById(R.id.stop);
        reset = findViewById(R.id.reset);
        mChronoTimer = findViewById(R.id.timer);
        inc1 = findViewById(R.id.incrementer1);
        inc2 = findViewById(R.id.incrementer2);
        dec1 = findViewById(R.id.decrementer1);
        dec2 = findViewById(R.id.decrementer2);
        disp1 = findViewById(R.id.disp1);
        disp2 = findViewById(R.id.disp2);
        mSubmit = findViewById(R.id.btn_submit);
        mTimeOut = findViewById(R.id.btn_timeout);
        et_teamName = findViewById(R.id.et_teamName);
        et_collegeName = findViewById(R.id.et_collegeName);
        et_suggestion = findViewById(R.id.et_suggestion);

        mTeamName = getIntent().getStringExtra("mTeamName");
        mCollegeName = getIntent().getStringExtra("mCollegeName");

        if (!isConnected(LineFollowerEvaluation.this)) {
            showCustomDialog();
        }


        Objects.requireNonNull(et_teamName.getEditText()).setText("" + mTeamName);
        Objects.requireNonNull(et_collegeName.getEditText()).setText("" + mCollegeName);
        et_teamName.setEnabled(false);
        et_collegeName.setEnabled(false);

        progressDialog = new ProgressDialog(LineFollowerEvaluation.this);
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.dismiss();

        btn_backToSd.setOnClickListener(v -> {
            startActivity(new Intent(LineFollowerEvaluation.this, EvaluatorDashboard.class));
            finishAffinity();
        });


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
                Toast.makeText(LineFollowerEvaluation.this, "Bing! Time Over!", Toast.LENGTH_SHORT).show();
                chronometer.stop();
                running = false;
                start.setVisibility(View.VISIBLE);
                stop.setVisibility(View.VISIBLE);
                reset.setVisibility(View.VISIBLE);
                startTimer();
            } else {
                Toast.makeText(LineFollowerEvaluation.this, "No More Technical Timeout!", Toast.LENGTH_SHORT).show();
            }
        });

        chronometer.setOnChronometerTickListener(chronometer -> {
            if ((SystemClock.elapsedRealtime() - chronometer.getBase()) >= 10000) {
                chronometer.setBase(SystemClock.elapsedRealtime());
                Toast.makeText(LineFollowerEvaluation.this, "Bing! Time Over!", Toast.LENGTH_SHORT).show();
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
            long mTotalTime, mCheckPoints, mHandTouches,mTotal = 0;
            mTeamName = et_teamName.getEditText().getText().toString();


            String[] timeArray = mChronoTimer.getText().toString().split(":");
            int minutes = Integer.parseInt(timeArray[0]);
            int seconds = Integer.parseInt(timeArray[1]);
            mTotalTime = (minutes * 60L) + seconds;

            mCheckPoints = Long.parseLong(disp1.getText().toString());
            mHandTouches = Long.parseLong(disp2.getText().toString());
            mSuggestion = Objects.requireNonNull(et_suggestion.getEditText()).getText().toString();
//            mTotal = 10*mCheckPoints;
            if(mHandTouches>3){
                mTotal = mTotal - 5*(mHandTouches-3);
            }
            DatabaseReference mSugDB = FirebaseDatabase.getInstance().getReference("Suggestions_Team").child("Line Follower").child(mTeamName).child("Suggestions");
            String id = mSugDB.push().getKey();
            if (mSuggestion.length() > 0 & id != null) {
                Suggestion suggestion = new Suggestion(mTeamName, mCollegeName, mSuggestion, id, true, false, 0L);
                mSugDB.child(id).setValue(suggestion);
            }
            mDocumentReference = FirebaseFirestore.getInstance().collection("Line Follower").document(mTeamName);
            mDocumentReference.update("mCheckPointCleared", mCheckPoints, "mHandTouches", mHandTouches, "mTimeOutTaken", mTechTimeTaken, "mTotalTimeTaken", mTotalTime,"mTotal",mTotal).addOnCompleteListener(task -> {
                progressDialog.dismiss();
                startActivity(new Intent(getApplicationContext(), EvaluatorDashboard.class));
                finish();
            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(LineFollowerEvaluation.this, "Completed!!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(LineFollowerEvaluation.this, "Failed!!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
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

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(LineFollowerEvaluation.this);
        builder.setMessage("Please connect to the internet")
                //.setCancelable(false)
                .setPositiveButton("Connect", (dialog, which) -> startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS))).setNegativeButton("Cancel", (dialog, which) -> {
                    startActivity(new Intent(getApplicationContext(), LineFollowerEvaluation.class));
                    finish();
                });
        android.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    //--------------- Check Internet Is Connected -----------
    private boolean isConnected(LineFollowerEvaluation userLogin) {

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
