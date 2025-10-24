package com.example.qld.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.qld.models.User;

/**
 * Lớp quản lý phiên đăng nhập của người dùng
 * Lưu trữ và truy xuất thông tin người dùng đã đăng nhập
 */
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

    /**
     * Constructor để khởi tạo SessionManager
     * @param context Context của ứng dụng
     */
    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    /**
     * Tạo phiên đăng nhập mới cho người dùng
     * @param user Đối tượng User chứa thông tin người dùng cần lưu
     */
    public void createLoginSession(User user) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putInt(KEY_USER_ID, user.getId());
        editor.putString(KEY_USERNAME, user.getUsername());
        editor.putString(KEY_ROLE, user.getRole());
        editor.putString(KEY_FULL_NAME, user.getFullName());
        editor.commit();
    }

    /**
     * Lấy thông tin người dùng đã lưu trong phiên
     * @return Đối tượng User chứa thông tin người dùng
     */
    public User getUserDetails() {
        User user = new User();
        user.setId(pref.getInt(KEY_USER_ID, 0));
        user.setUsername(pref.getString(KEY_USERNAME, null));
        user.setRole(pref.getString(KEY_ROLE, null));
        user.setFullName(pref.getString(KEY_FULL_NAME, null));
        return user;
    }

    /**
     * Kiểm tra xem người dùng đã đăng nhập hay chưa
     * @return true nếu đã đăng nhập, false nếu chưa
     */
    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    /**
     * Đăng xuất người dùng bằng cách xóa tất cả dữ liệu phiên
     */
    public void logoutUser() {
        editor.clear();
        editor.commit();
    }

    /**
     * Lấy vai trò của người dùng đã đăng nhập
     * @return Vai trò của người dùng ('ADMIN', 'TEACHER', 'STUDENT') hoặc null nếu chưa đăng nhập
     */
    public String getUserRole() {
        return pref.getString(KEY_ROLE, null);
    }

    /**
     * Lấy ID của người dùng đã đăng nhập
     * @return ID của người dùng hoặc 0 nếu chưa đăng nhập
     */
    public int getUserId() {
        return pref.getInt(KEY_USER_ID, 0);
    }

    /**
     * Lấy tên đăng nhập của người dùng đã đăng nhập
     * @return Tên đăng nhập hoặc null nếu chưa đăng nhập
     */
    public String getUsername() {
        return pref.getString(KEY_USERNAME, null);
    }

    /**
     * Lấy họ tên đầy đủ của người dùng đã đăng nhập
     * @return Họ tên đầy đủ hoặc null nếu chưa đăng nhập
     */
    public String getFullName() {
        return pref.getString(KEY_FULL_NAME, null);
    }
}