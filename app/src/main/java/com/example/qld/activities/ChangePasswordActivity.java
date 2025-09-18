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
 * Activity để thay đổi mật khẩu
 * Cho phép cả giáo viên và học sinh thay đổi mật khẩu
 */
public class ChangePasswordActivity extends AppCompatActivity {
    private EditText etCurrentPassword, etNewPassword, etConfirmPassword;
    private Button btnChangePassword, btnCancel;
    private DatabaseManager dbManager;
    private SessionManager sessionManager;
    private User currentUser;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        
        // Initialize views
        etCurrentPassword = findViewById(R.id.et_current_password);
        etNewPassword = findViewById(R.id.et_new_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        btnChangePassword = findViewById(R.id.btn_change_password);
        btnCancel = findViewById(R.id.btn_cancel);
        
        // Initialize managers
        dbManager = new DatabaseManager(this);
        sessionManager = new SessionManager(this);
        
        // Check if user is logged in
        if (!sessionManager.isLoggedIn()) {
            // Redirect to login if not logged in
            Intent intent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        
        // Load current user
        loadCurrentUser();
        
        // Set click listeners
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
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
     * Tải thông tin người dùng hiện tại từ database
     */
    private void loadCurrentUser() {
        try {
            dbManager.open();
            currentUser = dbManager.getUserById(sessionManager.getUserId());
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi tải thông tin người dùng: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            dbManager.close();
        }
    }
    
    /**
     * Thay đổi mật khẩu người dùng
     */
    private void changePassword() {
        String currentPassword = etCurrentPassword.getText().toString().trim();
        String newPassword = etNewPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();
        
        // Validate input
        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Check current password
        if (!currentPassword.equals(currentUser.getPassword())) {
            Toast.makeText(this, "Mật khẩu hiện tại không đúng", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Check new password and confirm password match
        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(this, "Mật khẩu mới và xác nhận mật khẩu không khớp", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Check password length
        if (newPassword.length() < 6) {
            Toast.makeText(this, "Mật khẩu mới phải có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Update password
        try {
            dbManager.open();
            
            // Create updated user object
            User updatedUser = new User();
            updatedUser.setId(currentUser.getId());
            updatedUser.setUsername(currentUser.getUsername());
            updatedUser.setPassword(newPassword); // New password
            updatedUser.setRole(currentUser.getRole());
            updatedUser.setFullName(currentUser.getFullName());
            updatedUser.setCreatedDate(currentUser.getCreatedDate());
            
            // Update user in database
            int result = dbManager.updateUser(updatedUser);
            
            if (result > 0) {
                Toast.makeText(this, "Thay đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Lỗi khi thay đổi mật khẩu", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi thay đổi mật khẩu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            dbManager.close();
        }
    }
}