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
 * Activity để đăng ký tài khoản giáo viên
 * Cho phép tạo tài khoản giáo viên mới
 */
public class TeacherRegistrationActivity extends AppCompatActivity {
    private EditText etFullName, etUsername, etPassword, etConfirmPassword;
    private Button btnRegisterTeacher, btnCancel;
    private MySQLManager mysqlManager;
    private SessionManager sessionManager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_registration);
        
        // Initialize views
        etFullName = findViewById(R.id.et_full_name);
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        btnRegisterTeacher = findViewById(R.id.btn_register_teacher);
        btnCancel = findViewById(R.id.btn_cancel);
        
        // Initialize managers
        mysqlManager = new MySQLManager(this);
        sessionManager = new SessionManager(this);
        
        // Set click listeners
        btnRegisterTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerTeacher();
            }
        });
        
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    
    /**
     * Đăng ký tài khoản giáo viên mới
     */
    private void registerTeacher() {
        String fullName = etFullName.getText().toString().trim();
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();
        
        // Validate input
        if (fullName.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Check password length
        if (password.length() < 6) {
            Toast.makeText(this, "Mật khẩu phải có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Check password confirmation
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Mật khẩu xác nhận không khớp", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Check if username already exists
        mysqlManager.getUserByUsername(username, new MySQLManager.UserCallback() {
            @Override
            public void onSuccess(User user) {
                runOnUiThread(() -> {
                    if (user != null) {
                        Toast.makeText(TeacherRegistrationActivity.this, "Tên đăng nhập đã tồn tại", Toast.LENGTH_SHORT).show();
                    } else {
                        // Create new teacher user (role = 1)
                        User newUser = new User(username, password, 1, fullName); // role 1 = teacher
                        mysqlManager.addUser(newUser, new MySQLManager.UserCallback() {
                            @Override
                            public void onSuccess(User user) {
                                runOnUiThread(() -> {
                                    Toast.makeText(TeacherRegistrationActivity.this, "Đăng ký giáo viên thành công", Toast.LENGTH_SHORT).show();
                                    setResult(RESULT_OK);
                                    finish();
                                });
                            }
                            
                            @Override
                            public void onError(String error) {
                                runOnUiThread(() -> {
                                    Toast.makeText(TeacherRegistrationActivity.this, error, Toast.LENGTH_LONG).show();
                                });
                            }
                        });
                    }
                });
            }
            
            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(TeacherRegistrationActivity.this, error, Toast.LENGTH_LONG).show();
                });
            }
        });
    }
}