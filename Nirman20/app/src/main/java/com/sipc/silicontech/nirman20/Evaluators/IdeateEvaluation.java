package com.sipc.silicontech.nirman20.Evaluators;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ramotion.fluidslider.FluidSlider;
import com.sipc.silicontech.nirman20.R;
import com.sipc.silicontech.nirman20.Users.Suggestion;
import com.sipc.silicontech.nirman20.Users.UserDashBoard;

import java.util.Objects;

import kotlin.Unit;

public class IdeateEvaluation extends AppCompatActivity {
    String mTeamName, mCollegeName, mProblemStat, mSugApp;
    TextInputLayout et_teamName, et_collegeName, et_Problem_Statement, et_description, et_suggestion;
    ProgressDialog progressDialog;
    CollectionReference mCollectionReference;
    SessionManagerEvaluator managerEvaluator;
    Button submit;
    int ev1, ev2, ev3, ev4, ev5;
    ImageView btn_backToSd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ideate_evaluation);

        final TextView textView = findViewById(R.id.textView);
        et_teamName = findViewById(R.id.et_teamName);
        et_collegeName = findViewById(R.id.et_collegeName);
        et_Problem_Statement = findViewById(R.id.et_Problem_Statement);
        et_description = findViewById(R.id.et_description);
        et_suggestion = findViewById(R.id.et_suggestion);
        btn_backToSd = findViewById(R.id.btn_backToSd);
        submit = findViewById(R.id.btn_submit);

        progressDialog = new ProgressDialog(IdeateEvaluation.this);
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.dismiss();

        if (!isConnected(IdeateEvaluation.this)) {
            showCustomDialog();
        }

        btn_backToSd.setOnClickListener(v -> {
            startActivity(new Intent(IdeateEvaluation.this, EvaluatorDashboard.class));
            finishAffinity();
        });

        managerEvaluator = new SessionManagerEvaluator(IdeateEvaluation.this);

        final int max1 = 10;
        final int min1 = 0;
        final int total1 = max1 - min1;

        final int max2 = 10;
        final int min2 = 0;
        final int total2 = max2 - min2;

        final int max3 = 30;
        final int min3 = 0;
        final int total3 = max3 - min3;

        final int max4 = 10;
        final int min4 = 0;
        final int total4 = max4 - min4;

        final int max5 = 10;
        final int min5 = 0;
        final int total5 = max5 - min5;

        mTeamName = getIntent().getStringExtra("mTeamName");
        mCollegeName = getIntent().getStringExtra("mCollegeName");
        mProblemStat = getIntent().getStringExtra("mProblemStat");
        mSugApp = getIntent().getStringExtra("mApproach");

        Objects.requireNonNull(et_teamName.getEditText()).setText(mTeamName);
        et_teamName.setEnabled(false);
        Objects.requireNonNull(et_collegeName.getEditText()).setText(mCollegeName);
        et_collegeName.setEnabled(false);
        Objects.requireNonNull(et_Problem_Statement.getEditText()).setText(mProblemStat);
        et_Problem_Statement.setEnabled(false);
        Objects.requireNonNull(et_description.getEditText()).setText(mSugApp);
        et_description.setEnabled(false);

        final FluidSlider slider1 = findViewById(R.id.fluidSlider1);
        final FluidSlider slider2 = findViewById(R.id.fluidSlider2);
        final FluidSlider slider3 = findViewById(R.id.fluidSlider3);
        final FluidSlider slider4 = findViewById(R.id.fluidSlider4);
        final FluidSlider slider5 = findViewById(R.id.fluidSlider5);

        slider1.setBeginTrackingListener(() -> {
            textView.setVisibility(View.INVISIBLE);
            return Unit.INSTANCE;
        });

        slider1.setEndTrackingListener(() -> {
            textView.setVisibility(View.VISIBLE);
            return Unit.INSTANCE;
        });

        // Java 8 lambda
        slider1.setPositionListener(pos -> {
            final String value = String.valueOf((int) (min1 + total1 * pos));
            slider1.setBubbleText(value);
            ev1 = Integer.parseInt(value);
            return Unit.INSTANCE;
        });

        slider1.setPosition(0.5f);
        slider1.setStartText(String.valueOf(min1));
        slider1.setEndText(String.valueOf(max1));


        slider2.setBeginTrackingListener(() -> {
            textView.setVisibility(View.INVISIBLE);
            return Unit.INSTANCE;
        });

        slider2.setEndTrackingListener(() -> {
            textView.setVisibility(View.VISIBLE);
            return Unit.INSTANCE;
        });

        // Java 8 lambda
        slider2.setPositionListener(pos -> {
            final String value = String.valueOf((int) (min2 + total2 * pos));
            slider2.setBubbleText(value);
            ev2 = Integer.parseInt(value);
            return Unit.INSTANCE;
        });

        slider2.setPosition(0.5f);
        slider2.setStartText(String.valueOf(min2));
        slider2.setEndText(String.valueOf(max2));

        slider3.setBeginTrackingListener(() -> {
            textView.setVisibility(View.INVISIBLE);
            return Unit.INSTANCE;
        });

        slider3.setEndTrackingListener(() -> {
            textView.setVisibility(View.VISIBLE);
            return Unit.INSTANCE;
        });

        // Java 8 lambda
        slider3.setPositionListener(pos -> {
            final String value = String.valueOf((int) (min3 + total3 * pos));
            slider3.setBubbleText(value);
            ev3 = Integer.parseInt(value);
            return Unit.INSTANCE;
        });

        slider3.setPosition(0.5f);
        slider3.setStartText(String.valueOf(min3));
        slider3.setEndText(String.valueOf(max3));

        slider4.setBeginTrackingListener(() -> {
            textView.setVisibility(View.INVISIBLE);
            return Unit.INSTANCE;
        });

        slider4.setEndTrackingListener(() -> {
            textView.setVisibility(View.VISIBLE);
            return Unit.INSTANCE;
        });

        // Java 8 lambda
        slider4.setPositionListener(pos -> {
            final String value = String.valueOf((int) (min4 + total4 * pos));
            slider4.setBubbleText(value);
            ev4 = Integer.parseInt(value);
            return Unit.INSTANCE;
        });

        slider4.setPosition(0.5f);
        slider4.setStartText(String.valueOf(min4));
        slider4.setEndText(String.valueOf(max4));


        slider5.setBeginTrackingListener(() -> {
            textView.setVisibility(View.INVISIBLE);
            return Unit.INSTANCE;
        });

        slider5.setEndTrackingListener(() -> {
            textView.setVisibility(View.VISIBLE);
            return Unit.INSTANCE;
        });

        // Java 8 lambda
        slider5.setPositionListener(pos -> {
            final String value = String.valueOf((int) (min5 + total5 * pos));
            slider5.setBubbleText(value);
            ev5 = Integer.parseInt(value);
            return Unit.INSTANCE;
        });

        slider5.setPosition(0.5f);
        slider5.setStartText(String.valueOf(min5));
        slider5.setEndText(String.valueOf(max5));

        submit.setOnClickListener(view -> {
            progressDialog.show();
            String sugg = Objects.requireNonNull(et_suggestion.getEditText()).getText().toString();
            final double average = (double) (ev1 + ev2 + ev3 + ev4 + ev5) / 60;
            DatabaseReference mSugDB = FirebaseDatabase.getInstance().getReference("Suggestions_Team").child("Ideate").child(mTeamName).child("Suggestions");
            String id = mSugDB.push().getKey();
            if (sugg.length() > 0 & id != null) {
                Suggestion suggestion = new Suggestion(mTeamName, mCollegeName, sugg, id, true, false, 0L);
                mSugDB.child(id).setValue(suggestion);
            }
            mCollectionReference = FirebaseFirestore.getInstance().collection("Ideate Evaluation");
            IdeateEvaluation_POJO ideateEvaluation_pojo = new IdeateEvaluation_POJO(mTeamName, mCollegeName, mProblemStat, mSugApp, sugg, managerEvaluator.getEvaluatorName(), ev1, ev2, ev3, ev4, ev5, average, null);
            mCollectionReference.add(ideateEvaluation_pojo).addOnSuccessListener(documentReference -> Toast.makeText(IdeateEvaluation.this, "Makring Done Successfully!!", Toast.LENGTH_SHORT).show()).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {

                    Intent intent = new Intent(IdeateEvaluation.this, EvaluatorDashboard.class);
                    progressDialog.dismiss();
                    startActivity(intent);
                    finish();
                }
            }).addOnFailureListener(e -> {
                progressDialog.dismiss();
                Toast.makeText(IdeateEvaluation.this, "Error Occured!!" + e, Toast.LENGTH_SHORT).show();
            });
        });
    }
    private void showCustomDialog() {

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(IdeateEvaluation.this);
        builder.setMessage("Please connect to the internet")
                //.setCancelable(false)
                .setPositiveButton("Connect", (dialog, which) -> startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS))).setNegativeButton("Cancel", (dialog, which) -> {
                    startActivity(new Intent(getApplicationContext(), IdeateEvaluation.class));
                    finish();
                });
        android.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    //--------------- Check Internet Is Connected -----------
    private boolean isConnected(IdeateEvaluation userLogin) {

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