package com.sipc.silicontech.nirman20.Users;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sipc.silicontech.nirman20.R;

public class UsersSignUp extends AppCompatActivity {
    ImageView bckBtn;
    Button next,login;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.users_sign_up);
        bckBtn = findViewById(R.id.btn_backToCd);
        next = findViewById(R.id.btn_next);
        login = findViewById(R.id.btn_login);
        title = findViewById(R.id.title);

    }
    public void callNextSignpScreen(View view)
    {
        Intent intent = new Intent(getApplicationContext(),UsersSignup2.class);
        Pair[] pairs = new Pair[4];
        pairs[0] = new Pair<View,String>(bckBtn,"transition_back_btn");
        pairs[1] = new Pair<View,String>(next,"transition_next_btn");
        pairs[2] = new Pair<View,String>(login,"transition_login_btn");
        pairs[3] = new Pair<View,String>(title,"transition_title_text");

        ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(UsersSignUp.this,pairs);
        startActivity(intent,activityOptions.toBundle());
    }
}