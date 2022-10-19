package com.sipc.silicontech.nirman20.Admins;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sipc.silicontech.nirman20.R;

public class PublishResults extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.publish_results);

        CollectionReference mCollectionReference = FirebaseFirestore.getInstance().collection("HackNation");
        mCollectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot document1 : queryDocumentSnapshots.getDocuments()) {
                    Log.d("45567Hack", document1.getId() + " => " + document1.getData());
                    FirebaseFirestore.getInstance().collection("HackNation Evaluation").whereEqualTo("mTeamName",document1.get("mTeamName")).addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            double sum = 0;
                            Log.e("45567", "onSuccess: "+value.getDocuments().toString() );
                            for (DocumentSnapshot document : value.getDocuments()) {
                                Log.d("45567InnerFOr", document.getId() + " => " + document.getData());
                                sum =sum+(double) document.get("mAvg");
                            }
                            double avg = sum/value.size();
                            Log.e("45567AVg", "onSuccess: "+ avg +"=>>"+document1.get("mTeamName"));
                            FirebaseFirestore.getInstance().collection("HackNation").document(document1.get("mTeamName").toString()).update("mRound1Eva",avg)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.e("45567Update", "onSuccess: " );
                                        }
                                    });
                        }
                    });
                }

            }
        });
    }
}