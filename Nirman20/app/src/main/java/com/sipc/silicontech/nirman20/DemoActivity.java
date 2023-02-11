package com.sipc.silicontech.nirman20;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.sipc.silicontech.nirman20.Admins.NewHackNationTeamData;
import com.sipc.silicontech.nirman20.Evaluators.DownloadResults;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class DemoActivity extends AppCompatActivity {
    //--------------- Encryption Variables -----------
    String AES = "AES";
    String keyPass = "Nirman@2023-SIPC";
    //------------------------------------------------

    private StorageReference mResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        mResult = FirebaseStorage.getInstance().getReference("QR CODES");
        FirebaseFirestore.getInstance().collection("HackNation").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {


                    ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
                    File InvDir = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
                    File file = new File(InvDir, "Nirman_2.0_QR" + ".pdf");
                    String mCurrentTime = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.getDefault()).format(new Date());
                    try {
                        OutputStream outputStream = new FileOutputStream(file);
                    } catch (FileNotFoundException exception) {
                        exception.printStackTrace();
                    }

                    Uri uri = Uri.fromFile(file);

                    PdfWriter pdfWriter = null;
                    try {
                        pdfWriter = new PdfWriter(file);
                    } catch (FileNotFoundException exception) {
                        exception.printStackTrace();
                    }
                    PdfDocument pdfDocument = new PdfDocument(Objects.requireNonNull(pdfWriter));
                    Document document = new Document(pdfDocument);

                    DeviceRgb invoiceyellow = new DeviceRgb(251, 192, 45);
                    DeviceRgb invoicegrey = new DeviceRgb(220, 220, 220);


                    float[] columnWidth = {250,250};
                    Table table1 = new Table(columnWidth);
                for(DocumentSnapshot snapshot : queryDocumentSnapshots){
                    NewHackNationTeamData teamData = snapshot.toObject(NewHackNationTeamData.class);
                    Bitmap bitmap1 = GenerateQR(teamData.getmTeamLead(),teamData.getmTeamName(),teamData.getmTeamLeadPhone());
                    Bitmap bitmap2 = GenerateQR(teamData.getmMem1Name(), teamData.getmTeamName(), teamData.getmMem1Phone());
                    Bitmap bitmap3 = GenerateQR(teamData.getmMem2Name(), teamData.getmTeamName(), teamData.getmMem2Phone());

                        Bitmap bitmap4 = GenerateQR(teamData.getmMem3Name(), teamData.getmTeamName(), teamData.getmMem3Phone());

                        ByteArrayOutputStream stream4 = new ByteArrayOutputStream();
                        bitmap4.compress(Bitmap.CompressFormat.PNG, 100, stream4);
                        byte[] bitmapData4 = stream4.toByteArray();

                        ImageData imageData4 = ImageDataFactory.create(bitmapData4);
                        Image image4 = new Image(imageData4);
                        image4.setHeight(200);
                        image4.setWidth(200);




                    ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
                    bitmap1.compress(Bitmap.CompressFormat.PNG, 100, stream1);
                    byte[] bitmapData1 = stream1.toByteArray();

                    ImageData imageData1 = ImageDataFactory.create(bitmapData1);
                    Image image1 = new Image(imageData1);
                    image1.setHeight(200);
                    image1.setWidth(200);

                    ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
                    bitmap2.compress(Bitmap.CompressFormat.PNG, 100, stream2);
                    byte[] bitmapData2 = stream2.toByteArray();

                    ImageData imageData2 = ImageDataFactory.create(bitmapData2);
                    Image image2 = new Image(imageData2);
                    image2.setHeight(200);
                    image2.setWidth(200);

                    ByteArrayOutputStream stream3 = new ByteArrayOutputStream();
                    bitmap3.compress(Bitmap.CompressFormat.PNG, 100, stream3);
                    byte[] bitmapData3 = stream3.toByteArray();

                    ImageData imageData3 = ImageDataFactory.create(bitmapData3);
                    Image image3 = new Image(imageData3);
                    image3.setHeight(200);
                    image3.setWidth(200);


                    table1.addCell(new Cell(3,1).add(image1).setBackgroundColor(invoicegrey));
                    table1.addCell(new Cell(3,1).add(image2).setBackgroundColor(invoicegrey));

                    table1.addCell(new Cell().add(new Paragraph("Event Name: HackNation")).setBorder(Border.NO_BORDER).setBackgroundColor(invoicegrey));
                    table1.addCell(new Cell().add(new Paragraph("Event Name: HackNation ")).setBorder(Border.NO_BORDER).setBackgroundColor(invoicegrey));


                    table1.addCell(new Cell().add(new Paragraph("Team Name : "+ teamData.getmTeamName())).setBorder(Border.NO_BORDER).setBackgroundColor(invoicegrey));
                    table1.addCell(new Cell().add(new Paragraph("Team Name : "+teamData.getmTeamName())).setBorder(Border.NO_BORDER).setBackgroundColor(invoicegrey));

                    table1.addCell(new Cell().add(new Paragraph("Participant Name : "+ teamData.getmTeamLead())).setBorder(Border.NO_BORDER).setBackgroundColor(invoicegrey));
                    table1.addCell(new Cell().add(new Paragraph("Participant Name : "+ teamData.getmMem1Name())).setBorder(Border.NO_BORDER).setBackgroundColor(invoicegrey));

                    table1.addCell(new Cell().add(new Paragraph("Participant Phone : "+ teamData.getmTeamLeadPhone())).setBorder(Border.NO_BORDER).setBackgroundColor(invoicegrey));
                    table1.addCell(new Cell().add(new Paragraph("Participant Phone : "+ teamData.getmMem1Phone())).setBorder(Border.NO_BORDER).setBackgroundColor(invoicegrey));



                    table1.addCell(new Cell(3,1).add(image3).setBackgroundColor(invoicegrey));
                    table1.addCell(new Cell(3,1).add(image4).setBackgroundColor(invoicegrey));

                    table1.addCell(new Cell().add(new Paragraph("Event Name: HackNation")).setBorder(Border.NO_BORDER).setBackgroundColor(invoicegrey));
                    table1.addCell(new Cell().add(new Paragraph("Event Name: HackNation ")).setBorder(Border.NO_BORDER).setBackgroundColor(invoicegrey));


                    table1.addCell(new Cell().add(new Paragraph("Team Name : "+ teamData.getmTeamName())).setBorder(Border.NO_BORDER).setBackgroundColor(invoicegrey));
                    table1.addCell(new Cell().add(new Paragraph("Team Name : "+teamData.getmTeamName())).setBorder(Border.NO_BORDER).setBackgroundColor(invoicegrey));

                    table1.addCell(new Cell().add(new Paragraph("Participant Name : "+ teamData.getmMem2Name())).setBorder(Border.NO_BORDER).setBackgroundColor(invoicegrey));
                    table1.addCell(new Cell().add(new Paragraph("Participant Name : "+ teamData.getmMem3Name())).setBorder(Border.NO_BORDER).setBackgroundColor(invoicegrey));

                    table1.addCell(new Cell().add(new Paragraph("Participant Phone : "+ teamData.getmMem2Phone())).setBorder(Border.NO_BORDER).setBackgroundColor(invoicegrey));
                    table1.addCell(new Cell().add(new Paragraph("Participant Phone : "+ teamData.getmMem3Phone())).setBorder(Border.NO_BORDER).setBackgroundColor(invoicegrey));


                }



                    document.add(table1);
                    document.add(new Paragraph(""));



                    document.add(new Paragraph("\n\n\n\n\n(Authorised Signatory)\n\n\n").setTextAlignment(TextAlignment.RIGHT));
                    document.close();

                    StorageReference reference = mResult.child("Nirman_2.0_HackNation" + "-" + mCurrentTime + ".pdf");

                    reference.putFile(uri).addOnSuccessListener(taskSnapshot -> {

                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                        }
                    }).addOnFailureListener(e -> {
                        Toast.makeText(DemoActivity.this, "Uploading PDF Failed !!" + e, Toast.LENGTH_SHORT).show();
                    }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri1) {
                                    String pdfurl = uri1.toString();
                                    if (pdfurl != null) {
                                        Intent intent = new Intent(Intent.ACTION_VIEW);
                                        intent.setType("application/pdf");
                                        intent.setData(Uri.parse(pdfurl));
                                        startActivity(intent);
                                    }
                                }
                            });
                        }
                    });
            }
        });
    }

    private Bitmap GenerateQR(String getmTeamLead, String getmTeamName, String getmTeamLeadPhone) {
        String appName = "Nirman 2.0";
        //--------------- Encoding Data -----------
        try {
            // assert phoneNumber != null;
            String encodedData = encrypt("HackNation" + ":" + getmTeamLead + ":" + getmTeamName + ":" + getmTeamLeadPhone);
            MultiFormatWriter writer = new MultiFormatWriter();

            //--------------- Create QR code -----------
            try {
                BitMatrix matrix = writer.encode(appName + ":" + encodedData, BarcodeFormat.QR_CODE, 350, 350);

                BarcodeEncoder encoder = new BarcodeEncoder();
                Bitmap bitmap = encoder.createBitmap(matrix);

                return bitmap;



            } catch (WriterException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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