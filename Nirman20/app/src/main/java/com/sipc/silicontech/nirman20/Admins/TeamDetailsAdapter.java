package com.sipc.silicontech.nirman20.Admins;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ramotion.foldingcell.FoldingCell;
import com.sipc.silicontech.nirman20.R;

import java.util.ArrayList;

public class TeamDetailsAdapter extends RecyclerView.Adapter<TeamDetailsAdapter.TeamViewHolder> {

    private Context context;
    private ArrayList<NewHackNationTeamData> newHackNationTeamData = new ArrayList<NewHackNationTeamData>();


    public TeamDetailsAdapter(Context context) {
        this.context = context;
    }
    public void setDataToAdapter(ArrayList<NewHackNationTeamData> list) {
        newHackNationTeamData = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TeamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.team_layout_hacknation, parent, false);
        return new TeamViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamViewHolder holder, int position) {
        NewHackNationTeamData item = newHackNationTeamData.get(position);
        holder.mClgName.setText(item.getmCollegeName());
        holder.mPhone1.setText(item.getmTeamLeadPhone());
        holder.mName1.setText(item.getmTeamLead());
        holder.mName2.setText(item.getmMem1Name());
        holder.mPhone2.setText(item.getmMem1Phone());
        holder.mName3.setText(item.getmMem2Name());
        holder.mPhone3.setText(item.getmMem2Phone());
        holder.mName4.setText(item.getmMem3Name());
        holder.mPhone4.setText(item.getmMem3Phone());
        holder.mTeamName.setText(item.getmTeamName());
        holder.mFinalMark.setText("" + item.getmFinalMark());
        holder.foldingCell.initialize(1000, Color.DKGRAY, 2);

        holder.foldingCell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.foldingCell.toggle(false);
            }
        });
    }



    @Override
    public int getItemCount() {
        return newHackNationTeamData.size();
    }

    public class TeamViewHolder extends RecyclerView.ViewHolder {
        private FoldingCell foldingCell;
        private TextView mClgName, mTeamName, mPhone1, mPhone2, mPhone3, mPhone4, mName1, mName2, mName3, mName4, mFinalMark;
        private TextView mCheckedIn;

        public TeamViewHolder(@NonNull View itemView) {
            super(itemView);

            foldingCell = itemView.findViewById(R.id.folding_cell);
            mCheckedIn = itemView.findViewById(R.id.tv_CheckinStatus);
            mFinalMark = itemView.findViewById(R.id.tv_Final_Mark);


        }
    }
}
