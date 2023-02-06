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

public class RequestHelpAdapter extends RecyclerView.Adapter<RequestHelpAdapter.RequestHelpViewHolder> {
    private final Context mContext;
    private final List<Help> mHelp;

    public RequestHelpAdapter(Context mContext, List<Help> mHelp) {
        this.mContext = mContext;
        this.mHelp = mHelp;
    }

    @NonNull
    @Override
    public RequestHelpViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.list_help, parent, false);
        return new RequestHelpViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestHelpViewHolder holder, int position) {
        Help help = mHelp.get(position);
        holder.mTag.setText(help.getmIssueType());
        holder.mDesc.setText(help.getmDescription());
        SessionManagerParticipant managerUser;
        managerUser = new SessionManagerParticipant(mContext.getApplicationContext());
        holder.mDelete.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

            builder.setTitle("Confirm");
            builder.setMessage("Are you sure?");

            builder.setPositiveButton("YES", (dialog, which) -> {

                String idUpdate = help.getmId();
                FirebaseDatabase.getInstance().getReference("EventIssues").child(managerUser.getEventName()).child(managerUser.getTeamName()).child("Issues").child(idUpdate).addListenerForSingleValueEvent(new ValueEventListener() {
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
        return mHelp.size();
    }

    public static class RequestHelpViewHolder extends RecyclerView.ViewHolder {
        TextView mTag, mDesc;
        Button mDelete;

        public RequestHelpViewHolder(@NonNull View itemView) {
            super(itemView);
            mTag = itemView.findViewById(R.id.tv_Tag);
            mDesc = itemView.findViewById(R.id.tv_description);
            mDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}
