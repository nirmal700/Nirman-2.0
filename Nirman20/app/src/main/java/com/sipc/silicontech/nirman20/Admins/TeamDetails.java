package com.sipc.silicontech.nirman20.Admins;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.sipc.silicontech.nirman20.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TeamDetails extends AppCompatActivity {
    AutoCompleteTextView mEventType;
    ImageView btn_back;
    private RecyclerView recyclerView;
    private EditText et_search;
    private MultiViewAdapter multiViewAdapter;
    private List list = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_details);

        mEventType = findViewById(R.id.autoCompleteEvent);
        et_search = findViewById(R.id.et_search);
        btn_back = findViewById(R.id.btn_back);
        et_search = findViewById(R.id.et_search);

        if (!isConnected(TeamDetails.this)) {
            showCustomDialog();
        }

        //Initialize ProgressDialog
        ProgressDialog progressDialog = new ProgressDialog(TeamDetails.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(false);
        progressDialog.cancel();

        recyclerView = findViewById(R.id.rv_team_details);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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


        list = new ArrayList<>();
        multiViewAdapter = new MultiViewAdapter(TeamDetails.this, list);
        recyclerView.setAdapter(multiViewAdapter);

        search();

        btn_back.setOnClickListener(v -> {
            startActivity(new Intent(TeamDetails.this, AdminDashboard.class));
            finish();
        });


        mEventType.setOnItemClickListener((parent, view, position, id) -> {
            progressDialog.show();
            CollectionReference mCollectionReference = FirebaseFirestore.getInstance().collection(arrayAdapterEventType.getItem(position));
            mCollectionReference.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    list.clear();
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        if (Objects.requireNonNull(documentSnapshot.getData().get("mEventParticipating")).toString().equals("Robo Race")) {
                            list.add(documentSnapshot.toObject(NewRoboRaceTeamData.class));
                        } else if (Objects.requireNonNull(documentSnapshot.getData().get("mEventParticipating")).toString().equals("Line Follower")) {
                            list.add(documentSnapshot.toObject(NewLineFollowerTeamData.class));
                        } else if (Objects.requireNonNull(documentSnapshot.getData().get("mEventParticipating")).toString().equals("Ideate - 1")) {
                            list.add(documentSnapshot.toObject(NewIdeateTeamData.class));
                        } else if (Objects.requireNonNull(documentSnapshot.getData().get("mEventParticipating")).toString().equals("Ideate - 2")) {
                            list.add(documentSnapshot.toObject(NewIdeateTeamData.class));
                        } else
                            list.add(documentSnapshot.toObject(NewHackNationTeamData.class));
                        multiViewAdapter = new MultiViewAdapter(TeamDetails.this, list);
                        recyclerView.setAdapter(multiViewAdapter);
                        multiViewAdapter.notifyDataSetChanged();
                    }
                    progressDialog.dismiss();
                } else {
                    list.clear();
                    multiViewAdapter.notifyDataSetChanged();
                    progressDialog.show();
                }
            });
        });


    }

    private void search() {
        if (et_search != null) {

            et_search.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    multiViewAdapter.Search(s);
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), AdminDashboard.class));
        super.onBackPressed();
    }

    //--------------- Internet Error Dialog Box -----------
    private void showCustomDialog() {

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(TeamDetails.this);
        builder.setMessage("Please connect to the internet")
                //.setCancelable(false)
                .setPositiveButton("Connect", (dialog, which) -> startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS)))
                .setNegativeButton("Cancel", (dialog, which) -> {
                    startActivity(new Intent(getApplicationContext(), AdminDashboard.class));
                    finish();
                });
        android.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    //--------------- Check Internet Is Connected -----------
    private boolean isConnected(TeamDetails adminDashboard) {

        ConnectivityManager connectivityManager = (ConnectivityManager) adminDashboard.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo bluetoothConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_BLUETOOTH);

        return (wifiConn != null && wifiConn.isConnected()) || (mobileConn != null && mobileConn.isConnected() || (bluetoothConn != null && bluetoothConn.isConnected())); // if true ,  else false

    }


}