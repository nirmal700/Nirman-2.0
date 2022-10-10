package com.sipc.silicontech.nirman20.Users;

public class UserData {
    String mParticipantName, mPassword, mPhoneNumber, mTeamName;
    boolean mCheckedIn, mFood;

    public UserData() {

    }

    public UserData(String mParticipantName, String mPassword, String mPhoneNumber, String mTeamName, boolean mCheckedIn, boolean mFood) {
        this.mParticipantName = mParticipantName;
        this.mPassword = mPassword;
        this.mPhoneNumber = mPhoneNumber;
        this.mTeamName = mTeamName;
        this.mCheckedIn = mCheckedIn;
        this.mFood = mFood;
    }

    public String getmParticipantName() {
        return mParticipantName;
    }

    public void setmParticipantName(String mParticipantName) {
        this.mParticipantName = mParticipantName;
    }

    public String getmPassword() {
        return mPassword;
    }

    public void setmPassword(String mPassword) {
        this.mPassword = mPassword;
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

    public boolean ismCheckedIn() {
        return mCheckedIn;
    }

    public void setmCheckedIn(boolean mCheckedIn) {
        this.mCheckedIn = mCheckedIn;
    }

    public boolean ismFood() {
        return mFood;
    }

    public void setmFood(boolean mFood) {
        this.mFood = mFood;
    }
}
