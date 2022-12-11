package com.sipc.silicontech.nirman20.Users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sipc.silicontech.nirman20.R;

import java.util.ArrayList;

public class Request_Help extends AppCompatActivity {
    AutoCompleteTextView mHelpType;
    TextInputLayout et_description;
    ProgressDialog progressDialog;
    Button btn_Submit;
    String helptype;
    SessionManagerParticipant sessionManagerParticipant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_help);
        mHelpType = findViewById(R.id.autoCompleteHelp);
        et_description = findViewById(R.id.et_description);
        btn_Submit = findViewById(R.id.btn_submit);
        sessionManagerParticipant = new SessionManagerParticipant(Request_Help.this);

        ArrayList<String> arrayListHelpType;
        ArrayAdapter<String> arrayAdapterHelpType;
        arrayListHelpType = new ArrayList<>();
        arrayListHelpType.add("LAN Problem");
        arrayListHelpType.add("Internet Connectivity");
        arrayListHelpType.add("Other");
        arrayListHelpType.add("Other");
        arrayAdapterHelpType = new ArrayAdapter<>(getApplicationContext(), R.layout.text_menu, arrayListHelpType);
        mHelpType.setAdapter(arrayAdapterHelpType);
        mHelpType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                helptype = arrayAdapterHelpType.getItem(position);
            }
        });

        progressDialog = new ProgressDialog(Request_Help.this);
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.dismiss();
        
        btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                if (et_description.getEditText().getText().toString().length() <= 5) {
                    btn_Submit.setError("Invalid Description!");
                    progressDialog.dismiss();
                    return;
                }
                Help help = new Help(false,et_description.getEditText().getText().toString(),helptype,sessionManagerParticipant.getTeamName(),sessionManagerParticipant.getEventName(),sessionManagerParticipant.getParticipantName(),sessionManagerParticipant.getPhone(),"",null);
                CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("RequestHelp");
                collectionReference.add(help).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(Request_Help.this, "You will be addressed by our Core Member Shortly!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(Request_Help.this, "Some Error Occured!!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(Request_Help.this, UserDashBoard.class);
                            progressDialog.dismiss();
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }
        });
    }
}