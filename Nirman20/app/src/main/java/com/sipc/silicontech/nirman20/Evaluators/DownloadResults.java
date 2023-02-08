package com.sipc.silicontech.nirman20.Evaluators;

import android.app.ProgressDialog;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
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
import com.sipc.silicontech.nirman20.Admins.NewHackNationTeamData;
import com.sipc.silicontech.nirman20.Admins.NewIdeateTeamData;
import com.sipc.silicontech.nirman20.Admins.NewLineFollowerTeamData;
import com.sipc.silicontech.nirman20.Admins.NewRoboRaceTeamData;
import com.sipc.silicontech.nirman20.R;
import com.sipc.silicontech.nirman20.Users.Request_Help;
import com.sipc.silicontech.nirman20.Users.UserDashBoard;

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
    AutoCompleteTextView mEventType;
    String event;

    NewHackNationTeamData mHackNation;
    NewRoboRaceTeamData mRoboRace;
    NewLineFollowerTeamData mLineFollower;
    NewIdeateTeamData mIdeate;
    String pdfurl;
    ProgressDialog progressDialog;
    private StorageReference mResult;
    private ArrayList<NewRoboRaceTeamData> roboRaceTeamData;
    private ArrayList<NewHackNationTeamData> hackNationTeamData;
    private ArrayList<NewIdeateTeamData> ideateTeamData;
    private ArrayList<NewLineFollowerTeamData> lineFollowerTeamData;
    private SessionManagerEvaluator managerEvaluator;
    ImageView btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_results);
        mEventType = findViewById(R.id.autoCompleteEventType);
        Button btn_submit = findViewById(R.id.btn_submit);
        mResult = FirebaseStorage.getInstance().getReference("Results");
        managerEvaluator = new SessionManagerEvaluator(DownloadResults.this);
        btn_back = findViewById(R.id.btn_backToCd);

        progressDialog = new ProgressDialog(DownloadResults.this);
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.dismiss();

        ArrayList<String> arrayListEventType;
        ArrayAdapter<String> arrayAdapterEventType;
        arrayListEventType = new ArrayList<>();
        arrayListEventType.add("Robo Race");
        arrayListEventType.add("Line Follower");
        arrayListEventType.add("Ideate");
        arrayListEventType.add("HackNation");
        arrayAdapterEventType = new ArrayAdapter<>(getApplicationContext(), R.layout.text_menu, arrayListEventType);
        mEventType.setAdapter(arrayAdapterEventType);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            mAuth.signInAnonymously().addOnSuccessListener(this, authResult -> Log.e("FireBase Anonymous ", "onSuccess: Anonymous Sign in Success")).addOnFailureListener(this, exception -> Log.e("FireBase Anonymous", "signInAnonymously:FAILURE", exception));
        }

        roboRaceTeamData = new ArrayList<>();
        hackNationTeamData = new ArrayList<>();
        ideateTeamData = new ArrayList<>();
        lineFollowerTeamData = new ArrayList<>();
        mEventType.setOnItemClickListener((adapterView, view, i, l) -> event = arrayAdapterEventType.getItem(i));


        btn_back.setOnClickListener(v -> {
            startActivity(new Intent(DownloadResults.this, EvaluatorDashboard.class));
            finish();
        });

        btn_submit.setOnClickListener(view -> {
            if (event.equals("HackNation")) {
                CollectionReference mCollectionReference = FirebaseFirestore.getInstance().collection("HackNation");
                mCollectionReference.get().addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document1 : queryDocumentSnapshots.getDocuments()) {
                        FirebaseFirestore.getInstance().collection("HackNation Evaluation").whereEqualTo("mTeamName", document1.get("mTeamName")).addSnapshotListener((value, error) -> {
                            double sum = 0;
                            for (DocumentSnapshot document : Objects.requireNonNull(value).getDocuments()) {
                                sum = sum + (double) document.get("mAvg");
                            }
                            double avg = sum / value.size();
                            FirebaseFirestore.getInstance().collection("HackNation").document(Objects.requireNonNull(document1.get("mTeamName")).toString()).update("mFinalMark", avg).addOnSuccessListener(unused -> {
                                FirebaseFirestore.getInstance().collection("HackNation").orderBy("mFinalMark", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots1) {
                                        List<DocumentSnapshot> snapshotList = queryDocumentSnapshots1.getDocuments();
                                        for (DocumentSnapshot documentSnapshot : snapshotList) {
                                            mHackNation = documentSnapshot.toObject(NewHackNationTeamData.class);
                                            hackNationTeamData.add(mHackNation);
                                        }
                                        GenerateHackNationPDF(hackNationTeamData);
                                    }

                                    private void GenerateHackNationPDF(ArrayList<NewHackNationTeamData> hackNationTeamData) {
                                        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
                                        File InvDir = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
                                        File file = new File(InvDir, "Nirman_2.0_HackNation" + ".pdf");
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

                                        float[] columnWidth = {140, 140, 140, 140};
                                        Table table1 = new Table(columnWidth);
                                        Drawable drawable1 = getDrawable(R.drawable.nirmanlogo);
                                        Bitmap bitmap1 = ((BitmapDrawable) drawable1).getBitmap();
                                        ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
                                        bitmap1.compress(Bitmap.CompressFormat.PNG, 100, stream1);
                                        byte[] bitmapData1 = stream1.toByteArray();

                                        ImageData imageData1 = ImageDataFactory.create(bitmapData1);
                                        Image image1 = new Image(imageData1);
                                        image1.setHeight(60);
                                        image1.setWidth(180);

    //1
                                        table1.addCell(new Cell(3, 1).add(image1).setBorder(Border.NO_BORDER));
                                        table1.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
                                        table1.addCell(new Cell(1, 2).add(new Paragraph("HackNation Report").setFontSize(26f).setFontColor(invoiceyellow)).setBorder(Border.NO_BORDER));
                                        //table1.addCell( new Cell().add(new Paragraph()));
    //2
                                        //table1.addCell( new Cell().add(new Paragraph()));
                                        table1.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
                                        table1.addCell(new Cell().add(new Paragraph("Event Name: ")).setBorder(Border.NO_BORDER));
                                        table1.addCell(new Cell().add(new Paragraph("HackNation ")).setBorder(Border.NO_BORDER));
    //3
                                        //table1.addCell( new Cell().add(new Paragraph()));
                                        table1.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
                                        table1.addCell(new Cell().add(new Paragraph("Generated By: ")).setBorder(Border.NO_BORDER));
                                        table1.addCell(new Cell().add(new Paragraph(managerEvaluator.getEvaluatorName())).setBorder(Border.NO_BORDER));
    //4
                                        table1.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
                                        table1.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
                                        table1.addCell(new Cell().add(new Paragraph("Report Generated on ")).setBorder(Border.NO_BORDER));
                                        table1.addCell(new Cell().add(new Paragraph(mCurrentTime)).setBorder(Border.NO_BORDER));


                                        document.add(table1);
                                        document.add(new Paragraph(""));

                                        float[] columnWidth2 = {20, 160, 150, 180};
                                        Table table2 = new Table(columnWidth2);
    //2-01
                                        table2.addCell(new Cell().add(new Paragraph("Pos.")).setBackgroundColor(invoiceyellow));
                                        table2.addCell(new Cell().add(new Paragraph("Team Name")).setBackgroundColor(invoiceyellow));
                                        table2.addCell(new Cell().add(new Paragraph("College Name")).setBackgroundColor(invoiceyellow));
                                        table2.addCell(new Cell().add(new Paragraph("Team Lead Name")).setBackgroundColor(invoiceyellow));
    //2-02
                                        for (int a = 0; a < hackNationTeamData.size(); a++) {
                                            table2.addCell(new Cell().add(new Paragraph("" + (a + 1))).setBackgroundColor(invoicegrey));
                                            table2.addCell(new Cell().add(new Paragraph(hackNationTeamData.get(a).getmTeamName())).setBackgroundColor(invoicegrey));
                                            table2.addCell(new Cell().add(new Paragraph(hackNationTeamData.get(a).getmCollegeName())).setBackgroundColor(invoicegrey));
                                            table2.addCell(new Cell().add(new Paragraph(hackNationTeamData.get(a).getmTeamLead())).setBackgroundColor(invoicegrey));

                                        }

                                        document.add(table2);


                                        document.add(new Paragraph("\n\n\n\n\n(Authorised Signatory)\n\n\n").setTextAlignment(TextAlignment.RIGHT));
                                        document.close();
                                        hackNationTeamData.clear();

                                        StorageReference reference = mResult.child("Nirman_2.0_HackNation" + "-" + mCurrentTime + ".pdf");

                                        reference.putFile(uri).addOnSuccessListener(taskSnapshot -> {

                                        }).addOnProgressListener(snapshot -> progressDialog.show()).addOnFailureListener(e -> {
                                            progressDialog.dismiss();
                                            Toast.makeText(DownloadResults.this, "Uploading PDF Failed !!" + e, Toast.LENGTH_SHORT).show();
                                        }).addOnCompleteListener(task -> reference.getDownloadUrl().addOnSuccessListener(uri1 -> {
                                            pdfurl = uri1.toString();
                                            progressDialog.dismiss();
                                            if (pdfurl != null) {
                                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                                intent.setType("application/pdf");
                                                intent.setData(Uri.parse(pdfurl));
                                                startActivity(intent);
                                            }
                                        }));
                                    }
                                });
                            });
                        });
                    }

                });
            } else if (event.equals("Ideate")) {
                CollectionReference mCollectionReferenceIdeate = FirebaseFirestore.getInstance().collection("Ideate");
                mCollectionReferenceIdeate.get().addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document1 : queryDocumentSnapshots.getDocuments()) {
                        FirebaseFirestore.getInstance().collection("Ideate Evaluation").whereEqualTo("mTeamName", document1.get("mTeamName")).addSnapshotListener((value, error) -> {
                            double sum = 0;
                            for (DocumentSnapshot document : Objects.requireNonNull(value).getDocuments()) {
                                sum = sum + (double) document.get("mAvg");
                            }
                            double avg = sum / value.size();
                            FirebaseFirestore.getInstance().collection("Ideate").document(Objects.requireNonNull(document1.get("mTeamName")).toString()).update("mFinalMark", avg).addOnSuccessListener(unused -> FirebaseFirestore.getInstance().collection("Ideate").orderBy("mFinalMark", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots12) {
                                    List<DocumentSnapshot> snapshotList = queryDocumentSnapshots12.getDocuments();
                                    for (DocumentSnapshot documentSnapshot : snapshotList) {
                                        mIdeate = documentSnapshot.toObject(NewIdeateTeamData.class);
                                        ideateTeamData.add(mIdeate);
                                    }
                                    GenerateIdeatePDF(ideateTeamData);

                                }

                                private void GenerateIdeatePDF(ArrayList<NewIdeateTeamData> ideateTeamData) {
                                    ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
                                    File InvDir = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
                                    File file = new File(InvDir, "Nirman_2.0_Ideate" + ".pdf");
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

                                    float[] columnWidth = {140, 140, 140, 140};
                                    Table table1 = new Table(columnWidth);
                                    Drawable drawable1 = getDrawable(R.drawable.nirmanlogo);
                                    Bitmap bitmap1 = ((BitmapDrawable) drawable1).getBitmap();
                                    ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
                                    bitmap1.compress(Bitmap.CompressFormat.PNG, 100, stream1);
                                    byte[] bitmapData1 = stream1.toByteArray();

                                    ImageData imageData1 = ImageDataFactory.create(bitmapData1);
                                    Image image1 = new Image(imageData1);
                                    image1.setHeight(60);
                                    image1.setWidth(180);

//1
                                    table1.addCell(new Cell(3, 1).add(image1).setBorder(Border.NO_BORDER));
                                    table1.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
                                    table1.addCell(new Cell(1, 2).add(new Paragraph("Ideate Report").setFontSize(26f).setFontColor(invoiceyellow)).setBorder(Border.NO_BORDER));
                                    //table1.addCell( new Cell().add(new Paragraph()));
//2
                                    //table1.addCell( new Cell().add(new Paragraph()));
                                    table1.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
                                    table1.addCell(new Cell().add(new Paragraph("Event Name: ")).setBorder(Border.NO_BORDER));
                                    table1.addCell(new Cell().add(new Paragraph("Ideate ")).setBorder(Border.NO_BORDER));
//3
                                    //table1.addCell( new Cell().add(new Paragraph()));
                                    table1.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
                                    table1.addCell(new Cell().add(new Paragraph("Generated By: ")).setBorder(Border.NO_BORDER));
                                    table1.addCell(new Cell().add(new Paragraph(managerEvaluator.getEvaluatorName())).setBorder(Border.NO_BORDER));
//4
                                    table1.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
                                    table1.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
                                    table1.addCell(new Cell().add(new Paragraph("Report Generated on ")).setBorder(Border.NO_BORDER));
                                    table1.addCell(new Cell().add(new Paragraph(mCurrentTime)).setBorder(Border.NO_BORDER));


                                    document.add(table1);
                                    document.add(new Paragraph(""));

                                    float[] columnWidth2 = {20, 160, 150, 180};
                                    Table table2 = new Table(columnWidth2);
//2-01
                                    table2.addCell(new Cell().add(new Paragraph("Pos.")).setBackgroundColor(invoiceyellow));
                                    table2.addCell(new Cell().add(new Paragraph("Team Name")).setBackgroundColor(invoiceyellow));
                                    table2.addCell(new Cell().add(new Paragraph("College Name")).setBackgroundColor(invoiceyellow));
                                    table2.addCell(new Cell().add(new Paragraph("Team Lead Name")).setBackgroundColor(invoiceyellow));
//2-02
                                    for (int a = 0; a < ideateTeamData.size(); a++) {
                                        table2.addCell(new Cell().add(new Paragraph("" + (a + 1))).setBackgroundColor(invoicegrey));
                                        table2.addCell(new Cell().add(new Paragraph(ideateTeamData.get(a).getmTeamName())).setBackgroundColor(invoicegrey));
                                        table2.addCell(new Cell().add(new Paragraph(ideateTeamData.get(a).getmCollegeName())).setBackgroundColor(invoicegrey));
                                        table2.addCell(new Cell().add(new Paragraph(ideateTeamData.get(a).getmTeamLead())).setBackgroundColor(invoicegrey));

                                    }

                                    document.add(table2);


                                    document.add(new Paragraph("\n\n\n\n\n(Authorised Signatory)\n\n\n").setTextAlignment(TextAlignment.RIGHT));
                                    document.close();
                                    hackNationTeamData.clear();

                                    StorageReference reference = mResult.child("Nirman_2.0_Ideate" + "-" + mCurrentTime + ".pdf");

                                    reference.putFile(uri).addOnSuccessListener(taskSnapshot -> {

                                    }).addOnProgressListener(snapshot -> progressDialog.show()).addOnFailureListener(e -> {
                                        progressDialog.dismiss();
                                        Toast.makeText(DownloadResults.this, "Uploading PDF Failed !!" + e, Toast.LENGTH_SHORT).show();
                                    }).addOnCompleteListener(task -> reference.getDownloadUrl().addOnSuccessListener(uri1 -> {
                                        pdfurl = uri1.toString();
                                        progressDialog.dismiss();
                                        progressDialog.dismiss();
                                        if (pdfurl != null) {
                                            Intent intent = new Intent(Intent.ACTION_VIEW);
                                            intent.setType("application/pdf");
                                            intent.setData(Uri.parse(pdfurl));
                                            startActivity(intent);
                                        }
                                    }));
                                }
                            }));
                        });
                    }

                });

            } else if (event.equals("Robo Race")) {
                FirebaseFirestore.getInstance().collection("Robo Race").orderBy("mTotalTimeTaken", Query.Direction.ASCENDING).orderBy("mCheckPointCleared", Query.Direction.DESCENDING).orderBy("mHandTouches", Query.Direction.ASCENDING).orderBy("mBonus", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot documentSnapshot : snapshotList) {
                            mRoboRace = documentSnapshot.toObject(NewRoboRaceTeamData.class);
                            roboRaceTeamData.add(mRoboRace);
                        }
                        GenerateRoboRacePDF(roboRaceTeamData);
                    }

                    private void GenerateRoboRacePDF(ArrayList<NewRoboRaceTeamData> roboRaceTeamData) {
                        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
                        File InvDir = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
                        File file = new File(InvDir, "Nirman_2.0_Robo_Race" + ".pdf");
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

                        float[] columnWidth = {140, 140, 140, 140};
                        Table table1 = new Table(columnWidth);
                        Drawable drawable1 = getDrawable(R.drawable.nirmanlogo);
                        Bitmap bitmap1 = ((BitmapDrawable) drawable1).getBitmap();
                        ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
                        bitmap1.compress(Bitmap.CompressFormat.PNG, 100, stream1);
                        byte[] bitmapData1 = stream1.toByteArray();

                        ImageData imageData1 = ImageDataFactory.create(bitmapData1);
                        Image image1 = new Image(imageData1);
                        image1.setHeight(60);
                        image1.setWidth(180);

//1
                        table1.addCell(new Cell(3, 1).add(image1).setBorder(Border.NO_BORDER));
                        table1.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
                        table1.addCell(new Cell(1, 2).add(new Paragraph("Robo-Race Report").setFontSize(26f).setFontColor(invoiceyellow)).setBorder(Border.NO_BORDER));
                        //table1.addCell( new Cell().add(new Paragraph()));
//2
                        //table1.addCell( new Cell().add(new Paragraph()));
                        table1.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
                        table1.addCell(new Cell().add(new Paragraph("Event Name: ")).setBorder(Border.NO_BORDER));
                        table1.addCell(new Cell().add(new Paragraph("Robo-Race ")).setBorder(Border.NO_BORDER));
//3
                        //table1.addCell( new Cell().add(new Paragraph()));
                        table1.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
                        table1.addCell(new Cell().add(new Paragraph("Generated By: ")).setBorder(Border.NO_BORDER));
                        table1.addCell(new Cell().add(new Paragraph(managerEvaluator.getEvaluatorName())).setBorder(Border.NO_BORDER));
//4
                        table1.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
                        table1.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
                        table1.addCell(new Cell().add(new Paragraph("Report Generated on ")).setBorder(Border.NO_BORDER));
                        table1.addCell(new Cell().add(new Paragraph(mCurrentTime)).setBorder(Border.NO_BORDER));


                        document.add(table1);
                        document.add(new Paragraph(""));

                        float[] columnWidth2 = {20, 100, 130, 40, 40, 40, 30, 30, 30};
                        Table table2 = new Table(columnWidth2);
//2-01
                        table2.addCell(new Cell().add(new Paragraph("Pos.")).setBackgroundColor(invoiceyellow));
                        table2.addCell(new Cell().add(new Paragraph("Team Name")).setBackgroundColor(invoiceyellow));
                        table2.addCell(new Cell().add(new Paragraph("College Name")).setBackgroundColor(invoiceyellow));
                        table2.addCell(new Cell().add(new Paragraph("Total Time")).setBackgroundColor(invoiceyellow));
                        table2.addCell(new Cell().add(new Paragraph("CheckPoints")).setBackgroundColor(invoiceyellow));
                        table2.addCell(new Cell().add(new Paragraph("CP Skiped")).setBackgroundColor(invoiceyellow));
                        table2.addCell(new Cell().add(new Paragraph("Penalty")).setBackgroundColor(invoiceyellow));
                        table2.addCell(new Cell().add(new Paragraph("HandTouches")).setBackgroundColor(invoiceyellow));
                        table2.addCell(new Cell().add(new Paragraph("Tech.Time")).setBackgroundColor(invoiceyellow));


//2-02

                        for (int a = 0; a < roboRaceTeamData.size(); a++) {
                            table2.addCell(new Cell().add(new Paragraph("" + (a + 1)).setBackgroundColor(invoicegrey)));
                            table2.addCell(new Cell().add(new Paragraph(roboRaceTeamData.get(a).getmTeamName())).setBackgroundColor(invoicegrey));
                            table2.addCell(new Cell().add(new Paragraph(roboRaceTeamData.get(a).getmCollegeName())).setBackgroundColor(invoicegrey));
                            table2.addCell(new Cell().add(new Paragraph("" + roboRaceTeamData.get(a).getmTotalTimeTaken())).setBackgroundColor(invoicegrey));
                            table2.addCell(new Cell().add(new Paragraph("" + roboRaceTeamData.get(a).getmCheckPointCleared())).setBackgroundColor(invoicegrey));
                            table2.addCell(new Cell().add(new Paragraph("" + roboRaceTeamData.get(a).getmCheckPointSkipped())).setBackgroundColor(invoicegrey));
                            table2.addCell(new Cell().add(new Paragraph("-")).setBackgroundColor(invoicegrey));
                            table2.addCell(new Cell().add(new Paragraph("" + roboRaceTeamData.get(a).getmHandTouches())).setBackgroundColor(invoicegrey));
                            table2.addCell(new Cell().add(new Paragraph("" + roboRaceTeamData.get(a).ismTimeOutTaken())).setBackgroundColor(invoicegrey));

                        }

                        document.add(table2);

                        document.add(new Paragraph("\n\n\n\n\n(Authorised Signatory)\n\n\n").setTextAlignment(TextAlignment.RIGHT));
                        document.close();
                        hackNationTeamData.clear();

                        StorageReference reference = mResult.child("Nirman_2.0_Robo_Race" + "-" + mCurrentTime + ".pdf");

                        reference.putFile(uri).addOnSuccessListener(taskSnapshot -> {

                        }).addOnProgressListener(snapshot -> progressDialog.show()).addOnFailureListener(e -> {
                            progressDialog.dismiss();
                            Toast.makeText(DownloadResults.this, "Uploading PDF Failed !!" + e, Toast.LENGTH_SHORT).show();
                        }).addOnCompleteListener(task -> reference.getDownloadUrl().addOnSuccessListener(uri1 -> {
                            pdfurl = uri1.toString();
                            progressDialog.dismiss();
                            if (pdfurl != null) {
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setType("application/pdf");
                                intent.setData(Uri.parse(pdfurl));
                                startActivity(intent);
                            }
                        }));


                    }
                });
            } else if (event.equals("Line Follower")) {
                FirebaseFirestore.getInstance().collection("Line Follower").orderBy("mTotalTimeTaken", Query.Direction.ASCENDING).orderBy("mCheckPointCleared", Query.Direction.DESCENDING).orderBy("mHandTouches", Query.Direction.ASCENDING).orderBy("mBonus", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot documentSnapshot : snapshotList) {
                            mLineFollower = documentSnapshot.toObject(NewLineFollowerTeamData.class);
                            lineFollowerTeamData.add(mLineFollower);
                        }
                        GenerateLineFollowerPDF(lineFollowerTeamData);
                    }

                    private void GenerateLineFollowerPDF(ArrayList<NewLineFollowerTeamData> lineFollowerTeamData) {
                        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
                        File InvDir = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
                        File file = new File(InvDir, "Nirman_2.0_Line_Follower" + ".pdf");
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

                        float[] columnWidth = {140, 140, 140, 140};
                        Table table1 = new Table(columnWidth);
                        Drawable drawable1 = getDrawable(R.drawable.nirmanlogo);
                        Bitmap bitmap1 = ((BitmapDrawable) drawable1).getBitmap();
                        ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
                        bitmap1.compress(Bitmap.CompressFormat.PNG, 100, stream1);
                        byte[] bitmapData1 = stream1.toByteArray();

                        ImageData imageData1 = ImageDataFactory.create(bitmapData1);
                        Image image1 = new Image(imageData1);
                        image1.setHeight(60);
                        image1.setWidth(180);

//1
                        table1.addCell(new Cell(3, 1).add(image1).setBorder(Border.NO_BORDER));
                        table1.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
                        table1.addCell(new Cell(1, 2).add(new Paragraph("Line-Follower Report").setFontSize(26f).setFontColor(invoiceyellow)).setBorder(Border.NO_BORDER));
                        //table1.addCell( new Cell().add(new Paragraph()));
//2
                        //table1.addCell( new Cell().add(new Paragraph()));
                        table1.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
                        table1.addCell(new Cell().add(new Paragraph("Event Name: ")).setBorder(Border.NO_BORDER));
                        table1.addCell(new Cell().add(new Paragraph("Line Follower ")).setBorder(Border.NO_BORDER));
//3
                        //table1.addCell( new Cell().add(new Paragraph()));
                        table1.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
                        table1.addCell(new Cell().add(new Paragraph("Generated By: ")).setBorder(Border.NO_BORDER));
                        table1.addCell(new Cell().add(new Paragraph(managerEvaluator.getEvaluatorName())).setBorder(Border.NO_BORDER));
//4
                        table1.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
                        table1.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
                        table1.addCell(new Cell().add(new Paragraph("Report Generated on ")).setBorder(Border.NO_BORDER));
                        table1.addCell(new Cell().add(new Paragraph(mCurrentTime)).setBorder(Border.NO_BORDER));


                        document.add(table1);
                        document.add(new Paragraph(""));

                        float[] columnWidth2 = {20, 100, 130, 40, 40, 40, 30, 30, 30};
                        Table table2 = new Table(columnWidth2);
//2-01
                        table2.addCell(new Cell().add(new Paragraph("Pos.")).setBackgroundColor(invoiceyellow));
                        table2.addCell(new Cell().add(new Paragraph("Team Name")).setBackgroundColor(invoiceyellow));
                        table2.addCell(new Cell().add(new Paragraph("College Name")).setBackgroundColor(invoiceyellow));
                        table2.addCell(new Cell().add(new Paragraph("Total Time")).setBackgroundColor(invoiceyellow));
                        table2.addCell(new Cell().add(new Paragraph("CheckPoints")).setBackgroundColor(invoiceyellow));
                        table2.addCell(new Cell().add(new Paragraph("CP Skiped")).setBackgroundColor(invoiceyellow));
                        table2.addCell(new Cell().add(new Paragraph("Penalty")).setBackgroundColor(invoiceyellow));
                        table2.addCell(new Cell().add(new Paragraph("HandTouches")).setBackgroundColor(invoiceyellow));
                        table2.addCell(new Cell().add(new Paragraph("Tech.Time")).setBackgroundColor(invoiceyellow));


//2-02

                        for (int a = 0; a < lineFollowerTeamData.size(); a++) {
                            table2.addCell(new Cell().add(new Paragraph("" + (a + 1)).setBackgroundColor(invoicegrey)));
                            table2.addCell(new Cell().add(new Paragraph(lineFollowerTeamData.get(a).getmTeamName())).setBackgroundColor(invoicegrey));
                            table2.addCell(new Cell().add(new Paragraph(lineFollowerTeamData.get(a).getmCollegeName())).setBackgroundColor(invoicegrey));
                            table2.addCell(new Cell().add(new Paragraph("" + lineFollowerTeamData.get(a).getmTotalTimeTaken())).setBackgroundColor(invoicegrey));
                            table2.addCell(new Cell().add(new Paragraph("" + lineFollowerTeamData.get(a).getmCheckPointCleared())).setBackgroundColor(invoicegrey));
                            table2.addCell(new Cell().add(new Paragraph("" + lineFollowerTeamData.get(a).getmCheckPointSkipped())).setBackgroundColor(invoicegrey));
                            table2.addCell(new Cell().add(new Paragraph("-")).setBackgroundColor(invoicegrey));
                            table2.addCell(new Cell().add(new Paragraph("" + lineFollowerTeamData.get(a).getmHandTouches())).setBackgroundColor(invoicegrey));
                            table2.addCell(new Cell().add(new Paragraph("" + lineFollowerTeamData.get(a).ismTimeOutTaken())).setBackgroundColor(invoicegrey));

                        }

                        document.add(table2);

                        document.add(new Paragraph("\n\n\n\n\n(Authorised Signatory)\n\n\n").setTextAlignment(TextAlignment.RIGHT));
                        document.close();
                        hackNationTeamData.clear();

                        StorageReference reference = mResult.child("Nirman_2.0_Line_Follower" + "-" + mCurrentTime + ".pdf");

                        reference.putFile(uri).addOnSuccessListener(taskSnapshot -> {

                        }).addOnProgressListener(snapshot -> progressDialog.show()).addOnFailureListener(e -> {
                            progressDialog.dismiss();
                            Toast.makeText(DownloadResults.this, "Uploading PDF Failed !!" + e, Toast.LENGTH_SHORT).show();
                        }).addOnCompleteListener(task -> reference.getDownloadUrl().addOnSuccessListener(uri1 -> {
                            pdfurl = uri1.toString();
                            progressDialog.dismiss();
                            if (pdfurl != null) {
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setType("application/pdf");
                                intent.setData(Uri.parse(pdfurl));
                                startActivity(intent);
                            }
                        }));
                    }
                });
            }
        });
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), EvaluatorDashboard.class));
        super.onBackPressed();
    }

}