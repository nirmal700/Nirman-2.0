package com.sipc.silicontech.nirman20.Admins;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.sipc.silicontech.nirman20.Evaluators.EvaluatorDashboard;
import com.sipc.silicontech.nirman20.QRCodeScanner;
import com.sipc.silicontech.nirman20.R;
import com.sipc.silicontech.nirman20.Users.UsersSignUp;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AdminDashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    static final float END_SCALE = 0.7f;
    TextView mUserRole, mName;
    SessionManagerAdmin managerAdmin;
    String AES = "AES";
    String keyPass = "Nirman@2023-SIPC";
    Boolean mCb1 = false, mCb2 = false, mCb3 = false, mCb4 = false;
    Button btCancel, btOk;
    CheckBox checkBox1, checkBox2, checkBox3, checkBox4;
    MaterialCardView mAddVolunteer, mAddNewTeams, mCheckIn, btn_ViewTeamDetails, mFoodCoupoun, btn_AddressIssues;
    Dialog dialog;
    String name, teamname, event;
    ProgressDialog progressDialog;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    LinearLayout contentView;
    View nav_headerView;
    private CollectionReference mCollectionReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_dashboard);
        mUserRole = findViewById(R.id.UserRole);
        LottieAnimationView lottieAnimationView1 = findViewById(R.id.drawer_btn);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        contentView = findViewById(R.id.linear_content);
        mName = findViewById(R.id.User_name);
        mAddVolunteer = findViewById(R.id.btn_AddVolunteers);
        mAddNewTeams = findViewById(R.id.btn_AddNewTeams);
        btn_ViewTeamDetails = findViewById(R.id.btn_ViewTeams);
        mCheckIn = findViewById(R.id.btn_CheckIn);
        mFoodCoupoun = findViewById(R.id.btn_FoodCoupouns);
        btn_AddressIssues = findViewById(R.id.btn_AddressIssues);

        //--------------- Internet Checking -----------
        if (!isConnected(AdminDashboard.this)) {
            showCustomDialog();
        }


        mFoodCoupoun.setOnClickListener(v -> {
            startActivity(new Intent(AdminDashboard.this, FoodCoupoun.class));
            finish();
        });

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


        progressDialog = new ProgressDialog(AdminDashboard.this);
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.dismiss();

        dialog = new Dialog(AdminDashboard.this);
        dialog.setContentView(R.layout.id_alert_dialog);
        checkBox1 = dialog.findViewById(R.id.check_box1);
        checkBox2 = dialog.findViewById(R.id.check_box2);
        checkBox3 = dialog.findViewById(R.id.check_box3);
        checkBox4 = dialog.findViewById(R.id.check_box4);
        btCancel = dialog.findViewById(R.id.bt_cancel);
        btOk = dialog.findViewById(R.id.bt_ok);

        mCheckIn.setOnClickListener(v -> {
            if (!isConnected(AdminDashboard.this)) {
                showCustomDialog();
            } else {
                scanCode();
            }
        });


        managerAdmin = new SessionManagerAdmin(getApplicationContext());
        mUserRole.setText(managerAdmin.getUserRole());
        mName.setText(managerAdmin.getName());

        mAddVolunteer.setOnClickListener(view -> {
            startActivity(new Intent(AdminDashboard.this, AddVolunteers.class));
            finish();
        });
        btn_AddressIssues.setOnClickListener(v -> {
            startActivity(new Intent(AdminDashboard.this, AddressIssues.class));
            finish();
        });
        mAddNewTeams.setOnClickListener(view -> {
            startActivity(new Intent(AdminDashboard.this, AddNewTeams.class));
            finish();
        });
        btn_ViewTeamDetails.setOnClickListener(view -> {
            startActivity(new Intent(AdminDashboard.this, TeamDetails.class));
            finish();
        });


    }

    private void navigationDrawer() {
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);


        animateNavigationDrawer();
    }



    private void scanCode() {
        IntentIntegrator intentIntegrator = new IntentIntegrator(AdminDashboard.this); //Initialize intent integrator

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
                    event = separateData[0];
                    name = separateData[1];
                    teamname = separateData[2];
                    progressDialog.show();
                    mCollectionReference = FirebaseFirestore.getInstance().collection(event);
                    mCollectionReference.document(teamname).get().addOnSuccessListener(documentSnapshot -> {
                        if (Boolean.FALSE.equals(documentSnapshot.getBoolean("mCheckedIn"))) {
                            progressDialog.dismiss();
                            dialog.show();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(AdminDashboard.this, "Already Checked-In", Toast.LENGTH_SHORT).show();
                        }
                    });

                    btCancel.setOnClickListener(v -> dialog.cancel());
                    btOk.setOnClickListener(v1 -> dialog.dismiss());
                    checkBox2.setOnClickListener(v -> {
                        mCb2 = checkBox2.isChecked();
                        CheckIfAllChecked();
                    });
                    checkBox3.setOnClickListener(v -> {
                        mCb3 = checkBox3.isChecked();
                        CheckIfAllChecked();
                    });
                    checkBox4.setOnClickListener(v -> {
                        mCb4 = checkBox4.isChecked();
                        CheckIfAllChecked();
                    });
                    checkBox1.setOnClickListener(v -> {
                        mCb1 = checkBox1.isChecked();
                        CheckIfAllChecked();
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    AlertDialog.Builder builder = new AlertDialog.Builder(AdminDashboard.this);
                    builder.setMessage("Wrong QR Code");
                    builder.setPositiveButton("Scan Again", (dialog, which) -> scanCode());
                    builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
                    builder.show();
                }
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminDashboard.this);
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

    private void CheckIfAllChecked() {
        if (mCb1 & mCb2 & mCb3 & mCb4) {
            btOk.setBackgroundColor(getResources().getColor(R.color.light_green));
            btOk.setEnabled(true);
            btOk.setOnClickListener(v -> {
                mCollectionReference = FirebaseFirestore.getInstance().collection(event);
                progressDialog.show();
                mCollectionReference.document(teamname).update("mCheckedIn", true).addOnSuccessListener(unused -> {
                    Toast.makeText(AdminDashboard.this, "Success!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    dialog.dismiss();
                    checkBox1.setChecked(false);
                    checkBox2.setChecked(false);
                    checkBox3.setChecked(false);
                    checkBox4.setChecked(false);
                }).addOnFailureListener(e -> {
                    Toast.makeText(AdminDashboard.this, e.toString(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    dialog.dismiss();
                });
            });
        } else {
            btOk.setEnabled(false);
            btOk.setBackgroundColor(getResources().getColor(com.google.android.gms.base.R.color.common_google_signin_btn_text_dark_disabled));
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

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(AdminDashboard.this);
        builder.setMessage("Please connect to the internet")
                //.setCancelable(false)
                .setPositiveButton("Connect", (dialog, which) -> startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS))).setNegativeButton("Cancel", (dialog, which) -> {
                    startActivity(new Intent(getApplicationContext(), AdminDashboard.class));
                    finish();
                });
        android.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    //--------------- Check Internet Is Connected -----------
    private boolean isConnected(AdminDashboard adminDashboard) {

        ConnectivityManager connectivityManager = (ConnectivityManager) adminDashboard.getSystemService(Context.CONNECTIVITY_SERVICE);

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
                share();
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

    private void logout() {

        //Initialize alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        SessionManagerAdmin sessionManagerAdmin = new SessionManagerAdmin(AdminDashboard.this);

        //Set Title
        builder.setTitle("Log out");

        //set Message
        builder.setMessage("Are you sure to Log out ?");

        //positive YES button
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                sessionManagerAdmin.setAdminLogin(false);
                sessionManagerAdmin.setDetails("", "", "", "", "","");

                //activity.finishAffinity();
                dialog.dismiss();

                //Finish Activity
                startActivity(new Intent(getApplicationContext(), AdminSignin.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
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


}