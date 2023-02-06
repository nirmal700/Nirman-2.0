package com.sipc.silicontech.nirman20.Users;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ramotion.fluidslider.FluidSlider;
import com.sipc.silicontech.nirman20.R;

import java.util.Objects;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;

public class ReviewTeam extends AppCompatActivity {
    String mTeamName;
    TextInputLayout et_teamName;
    ProgressDialog progressDialog;
    int ev1, ev2, ev3;
    Button next;
    TextInputLayout et_suggestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_team);
        et_teamName = findViewById(R.id.et_teamName);
        mTeamName = getIntent().getStringExtra("mTeamName");
        Objects.requireNonNull(et_teamName.getEditText()).setText(mTeamName);
        et_teamName.setEnabled(false);
        final FluidSlider slider1 = findViewById(R.id.fluidSlider1);
        final FluidSlider slider2 = findViewById(R.id.fluidSlider2);
        final FluidSlider slider3 = findViewById(R.id.fluidSlider3);
        final TextView textView1 = findViewById(R.id.textView1);
        final TextView textView2 = findViewById(R.id.textView2);
        final TextView textView3 = findViewById(R.id.textView3);
        et_suggestion = findViewById(R.id.et_suggestion);
        SessionManagerParticipant managerParticipant = new SessionManagerParticipant(ReviewTeam.this);

        next = findViewById(R.id.btn_submit);

        progressDialog = new ProgressDialog(ReviewTeam.this);
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.dismiss();

        final int max1 = 100;
        final int min1 = 0;
        final int total1 = max1 - min1;

        final int max2 = 100;
        final int min2 = 0;
        final int total2 = max2 - min2;

        final int max3 = 100;
        final int min3 = 0;
        final int total3 = max3 - min3;

        slider1.setBeginTrackingListener(new Function0<Unit>() {
            @Override
            public Unit invoke() {
                textView1.setVisibility(View.INVISIBLE);
                return Unit.INSTANCE;
            }
        });

        slider1.setEndTrackingListener(new Function0<Unit>() {
            @Override
            public Unit invoke() {
                textView1.setVisibility(View.VISIBLE);
                return Unit.INSTANCE;
            }
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


        slider2.setBeginTrackingListener(new Function0<Unit>() {
            @Override
            public Unit invoke() {
                textView2.setVisibility(View.INVISIBLE);
                return Unit.INSTANCE;
            }
        });

        slider2.setEndTrackingListener(new Function0<Unit>() {
            @Override
            public Unit invoke() {
                textView2.setVisibility(View.VISIBLE);
                return Unit.INSTANCE;
            }
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

        slider3.setBeginTrackingListener(new Function0<Unit>() {
            @Override
            public Unit invoke() {
                textView3.setVisibility(View.INVISIBLE);
                return Unit.INSTANCE;
            }
        });

        slider3.setEndTrackingListener(new Function0<Unit>() {
            @Override
            public Unit invoke() {
                textView3.setVisibility(View.VISIBLE);
                return Unit.INSTANCE;
            }
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


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sugg = et_suggestion.getEditText().getText().toString();
                if (sugg.isEmpty()) {
                    Toast.makeText(ReviewTeam.this, "Fill the Suggestion!", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.show();
                    DatabaseReference mSugDB = FirebaseDatabase.getInstance().getReference("Suggestions_Team").child(managerParticipant.getEventName()).child(mTeamName).child("Suggestions");
                    String id = mSugDB.push().getKey();
                    final int average = (ev1 + ev2 + ev3) / 4;
                    if (id != null) {
                        Suggestion suggestion = new Suggestion(mTeamName, "", sugg, id, false, true, average);
                        mSugDB.child(id).setValue(suggestion).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                progressDialog.dismiss();
                                Toast.makeText(ReviewTeam.this, "Thanks!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), UserDashBoard.class));
                                finish();
                            }
                        });
                    }
                }
            }
        });

    }
}