package com.sipc.silicontech.nirman20.Users;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.sipc.silicontech.nirman20.R;

import java.util.List;

public class LeaderBoardAdapter extends RecyclerView.Adapter<LeaderBoardAdapter.LeaderBoardViewHolder> {
    private final Context mContext;
    private final List<LeaderBoard> mLeaderBoard;

    public LeaderBoardAdapter(Context mContext, List<LeaderBoard> mLeaderBoard) {
        this.mContext = mContext;
        this.mLeaderBoard = mLeaderBoard;
    }

    @NonNull
    @Override
    public LeaderBoardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.list_new_leaderboard, parent, false);
        return new LeaderBoardViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderBoardViewHolder holder, int position) {
        LeaderBoard leaderBoard = mLeaderBoard.get(position);
        holder.tv_TeamName.setText(leaderBoard.getmTeamName());
        double mMid = Double.parseDouble(String.valueOf(Math.ceil(mLeaderBoard.size() / 3)));
        if(mMid == 0){
            mMid = 1;
        }
        double mPos = leaderBoard.getmPosition();
        if (mPos >= 0 & mPos <= mMid) {
            Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.bg_leader_grey);
            holder.mLinearLayout.setBackground(drawable);
        } else if (mPos > mMid & mPos <= 2 * mMid) {
            Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.bg_leader_yellow);
            holder.mLinearLayout.setBackground(drawable);
        } else if (mPos > 2 * mMid) {
            Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.bg_leader_red);
            holder.mLinearLayout.setBackground(drawable);
        }
        holder.tv_position.setText("Position: "+leaderBoard.getmPosition());
        holder.tv_eventName.setText("Event: "+leaderBoard.getmEvent());
        holder.tv_mFinalMark.setText("Final Mark: "+leaderBoard.getmFinalMark());
    }

    @Override
    public int getItemCount() {
        return mLeaderBoard.size();
    }

    public class LeaderBoardViewHolder extends RecyclerView.ViewHolder {
        TextView tv_TeamName;
        TextView tv_eventName;
        TextView tv_mFinalMark;
        TextView tv_position;
        LinearLayout mLinearLayout;
        public LeaderBoardViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_TeamName = itemView.findViewById(R.id.tv_TeamName);
            mLinearLayout = itemView.findViewById(R.id.mLinearLayout);
            tv_eventName = itemView.findViewById(R.id.mEvent);
            tv_mFinalMark = itemView.findViewById(R.id.mFinalMark);
            tv_position = itemView.findViewById(R.id.mPosition);

        }
    }
}
