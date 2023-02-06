package com.sipc.silicontech.nirman20.Users;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

public class User_Suggestion extends AppCompatActivity {
    SessionManagerParticipant managerUser;
    ImageView btn_back;

    private RecyclerView recyclerView;

    private ProgressDialog progressDialog;
    private DatabaseReference mSug;
    private List<Suggestion> suggestions;
    private SuggestionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_suggestion);
        btn_back = findViewById(R.id.btn_backToCd);
        recyclerView = findViewById(R.id.sugListView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        suggestions = new ArrayList<>();

        managerUser = new SessionManagerParticipant(getApplicationContext());
        String teamNamae = managerUser.getTeamName();
        String event = managerUser.getEventName();

        mSug = FirebaseDatabase.getInstance().getReference("Suggestions_Team").child(event).child(teamNamae).child("Suggestions");

        btn_back.setOnClickListener(v -> {
            startActivity(new Intent(User_Suggestion.this, UserDashBoard.class));
            finishAffinity();
        });

        //Initialize ProgressDialog
        loadProgressDialog();

        list(); // Load all data
    }

    //-----------------------Progress Dialog-------------------
    private void loadProgressDialog() {

        //Initialize ProgressDialog
        progressDialog = new ProgressDialog(User_Suggestion.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

    }

    private void list() {

        mSug.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                suggestions.clear();

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                    Suggestion model = postSnapshot.getValue(Suggestion.class);
                    suggestions.add(0, model);
                }

                adapter = new SuggestionAdapter(User_Suggestion.this, suggestions);
                recyclerView.setAdapter(adapter);
                recyclerView.smoothScrollToPosition(0);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(User_Suggestion.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}