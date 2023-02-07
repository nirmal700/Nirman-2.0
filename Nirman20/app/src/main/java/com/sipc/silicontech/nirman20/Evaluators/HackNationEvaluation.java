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

public class HackNationEvaluation extends AppCompatActivity {
    String mTeamName, mCollegeName, mProblemStat, mSugApp;
    TextInputLayout et_teamName, et_collegeName, et_Problem_Statement, et_description, et_suggestion;
    ProgressDialog progressDialog;
    CollectionReference mCollectionReference;
    Button next;
    int ev1, ev2, ev3, ev4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.evaluator_hacknation_evaluation);
        final TextView textView = findViewById(R.id.textView);
        et_teamName = findViewById(R.id.et_teamName);
        et_collegeName = findViewById(R.id.et_collegeName);
        et_Problem_Statement = findViewById(R.id.et_Problem_Statement);
        et_description = findViewById(R.id.et_description);
        et_suggestion = findViewById(R.id.et_suggestion);
        next = findViewById(R.id.btn_submit);

        progressDialog = new ProgressDialog(HackNationEvaluation.this);
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.dismiss();

        if (!isConnected(HackNationEvaluation.this)) {
            showCustomDialog();
        }

        final int max1 = 100;
        final int min1 = 0;
        final int total1 = max1 - min1;

        final int max2 = 100;
        final int min2 = 0;
        final int total2 = max2 - min2;

        final int max3 = 100;
        final int min3 = 0;
        final int total3 = max3 - min3;

        final int max4 = 100;
        final int min4 = 0;
        final int total4 = max4 - min4;


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
            slider1.setBubbleText(value);
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
            slider1.setBubbleText(value);
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
            slider1.setBubbleText(value);
            ev4 = Integer.parseInt(value);
            return Unit.INSTANCE;
        });

        slider4.setPosition(0.5f);
        slider4.setStartText(String.valueOf(min4));
        slider4.setEndText(String.valueOf(max4));


        next.setOnClickListener(view -> {
            progressDialog.show();
            String sugg = Objects.requireNonNull(et_suggestion.getEditText()).getText().toString();
            final double average = (double) (ev1 + ev2 + ev3 + ev4) / 4;
            DatabaseReference mSugDB = FirebaseDatabase.getInstance().getReference("Suggestions_Team").child("Robo Race").child(mTeamName).child("Suggestions");
            String id = mSugDB.push().getKey();
            if (sugg.length() > 0 & id != null) {
                Suggestion suggestion = new Suggestion(mTeamName, mCollegeName, sugg, id, true, false, 0L);
                mSugDB.child(id).setValue(suggestion);
            }
            mCollectionReference = FirebaseFirestore.getInstance().collection("HackNation Evaluation");
            HackNationEvaluation_POJO hackNationEvaluationPOJO = new HackNationEvaluation_POJO(mTeamName, mCollegeName, mProblemStat, mSugApp, sugg, ev1, ev2, ev3, ev4, average, null);
            mCollectionReference.add(hackNationEvaluationPOJO).addOnSuccessListener(documentReference -> {

            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(HackNationEvaluation.this, "Makring Done!!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(HackNationEvaluation.this, EvaluatorDashboard.class);
                    progressDialog.dismiss();
                    startActivity(intent);
                    finish();
                }
            }).addOnFailureListener(e -> {
                progressDialog.dismiss();
                Toast.makeText(HackNationEvaluation.this, "Error Occured!!" + e, Toast.LENGTH_SHORT).show();
            });
        });

    }

    private void showCustomDialog() {

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(HackNationEvaluation.this);
        builder.setMessage("Please connect to the internet")
                //.setCancelable(false)
                .setPositiveButton("Connect", (dialog, which) -> startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS))).setNegativeButton("Cancel", (dialog, which) -> {
                    startActivity(new Intent(getApplicationContext(), HackNationEvaluation.class));
                    finish();
                });
        android.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    //--------------- Check Internet Is Connected -----------
    private boolean isConnected(HackNationEvaluation userLogin) {

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