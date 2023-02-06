package com.sipc.silicontech.nirman20.Admins;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sipc.silicontech.nirman20.R;
import com.sipc.silicontech.nirman20.Users.UsersSignUp;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class AdminSignin extends AppCompatActivity {

    Button mSignIn, mParticipant;
    ProgressDialog progressDialog;
    SessionManagerAdmin sessionManagerAdmin;
    String phoneNumber;
    private TextInputLayout et_Sic, et_Password;
    private FirebaseAuth auth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_signin);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        et_Sic = findViewById(R.id.et_SIC);
        et_Password = findViewById(R.id.et_password);
        mParticipant = findViewById(R.id.btn_backSignIn);
        mSignIn = findViewById(R.id.btn_login);

        auth = FirebaseAuth.getInstance();

        sessionManagerAdmin = new SessionManagerAdmin(getApplicationContext());

        mParticipant.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), UsersSignUp.class);

            Pair[] pairs = new Pair[1];
            pairs[0] = new Pair<View, String>(findViewById(R.id.btn_backSignIn), "transition_signUp");

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(AdminSignin.this, pairs);
                startActivity(intent, options.toBundle());
            } else {
                finish();
            }
        });

        mSignIn.setOnClickListener(view -> SipcLogin());

        //--------------- Internet Checking -----------
        if (!isConnected(AdminSignin.this)) {
            showCustomDialog();
        }
    }

    private void SipcLogin() {
        if (!isConnected(AdminSignin.this)) {
            showCustomDialog();
        }

        //EditText Validations
        if (!validatePassword()) {

            return;
        }

        //Initialize ProgressDialog
        progressDialog = new ProgressDialog(AdminSignin.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(false);

        String _sic = Objects.requireNonNull(et_Sic.getEditText()).getText().toString().trim();
        String _password = Objects.requireNonNull(et_Password.getEditText()).getText().toString().trim();

        Query checkUser = FirebaseDatabase.getInstance().getReference("SIPC").orderByChild("mSic").equalTo(_sic);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    et_Sic.getEditText().setError(null);
                    String systemPassword = snapshot.child(_sic).child("Profile").child("mPassword").getValue(String.class);

                    if (Objects.requireNonNull(systemPassword).equals(_password)) {
                        et_Sic.getEditText().setError(null);
                        String Name = snapshot.child(_sic).child("Profile").child("mName").getValue(String.class);
                        String AccessLev = snapshot.child(_sic).child("Profile").child("mAccessLevel").getValue(String.class);
                        String SIC = snapshot.child(_sic).child("Profile").child("SIC").getValue(String.class);
                        String UserRole = snapshot.child(_sic).child("Profile").child("mUserRole").getValue(String.class);
                        String Password = snapshot.child(_sic).child("Profile").child("mPassword").getValue(String.class);
                        String Phone = "+91" + snapshot.child(_sic).child("mPhoneNo").getValue(String.class);
                        sessionManagerAdmin.setAdminLogin(true);
                        sessionManagerAdmin.setDetails(Name, SIC, Password, AccessLev, UserRole, Phone);

                        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth)
                                .setPhoneNumber(Phone)
                                .setTimeout(60L, TimeUnit.SECONDS)
                                .setActivity(AdminSignin.this)
                                .setCallbacks(mCallBacks)
                                .build();
                        PhoneAuthProvider.verifyPhoneNumber(options);


                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(AdminSignin.this, "Password Doesn't Match!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(AdminSignin.this, "User Does Not Exist!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminSignin.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

                Toast.makeText(AdminSignin.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);

                //sometime the code is not detected automatically
                //so user has to manually enter the code

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent otpIntent = new Intent(AdminSignin.this, AdminPhoneVerification.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        otpIntent.putExtra("auth", s);
                        phoneNumber = sessionManagerAdmin.getPhone();
                        otpIntent.putExtra("phoneNumber", phoneNumber);
                        startActivity(otpIntent);
                        finish();
                    }

                }, 1);

            }
        };
    }

    private boolean validatePassword() {
        String val = Objects.requireNonNull(et_Password.getEditText()).getText().toString().trim();

        if (val.isEmpty()) {
            et_Password.setError("Field can not be empty");
            return false;
        } else if (!val.matches("\\w*")) {
            et_Password.setError("White spaces & Special Char not allowed");
            return false;
        } else {
            et_Password.setError(null);
            return true;
        }
    }

    //--------------- Internet Error Dialog Box -----------
    private void showCustomDialog() {

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(AdminSignin.this);
        builder.setMessage("Please connect to the internet")
                //.setCancelable(false)
                .setPositiveButton("Connect", (dialog, which) -> startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS)))
                .setNegativeButton("Cancel", (dialog, which) -> {
                    startActivity(new Intent(getApplicationContext(), AdminSignin.class));
                    finish();
                });
        android.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    //--------------- Check Internet Is Connected -----------
    private boolean isConnected(AdminSignin userLogin) {

        ConnectivityManager connectivityManager = (ConnectivityManager) userLogin.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo bluetoothConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_BLUETOOTH);

        return (wifiConn != null && wifiConn.isConnected()) || (mobileConn != null && mobileConn.isConnected() || (bluetoothConn != null && bluetoothConn.isConnected())); // if true ,  else false

    }
}