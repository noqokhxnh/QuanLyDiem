package com.example.qld.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SessionManager {
    private static final String TAG = "SessionManager";
    private static final String PREF_NAME = "StudentManagerSession";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_FULL_NAME = "fullName";
    private static final String KEY_ROLE = "role";
    
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;
    
    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
    
    public void createLoginSession(int userId, String username, String fullName, int role) {
        Log.d(TAG, "Creating login session for user: " + username);
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putInt(KEY_USER_ID, userId);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_FULL_NAME, fullName);
        editor.putInt(KEY_ROLE, role);
        editor.commit();
    }
    
    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }
    
    public int getUserId() {
        return sharedPreferences.getInt(KEY_USER_ID, -1);
    }
    
    public String getUsername() {
        return sharedPreferences.getString(KEY_USERNAME, "");
    }
    
    public String getFullName() {
        return sharedPreferences.getString(KEY_FULL_NAME, "");
    }
    
    public int getUserRole() {
        return sharedPreferences.getInt(KEY_ROLE, -1);
    }
    
    public void logout() {
        Log.d(TAG, "Logging out user");
        editor.clear();
        editor.commit();
    }
}