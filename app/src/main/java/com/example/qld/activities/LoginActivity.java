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

public class LoginActivity extends AppCompatActivity {
    private EditText etUsername, etPassword;
    private Button btnLogin;
    private DatabaseManager dbManager;
    private SessionManager sessionManager;

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

    private void login() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validate input
        if (username.isEmpty()) {
            etUsername.setError("Vui lòng nhập tên đăng nhập");
            etUsername.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            etPassword.setError("Vui lòng nhập mật khẩu");
            etPassword.requestFocus();
            return;
        }

        try {
            dbManager.open();
            User user = dbManager.authenticateUser(username, password);
            
            if (user != null) {
                // Successful login
                sessionManager.createLoginSession(user);
                Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                
                // Redirect to appropriate main activity based on role
                redirectToMainActivity();
            } else {
                // Login failed
                Toast.makeText(this, "Tên đăng nhập hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi hệ thống: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            dbManager.close();
        }
    }

    private void redirectToMainActivity() {
        Intent intent;
        if (sessionManager.getUserRole() == 1) { // Teacher
            intent = new Intent(LoginActivity.this, TeacherMainActivity.class);
        } else { // Student
            intent = new Intent(LoginActivity.this, StudentMainActivity.class);
        }
        startActivity(intent);
        finish(); // Close login activity
    }
}