package com.sipc.silicontech.nirman20.Users;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sipc.silicontech.nirman20.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Request_Help extends AppCompatActivity {
    SessionManagerParticipant managerUser;

    FloatingActionButton btn_add;
    ImageView btn_back;
    String helptype;
    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    private DatabaseReference mHelpDB;
    private List<Help> mHelp;
    private RequestHelpAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_help_user);
        btn_back = findViewById(R.id.btn_backToCd);
        recyclerView = findViewById(R.id.issueListView);
        btn_add = findViewById(R.id.btn_add);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mHelp = new ArrayList<>();

        managerUser = new SessionManagerParticipant(getApplicationContext());
        mHelpDB = FirebaseDatabase.getInstance().getReference("EventIssues").child(managerUser.getEventName()).child(managerUser.getTeamName()).child("Issues");
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {

                if (dy > 0) {
                    btn_add.hide();
                } else {
                    btn_add.show();
                }

                super.onScrolled(recyclerView, dx, dy);

            }
        });
        btn_back.setOnClickListener(v -> {
            startActivity(new Intent(Request_Help.this, UserDashBoard.class));
            finishAffinity();
        });

        //Initialize ProgressDialog
        loadProgressDialog();

        list(); // Load all data

        btn_add.setOnClickListener(v -> {


            final Dialog dialog = new Dialog(Request_Help.this);

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.add_help);

            dialog.show();
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setWindowAnimations(R.style.BottomDialog);
            dialog.getWindow().setGravity(Gravity.BOTTOM);

            AutoCompleteTextView mHelpType = dialog.findViewById(R.id.autoCompleteHelp);
            TextInputLayout et_description = dialog.findViewById(R.id.et_description);

            ArrayList<String> arrayListHelpType;
            ArrayAdapter<String> arrayAdapterHelpType;
            arrayListHelpType = new ArrayList<>();
            arrayListHelpType.add("LAN Problem");
            arrayListHelpType.add("Internet Connectivity");
            arrayListHelpType.add("Other");
            arrayListHelpType.add("Other");
            arrayAdapterHelpType = new ArrayAdapter<>(getApplicationContext(), R.layout.text_menu, arrayListHelpType);
            mHelpType.setAdapter(arrayAdapterHelpType);

            mHelpType.setOnItemClickListener((parent, view, position, id) -> helptype = arrayAdapterHelpType.getItem(position));

            Button submit = dialog.findViewById(R.id.btn_Submit);


            submit.setOnClickListener(v1 -> {

                if (helptype.isEmpty() | Objects.requireNonNull(et_description.getEditText()).getText().toString().trim().isEmpty()) {
                    Toast.makeText(Request_Help.this, "Do not empty Title and Description", Toast.LENGTH_SHORT).show();
                } else {


                    String tag = helptype;
                    String nDesc = et_description.getEditText().getText().toString().trim();

                    String id = mHelpDB.push().getKey();


                    if (id != null) {
                        Help help = new Help(false, nDesc, tag, managerUser.getTeamName(), managerUser.getEventName(), managerUser.getParticipantName(), managerUser.getPhone(), "", id);
                        mHelpDB.child(id).setValue(help);
                    }

                    dialog.dismiss();
                    Toast.makeText(Request_Help.this, "New Issue listed", Toast.LENGTH_SHORT).show();
                    loadProgressDialog();
                    list();

                }

            });

        });


    }

    //-----------------------Progress Dialog-------------------
    private void loadProgressDialog() {

        //Initialize ProgressDialog
        progressDialog = new ProgressDialog(Request_Help.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

    }

    private void list() {

        mHelpDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                mHelp.clear();

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                    Help help = postSnapshot.getValue(Help.class);
                    mHelp.add(0, help);
                }

                adapter = new RequestHelpAdapter(Request_Help.this, mHelp);
                recyclerView.setAdapter(adapter);
                recyclerView.smoothScrollToPosition(0);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Request_Help.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}