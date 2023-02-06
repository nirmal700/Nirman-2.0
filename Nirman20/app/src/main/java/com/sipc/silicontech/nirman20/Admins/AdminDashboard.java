package com.sipc.silicontech.nirman20.Admins;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.sipc.silicontech.nirman20.Evaluators.EvaluatorDashboard;
import com.sipc.silicontech.nirman20.QRCodeScanner;
import com.sipc.silicontech.nirman20.R;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AdminDashboard extends AppCompatActivity {

    TextView mUserRole, mName;
    SessionManagerAdmin managerAdmin;
    String AES = "AES";
    String keyPass = "Nirman@2023-SIPC";
    Boolean mCb1 = false, mCb2= false, mCb3= false, mCb4= false;
    Button btCancel, btOk;
    CheckBox checkBox1, checkBox2, checkBox3, checkBox4;

    MaterialCardView mAddVolunteer, mAddNewTeams, mCheckIn, btn_ViewTeamDetails,mFoodCoupoun;
    Dialog dialog;
    private CollectionReference mCollectionReference;
    String name,teamname,event;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_dashboard);
        mUserRole = findViewById(R.id.UserRole);
        mName = findViewById(R.id.User_name);
        mAddVolunteer = findViewById(R.id.btn_AddVolunteers);
        mAddNewTeams = findViewById(R.id.btn_AddNewTeams);
        btn_ViewTeamDetails = findViewById(R.id.btn_ViewTeams);
        mCheckIn = findViewById(R.id.btn_CheckIn);
        mFoodCoupoun  = findViewById(R.id.btn_FoodCoupouns);

        mFoodCoupoun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminDashboard.this, FoodCoupoun.class));
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

        mCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isConnected(AdminDashboard.this)) {
                    showCustomDialog();
                } else {
                    scanCode();
                }
            }
        });


        managerAdmin = new SessionManagerAdmin(getApplicationContext());
        mUserRole.setText(managerAdmin.getUserRole());
        mName.setText(managerAdmin.getName());

        mAddVolunteer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminDashboard.this, AddVolunteers.class));
            }
        });
        mAddNewTeams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminDashboard.this, AddNewTeams.class));
            }
        });
        btn_ViewTeamDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminDashboard.this, TeamDetails.class));
            }
        });


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
                    mCollectionReference.document(teamname).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if(Boolean.FALSE.equals(documentSnapshot.getBoolean("mCheckedIn"))){
                                progressDialog.dismiss();
                                dialog.show();
                            }
                            else {
                                progressDialog.dismiss();
                                Toast.makeText(AdminDashboard.this, "Already Checked-In", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    
                    btCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.cancel();
                        }
                    });
                    btOk.setOnClickListener(v1 -> {
                        dialog.dismiss();
                    });
                    checkBox2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mCb2 = checkBox2.isChecked();
                            CheckIfAllChecked();
                        }
                    });
                    checkBox3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mCb3 = checkBox3.isChecked();
                            CheckIfAllChecked();
                        }
                    });
                    checkBox4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mCb4 = checkBox4.isChecked();
                            CheckIfAllChecked();
                        }
                    });
                    checkBox1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mCb1 = checkBox1.isChecked();
                            CheckIfAllChecked();
                        }
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
        Log.e("2342", "CheckIfAllChecked: "+mCb1+" "+mCb2+" "+mCb3+" "+mCb4 );
        if (mCb1 & mCb2 & mCb3 & mCb4) {
            btOk.setBackgroundColor(getResources().getColor(R.color.light_green));
            btOk.setEnabled(true);
            btOk.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    mCollectionReference = FirebaseFirestore.getInstance().collection(event);
                    progressDialog.show();
                    mCollectionReference.document(teamname).update("mCheckedIn", true).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(AdminDashboard.this, "Success!", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            dialog.dismiss();
                            checkBox1.setChecked(false);
                            checkBox2.setChecked(false);
                            checkBox3.setChecked(false);
                            checkBox4.setChecked(false);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AdminDashboard.this, e.toString(), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
                }
            });
        } else {
            btOk.setEnabled(false);
            btOk.setBackgroundColor(getResources().getColor(com.google.android.gms.base.R.color.common_google_signin_btn_text_dark_disabled));
        }

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
                .setPositiveButton("Connect", (dialog, which) -> startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS)))
                .setNegativeButton("Cancel", (dialog, which) -> {
                    startActivity(new Intent(getApplicationContext(), EvaluatorDashboard.class));
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
}