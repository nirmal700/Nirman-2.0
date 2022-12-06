package com.sipc.silicontech.nirman20.Admins;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.sipc.silicontech.nirman20.Evaluators.RoboRaceEvaluation;
import com.sipc.silicontech.nirman20.R;
import com.sipc.silicontech.nirman20.Users.UserSignIn;

public class AdminDashboard extends AppCompatActivity {

    TextView mUserRole,mName;
    SessionManagerAdmin managerAdmin;
    MaterialCardView mAddVolunteer,mAddNewTeams,btn_CustomerProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_dashboard);
        mUserRole = findViewById(R.id.UserRole);
        mName = findViewById(R.id.User_name);
        mAddVolunteer = findViewById(R.id.btn_AddVolunteers);
        mAddNewTeams = findViewById(R.id.btn_AddNewTeams);
        btn_CustomerProfile  = findViewById(R.id.btn_CustomerProfile);


        managerAdmin = new SessionManagerAdmin(getApplicationContext());
        mUserRole.setText(managerAdmin.getUserRole());
        mName.setText(managerAdmin.getName());

        mAddVolunteer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminDashboard.this, AddVolunteers.class));
            }
        });
        mAddNewTeams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminDashboard.this, AddNewTeams.class));
            }
        });
        btn_CustomerProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminDashboard.this, RoboRaceEvaluation.class));
            }
        });


    }
}