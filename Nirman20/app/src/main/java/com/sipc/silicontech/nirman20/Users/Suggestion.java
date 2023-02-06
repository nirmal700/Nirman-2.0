package com.sipc.silicontech.nirman20.Users;

public class Suggestion {
    String mTeamName, mCollegeName, mSuggestion, mId;
    Boolean isEvaluatorSug, isParticipantSug;
    long mAvg;

    public Suggestion() {
    }

    public Suggestion(String mTeamName, String mCollegeName, String mSuggestion, String mId, Boolean isEvaluatorSug, Boolean isParticipantSug, long mAvg) {
        this.mTeamName = mTeamName;
        this.mCollegeName = mCollegeName;
        this.mSuggestion = mSuggestion;
        this.mId = mId;
        this.isEvaluatorSug = isEvaluatorSug;
        this.isParticipantSug = isParticipantSug;
        this.mAvg = mAvg;
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

    public String getmSuggestion() {
        return mSuggestion;
    }

    public void setmSuggestion(String mSuggestion) {
        this.mSuggestion = mSuggestion;
    }

    public Boolean getEvaluatorSug() {
        return isEvaluatorSug;
    }

    public void setEvaluatorSug(Boolean evaluatorSug) {
        isEvaluatorSug = evaluatorSug;
    }

    public Boolean getParticipantSug() {
        return isParticipantSug;
    }

    public void setParticipantSug(Boolean participantSug) {
        isParticipantSug = participantSug;
    }

    public long getmAvg() {
        return mAvg;
    }

    public void setmAvg(long mAvg) {
        this.mAvg = mAvg;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }
}
