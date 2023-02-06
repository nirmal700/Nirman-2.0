package com.sipc.silicontech.nirman20.Admins;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sipc.silicontech.nirman20.R;

import java.util.Objects;

public class PublishResults extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.publish_results);

        CollectionReference mCollectionReference = FirebaseFirestore.getInstance().collection("HackNation");
        mCollectionReference.get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (DocumentSnapshot document1 : queryDocumentSnapshots.getDocuments()) {
                Log.d("45567Hack", document1.getId() + " => " + document1.getData());
                FirebaseFirestore.getInstance().collection("HackNation Evaluation").whereEqualTo("mTeamName", document1.get("mTeamName")).addSnapshotListener((value, error) -> {
                    double sum = 0;
                    Log.e("45567", "onSuccess: " + Objects.requireNonNull(value).getDocuments());
                    for (DocumentSnapshot document : value.getDocuments()) {
                        Log.d("45567InnerFOr", document.getId() + " => " + document.getData());
                        sum = sum + (double) document.get("mAvg");
                    }
                    double avg = sum / value.size();
                    Log.e("45567AVg", "onSuccess: " + avg + "=>>" + document1.get("mTeamName"));
                    FirebaseFirestore.getInstance().collection("HackNation").document(Objects.requireNonNull(document1.get("mTeamName")).toString()).update("mRound1Eva", avg)
                            .addOnSuccessListener(unused -> Log.e("45567Update", "onSuccess: "));
                });
            }

        });
    }
}