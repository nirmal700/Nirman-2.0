package com.sipc.silicontech.nirman20.Admins;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
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


public class FoodCoupoun extends AppCompatActivity {
    String AES = "AES";
    String keyPass = "Nirman@2023-SIPC";
    private CollectionReference mCollectionReference;
    String name,teamname,event;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_coupoun);
        progressDialog = new ProgressDialog(FoodCoupoun.this);
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.dismiss();

        if (!isConnected(FoodCoupoun.this)) {
            showCustomDialog();
        } else {
            scanCode();
        }
    }
    private void scanCode() {
        IntentIntegrator intentIntegrator = new IntentIntegrator(FoodCoupoun.this); //Initialize intent integrator

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
                    String phone = separateData[3];
                    progressDialog.show();
                    Query checkUser = FirebaseDatabase.getInstance().getReference("Fooding").child(event).orderByChild("mPhoneNumber").equalTo(phone);
                    checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                progressDialog.dismiss();
                                AlertDialog.Builder builder = new AlertDialog.Builder(FoodCoupoun.this);
                                builder.setMessage("Participant Already Availed Food ");
                                builder.setPositiveButton("Scan Again", (dialog, which) -> scanCode());
                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        startActivity(new Intent(FoodCoupoun.this, AdminDashboard.class));
                                    }
                                });
                                builder.show();
                                Toast.makeText(FoodCoupoun.this, "This User already Availed Food", Toast.LENGTH_LONG).show();
                            }
                            else {
                                progressDialog.dismiss();
                                Food_POJO foodCoupoun = new Food_POJO(name,phone,teamname,true);
                                FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
                                DatabaseReference reference = rootNode.getReference("Fooding").child(event);
                                reference.child(phone).child("Profile").setValue(foodCoupoun);
                                reference.child(phone).child("mTeamName").setValue(teamname);
                                reference.child(phone).child("mPhoneNumber").setValue(phone);
                                AlertDialog.Builder builder = new AlertDialog.Builder(FoodCoupoun.this);

                                //Set Title
                                builder.setTitle("Result");

                                //Set Message
                                builder.setMessage("Read Successfully"+"\n"+"Team Name: "+teamname+"\n"+"Participant Name: "+name+"\n"+"Event Name: "+event+"\n");

                                //set Positive Button
                                builder.setPositiveButton("Scan Again", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        scanCode();
                                    }
                                }).setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        startActivity(new Intent(FoodCoupoun.this, AdminDashboard.class));
                                    }
                                });

                                //Show Alert Dialog
                                builder.show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });




                } catch (Exception e) {
                    e.printStackTrace();
                    AlertDialog.Builder builder = new AlertDialog.Builder(FoodCoupoun.this);
                    builder.setMessage("Wrong QR Code1");
                    Log.e("2324", "onActivityResult: "+e.toString() );
                    builder.setPositiveButton("Scan Again", (dialog, which) -> scanCode());
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            startActivity(new Intent(FoodCoupoun.this, AdminDashboard.class));
                        }
                    });
                    builder.show();
                }
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(FoodCoupoun.this);
                builder.setMessage("Wrong QR Code2");
                builder.setPositiveButton("Scan Again", (dialog, which) -> scanCode());
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        startActivity(new Intent(FoodCoupoun.this, AdminDashboard.class));
                    }
                });
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

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(FoodCoupoun.this);
        builder.setMessage("Please connect to the internet")
                //.setCancelable(false)
                .setPositiveButton("Connect", (dialog, which) -> startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS)))
                .setNegativeButton("Cancel", (dialog, which) -> {
                    startActivity(new Intent(getApplicationContext(), AdminDashboard.class));
                    finish();
                });
        android.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    //--------------- Check Internet Is Connected -----------
    private boolean isConnected(FoodCoupoun adminDashboard) {

        ConnectivityManager connectivityManager = (ConnectivityManager) adminDashboard.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo bluetoothConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_BLUETOOTH);

        return (wifiConn != null && wifiConn.isConnected()) || (mobileConn != null && mobileConn.isConnected() || (bluetoothConn != null && bluetoothConn.isConnected())); // if true ,  else false

    }
}