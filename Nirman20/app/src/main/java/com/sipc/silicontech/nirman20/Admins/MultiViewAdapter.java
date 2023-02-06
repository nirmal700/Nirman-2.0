package com.sipc.silicontech.nirman20.Admins;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ramotion.foldingcell.FoldingCell;
import com.sipc.silicontech.nirman20.R;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class MultiViewAdapter extends RecyclerView.Adapter {

    static int PERMISSION_CODE = 100;
    private final Context context;
    private final List list;
    private final List<NewIdeateTeamData> newIdeateTeamData = new ArrayList<>();
    private final List<NewHackNationTeamData> newHackNationTeamData = new ArrayList<>();
    private final List<NewRoboRaceTeamData> newRoboRaceTeamData = new ArrayList<>();
    private final List copyList;
    private final List<NewLineFollowerTeamData> newLineFollowerTeamData = new ArrayList<>();

    public MultiViewAdapter(Context context, List list) {
        this.context = context;
        this.list = list;
        this.copyList = new ArrayList<>();
        copyList.addAll(list);
        int n = list.size();
        newLineFollowerTeamData.clear();
        newRoboRaceTeamData.clear();
        newIdeateTeamData.clear();
        newHackNationTeamData.clear();
        for (int i = 0; i < n; i++) {
            if (this.list.get(i) instanceof NewHackNationTeamData) {
                newHackNationTeamData.add((NewHackNationTeamData) this.list.get(i));
            } else if (this.list.get(i) instanceof NewIdeateTeamData) {
                newIdeateTeamData.add((NewIdeateTeamData) this.list.get(i));
            } else if (this.list.get(i) instanceof NewRoboRaceTeamData) {
                newRoboRaceTeamData.add((NewRoboRaceTeamData) this.list.get(i));
            } else {
                newLineFollowerTeamData.add((NewLineFollowerTeamData) this.list.get(i));
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position) instanceof NewHackNationTeamData) return 0;
        else if (list.get(position) instanceof NewIdeateTeamData) return 1;
        else if (list.get(position) instanceof NewRoboRaceTeamData) return 2;
        else return 3;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.e("Fatal", "onCreateViewHolder:View Type " + viewType);
        if (viewType == 0) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.team_layout_hacknation, parent, false);
            HackNation holder = new HackNation(view);
            Log.e("2255", "onCreateViewHolder: " + "HAckationCalled" + list.toString());
            return holder;
        } else if (viewType == 1) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.team_layout_ideate, parent, false);
            Ideate holder = new Ideate(view);
            Log.e("2255", "onCreateViewHolder: " + "IdeatenCalled");
            return holder;
        } else if (viewType == 2) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.team_layout_roborace, parent, false);
            RoboRace holder = new RoboRace(view);
            Log.e("2255", "onCreateViewHolder: " + "RoboRace Called");
            return holder;
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.team_layout_line_follower, parent, false);
            LineFollower holder = new LineFollower(view);
            Log.e("2255", "onCreateViewHolder: " + "LineFollowerCalled");
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (this.getItemViewType(position) == 0) {
            NewHackNationTeamData hackNationTeamData = (NewHackNationTeamData) list.get(position);
            HackNation HackNationHolder = (HackNation) holder;
            HackNationHolder.mTeamName.setText(hackNationTeamData.getmTeamName());
            HackNationHolder.mCollegeName.setText(hackNationTeamData.getmCollegeName());
            HackNationHolder.mTeamLeadName.setText(hackNationTeamData.getmTeamLead());
            HackNationHolder.mFinalMark.setText("" + hackNationTeamData.getmFinalMark());
            HackNationHolder.mApproach.setText(hackNationTeamData.getmApproach());
            HackNationHolder.mProblemStatement.setText(hackNationTeamData.getmProblemStat());
            HackNationHolder.mTeamLeadPhone.setText(hackNationTeamData.getmTeamLeadPhone());
            HackNationHolder.mEventType.setText(hackNationTeamData.getmEventParticipating());
            if (hackNationTeamData.ismCheckedIn()) {
                HackNationHolder.mCheckinStatus.setText("Checked-In");
                HackNationHolder.mCheckinStatus.setBackgroundColor(Color.GREEN);
            } else {
                HackNationHolder.mCheckinStatus.setText("Not Checked-In");
                HackNationHolder.mCheckinStatus.setBackgroundColor(Color.RED);
            }
            HackNationHolder.mWhatsApp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String phone = hackNationTeamData.getmTeamLeadPhone();

                    Intent whatsapp = new Intent(Intent.ACTION_VIEW);

                    try {
                        String url = "https://api.whatsapp.com/send?phone=" + phone + "&text=" + URLEncoder.encode("Hey,\n", "UTF-8");
                        whatsapp.setPackage("com.whatsapp");
                        whatsapp.setData(Uri.parse(url));
                        context.startActivity(whatsapp);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(context, "Contact " + hackNationTeamData.getmTeamName() + " via Whatsapp", Toast.LENGTH_SHORT).show();
                }
            });
            HackNationHolder.mCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        String dail = "tel:" + hackNationTeamData.getmTeamLeadPhone();
                        context.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dail)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(context, "Contact " + hackNationTeamData.getmTeamName() + " via Call", Toast.LENGTH_SHORT).show();


                }
            });
            HackNationHolder.foldingCell.initialize(1000, Color.DKGRAY, 2);

            HackNationHolder.foldingCell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HackNationHolder.foldingCell.toggle(false);
                }
            });

        } else if (this.getItemViewType(position) == 1) {
            NewIdeateTeamData ideateTeamData = (NewIdeateTeamData) list.get(position);
            Ideate IdeateHolder = (Ideate) holder;
            IdeateHolder.mTeamName.setText(ideateTeamData.getmTeamName());
            IdeateHolder.mTeamName.setText(ideateTeamData.getmTeamName());
            IdeateHolder.mCollegeName.setText(ideateTeamData.getmCollegeName());
            IdeateHolder.mTeamLeadName.setText(ideateTeamData.getmTeamLead());
            IdeateHolder.mFinalMark.setText("" + ideateTeamData.getmFinalMark());
            IdeateHolder.mApproach.setText(ideateTeamData.getmApproach());
            IdeateHolder.mProblemStatement.setText(ideateTeamData.getmProblemStat());
            IdeateHolder.mTeamLeadPhone.setText(ideateTeamData.getmTeamLeadPhone());
            IdeateHolder.mEventType.setText(ideateTeamData.getmEventParticipating());
            if (ideateTeamData.ismCheckedIn()) {
                IdeateHolder.mCheckinStatus.setText("Checked-In");
                IdeateHolder.mCheckinStatus.setBackgroundColor(Color.GREEN);
            } else {
                IdeateHolder.mCheckinStatus.setText("Not Checked-In");
                IdeateHolder.mCheckinStatus.setBackgroundColor(Color.RED);
            }
            IdeateHolder.mWhatsApp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String phone = ideateTeamData.getmTeamLeadPhone();

                    Intent whatsapp = new Intent(Intent.ACTION_VIEW);

                    try {
                        String url = "https://api.whatsapp.com/send?phone=" + phone + "&text=" + URLEncoder.encode("Hey,\n", "UTF-8");
                        whatsapp.setPackage("com.whatsapp");
                        whatsapp.setData(Uri.parse(url));
                        context.startActivity(whatsapp);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(context, "Contact " + ideateTeamData.getmTeamName() + " via Whatsapp", Toast.LENGTH_SHORT).show();
                }
            });
            IdeateHolder.mCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        String dail = "tel:" + ideateTeamData.getmTeamLeadPhone();
                        context.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dail)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(context, "Contact " + ideateTeamData.getmTeamName() + " via Call", Toast.LENGTH_SHORT).show();


                }
            });
            IdeateHolder.foldingCell.initialize(1000, Color.DKGRAY, 2);
            IdeateHolder.foldingCell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IdeateHolder.foldingCell.toggle(false);
                }
            });
        } else if (this.getItemViewType(position) == 2) {
            NewRoboRaceTeamData roboRaceTeamData = (NewRoboRaceTeamData) list.get(position);
            RoboRace RoboHolder = (RoboRace) holder;
            RoboHolder.mTeamName.setText(roboRaceTeamData.getmTeamName());
            RoboHolder.mTeamLeadName.setText("" + roboRaceTeamData.getmTeamLead());
            RoboHolder.mTeamLeadPhone.setText(roboRaceTeamData.getmTeamLeadPhone());
            RoboHolder.mCollegeName.setText(roboRaceTeamData.getmCollegeName());
            RoboHolder.mEventType.setText(roboRaceTeamData.getmEventParticipating());
            RoboHolder.mBonus.setText("" + roboRaceTeamData.getmBonus());
            RoboHolder.mCheckPointsCleared.setText("" + roboRaceTeamData.getmCheckPointCleared());
            RoboHolder.mCheckPointsSkipped.setText("" + roboRaceTeamData.getmCheckPointSkipped());
            RoboHolder.mHandtouches.setText("" + roboRaceTeamData.getmHandTouches());
            RoboHolder.mTotalTimeTaken.setText("" + roboRaceTeamData.getmTotalTimeTaken());
            RoboHolder.mTotal.setText("" + roboRaceTeamData.getmTotal());
            RoboHolder.mTimeOutTaken.setText("" + roboRaceTeamData.ismTimeOutTaken());
            if (roboRaceTeamData.ismCheckedIn()) {
                RoboHolder.mCheckinStatus.setText("Checked-In");
                RoboHolder.mCheckinStatus.setBackgroundColor(Color.GREEN);
            } else {
                RoboHolder.mCheckinStatus.setText("Not Checked-In");
                RoboHolder.mCheckinStatus.setBackgroundColor(Color.RED);
            }
            RoboHolder.mWhatsApp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String phone = roboRaceTeamData.getmTeamLeadPhone();

                    Intent whatsapp = new Intent(Intent.ACTION_VIEW);

                    try {
                        String url = "https://api.whatsapp.com/send?phone=" + phone + "&text=" + URLEncoder.encode("Hey,\n", "UTF-8");
                        whatsapp.setPackage("com.whatsapp");
                        whatsapp.setData(Uri.parse(url));
                        context.startActivity(whatsapp);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(context, "Contact " + roboRaceTeamData.getmTeamName() + " via Whatsapp", Toast.LENGTH_SHORT).show();
                }
            });
            RoboHolder.mCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        String dail = "tel:" + roboRaceTeamData.getmTeamLeadPhone();
                        context.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dail)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(context, "Contact " + roboRaceTeamData.getmTeamName() + " via Call", Toast.LENGTH_SHORT).show();


                }
            });
            RoboHolder.foldingCell.initialize(1000, Color.DKGRAY, 2);
            RoboHolder.foldingCell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RoboHolder.foldingCell.toggle(false);
                }
            });

        } else {
            NewLineFollowerTeamData lineFollowerTeamData = (NewLineFollowerTeamData) list.get(position);
            LineFollower LineHolder = (LineFollower) holder;
            LineHolder.mTeamName.setText(lineFollowerTeamData.getmTeamName());
            LineHolder.mTeamName.setText(lineFollowerTeamData.getmTeamName());
            LineHolder.mTeamLeadName.setText(lineFollowerTeamData.getmTeamLead());
            LineHolder.mTeamLeadPhone.setText(lineFollowerTeamData.getmTeamLeadPhone());
            LineHolder.mCollegeName.setText(lineFollowerTeamData.getmCollegeName());
            LineHolder.mEventType.setText(lineFollowerTeamData.getmEventParticipating());
            LineHolder.mBonus.setText("" + lineFollowerTeamData.getmBonus());
            LineHolder.mCheckPointsCleared.setText("" + lineFollowerTeamData.getmCheckPointCleared());
            LineHolder.mCheckPointsSkipped.setText("" + lineFollowerTeamData.getmCheckPointSkipped());
            LineHolder.mHandtouches.setText("" + lineFollowerTeamData.getmHandTouches());
            LineHolder.mTotalTimeTaken.setText("" + lineFollowerTeamData.getmTotalTimeTaken());
            LineHolder.mTotal.setText("" + lineFollowerTeamData.getmTotal());
            LineHolder.mTimeOutTaken.setText("" + lineFollowerTeamData.ismTimeOutTaken());
            if (lineFollowerTeamData.ismCheckedIn()) {
                LineHolder.mCheckinStatus.setText("Checked-In");
                LineHolder.mCheckinStatus.setBackgroundColor(Color.GREEN);
            } else {
                LineHolder.mCheckinStatus.setText("Not Checked-In");
                LineHolder.mCheckinStatus.setBackgroundColor(Color.RED);
            }
            LineHolder.mWhatsApp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String phone = lineFollowerTeamData.getmTeamLeadPhone();

                    Intent whatsapp = new Intent(Intent.ACTION_VIEW);

                    try {
                        String url = "https://api.whatsapp.com/send?phone=" + phone + "&text=" + URLEncoder.encode("Hey,\n", "UTF-8");
                        whatsapp.setPackage("com.whatsapp");
                        whatsapp.setData(Uri.parse(url));
                        context.startActivity(whatsapp);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(context, "Contact " + lineFollowerTeamData.getmTeamName() + " via Whatsapp", Toast.LENGTH_SHORT).show();
                }
            });
            LineHolder.mCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        String dail = "tel:" + lineFollowerTeamData.getmTeamLeadPhone();
                        context.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dail)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(context, "Contact " + lineFollowerTeamData.getmTeamName() + " via Call", Toast.LENGTH_SHORT).show();


                }
            });
            LineHolder.foldingCell.initialize(1000, Color.DKGRAY, 2);
            LineHolder.foldingCell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LineHolder.foldingCell.toggle(false);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void Search(CharSequence txt) {
        txt = txt.toString().toLowerCase();
        List searchList = new ArrayList<>();


        if (!TextUtils.isEmpty(txt)) {
            for (NewHackNationTeamData data : newHackNationTeamData) {
                if (data.getmTeamName().toLowerCase().contains(txt) || data.getmCollegeName().toLowerCase().contains(txt)) {
                    searchList.add(data);
                }
            }
            for (NewIdeateTeamData data : newIdeateTeamData) {
                if (data.getmTeamName().toLowerCase().contains(txt) || data.getmCollegeName().toLowerCase().contains(txt)) {
                    searchList.add(data);
                }
            }
            for (NewRoboRaceTeamData data : newRoboRaceTeamData) {
                if (data.getmTeamName().toLowerCase().contains(txt) || data.getmCollegeName().toLowerCase().contains(txt)) {
                    searchList.add(data);
                }
            }
            for (NewLineFollowerTeamData data : newLineFollowerTeamData) {
                if (data.getmTeamName().toLowerCase().contains(txt) || data.getmCollegeName().toLowerCase().contains(txt)) {
                    searchList.add(data);
                }
            }
        } else {
            searchList.addAll(copyList);
        }
        list.clear();
        list.addAll(searchList);
        notifyDataSetChanged();
        searchList.clear();
    }

    static class HackNation extends RecyclerView.ViewHolder {
        private final TextView mTeamName;
        private final TextView mCollegeName;
        private final FoldingCell foldingCell;
        private final TextView mEventType;
        private final TextView mTeamLeadName;
        private final TextView mTeamLeadPhone;
        private final TextView mProblemStatement;
        private final TextView mApproach;
        private final TextView mFinalMark;
        private final TextView mCheckinStatus;
        private final Button mCall;
        private final ImageView mWhatsApp;

        public HackNation(@NonNull View itemView) {
            super(itemView);
            mTeamName = itemView.findViewById(R.id.tv_team_Name);
            mCollegeName = itemView.findViewById(R.id.tv_Clg_Name);
            mEventType = itemView.findViewById(R.id.tv_Event);
            mCheckinStatus = itemView.findViewById(R.id.tv_CheckinStatus);
            mTeamLeadName = itemView.findViewById(R.id.tv_Team_Lead_Name);
            mTeamLeadPhone = itemView.findViewById(R.id.tv_Team_Lead_Phone);
            mProblemStatement = itemView.findViewById(R.id.tv_Problem_Statement);
            mApproach = itemView.findViewById(R.id.tv_Approach);
            mFinalMark = itemView.findViewById(R.id.tv_Final_Mark);
            foldingCell = itemView.findViewById(R.id.folding_cell);
            mCall = itemView.findViewById(R.id.btn_call);
            mWhatsApp = itemView.findViewById(R.id.btn_whatsapp);
        }
    }

    static class Ideate extends RecyclerView.ViewHolder {


        private final TextView mTeamName;
        private final TextView mCollegeName;
        private final FoldingCell foldingCell;
        private final TextView mEventType;
        private final TextView mTeamLeadName;
        private final TextView mTeamLeadPhone;
        private final TextView mProblemStatement;
        private final TextView mApproach;
        private final TextView mFinalMark;
        private final TextView mCheckinStatus;
        private final Button mCall;
        private final ImageView mWhatsApp;

        public Ideate(@NonNull View itemView) {
            super(itemView);
            mTeamName = itemView.findViewById(R.id.tv_team_Name);
            mCollegeName = itemView.findViewById(R.id.tv_Clg_Name);
            mEventType = itemView.findViewById(R.id.tv_Event);
            mCheckinStatus = itemView.findViewById(R.id.tv_CheckinStatus);
            mTeamLeadName = itemView.findViewById(R.id.tv_Team_Lead_Name);
            mTeamLeadPhone = itemView.findViewById(R.id.tv_Lead_Phone);
            mProblemStatement = itemView.findViewById(R.id.tv_Problem_Statement);
            mApproach = itemView.findViewById(R.id.tv_Approach);
            mFinalMark = itemView.findViewById(R.id.tv_Final_Mark);
            foldingCell = itemView.findViewById(R.id.folding_cell);
            mCall = itemView.findViewById(R.id.btn_call);
            mWhatsApp = itemView.findViewById(R.id.btn_whatsapp);
        }
    }

    static class RoboRace extends RecyclerView.ViewHolder {

        private final TextView mTeamName;
        private final TextView mCollegeName;
        private final FoldingCell foldingCell;
        private final TextView mEventType;
        private final TextView mTeamLeadName;
        private final TextView mTeamLeadPhone;
        private final TextView mCheckinStatus;
        private final TextView mCheckPointsCleared;
        private final TextView mHandtouches;
        private final TextView mBonus;
        private final TextView mTimeOutTaken;
        private final TextView mCheckPointsSkipped;
        private final TextView mTotalTimeTaken;
        private final TextView mTotal;
        private final Button mCall;
        private final ImageView mWhatsApp;

        public RoboRace(@NonNull View itemView) {
            super(itemView);
            mTeamName = itemView.findViewById(R.id.tv_team_Name);
            mCollegeName = itemView.findViewById(R.id.tv_Clg_Name);
            mEventType = itemView.findViewById(R.id.tv_Event);
            mCheckinStatus = itemView.findViewById(R.id.tv_CheckinStatus);
            mTeamLeadName = itemView.findViewById(R.id.tv_Team_Lead_Name_Robo);
            mTeamLeadPhone = itemView.findViewById(R.id.tv_Lead_Phone);
            mCheckPointsCleared = itemView.findViewById(R.id.tv_CheckPoints_Cleared);
            mHandtouches = itemView.findViewById(R.id.tv_HandTouches);
            mBonus = itemView.findViewById(R.id.tv_Bonus);
            mTimeOutTaken = itemView.findViewById(R.id.tv_TimeOutTaken);
            mCheckPointsSkipped = itemView.findViewById(R.id.tv_CheckPoints_Skipped);
            mTotalTimeTaken = itemView.findViewById(R.id.tv_TotalTimeTaken);
            mTotal = itemView.findViewById(R.id.tv_Final_Mark);
            foldingCell = itemView.findViewById(R.id.folding_cell);
            mCall = itemView.findViewById(R.id.btn_call);
            mWhatsApp = itemView.findViewById(R.id.btn_whatsapp);
        }
    }

    static class LineFollower extends RecyclerView.ViewHolder {

        private final TextView mTeamName;
        private final TextView mCollegeName;
        private final FoldingCell foldingCell;
        private final TextView mEventType;
        private final TextView mTeamLeadName;
        private final TextView mTeamLeadPhone;
        private final TextView mCheckinStatus;
        private final TextView mCheckPointsCleared;
        private final TextView mHandtouches;
        private final TextView mBonus;
        private final TextView mTimeOutTaken;
        private final TextView mCheckPointsSkipped;
        private final TextView mTotalTimeTaken;
        private final TextView mTotal;
        private final Button mCall;
        private final ImageView mWhatsApp;

        public LineFollower(@NonNull View itemView) {
            super(itemView);
            mTeamName = itemView.findViewById(R.id.tv_team_Name);
            mCollegeName = itemView.findViewById(R.id.tv_Clg_Name);
            mEventType = itemView.findViewById(R.id.tv_Event);
            mCheckinStatus = itemView.findViewById(R.id.tv_CheckinStatus);
            mTeamLeadName = itemView.findViewById(R.id.tv_Team_Lead_Name);
            mTeamLeadPhone = itemView.findViewById(R.id.tv_Lead_Phone);
            mCheckPointsCleared = itemView.findViewById(R.id.tv_CheckPoints_Cleared);
            mHandtouches = itemView.findViewById(R.id.tv_HandTouches);
            mBonus = itemView.findViewById(R.id.tv_Bonus);
            mTimeOutTaken = itemView.findViewById(R.id.tv_TimeOutTaken);
            mCheckPointsSkipped = itemView.findViewById(R.id.tv_CheckPoints_Skipped);
            mTotalTimeTaken = itemView.findViewById(R.id.tv_TotalTimeTaken);
            mTotal = itemView.findViewById(R.id.tv_Final_Mark);
            foldingCell = itemView.findViewById(R.id.folding_cell);
            mCall = itemView.findViewById(R.id.btn_call);
            mWhatsApp = itemView.findViewById(R.id.btn_whatsapp);
        }
    }

}
