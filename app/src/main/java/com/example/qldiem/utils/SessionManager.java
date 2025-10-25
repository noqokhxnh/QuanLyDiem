package com.example.qldiem.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Utility class cho quản lý session người dùng
 */
public class SessionManager {
    // Tên của tệp SharedPreferences để lưu trữ session người dùng
    private static final String PREF_NAME = "UserSession";
    
    // Các khóa để lưu trữ dữ liệu khác nhau trong SharedPreferences
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";  // Trạng thái đăng nhập (true/false)
    private static final String KEY_USERNAME = "username";        // Tên đăng nhập của người dùng
    private static final String KEY_FULLNAME = "fullName";        // Họ tên đầy đủ của người dùng
    private static final String KEY_ROLE = "role";                // Vai trò của người dùng (Admin/Lecturer/Student)

    // SharedPreferences để lưu trữ dữ liệu phiên làm việc
    private SharedPreferences pref;
    
    // Editor để chỉnh sửa dữ liệu trong SharedPreferences
    private SharedPreferences.Editor editor;
    
    // Context của ứng dụng để truy cập SharedPreferences
    private Context context;

    /**
     * Hàm khởi tạo SessionManager để quản lý phiên làm việc của người dùng
     *
     * CÔNG DỤNG:
     * - Khởi tạo SharedPreferences với tên là "UserSession"
     * - Tạo editor để có thể ghi dữ liệu vào SharedPreferences
     * - Lưu context để sử dụng cho các thao tác với SharedPreferences
     *
     * CÁC BIẾN TRONG HÀM:
     * - context: context của activity hoặc application để truy cập SharedPreferences
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - Context: cung cấp môi trường chạy ứng dụng, cần thiết để truy cập SharedPreferences
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể khởi tạo SessionManager
     * - Ứng dụng không thể quản lý trạng thái đăng nhập của người dùng
     * - Người dùng sẽ bị đăng xuất mỗi khi mở lại ứng dụng
     */
    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    /**
     * Hàm tạo session đăng nhập và lưu thông tin người dùng vào SharedPreferences
     *
     * CÔNG DỤNG:
     * - Đánh dấu người dùng đã đăng nhập (isLoggedIn = true)
     * - Lưu tên đăng nhập, họ tên đầy đủ và vai trò của người dùng
     * - Dữ liệu được lưu trữ cục bộ trên thiết bị để duy trì phiên đăng nhập
     *
     * CÁC BIẾN TRONG HÀM:
     * - username: tên đăng nhập của người dùng
     * - fullName: họ tên đầy đủ của người dùng
     * - role: vai trò của người dùng (Admin/Lecturer/Student)
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - username: định danh duy nhất của tài khoản người dùng
     * - fullName: tên hiển thị của người dùng trong ứng dụng
     * - role: quyền hạn của người dùng để phân quyền truy cập tính năng
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Người dùng không thể duy trì đăng nhập sau khi thoát ứng dụng
     * - Không thể phân biệt người dùng đã đăng nhập hay chưa
     * - Không thể hiển thị thông tin người dùng trong ứng dụng
     * - Tính năng cá nhân hóa sẽ không hoạt động được
     */
    public void createLoginSession(String username, String fullName, String role) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_FULLNAME, fullName);
        editor.putString(KEY_ROLE, role);
        editor.commit();
    }

    /**
     * Hàm kiểm tra xem người dùng đã đăng nhập hay chưa
     *
     * CÔNG DỤNG:
     * - Trả về trạng thái đăng nhập của người dùng
     * - Kiểm tra giá trị của khóa KEY_IS_LOGGED_IN trong SharedPreferences
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ, chỉ sử dụng pref (SharedPreferences)
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - Không có đối số đầu vào
     *
     * GIÁ TRỊ TRẢ VỀ:
     * - true: nếu người dùng đã đăng nhập
     * - false: nếu người dùng chưa đăng nhập hoặc đã đăng xuất
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Ứng dụng không thể xác định được trạng thái đăng nhập của người dùng
     * - Không thể kiểm tra xem có nên hiển thị giao diện chính hay màn hình đăng nhập
     * - Tính năng phân quyền sẽ không hoạt động chính xác
     * - Người dùng có thể truy cập các tính năng mà không cần đăng nhập
     */
    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    /**
     * Hàm lấy tên đăng nhập của người dùng đã lưu trong session
     *
     * CÔNG DỤNG:
     * - Trả về tên đăng nhập của người dùng hiện tại
     * - Dùng để xác định danh tính người dùng trong các thao tác hệ thống
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - Không có đối số đầu vào
     *
     * GIÁ TRỊ TRẢ VỀ:
     * - Tên đăng nhập nếu tồn tại trong session
     * - null nếu không tìm thấy dữ liệu
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể xác định tên đăng nhập của người dùng hiện tại
     * - Không thể thực hiện các thao tác yêu cầu xác định danh tính người dùng
     * - Tính năng cá nhân hóa sẽ không hoạt động
     */
    public String getUsername() {
        return pref.getString(KEY_USERNAME, null);
    }

    /**
     * Hàm lấy họ tên đầy đủ của người dùng từ session
     *
     * CÔNG DỤNG:
     * - Trả về họ tên đầy đủ của người dùng để hiển thị trong giao diện
     * - Dùng để cá nhân hóa trải nghiệm người dùng
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - Không có đối số đầu vào
     *
     * GIÁ TRỊ TRẢ VỀ:
     * - Họ tên đầy đủ nếu tồn tại trong session
     * - "User" nếu không tìm thấy dữ liệu
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Giao diện sẽ không thể hiển thị tên người dùng
     * - Trải nghiệm người dùng bị giảm sút do thiếu cá nhân hóa
     * - Không thể hiển thị thông tin người dùng trong ProfileActivity
     */
    public String getFullName() {
        return pref.getString(KEY_FULLNAME, "User");
    }

    /**
     * Hàm lấy vai trò của người dùng từ session
     *
     * CÔNG DỤNG:
     * - Trả về vai trò của người dùng để phân quyền truy cập
     * - Dùng để xác định quyền hạn của người dùng trong ứng dụng
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - Không có đối số đầu vào
     *
     * GIÁ TRỊ TRẢ VỀ:
     * - Vai trò của người dùng (Admin/Lecturer/Student) nếu tồn tại
     * - "Student" nếu không tìm thấy dữ liệu
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể phân quyền truy cập các chức năng trong ứng dụng
     * - Tất cả người dùng đều có quyền như nhau bất kể vai trò thực tế
     * - Tính năng bảo mật và phân quyền sẽ không hoạt động
     */
    public String getRole() {
        return pref.getString(KEY_ROLE, "Student");
    }

    /**
     * Hàm kiểm tra xem người dùng hiện tại có phải là Admin hay không
     *
     * CÔNG DỤNG:
     * - Kiểm tra vai trò của người dùng so với "Admin"
     * - Trả về kết quả true/false để phân quyền chức năng
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - Không có đối số đầu vào
     *
     * GIÁ TRỊ TRẢ VỀ:
     * - true: nếu người dùng có vai trò là Admin
     * - false: nếu người dùng có vai trò khác
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể phân biệt người dùng Admin với các vai trò khác
     * - Không thể cấp quyền quản trị đặc biệt cho Admin
     * - Tính năng quản lý hệ thống sẽ không hoạt động đúng
     */
    public boolean isAdmin() {
        return "Admin".equals(getRole());
    }

    /**
     * Hàm kiểm tra xem người dùng hiện tại có phải là Lecturer hay không
     *
     * CÔNG DỤNG:
     * - Kiểm tra vai trò của người dùng so với "Lecturer"
     * - Trả về kết quả true/false để phân quyền chức năng
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - Không có đối số đầu vào
     *
     * GIÁ TRỊ TRẢ VỀ:
     * - true: nếu người dùng có vai trò là Lecturer
     * - false: nếu người dùng có vai trò khác
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể phân biệt người dùng Lecturer với các vai trò khác
     * - Không thể cấp quyền giảng viên phù hợp
     * - Tính năng giảng viên sẽ không hoạt động đúng
     */
    public boolean isLecturer() {
        return "Lecturer".equals(getRole());
    }

    /**
     * Hàm kiểm tra xem người dùng hiện tại có phải là Student hay không
     *
     * CÔNG DỤNG:
     * - Kiểm tra vai trò của người dùng so với "Student"
     * - Trả về kết quả true/false để phân quyền chức năng
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - Không có đối số đầu vào
     *
     * GIÁ TRỊ TRẢ VỀ:
     * - true: nếu người dùng có vai trò là Student
     * - false: nếu người dùng có vai trò khác
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể phân biệt người dùng Student với các vai trò khác
     * - Không thể cấp quyền sinh viên phù hợp
     * - Tính năng dành cho sinh viên sẽ không hoạt động đúng
     */
    public boolean isStudent() {
        return "Student".equals(getRole());
    }

    /**
     * Hàm đăng xuất người dùng, xóa tất cả thông tin đăng nhập khỏi session
     *
     * CÔNG DỤNG:
     * - Xóa tất cả dữ liệu đã lưu trong SharedPreferences
     * - Đặt trạng thái đăng nhập về chưa đăng nhập (isLoggedIn = false)
     * - Làm sạch thông tin người dùng khỏi thiết bị
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - Không có đối số đầu vào
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Người dùng không thể đăng xuất khỏi ứng dụng
     * - Thông tin đăng nhập sẽ lưu trữ vĩnh viễn trên thiết bị
     * - Vi phạm nguyên tắc bảo mật cơ bản
     * - Người dùng khác có thể truy cập tài khoản nếu dùng chung thiết bị
     */
    public void logout() {
        editor.clear();
        editor.commit();
    }
}
