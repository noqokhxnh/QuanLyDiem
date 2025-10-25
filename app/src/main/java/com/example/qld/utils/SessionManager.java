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
     * 
     * Cách thức hoạt động:
     * 1. Lưu context của ứng dụng để truy cập SharedPreferences
     * 2. Khởi tạo SharedPreferences với tên "StudentManagerPrefs" ở chế độ PRIVATE
     * 3. Tạo editor để thực hiện các thao tác ghi dữ liệu
     * 
     * @param context Context của ứng dụng (Activity, Service, Application, v.v.)
     *                Được sử dụng để truy cập SharedPreferences
     */
    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    /**
     * Tạo phiên đăng nhập mới cho người dùng
     * 
     * Cách thức hoạt động:
     * 1. Thiết lập trạng thái đăng nhập là true
     * 2. Lưu ID người dùng, tên đăng nhập, vai trò và họ tên đầy đủ vào SharedPreferences
     * 3. Hoàn tất ghi dữ liệu bằng phương thức commit()
     * 
     * @param user Đối tượng User chứa thông tin người dùng cần lưu (không được null)
     *             Bao gồm ID, tên đăng nhập, vai trò, họ tên đầy đủ
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
     * 
     * Cách thức hoạt động:
     * 1. Tạo một đối tượng User mới
     * 2. Lấy các giá trị đã lưu trong SharedPreferences (ID, tên đăng nhập, vai trò, họ tên)
     * 3. Gán các giá trị này vào các thuộc tính tương ứng của đối tượng User
     * 4. Trả về đối tượng User chứa thông tin người dùng
     * 
     * @return Đối tượng User chứa thông tin người dùng đã lưu trong phiên
     *         Nếu không có giá trị nào được lưu, các thuộc tính sẽ có giá trị mặc định (0 cho int, null cho String)
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
     * 
     * Cách thức hoạt động:
     * 1. Lấy giá trị boolean của khóa KEY_IS_LOGGED_IN từ SharedPreferences
     * 2. Nếu không tìm thấy khóa này, trả về giá trị mặc định là false
     * 3. Trả về kết quả kiểm tra
     * 
     * @return true nếu người dùng đã đăng nhập (trạng thái đăng nhập = true), false nếu chưa đăng nhập
     */
    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    /**
     * Đăng xuất người dùng bằng cách xóa tất cả dữ liệu phiên
     * 
     * Cách thức hoạt động:
     * 1. Xóa tất cả các cặp khóa-giá trị trong SharedPreferences
     * 2. Hoàn tất ghi dữ liệu bằng phương thức commit()
     * 3. Hiệu ứng: người dùng sẽ được coi là chưa đăng nhập
     * 
     * Ghi chú: Phương thức này sẽ xóa tất cả dữ liệu phiên, không chỉ trạng thái đăng nhập
     */
    public void logoutUser() {
        editor.clear();
        editor.commit();
    }

    /**
     * Lấy vai trò của người dùng đã đăng nhập
     * 
     * Cách thức hoạt động:
     * 1. Lấy giá trị String của khóa KEY_ROLE từ SharedPreferences
     * 2. Nếu không tìm thấy khóa này, trả về giá trị mặc định là null
     * 3. Trả về vai trò của người dùng (ADMIN, TEACHER, STUDENT hoặc null)
     * 
     * @return Vai trò của người dùng ('ADMIN', 'TEACHER', 'STUDENT') hoặc null nếu chưa đăng nhập hoặc không có dữ liệu
     */
    public String getUserRole() {
        return pref.getString(KEY_ROLE, null);
    }

    /**
     * Lấy ID của người dùng đã đăng nhập
     * 
     * Cách thức hoạt động:
     * 1. Lấy giá trị int của khóa KEY_USER_ID từ SharedPreferences
     * 2. Nếu không tìm thấy khóa này, trả về giá trị mặc định là 0
     * 3. Trả về ID người dùng đã lưu
     * 
     * @return ID của người dùng (số nguyên dương) hoặc 0 nếu chưa đăng nhập hoặc không có dữ liệu
     */
    public int getUserId() {
        return pref.getInt(KEY_USER_ID, 0);
    }

    /**
     * Lấy tên đăng nhập của người dùng đã đăng nhập
     * 
     * Cách thức hoạt động:
     * 1. Lấy giá trị String của khóa KEY_USERNAME từ SharedPreferences
     * 2. Nếu không tìm thấy khóa này, trả về giá trị mặc định là null
     * 3. Trả về tên đăng nhập đã lưu
     * 
     * @return Tên đăng nhập (chuỗi không khoảng trắng) hoặc null nếu chưa đăng nhập hoặc không có dữ liệu
     */
    public String getUsername() {
        return pref.getString(KEY_USERNAME, null);
    }

    /**
     * Lấy họ tên đầy đủ của người dùng đã đăng nhập
     * 
     * Cách thức hoạt động:
     * 1. Lấy giá trị String của khóa KEY_FULL_NAME từ SharedPreferences
     * 2. Nếu không tìm thấy khóa này, trả về giá trị mặc định là null
     * 3. Trả về họ tên đầy đủ đã lưu
     * 
     * @return Họ tên đầy đủ (chuỗi có thể chứa khoảng trắng) hoặc null nếu chưa đăng nhập hoặc không có dữ liệu
     */
    public String getFullName() {
        return pref.getString(KEY_FULL_NAME, null);
    }
}