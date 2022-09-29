package com.sipc.silicontech.nirman20.Users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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
                                            arrayListPartcipantNames.add(teamData.getmTeamLead());
                                            arrayListPartcipantNames.add(teamData.getmMem1Name());
                                            arrayListPartcipantNames.add(teamData.getmMem2Name());
                                            if(!(teamData.getmMem3Name().length() <1))
                                            arrayListPartcipantNames.add(teamData.getmMem3Name());
                                            arrayAdapterPartcipantNames = new ArrayAdapter<>(getApplicationContext(),R.layout.text_menu,arrayListPartcipantNames);
                                            mParticipants.setAdapter(arrayAdapterPartcipantNames);
                                            Log.e("TAG", "onItemClick: "+arrayListPartcipantNames.toString() );
                                        }
                                    } 
                                }
                            });
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
}