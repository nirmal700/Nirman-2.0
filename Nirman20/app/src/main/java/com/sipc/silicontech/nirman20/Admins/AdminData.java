package com.sipc.silicontech.nirman20.Admins;

public class AdminData {
    String SIC, mAccessLevel, mName, mPassword, mUserRole, mPhoneNo;

    public AdminData() {

    }

    public AdminData(String SIC, String mAccessLevel, String mName, String mPassword, String mUserRole, String mPhoneNo) {
        this.SIC = SIC;
        this.mAccessLevel = mAccessLevel;
        this.mName = mName;
        this.mPassword = mPassword;
        this.mUserRole = mUserRole;
        this.mPhoneNo = mPhoneNo;
    }

    public String getSIC() {
        return SIC;
    }

    public void setSIC(String SIC) {
        this.SIC = SIC;
    }

    public String getmAccessLevel() {
        return mAccessLevel;
    }

    public void setmAccessLevel(String mAccessLevel) {
        this.mAccessLevel = mAccessLevel;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmPassword() {
        return mPassword;
    }

    public void setmPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public String getmUserRole() {
        return mUserRole;
    }

    public void setmUserRole(String mUserRole) {
        this.mUserRole = mUserRole;
    }

    public String getmPhoneNo() {
        return mPhoneNo;
    }

    public void setmPhoneNo(String mPhoneNo) {
        this.mPhoneNo = mPhoneNo;
    }
}
