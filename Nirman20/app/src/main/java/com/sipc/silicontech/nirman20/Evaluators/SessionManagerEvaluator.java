package com.sipc.silicontech.nirman20.Evaluators;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManagerEvaluator {
    // Initialize Variables
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public SessionManagerEvaluator(Context context){
        sharedPreferences = context.getSharedPreferences("EvaluatorAppKey",0);
        editor = sharedPreferences.edit();
        editor.apply();
    }
    // Set Login
    public void setEvaluatorLogin(boolean login){
        editor.putBoolean("KEY_EVALUATOR_LOGIN",login);
        editor.commit();
    }
    public boolean getEvaluatorLogin(){

        return sharedPreferences.getBoolean("KEY_EVALUATOR_LOGIN",false);
    }
    public void setEvaluatorDetails(String name,String event,String password,String phoneNo){
        editor.putString("KEY_EVALUATOR_NAME",name);
        editor.putString("KEY_EVENT",event);
        editor.putString("KEY_EVA_PASSWORD",password);
        editor.putString("KEY_EVALUATOR_PHONE",phoneNo);
        editor.commit();
    }
    public String getEvaluatorName(){
        return sharedPreferences.getString("KEY_EVALUATOR_NAME","");
    }
    public String getEventAssigned(){
        return sharedPreferences.getString("KEY_EVENT","");
    }
    public String getEvaPassword(){
        return sharedPreferences.getString("KEY_EVA_PASSWORD","");
    }
    public String getPhone(){
        return sharedPreferences.getString("KEY_EVALUATOR_PHONE","");
    }
}
