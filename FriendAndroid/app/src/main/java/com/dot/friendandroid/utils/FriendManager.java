package com.dot.friendandroid.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class FriendManager {
    public static final String NAME_PREFERENCE = "myt_preference";

    private static final String PREF_IS_LOGGIN_IN = "pref_is_loggin_in";
    private static final String PREF_TOKEN = "pref_token";
    private static final String PREF_USER_ID = "pred_user_id";

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public FriendManager(Context context) {
        this.preferences = context.getSharedPreferences(NAME_PREFERENCE, Context.MODE_PRIVATE);
        this.editor = preferences.edit();
    }

    public boolean isLoggedIn() {
        return preferences.getBoolean(PREF_IS_LOGGIN_IN, false);
    }

    public void logIn(String token, String userID) {
        editor.putString(PREF_TOKEN, "Bearer " + token).commit();
        editor.putString(PREF_USER_ID, userID).commit();
        editor.putBoolean(PREF_IS_LOGGIN_IN, true).commit();
    }

    public void logOut() {
        editor.putString(PREF_TOKEN, null).commit();
        editor.putBoolean(PREF_IS_LOGGIN_IN, false).commit();
    }

    public String getToken() {
        return preferences.getString(PREF_TOKEN, null);
    }

    public String getUserID() {
        return preferences.getString(PREF_USER_ID, null);
    }
}
