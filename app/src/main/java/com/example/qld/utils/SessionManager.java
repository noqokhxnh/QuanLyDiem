package com.example.qld.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.qld.models.User;

public class SessionManager {
    private static final String PREF_NAME = "StudentManagerPrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_ROLE = "role";
    private static final String KEY_FULL_NAME = "fullName";

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;

    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void createLoginSession(User user) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putInt(KEY_USER_ID, user.getId());
        editor.putString(KEY_USERNAME, user.getUsername());
        editor.putInt(KEY_ROLE, user.getRole());
        editor.putString(KEY_FULL_NAME, user.getFullName());
        editor.commit();
    }

    public User getUserDetails() {
        User user = new User();
        user.setId(pref.getInt(KEY_USER_ID, 0));
        user.setUsername(pref.getString(KEY_USERNAME, null));
        user.setRole(pref.getInt(KEY_ROLE, -1));
        user.setFullName(pref.getString(KEY_FULL_NAME, null));
        return user;
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void logoutUser() {
        editor.clear();
        editor.commit();
    }

    public int getUserRole() {
        return pref.getInt(KEY_ROLE, -1);
    }

    public int getUserId() {
        return pref.getInt(KEY_USER_ID, 0);
    }

    public String getUsername() {
        return pref.getString(KEY_USERNAME, null);
    }

    public String getFullName() {
        return pref.getString(KEY_FULL_NAME, null);
    }
}