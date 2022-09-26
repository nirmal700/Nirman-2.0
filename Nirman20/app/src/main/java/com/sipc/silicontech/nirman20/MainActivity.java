package com.sipc.silicontech.nirman20;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.sipc.silicontech.nirman20.Admins.AdminPhoneVerification;
import com.sipc.silicontech.nirman20.Admins.AdminSignin;

public class MainActivity extends AppCompatActivity {
    Animation topanim, botanim;
    ImageView logo;
    View robo;
    TextView cn1, cn2, rwth, wel, dev;

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

        startActivity(new Intent(getApplicationContext(), AdminSignin.class));
        finish();

    }
}