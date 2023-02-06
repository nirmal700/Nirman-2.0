package com.sipc.silicontech.nirman20.Admins;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sipc.silicontech.nirman20.R;
import com.sipc.silicontech.nirman20.Users.Help;
import com.sipc.silicontech.nirman20.Users.RequestHelpAdapter;
import com.sipc.silicontech.nirman20.Users.Request_Help;
import com.sipc.silicontech.nirman20.Users.SessionManagerParticipant;
import com.sipc.silicontech.nirman20.Users.UserDashBoard;

import java.util.ArrayList;
import java.util.List;

public class AddressIssues extends AppCompatActivity {
    SessionManagerParticipant managerUser;


    ImageView btn_back;
    private RecyclerView recyclerView;

    private ProgressDialog progressDialog;
    private DatabaseReference mHelpDB;


    private List<Help> mHelp;
    String helptype;
    private AddressIssuesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.address_issues);
        btn_back = findViewById(R.id.btn_backToCd);
        recyclerView = findViewById(R.id.issueListView);
        mHelp = new ArrayList<>();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        managerUser = new SessionManagerParticipant(getApplicationContext());
        mHelpDB = FirebaseDatabase.getInstance().getReference("EventIssues");


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddressIssues.this, AdminDashboard.class));
                finishAffinity();
            }
        });

        //Initialize ProgressDialog
        loadProgressDialog();

        list(); // Load all data

    }
    //-----------------------Progress Dialog-------------------
    private void loadProgressDialog() {

        //Initialize ProgressDialog
        progressDialog = new ProgressDialog(AddressIssues.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

    }
    private void list() {

        mHelpDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mHelp.clear();
                for ( DataSnapshot snapshot : dataSnapshot.getChildren()){
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        for (DataSnapshot snapshot2 : snapshot1.getChildren()){
                            for (DataSnapshot snapshot3 : snapshot2.getChildren()){
                                Help help = snapshot3.getValue(Help.class);
                                mHelp.add(0,help);
                            }
                        }
                    }
                }
                adapter = new AddressIssuesAdapter(AddressIssues.this, mHelp);
                recyclerView.setAdapter(adapter);
                recyclerView.smoothScrollToPosition(0);
                progressDialog.dismiss();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AddressIssues.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });




    }
}