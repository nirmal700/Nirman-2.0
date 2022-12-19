package com.sipc.silicontech.nirman20.Admins;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ramotion.foldingcell.FoldingCell;
import com.sipc.silicontech.nirman20.R;

import org.w3c.dom.Text;

import java.util.List;

public class MultiViewAdapter extends RecyclerView.Adapter {

    private Context context;
    private List list;

    public MultiViewAdapter(Context context, List list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getItemViewType(int position) {
        if(list.get(position) instanceof NewHackNationTeamData)
            return 0;
        else if (list.get(position) instanceof NewIdeateTeamData)
            return 1;
        else if(list.get(position) instanceof NewRoboRaceTeamData)
            return 2;
        else
            return 3;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.e("Fatal", "onCreateViewHolder:View Type "+viewType );
        if(viewType == 0)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.team_layout_hacknation,parent,false);
            HackNation holder = new HackNation(view);
            Log.e("2255", "onCreateViewHolder: "+"HAckationCalled"+list.toString() );
            return holder;
        }
        else if(viewType == 1)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.team_layout_ideate,parent,false);
            Ideate holder = new Ideate(view);
            Log.e("2255", "onCreateViewHolder: "+"IdeatenCalled" );
            return holder;
        }
        else if(viewType == 2)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.team_layout_roborace,parent,false);
            RoboRace holder = new RoboRace(view);
            Log.e("2255", "onCreateViewHolder: "+"RoboRace Called" );
            return holder;
        }
        else
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.team_layout_line_follower,parent,false);
            LineFollower holder = new LineFollower(view);
            Log.e("2255", "onCreateViewHolder: "+"LineFollowerCalled" );
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(this.getItemViewType(position) == 0)
        {
            NewHackNationTeamData hackNationTeamData = (NewHackNationTeamData) list.get(position);
            HackNation HackNationHolder = (HackNation)holder;
            ((HackNation) HackNationHolder).mTeamName.setText(hackNationTeamData.getmTeamName());
            ((HackNation) HackNationHolder).mCollegeName.setText(hackNationTeamData.getmCollegeName());
            ((HackNation) HackNationHolder).mTeamLeadName.setText(hackNationTeamData.getmTeamLead());
            ((HackNation) HackNationHolder).mFinalMark.setText(""+ hackNationTeamData.getmFinalMark());
            ((HackNation) HackNationHolder).mApproach.setText(hackNationTeamData.getmApproach());
            ((HackNation) HackNationHolder).mProblemStatement.setText(hackNationTeamData.getmProblemStat());
            ((HackNation) HackNationHolder).mTeamLeadPhone.setText(hackNationTeamData.getmTeamLeadPhone());
            ((HackNation) HackNationHolder).mEventType.setText(hackNationTeamData.getmEventParticipating());
            ((HackNation) HackNationHolder).foldingCell.initialize(1000, Color.DKGRAY, 2);

            ((HackNation) HackNationHolder).foldingCell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((HackNation) HackNationHolder).foldingCell.toggle(false);
                }
            });

        }
        else if(this.getItemViewType(position) == 1)
        {
            NewIdeateTeamData ideateTeamData = (NewIdeateTeamData) list.get(position);
            Ideate IdeateHolder = (Ideate) holder;
            ((Ideate) IdeateHolder).mTeamName.setText(ideateTeamData.getmTeamName());
            ((Ideate) IdeateHolder).mTeamName.setText(ideateTeamData.getmTeamName());
            ((Ideate) IdeateHolder).mCollegeName.setText(ideateTeamData.getmCollegeName());
            ((Ideate) IdeateHolder).mTeamLeadName.setText(ideateTeamData.getmTeamLead());
            ((Ideate) IdeateHolder).mFinalMark.setText(""+ ideateTeamData.getmFinalMark());
            ((Ideate) IdeateHolder).mApproach.setText(ideateTeamData.getmApproach());
            ((Ideate) IdeateHolder).mProblemStatement.setText(ideateTeamData.getmProblemStat());
            ((Ideate) IdeateHolder).mTeamLeadPhone.setText(ideateTeamData.getmTeamLeadPhone());
            ((Ideate) IdeateHolder).mEventType.setText(ideateTeamData.getmEventParticipating());
            ((Ideate) IdeateHolder).foldingCell.initialize(1000, Color.DKGRAY, 2);
            ((Ideate) IdeateHolder).foldingCell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((Ideate) IdeateHolder).foldingCell.toggle(false);
                }
            });
        }
        else if(this.getItemViewType(position) == 2){
            NewRoboRaceTeamData roboRaceTeamData = (NewRoboRaceTeamData) list.get(position);
            RoboRace RoboHolder = (RoboRace) holder;
            ((RoboRace) RoboHolder).mTeamName.setText(roboRaceTeamData.getmTeamName());
            ((RoboRace) RoboHolder).mTeamLeadName.setText(roboRaceTeamData.getmTeamLead());
            ((RoboRace) RoboHolder).mTeamLeadPhone.setText(roboRaceTeamData.getmTeamLeadPhone());
            ((RoboRace) RoboHolder).mCollegeName.setText(roboRaceTeamData.getmCollegeName());
            ((RoboRace) RoboHolder).mEventType.setText(roboRaceTeamData.getmEventParticipating());
            ((RoboRace) RoboHolder).mBonus.setText(roboRaceTeamData.getmBonus());
            ((RoboRace) RoboHolder).mCheckPointsCleared.setText(roboRaceTeamData.getmCheckPointCleared());
            ((RoboRace) RoboHolder).mCheckPointsSkipped.setText(roboRaceTeamData.getmCheckPointSkipped());
            ((RoboRace) RoboHolder).mHandtouches.setText(roboRaceTeamData.getmHandTouches());
            ((RoboRace) RoboHolder).mTotalTimeTaken.setText(""+roboRaceTeamData.getmTotalTimeTaken());
            ((RoboRace) RoboHolder).mTotal.setText(""+roboRaceTeamData.getmTotal());
            ((RoboRace) RoboHolder).mTimeOutTaken.setText(""+roboRaceTeamData.ismTimeOutTaken());
            ((RoboRace) RoboHolder).foldingCell.initialize(1000, Color.DKGRAY, 2);
            ((RoboRace) RoboHolder).foldingCell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((RoboRace) RoboHolder).foldingCell.toggle(false);
                }
            });
        }
        else{
            NewLineFollowerTeamData lineFollowerTeamData = (NewLineFollowerTeamData) list.get(position);
            LineFollower LineHolder = (LineFollower) holder;
            ((LineFollower) LineHolder).mTeamName.setText(lineFollowerTeamData.getmTeamName());
            ((LineFollower) LineHolder).mTeamName.setText(lineFollowerTeamData.getmTeamName());
            ((LineFollower) LineHolder).mTeamLeadName.setText(lineFollowerTeamData.getmTeamLead());
            ((LineFollower) LineHolder).mTeamLeadPhone.setText(lineFollowerTeamData.getmTeamLeadPhone());
            ((LineFollower) LineHolder).mCollegeName.setText(lineFollowerTeamData.getmCollegeName());
            ((LineFollower) LineHolder).mEventType.setText(lineFollowerTeamData.getmEventParticipating());
            ((LineFollower) LineHolder).mBonus.setText(""+lineFollowerTeamData.getmBonus());
            ((LineFollower) LineHolder).mCheckPointsCleared.setText(""+lineFollowerTeamData.getmCheckPointCleared());
            ((LineFollower) LineHolder).mCheckPointsSkipped.setText(""+lineFollowerTeamData.getmCheckPointSkipped());
            ((LineFollower) LineHolder).mHandtouches.setText(""+lineFollowerTeamData.getmHandTouches());
            ((LineFollower) LineHolder).mTotalTimeTaken.setText(""+lineFollowerTeamData.getmTotalTimeTaken());
            ((LineFollower) LineHolder).mTotal.setText(""+lineFollowerTeamData.getmTotal());
            ((LineFollower) LineHolder).mTimeOutTaken.setText(""+lineFollowerTeamData.ismTimeOutTaken());
            ((LineFollower) LineHolder).foldingCell.initialize(1000, Color.DKGRAY, 2);
            ((LineFollower) LineHolder).foldingCell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((LineFollower) LineHolder).foldingCell.toggle(false);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class HackNation extends RecyclerView.ViewHolder{
        private TextView mTeamName;
        private TextView mCollegeName;
        private FoldingCell foldingCell;
        private TextView mEventType,mTeamLeadName,mTeamLeadPhone,mProblemStatement,mApproach,mFinalMark,mCheckinStatus;
        public HackNation(@NonNull View itemView) {
            super(itemView);
            mTeamName = itemView.findViewById(R.id.tv_team_Name);
            mCollegeName = itemView.findViewById(R.id.tv_Clg_Name);
            mEventType = itemView.findViewById(R.id.tv_Event);
            mCheckinStatus = itemView.findViewById(R.id.tv_CheckinStatus);
            mTeamLeadName =  itemView.findViewById(R.id.tv_Team_Lead_Name);
            mTeamLeadPhone =  itemView.findViewById(R.id.tv_Team_Lead_Phone);
            mProblemStatement =  itemView.findViewById(R.id.tv_Problem_Statement);
            mApproach =  itemView.findViewById(R.id.tv_Approach);
            mFinalMark =  itemView.findViewById(R.id.tv_Final_Mark);
            foldingCell = itemView.findViewById(R.id.folding_cell);
        }
    }
    static class Ideate extends RecyclerView.ViewHolder{


        private TextView mTeamName;
        private TextView mCollegeName;
        private FoldingCell foldingCell;
        private TextView mEventType,mTeamLeadName,mTeamLeadPhone,mProblemStatement,mApproach,mFinalMark,mCheckinStatus;
        public Ideate(@NonNull View itemView) {
            super(itemView);
            mTeamName = itemView.findViewById(R.id.tv_team_Name);
            mCollegeName = itemView.findViewById(R.id.tv_Clg_Name);
            mEventType = itemView.findViewById(R.id.tv_Event);
            mCheckinStatus = itemView.findViewById(R.id.tv_CheckinStatus);
            mTeamLeadName =  itemView.findViewById(R.id.tv_Team_Lead_Name);
            mTeamLeadPhone =  itemView.findViewById(R.id.tv_Lead_Phone);
            mProblemStatement =  itemView.findViewById(R.id.tv_Problem_Statement);
            mApproach =  itemView.findViewById(R.id.tv_Approach);
            mFinalMark =  itemView.findViewById(R.id.tv_Final_Mark);
            foldingCell = itemView.findViewById(R.id.folding_cell);
        }
    }
    static class RoboRace extends RecyclerView.ViewHolder{

        private TextView mTeamName;
        private TextView mCollegeName;
        private FoldingCell foldingCell;
        private TextView mEventType,mTeamLeadName,mTeamLeadPhone,mCheckinStatus,mCheckPointsCleared,mHandtouches,mBonus,mTimeOutTaken,mCheckPointsSkipped,mTotalTimeTaken,mTotal;
        public RoboRace(@NonNull View itemView) {
            super(itemView);
            mTeamName = itemView.findViewById(R.id.tv_team_Name);
            mCollegeName = itemView.findViewById(R.id.tv_Clg_Name);
            mEventType = itemView.findViewById(R.id.tv_Event);
            mCheckinStatus = itemView.findViewById(R.id.tv_CheckinStatus);
            mTeamLeadName =  itemView.findViewById(R.id.tv_Team_Lead_Name);
            mTeamLeadPhone =  itemView.findViewById(R.id.tv_Lead_Phone);
            mCheckPointsCleared = itemView.findViewById(R.id.tv_CheckPoints_Cleared);
            mHandtouches = itemView.findViewById(R.id.tv_HandTouches);
            mBonus = itemView.findViewById(R.id.tv_Bonus);
            mTimeOutTaken = itemView.findViewById(R.id.tv_TimeOutTaken);
            mCheckPointsSkipped = itemView.findViewById(R.id.tv_CheckPoints_Skipped);
            mTotalTimeTaken = itemView.findViewById(R.id.tv_TotalTimeTaken);
            mTotal = itemView.findViewById(R.id.tv_Final_Mark);
            foldingCell = itemView.findViewById(R.id.folding_cell);
        }
    }
    static class LineFollower extends RecyclerView.ViewHolder{

        private TextView mTeamName;
        private TextView mCollegeName;
        private FoldingCell foldingCell;
        private TextView mEventType,mTeamLeadName,mTeamLeadPhone,mCheckinStatus,mCheckPointsCleared,mHandtouches,mBonus,mTimeOutTaken,mCheckPointsSkipped,mTotalTimeTaken,mTotal;
        public LineFollower(@NonNull View itemView) {
            super(itemView);
            mTeamName = itemView.findViewById(R.id.tv_team_Name);
            mCollegeName = itemView.findViewById(R.id.tv_Clg_Name);
            mEventType = itemView.findViewById(R.id.tv_Event);
            mCheckinStatus = itemView.findViewById(R.id.tv_CheckinStatus);
            mTeamLeadName =  itemView.findViewById(R.id.tv_Team_Lead_Name);
            mTeamLeadPhone =  itemView.findViewById(R.id.tv_Lead_Phone);
            mCheckPointsCleared = itemView.findViewById(R.id.tv_CheckPoints_Cleared);
            mHandtouches = itemView.findViewById(R.id.tv_HandTouches);
            mBonus = itemView.findViewById(R.id.tv_Bonus);
            mTimeOutTaken = itemView.findViewById(R.id.tv_TimeOutTaken);
            mCheckPointsSkipped = itemView.findViewById(R.id.tv_CheckPoints_Skipped);
            mTotalTimeTaken = itemView.findViewById(R.id.tv_TotalTimeTaken);
            mTotal = itemView.findViewById(R.id.tv_Final_Mark);
            foldingCell = itemView.findViewById(R.id.folding_cell);
        }
    }
}
