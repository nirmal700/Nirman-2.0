package com.sipc.silicontech.nirman20.Users;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManagerParticipant {

    // Initialize Variables
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public SessionManagerParticipant(Context context){
        sharedPreferences = context.getSharedPreferences("ParticipantAppKey",0);
        editor = sharedPreferences.edit();
        editor.apply();
    }
    // Set Login
    public void setParticipantLogin(boolean login){
        editor.putBoolean("KEY_LOGIN_PARTICIPANT",login);
        editor.commit();
    }

    public boolean getParticipantLogin(){

        return sharedPreferences.getBoolean("KEY_LOGIN_PARTICIPANT",false);
    }
    public void setDetails(String event,String partname,String password,String phoneno,String teamname){
        editor.putString("KEY_EVENT_NAME",event);
        editor.putString("KEY_PARTICIPANT_NAME",partname);
        editor.putString("KEY_PASSWORD",password);
        editor.putString("KEY_TEAM_NAME",teamname);
        editor.putString("KEY_PHONE_NO",phoneno);


        editor.commit();

    }

    public String getEventName(){
        return sharedPreferences.getString("KEY_EVENT_NAME","");
    }
    public String getParticipantName(){
        return sharedPreferences.getString("KEY_PARTICIPANT_NAME","");
    }
    public String getPassword(){
        return sharedPreferences.getString("KEY_PASSWORD","");
    }
    public String getTeamName(){
        return sharedPreferences.getString("KEY_TEAM_NAME","");
    }
    public String getPhone(){
        return sharedPreferences.getString("KEY_PHONE_NO","");
    }

    public void logoutUserFromSession(){
        editor.clear();
        editor.commit();
    }
}
