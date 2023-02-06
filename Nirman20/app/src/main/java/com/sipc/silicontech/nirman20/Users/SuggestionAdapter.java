package com.sipc.silicontech.nirman20.Users;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sipc.silicontech.nirman20.R;

import java.util.List;

public class SuggestionAdapter extends RecyclerView.Adapter<SuggestionAdapter.SuggestionViewHolder> {

    private final Context mContext;
    private final List<Suggestion> mSuggestion;

    public SuggestionAdapter(Context mContext, List<Suggestion> mSuggestion) {
        this.mContext = mContext;
        this.mSuggestion = mSuggestion;
    }


    @NonNull
    @Override
    public SuggestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.list_suggestion, parent, false);
        return new SuggestionViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull SuggestionViewHolder holder, int position) {

        Suggestion suggestion = mSuggestion.get(position);
        holder.mTextSuggestion.setText(suggestion.getmSuggestion());
        holder.mTagLine.setText("Nice Work!");
        SessionManagerParticipant managerParticipant = new SessionManagerParticipant(mContext.getApplicationContext());
        String teamname = managerParticipant.getTeamName();
        String event = managerParticipant.getEventName();
        holder.btn_delete.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

            builder.setTitle("Confirm");
            builder.setMessage("Are you sure?");

            builder.setPositiveButton("YES", (dialog, which) -> {

                String idUpdate = suggestion.getmId();
                FirebaseDatabase.getInstance().getReference("Suggestions_Team").child(event).child(teamname).child("Suggestions").child(idUpdate).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        snapshot.getRef().removeValue();
                        notifyDataSetChanged();
                        Toast.makeText(mContext, "Item Deleted..", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                dialog.dismiss();
            });

            builder.setNegativeButton("NO", (dialog, which) -> {

                // Do nothing
                dialog.dismiss();
            });

            AlertDialog alert = builder.create();
            alert.show();

        });

    }

    @Override
    public int getItemCount() {
        return mSuggestion.size();
    }

    public static class SuggestionViewHolder extends RecyclerView.ViewHolder {
        TextView mTextSuggestion, mTagLine;
        Button btn_delete;

        public SuggestionViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextSuggestion = itemView.findViewById(R.id.tv_suggestion);
            mTagLine = itemView.findViewById(R.id.tv_tagLine);
            btn_delete = itemView.findViewById(R.id.btn_delete);
        }
    }
}
