package com.example.studentscoremanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import android.widget.TextView;

public class AdminLoginActivity extends AppCompatActivity {

    private TextInputEditText edtUsername, edtPassword;
    private MaterialButton btnLogin;
    private TextView btnGoRegister, btnGoForgot, btnBackToStudent;
    private DatabaseHelper dbHelper;
    private SharedPreferencesHelper prefsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_login);

        dbHelper = new DatabaseHelper(this);
        prefsHelper = new SharedPreferencesHelper(this);

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnGoRegister = findViewById(R.id.btnGoRegister);
        btnGoForgot = findViewById(R.id.btnGoForgot);
        btnBackToStudent = findViewById(R.id.btnBackToStudent);

        btnLogin.setOnClickListener(v -> doLogin());
        btnGoRegister.setOnClickListener(v -> startActivity(new Intent(this, AdminRegisterActivity.class)));
        btnGoForgot.setOnClickListener(v -> startActivity(new Intent(this, AdminForgotPasswordActivity.class)));
        btnBackToStudent.setOnClickListener(v -> finish());
    }

    private void doLogin() {
        String username = edtUsername.getText() == null ? "" : edtUsername.getText().toString().trim();
        String password = edtPassword.getText() == null ? "" : edtPassword.getText().toString().trim();
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ", Toast.LENGTH_SHORT).show();
            return;
        }
        // Giới hạn chỉ tài khoản admin
        if (!"admin".equalsIgnoreCase(username)) {
            Toast.makeText(this, "Chỉ tài khoản admin được phép", Toast.LENGTH_SHORT).show();
            return;
        }
        boolean ok = dbHelper.checkUser(username, password);
        if (ok) {
            Toast.makeText(this, "Đăng nhập admin thành công", Toast.LENGTH_SHORT).show();
            // Lưu session admin
            prefsHelper.saveLoginInfo("admin", password, prefsHelper.getCurrentStudentId());
            startActivity(new Intent(this, AdminDashboardActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show();
        }
    }
}


