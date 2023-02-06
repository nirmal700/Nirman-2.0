package com.sipc.silicontech.nirman20.Admins;

import android.content.Context;
import android.content.DialogInterface;
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
import com.sipc.silicontech.nirman20.Users.Help;
import com.sipc.silicontech.nirman20.Users.RequestHelpAdapter;
import com.sipc.silicontech.nirman20.Users.SessionManagerParticipant;

import java.util.List;

public class AddressIssuesAdapter extends RecyclerView.Adapter<AddressIssuesAdapter.AddressIssueViewHolder> {
    private final Context mContext;
    private final List<Help> mHelp;

    public AddressIssuesAdapter(Context mContext, List<Help> mHelp) {
        this.mContext = mContext;
        this.mHelp = mHelp;
    }

    @NonNull
    @Override
    public AddressIssueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.list_help_admin,parent,false);
        return new AddressIssueViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressIssueViewHolder holder, int position) {
        Help help = mHelp.get(position);
        holder.mTag.setText(help.getmIssueType());
        holder.mDesc.setText(help.getmDescription());
        holder.tv_EventName.setText("Event: "+help.getmEvent());
        holder.tv_TeamName.setText("Team: "+help.getmTeamname());
        holder.mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

                builder.setTitle("Confirm");
                builder.setMessage("Are you sure?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        String idUpdate = help.getmId();
                        FirebaseDatabase.getInstance().getReference("EventIssues").child(help.getmEvent()).child(help.getmTeamname()).child("Issues").child(idUpdate)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
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
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return mHelp.size();
    }

    public class AddressIssueViewHolder extends RecyclerView.ViewHolder {
        TextView mTag,mDesc,tv_TeamName,tv_EventName;
        Button mDelete;
        public AddressIssueViewHolder(@NonNull View itemView) {
            super(itemView);
            mTag = itemView.findViewById(R.id.tv_Tag);
            mDesc = itemView.findViewById(R.id.tv_description);
            mDelete = itemView.findViewById(R.id.btn_delete);
            tv_TeamName = itemView.findViewById(R.id.tv_TeamName);
            tv_EventName = itemView.findViewById(R.id.tv_TeamName);
        }
    }
}
