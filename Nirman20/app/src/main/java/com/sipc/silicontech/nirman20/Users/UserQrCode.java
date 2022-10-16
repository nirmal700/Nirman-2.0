package com.sipc.silicontech.nirman20.Users;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.sipc.silicontech.nirman20.Admins.AdminDashboard;
import com.sipc.silicontech.nirman20.R;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class UserQrCode extends AppCompatActivity {



    ImageView btn_backToCd,qr_output;
    ProgressDialog progressDialog;
    TextView team_name;

    //--------------- Encryption Variables -----------
    String AES = "AES";
    String keyPass = "Nirman@2023-SIPC";
    //------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_qr_code);

        qr_output = findViewById(R.id.qr_output);
        btn_backToCd  = findViewById(R.id.btn_backToCd);
        team_name = findViewById(R.id.mTeam_name);
        SessionManagerParticipant managerParticipant = new SessionManagerParticipant(getApplicationContext());
        String phoneNo = managerParticipant.getPhone(); //Get Phone Number from session

        //--------------- Internet Checking -----------
        if (!isConnected(UserQrCode.this)){
            showCustomDialog();
        }
        team_name.setText(managerParticipant.getTeamName().toString());

        //--------------- Initialize ProgressDialog -----------
        progressDialog = new ProgressDialog(UserQrCode.this);
        progressDialog.show();
        //progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        //--------------- Data Access from User Database -----------
        Query getCustomerDetails = FirebaseDatabase.getInstance().getReference("Participants").child(managerParticipant.getEventName()).child(phoneNo).child("Profile");
        getCustomerDetails.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //--------------- Access Data to Strings -----------
                String appName = "Nirman 2.0";
                String name = snapshot.child("mParticipantName").getValue(String.class);
                String phoneNumber = snapshot.child("mPhoneNumber").getValue(String.class);
                String teamname = snapshot.child("mTeamName").getValue(String.class);



                //--------------- Encoding Data -----------
                try {
                    // assert phoneNumber != null;
                    String encodedData = encrypt(managerParticipant.getEventName()+ ":" +name+ ":" +teamname+ ":" +phoneNumber);
                    MultiFormatWriter writer = new MultiFormatWriter();

                    //--------------- Create QR code -----------
                    try {
                        BitMatrix matrix = writer.encode( appName+":" + encodedData , BarcodeFormat.QR_CODE,350,350);

                        BarcodeEncoder encoder =new BarcodeEncoder();
                        Bitmap bitmap = encoder.createBitmap(matrix);
                        qr_output.setImageBitmap(bitmap);

                        progressDialog.dismiss();


                    } catch (WriterException e) {
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //--------------- Button for back to User Dashboard -----------
        btn_backToCd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserQrCode.this,UserDashBoard.class));
                finishAffinity();
            }
        });



    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), UserDashBoard.class));
        super.onBackPressed();
    }

    //--------------- Internet Error Dialog Box -----------
    private void showCustomDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(UserQrCode.this);
        builder.setMessage("Please connect to the internet")
                //   .setCancelable(false)
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
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    //--------------- Check Internet Is Connected -----------
    private boolean isConnected(UserQrCode userQrCode) {

        ConnectivityManager connectivityManager = (ConnectivityManager) userQrCode.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo bluetoothConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_BLUETOOTH);

        return (wifiConn != null && wifiConn.isConnected()) || (mobileConn != null && mobileConn.isConnected() || (bluetoothConn != null && bluetoothConn.isConnected())); // if true ,  else false

    }

    //--------------- Encode Data -----------
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private String encrypt(String forEncode) throws Exception{
        SecretKeySpec key = generateKey(keyPass);
        Cipher c = Cipher.getInstance(AES);
        c.init(Cipher.ENCRYPT_MODE,key);
        byte[] encVal = c.doFinal(forEncode.getBytes());
        return Base64.encodeToString(encVal,Base64.DEFAULT);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private SecretKeySpec generateKey(String keyPass) throws Exception {
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = keyPass.getBytes(StandardCharsets.UTF_8); //"UTF-8"
        digest.update(bytes, 0, bytes.length);
        byte[] key = digest.digest();
        return new SecretKeySpec(key, "AES"); //SecretKeySpec secretKeySpec = new SecretKeySpec(key,"AES");

    }





}