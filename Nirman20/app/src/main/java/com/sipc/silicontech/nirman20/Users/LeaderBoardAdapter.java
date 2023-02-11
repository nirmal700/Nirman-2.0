package com.sipc.silicontech.nirman20.Users;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
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
        View v = LayoutInflater.from(mContext).inflate(R.layout.list_suggestion, parent, false);
        return new LeaderBoardViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderBoardViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class LeaderBoardViewHolder extends RecyclerView.ViewHolder {
        public LeaderBoardViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
