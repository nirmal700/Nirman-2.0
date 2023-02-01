package com.sipc.silicontech.nirman20.Evaluators;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
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
import com.sipc.silicontech.nirman20.Admins.NewRoboRaceTeamData;
import com.sipc.silicontech.nirman20.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


public class DownloadResults extends AppCompatActivity {
    private ArrayList<NewRoboRaceTeamData> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_results);
        FirebaseFirestore.getInstance().collection("Robo Race").orderBy("mTotalTimeTaken", Query.Direction.ASCENDING).orderBy("mCheckPointCleared", Query.Direction.DESCENDING).orderBy("mHandTouches", Query.Direction.ASCENDING).orderBy("mBonus", Query.Direction.DESCENDING)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot documentSnapshot : snapshotList)
                        {
                            Log.e("3452", "onSuccess: "+documentSnapshot.getData().toString() );
                        }
                    }
                });
    }

    private void GeneratePDF(ArrayList<NewRoboRaceTeamData> list) {
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File ResDir = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(ResDir, "Results_Nirman_2.0" + ".pdf");
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

        DeviceRgb invoiceblue = new DeviceRgb(30, 200, 230);
        DeviceRgb invoicegrey = new DeviceRgb(220, 220, 220);

        float columnWidth[] = {60,120,240,120};
        Table table1 = new Table(columnWidth);

        Drawable drawable1 = getDrawable(R.drawable.nirmanlogo);
        Bitmap bitmap1 = ((BitmapDrawable)drawable1).getBitmap();
        ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
        bitmap1.compress(Bitmap.CompressFormat.PNG,100,stream1);
        byte[] bitmapData1 = stream1.toByteArray();

        ImageData imageData1 = ImageDataFactory.create(bitmapData1);
        Image image1 = new Image(imageData1);
        image1.setHeight(110);
        image1.setWidth(180);

        table1.addCell( new Cell(3,1).add(image1).setBorder(Border.NO_BORDER));
        table1.addCell( new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table1.addCell( new Cell(1,2).add(new Paragraph("Screening Report").setFontSize(26f).setFontColor(invoiceblue)).setBorder(Border.NO_BORDER));

        document.add(table1);

        document.close();

        Intent intent = new Intent(Intent.ACTION_VIEW);
        File file1 = new File(Environment.getExternalStorageDirectory()+"/Nirman2.0_Result.pdf");
        Log.e("URI PDF", "createPDF: "+uri.toString() );
        intent.setDataAndType(Uri.fromFile(file1), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);

    }
}