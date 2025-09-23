package com.example.studentscoremanager;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesHelper {
    
    private static final String PREFS_NAME = "StudentScoreManagerPrefs";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    private static final String KEY_STUDENT_ID = "student_id";
    
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    
    public SharedPreferencesHelper(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }
    
    // Lưu thông tin đăng nhập
    public void saveLoginInfo(String username, String password, String studentId) {
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_PASSWORD, password);
        editor.putString(KEY_STUDENT_ID, studentId);
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.apply();
    }
    
    // Cập nhật mật khẩu mới
    public void updatePassword(String newPassword) {
        editor.putString(KEY_PASSWORD, newPassword);
        editor.apply();
    }
    
    // Lấy username hiện tại
    public String getCurrentUsername() {
        return prefs.getString(KEY_USERNAME, "");
    }
    
    // Lấy password hiện tại
    public String getCurrentPassword() {
        return prefs.getString(KEY_PASSWORD, "");
    }
    
    // Lấy student ID hiện tại
    public String getCurrentStudentId() {
        return prefs.getString(KEY_STUDENT_ID, "");
    }
    
    // Kiểm tra trạng thái đăng nhập
    public boolean isLoggedIn() {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false);
    }
    
    // Đăng xuất
    public void logout() {
        editor.clear();
        editor.apply();
    }
    
    // Xóa thông tin đăng nhập (alias cho logout)
    public void clearLoginInfo() {
        logout();
    }
    
    // Lưu thông tin đăng nhập khi đổi mật khẩu
    public void updateLoginAfterPasswordChange(String newPassword) {
        editor.putString(KEY_PASSWORD, newPassword);
        editor.apply();
    }
}
