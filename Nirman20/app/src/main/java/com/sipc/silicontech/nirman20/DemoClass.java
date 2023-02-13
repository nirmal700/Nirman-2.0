package com.sipc.silicontech.nirman20;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sipc.silicontech.nirman20.Admins.NewHackNationTeamData;
import com.sipc.silicontech.nirman20.Admins.NewIdeateTeamData;
import com.sipc.silicontech.nirman20.Admins.NewLineFollowerTeamData;

import java.util.ArrayList;

public class DemoClass extends AppCompatActivity {

    ArrayList<NewLineFollowerTeamData> mLine;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_class);
        mLine = new ArrayList<>();
        NewLineFollowerTeamData team1 = new NewLineFollowerTeamData("Line Follower","Team-1","SIT BBSR","Nirmal","7008000094","Demo","7008000094","Demo","7008000094","Demo","7008000094",0,0,false,false,0,0,0,0,0);
        NewLineFollowerTeamData team2 = new NewLineFollowerTeamData("Line Follower","Team-2","SIT BBSR","Nirmal","7008000094","Demo","7008000094","Demo","7008000094","Demo","7008000094",0,0,false,false,0,0,0,0,0);
        NewLineFollowerTeamData team3 = new NewLineFollowerTeamData("Line Follower","Team-3","SIT BBSR","Nirmal","7008000094","Demo","7008000094","Demo","7008000094","Demo","7008000094",0,0,false,false,0,0,0,0,0);
        NewLineFollowerTeamData team4 = new NewLineFollowerTeamData("Line Follower","Team-4","SIT BBSR","Nirmal","7008000094","Demo","7008000094","Demo","7008000094","Demo","7008000094",0,0,false,false,0,0,0,0,0);
        NewLineFollowerTeamData team5 = new NewLineFollowerTeamData("Line Follower","Team-5","SIT BBSR","Nirmal","7008000094","Demo","7008000094","Demo","7008000094","Demo","7008000094",0,0,false,false,0,0,0,0,0);

        mLine.add(team1);
        mLine.add(team2);
        mLine.add(team3);
        mLine.add(team4);
        mLine.add(team5);
        for(int i=0;i<mLine.size();i++){
            FirebaseFirestore.getInstance().collection("Line Follower").document(mLine.get(i).getmTeamName()).set(mLine.get(i)).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {

                }
            });
        }
    }
}