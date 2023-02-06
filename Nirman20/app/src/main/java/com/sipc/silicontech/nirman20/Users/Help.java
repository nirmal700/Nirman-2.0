package com.sipc.silicontech.nirman20.Users;

public class Help {
    boolean isResolved;
    String mDescription, mIssueType, mTeamname, mEvent, mName, mPhoneNo, mResolvedby, mId;


    public Help() {
    }

    public Help(boolean isResolved, String mDescription, String mIssueType, String mTeamname, String mEvent, String mName, String mPhoneNo, String mResolvedby, String mId) {
        this.isResolved = isResolved;
        this.mDescription = mDescription;
        this.mIssueType = mIssueType;
        this.mTeamname = mTeamname;
        this.mEvent = mEvent;
        this.mName = mName;
        this.mPhoneNo = mPhoneNo;
        this.mResolvedby = mResolvedby;
        this.mId = mId;
    }

    public boolean isResolved() {
        return isResolved;
    }

    public void setResolved(boolean resolved) {
        isResolved = resolved;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getmIssueType() {
        return mIssueType;
    }

    public void setmIssueType(String mIssueType) {
        this.mIssueType = mIssueType;
    }

    public String getmTeamname() {
        return mTeamname;
    }

    public void setmTeamname(String mTeamname) {
        this.mTeamname = mTeamname;
    }

    public String getmEvent() {
        return mEvent;
    }

    public void setmEvent(String mEvent) {
        this.mEvent = mEvent;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmPhoneNo() {
        return mPhoneNo;
    }

    public void setmPhoneNo(String mPhoneNo) {
        this.mPhoneNo = mPhoneNo;
    }

    public String getmResolvedby() {
        return mResolvedby;
    }

    public void setmResolvedby(String mResolvedby) {
        this.mResolvedby = mResolvedby;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

}
