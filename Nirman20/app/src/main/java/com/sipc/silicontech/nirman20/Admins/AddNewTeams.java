package com.sipc.silicontech.nirman20.Admins;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sipc.silicontech.nirman20.R;

import java.util.ArrayList;
import java.util.Objects;

public class AddNewTeams extends AppCompatActivity {
    TextInputLayout mTeamName;
    TextInputLayout mProblemStatement;
    TextInputLayout mApproach;
    TextInputLayout mCollegeName;
    TextInputLayout mTeamLeadName;
    TextInputLayout mTeamLeadPhone;
    TextInputLayout mMember1Phone;
    TextInputLayout mMember1Name;
    TextInputLayout mMember2Name;
    TextInputLayout mMember2Phone;
    TextInputLayout mMember3Name;
    TextInputLayout mMember3Phone;
    int b = -1;
    ImageView btn_back;
    RadioGroup radio_group;
    AutoCompleteTextView mEventType;
    String event, teamname, problemstatement, approach, clgname, teamleadname, teamleadph, mem1name, mem1phone, mem2name, mem2phone, mem3name, mem3phone;
    ProgressDialog progressDialog;
    private RadioButton t3, t4;

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
        Button btnSubmit = findViewById(R.id.btn_AddTeam);
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
        arrayListEventType.add("Ideate - 1");
        arrayListEventType.add("Ideate - 2");
        arrayListEventType.add("HackNation");
        arrayAdapterEventType = new ArrayAdapter<>(getApplicationContext(), R.layout.text_menu, arrayListEventType);
        mEventType.setAdapter(arrayAdapterEventType);

        //--------------- Internet Checking -----------
        if (!isConnected(AddNewTeams.this)) {
            showCustomDialog();
        }


        radio_group.setOnCheckedChangeListener((radioGroup, i) -> {
            if (i == t4.getId()) {
                mMember3Name.setVisibility(View.VISIBLE);
                mMember3Phone.setVisibility(View.VISIBLE);
                b = 4;
            } else {
                mMember3Name.setVisibility(View.GONE);
                mMember3Phone.setVisibility(View.GONE);
                b = 3;
            }
        });
        mEventType.setOnItemClickListener((adapterView, view, i, l) -> {
            event = arrayAdapterEventType.getItem(i);
            if (event.equals("Line Follower") || event.equals("Robo Race")) {
                mProblemStatement.setVisibility(View.GONE);
                mApproach.setVisibility(View.GONE);
            } else {
                mProblemStatement.setVisibility(View.VISIBLE);
                mApproach.setVisibility(View.VISIBLE);
            }
        });

