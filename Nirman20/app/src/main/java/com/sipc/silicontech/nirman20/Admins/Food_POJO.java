package com.sipc.silicontech.nirman20.Admins;

public class Food_POJO {
    String mParticipantName,mPhoneNumber,mTeamName;
    Boolean mFoodAvailed;

    public Food_POJO() {
    }

    public Food_POJO(String mParticipantName, String mPhoneNumber, String mTeamName, Boolean mFoodAvailed) {
        this.mParticipantName = mParticipantName;
        this.mPhoneNumber = mPhoneNumber;
        this.mTeamName = mTeamName;
        this.mFoodAvailed = mFoodAvailed;
    }

    public String getmParticipantName() {
        return mParticipantName;
    }

    public void setmParticipantName(String mParticipantName) {
        this.mParticipantName = mParticipantName;
    }

    public String getmPhoneNumber() {
        return mPhoneNumber;
    }

    public void setmPhoneNumber(String mPhoneNumber) {
        this.mPhoneNumber = mPhoneNumber;
    }

    public String getmTeamName() {
        return mTeamName;
    }

    public void setmTeamName(String mTeamName) {
        this.mTeamName = mTeamName;
    }

    public Boolean getmFoodAvailed() {
        return mFoodAvailed;
    }

    public void setmFoodAvailed(Boolean mFoodAvailed) {
        this.mFoodAvailed = mFoodAvailed;
    }
}
