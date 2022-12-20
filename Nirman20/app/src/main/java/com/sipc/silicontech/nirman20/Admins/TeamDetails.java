package com.sipc.silicontech.nirman20.Admins;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sipc.silicontech.nirman20.R;

import java.util.ArrayList;
import java.util.List;

public class TeamDetails extends AppCompatActivity {
    AutoCompleteTextView mEventType;
    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    private EditText et_search;
    private MultiViewAdapter multiViewAdapter;
    ImageView btn_back;
   private List list = new ArrayList();
    Query eventDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_details);

        mEventType = findViewById(R.id.autoCompleteEvent);
        et_search = findViewById(R.id.et_search);
        btn_back = findViewById(R.id.btn_back);
        et_search = findViewById(R.id.et_search);
        //Initialize ProgressDialog
        progressDialog = new ProgressDialog(TeamDetails.this);
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
        arrayListEventType.add("Ideate");
        arrayListEventType.add("HackNation");
        arrayAdapterEventType = new ArrayAdapter<>(getApplicationContext(), R.layout.text_menu, arrayListEventType);
        mEventType.setAdapter(arrayAdapterEventType);


        list = new ArrayList<>();
        multiViewAdapter = new MultiViewAdapter(TeamDetails.this,list);
        recyclerView.setAdapter(multiViewAdapter);


        mEventType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CollectionReference mCollectionReference = FirebaseFirestore.getInstance().collection(arrayAdapterEventType.getItem(position));
                mCollectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            list.clear();
                            for(QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                Log.e("TAG7009", "onComplete: " + documentSnapshot.getId() + "=>" + documentSnapshot.getData());
                                if(documentSnapshot.getData().get("mEventParticipating").toString().equals("Robo Race"))
                                {
                                    list.add(documentSnapshot.toObject(NewRoboRaceTeamData.class));
                                }else if(documentSnapshot.getData().get("mEventParticipating").toString().equals("Line Follower"))
                                {
                                    list.add(documentSnapshot.toObject(NewLineFollowerTeamData.class));
                                }else if(documentSnapshot.getData().get("mEventParticipating").toString().equals("Ideate")){
                                    list.add(documentSnapshot.toObject(NewIdeateTeamData.class));
                                }
                                else
                                    list.add(documentSnapshot.toObject(NewHackNationTeamData.class));
                                multiViewAdapter = new MultiViewAdapter(TeamDetails.this,list);
                                recyclerView.setAdapter(multiViewAdapter);
                                multiViewAdapter.notifyDataSetChanged();
                            }
                        }else {
                            list.clear();
                            multiViewAdapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        });


    }
}