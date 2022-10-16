package com.sipc.silicontech.nirman20.Evaluators;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ramotion.fluidslider.FluidSlider;
import com.sipc.silicontech.nirman20.R;
import com.sipc.silicontech.nirman20.Users.UsersSignUp;

import java.util.Objects;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;

public class EvaluatorReviewEvaluation extends AppCompatActivity {
    String mTeamName,mCollegeName,mProblemStat,mSugApp;
    TextInputLayout et_teamName,et_collegeName,et_Problem_Statement,et_description,et_suggestion;
    ProgressDialog progressDialog;
    CollectionReference mCollectionReference;
    Button next;
    int ev1,ev2,ev3,ev4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.evaluator_review_evaluation);
        final TextView textView = findViewById(R.id.textView);
        et_teamName = findViewById(R.id.et_teamName);
        et_collegeName = findViewById(R.id.et_collegeName);
        et_Problem_Statement = findViewById(R.id.et_Problem_Statement);
        et_description = findViewById(R.id.et_description);
        et_suggestion = findViewById(R.id.et_suggestion);
        next = findViewById(R.id.btn_submit);

        progressDialog = new ProgressDialog(EvaluatorReviewEvaluation.this);
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
        slider1.setBeginTrackingListener(new Function0<Unit>() {
            @Override
            public Unit invoke() {
                textView.setVisibility(View.INVISIBLE);
                return Unit.INSTANCE;
            }
        });

        slider1.setEndTrackingListener(new Function0<Unit>() {
            @Override
            public Unit invoke() {
                textView.setVisibility(View.VISIBLE);
                return Unit.INSTANCE;
            }
        });

        // Java 8 lambda
        slider1.setPositionListener(pos -> {
            final String value = String.valueOf( (int)(min1 + total1 * pos) );
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
                textView.setVisibility(View.INVISIBLE);
                return Unit.INSTANCE;
            }
        });

        slider2.setEndTrackingListener(new Function0<Unit>() {
            @Override
            public Unit invoke() {
                textView.setVisibility(View.VISIBLE);
                return Unit.INSTANCE;
            }
        });

        // Java 8 lambda
        slider2.setPositionListener(pos -> {
            final String value = String.valueOf( (int)(min2 + total2 * pos) );
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
                textView.setVisibility(View.INVISIBLE);
                return Unit.INSTANCE;
            }
        });

        slider3.setEndTrackingListener(new Function0<Unit>() {
            @Override
            public Unit invoke() {
                textView.setVisibility(View.VISIBLE);
                return Unit.INSTANCE;
            }
        });

        // Java 8 lambda
        slider3.setPositionListener(pos -> {
            final String value = String.valueOf( (int)(min3 + total3 * pos) );
            slider1.setBubbleText(value);
            ev3 = Integer.parseInt(value);
            return Unit.INSTANCE;
        });

        slider3.setPosition(0.5f);
        slider3.setStartText(String.valueOf(min3));
        slider3.setEndText(String.valueOf(max3));

        slider4.setBeginTrackingListener(new Function0<Unit>() {
            @Override
            public Unit invoke() {
                textView.setVisibility(View.INVISIBLE);
                return Unit.INSTANCE;
            }
        });

        slider4.setEndTrackingListener(new Function0<Unit>() {
            @Override
            public Unit invoke() {
                textView.setVisibility(View.VISIBLE);
                return Unit.INSTANCE;
            }
        });

        // Java 8 lambda
        slider4.setPositionListener(pos -> {
            final String value = String.valueOf( (int)(min4 + total4 * pos) );
            slider1.setBubbleText(value);
            ev4 = Integer.parseInt(value);
            return Unit.INSTANCE;
        });

        slider4.setPosition(0.5f);
        slider4.setStartText(String.valueOf(min4));
        slider4.setEndText(String.valueOf(max4));


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                String sugg = et_suggestion.getEditText().getText().toString();
                final double average = (double) (ev1+ev2+ev3+ev4)/4;
                mCollectionReference = FirebaseFirestore.getInstance().collection("HackNation Evaluation");
                HackNationEvaluation hackNationEvaluation = new HackNationEvaluation(mTeamName,mCollegeName,mProblemStat,mSugApp,sugg,ev1,ev2,ev3,ev4,average,null);
                mCollectionReference.add(hackNationEvaluation).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                    }
                }).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(EvaluatorReviewEvaluation.this, "Makring Done!!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(EvaluatorReviewEvaluation.this, EvaluatorDashboard.class);
                            progressDialog.dismiss();
                            startActivity(intent);
                            finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(EvaluatorReviewEvaluation.this, "Error Occured!!"+e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}