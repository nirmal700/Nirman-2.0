package com.sipc.silicontech.nirman20.Evaluators;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.sipc.silicontech.nirman20.Admins.NewHackNationTeamData;
import com.sipc.silicontech.nirman20.Admins.NewIdeateTeamData;
import com.sipc.silicontech.nirman20.Admins.NewLineFollowerTeamData;
import com.sipc.silicontech.nirman20.Admins.NewRoboRaceTeamData;
import com.sipc.silicontech.nirman20.QRCodeScanner;
import com.sipc.silicontech.nirman20.R;
import com.sipc.silicontech.nirman20.Users.UserDashBoard;
import com.sipc.silicontech.nirman20.Users.UsersSignUp;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Objects;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class EvaluatorDashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    MaterialCardView btn_evaluate, btn_demo;
    static final float END_SCALE = 0.7f;
    String AES = "AES";
    String keyPass = "Nirman@2023-SIPC";
    CollectionReference mCollectionReference;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    LinearLayout contentView;
    View nav_headerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.evaluator_dashboard);

        btn_evaluate = findViewById(R.id.btn_evaluate);
        btn_demo = findViewById(R.id.btn_FoodCoupouns);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        contentView = findViewById(R.id.linear_content);
        LottieAnimationView lottieAnimationView1 = findViewById(R.id.drawer_btn);
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

        btn_demo.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), DownloadResults.class)));


        btn_evaluate.setOnClickListener(view -> {
            if (!isConnected(EvaluatorDashboard.this)) {
                showCustomDialog();
            } else {
                scanCode();
            }
        });
    }

    private void navigationDrawer() {
        // Navigation Drawer Functions


            navigationView.bringToFront();
            navigationView.setNavigationItemSelectedListener(this);
            navigationView.setCheckedItem(R.id.nav_home);


            animateNavigationDrawer();

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

    private void scanCode() {
        IntentIntegrator intentIntegrator = new IntentIntegrator(EvaluatorDashboard.this); //Initialize intent integrator

        intentIntegrator.setPrompt("For Flash Use Volume Up Key");  //Set Prompt text
        intentIntegrator.setBeepEnabled(true);  //set beep
        intentIntegrator.setCameraId(0);  //set Camera
        intentIntegrator.setOrientationLocked(true);  //Locked Orientation
        intentIntegrator.setCaptureActivity(QRCodeScanner.class);  //Set Capture Activity
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        intentIntegrator.initiateScan();  //Initiate Scan
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Initiate Intent Result
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        //Check Condition
        if (intentResult.getContents() != null && resultCode == RESULT_OK) {

            String output = intentResult.getContents();

            if (output.startsWith("Nirman 2.0")) {

                try {
                    String[] separated = output.split(":");

                    String userDetails = separated[1];

                    String decodedData = (String) decrypt(userDetails);

                    String[] separateData = decodedData.split(":");
                    Log.e("TAG", "onActivityResult: " + decodedData);
                    String event = separateData[0];
                    String name = separateData[1];
                    String teamname = separateData[2];
                    String phoneNo = separateData[3];
                    Log.e("7565", "onActivityResult: " + event + "" + teamname);
                    mCollectionReference = FirebaseFirestore.getInstance().collection(event);

                    if (event.equals("HackNation")) {
                        DocumentReference documentReference = mCollectionReference.document(teamname);
                        documentReference.get().addOnSuccessListener(documentSnapshot -> {
                            NewHackNationTeamData newHackNationTeamData = documentSnapshot.toObject(NewHackNationTeamData.class);
                            String clgname = Objects.requireNonNull(newHackNationTeamData).getmCollegeName();
                            String problemstat = newHackNationTeamData.getmProblemStat();
                            String approach = newHackNationTeamData.getmApproach();
                            Intent mEvaluatorIntent = new Intent(EvaluatorDashboard.this, HackNationEvaluation.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            mEvaluatorIntent.putExtra("mTeamName", teamname);
                            mEvaluatorIntent.putExtra("mCollegeName", clgname);
                            mEvaluatorIntent.putExtra("mProblemStat", problemstat);
                            mEvaluatorIntent.putExtra("mApproach", approach);
                            startActivity(mEvaluatorIntent);
                            finish();


                        }).addOnFailureListener(e -> Toast.makeText(EvaluatorDashboard.this, "Error!!" + e, Toast.LENGTH_SHORT).show());
                    } else if (event.equals("Ideate")) {
                        Log.e("3432", "onActivityResult: '" + teamname + "'");
                        DocumentReference documentReference = mCollectionReference.document(teamname);
                        documentReference.get().addOnSuccessListener(documentSnapshot -> {
                            NewIdeateTeamData newIdeateTeamData = documentSnapshot.toObject(NewIdeateTeamData.class);
                            String clgname = Objects.requireNonNull(newIdeateTeamData).getmCollegeName();
                            String problemstat = newIdeateTeamData.getmProblemStat();
                            String approach = newIdeateTeamData.getmApproach();
                            Intent mEvaluatorIntent = new Intent(EvaluatorDashboard.this, IdeateEvaluation.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            mEvaluatorIntent.putExtra("mTeamName", teamname);
                            mEvaluatorIntent.putExtra("mCollegeName", clgname);
                            mEvaluatorIntent.putExtra("mProblemStat", problemstat);
                            mEvaluatorIntent.putExtra("mApproach", approach);
                            startActivity(mEvaluatorIntent);
                            finish();
                        }).addOnFailureListener(e -> Toast.makeText(EvaluatorDashboard.this, "Error!!" + e, Toast.LENGTH_SHORT).show());
                    } else if (event.equals("Robo Race")) {
                        DocumentReference documentReference = mCollectionReference.document(teamname);
                        documentReference.get().addOnSuccessListener(documentSnapshot -> {
                            NewRoboRaceTeamData newRoboRaceTeamData = documentSnapshot.toObject(NewRoboRaceTeamData.class);
                            String clgname = Objects.requireNonNull(newRoboRaceTeamData).getmCollegeName();
                            Intent mEvaluatorIntent = new Intent(EvaluatorDashboard.this, RoboRaceEvaluation.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            mEvaluatorIntent.putExtra("mTeamName", teamname);
                            mEvaluatorIntent.putExtra("mCollegeName", clgname);
                            startActivity(mEvaluatorIntent);
                            finish();
                        }).addOnFailureListener(e -> Toast.makeText(EvaluatorDashboard.this, "Error!!" + e, Toast.LENGTH_SHORT).show());
                    } else {
                        DocumentReference documentReference = mCollectionReference.document(teamname);
                        documentReference.get().addOnSuccessListener(documentSnapshot -> {
                            NewLineFollowerTeamData newLineFollowerTeamData = documentSnapshot.toObject(NewLineFollowerTeamData.class);
                            String clgname = Objects.requireNonNull(newLineFollowerTeamData).getmCollegeName();
                            Intent mEvaluatorIntent = new Intent(EvaluatorDashboard.this, LineFollowerEvaluation.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            mEvaluatorIntent.putExtra("mTeamName", teamname);
                            mEvaluatorIntent.putExtra("mCollegeName", clgname);
                            startActivity(mEvaluatorIntent);
                            finish();
                        }).addOnFailureListener(e -> Toast.makeText(EvaluatorDashboard.this, "Error!!" + e, Toast.LENGTH_SHORT).show());
                    }
                    //Initialize Dialog box
                    AlertDialog.Builder builder = new AlertDialog.Builder(EvaluatorDashboard.this);

                    builder.setTitle("Result"); //Set Title
                    builder.setMessage("Read Successfully!!");  //Set Message

                    //set Positive Button
                    builder.setPositiveButton("Scan Again", (dialog, which) -> scanCode()).setNegativeButton("OK", (dialog, which) -> dialog.dismiss());

                    builder.show();  //Show Alert Dialog
                } catch (Exception e) {
                    e.printStackTrace();
                    AlertDialog.Builder builder = new AlertDialog.Builder(EvaluatorDashboard.this);
                    builder.setMessage("Wrong QR Code1");
                    builder.setPositiveButton("Scan Again", (dialog, which) -> scanCode());
                    builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
                    builder.show();
                }
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(EvaluatorDashboard.this);
                builder.setMessage("Wrong QR Code2");
                builder.setPositiveButton("Scan Again", (dialog, which) -> scanCode());
                builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
                builder.show();
            }
        } else {

            Toast.makeText(getApplicationContext(), "You did not scan anything", Toast.LENGTH_SHORT).show();

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private Object decrypt(String userDetails) throws Exception {

        SecretKeySpec key = generateKey(keyPass);
        @SuppressLint("GetInstance") Cipher c = Cipher.getInstance(AES);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decodeValue = android.util.Base64.decode(userDetails, android.util.Base64.DEFAULT);
        byte[] decValue = c.doFinal(decodeValue);
        return new String(decValue);

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private SecretKeySpec generateKey(String keyPass) throws Exception {
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = keyPass.getBytes(StandardCharsets.UTF_8);
        digest.update(bytes, 0, bytes.length);
        byte[] key = digest.digest();
        return new SecretKeySpec(key, "AES");
    }

    //--------------- Internet Error Dialog Box -----------
    private void showCustomDialog() {

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(EvaluatorDashboard.this);
        builder.setMessage("Please connect to the internet")
                //.setCancelable(false)
                .setPositiveButton("Connect", (dialog, which) -> startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS))).setNegativeButton("Cancel", (dialog, which) -> {
                    startActivity(new Intent(getApplicationContext(), EvaluatorDashboard.class));
                    finish();
                });
        android.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    //--------------- Check Internet Is Connected -----------
    private boolean isConnected(EvaluatorDashboard userDashBoard) {

        ConnectivityManager connectivityManager = (ConnectivityManager) userDashBoard.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo bluetoothConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_BLUETOOTH);

        return (wifiConn != null && wifiConn.isConnected()) || (mobileConn != null && mobileConn.isConnected() || (bluetoothConn != null && bluetoothConn.isConnected())); // if true ,  else false

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {


            case R.id.nav_home:
                Toast.makeText(getApplicationContext(), "Home", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_contactUs:
                Toast.makeText(getApplicationContext(), "Contact Us", Toast.LENGTH_SHORT).show();
//                contactUs();
                break;

            case R.id.nav_share:
                Toast.makeText(getApplicationContext(), "Share", Toast.LENGTH_SHORT).show();
//                share();
                break;

            case R.id.nav_about:
                Toast.makeText(getApplicationContext(), "About", Toast.LENGTH_SHORT).show();
//                about();
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

    private void logout() {

        SessionManagerEvaluator sessionManagerEvaluator = new SessionManagerEvaluator(EvaluatorDashboard.this);


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

                sessionManagerEvaluator.setEvaluatorLogin(false);
                sessionManagerEvaluator.setEvaluatorDetails("", "", "", "");

                //activity.finishAffinity();
                dialog.dismiss();

                //Finish Activity
                startActivity(new Intent(getApplicationContext(), EvaluatorSignIn.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
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
    @Override
    public void onBackPressed() {
        finishAffinity();
        super.onBackPressed();
    }
}