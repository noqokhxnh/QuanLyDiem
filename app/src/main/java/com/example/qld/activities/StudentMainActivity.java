package com.example.qld.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qld.R;
import com.example.qld.database.DatabaseManager;
import com.example.qld.models.Student;
import com.example.qld.utils.SessionManager;

public class StudentMainActivity extends AppCompatActivity {
    private Button btnViewScores, btnChangePassword, btnLogout;
    private TextView tvWelcome;
    private SessionManager sessionManager;
    private DatabaseManager dbManager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);
        
        // Initialize views
        tvWelcome = findViewById(R.id.tv_welcome);
        btnViewScores = findViewById(R.id.btn_view_scores);
        btnChangePassword = findViewById(R.id.btn_change_password);
        btnLogout = findViewById(R.id.btn_logout);
        
        // Initialize managers
        sessionManager = new SessionManager(this);
        dbManager = new DatabaseManager(this);
        
        // Check if user is logged in
        if (!sessionManager.isLoggedIn() || sessionManager.getUserRole() != 0) {
            // Redirect to login if not logged in or not a student
            Intent intent = new Intent(StudentMainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        
        // Set welcome message
        String fullName = sessionManager.getFullName();
        tvWelcome.setText("Chào mừng, " + fullName);
        
        // Set click listeners
        btnViewScores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentMainActivity.this, ViewScoresActivity.class);
                startActivity(intent);
            }
        });
        
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentMainActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
            }
        });
        
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.logout();
                Intent intent = new Intent(StudentMainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}