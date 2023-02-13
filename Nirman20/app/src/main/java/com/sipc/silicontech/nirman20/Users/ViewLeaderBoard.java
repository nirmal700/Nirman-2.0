package com.sipc.silicontech.nirman20.Users;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.sipc.silicontech.nirman20.Admins.AdminDashboard;
import com.sipc.silicontech.nirman20.Admins.NewHackNationTeamData;
import com.sipc.silicontech.nirman20.Admins.NewIdeateTeamData;
import com.sipc.silicontech.nirman20.Admins.NewLineFollowerTeamData;
import com.sipc.silicontech.nirman20.Admins.NewRoboRaceTeamData;
import com.sipc.silicontech.nirman20.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ViewLeaderBoard extends AppCompatActivity {

    AutoCompleteTextView mEventType;
    ImageView btn_back;
    private RecyclerView recyclerView;

    private ArrayList<NewRoboRaceTeamData> roboRaceTeamData;
    private ArrayList<NewHackNationTeamData> hackNationTeamData;
    private ArrayList<NewIdeateTeamData> ideateTeamData;
    private ArrayList<NewLineFollowerTeamData> lineFollowerTeamData;

    private List<LeaderBoard> mLeaderBoard;
    private LeaderBoardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_leader_board);
        mEventType = findViewById(R.id.autoCompleteEvent);
        EditText et_search = findViewById(R.id.et_search);
        btn_back = findViewById(R.id.btn_back);
        et_search = findViewById(R.id.et_search);

        roboRaceTeamData = new ArrayList<>();
        hackNationTeamData = new ArrayList<>();
        lineFollowerTeamData = new ArrayList<>();
        ideateTeamData = new ArrayList<>();
        mLeaderBoard = new ArrayList<>();

        ProgressDialog progressDialog = new ProgressDialog(ViewLeaderBoard.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(false);
        progressDialog.cancel();

        recyclerView = findViewById(R.id.rv_leaderboard);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        if (!isConnected(ViewLeaderBoard.this)) {
            showCustomDialog();
        }

        ArrayList<String> arrayListEventType;
        ArrayAdapter<String> arrayAdapterEventType;
        arrayListEventType = new ArrayList<>();
        arrayListEventType.add("Robo Race");
        arrayListEventType.add("Line Follower");
        arrayListEventType.add("Ideate - 1");
        arrayListEventType.add("Ideate - 2");
        arrayListEventType.add("HackNation");
        arrayAdapterEventType = new ArrayAdapter<>(getApplicationContext(), R.layout.text_menu, arrayListEventType);
        mEventType.setAdapter(arrayAdapterEventType);


        mEventType.setOnItemClickListener((parent, view, position, id) -> {
            if (arrayAdapterEventType.getItem(position).equals("Robo Race") | arrayAdapterEventType.getItem(position).equals("Line Follower")) {
                if (arrayAdapterEventType.getItem(position).equals("Robo Race")) {
                    FirebaseFirestore.getInstance().collection(arrayAdapterEventType.getItem(position))
                            .orderBy("mTotal", Query.Direction.DESCENDING)
                            .orderBy("mTotalTimeTaken", Query.Direction.ASCENDING).get()
                            .addOnSuccessListener(queryDocumentSnapshots -> {
                                mLeaderBoard.clear();
                                roboRaceTeamData.clear();
                                List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                                for (DocumentSnapshot documentSnapshot : snapshotList) {
                                    NewRoboRaceTeamData mRoboRace = documentSnapshot.toObject(NewRoboRaceTeamData.class);
                                    if (Objects.requireNonNull(mRoboRace).getmTotalTimeTaken() > 1) {
                                        roboRaceTeamData.add(mRoboRace);
                                    }
                                }
                                for (int i = 0; i < roboRaceTeamData.size(); i++) {
                                    LeaderBoard leaderBoard = new LeaderBoard(roboRaceTeamData.get(i).getmTeamName().toString(), "Robo Race", (long) roboRaceTeamData.get(i).getmTotal(), (long) (i + 1), (long) roboRaceTeamData.size());
                                    mLeaderBoard.add(leaderBoard);
                                }
                                adapter = new LeaderBoardAdapter(ViewLeaderBoard.this, mLeaderBoard);
                                recyclerView.setAdapter(adapter);
                                recyclerView.smoothScrollToPosition(0);
                                adapter.notifyDataSetChanged();
                                progressDialog.dismiss();
                            });
                } else if (arrayAdapterEventType.getItem(position).equals("Line Follower")) {
                    FirebaseFirestore.getInstance().collection(arrayAdapterEventType.getItem(position))
                            .orderBy("mTotalTimeTaken", Query.Direction.ASCENDING)
                            .orderBy("mTotal", Query.Direction.DESCENDING).get()
                            .addOnSuccessListener(queryDocumentSnapshots -> {
                                mLeaderBoard.clear();
                                lineFollowerTeamData.clear();
                                List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                                for (DocumentSnapshot documentSnapshot : snapshotList) {
                                    NewLineFollowerTeamData mLineFollower = documentSnapshot.toObject(NewLineFollowerTeamData.class);
                                    if (Objects.requireNonNull(mLineFollower).getmTotalTimeTaken() > 1) {
                                        lineFollowerTeamData.add(mLineFollower);
                                    }
                                }

                                for (int i = 0; i < lineFollowerTeamData.size(); i++) {

                                    LeaderBoard leaderBoard = new LeaderBoard(lineFollowerTeamData.get(i).getmTeamName().toString(), "Line Follower", (long) lineFollowerTeamData.get(i).getmTotal(), (long) (i + 1), (long) lineFollowerTeamData.size());
                                    mLeaderBoard.add(leaderBoard);
                                }
                                adapter = new LeaderBoardAdapter(ViewLeaderBoard.this, mLeaderBoard);
                                recyclerView.setAdapter(adapter);
                                recyclerView.smoothScrollToPosition(0);
                                adapter.notifyDataSetChanged();
                                progressDialog.dismiss();
                            });
                }
            } else if (arrayAdapterEventType.getItem(position).equals("HackNation") | arrayAdapterEventType.getItem(position).equals("Ideate - 1") | arrayAdapterEventType.getItem(position).equals("Ideate - 2")) {
                FirebaseFirestore.getInstance().collection(arrayAdapterEventType.getItem(position)).orderBy("mFinalMark", Query.Direction.DESCENDING).get().addOnSuccessListener(queryDocumentSnapshots -> {
                    if (arrayAdapterEventType.getItem(position).equals("HackNation")) {
                        mLeaderBoard.clear();
                        hackNationTeamData.clear();
                        List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot documentSnapshot : snapshotList) {
                            NewHackNationTeamData mHackNation = documentSnapshot.toObject(NewHackNationTeamData.class);
                            if (Objects.requireNonNull(mHackNation).getmFinalMark() > 1) {
                                hackNationTeamData.add(mHackNation);
                            }
                        }

                        for (int i = 0; i < hackNationTeamData.size(); i++) {
                            LeaderBoard leaderBoard = new LeaderBoard(hackNationTeamData.get(i).getmTeamName().toString(), "HackNation", (long) hackNationTeamData.get(i).getmFinalMark(), (long) (i + 1), (long) hackNationTeamData.size());
                            mLeaderBoard.add(leaderBoard);
                        }
                        adapter = new LeaderBoardAdapter(ViewLeaderBoard.this, mLeaderBoard);
                        recyclerView.setAdapter(adapter);
                        recyclerView.smoothScrollToPosition(0);
                        adapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                    } else if (arrayAdapterEventType.getItem(position).equals("Ideate - 1")) {
                        mLeaderBoard.clear();
                        ideateTeamData.clear();
                        List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot documentSnapshot : snapshotList) {
                            NewIdeateTeamData mIdeate = documentSnapshot.toObject(NewIdeateTeamData.class);
                            if (Objects.requireNonNull(mIdeate).getmFinalMark() > 1) {
                                ideateTeamData.add(mIdeate);
                            }
                        }
                        for (int i = 0; i < ideateTeamData.size(); i++) {
                            LeaderBoard leaderBoard = new LeaderBoard(ideateTeamData.get(i).getmTeamName().toString(), "Ideate - 1", (long) ideateTeamData.get(i).getmFinalMark(), (long) (i + 1), (long) ideateTeamData.size());
                            mLeaderBoard.add(leaderBoard);
                        }
                        adapter = new LeaderBoardAdapter(ViewLeaderBoard.this, mLeaderBoard);
                        recyclerView.setAdapter(adapter);
                        recyclerView.smoothScrollToPosition(0);
                        adapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                    } else if (arrayAdapterEventType.getItem(position).equals("Ideate - 2")) {
                        mLeaderBoard.clear();
                        ideateTeamData.clear();
                        List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot documentSnapshot : snapshotList) {
                            NewIdeateTeamData mIdeate = documentSnapshot.toObject(NewIdeateTeamData.class);
                            if (Objects.requireNonNull(mIdeate).getmFinalMark() > 1) {
                                ideateTeamData.add(mIdeate);
                            }
                        }

                        for (int i = 0; i < ideateTeamData.size(); i++) {
                            LeaderBoard leaderBoard = new LeaderBoard(ideateTeamData.get(i).getmTeamName().toString(), "Ideate - 2", (long) ideateTeamData.get(i).getmFinalMark(), (long) (i + 1), (long) ideateTeamData.size());
                            mLeaderBoard.add(leaderBoard);
                        }
                        adapter = new LeaderBoardAdapter(ViewLeaderBoard.this, mLeaderBoard);
                        recyclerView.setAdapter(adapter);
                        recyclerView.smoothScrollToPosition(0);
                        adapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                    }

                });


            }

        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), AdminDashboard.class));
        super.onBackPressed();
    }

    private void showCustomDialog() {

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ViewLeaderBoard.this);
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
    private boolean isConnected(ViewLeaderBoard userLogin) {

        ConnectivityManager connectivityManager = (ConnectivityManager) userLogin.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo bluetoothConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_BLUETOOTH);

        return (wifiConn != null && wifiConn.isConnected()) || (mobileConn != null && mobileConn.isConnected() || (bluetoothConn != null && bluetoothConn.isConnected())); // if true ,  else false

    }
}