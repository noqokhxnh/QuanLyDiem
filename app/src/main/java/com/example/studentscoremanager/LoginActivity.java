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
        btnLogin.setOnClickListener(v -> doLogin());

        // Vô hiệu tự đăng ký: chuyển sang ẩn/không hoạt động
        tvRegister.setOnClickListener(v ->
                Toast.makeText(LoginActivity.this, "Tài khoản do giảng viên tạo", Toast.LENGTH_SHORT).show());

        // Quên mật khẩu
        tvForgotPassword.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, ForgotPassword.class)));

        // Link: Đăng nhập với tư cách Giảng viên
        tvAdminLogin.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, AdminLoginActivity.class)));
    }

    private void doLogin() {
        String username = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        Log.d("LoginActivity", "Attempting login with username: " + username);

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        // Giảng viên đăng nhập đặc biệt bằng username cố định
        if ("giangvien".equalsIgnoreCase(username)) {
            boolean ok = dbHelper.checkUser(username, password);
            if (!ok) {
                Toast.makeText(this, "Sai thông tin giảng viên", Toast.LENGTH_SHORT).show();
                return;
            }
            prefsHelper.saveLoginInfo(username, password, prefsHelper.getCurrentStudentId());
            startActivity(new Intent(LoginActivity.this, AdminDashboardActivity.class));
            finish();
            return;
        }

        // Sinh viên: phải là tài khoản do giảng viên tạo sẵn
        boolean userExists = dbHelper.checkUser(username, password);
        Log.d("LoginActivity", "User check result: " + userExists);
        if (!userExists) {
            Toast.makeText(this, "Tài khoản chưa tồn tại. Vui lòng liên hệ giảng viên.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lưu studentId = username để ánh xạ hồ sơ
        prefsHelper.saveLoginInfo(username, password, username);
        Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(LoginActivity.this, StudentMainActivity.class));
        finish();
    }
}
