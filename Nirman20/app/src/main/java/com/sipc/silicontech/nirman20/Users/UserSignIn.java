package com.sipc.silicontech.nirman20.Users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sipc.silicontech.nirman20.R;

import java.util.ArrayList;

public class UserSignIn extends AppCompatActivity {
    SessionManagerParticipant managerParticipant;
    ProgressDialog progressDialog;
    TextInputLayout et_phoneNumber, et_password;
    Button btn_login,btn_backSignUp;
    AutoCompleteTextView mEventType;
    String event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_sign_in);

        et_phoneNumber = findViewById(R.id.et_phoneNumber);
        et_password = findViewById(R.id.et_password);
        mEventType = findViewById(R.id.autoCompleteEvent);

        btn_login = findViewById(R.id.btn_login);
        btn_backSignUp = findViewById(R.id.btn_backSignUp);

        ArrayList<String> arrayListEventType;
        ArrayAdapter<String> arrayAdapterEventType;
        arrayListEventType = new ArrayList<>();
        arrayListEventType.add("Robo Race");
        arrayListEventType.add("Line Follower");
        arrayListEventType.add("Ideate");
        arrayListEventType.add("HackNation");
        arrayAdapterEventType = new ArrayAdapter<>(getApplicationContext(), R.layout.text_menu, arrayListEventType);
        mEventType.setAdapter(arrayAdapterEventType);
        mEventType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                event = arrayAdapterEventType.getItem(i).toString();
            }
        });

        managerParticipant = new SessionManagerParticipant(getApplicationContext());

        if (!isConnected(UserSignIn.this)){
            showCustomDialog();
        }

        btn_backSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UsersSignUp.class);

                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View,String>(findViewById(R.id.btn_backSignUp),"transition_signUp");

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(UserSignIn.this,pairs);
                    startActivity(intent,options.toBundle());
                }
                else{
                    finish();
                }
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });

    }

    private void userLogin() {

        if (!isConnected(UserSignIn.this)){
            showCustomDialog();
        }

        //EditText Validations
        if (!validatePhoneNumber() | !validatePassword() | !validateEvent() ) {

            return;
        }

        //Initialize ProgressDialog
        progressDialog = new ProgressDialog(UserSignIn.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(false);

        String _phoneNumber = et_phoneNumber.getEditText().getText().toString().trim();
        String _password = et_password.getEditText().getText().toString().trim();

        if (_phoneNumber.charAt(0) == '0') {

            _phoneNumber = _phoneNumber.substring(1);
        }

        String _completePhoneNumber = "+91" + _phoneNumber;

        Query checkUser = FirebaseDatabase.getInstance().getReference("Participants").child(event).orderByChild("mPhoneNo").equalTo(_completePhoneNumber);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) { //Check User

                    et_phoneNumber.getEditText().setError(null);
                    String systemPassword = snapshot.child(_completePhoneNumber).child("Profile").child("mPassword").getValue(String.class);

                    if (systemPassword.equals(_password)) {
                        et_phoneNumber.getEditText().setError(null);

                        //Get User data From DataBase
                        String _name = snapshot.child(_completePhoneNumber).child("Profile").child("mParticipantName").getValue(String.class);
                        String _phoneNo = snapshot.child(_completePhoneNumber).child("Profile").child("mPhoneNumber").getValue(String.class);
                        String _password = snapshot.child(_completePhoneNumber).child("Profile").child("mPassword").getValue(String.class);
                        String _teamname = snapshot.child(_completePhoneNumber).child("Profile").child("mTeamName").getValue(String.class);



                        managerParticipant.setParticipantLogin(true); //Set User Login Session
                        managerParticipant.setDetails(event,_name,_password,_phoneNo,_teamname); //Add Data To User Session manager
                        // Intent to Next Activity
                        startActivity(new Intent(getApplicationContext(), UserDashBoard.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        finish();

                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(UserSignIn.this, "Password Doesn't Match!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(UserSignIn.this, "User Does Not Exist!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(UserSignIn.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean validateEvent() {
        String val = mEventType.getText().toString().trim();

        if (val.isEmpty()) {
            mEventType.setError("Field can not be empty");
            return false;
        } else {
            mEventType.setError(null);
            return true;
        }
    }

    //--------------- Internet Error Dialog Box -----------
    private void showCustomDialog() {

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(UserSignIn.this);
        builder.setMessage("Please connect to the internet")
                //.setCancelable(false)
                .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(getApplicationContext(),UserSignIn.class));
                        finish();
                    }
                });
        android.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private boolean validatePassword() {
        String val = et_password.getEditText().getText().toString().trim();

        if (val.isEmpty()){
            et_password.setError("Field can not be empty");
            return false;
        }else if (!val.matches("\\w*")){
            et_password.setError("White spaces not allowed");
            return false;
        }else {
            et_password.setError(null);
            return true;
        }
    }

    private boolean validatePhoneNumber(){
        String val = et_phoneNumber.getEditText().getText().toString().trim();

        if (val.isEmpty()){
            et_phoneNumber.setError("Field can not be empty");
            return false;
        }else if(val.length()>10 | val.length()<10){
            et_phoneNumber.setError("Please Enter 10 Digit Phone Number");
            return false;
        }else if (!val.matches("\\w*")){
            et_phoneNumber.setError("White spaces not allowed");
            return false;
        }else {
            et_phoneNumber.setError(null);
            return true;
        }
    }


    //--------------- Check Internet Is Connected -----------
    private boolean isConnected(UserSignIn userLogin) {

        ConnectivityManager connectivityManager = (ConnectivityManager) userLogin.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo bluetoothConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_BLUETOOTH);

        return (wifiConn != null && wifiConn.isConnected()) || (mobileConn != null && mobileConn.isConnected() || (bluetoothConn != null && bluetoothConn.isConnected())); // if true ,  else false

    }

}