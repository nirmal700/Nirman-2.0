package com.sipc.silicontech.nirman20.Evaluators;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sipc.silicontech.nirman20.Admins.AdminSignin;
import com.sipc.silicontech.nirman20.R;
import com.sipc.silicontech.nirman20.Users.UserSignIn;

import java.util.ArrayList;
import java.util.Objects;

public class EvaluatorSignIn extends AppCompatActivity {
    ArrayList<String> arrayListEvaluatorName;
    ArrayAdapter<String> arrayAdapterEvaluatorName;
    AutoCompleteTextView autoCompleteEvaluator;
    TextInputLayout et_password;
    Button btn_login, btn_sipc;
    String evaluator_name;
    ProgressDialog progressDialog;
    SessionManagerEvaluator managerEvaluator;
    TextView participant;
    FirebaseDatabase database = FirebaseDatabase.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.evaluator_sign_in);
        autoCompleteEvaluator = findViewById(R.id.autoCompleteEvaluator);
        et_password = findViewById(R.id.et_password);
        btn_login = findViewById(R.id.btn_login);
        btn_sipc = findViewById(R.id.btn_sipc);
        participant = findViewById(R.id.tv_particiapnt);
        managerEvaluator = new SessionManagerEvaluator(getApplicationContext());
        arrayListEvaluatorName = new ArrayList<>();
        arrayAdapterEvaluatorName = new ArrayAdapter<>(getApplicationContext(), R.layout.text_menu, arrayListEvaluatorName);

        //Initialize ProgressDialog
        progressDialog = new ProgressDialog(EvaluatorSignIn.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(false);
        progressDialog.dismiss();

        if (!isConnected(EvaluatorSignIn.this)) {
            showCustomDialog();
        }

        DatabaseReference myRef = database.getReference("Evaluator");
        progressDialog.show();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String name = ds.child("eName").getValue(String.class);
                    Log.e("TAG", "onDataChange: " + name);
                    arrayListEvaluatorName.add(name);
                    arrayAdapterEvaluatorName = new ArrayAdapter<>(getApplicationContext(), R.layout.text_menu, arrayListEvaluatorName);
                    autoCompleteEvaluator.setAdapter(arrayAdapterEvaluatorName);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EvaluatorSignIn.this, "Error!!" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        btn_login.setOnClickListener(v -> Evaluatorlogin());

        btn_sipc.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), AdminSignin.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)));
        participant.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), UserSignIn.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)));

        autoCompleteEvaluator.setOnItemClickListener((parent, view, position, id) -> evaluator_name = arrayAdapterEvaluatorName.getItem(position));


    }

    private void Evaluatorlogin() {
        progressDialog.show();
        if (!validatePassword()) {
            Toast.makeText(EvaluatorSignIn.this, "Invalid Inputs", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            return;
        }

        String _password1 = Objects.requireNonNull(et_password.getEditText()).getText().toString().trim();
        Query checkUser = FirebaseDatabase.getInstance().getReference("Evaluator").orderByChild("eName").equalTo(evaluator_name);
        progressDialog.show();
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) { //Check User


                    String systemPassword = snapshot.child(evaluator_name).child("ePassword").getValue(String.class);

                    if (Objects.requireNonNull(systemPassword).equals(_password1)) {
                        autoCompleteEvaluator.setError(null);

                        //Get User data From DataBase
                        String _name = snapshot.child(evaluator_name).child("eName").getValue(String.class);
                        String _phoneNo = snapshot.child(evaluator_name).child("ePhoneNo").getValue(String.class);
                        String _password = snapshot.child(evaluator_name).child("ePassword").getValue(String.class);
                        String _event = snapshot.child(evaluator_name).child("eEventAssigned").getValue(String.class);


                        managerEvaluator.setEvaluatorLogin(true); //Set User Login Session
                        managerEvaluator.setEvaluatorDetails(_name, _event, _password, _phoneNo); //Add Data To User Session manager
                        progressDialog.dismiss();
                        // Intent to Next Activity
                        startActivity(new Intent(getApplicationContext(), EvaluatorDashboard.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        finish();

                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(EvaluatorSignIn.this, "Password Doesn't Match!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(EvaluatorSignIn.this, "User Does Not Exist!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(EvaluatorSignIn.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validatePassword() {
        String val = Objects.requireNonNull(et_password.getEditText()).getText().toString().trim();

        if (val.isEmpty()) {
            et_password.setError("Field can not be empty");
            return false;
        } else if (val.length() < 8) {
            et_password.setError("Password minimum 8 Characters");
            return false;
        } else if (!val.matches("\\w*")) {
            et_password.setError("White spaces not allowed");
            return false;
        } else {
            et_password.setError(null);
            return true;
        }

    }

    private void showCustomDialog() {

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(EvaluatorSignIn.this);
        builder.setMessage("Please connect to the internet")
                //.setCancelable(false)
                .setPositiveButton("Connect", (dialog, which) -> startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS))).setNegativeButton("Cancel", (dialog, which) -> {
                    startActivity(new Intent(getApplicationContext(), EvaluatorSignIn.class));
                    finish();
                });
        android.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    //--------------- Check Internet Is Connected -----------
    private boolean isConnected(EvaluatorSignIn userLogin) {

        ConnectivityManager connectivityManager = (ConnectivityManager) userLogin.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo bluetoothConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_BLUETOOTH);

        return (wifiConn != null && wifiConn.isConnected()) || (mobileConn != null && mobileConn.isConnected() || (bluetoothConn != null && bluetoothConn.isConnected())); // if true ,  else false

    }
}