package com.sipc.silicontech.nirman20;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sipc.silicontech.nirman20.Admins.NewHackNationTeamData;
import com.sipc.silicontech.nirman20.Admins.NewIdeateTeamData;

import java.util.ArrayList;

public class DemoClass extends AppCompatActivity {

    ArrayList<NewIdeateTeamData> mIdeate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_class);
        mIdeate = new ArrayList<>();
        NewIdeateTeamData team1 = new NewIdeateTeamData("Ideate - 1","Team-1","The Problem is that the solution can't be found","The Solution for such problem doesn't exist","SIT Bhubaneswar","Nirmal Kumar","7008000094","Nirmal Kumar","7008000094","Nirmal Kumar","7008000094","","",0,false);
        NewIdeateTeamData team2 = new NewIdeateTeamData("Ideate - 1","Team-2","The Problem is that the solution can't be found","The Solution for such problem doesn't exist","SIT Bhubaneswar","Nirmal Kumar","7008000094","Nirmal Kumar","7008000094","Nirmal Kumar","7008000094","","",0,false);
        NewIdeateTeamData team3 = new NewIdeateTeamData("Ideate - 1","Team-3","The Problem is that the solution can't be found","The Solution for such problem doesn't exist","SIT Bhubaneswar","Nirmal Kumar","7008000094","Nirmal Kumar","7008000094","Nirmal Kumar","7008000094","","",0,false);
        NewIdeateTeamData team4 = new NewIdeateTeamData("Ideate - 1","Team-4","The Problem is that the solution can't be found","The Solution for such problem doesn't exist","SIT Bhubaneswar","Nirmal Kumar","7008000094","Nirmal Kumar","7008000094","Nirmal Kumar","7008000094","","",0,false);
        NewIdeateTeamData team5 = new NewIdeateTeamData("Ideate - 1","Team-5","The Problem is that the solution can't be found","The Solution for such problem doesn't exist","SIT Bhubaneswar","Nirmal Kumar","7008000094","Nirmal Kumar","7008000094","Nirmal Kumar","7008000094","","",0,false);
        NewIdeateTeamData team6 = new NewIdeateTeamData("Ideate - 1","Team-6","The Problem is that the solution can't be found","The Solution for such problem doesn't exist","SIT Bhubaneswar","Nirmal Kumar","7008000094","Nirmal Kumar","7008000094","Nirmal Kumar","7008000094","","",0,false);
        NewIdeateTeamData team7 = new NewIdeateTeamData("Ideate - 1","Team-7","The Problem is that the solution can't be found","The Solution for such problem doesn't exist","SIT Bhubaneswar","Nirmal Kumar","7008000094","Nirmal Kumar","7008000094","Nirmal Kumar","7008000094","","",0,false);
        NewIdeateTeamData team8 = new NewIdeateTeamData("Ideate - 1","Team-8","The Problem is that the solution can't be found","The Solution for such problem doesn't exist","SIT Bhubaneswar","Nirmal Kumar","7008000094","Nirmal Kumar","7008000094","Nirmal Kumar","7008000094","","",0,false);
        NewIdeateTeamData team9 = new NewIdeateTeamData("Ideate - 1","Team-9","The Problem is that the solution can't be found","The Solution for such problem doesn't exist","SIT Bhubaneswar","Nirmal Kumar","7008000094","Nirmal Kumar","7008000094","Nirmal Kumar","7008000094","","",0,false);
        NewIdeateTeamData team10 = new NewIdeateTeamData("Ideate - 1","Team-10","The Problem is that the solution can't be found","The Solution for such problem doesn't exist","SIT Bhubaneswar","Nirmal Kumar","7008000094","Nirmal Kumar","7008000094","Nirmal Kumar","7008000094","","",0,false);
        mIdeate.add(team1);
        mIdeate.add(team2);
        mIdeate.add(team3);
        mIdeate.add(team4);
        mIdeate.add(team5);
        mIdeate.add(team6);
        mIdeate.add(team7);
        mIdeate.add(team8);
        mIdeate.add(team9);
        mIdeate.add(team10);
        for(int i=0;i<mIdeate.size();i++){
            FirebaseFirestore.getInstance().collection("Ideate - 1").document(mIdeate.get(i).getmTeamName()).set(mIdeate.get(i)).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {

                }
            });
        }
    }
}