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
import com.example.qld.utils.PasswordUtil;
import com.example.qld.utils.SessionManager;

public class ChangePasswordActivity extends AppCompatActivity {
    private SessionManager sessionManager;
    private DatabaseManager dbManager;
    private EditText etCurrentPassword, etNewPassword, etConfirmNewPassword;
    private Button btnChangePassword, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        // Initialize session and database managers
        sessionManager = new SessionManager(this);
        dbManager = new DatabaseManager(this);

        // Check if user is logged in
        if (!sessionManager.isLoggedIn()) {
            redirectToLogin();
            return;
        }

        // Initialize views
        etCurrentPassword = findViewById(R.id.et_current_password);
        etNewPassword = findViewById(R.id.et_new_password);
        etConfirmNewPassword = findViewById(R.id.et_confirm_new_password);
        btnChangePassword = findViewById(R.id.btn_change_password);
        btnBack = findViewById(R.id.btn_back);

        // Set click listeners
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void changePassword() {
        String currentPassword = etCurrentPassword.getText().toString().trim();
        String newPassword = etNewPassword.getText().toString().trim();
        String confirmNewPassword = etConfirmNewPassword.getText().toString().trim();

        // Validate input
        if (currentPassword.isEmpty()) {
            etCurrentPassword.setError("Vui lòng nhập mật khẩu hiện tại");
            etCurrentPassword.requestFocus();
            return;
        }

        if (currentPassword.length() < 6) {
            etCurrentPassword.setError("Mật khẩu hiện tại không đúng");
            etCurrentPassword.requestFocus();
            return;
        }

        if (newPassword.isEmpty()) {
            etNewPassword.setError("Vui lòng nhập mật khẩu mới");
            etNewPassword.requestFocus();
            return;
        }

        if (newPassword.length() < 6) {
            etNewPassword.setError("Mật khẩu phải có ít nhất 6 ký tự");
            etNewPassword.requestFocus();
            return;
        }

        // Add additional password strength validation
        if (!isPasswordStrong(newPassword)) {
            etNewPassword.setError("Mật khẩu phải chứa ít nhất 1 chữ cái in hoa, 1 chữ cái thường và 1 số");
            etNewPassword.requestFocus();
            return;
        }

        if (!newPassword.equals(confirmNewPassword)) {
            etConfirmNewPassword.setError("Mật khẩu xác nhận không khớp");
            etConfirmNewPassword.requestFocus();
            return;
        }

        try {
            dbManager.open();
            
            // Get current user
            User currentUser = dbManager.getUserById(sessionManager.getUserId());
            
            if (currentUser == null) {
                ErrorHandler.showErrorToast(this, "Lỗi: Không tìm thấy người dùng");
                return;
            }
            
            // Verify current password using the verification function
            if (!PasswordUtil.verifyPassword(currentPassword, currentUser.getPassword())) {
                etCurrentPassword.setError("Mật khẩu hiện tại không đúng");
                etCurrentPassword.requestFocus();
                return;
            }
            
            // Update password in database
            currentUser.setPassword(newPassword);
            int result = dbManager.updateUser(currentUser);
            
            if (result > 0) {
                // Update session if needed
                // For this simple implementation, we just inform the user
                ErrorHandler.showSuccessSnackbar(findViewById(android.R.id.content), "Đổi mật khẩu thành công");
                finish();
            } else {
                ErrorHandler.showErrorToast(this, "Lỗi khi đổi mật khẩu");
            }
        } catch (Exception e) {
            ErrorHandler.showErrorToast(this, "Lỗi hệ thống: " + e.getMessage());
        } finally {
            dbManager.close();
        }
    }

    /**
     * Validates password strength
     * @param password the password to validate
     * @return true if password is strong, false otherwise
     */
    private boolean isPasswordStrong(String password) {
        // At least one uppercase letter
        boolean hasUppercase = !password.equals(password.toLowerCase());
        // At least one lowercase letter
        boolean hasLowercase = !password.equals(password.toUpperCase());
        // At least one digit
        boolean hasDigit = password.matches(".*\\d.*");

        return hasUppercase && hasLowercase && hasDigit;
    }

    private void redirectToLogin() {
        Intent intent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}