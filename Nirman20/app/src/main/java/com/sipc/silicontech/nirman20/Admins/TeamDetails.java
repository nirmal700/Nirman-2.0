package com.sipc.silicontech.nirman20.Admins;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.firestore.Query;
import com.sipc.silicontech.nirman20.R;

import java.util.ArrayList;

public class TeamDetails extends AppCompatActivity {
    AutoCompleteTextView mEventType;
    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    private EditText et_search;
    private TeamDetailsAdapter teamDetailsAdapter;
    ImageView btn_back;
    Query eventDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_details);

        mEventType = findViewById(R.id.autoCompleteEvent);
        et_search = findViewById(R.id.et_search);
        btn_back = findViewById(R.id.btn_back);
        //Initialize ProgressDialog
        progressDialog = new ProgressDialog(TeamDetails.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        recyclerView = findViewById(R.id.rv_team_details);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<String> arrayListEventType;
        ArrayAdapter<String> arrayAdapterEventType;
        arrayListEventType = new ArrayList<>();
        arrayListEventType.add("Robo Race");
        arrayListEventType.add("Line Follower");
        arrayListEventType.add("Ideate");
        arrayListEventType.add("HackNation");
        arrayAdapterEventType = new ArrayAdapter<>(getApplicationContext(), R.layout.text_menu, arrayListEventType);
        mEventType.setAdapter(arrayAdapterEventType);

    }
}