        btnSubmit.setOnClickListener(view -> {
            teamname = Objects.requireNonNull(mTeamName.getEditText()).getText().toString().trim();
            problemstatement = Objects.requireNonNull(mProblemStatement.getEditText()).getText().toString().trim();
            approach = Objects.requireNonNull(mApproach.getEditText()).getText().toString().trim();
            clgname = Objects.requireNonNull(mCollegeName.getEditText()).getText().toString().trim();
            teamleadname = Objects.requireNonNull(mTeamLeadName.getEditText()).getText().toString().trim();
            teamleadph = Objects.requireNonNull(mTeamLeadPhone.getEditText()).getText().toString().trim();
            mem1name = Objects.requireNonNull(mMember1Name.getEditText()).getText().toString().trim();
            mem1phone = Objects.requireNonNull(mMember1Phone.getEditText()).getText().toString().trim();
            mem2name = Objects.requireNonNull(mMember2Name.getEditText()).getText().toString().trim();
            mem2phone = Objects.requireNonNull(mMember2Phone.getEditText()).getText().toString().trim();
            mem3name = Objects.requireNonNull(mMember3Name.getEditText()).getText().toString().trim();
            mem3phone = Objects.requireNonNull(mMember3Phone.getEditText()).getText().toString().trim();
            if (b == 3) {
                if (!ValidTeamName() | !ValidCollegeName() | !ValidName1() | !ValidPhone1() | !ValidName2() | !ValidPhone2() | !ValidName3() | !ValidPhone3() | !ValidateEventType()) {
                    return;
                } else {
                    if (event.equals("HackNation") || event.equals("Ideate - 1") || event.equals("Ideate - 2")) {
                        if (!ValidProblemStat() | !ValidApproach()) {
                            return;
                        }
                    }
                    progressDialog.show();
                    CollectionReference mCollectionReference = FirebaseFirestore.getInstance().collection(event);
                    if (event.equals("Line Follower")) {
                        NewLineFollowerTeamData newLineFollowerTeamData = new NewLineFollowerTeamData(event, teamname, clgname, teamleadname, teamleadph, mem1name, mem1phone, mem2name, mem2phone, mem3name, mem3phone, 0, 0, false, false, 0, 0, 0, 0, 0);
                        mCollectionReference.document(teamname).set(newLineFollowerTeamData).addOnSuccessListener(unused -> progressDialog.cancel()).addOnFailureListener(e -> {
                            progressDialog.cancel();
                            Toast.makeText(AddNewTeams.this, "Failed! Try Again" + e, Toast.LENGTH_SHORT).show();
                        }).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(AddNewTeams.this, teamname + "Added Successfully!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(AddNewTeams.this, AdminDashboard.class);
                                progressDialog.dismiss();
                                startActivity(intent);
                                finish();
                            }
                        });
                    } else if (event.equals("Robo Race")) {
                        NewRoboRaceTeamData newRoboRaceTeamData = new NewRoboRaceTeamData(event, teamname, clgname, teamleadname, teamleadph, mem1name, mem1phone, mem2name, mem2phone, mem3name, mem3phone, 0, 0, false, false, 0L, 0L, 0L, 0L, 0L);
                        mCollectionReference.document(teamname).set(newRoboRaceTeamData).addOnSuccessListener(unused -> progressDialog.cancel()).addOnFailureListener(e -> {
                            progressDialog.cancel();
                            Toast.makeText(AddNewTeams.this, "Failed! Try Again" + e, Toast.LENGTH_SHORT).show();
                        }).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(AddNewTeams.this, teamname + "Added Successfully!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(AddNewTeams.this, AdminDashboard.class);
                                progressDialog.dismiss();
                                startActivity(intent);
                                finish();
                            }
                        });
                    } else if (event.equals("Ideate - 1")) {
                        NewIdeateTeamData newIdeateTeamData = new NewIdeateTeamData(event, teamname, problemstatement, approach, clgname, teamleadname, teamleadph, mem1name, mem1phone, mem2name, mem2phone, mem3name, mem3phone, 0, false);
                        mCollectionReference.document(teamname).set(newIdeateTeamData).addOnSuccessListener(unused -> progressDialog.cancel()).addOnFailureListener(e -> {
                            progressDialog.cancel();
                            Toast.makeText(AddNewTeams.this, "Failed! Try Again" + e, Toast.LENGTH_SHORT).show();
                        }).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(AddNewTeams.this, teamname + "Added Successfully!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(AddNewTeams.this, AdminDashboard.class);
                                progressDialog.dismiss();
                                startActivity(intent);
                                finish();
                            }
                        });
                    } else if (event.equals("Ideate - 2")) {
                        NewIdeateTeamData newIdeateTeamData = new NewIdeateTeamData(event, teamname, problemstatement, approach, clgname, teamleadname, teamleadph, mem1name, mem1phone, mem2name, mem2phone, mem3name, mem3phone, 0, false);
                        mCollectionReference.document(teamname).set(newIdeateTeamData).addOnSuccessListener(unused -> progressDialog.cancel()).addOnFailureListener(e -> {
                            progressDialog.cancel();
                            Toast.makeText(AddNewTeams.this, "Failed! Try Again" + e, Toast.LENGTH_SHORT).show();
                        }).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(AddNewTeams.this, teamname + "Added Successfully!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(AddNewTeams.this, AdminDashboard.class);
                                progressDialog.dismiss();
                                startActivity(intent);
                                finish();
                            }
                        });
                    } else if (event.equals("HackNation")) {
                        NewHackNationTeamData newHackNationTeamData = new NewHackNationTeamData(event, teamname, problemstatement, approach, clgname, teamleadname, teamleadph, mem1name, mem1phone, mem2name, mem2phone, mem3name, mem3phone, 0, false);
                        mCollectionReference.document(teamname).set(newHackNationTeamData).addOnSuccessListener(unused -> progressDialog.cancel()).addOnFailureListener(e -> {
                            progressDialog.cancel();
                            Toast.makeText(AddNewTeams.this, "Failed! Try Again" + e, Toast.LENGTH_SHORT).show();
                        }).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(AddNewTeams.this, teamname + "Added Successfully!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(AddNewTeams.this, AdminDashboard.class);
                                progressDialog.dismiss();
                                startActivity(intent);
                                finish();
                            }
                        });
                    }

                }
            }
            if (b == 4) {
                if (!ValidTeamName() | !ValidProblemStat() | !ValidApproach() | !ValidCollegeName() | !ValidName1() | !ValidPhone1() | !ValidName2() | !ValidPhone2() | !ValidName3() | !ValidPhone3() | !ValidName4() | !ValidPhone4() | !ValidateEventType()) {
                    return;
                } else {
                    if (event.equals("HackNation") || event.equals("Ideate")) {
                        if (!ValidProblemStat() | !ValidApproach()) {
                            return;
                        }
                    }
                    progressDialog.show();
                    CollectionReference mCollectionReference = FirebaseFirestore.getInstance().collection(event);
                    if (event.equals("Line Follower")) {
                        NewLineFollowerTeamData newLineFollowerTeamData = new NewLineFollowerTeamData(event, teamname, clgname, teamleadname, teamleadph, mem1name, mem1phone, mem2name, mem2phone, mem3name, mem3phone, 0, 0, false, false, 0, 0, 0, 0, 0);
                        mCollectionReference.document(teamname).set(newLineFollowerTeamData).addOnSuccessListener(unused -> progressDialog.cancel()).addOnFailureListener(e -> {
                            progressDialog.cancel();
                            Toast.makeText(AddNewTeams.this, "Failed! Try Again" + e, Toast.LENGTH_SHORT).show();
                        }).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(AddNewTeams.this, teamname + "Added Successfully!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(AddNewTeams.this, AdminDashboard.class);
                                progressDialog.dismiss();
                                startActivity(intent);
                                finish();
                            }
                        });
                    } else if (event.equals("Robo Race")) {
                        NewRoboRaceTeamData newRoboRaceTeamData = new NewRoboRaceTeamData(event, teamname, clgname, teamleadname, teamleadph, mem1name, mem1phone, mem2name, mem2phone, mem3name, mem3phone, 0, 0, false, false, 0, 0, 0, 0, 0L);
                        mCollectionReference.document(teamname).set(newRoboRaceTeamData).addOnSuccessListener(unused -> progressDialog.cancel()).addOnFailureListener(e -> {
                            progressDialog.cancel();
                            Toast.makeText(AddNewTeams.this, "Failed! Try Again" + e, Toast.LENGTH_SHORT).show();
                        }).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(AddNewTeams.this, teamname + "Added Successfully!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(AddNewTeams.this, AdminDashboard.class);
                                progressDialog.dismiss();
                                startActivity(intent);
                                finish();
                            }
                        });
                    } else if (event.equals("Ideate")) {
                        NewIdeateTeamData newIdeateTeamData = new NewIdeateTeamData(event, teamname, problemstatement, approach, clgname, teamleadname, teamleadph, mem1name, mem1phone, mem2name, mem2phone, mem3name, mem3phone, 0, false);
                        mCollectionReference.document(teamname).set(newIdeateTeamData).addOnSuccessListener(unused -> progressDialog.cancel()).addOnFailureListener(e -> {
                            progressDialog.cancel();
                            Toast.makeText(AddNewTeams.this, "Failed! Try Again" + e, Toast.LENGTH_SHORT).show();
                        }).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(AddNewTeams.this, teamname + "Added Successfully!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(AddNewTeams.this, AdminDashboard.class);
                                progressDialog.dismiss();
                                startActivity(intent);
                                finish();
                            }
                        });
                    } else if (event.equals("HackNation")) {
                        NewHackNationTeamData newHackNationTeamData = new NewHackNationTeamData(event, teamname, problemstatement, approach, clgname, teamleadname, teamleadph, mem1name, mem1phone, mem2name, mem2phone, mem3name, mem3phone, 0, false);
                        mCollectionReference.document(teamname).set(newHackNationTeamData).addOnSuccessListener(unused -> progressDialog.cancel()).addOnFailureListener(e -> {
                            progressDialog.cancel();
                            Toast.makeText(AddNewTeams.this, "Failed! Try Again" + e, Toast.LENGTH_SHORT).show();
                        }).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(AddNewTeams.this, teamname + "Added Successfully!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(AddNewTeams.this, AdminDashboard.class);
                                progressDialog.dismiss();
                                startActivity(intent);
                                finish();
                            }
                        });
                    }

                }
            } else {
                t3.setError("Select Any One To Proceed");
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
        if (Objects.requireNonNull(mTeamLeadName.getEditText()).getText().toString().length() <= 35 && mTeamLeadName.getEditText().getText().toString().length() >= 3) {
            mTeamLeadName.setError(null);
            return true;
        } else {
            mTeamLeadName.setError("The Name Should be between 25");
            return false;
        }
    }

    private boolean ValidName2() {
        if (Objects.requireNonNull(mMember1Name.getEditText()).getText().toString().length() <= 35 && mMember1Name.getEditText().getText().toString().length() >= 3) {
            mMember1Name.setError(null);
            return true;
        } else {
            mMember1Name.setError("The Name Should be between 25");
            return false;
        }
    }

    private boolean ValidName3() {
        if (Objects.requireNonNull(mMember2Name.getEditText()).getText().toString().length() <= 35 && mMember2Name.getEditText().getText().toString().length() >= 3) {
            mMember2Name.setError(null);
            return true;
        } else {
            mMember2Name.setError("The Name Should be between 25");
            return false;
        }
    }

    private boolean ValidName4() {
        if (Objects.requireNonNull(mMember3Name.getEditText()).getText().toString().length() <= 35 && mMember3Name.getEditText().getText().toString().length() >= 3) {
            mMember3Name.setError(null);
            return true;
        } else {
            mMember3Name.setError("The Name Should be between 25");
            return false;
        }
    }

    private boolean ValidCollegeName() {
        if (clgname.length() <= 55 && clgname.length() >= 2) {
            mCollegeName.setError(null);
            return true;
        } else {
            mCollegeName.setError("The College Name Should be between 35");
            return false;
        }
    }

    private boolean ValidApproach() {
        if (approach.length() <= 250 && approach.length() >= 15) {
            mApproach.setError(null);
            return true;
        } else {
            mApproach.setError("The Approach Should be between 75");
            return false;
        }
    }

    private boolean ValidProblemStat() {
        if (problemstatement.length() <= 200 && problemstatement.length() >= 10) {
            mProblemStatement.setError(null);
            return true;
        } else {
            mProblemStatement.setError("The Problem Statement Should be between 75");
            return false;
        }
    }

    private boolean ValidTeamName() {
        if (teamname.length() <= 25 && teamname.length() >= 5) {
            mTeamName.setError(null);
            return true;
        } else {
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
                .setPositiveButton("Connect", (dialog, which) -> startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS))).setNegativeButton("Cancel", (dialog, which) -> {
                    startActivity(new Intent(getApplicationContext(), AddNewTeams.class));
                    finish();
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