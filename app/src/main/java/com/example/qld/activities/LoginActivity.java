package com.example.qld.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qld.R;
import com.example.qld.database.DatabaseManager;
import com.example.qld.models.User;
import com.example.qld.utils.ErrorHandler;
import com.example.qld.utils.SessionManager;

public class LoginActivity extends AppCompatActivity {
    private EditText etUsername, etPassword;
    private Button btnLogin;
    private DatabaseManager dbManager;
    private SessionManager sessionManager;

    /**
     * Phương thức được gọi khi activity được tạo
     * Khởi tạo giao diện, các thành phần và thiết lập các sự kiện
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize views
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);

        // Initialize database manager and session manager
        dbManager = new DatabaseManager(this);
        sessionManager = new SessionManager(this);

        // Check if user is already logged in
        if (sessionManager.isLoggedIn()) {
            redirectToMainActivity();
        }

        // Set login button click listener
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    /**
     * Phương thức thực hiện quá trình đăng nhập
     * Xác thực thông tin đăng nhập của người dùng trong hệ thống
     * 
     * Cách thức hoạt động:
     * 1. Lấy thông tin username và password từ giao diện
     * 2. Thực hiện kiểm tra tính hợp lệ của dữ liệu đầu vào:
     *    - Kiểm tra xem có nhập username hay không
     *    - Kiểm tra username có tối thiểu 3 ký tự hay không
     *    - Kiểm tra có nhập password hay không
     *    - Kiểm tra password có tối thiểu 6 ký tự hay không
     * 3. Nếu dữ liệu hợp lệ, kết nối đến cơ sở dữ liệu và xác thực người dùng
     * 4. Nếu xác thực thành công, tạo session và chuyển hướng người dùng
     * 5. Nếu xác thực thất bại, hiển thị thông báo lỗi và yêu cầu nhập lại
     * 
     * @param username tên đăng nhập do người dùng nhập vào
     * @param password mật khẩu do người dùng nhập vào (đã được mã hóa)
     * @return void - phương thức không trả về giá trị, thực hiện đăng nhập hệ thống
     */
    private void login() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validate input
        if (username.isEmpty()) {
            etUsername.setError("Vui lòng nhập tên đăng nhập");
            etUsername.requestFocus();
            return;
        }

        if (username.length() < 3) {
            etUsername.setError("Tên đăng nhập phải có ít nhất 3 ký tự");
            etUsername.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            etPassword.setError("Vui lòng nhập mật khẩu");
            etPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            etPassword.setError("Mật khẩu phải có ít nhất 6 ký tự");
            etPassword.requestFocus();
            return;
        }

        try {
            dbManager.open();
            User user = dbManager.authenticateUser(username, password);
            
            if (user != null) {

                sessionManager.createLoginSession(user);
                ErrorHandler.showSuccessSnackbar(findViewById(android.R.id.content), "Đăng nhập thành công");
                

                redirectToMainActivity();
            } else {

                etPassword.setText("");
                etPassword.requestFocus();
                ErrorHandler.showErrorSnackbar(findViewById(android.R.id.content), "Tên đăng nhập hoặc mật khẩu không đúng");
            }
        } catch (Exception e) {
            ErrorHandler.showErrorToast(this, "Lỗi hệ thống: " + e.getMessage());
        } finally {
            dbManager.close();
        }
    }

    /**
     * Phương thức chuyển hướng người dùng đến activity chính tương ứng
     * Dựa trên vai trò (role) của người dùng sau khi đăng nhập thành công
     * 
     * Cách thức hoạt động:
     * 1. Lấy vai trò của người dùng từ session
     * 2. Dựa vào vai trò để xác định activity chính tương ứng:
     *    - Nếu là ADMIN thì chuyển đến AdminMainActivity
     *    - Nếu là TEACHER thì chuyển đến TeacherMainActivity
     *    - Nếu là STUDENT thì chuyển đến StudentMainActivity
     * 3. Khởi tạo Intent và bắt đầu activity mới
     * 4. Kết thúc activity hiện tại để người dùng không thể quay lại màn hình đăng nhập
     * 
     * @param role vai trò của người dùng đã đăng nhập ('ADMIN', 'TEACHER', 'STUDENT')
     */
    private void redirectToMainActivity() {
        Intent intent;
        String role = sessionManager.getUserRole();
        
        switch (role) {
            case "ADMIN":
                intent = new Intent(LoginActivity.this, AdminMainActivity.class);
                break;
            case "TEACHER":
                intent = new Intent(LoginActivity.this, TeacherMainActivity.class);
                break;
            case "STUDENT":
            default:
                intent = new Intent(LoginActivity.this, StudentMainActivity.class);
                break;
        }
        startActivity(intent);
        finish();
    }
}