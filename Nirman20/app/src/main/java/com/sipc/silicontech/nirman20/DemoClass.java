package com.sipc.silicontech.nirman20;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sipc.silicontech.nirman20.Admins.NewLineFollowerTeamData;

import java.util.ArrayList;

public class DemoClass extends AppCompatActivity {

    ArrayList<NewLineFollowerTeamData> mLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_class);
        mLine = new ArrayList<>();
        NewLineFollowerTeamData team1 = new NewLineFollowerTeamData("Line Follower", "The Bot Builders", "ITER", "Amrito Ballav Roy ", "8789217192", "Amlan Pradhan ", "9337786940", "Priyanshu Kumar Jena", "7682943276", "Kamaljeet Swain", "8018041867", 0, 0, false, false, 0, 0, 0, 0, 0);
        NewLineFollowerTeamData team2 = new NewLineFollowerTeamData("Line Follower", "Team Droidx", "Silicon Institute", "Debasish Mohanta", "7008539375", "S. Nirlipta Nitinimagna", "9437265390", "Abinash Panda", "9692183089", "Pratyush Kumar Mahanta", "8763519921", 0, 0, false, false, 0, 0, 0, 0, 0);
        NewLineFollowerTeamData team3 = new NewLineFollowerTeamData("Line Follower", "Team Vikings", "ITER", "Shaikh Tabrez", "7978702229", "Aniket Singh", "8480556272", "Raj Nandini Panda", "9692843536", "Sobhit Singh", "9776245195", 0, 0, false, false, 0, 0, 0, 0, 0);
        NewLineFollowerTeamData team4 = new NewLineFollowerTeamData("Line Follower", "Team Phoenix", "Parala Maharaja", "Prachi Pragnya Padhi ", "7853976062", "Diptiman Mohanta", "9692559424", "Monali Sardar", "8455860746", "Omkar Behera", "7325929062", 0, 0, false, false, 0, 0, 0, 0, 0);
        NewLineFollowerTeamData team5 = new NewLineFollowerTeamData("Line Follower", "Stark Industry", "Trident", "Dibyajit Mishra", "7849096216", "Piyush Kumar Singh", "8598855750", "S Sriram Reddy", "7894261416", "", "", 0, 0, false, false, 0, 0, 0, 0, 0);
        NewLineFollowerTeamData team6 = new NewLineFollowerTeamData("Line Follower", "Team Groot", "Parala Maharaja", "Karanjeet Behera", "6372012419", "Preeti Priyanshu Srichandan", "6373797720", "Rabinarayan Sahu", "7684074140", "Anil kumar sahoo", "6372651603", 0, 0, false, false, 0, 0, 0, 0, 0);
        NewLineFollowerTeamData team7 = new NewLineFollowerTeamData("Line Follower", "Team Rust", "Parala Maharaja", "Abinash Pati", "7377616040", "Priyanka Mohapatra", "7205847552", "Twarit Nanda", "8249721755", "Priyanka Das", "8599023292", 0, 0, false, false, 0, 0, 0, 0, 0);
        NewLineFollowerTeamData team8 = new NewLineFollowerTeamData("Line Follower", "The Fraud Engineers", "Silicon Institute", "Adarsh Kumar", "6200293206", "Abhinash Mohapatra", "8658733069", "Ayush Dwivedi", "8658334575", "Shabana Akhtar", "6372918078", 0, 0, false, false, 0, 0, 0, 0, 0);
        NewLineFollowerTeamData team9 = new NewLineFollowerTeamData("Line Follower", "The Whizzers", "Parala Maharaja", "M LISHA", "9078119269", "AMAN PATRA", "7377377956", "PREETAM KUMAR SAHU", "7735242813", "PRINCE MALLICK", "6290840599", 0, 0, false, false, 0, 0, 0, 0, 0);
        NewLineFollowerTeamData team10 = new NewLineFollowerTeamData("Line Follower", "Team Yodha", "PARALA MAHARAJA", "BISHNU PRASAD SAMAL", "9777271005", "Roshan Kumar padhi", "7992542596", "LIZA BEHERA", "8984053807", "SHARADHA SUMAN KANUNGO", "9937744554", 0, 0, false, false, 0, 0, 0, 0, 0);
        NewLineFollowerTeamData team11 = new NewLineFollowerTeamData("Line Follower", "Team Volt", "Silicon Institute", "Sailendra Ku Biswal", "8249857834", "Subhashree Samantray", "9937735552", "Sai Shradha Pattnaik", "6371039007", "Chetna pati", "8260446990", 0, 0, false, false, 0, 0, 0, 0, 0);
        NewLineFollowerTeamData team12 = new NewLineFollowerTeamData("Line Follower", "Team SNM", "ITER", "Mousumi prava pradhan", "6370480018", "Naisha Khilar", "6289379600", "Samikshya sahoo", "7735665992", "", "", 0, 0, false, false, 0, 0, 0, 0, 0);
        NewLineFollowerTeamData team13 = new NewLineFollowerTeamData("Line Follower", "Team IOTA", "ITER", "Taufeeq Iqbal Khan", "7815021399", "Ashutosh Mall", "9692339549", "Aditya Ray", "8260553351", "", "", 0, 0, false, false, 0, 0, 0, 0, 0);
        NewLineFollowerTeamData team14 = new NewLineFollowerTeamData("Line Follower", "Na-Jeez", "NISER", "Aditya Pravin Kamble", "8421357835", "Vishal Meena", "8488998151", "Aayush Jalmeria", "7889809039", "", "", 0, 0, false, false, 0, 0, 0, 0, 0);
        NewLineFollowerTeamData team15 = new NewLineFollowerTeamData("Line Follower", "Acolyte De Vulcan ", "Parala Maharaja", "Ashutosh Rout", "9039251961", "Amruta Sagar Bisoyi", "7735469462", "Shubham Ranjan Sahoo", "9040283999", "Saswati Sahoo", "7749082571", 0, 0, false, false, 0, 0, 0, 0, 0);
        NewLineFollowerTeamData team16= new NewLineFollowerTeamData("Line Follower", "Zephbotzs", "OUTR", "Satyabrata sahu", "7855011513", "Sahil kumar sahu", "9078101920", "Bijayananda sahu", "9777944326", "Sanket kumar Budek", "8084898094", 0, 0, false, false, 0, 0, 0, 0, 0);
        NewLineFollowerTeamData team17 = new NewLineFollowerTeamData("Line Follower", "Q-Mech", "Silicon Institute", "Anjali Routray", "8249873687", "Ajit Kumar Mishra", "9717214990", "Soumyajeet Parida", "9438033899", "Sumit Kumar Rath", "9348879505", 0, 0, false, false, 0, 0, 0, 0, 0);
        NewLineFollowerTeamData team18 = new NewLineFollowerTeamData("Line Follower", "Brute Force", "IGIT", "Debasish Mallick", "6371589306", "Amar Prasad Dhal", "7815032462", "Soubhagya Ranjan Sahoo", "7978662397", "", "", 0, 0, false, false, 0, 0, 0, 0, 0);
        NewLineFollowerTeamData team19 = new NewLineFollowerTeamData("Line Follower", "The Spartans", "Silicon Institute", "Priyabrat Omm Kumar", "9827499758", "Jagannath Patra", "9777548904", "Surbhi Saswati Mohanty", "7873605786", "Ansuman Panda", "9348380500", 0, 0, false, false, 0, 0, 0, 0, 0);
        NewLineFollowerTeamData team20 = new NewLineFollowerTeamData("Line Follower", "Iterate_NISER", "NISER", "Girija Sankar Ray", "9237065015", "S. Mahesh", "8220122293", "Karan Kumar Sahoo", "7894106006", "Sanat Kumar Behera", "", 0, 0, false, false, 0, 0, 0, 0, 0);
        NewLineFollowerTeamData team21 = new NewLineFollowerTeamData("Line Follower", "Pro.Bots", "Silicon Institute", "Rojy Samal", "8926107838", "Shibashish Chaulia", "7077976312", "Soumyadeep Shit", "9693651212", "", "", 0, 0, false, false, 0, 0, 0, 0, 0);
        NewLineFollowerTeamData team22 = new NewLineFollowerTeamData("Line Follower", "Team Blue", "Parala Maharaja", "Saswat Rath", "9668092094", "Gyana Ranjan Khatua", "7894459392", "Ashutosh Dalei", "8018356141", "Biswajit Dhal", "9937254505", 0, 0, false, false, 0, 0, 0, 0, 0);
        NewLineFollowerTeamData team23 = new NewLineFollowerTeamData("Line Follower", "Team Ultron", "Parala maharaja", "Ankit Pattnaik", "6372669676", "Alok Panigrahy", "8114902676", "Satyam Subham Das", "7894574493", "Ch Pritam Patro", "8637292971", 0, 0, false, false, 0, 0, 0, 0, 0);
        NewLineFollowerTeamData team24 = new NewLineFollowerTeamData("Line Follower", "Night Dwelers", "ITER", "Lalit Pradhan", "7735370990", "Sanat Kumar Naik", "7815044689", "Saumya Shukla", "9305463842", "Saswati Dhal", "9556977605", 0, 0, false, false, 0, 0, 0, 0, 0);
        NewLineFollowerTeamData team25 = new NewLineFollowerTeamData("Line Follower", "Robo Abeyaantrix", "Einstein", "Bishnuprasad Behera", "9668029067", "Ananya Sruti Sahoo", "9178706945", "SK Sohail", "7735548481", "Amarendra barik", "6370908512", 0, 0, false, false, 0, 0, 0, 0, 0);
        NewLineFollowerTeamData team26 = new NewLineFollowerTeamData("Line Follower", "Team Shinchan", "ITER", "Satyapratik Sahoo", "9348454678", "T Vinesh", "9114482646", "Bibhudatta Mohanty", "7751816604", "", "", 0, 0, false, false, 0, 0, 0, 0, 0);
        NewLineFollowerTeamData team27 = new NewLineFollowerTeamData("Line Follower", "CHITTI 2.0", "Silicon Institute", "Biswajit Parida", "6370307686", "", "", "", "", "", "", 0, 0, false, false, 0, 0, 0, 0, 0);
        NewLineFollowerTeamData team28 = new NewLineFollowerTeamData("Line Follower", "Sharpbotz", "CTTC", "SOUMITRI SWARUP DASH", "7788992664", "", "", "", "", "", "", 0, 0, false, false, 0, 0, 0, 0, 0);
        NewLineFollowerTeamData team29 = new NewLineFollowerTeamData("Line Follower", "TeamROBUSTA98a", "ITER", "Aditya kumar lenka", "8260341467", "Sudipta kumar Tripathy", "9348414198", "Aditya panigrahi", "7077040704", "", "", 0, 0, false, false, 0, 0, 0, 0, 0);
        NewLineFollowerTeamData team30 = new NewLineFollowerTeamData("Line Follower", "Robuster", "Einstein", "Saurav Swain", "8114749783", "Debaneek chhotaray", "8280734007", "Biswajit Sahoo", "7326051208", "Guruprasada Swain", "7205475207", 0, 0, false, false, 0, 0, 0, 0, 0);


        mLine.add(team1);
        mLine.add(team2);
        mLine.add(team3);
        mLine.add(team4);
        mLine.add(team5);
        mLine.add(team6);
        mLine.add(team7);
        mLine.add(team8);
        mLine.add(team9);
        mLine.add(team10);
        mLine.add(team11);
        mLine.add(team12);
        mLine.add(team13);
        mLine.add(team14);
        mLine.add(team15);
        mLine.add(team16);
        mLine.add(team17);
        mLine.add(team18);
        mLine.add(team19);
        mLine.add(team20);
        mLine.add(team21);
        mLine.add(team22);
        mLine.add(team23);
        mLine.add(team24);
        mLine.add(team25);
        mLine.add(team26);
        mLine.add(team27);
        mLine.add(team28);
        mLine.add(team29);
        mLine.add(team30);

        for (int i = 0; i < mLine.size(); i++) {
            FirebaseFirestore.getInstance().collection("Line Follower").document(mLine.get(i).getmTeamName()).set(mLine.get(i)).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {

                }
            });
        }
    }
}