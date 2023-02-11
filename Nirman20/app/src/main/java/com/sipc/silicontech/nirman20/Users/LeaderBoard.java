package com.sipc.silicontech.nirman20.Users;

public class LeaderBoard {
    String mTeamName,mEvent;
    Long mFinalMark,mPosition;

    public LeaderBoard() {
    }

    public LeaderBoard(String mTeamName, String mEvent, Long mFinalMark, Long mPosition) {
        this.mTeamName = mTeamName;
        this.mEvent = mEvent;
        this.mFinalMark = mFinalMark;
        this.mPosition = mPosition;
    }

    public String getmTeamName() {
        return mTeamName;
    }

    public void setmTeamName(String mTeamName) {
        this.mTeamName = mTeamName;
    }

    public String getmEvent() {
        return mEvent;
    }

    public void setmEvent(String mEvent) {
        this.mEvent = mEvent;
    }

    public Long getmFinalMark() {
        return mFinalMark;
    }

    public void setmFinalMark(Long mFinalMark) {
        this.mFinalMark = mFinalMark;
    }

    public Long getmPosition() {
        return mPosition;
    }

    public void setmPosition(Long mPosition) {
        this.mPosition = mPosition;
    }
}
