package com.sipc.silicontech.nirman20;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class AboutUs extends AppCompatActivity {
    ImageView to_gmail,to_twitter,to_instagram,to_gitHub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        to_gitHub = findViewById(R.id.to_gitHub);
        to_twitter = findViewById(R.id.to_twitter);
        to_gmail = findViewById(R.id.to_gMail);
        to_instagram = findViewById(R.id.to_instagram);
        to_instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri uri = Uri.parse("https://instagram.com/nirman_silicon?utm_medium=copy_link");
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);
                likeIng.setPackage("com.instagram.android");

                try {
                    startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://instagram.com/nirman_silicon?utm_medium=copy_link")));
                }
                Toast.makeText(AboutUs.this, "Contact Team Nirman via Instagram", Toast.LENGTH_SHORT).show();

            }
        });

        to_gmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent emailIntent = new Intent(Intent.ACTION_VIEW);
                Uri data = Uri.parse("mailto:?subject=" +"Contact Team Nirman 2.0"+ "&body=" + "Hi Team Nirman,\n" + "&to=" + "nirman@silicon.ac.in");
                emailIntent.setData(data);
                startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                Toast.makeText(AboutUs.this, "Contact Team Nirman via E-mail", Toast.LENGTH_SHORT).show();

            }
        });

        to_gitHub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = "https://github.com/nirmal700";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                Toast.makeText(AboutUs.this, "Contact Team Nirman via GitHub", Toast.LENGTH_SHORT).show();

            }
        });
        to_twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = "https://twitter.com/nirman_silicon";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                Toast.makeText(AboutUs.this, "Contact Team Nirman via GitHub", Toast.LENGTH_SHORT).show();

            }
        });

    }
}