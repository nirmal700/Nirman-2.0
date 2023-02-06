package com.sipc.silicontech.nirman20;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.sipc.silicontech.nirman20.Admins.AdminDashboard;
import com.sipc.silicontech.nirman20.Admins.SessionManagerAdmin;
import com.sipc.silicontech.nirman20.Evaluators.EvaluatorDashboard;
import com.sipc.silicontech.nirman20.Evaluators.SessionManagerEvaluator;
import com.sipc.silicontech.nirman20.Users.SessionManagerParticipant;
import com.sipc.silicontech.nirman20.Users.UserDashBoard;
import com.sipc.silicontech.nirman20.Users.UsersSignUp;

public class MainActivity extends AppCompatActivity {
    private static final int SPLASH_TIMER = 3900;
    private static final int RC_APP_UPDATE = 895;
    Animation topanim, botanim;
    ImageView logo;
    View robo;
    TextView cn1, cn2, rwth, wel, dev;
    SessionManagerAdmin managerAdmin;
    SessionManagerParticipant managerParticipant;
    SessionManagerEvaluator managerEvaluator;

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
                managerEvaluator = new SessionManagerEvaluator(getApplicationContext());

                if (managerAdmin.getAdminLogin()) {
                    startActivity(new Intent(getApplicationContext(), AdminDashboard.class));
                } else if (managerParticipant.getParticipantLogin()) {
                    startActivity(new Intent(getApplicationContext(), UserDashBoard.class));
                } else if (managerEvaluator.getEvaluatorLogin()) {
                    startActivity(new Intent(getApplicationContext(), EvaluatorDashboard.class));
                } else {
                    Intent intent = new Intent(MainActivity.this, UsersSignUp.class);
                    startActivity(intent);
                }
                finish();

            }
        }, SPLASH_TIMER);

    }
}