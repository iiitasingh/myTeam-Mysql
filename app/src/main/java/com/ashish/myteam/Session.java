package com.ashish.myteam;

import android.content.Context;
import android.content.SharedPreferences;

public class Session {
    SharedPreferences prefs;
    String USERNAME = "username";
    SharedPreferences.Editor editor1;
    Context ctx;

    public Session(Context ctx) {
        this.ctx = ctx;
        prefs = ctx.getSharedPreferences("myapp", Context.MODE_PRIVATE);
        editor1 = prefs.edit();
    }
    public void setUser(String userName)
    {
        editor1.putString(USERNAME, userName);
        editor1.apply();
    }
    public String getUser()
    {
        return prefs.getString(USERNAME,"");
    }

    public void setLoggedin(boolean logggedin) {
        editor1.putBoolean("loggedInmode", logggedin);
        editor1.commit();
    }

    public boolean loggedin() {
        return prefs.getBoolean("loggedInmode", false);
    }
}

