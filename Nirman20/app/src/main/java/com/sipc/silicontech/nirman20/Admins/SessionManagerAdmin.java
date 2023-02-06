package com.sipc.silicontech.nirman20.Admins;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManagerAdmin {

    // Initialize Variables
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public SessionManagerAdmin(Context context) {
        sharedPreferences = context.getSharedPreferences("CustomerAppKey", 0);
        editor = sharedPreferences.edit();
        editor.apply();
    }

    // Set Button
    public void setShopButton(boolean disable) {
        editor.putBoolean("KEY_BUTTON", disable);
        editor.commit();
    }

    public boolean getAdminLogin() {

        return sharedPreferences.getBoolean("KEY_LOGIN", false);
    }

    // Set Login
    public void setAdminLogin(boolean login) {
        editor.putBoolean("KEY_LOGIN", login);
        editor.commit();
    }

    public void setDetails(String name, String sic, String password, String accesslev, String userrole, String phoneNo) {
        editor.putString("KEY_NAME", name);
        editor.putString("KEY_SIC", sic);
        editor.putString("KEY_PASSWORD", password);
        editor.putString("KEY_ACCESS_LEVEL", accesslev);
        editor.putString("KEY_USER_ROLE", userrole);
        editor.putString("KEY_PHONE", phoneNo);


        editor.commit();

    }

    public String getName() {
        return sharedPreferences.getString("KEY_NAME", "");
    }

    public String getSic() {
        return sharedPreferences.getString("KEY_SIC", "");
    }

    public String getPassword() {
        return sharedPreferences.getString("KEY_PASSWORD", "");
    }

    public String getAccessLevel() {
        return sharedPreferences.getString("KEY_ACCESS_LEVEL", "");
    }

    public String getUserRole() {
        return sharedPreferences.getString("KEY_USER_ROLE", "");
    }

    public String getPhone() {
        return sharedPreferences.getString("KEY_PHONE", "");
    }

    public void logoutUserFromSession() {
        editor.clear();
        editor.commit();
    }
}
