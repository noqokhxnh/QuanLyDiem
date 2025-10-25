package com.example.qldiem.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qldiem.R;
import com.example.qldiem.database.DatabaseHelper;
import com.example.qldiem.models.TaiKhoan;
import com.example.qldiem.utils.SessionManager;
import com.example.qldiem.utils.ValidationUtils;

/**
 * Activity đăng nhập - nơi người dùng xác thực danh tính để truy cập ứng dụng
 *
 * CÔNG DỤNG:
 * - Cung cấp giao diện cho người dùng nhập thông tin đăng nhập
 * - Xác thực tên đăng nhập và mật khẩu với cơ sở dữ liệu
 * - Tạo session đăng nhập nếu xác thực thành công
 * - Điều hướng người dùng đến màn hình chính sau khi đăng nhập
 *
 * CÁC THÀNH PHẦN CHÍNH:
 * - edtUsername: EditText để nhập tên đăng nhập
 * - edtPassword: EditText để nhập mật khẩu
 * - btnLogin: Button để thực hiện đăng nhập
 * - tvRegister: TextView để chuyển đến màn hình đăng ký (đã ẩn trong phiên bản này)
 * - db: DatabaseHelper để truy vấn cơ sở dữ liệu
 * - session: SessionManager để quản lý phiên làm việc
 *
 * NẾU KHÔNG CÓ ACTIVITY NÀY:
 * - Người dùng không thể đăng nhập vào ứng dụng
 * - Toàn bộ hệ thống bảo mật và phân quyền sẽ không hoạt động
 * - Ứng dụng không thể xác thực danh tính người dùng
 */
public class LoginActivity extends AppCompatActivity {

    // EditText để người dùng nhập tên đăng nhập
    private EditText edtUsername, edtPassword;
    
    // Button để thực hiện hành động đăng nhập
    private Button btnLogin;
    
    // TextView để chuyển đến màn hình đăng ký (đã ẩn)
    private TextView tvRegister;
    
    // Quản lý truy cập cơ sở dữ liệu
    private DatabaseHelper db;
    
    // Quản lý phiên đăng nhập của người dùng
    private SessionManager session;

    /**
     * Hàm khởi tạo activity đăng nhập, thiết lập giao diện và xử lý trạng thái đăng nhập
     *
     * CÔNG DỤNG:
     * - Thiết lập layout cho activity (activity_login.xml)
     * - Khởi tạo các thành phần DatabaseHelper và SessionManager
     * - Kiểm tra nếu người dùng đã đăng nhập trước đó thì chuyển thẳng đến màn hình chính
     * - Ánh xạ các thành phần giao diện (EditText, Button, TextView)
     * - Thiết lập sự kiện click cho nút đăng nhập
     * - Ẩn liên kết đăng ký vì chỉ Admin mới được tạo tài khoản
     *
     * CÁC BIẾN TRONG HÀM:
     * - savedInstanceState: bundle chứa dữ liệu lưu trước đó khi activity bị hủy (nếu có)
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - savedInstanceState: chứa trạng thái trước đó của activity, giúp khôi phục dữ liệu nếu activity bị hủy do hệ thống
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Activity đăng nhập sẽ không được khởi tạo đúng cách
     * - Giao diện đăng nhập sẽ không hiển thị
     * - Không thể tương tác với các thành phần giao diện
     * - Ứng dụng sẽ bị lỗi khi mở activity đăng nhập
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Khởi tạo database và session
        db = new DatabaseHelper(this);
        session = new SessionManager(this);

        // Kiểm tra đã đăng nhập chưa
        if (session.isLoggedIn()) {
            navigateToMain();
            return;
        }

        // Ánh xạ views
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);

        // Xử lý sự kiện đăng nhập
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        // Ẩn link đăng ký - chỉ Admin mới tạo tài khoản
        tvRegister.setVisibility(View.GONE);
    }

    /**
     * Hàm xử lý quá trình đăng nhập của người dùng
     *
     * CÔNG DỤNG:
     * - Lấy thông tin tên đăng nhập và mật khẩu từ giao diện
     * - Thực hiện validation để đảm bảo thông tin không trống
     * - Gọi phương thức đăng nhập từ DatabaseHelper để xác thực
     * - Nếu đăng nhập thành công, tạo session và chuyển đến màn hình chính
     * - Nếu đăng nhập thất bại, hiển thị thông báo lỗi
     *
     * CÁC BIẾN TRONG HÀM:
     * - username: tên đăng nhập lấy từ EditText (đã loại bỏ khoảng trắng dư)
     * - password: mật khẩu lấy từ EditText (đã loại bỏ khoảng trắng dư)
     * - taiKhoan: đối tượng tài khoản tìm được từ cơ sở dữ liệu (null nếu không tìm thấy)
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - Không có đối số đầu vào, vì hàm sử dụng các thành viên của class (edtUsername, edtPassword)
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Người dùng không thể thực hiện đăng nhập
     * - Không có cơ chế xác thực danh tính
     * - Tính năng bảo mật của ứng dụng sẽ không hoạt động
     */
    private void login() {
        String username = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        // Validation
        if (ValidationUtils.isEmpty(username)) {
            edtUsername.setError("Vui lòng nhập tên đăng nhập");
            edtUsername.requestFocus();
            return;
        }

        if (ValidationUtils.isEmpty(password)) {
            edtPassword.setError("Vui lòng nhập mật khẩu");
            edtPassword.requestFocus();
            return;
        }

        // Kiểm tra đăng nhập
        TaiKhoan taiKhoan = db.login(username, password);
        
        if (taiKhoan != null) {
            // Đăng nhập thành công
            session.createLoginSession(
                taiKhoan.getTenTaiKhoan(),
                taiKhoan.getHoTen(),
                taiKhoan.getVaiTro()
            );
            
            Toast.makeText(this, "Đăng nhập thành công! Xin chào " + taiKhoan.getHoTen(), 
                Toast.LENGTH_SHORT).show();
            
            navigateToMain();
        } else {
            Toast.makeText(this, "Tên đăng nhập hoặc mật khẩu không đúng", 
                Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Hàm điều hướng người dùng từ màn hình đăng nhập sang màn hình chính
     *
     * CÔNG DỤNG:
     * - Tạo Intent để chuyển sang MainActivity
     * - Đặt cờ để xóa toàn bộ activity trong stack và bắt đầu MainActivity như là root
     * - Kết thúc LoginActivity để người dùng không thể quay lại màn hình đăng nhập khi nhấn nút back
     *
     * CÁC BIẾN TRONG HÀM:
     * - intent: đối tượng Intent để điều hướng giữa các activity
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - Không có đối số đầu vào, vì hàm sử dụng context của class hiện tại
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể chuyển từ màn hình đăng nhập sang màn hình chính
     * - Người dùng sẽ ở lại màn hình đăng nhập sau khi đăng nhập thành công
     * - Trải nghiệm người dùng sẽ bị gián đoạn
     */
    private void navigateToMain() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    /**
     * Hàm được gọi khi activity bị hủy, dùng để giải phóng tài nguyên
     *
     * CÔNG DỤNG:
     * - Đảm bảo đóng kết nối cơ sở dữ liệu nếu còn mở
     * - Giải phóng tài nguyên để tránh rò rỉ bộ nhớ
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - Không có đối số đầu vào
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Kết nối cơ sở dữ liệu có thể không được đóng đúng cách
     * - Có thể xảy ra rò rỉ tài nguyên/bộ nhớ
     * - Ứng dụng có thể gặp lỗi khi chạy lâu dài
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null) {
            db.close();
        }
    }
}
