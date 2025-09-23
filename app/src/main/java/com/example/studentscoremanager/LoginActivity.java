package com.example.studentscoremanager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText edtUsername, edtPassword;
    MaterialButton btnLogin;
    TextView tvRegister, tvForgotPassword, tvAdminLogin;
    DatabaseHelper dbHelper;
    SharedPreferencesHelper prefsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // Ánh xạ view
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvAdminLogin = findViewById(R.id.tvAdminLogin);

        dbHelper = new DatabaseHelper(this);
        prefsHelper = new SharedPreferencesHelper(this);

        // Nút đăng nhập
        btnLogin.setOnClickListener(v -> loginOrRegisterUser());

        // Chuyển sang màn hình đăng ký thủ công
        tvRegister.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));

        // Quên mật khẩu
        tvForgotPassword.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, ForgotPassword.class)));

        // Link: Đăng nhập với tư cách Admin → mở màn đăng nhập admin riêng
        tvAdminLogin.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, AdminLoginActivity.class)));
    }

    private void loginOrRegisterUser() {
        String username = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        Log.d("LoginActivity", "Attempting login with username: " + username);

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        // Nếu tài khoản tồn tại → cho đăng nhập
        boolean userExists = dbHelper.checkUser(username, password);
        Log.d("LoginActivity", "User check result: " + userExists);
        
        if (userExists) {
            Log.d("LoginActivity", "Login successful");
            Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

            // Lưu thông tin đăng nhập vào SharedPreferences
            prefsHelper.saveLoginInfo(username, password, "20201234"); // Mặc định student ID

            // Điều hướng theo vai trò
            if ("admin".equalsIgnoreCase(username)) {
                startActivity(new Intent(LoginActivity.this, AdminDashboardActivity.class));
            } else {
                startActivity(new Intent(LoginActivity.this, StudentMainActivity.class));
            }
            finish();
        } else {
            Log.d("LoginActivity", "User not found, showing registration dialog");
            // Nếu chưa tồn tại → hỏi email để đăng ký mới
            showEmailDialog(username, password);
        }
    }

    private void showEmailDialog(String username, String password) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Tài khoản mới");
        builder.setMessage("Tài khoản chưa tồn tại.\nNhập email để đăng ký:");

        // Tạo ô nhập email
        final TextInputEditText input = new TextInputEditText(this);
        input.setHint("Email");
        input.setSingleLine(true);
        builder.setView(input);

        // Xử lý nút "Đăng ký"
        builder.setPositiveButton("Đăng ký", (dialog, which) -> {
            String email = input.getText() != null ? input.getText().toString().trim() : "";

            if (email.isEmpty()) {
                Toast.makeText(this, "Email không được để trống", Toast.LENGTH_SHORT).show();
            } else {
                boolean success = dbHelper.addUser(username, password, email);
                if (success) {
                    Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                    
                    // Lưu thông tin đăng nhập vào SharedPreferences
                    prefsHelper.saveLoginInfo(username, password, "20201234");
                    
                    // Chuyển đến trang chủ chính
                    startActivity(new Intent(LoginActivity.this, StudentMainActivity.class));
                    finish();
                } else {
                    Toast.makeText(this, "Tên đăng nhập đã tồn tại!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Nút hủy
        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

        builder.show();
    }
}
