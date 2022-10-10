package com.sipc.silicontech.nirman20.Users;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.sipc.silicontech.nirman20.R;

public class UserSignIn extends AppCompatActivity {
    SessionManagerParticipant managerParticipant;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_sign_in);

    }
}