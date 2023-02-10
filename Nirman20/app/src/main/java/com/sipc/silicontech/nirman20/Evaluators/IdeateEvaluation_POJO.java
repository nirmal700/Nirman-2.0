package com.sipc.silicontech.nirman20.Evaluators;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class IdeateEvaluation_POJO {
    String mTeamName, mCollegeName, mProblemStat, mApproach, mSuggestion, mEvalatedBy;
    int mMark1, mMark2, mMark3, mMark4, mMark5;
    long mAvg;
    @ServerTimestamp
    Date mEvaluatedTime;

    public IdeateEvaluation_POJO() {
    }

    public IdeateEvaluation_POJO(String mTeamName, String mCollegeName, String mProblemStat, String mApproach, String mSuggestion, String mEvalatedBy, int mMark1, int mMark2, int mMark3, int mMark4, int mMark5, long mAvg, Date mEvaluatedTime) {
        this.mTeamName = mTeamName;
        this.mCollegeName = mCollegeName;
        this.mProblemStat = mProblemStat;
        this.mApproach = mApproach;
        this.mSuggestion = mSuggestion;
        this.mEvalatedBy = mEvalatedBy;
        this.mMark1 = mMark1;
        this.mMark2 = mMark2;
        this.mMark3 = mMark3;
        this.mMark4 = mMark4;
        this.mMark5 = mMark5;
        this.mAvg = mAvg;
        this.mEvaluatedTime = mEvaluatedTime;
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

    public String getmEvalatedBy() {
        return mEvalatedBy;
    }

    public void setmEvalatedBy(String mEvalatedBy) {
        this.mEvalatedBy = mEvalatedBy;
    }

    public String getmProblemStat() {
        return mProblemStat;
    }

    public void setmProblemStat(String mProblemStat) {
        this.mProblemStat = mProblemStat;
    }

    public String getmApproach() {
        return mApproach;
    }

    public void setmApproach(String mApproach) {
        this.mApproach = mApproach;
    }

    public String getmSuggestion() {
        return mSuggestion;
    }

    public void setmSuggestion(String mSuggestion) {
        this.mSuggestion = mSuggestion;
    }

    public int getmMark1() {
        return mMark1;
    }

    public void setmMark1(int mMark1) {
        this.mMark1 = mMark1;
    }

    public int getmMark2() {
        return mMark2;
    }

    public void setmMark2(int mMark2) {
        this.mMark2 = mMark2;
    }

    public int getmMark3() {
        return mMark3;
    }

    public void setmMark3(int mMark3) {
        this.mMark3 = mMark3;
    }

    public int getmMark4() {
        return mMark4;
    }

    public void setmMark4(int mMark4) {
        this.mMark4 = mMark4;
    }

    public int getmMark5() {
        return mMark5;
    }

    public void setmMark5(int mMark5) {
        this.mMark5 = mMark5;
    }

    public Long getmAvg() {
        return mAvg;
    }

    public void setmAvg(Long mAvg) {
        this.mAvg = mAvg;
    }

    public Date getmEvaluatedTime() {
        return mEvaluatedTime;
    }

    public void setmEvaluatedTime(Date mEvaluatedTime) {
        this.mEvaluatedTime = mEvaluatedTime;
    }
}
