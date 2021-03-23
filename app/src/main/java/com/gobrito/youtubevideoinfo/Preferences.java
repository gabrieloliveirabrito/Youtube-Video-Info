package com.gobrito.youtubevideoinfo;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {
    Context context;
    SharedPreferences sharedPreferences;

    public Preferences(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(Constants.PREF_ID, Context.MODE_PRIVATE);
    }

    public String getAccountName() {
        return sharedPreferences.getString("google_account_name", null);
    }

    public void setGoogleAccountName(String googleAccountName) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("google_account_name", googleAccountName);
        editor.apply();
    }
}
