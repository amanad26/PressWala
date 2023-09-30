package com.abmtech.presswala.Session;

import android.content.Context;
import android.content.SharedPreferences;

public class Session {

    private String fileName = "user_data";
    private String user_id = "user_id";
    private String email = "email";
    private String name = "name";
    private String mobile = "mobile";
    private String password = "password";
    private String address = "address";
    private String type = "type";

    Context context;
    public SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    public Session(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
    }


    public void setEmail(String em) {
        editor.putString(email, em);
        editor.commit();
    }


    public void setAddress(String em) {
        editor.putString(address, em);
        editor.commit();
    }

    public void setType(String em) {
        editor.putString(type, em);
        editor.commit();
    }

    public String getEmail() {
        return sharedPreferences.getString(email, "");
    }

    public String getAddress() {
        return sharedPreferences.getString(address, "");
    }

    public String getType() {
        return sharedPreferences.getString(type, "");
    }


    public void setUser_id(String id) {
        editor.putString(user_id, id);
        editor.commit();
    }


    public void setMobile(String mob) {
        editor.putString(mobile, mob);
        editor.commit();
        editor.apply();
    }

    public void setPassword(String mob) {
        editor.putString(password, mob);
        editor.commit();
        editor.apply();
    }

    public String getMobile() {
        return sharedPreferences.getString(mobile, "");
    }

    public String getPassword() {
        return sharedPreferences.getString(password, "");
    }


    public void setName(String mob) {
        editor.putString(name, mob);
        editor.commit();
        editor.apply();
    }


    public String getUserId() {
        return sharedPreferences.getString(user_id, "");
    }


    public String getName() {
        return sharedPreferences.getString(name, "");
    }


    public void logOut() {
        editor.clear();
        editor.commit();
        editor.apply();
    }

}
