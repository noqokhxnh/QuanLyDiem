package com.example.qld.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qld.R;
import com.example.qld.database.mysql.MySQLManager;
import com.example.qld.models.User;
import com.example.qld.utils.SessionManager;

/**
 * Activity để đăng nhập vào hệ thống
 * Cho phép giáo viên và học sinh đăng nhập
 */
public class LoginActivity extends AppCompatActivity {
    private EditText etUsername, etPassword;
    private Button btnLogin, btnRegisterTeacher;
    private MySQLManager mysqlManager;
    private SessionManager sessionManager;
    
    private static final int TEACHER_REGISTRATION_REQUEST = 1;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        // Initialize views
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        btnRegisterTeacher = findViewById(R.id.btn_register_teacher);
        
        // Initialize managers
        mysqlManager = new MySQLManager(this);
        sessionManager = new SessionManager(this);
        
        // Check if user is already logged in
        if (sessionManager.isLoggedIn()) {
            redirectToMainActivity();
            return;
        }
        
        // Set click listeners
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
        
        btnRegisterTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open teacher registration activity
                Intent intent = new Intent(LoginActivity.this, TeacherRegistrationActivity.class);
                startActivityForResult(intent, TEACHER_REGISTRATION_REQUEST);
            }
        });
    }
    
    /**
     * Đăng nhập người dùng vào hệ thống
     */
    private void loginUser() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        
        // Validate input
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Show loading indicator
        btnLogin.setEnabled(false);
        btnLogin.setText("Đang đăng nhập...");
        
        // Authenticate user
        mysqlManager.authenticateUser(username, password, new MySQLManager.UserCallback() {
            @Override
            public void onSuccess(User user) {
                runOnUiThread(() -> {
                    btnLogin.setEnabled(true);
                    btnLogin.setText("Đăng nhập");
                    
                    if (user != null) {
                        // Login successful
                        sessionManager.createLoginSession(user.getId(), user.getUsername(), user.getFullName(), user.getRole());
                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                        redirectToMainActivity();
                    } else {
                        // Login failed
                        Toast.makeText(LoginActivity.this, "Tên đăng nhập hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            
            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    btnLogin.setEnabled(true);
                    btnLogin.setText("Đăng nhập");
                    Toast.makeText(LoginActivity.this, "Lỗi đăng nhập: " + error, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
    
    /**
     * Chuyển hướng người dùng đến màn hình chính phù hợp với vai trò
     */
    private void redirectToMainActivity() {
        int role = sessionManager.getUserRole();
        Intent intent;
        
        if (role == 1) {
            // Teacher
            intent = new Intent(LoginActivity.this, TeacherMainActivity.class);
        } else if (role == 0) {
            // Student
            intent = new Intent(LoginActivity.this, StudentMainActivity.class);
        } else {
            // Default to login if role is not recognized
            sessionManager.logout();
            intent = new Intent(LoginActivity.this, LoginActivity.class);
        }
        
        startActivity(intent);
        finish();
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == TEACHER_REGISTRATION_REQUEST) {
            if (resultCode == RESULT_OK) {
                // Teacher registration successful
                Toast.makeText(this, "Đăng ký giáo viên thành công. Bạn có thể đăng nhập ngay bây giờ.", Toast.LENGTH_LONG).show();
            }
        }
    }
}