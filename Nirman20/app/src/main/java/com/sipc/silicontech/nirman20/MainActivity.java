package com.sipc.silicontech.nirman20;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.play.core.appupdate.AppUpdateManager;
import com.sipc.silicontech.nirman20.Admins.AdminDashboard;
import com.sipc.silicontech.nirman20.Admins.AdminPhoneVerification;
import com.sipc.silicontech.nirman20.Admins.AdminSignin;
import com.sipc.silicontech.nirman20.Admins.SessionManagerAdmin;
import com.sipc.silicontech.nirman20.Evaluators.EvaluatorLogin;
import com.sipc.silicontech.nirman20.Users.SessionManagerParticipant;
import com.sipc.silicontech.nirman20.Users.UserDashBoard;
import com.sipc.silicontech.nirman20.Users.UsersSignUp;

public class MainActivity extends AppCompatActivity {
    Animation topanim, botanim;
    ImageView logo;
    View robo;
    TextView cn1, cn2, rwth, wel, dev;


    private static final int SPLASH_TIMER = 3900;
    private static final int RC_APP_UPDATE = 895;
    

    SessionManagerAdmin managerAdmin;
    SessionManagerParticipant managerParticipant;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        topanim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        botanim = AnimationUtils.loadAnimation(this, R.anim.bottom_anim);

        logo = findViewById(R.id.logo);
        robo = findViewById(R.id.splash_robo);
        cn1 = findViewById(R.id.content);
        cn2 = findViewById(R.id.content1);
        rwth = findViewById(R.id.rwth);
        wel = findViewById(R.id.welcome);
        dev = findViewById(R.id.devlopedby);

        wel.setAnimation(topanim);
        robo.setAnimation(topanim);
        logo.setAnimation(topanim);
        cn1.setAnimation(botanim);
        cn2.setAnimation(botanim);
        rwth.setAnimation(botanim);
        dev.setAnimation(botanim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //  startActivity( new Intent(MainActivity.this, ShopSignup.class));
                //Initialize SessionManager
                managerAdmin = new SessionManagerAdmin(getApplicationContext());
                managerParticipant = new SessionManagerParticipant(getApplicationContext());

                if (managerAdmin.getAdminLogin()){
                    startActivity(new Intent(getApplicationContext(), AdminDashboard.class));
                }
                else if(managerParticipant.getParticipantLogin())
                {
                    startActivity(new Intent(getApplicationContext(), UserDashBoard.class));
                }
                else {
                    Intent intent = new Intent(MainActivity.this, UsersSignUp.class);
                    startActivity(intent);
                }
                finish();

            }
        },SPLASH_TIMER);

    }
}