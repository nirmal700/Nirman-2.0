package com.sipc.silicontech.nirman20.Admins;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sipc.silicontech.nirman20.R;
import com.sipc.silicontech.nirman20.Users.Help;
import com.sipc.silicontech.nirman20.Users.SessionManagerParticipant;

import java.util.ArrayList;
import java.util.List;

public class AddressIssues extends AppCompatActivity {
    SessionManagerParticipant managerUser;
    ImageView btn_back;
    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    private DatabaseReference mHelpDB;
    private List<Help> mHelp;
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

        //--------------- Internet Checking -----------
        if (!isConnected(AddressIssues.this)) {
            showCustomDialog();
        }


        btn_back.setOnClickListener(v -> {
            startActivity(new Intent(AddressIssues.this, AdminDashboard.class));
            finishAffinity();
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
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

    }

    private void list() {

        mHelpDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mHelp.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                            for (DataSnapshot snapshot3 : snapshot2.getChildren()) {
                                Help help = snapshot3.getValue(Help.class);
                                mHelp.add(0, help);
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

    //--------------- Internet Error Dialog Box -----------
    private void showCustomDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(AddressIssues.this);
        builder.setMessage("Please connect to the internet")
                //   .setCancelable(false)
                .setPositiveButton("Connect", (dialog, which) -> startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS))).setNegativeButton("Cancel", (dialog, which) -> {
                    startActivity(new Intent(getApplicationContext(), AddressIssues.class));
                    finish();
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    //--------------- Check Internet Is Connected -----------
    private boolean isConnected(AddressIssues userSignUp) {

        ConnectivityManager connectivityManager = (ConnectivityManager) userSignUp.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo bluetoothConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_BLUETOOTH);

        return (wifiConn != null && wifiConn.isConnected()) || (mobileConn != null && mobileConn.isConnected() || (bluetoothConn != null && bluetoothConn.isConnected())); // if true ,  else false

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), AdminDashboard.class));
        super.onBackPressed();
    }
}