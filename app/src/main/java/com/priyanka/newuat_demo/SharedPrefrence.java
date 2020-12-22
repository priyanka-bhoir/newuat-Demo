package com.priyanka.newuat_demo;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefrence {
    private String Id;
    private String Uname;
    private String Password;
    private String URl;
    private String Token;
    private SharedPreferences sharedPreferences;

    public SharedPrefrence(Context context){
        sharedPreferences = context.getSharedPreferences("abc",Context.MODE_PRIVATE);
    }


    public String getId(){
        Id=sharedPreferences.getString("id","");
        return Id;
    }
    public void setId(String id){
        sharedPreferences.edit().putString("id",id).commit();
    }

    public String getUname() {
        Uname=sharedPreferences.getString("uname","");
        return Uname;
    }

    public void setUname(String uname) {
        sharedPreferences.edit().putString("uname",uname).commit();
    }

    public String getPassword() {
        Password=sharedPreferences.getString("password","");
        return Password;
    }

    public void setPassword(String password) {
        sharedPreferences.edit().putString("password",password).commit();
    }

    public String getURl() {
        URl=sharedPreferences.getString("url","");
        return URl;
    }

    public void setURl(String URl) {
        sharedPreferences.edit().putString("url",URl).commit();
    }

    public String getToken() {
        Token=sharedPreferences.getString("token","");
        return Token;
    }

    public void setToken(String token) {
        sharedPreferences.edit().putString("token",token).commit();
    }
}
