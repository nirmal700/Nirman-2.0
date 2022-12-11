package com.sipc.silicontech.nirman20.Admins;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ramotion.foldingcell.FoldingCell;
import com.sipc.silicontech.nirman20.R;

import java.util.List;

public class ParticipantTeamsAdapter extends RecyclerView.Adapter<ParticipantTeamsAdapter.TeamHolder> {
    private final Context mContext;
    private final List<NewHackNationTeamData> hackNationTeamData;

    public ParticipantTeamsAdapter(Context mContext, List<NewHackNationTeamData> hackNationTeamData) {
        this.mContext = mContext;
        this.hackNationTeamData = hackNationTeamData;
    }

    @NonNull
    @Override
    public TeamHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.team_layout_hacknation,parent,false);
        return new TeamHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamHolder holder, int position) {
        NewHackNationTeamData TeamData = hackNationTeamData.get(position);
        holder.mFinalMark.setText(String.format("%s", TeamData.getmFinalMark()));
        holder.mApporach.setText(TeamData.getmApproach());
        holder.mProblem.setText(TeamData.getmProblemStat());
        holder.mTeamLeadph.setText(TeamData.getmTeamLeadPhone());
        holder.mTeamLeadName.setText(TeamData.getmTeamLead());
        holder.mClgName.setText(TeamData.getmCollegeName());
        holder.mTeamName.setText(TeamData.getmTeamName());
        holder.foldingCell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.foldingCell.toggle(false);
            }
        });
    }

    @Override
    public int getItemCount() {
        return hackNationTeamData.size();
    }

    public class TeamHolder extends RecyclerView.ViewHolder  {
        private TextView mClgName, mTeamName, mTeamLeadph, mTeamLeadName, mProblem, mApporach, mFinalMark;
        private FoldingCell foldingCell;
        public TeamHolder(@NonNull View itemView) {
            super(itemView);
            foldingCell = itemView.findViewById(R.id.folding_cell);
            mClgName = itemView.findViewById(R.id.tv_Clg_Name);
            mTeamLeadName = itemView.findViewById(R.id.tv_Team_Lead_Name);
            mTeamLeadph = itemView.findViewById(R.id.tv_Team_Lead_Phone);
            mTeamName = itemView.findViewById(R.id.tv_team_Name);
            mProblem = itemView.findViewById(R.id.tv_Problem_Statement);
            mApporach = itemView.findViewById(R.id.tv_Approach);
            mFinalMark = itemView.findViewById(R.id.tv_Final_Mark);
        }

    }
}
