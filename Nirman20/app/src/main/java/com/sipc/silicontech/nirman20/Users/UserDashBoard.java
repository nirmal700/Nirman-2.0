package com.sipc.silicontech.nirman20.Users;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.sipc.silicontech.nirman20.AboutUs;
import com.sipc.silicontech.nirman20.Admins.NewHackNationTeamData;
import com.sipc.silicontech.nirman20.Admins.NewIdeateTeamData;
import com.sipc.silicontech.nirman20.Admins.NewLineFollowerTeamData;
import com.sipc.silicontech.nirman20.Admins.NewRoboRaceTeamData;
import com.sipc.silicontech.nirman20.QRCodeScanner;
import com.sipc.silicontech.nirman20.R;
import com.sipc.silicontech.nirman20.Users.ToDoList.UserToDoList;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class UserDashBoard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    static final float END_SCALE = 0.7f;

    String AES = "AES";
    String keyPass = "Nirman@2023-SIPC";
    MaterialCardView mGenQR, btn_RequestHelp, btn_TodoList, btn_Suggestion, btn_RateCoParticipant,btn_LeaderBoard;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    LinearLayout contentView;
    TextView user_Name, team_name, risk_level;
    View nav_headerView;
    ProgressDialog progressDialog;
    RelativeLayout mRelative;
    LottieAnimationView lottieAnimationView;
    int mPos, mSize;

    TextView tv_date;
    TextClock tv_time;


    String phoneNo;
    String view_date = new SimpleDateFormat("d MMM yyyy", Locale.getDefault()).format(new Date());
    SessionManagerParticipant managerParticipant;
    NewRoboRaceTeamData frmDb;
    private ArrayList<NewRoboRaceTeamData> roboRaceTeamData;
    private ArrayList<NewHackNationTeamData> hackNationTeamData;
    private ArrayList<NewIdeateTeamData> ideateTeamData;
    private ArrayList<NewLineFollowerTeamData> lineFollowerTeamData;

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
        btn_Suggestion = findViewById(R.id.btn_Suggestion);
        btn_LeaderBoard = findViewById(R.id.btn_LeaderBoard);
        btn_RateCoParticipant = findViewById(R.id.btn_RateCoParticipant);
        mRelative = findViewById(R.id.mRealtive);
        lottieAnimationView = findViewById(R.id.splash_robo);


        roboRaceTeamData = new ArrayList<>();
        hackNationTeamData = new ArrayList<>();
        lineFollowerTeamData = new ArrayList<>();
        ideateTeamData = new ArrayList<>();

        progressDialog = new ProgressDialog(UserDashBoard.this);
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.dismiss();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);


        managerParticipant = new SessionManagerParticipant(getApplicationContext());
        if (!isConnected(UserDashBoard.this)) {
            showCustomDialog();
        }
        user_Name.setText(managerParticipant.getParticipantName().toString());
        team_name.setText(managerParticipant.getTeamName().toString());

        Menu menuNav = navigationView.getMenu();

        nav_headerView = navigationView.inflateHeaderView(R.layout.menu_header);
        navigationDrawer();

        lottieAnimationView1.setOnClickListener(v -> {

            lottieAnimationView1.playAnimation();
            lottieAnimationView1.loop(true);

            if (drawerLayout.isDrawerVisible(GravityCompat.START))
                drawerLayout.closeDrawer(GravityCompat.START);
            else drawerLayout.openDrawer(GravityCompat.START);
        });

        mGenQR.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), UserQrCode.class));
            finish();
        });
        btn_RequestHelp.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), Request_Help.class));
            finish();
        });
        btn_TodoList.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), UserToDoList.class));
            finish();
        });
        btn_Suggestion.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), User_Suggestion.class));
            finish();
        });
        btn_RateCoParticipant.setOnClickListener(v -> {
            if (!isConnected(UserDashBoard.this)) {
                showCustomDialog();
            } else {
                scanCode();
            }
        });
        btn_LeaderBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UserDashBoard.this, "Not Enabled!!", Toast.LENGTH_SHORT).show();
            }
        });
        if (managerParticipant.getEventName().equals("Robo Race") | managerParticipant.getEventName().equals("Line Follower")) {
            if (managerParticipant.getEventName().equals("Robo Race")) {
                FirebaseFirestore.getInstance().collection(managerParticipant.getEventName())
                        .orderBy("mTotal", Query.Direction.DESCENDING)
                        .orderBy("mTotalTimeTaken", Query.Direction.ASCENDING).get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot documentSnapshot : snapshotList) {
                                NewRoboRaceTeamData mRoboRace = documentSnapshot.toObject(NewRoboRaceTeamData.class);
                                if (Objects.requireNonNull(mRoboRace).getmTotalTimeTaken() > 1) {
                                    roboRaceTeamData.add(mRoboRace);
                                }
                            }
                            mSize = roboRaceTeamData.size();
                            for (int i = 0; i < roboRaceTeamData.size(); i++) {
                                if (roboRaceTeamData.get(i).getmTeamName().equals(managerParticipant.getTeamName())) {
                                    mPos = i + 1;
                                    ChangeColor();
                                }
                            }
                        });
            } else if (managerParticipant.getEventName().equals("Line Follower")) {
                FirebaseFirestore.getInstance().collection(managerParticipant.getEventName())
                        .orderBy("mTotalTimeTaken", Query.Direction.ASCENDING)
                        .orderBy("mTotal", Query.Direction.DESCENDING).get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot documentSnapshot : snapshotList) {
                                NewLineFollowerTeamData mLineFollower = documentSnapshot.toObject(NewLineFollowerTeamData.class);
                                if (Objects.requireNonNull(mLineFollower).getmTotalTimeTaken() > 1) {
                                    lineFollowerTeamData.add(mLineFollower);
                                }
                            }
                            mSize = lineFollowerTeamData.size();
                            for (int i = 0; i < lineFollowerTeamData.size(); i++) {
                                if (lineFollowerTeamData.get(i).getmTeamName().equals(managerParticipant.getTeamName())) {
                                    mPos = i + 1;
                                    ChangeColor();
                                }
                            }
                        });
            }
        } else if (managerParticipant.getEventName().equals("HackNation") | managerParticipant.getEventName().equals("Ideate - 1") | managerParticipant.getEventName().equals("Ideate - 2")) {
            FirebaseFirestore.getInstance().collection(managerParticipant.getEventName()).orderBy("mFinalMark", Query.Direction.DESCENDING).get().addOnSuccessListener(queryDocumentSnapshots -> {
                if (managerParticipant.getEventName().equals("HackNation")) {
                    List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot documentSnapshot : snapshotList) {
                        NewHackNationTeamData mHackNation = documentSnapshot.toObject(NewHackNationTeamData.class);
                        if (Objects.requireNonNull(mHackNation).getmFinalMark() > 1) {
                            hackNationTeamData.add(mHackNation);
                        }
                    }
                    mSize = hackNationTeamData.size();
                    for (int i = 0; i < hackNationTeamData.size(); i++) {
                        if (hackNationTeamData.get(i).getmTeamName().equals(managerParticipant.getTeamName())) {
                            mPos = i + 1;
                            ChangeColor();
                        }
                    }
                } else if (managerParticipant.getEventName().equals("Ideate - 1")) {
                    List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot documentSnapshot : snapshotList) {
                        NewIdeateTeamData mIdeate = documentSnapshot.toObject(NewIdeateTeamData.class);
                        if (Objects.requireNonNull(mIdeate).getmFinalMark() > 1) {
                            ideateTeamData.add(mIdeate);
                        }
                    }
                    mSize = ideateTeamData.size();
                    for (int i = 0; i < ideateTeamData.size(); i++) {
                        if (ideateTeamData.get(i).getmTeamName().equals(managerParticipant.getTeamName())) {
                            mPos = i + 1;
                            ChangeColor();
                        }
                    }
                } else if (managerParticipant.getEventName().equals("Ideate - 2")) {
                    List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot documentSnapshot : snapshotList) {
                        NewIdeateTeamData mIdeate = documentSnapshot.toObject(NewIdeateTeamData.class);
                        if (Objects.requireNonNull(mIdeate).getmFinalMark() > 1) {
                            ideateTeamData.add(mIdeate);
                        }
                    }
                    mSize = ideateTeamData.size();
                    for (int i = 0; i < ideateTeamData.size(); i++) {
                        if (ideateTeamData.get(i).getmTeamName().equals(managerParticipant.getTeamName())) {
                            mPos = i + 1;
                            ChangeColor();
                        }
                    }
                }

            });


        }

    }

    private void ChangeColor() {
        Log.e("343535", "ChangeColor: " + mPos);
        double mMid = Double.parseDouble(String.valueOf(Math.ceil(mSize / 3)));
        if (mMid == 0) {
            mMid = 1;
        }
        if (mPos >= 0 & mPos <= mMid) {
            Drawable drawable = ContextCompat.getDrawable(this, R.drawable.bg_primary);
            mRelative.setBackground(drawable);
            lottieAnimationView.setAnimation("med.json");
            lottieAnimationView.playAnimation();
            lottieAnimationView.loop(true);
            risk_level.setText("Low");
        } else if (mPos > mMid & mPos <= 2 * mMid) {
            Drawable drawable = ContextCompat.getDrawable(this, R.drawable.bg_primary_green);
            mRelative.setBackground(drawable);
            lottieAnimationView.setAnimation("ic_man_search.json");
            lottieAnimationView.playAnimation();
            lottieAnimationView.loop(true);
            risk_level.setText("Medium");
        } else if (mPos > 2 * mMid) {
            Drawable drawable = ContextCompat.getDrawable(this, R.drawable.bg_primary_red);
            mRelative.setBackground(drawable);
            lottieAnimationView.setAnimation("ic_weight_lifting.json");
            lottieAnimationView.playAnimation();
            lottieAnimationView.loop(true);
            risk_level.setText("High");
        }
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

        switch (id) {


            case R.id.nav_home:
                Toast.makeText(getApplicationContext(), "Home", Toast.LENGTH_SHORT).show();
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

                case R.id.nav_feedback:
                feedback();
                break;


            case R.id.exit:
                Toast.makeText(this, "Thank you :)", Toast.LENGTH_SHORT).show();
                finishAffinity();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    private void feedback() {
        try {

            String url = "https://forms.gle/JyLYKhLcgBodQRoj6";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);

        } catch (Exception e) {
            Toast.makeText(this, "Unable to Give Feedback.", Toast.LENGTH_SHORT).show();
        }


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
            i.putExtra(Intent.EXTRA_SUBJECT, "Nirman 2.0");
            i.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName());
            startActivity(Intent.createChooser(i, "Share With"));

        } catch (Exception e) {
            Toast.makeText(this, "Unable to share this app.", Toast.LENGTH_SHORT).show();
        }


    }

    private void about() {
        startActivity(new Intent(getApplicationContext(), AboutUs.class));
    }

    private void logout() {


        //Initialize alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //Set Title
        builder.setTitle("Log out");

        //set Message
        builder.setMessage("Are you sure to Log out ?");

        //positive YES button
        builder.setPositiveButton("YES", (dialog, which) -> {

            managerParticipant.setParticipantLogin(false);
            managerParticipant.setDetails("", "", "", "", "");

            //activity.finishAffinity();
            dialog.dismiss();

            //Finish Activity
            startActivity(new Intent(getApplicationContext(), UsersSignUp.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            finish();
        });

        //Negative NO button
        builder.setNegativeButton("NO", (dialog, which) -> {
            //Dismiss Dialog
            dialog.dismiss();
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void scanCode() {
        IntentIntegrator intentIntegrator = new IntentIntegrator(UserDashBoard.this); //Initialize intent integrator

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
                    String event = separateData[0];
                    String name = separateData[1];
                    String teamname = separateData[2];
                    progressDialog.show();
                    if (managerParticipant.getEventName().equals(event)) {
                        if (!(managerParticipant.getTeamName().equals(teamname))) {
                            progressDialog.dismiss();
                            Intent mCoPart = new Intent(UserDashBoard.this, ReviewTeam.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            mCoPart.putExtra("mTeamName", teamname);
                            startActivity(mCoPart);
                            finish();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(this, "Can't Review Self!!", Toast.LENGTH_SHORT).show();
                        }

                    }else {
                        progressDialog.dismiss();
                        Toast.makeText(this, "Can't Review Other Event Participant's", Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    AlertDialog.Builder builder = new AlertDialog.Builder(UserDashBoard.this);
                    builder.setMessage("Wrong QR Code");
                    builder.setPositiveButton("Scan Again", (dialog, which) -> scanCode());
                    builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
                    builder.show();
                }
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(UserDashBoard.this);
                builder.setMessage("Wrong QR Code");
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

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(UserDashBoard.this);
        builder.setMessage("Please connect to the internet")
                //.setCancelable(false)
                .setPositiveButton("Connect", (dialog, which) -> startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS)))
                .setNegativeButton("Cancel", (dialog, which) -> {
                    startActivity(new Intent(getApplicationContext(), UserDashBoard.class));
                    finish();
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