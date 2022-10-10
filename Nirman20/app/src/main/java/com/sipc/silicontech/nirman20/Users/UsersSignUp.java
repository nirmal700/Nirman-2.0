package com.sipc.silicontech.nirman20.Users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sipc.silicontech.nirman20.Admins.AddNewTeams;
import com.sipc.silicontech.nirman20.Admins.AdminDashboard;
import com.sipc.silicontech.nirman20.Admins.AdminSignin;
import com.sipc.silicontech.nirman20.Admins.NewTeamData;
import com.sipc.silicontech.nirman20.R;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class UsersSignUp extends AppCompatActivity {
    ImageView bckBtn;
    Button next, login;
    TextView title;
    AutoCompleteTextView mEventType,mTeam,mParticipants;
    NewTeamData teamData;
    ArrayList<String> arrayListPartcipantTeamName;
    ArrayAdapter<String> arrayAdapterPartcipantTeamName;
    ArrayList<String> arrayListPartcipantNames;
    ArrayAdapter<String> arrayAdapterPartcipantNames;
    ProgressDialog progressDialog;
    TextInputLayout et_password;
    String phone,password,teamname,event,participantName;
    private FirebaseAuth auth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.users_sign_up);
        bckBtn = findViewById(R.id.btn_backToCd);
        next = findViewById(R.id.btn_next);
        login = findViewById(R.id.btn_login);
        title = findViewById(R.id.title);
        mEventType = findViewById(R.id.autoCompleteEvent);
        mTeam = findViewById(R.id.autoCompleteTeam);
        mParticipants = findViewById(R.id.autoCompleteUserName);
        et_password = findViewById(R.id.et_password);

        auth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(UsersSignUp.this);
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.dismiss();

        ArrayList<String> arrayListEventType;
        ArrayAdapter<String> arrayAdapterEventType;
        arrayListEventType = new ArrayList<>();
        arrayListEventType.add("Robo Race");
        arrayListEventType.add("Line Follower");
        arrayListEventType.add("Ideate");
        arrayListEventType.add("HackNation");
        arrayAdapterEventType = new ArrayAdapter<>(getApplicationContext(), R.layout.text_menu, arrayListEventType);
        mEventType.setAdapter(arrayAdapterEventType);

        mEventType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mTeam.setText("Select Team Name");
                mParticipants.setText("Select Your Name");
                progressDialog.show();
                CollectionReference mCollectionReference = FirebaseFirestore.getInstance().collection(arrayAdapterEventType.getItem(i).toString());
                event = arrayAdapterEventType.getItem(i).toString();
                mCollectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        progressDialog.cancel();
                        if(task.isSuccessful())
                        {
                            arrayListPartcipantTeamName = new ArrayList<>();
                            arrayAdapterPartcipantTeamName = new ArrayAdapter<>(getApplicationContext(), R.layout.text_menu, arrayListPartcipantTeamName);
                            for(QueryDocumentSnapshot document : task.getResult())
                            {
                                teamData = document.toObject(NewTeamData.class);
                                Log.e("TAG", "onComplete: "+document.getId() +"=>"+document.getData() );
                                arrayListPartcipantTeamName.add(teamData.getmTeamName());
                                arrayAdapterPartcipantTeamName = new ArrayAdapter<>(getApplicationContext(), R.layout.text_menu, arrayListPartcipantTeamName);
                                mTeam.setAdapter(arrayAdapterPartcipantTeamName);
                            }
                            mTeam.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    mParticipants.setText("Select Your Name");
                                    arrayListPartcipantNames = new ArrayList<>();
                                    arrayAdapterPartcipantNames = new ArrayAdapter<>(getApplicationContext(),R.layout.text_menu,arrayListPartcipantNames);
                                    for(QueryDocumentSnapshot documentSnapshot : task.getResult())
                                    {
                                        teamData = documentSnapshot.toObject(NewTeamData.class);
                                        if(teamData.getmTeamName() == arrayAdapterPartcipantTeamName.getItem(i))
                                        {
                                            teamname = arrayAdapterPartcipantTeamName.getItem(i).toString();
                                            arrayListPartcipantNames.add(teamData.getmTeamLead());
                                            arrayListPartcipantNames.add(teamData.getmMem1Name());
                                            arrayListPartcipantNames.add(teamData.getmMem2Name());
                                            if(!(teamData.getmMem3Name().length() <1))
                                            {
                                                arrayListPartcipantNames.add(teamData.getmMem3Name());
                                            }
                                            arrayAdapterPartcipantNames = new ArrayAdapter<>(getApplicationContext(),R.layout.text_menu,arrayListPartcipantNames);
                                            mParticipants.setAdapter(arrayAdapterPartcipantNames);
                                            Log.e("TAG", "onItemClick: "+arrayListPartcipantNames.toString() );
                                        }
                                    } 
                                }
                            });
                            mParticipants.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    for(QueryDocumentSnapshot documentSnapshot :task.getResult())
                                    {
                                        teamData = documentSnapshot.toObject(NewTeamData.class);
                                        participantName = arrayAdapterPartcipantNames.getItem(i).toString();
                                        if(teamData.getmTeamLead() == arrayAdapterPartcipantNames.getItem(i).toString())
                                        {
                                            phone = "+91"+teamData.getmTeamLeadPhone().toString();
                                            Log.e("TAG phone1", "onItemClick: "+phone );
                                        }
                                        else if(teamData.getmMem1Name() == arrayAdapterPartcipantNames.getItem(i).toString()){
                                            phone = "+91"+teamData.getmMem1Phone().toString();
                                            Log.e("TAG phone2", "onItemClick: "+phone );
                                        }
                                        else if(teamData.getmMem2Name() == arrayAdapterPartcipantNames.getItem(i).toString()){
                                            phone = "+91"+teamData.getmMem2Phone().toString();
                                            Log.e("TAG phone3", "onItemClick: "+phone );
                                        }
                                        else if(teamData.getmMem3Name() == arrayAdapterPartcipantNames.getItem(i).toString())
                                        {
                                            phone = "+91"+teamData.getmMem3Phone().toString();
                                            Log.e("TAG phone4", "onItemClick: "+phone );
                                        }
                                    }
                                }
                            });
                            next.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (!validatePassword() | (teamname.length() <1) | (event.length() < 3) | (participantName.length() < 3) | (phone.length() < 9)) {
                                        Toast.makeText(UsersSignUp.this, "Invalid Inputs", Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                    progressDialog = new ProgressDialog(UsersSignUp.this);
                                    progressDialog.show();
                                    progressDialog.setContentView(R.layout.progress_dialog);
                                    progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                                    progressDialog.setCancelable(false);

                                    String phoneNo = phone.substring(3);
                                    if (!phoneNo.isEmpty()) {
                                        if (phoneNo.length() == 10) {

                                            Query checkUser = FirebaseDatabase.getInstance().getReference("Participants").child(event).orderByChild("mPhoneNo").equalTo(phone);

                                            checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                    if (snapshot.exists()) {
                                                        progressDialog.dismiss();
                                                        Toast.makeText(UsersSignUp.this, "This User already Exist  Please Login", Toast.LENGTH_LONG).show();

                                                    } else {

                                                        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth)
                                                                .setPhoneNumber(phone)
                                                                .setTimeout(60L, TimeUnit.SECONDS)
                                                                .setActivity(UsersSignUp.this)
                                                                .setCallbacks(mCallBacks)
                                                                .build();
                                                        PhoneAuthProvider.verifyPhoneNumber(options);

                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });

                                        } else {
                                            progressDialog.dismiss();
                                            Toast.makeText(UsersSignUp.this, "Please Enter Correct Mobile Number", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(UsersSignUp.this, "Please Enter Mobile Number", Toast.LENGTH_SHORT).show();
                                    }


                                    Log.e("TAG345", "onComplete: "+teamname+event+participantName+phone );
                                }
                            });
                            mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                @Override
                                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                                }

                                @Override
                                public void onVerificationFailed(@NonNull FirebaseException e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(UsersSignUp.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                    super.onCodeSent(s, forceResendingToken);

                                    //sometime the code is not detected automatically
                                    //so user has to manually enter the code

                                    new Handler().postDelayed(() -> {

                                        Intent otpIntent = new Intent(UsersSignUp.this, UserPhoneVerification.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        otpIntent.putExtra("auth", s);
                                        otpIntent.putExtra("phoneNumber", phone);
                                        String password = Objects.requireNonNull(et_password.getEditText()).getText().toString();
                                        otpIntent.putExtra("event", event);
                                        otpIntent.putExtra("mTeamName", teamname);
                                        otpIntent.putExtra("mParticipant", participantName);
                                        otpIntent.putExtra("password", password);
                                        startActivity(otpIntent);
                                        finish();
                                    }, 1);

                                }
                            };



                        }
                        else
                        {
                            Log.e("TAG", "onComplete: Error Getting Documents"+task.getException() );
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.cancel();
                        Toast.makeText(UsersSignUp.this, "Error! "+e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AdminSignin.class));
            }
        });

    }


    public void callNextSignpScreen(View view) {
        Intent intent = new Intent(getApplicationContext(), UsersSignup2.class);
        Pair[] pairs = new Pair[4];
        pairs[0] = new Pair<View, String>(bckBtn, "transition_back_btn");
        pairs[1] = new Pair<View, String>(next, "transition_next_btn");
        pairs[2] = new Pair<View, String>(login, "transition_login_btn");
        pairs[3] = new Pair<View, String>(title, "transition_title_text");

        ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(UsersSignUp.this, pairs);
        startActivity(intent, activityOptions.toBundle());
    }
    private boolean validatePassword() {
        String val = Objects.requireNonNull(et_password.getEditText()).getText().toString().trim();

        if (val.isEmpty()) {
            et_password.setError("Field can not be empty");
            return false;
        } else if (val.length() < 8) {
            et_password.setError("Password minimum 8 Characters");
            return false;
        } else if (!val.matches("\\w*")) {
            et_password.setError("White spaces not allowed");
            return false;
        } else {
            et_password.setError(null);
            return true;
        }

    }
}