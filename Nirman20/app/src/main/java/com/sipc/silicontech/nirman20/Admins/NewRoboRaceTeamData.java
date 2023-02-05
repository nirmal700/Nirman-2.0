package com.sipc.silicontech.nirman20.Admins;

public class NewRoboRaceTeamData {
    String mEventParticipating,mTeamName,mCollegeName,mTeamLead,mTeamLeadPhone,mMem1Name,mMem1Phone,mMem2Name,mMem2Phone,mMem3Name,mMem3Phone;
    long mTotalTimeTaken,mTotal;
    boolean mCheckedIn,mTimeOutTaken;
    long mCheckPointCleared,mHandTouches,mBonus,mCheckPointSkipped,mPenalty;

    public NewRoboRaceTeamData() {
    }

    public NewRoboRaceTeamData(String mEventParticipating, String mTeamName, String mCollegeName, String mTeamLead, String mTeamLeadPhone, String mMem1Name, String mMem1Phone, String mMem2Name, String mMem2Phone, String mMem3Name, String mMem3Phone, long mTotalTimeTaken, long mTotal, boolean mCheckedIn, boolean mTimeOutTaken, long mCheckPointCleared, long mHandTouches, long mBonus, long mCheckPointSkipped, long mPenalty) {
        this.mEventParticipating = mEventParticipating;
        this.mTeamName = mTeamName;
        this.mCollegeName = mCollegeName;
        this.mTeamLead = mTeamLead;
        this.mTeamLeadPhone = mTeamLeadPhone;
        this.mMem1Name = mMem1Name;
        this.mMem1Phone = mMem1Phone;
        this.mMem2Name = mMem2Name;
        this.mMem2Phone = mMem2Phone;
        this.mMem3Name = mMem3Name;
        this.mMem3Phone = mMem3Phone;
        this.mTotalTimeTaken = mTotalTimeTaken;
        this.mTotal = mTotal;
        this.mCheckedIn = mCheckedIn;
        this.mTimeOutTaken = mTimeOutTaken;
        this.mCheckPointCleared = mCheckPointCleared;
        this.mHandTouches = mHandTouches;
        this.mBonus = mBonus;
        this.mCheckPointSkipped = mCheckPointSkipped;
        this.mPenalty = mPenalty;
    }

    public String getmEventParticipating() {
        return mEventParticipating;
    }

    public void setmEventParticipating(String mEventParticipating) {
        this.mEventParticipating = mEventParticipating;
    }

    public String getmTeamName() {
        return mTeamName;
    }

    public void setmTeamName(String mTeamName) {
        this.mTeamName = mTeamName;
    }

    public String getmCollegeName() {
        return mCollegeName;
    }

    public void setmCollegeName(String mCollegeName) {
        this.mCollegeName = mCollegeName;
    }

    public String getmTeamLead() {
        return mTeamLead;
    }

    public void setmTeamLead(String mTeamLead) {
        this.mTeamLead = mTeamLead;
    }

    public String getmTeamLeadPhone() {
        return mTeamLeadPhone;
    }

    public void setmTeamLeadPhone(String mTeamLeadPhone) {
        this.mTeamLeadPhone = mTeamLeadPhone;
    }

    public String getmMem1Name() {
        return mMem1Name;
    }

    public void setmMem1Name(String mMem1Name) {
        this.mMem1Name = mMem1Name;
    }

    public String getmMem1Phone() {
        return mMem1Phone;
    }

    public void setmMem1Phone(String mMem1Phone) {
        this.mMem1Phone = mMem1Phone;
    }

    public String getmMem2Name() {
        return mMem2Name;
    }

    public void setmMem2Name(String mMem2Name) {
        this.mMem2Name = mMem2Name;
    }

    public String getmMem2Phone() {
        return mMem2Phone;
    }

    public void setmMem2Phone(String mMem2Phone) {
        this.mMem2Phone = mMem2Phone;
    }

    public String getmMem3Name() {
        return mMem3Name;
    }

    public void setmMem3Name(String mMem3Name) {
        this.mMem3Name = mMem3Name;
    }

    public String getmMem3Phone() {
        return mMem3Phone;
    }

    public void setmMem3Phone(String mMem3Phone) {
        this.mMem3Phone = mMem3Phone;
    }

    public double getmTotalTimeTaken() {
        return mTotalTimeTaken;
    }

    public void setmTotalTimeTaken(long mTotalTimeTaken) {
        this.mTotalTimeTaken = mTotalTimeTaken;
    }

    public double getmTotal() {
        return mTotal;
    }

    public void setmTotal(long mTotal) {
        this.mTotal = mTotal;
    }

    public boolean ismCheckedIn() {
        return mCheckedIn;
    }

    public void setmCheckedIn(boolean mCheckedIn) {
        this.mCheckedIn = mCheckedIn;
    }

    public boolean ismTimeOutTaken() {
        return mTimeOutTaken;
    }

    public void setmTimeOutTaken(boolean mTimeOutTaken) {
        this.mTimeOutTaken = mTimeOutTaken;
    }

    public long getmCheckPointCleared() {
        return mCheckPointCleared;
    }

    public void setmCheckPointCleared(int mCheckPointCleared) {
        this.mCheckPointCleared = mCheckPointCleared;
    }

    public long getmHandTouches() {
        return mHandTouches;
    }

    public void setmHandTouches(int mHandTouches) {
        this.mHandTouches = mHandTouches;
    }

    public long getmBonus() {
        return mBonus;
    }

    public void setmBonus(int mBonus) {
        this.mBonus = mBonus;
    }

    public long getmCheckPointSkipped() {
        return mCheckPointSkipped;
    }

    public void setmCheckPointSkipped(int mCheckPointSkipped) {
        this.mCheckPointSkipped = mCheckPointSkipped;
    }

    public long getmPenalty() {
        return mPenalty;
    }

    public void setmPenalty(long mPenalty) {
        this.mPenalty = mPenalty;
    }
}
