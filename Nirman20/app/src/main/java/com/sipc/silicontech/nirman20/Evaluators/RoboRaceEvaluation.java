package com.sipc.silicontech.nirman20.Evaluators;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sipc.silicontech.nirman20.R;

import java.util.Locale;

public class RoboRaceEvaluation extends AppCompatActivity {
    Button start,stop,reset,inc1,dec1,inc2,dec2;
    TextView time,disp1,disp2;
    boolean run=false;
    Runnable r;
    int seconds,count1,count2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.robo_race_evaluation);
        start=findViewById(R.id.start);
        stop=findViewById(R.id.stop);
        reset=findViewById(R.id.reset);
        time=findViewById(R.id.time);
        inc1=findViewById(R.id.incrementer1);
        inc2=findViewById(R.id.incrementer2);
        dec1=findViewById(R.id.decrementer1);
        dec2=findViewById(R.id.decrementer2);
        disp1=findViewById(R.id.disp1);
        disp2=findViewById(R.id.disp2);
        final Handler handler = new Handler();
        handler.post(r = new Runnable() {
            @Override
            public void run() {
                int min = ((seconds/100)%3600)/60;
                int sec = (seconds/100)%60;
                int mil = (seconds)%100;
                String timeText = String.format(Locale.getDefault(),"%02d:%02d:%02d",min,sec,mil);
                time.setText(timeText);
                if (run) {
                    seconds++;
                }
                handler.postDelayed(this,10);
            }
        });
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start.setVisibility(view.INVISIBLE);
                stop.setVisibility(view.VISIBLE);
                time.setVisibility(view.VISIBLE);
                run = true;
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stop.setVisibility(view.INVISIBLE);
                start.setVisibility(view.VISIBLE);
                run=false;
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stop.setVisibility(view.INVISIBLE);
                start.setVisibility(view.VISIBLE);
                seconds=0;
                run= false;
            }
        });

        inc1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { 
                 count1++;
                 disp1.setText(String.format("%d", count1));
            }
        });
        dec1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count1<=0)
                    count1=0;
                else
                    count1--;
                disp1.setText(String.format("%d", count1));
            }
        });

        inc2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count2++;
                disp2.setText(String.format("%d", count2));
            }
        });
        dec2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count2<=0)
                    count2=0;
                else
                    count2--;
                disp2.setText(String.format("%d", count2));
            }
        });
    }
}