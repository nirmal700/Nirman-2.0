package com.sipc.silicontech.nirman20.Evaluators;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.sipc.silicontech.nirman20.Admins.NewTeamData;
import com.sipc.silicontech.nirman20.QRCodeScanner;
import com.sipc.silicontech.nirman20.R;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class EvaluatorDashboard extends AppCompatActivity {

    MaterialCardView btn_evaluate;
    String AES = "AES";
    String keyPass = "Nirman@2023-SIPC";
    CollectionReference mCollectionReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.evaluator_dashboard);

        btn_evaluate = findViewById(R.id.btn_evaluate);


        btn_evaluate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isConnected(EvaluatorDashboard.this)){
                    showCustomDialog();
                }else {
                    scanCode();
                }
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
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);

        //Check Condition
        if (intentResult.getContents() != null && resultCode==RESULT_OK) {

            SessionManagerEvaluator manager = new SessionManagerEvaluator(getApplicationContext());

            FirebaseDatabase rootNode = FirebaseDatabase.getInstance();

            String output = intentResult.getContents();

            if (output.startsWith("Nirman 2.0")) {

                try {
                    String[] separated = output.split(":");

                    String userDetails = separated[1];
                    String currentTime = new SimpleDateFormat("dd-MM-yyyy hh:mm aa", Locale.getDefault()).format(new Date());

                    String decodedData = (String) decrypt(userDetails);

                    String[] separateData = decodedData.split(":");
                    Log.e("TAG", "onActivityResult: "+decodedData);
                    String event = separateData[0];
                    String name = separateData[1];
                    String teamname = separateData[2];
                    String phoneNo = separateData[3];
                    mCollectionReference = FirebaseFirestore.getInstance().collection(event);

//                    if (email.equals(" ")) {
//                        email = "";
//                    }
//
//                    if (address.equals(" ")) {
//                        address = "";
//                    }

                    DocumentReference documentReference = mCollectionReference.document(teamname);
                    documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            NewTeamData newTeamData = documentSnapshot.toObject(NewTeamData.class);
                            String clgname = newTeamData.getmCollegeName().toString();
                            String problemstat = newTeamData.getmProblemStat().toString();
                            String approach = newTeamData.getmApproach().toString();
                            Intent mEvaluatorIntent = new Intent(EvaluatorDashboard.this, EvaluatorReviewEvaluation.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            mEvaluatorIntent.putExtra("mTeamName",teamname);
                            mEvaluatorIntent.putExtra("mCollegeName",clgname);
                            mEvaluatorIntent.putExtra("mProblemStat",problemstat);
                            mEvaluatorIntent.putExtra("mApproach",approach);
                            startActivity(mEvaluatorIntent);
                            finish();



                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EvaluatorDashboard.this, "Error!!"+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });

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
            }

            else{
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
        c.init(Cipher.DECRYPT_MODE,key);
        byte[] decodeValue = android.util.Base64.decode(userDetails,android.util.Base64.DEFAULT);
        byte[] decValue = c.doFinal(decodeValue);
        return new String(decValue);

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private SecretKeySpec generateKey(String keyPass) throws Exception {
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = keyPass.getBytes(StandardCharsets.UTF_8);
        digest.update(bytes,0,bytes.length);
        byte[] key = digest.digest();
        return new SecretKeySpec(key,"AES");
    }

    //--------------- Internet Error Dialog Box -----------
    private void showCustomDialog() {

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(EvaluatorDashboard.this);
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
    private boolean isConnected(EvaluatorDashboard userDashBoard) {

        ConnectivityManager connectivityManager = (ConnectivityManager) userDashBoard.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo bluetoothConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_BLUETOOTH);

        return (wifiConn != null && wifiConn.isConnected()) || (mobileConn != null && mobileConn.isConnected() || (bluetoothConn != null && bluetoothConn.isConnected())); // if true ,  else false

    }
}