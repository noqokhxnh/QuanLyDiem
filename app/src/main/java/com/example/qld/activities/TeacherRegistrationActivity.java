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
import com.example.qld.utils.SessionManager;

/**
 * Activity để đăng ký tài khoản giáo viên
 * Cho phép tạo tài khoản giáo viên mới
 */
public class TeacherRegistrationActivity extends AppCompatActivity {
    private EditText etFullName, etUsername, etPassword, etConfirmPassword;
    private Button btnRegisterTeacher, btnCancel;
    private DatabaseManager dbManager;
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
        dbManager = new DatabaseManager(this);
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
        
        try {
            dbManager.open();
            
            // Check if username already exists
            User existingUser = dbManager.getUserByUsername(username);
            if (existingUser != null) {
                Toast.makeText(this, "Tên đăng nhập đã tồn tại", Toast.LENGTH_SHORT).show();
                return;
            }
            
            // Create new teacher user (role = 1)
            User user = new User(username, password, 1, fullName); // role 1 = teacher
            long userId = dbManager.addUser(user);
            
            if (userId != -1) {
                Toast.makeText(this, "Đăng ký giáo viên thành công", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            } else {
                Toast.makeText(this, "Lỗi khi đăng ký tài khoản", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi đăng ký giáo viên: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            dbManager.close();
        }
    }
}