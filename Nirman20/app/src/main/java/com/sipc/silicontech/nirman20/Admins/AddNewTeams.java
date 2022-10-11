package com.sipc.silicontech.nirman20.Admins;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sipc.silicontech.nirman20.R;

import java.util.ArrayList;
import java.util.Objects;

public class AddNewTeams extends AppCompatActivity {
    TextInputLayout mTeamName, mProblemStatement, mApproach, mCollegeName, mTeamLeadName, mTeamLeadPhone, mMember1Phone, mMember1Name, mMember2Name, mMember2Phone, mMember3Name, mMember3Phone,mMember4Name,mMember4Phone;
    private RadioButton t3,t4,t5;
    private Button btnSubmit;
    int b = -1, flag = 0;
    ImageView btn_back;
    RadioGroup radio_group;
    AutoCompleteTextView mEventType;
    String event,teamname, problemstatement,approach,clgname,teamleadname,teamleadph,mem1name,mem1phone,mem2name,mem2phone,mem3name,mem3phone;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_teams);

        mTeamName = findViewById(R.id.et_TeamName);
        mProblemStatement = findViewById(R.id.et_Problem_Statement);
        mApproach = findViewById(R.id.et_Problem_Approach);
        mCollegeName = findViewById(R.id.et_CollegeName);
        mTeamLeadName = findViewById(R.id.et_TeamLeadName);
        mTeamLeadPhone = findViewById(R.id.et_TeamLeadphoneNumber);
        mMember1Name = findViewById(R.id.et_Member1Name);
        mMember1Phone = findViewById(R.id.et_Member1phoneNumber);
        mMember2Name = findViewById(R.id.et_Member2Name);
        mMember2Phone = findViewById(R.id.et_Member2phoneNumber);
        mMember3Name = findViewById(R.id.et_Member3Name);
        mMember3Phone = findViewById(R.id.et_Member3phoneNumber);
        btn_back = findViewById(R.id.btn_backToSd);
        mEventType = findViewById(R.id.autoCompleteEventType);
        t3 = findViewById(R.id.single);
        t4 = findViewById(R.id.duo);
        btnSubmit = findViewById(R.id.btn_AddTeam);
        radio_group = findViewById(R.id.radio_group);

        progressDialog = new ProgressDialog(AddNewTeams.this);
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.dismiss();

        mMember3Phone.setVisibility(View.GONE);
        mMember3Name.setVisibility(View.GONE);

        ArrayList<String> arrayListEventType;
        ArrayAdapter<String> arrayAdapterEventType;
        arrayListEventType = new ArrayList<>();
        arrayListEventType.add("Robo Race");
        arrayListEventType.add("Line Follower");
        arrayListEventType.add("Ideate");
        arrayListEventType.add("HackNation");
        arrayAdapterEventType = new ArrayAdapter<>(getApplicationContext(), R.layout.text_menu, arrayListEventType);
        mEventType.setAdapter(arrayAdapterEventType);

        //--------------- Internet Checking -----------
        if (!isConnected(AddNewTeams.this)){
            showCustomDialog();
        }


        radio_group.setOnCheckedChangeListener((radioGroup, i) -> {
            if (i == t4.getId()) {
                mMember3Name.setVisibility(View.VISIBLE);
                mMember3Phone.setVisibility(View.VISIBLE);
                b = 4;
            }
            else{
                mMember3Name.setVisibility(View.GONE);
                mMember3Phone.setVisibility(View.GONE);
                b=3;
            }
            Log.e("No Of Persons", "onCheckedChanged: " + b);
        });
        mEventType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                event = arrayAdapterEventType.getItem(i).toString();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                teamname = Objects.requireNonNull(mTeamName.getEditText()).getText().toString();
                problemstatement = Objects.requireNonNull(mProblemStatement.getEditText()).getText().toString();
                approach = Objects.requireNonNull(mApproach.getEditText()).getText().toString();
                clgname = Objects.requireNonNull(mCollegeName.getEditText()).getText().toString();
                teamleadname = Objects.requireNonNull(mTeamLeadName.getEditText()).getText().toString();
                teamleadph = Objects.requireNonNull(mTeamLeadPhone.getEditText()).getText().toString();
                mem1name = Objects.requireNonNull(mMember1Name.getEditText()).getText().toString();
                mem1phone = Objects.requireNonNull(mMember1Phone.getEditText()).getText().toString();
                mem2name = Objects.requireNonNull(mMember2Name.getEditText()).getText().toString();
                mem2phone = Objects.requireNonNull(mMember2Phone.getEditText()).getText().toString();
                mem3name = mMember3Name.getEditText().getText().toString();
                mem3phone = mMember3Phone.getEditText().getText().toString();
                if(b==3)
                {
                    if(!ValidTeamName() | !ValidProblemStat()| !ValidApproach()| !ValidCollegeName()| !ValidName1()| !ValidPhone1()| !ValidName2()| !ValidPhone2() | !ValidName3()| !ValidPhone3() |!ValidateEventType())
                    {
                        Toast.makeText(AddNewTeams.this, "Not Entered Into Else" ,Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else {
                        progressDialog.show();
                        CollectionReference mCollectionReference = FirebaseFirestore.getInstance().collection(event);
                        NewTeamData newTeamData = new NewTeamData(event, teamname, problemstatement,approach,clgname,teamleadname,teamleadph,mem1name,mem1phone,mem2name,mem2phone,mem3name,mem3phone,null,null,null,null,null,false);
                        mCollectionReference.document(teamname).set(newTeamData).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                progressDialog.cancel();
                                Log.e("TAG", "onSuccess: "+"Successfully Updated new team details" );
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.cancel();
                                Toast.makeText(AddNewTeams.this, "Failed! Try Again"+e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(AddNewTeams.this, teamname+"Added Successfully!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(AddNewTeams.this, AdminDashboard.class);
                                    progressDialog.dismiss();
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
                    }
                }
                if(b==4)
                {
                    if(!ValidTeamName() | !ValidProblemStat()| !ValidApproach()| !ValidCollegeName()| !ValidName1()| !ValidPhone1()| !ValidName2()| !ValidPhone2() | !ValidName3()| !ValidPhone3()| !ValidName4()| !ValidPhone4() |!ValidateEventType())
                    {
                        return;
                    }
                    else {
                        progressDialog.show();
                        CollectionReference mCollectionReference = FirebaseFirestore.getInstance().collection(event);
                        NewTeamData newTeamData = new NewTeamData(event, teamname, problemstatement,approach,clgname,teamleadname,teamleadph,mem1name,mem1phone,mem2name,mem2phone,mem3name,mem3phone,null,null,null,null,null,false);
                        mCollectionReference.document(teamname).set(newTeamData).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                progressDialog.cancel();
                                Log.e("TAG", "onSuccess: "+"Successfully Updated new team details" );
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.cancel();
                                Toast.makeText(AddNewTeams.this, "Failed! Try Again"+e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(AddNewTeams.this, teamname+"Added Successfully!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(AddNewTeams.this, AdminDashboard.class);
                                    progressDialog.dismiss();
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
                    }
                }
                else {
                    t3.setError("Select Any One To Proceed");
                }
            }
        });
        btn_back.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), AdminDashboard.class));
            finishAffinity();
        });



    }

    private boolean ValidateEventType() {
        String val = mEventType.getText().toString().trim();

        if (val.isEmpty()) {
            mEventType.setError("Field can not be empty");
            return false;
        } else {
            mEventType.setError(null);
            return true;
        }
    }

    private boolean ValidPhone1() {
        if (teamleadph.isEmpty()) {
            mTeamLeadPhone.setError("Field can not be empty");
            return false;
        } else if (teamleadph.length() > 10 | teamleadph.length() < 10) {
            mTeamLeadPhone.setError("Please Enter 10 Digit Phone Number");
            return false;
        } else if (!teamleadph.matches("\\w*")) {
            mTeamLeadPhone.setError("White spaces not allowed");
            return false;
        } else {
            mTeamLeadPhone.setError(null);
            return true;
        }
    }

    private boolean ValidPhone2() {
        if (mem1phone.isEmpty()) {
            mMember1Phone.setError("Field can not be empty");
            return false;
        } else if (mem1phone.length() > 10 | mem1phone.length() < 10) {
            mMember1Phone.setError("Please Enter 10 Digit Phone Number");
            return false;
        } else if (!mem1phone.matches("\\w*")) {
            mMember1Phone.setError("White spaces not allowed");
            return false;
        } else {
            mMember1Phone.setError(null);
            return true;
        }
    }
    private boolean ValidPhone3() {
        if (mem2phone.isEmpty()) {
            mMember2Phone.setError("Field can not be empty");
            return false;
        } else if (mem2phone.length() > 10 | mem2phone.length() < 10) {
            mMember2Phone.setError("Please Enter 10 Digit Phone Number");
            return false;
        } else if (!mem2phone.matches("\\w*")) {
            mMember2Phone.setError("White spaces not allowed");
            return false;
        } else {
            mMember2Phone.setError(null);
            return true;
        }
    }

    private boolean ValidPhone4() {
        if (mem3phone.isEmpty()) {
            mMember3Phone.setError("Field can not be empty");
            return false;
        } else if (mem3phone.length() > 10 | mem3phone.length() < 10) {
            mMember3Phone.setError("Please Enter 10 Digit Phone Number");
            return false;
        } else if (!mem3phone.matches("\\w*")) {
            mMember3Phone.setError("White spaces not allowed");
            return false;
        } else {
            mMember3Phone.setError(null);
            return true;
        }
    }

    private boolean ValidName1() {
        if(mTeamLeadName.getEditText().getText().toString().length() <= 25)
        {
            mTeamLeadName.setError(null);
            return true;
        }
        else
        {
            mTeamLeadName.setError("The Name Should be between 25");
            return false;
        }
    }

    private boolean ValidName2() {
        if(mMember1Name.getEditText().getText().toString().length() <= 25)
        {
            mMember1Name.setError(null);
            return true;
        }
        else
        {
            mMember1Name.setError("The Name Should be between 25");
            return false;
        }
    }

    private boolean ValidName3() {
        if(mMember2Name.getEditText().getText().toString().length() <= 25)
        {
            mMember2Name.setError(null);
            return true;
        }
        else
        {
            mMember2Name.setError("The Name Should be between 25");
            return false;
        }
    }

    private boolean ValidName4() {
        if(mMember3Name.getEditText().getText().toString().length() <= 25)
        {
            mMember3Name.setError(null);
            return true;
        }
        else
        {
            mMember3Name.setError("The Name Should be between 25");
            return false;
        }
    }

    private boolean ValidCollegeName() {
        if( clgname.length() <=35 && clgname.length() >=10  )
        {
            mCollegeName.setError(null);
            return true;
        }
        else
        {
            mCollegeName.setError("The College Name Should be between 35");
            return false;
        }
    }

    private boolean ValidApproach() {
        if(approach.length() <=100 && approach.length() >=25 )
        {
            mApproach.setError(null);
            return true;
        }
        else
        {
            mApproach.setError("The Approach Should be between 75");
            return false;
        }
    }

    private boolean ValidProblemStat() {
        if(problemstatement.length() <=75 && problemstatement.length() >=25)
        {
            mProblemStatement.setError(null);
            return true;
        }
        else
        {
            mProblemStatement.setError("The Problem Statement Should be between 75");
            return false;
        }
    }

    private boolean ValidTeamName() {
        if(teamname.length() <=25 && teamname.length() >=5 )
        {
            mTeamName.setError(null);
            return true;
        }
        else
        {
            mTeamName.setError("The Team Name Should be between 25");
            return false;
        }
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), AdminDashboard.class));
        super.onBackPressed();
    }
    //--------------- Internet Error Dialog Box -----------
    private void showCustomDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(AddNewTeams.this);
        builder.setMessage("Please connect to the internet")
                //   .setCancelable(false)
                .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(getApplicationContext(),AddNewTeams.class));
                        finish();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    //--------------- Check Internet Is Connected -----------
    private boolean isConnected(AddNewTeams userSignUp) {

        ConnectivityManager connectivityManager = (ConnectivityManager) userSignUp.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo bluetoothConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_BLUETOOTH);

        return (wifiConn != null && wifiConn.isConnected()) || (mobileConn != null && mobileConn.isConnected() || (bluetoothConn != null && bluetoothConn.isConnected())); // if true ,  else false

    }

}