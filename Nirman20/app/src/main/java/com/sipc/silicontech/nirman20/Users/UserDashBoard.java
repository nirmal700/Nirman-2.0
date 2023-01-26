package com.sipc.silicontech.nirman20.Users;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.navigation.NavigationView;
import com.sipc.silicontech.nirman20.Admins.PublishResults;
import com.sipc.silicontech.nirman20.Admins.SessionManagerAdmin;
import com.sipc.silicontech.nirman20.Admins.TeamDetails;
import com.sipc.silicontech.nirman20.Evaluators.EvaluatorDashboard;
import com.sipc.silicontech.nirman20.Evaluators.EvaluatorReviewEvaluation;
import com.sipc.silicontech.nirman20.Evaluators.EvaluatorSignIn;
import com.sipc.silicontech.nirman20.R;
import com.sipc.silicontech.nirman20.Users.ToDoList.UserToDoList;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UserDashBoard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    static final float END_SCALE = 0.7f;

    String AES = "AES";
    String keyPass = "Nirman@2023-SIPC";
    MaterialCardView mGenQR,btn_RequestHelp,btn_TodoList,btn_FoodCoupouns;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    LinearLayout contentView;
    TextView user_Name,team_name,risk_level;
    View nav_headerView;

    TextView tv_date;
    TextClock tv_time;


    String phoneNo;
    String view_date= new SimpleDateFormat("d MMM yyyy", Locale.getDefault()).format(new Date());
    SessionManagerParticipant managerParticipant;
    SessionManagerAdmin managerAdmin;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_dashboard);
        LottieAnimationView lottieAnimationView1 = findViewById(R.id.drawer_btn);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        contentView = findViewById(R.id.linear_content);
        team_name = findViewById(R.id.team_name);
        user_Name = findViewById(R.id.participant_name);
        risk_level = findViewById(R.id.mRiskLevel);
        mGenQR = findViewById(R.id.btn_generateQR);
        btn_RequestHelp = findViewById(R.id.btn_RequestHelp);
        btn_TodoList = findViewById(R.id.btn_TodoList);
        btn_FoodCoupouns = findViewById(R.id.btn_FoodCoupouns);

        managerParticipant = new SessionManagerParticipant(getApplicationContext());
        managerAdmin = new SessionManagerAdmin(getApplicationContext());
        if (!isConnected(UserDashBoard.this)){
            showCustomDialog();
        }
        user_Name.setText(managerParticipant.getParticipantName().toString());
        team_name.setText(managerParticipant.getTeamName().toString());

        Menu menuNav = navigationView.getMenu();

        nav_headerView = navigationView.inflateHeaderView(R.layout.menu_header);
        navigationDrawer();

        lottieAnimationView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lottieAnimationView1.playAnimation();
                lottieAnimationView1.loop(true);

                if (drawerLayout.isDrawerVisible(GravityCompat.START))
                    drawerLayout.closeDrawer(GravityCompat.START);
                else drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        mGenQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), UserQrCode.class));
            }
        });
        btn_RequestHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Request_Help.class));
            }
        });
        btn_TodoList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), UserToDoList.class));
            }
        });
        btn_FoodCoupouns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Food_Coupouns.class));
            }
        });


    }

    // Navigation Drawer Functions
    private void navigationDrawer() {

        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);



        animateNavigationDrawer();
    }
    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);

        } else
            super.onBackPressed();
    }
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        switch (id){


            case R.id.nav_home:
                Toast.makeText(getApplicationContext(), "Home", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_contactUs:
                Toast.makeText(getApplicationContext(), "Contact Us", Toast.LENGTH_SHORT).show();
                contactUs();
                break;

            case R.id.nav_share:
                Toast.makeText(getApplicationContext(), "Share", Toast.LENGTH_SHORT).show();
                share();
                break;

            case R.id.nav_about:
                Toast.makeText(getApplicationContext(), "About", Toast.LENGTH_SHORT).show();
                about();
                break;

            case R.id.logout:
                logout();
                break;

            case R.id.exit:
                Toast.makeText(this, "Thank you :)", Toast.LENGTH_SHORT).show();
                finishAffinity();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }
    private void animateNavigationDrawer() {

        //Add any color or remove it to use the default one!
        //drawerLayout.setScrimColor(getResources().getColor(R.color.red));
        //To make it transparent use Color.Transparent in side setScrimColor();
        //drawerLayout.setScrimColor(Color.TRANSPARENT);
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                // Scale the View based on current slide offset
                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                contentView.setScaleX(offsetScale);
                contentView.setScaleY(offsetScale);

                // Translate the View, accounting for the scaled width
                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = contentView.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xOffset - xOffsetDiff;
                contentView.setTranslationX(xTranslation);
            }
        });
    }
    private void share() {

        try {

            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT,"QR Registry");
            i.putExtra(Intent.EXTRA_TEXT,"https://play.google.com/store/apps/details?id="+getApplicationContext().getPackageName());
            startActivity(Intent.createChooser(i,"Share With"));

        }catch (Exception e){
            Toast.makeText(this, "Unable to share this app.", Toast.LENGTH_SHORT).show();
        }


    }

    private void contactUs() {
        //startActivity(new Intent(getApplicationContext(), ContactUs.class));
    }

    private void about() {
        //startActivity(new Intent(getApplicationContext(), AboutQrRegistry.class));
    }
    private void logout() {



        //Initialize alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //Set Title
        builder.setTitle("Log out");

        //set Message
        builder.setMessage("Are you sure to Log out ?");

        //positive YES button
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                managerParticipant.setParticipantLogin(false);
                managerParticipant.setDetails("","","","","");

                managerAdmin.setAdminLogin(false);
                managerAdmin.setDetails("","","","","","");

                //activity.finishAffinity();
                dialog.dismiss();

                //Finish Activity
                startActivity(new Intent(getApplicationContext(), UsersSignUp.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                finish();
            }
        });

        //Negative NO button
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Dismiss Dialog
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }


    //--------------- Internet Error Dialog Box -----------
    private void showCustomDialog() {

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(UserDashBoard.this);
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
                        startActivity(new Intent(getApplicationContext(),UserDashBoard.class));
                        finish();
                    }
                });
        android.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    //--------------- Check Internet Is Connected -----------
    private boolean isConnected(UserDashBoard userDashBoard) {

        ConnectivityManager connectivityManager = (ConnectivityManager) userDashBoard.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo bluetoothConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_BLUETOOTH);

        return (wifiConn != null && wifiConn.isConnected()) || (mobileConn != null && mobileConn.isConnected() || (bluetoothConn != null && bluetoothConn.isConnected())); // if true ,  else false

    }
}