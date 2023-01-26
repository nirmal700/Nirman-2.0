package com.sipc.silicontech.nirman20.Users;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.sipc.silicontech.nirman20.R;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Food_Coupouns extends AppCompatActivity {
    SessionManagerParticipant managerParticipant;
    String Name,Event,TeamName,mPhoneNo;

    String AES = "AES";
    String keyPass = "NirmanSITBBSR@Silicon";
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_coupouns);

        TextView mName,mTeamName,mEventName;
        ImageView iv_qr;

        mName = findViewById(R.id.tv_MemberName);
        mEventName = findViewById(R.id.tv_EventDetails);
        mTeamName = findViewById(R.id.tv_TeamName);
        iv_qr = findViewById(R.id.iv_qr);

        managerParticipant = new SessionManagerParticipant(getApplicationContext());
        Name = managerParticipant.getParticipantName();
        Event = managerParticipant.getEventName();
        TeamName = managerParticipant.getTeamName();
        mPhoneNo = managerParticipant.getPhone();

        mName.setText(Name);
        mEventName.setText(Event);
        mTeamName.setText(TeamName);

        if (!isConnected(Food_Coupouns.this))
            showCustomDialog();
        //--------------- Encoding Data -----------
        try {
            // assert phoneNumber != null;
            String mEncode = Name + ":" + Event + ":" + TeamName + ":" + mPhoneNo ;
            String encodedData = encrypt(mEncode);
            Log.e("TAG", "onCreate: " + mEncode);
            MultiFormatWriter writer = new MultiFormatWriter();

            //--------------- Create QR code -----------
            try {
                BitMatrix matrix = writer.encode("Nirman-SITBBSR" + ":" + "Food Coupoun's" + ":" + encodedData, BarcodeFormat.QR_CODE, 350, 350);

                BarcodeEncoder encoder = new BarcodeEncoder();
                Bitmap bitmap = encoder.createBitmap(matrix);
                iv_qr.setImageBitmap(bitmap);

                progressDialog.dismiss();


            } catch (WriterException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }



    }
    //--------------- Internet Error Dialog Box -----------
    private void showCustomDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Food_Coupouns.this);
        builder.setMessage("Please connect to the internet")
                //   .setCancelable(false)
                .setPositiveButton("Connect", (dialog, which) -> startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS)))
                .setNegativeButton("Cancel", (dialog, which) -> {
                    startActivity(new Intent(getApplicationContext(), UserDashBoard.class));
                    finish();
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    //--------------- Check Internet Is Connected -----------
    private boolean isConnected(Food_Coupouns singleTicket_qr) {

        ConnectivityManager connectivityManager = (ConnectivityManager) singleTicket_qr.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo bluetoothConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_BLUETOOTH);

        return (wifiConn != null && wifiConn.isConnected()) || (mobileConn != null && mobileConn.isConnected() || (bluetoothConn != null && bluetoothConn.isConnected())); // if true ,  else false

    }

    //--------------- Encode Data -----------
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private String encrypt(String forEncode) throws Exception {
        SecretKeySpec key = generateKey(keyPass);
        Cipher c = Cipher.getInstance(AES);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(forEncode.getBytes());
        return Base64.encodeToString(encVal, Base64.DEFAULT);
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