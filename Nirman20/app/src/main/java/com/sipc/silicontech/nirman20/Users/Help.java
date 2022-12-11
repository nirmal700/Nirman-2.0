package com.sipc.silicontech.nirman20.Users;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Help {
    boolean IsResolved;
    String mDescription, mIssueType,mTeamname,mEvent,mName,mPhoneNo,mResolvedby;
    @ServerTimestamp
    Date mUpdatedTime;

    public Help() {
    }

    public Help(boolean isResolved, String mDescription, String mIssueType, String mTeamname, String mEvent, String mName, String mPhoneNo, String mResolvedby, Date mUpdatedTime) {
        IsResolved = isResolved;
        this.mDescription = mDescription;
        this.mIssueType = mIssueType;
        this.mTeamname = mTeamname;
        this.mEvent = mEvent;
        this.mName = mName;
        this.mPhoneNo = mPhoneNo;
        this.mResolvedby = mResolvedby;
        this.mUpdatedTime = mUpdatedTime;
    }

    public boolean isResolved() {
        return IsResolved;
    }

    public void setResolved(boolean resolved) {
        IsResolved = resolved;
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

    public Date getmUpdatedTime() {
        return mUpdatedTime;
    }

    public void setmUpdatedTime(Date mUpdatedTime) {
        this.mUpdatedTime = mUpdatedTime;
    }
}
