package com.example.qld.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qld.R;
import com.example.qld.utils.SessionManager;

public class TeacherMainActivity extends AppCompatActivity {
    private Button btnManageStudents, btnManageScores, btnStatistics, btnExportReport, btnChangePassword, btnLogout;
    private SessionManager sessionManager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_main);
        
        // Initialize views
        btnManageStudents = findViewById(R.id.btn_manage_students);
        btnManageScores = findViewById(R.id.btn_manage_scores);
        btnStatistics = findViewById(R.id.btn_statistics);
        btnExportReport = findViewById(R.id.btn_export_report);
        btnChangePassword = findViewById(R.id.btn_change_password);
        btnLogout = findViewById(R.id.btn_logout);
        
        // Initialize session manager
        sessionManager = new SessionManager(this);
        
        // Check if user is logged in
        if (!sessionManager.isLoggedIn() || sessionManager.getUserRole() != 1) {
            // Redirect to login if not logged in or not a teacher
            Intent intent = new Intent(TeacherMainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        
        // Set click listeners
        btnManageStudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeacherMainActivity.this, ManageStudentsActivity.class);
                startActivity(intent);
            }
        });
        
        btnManageScores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeacherMainActivity.this, ManageScoresActivity.class);
                startActivity(intent);
            }
        });
        
        btnStatistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeacherMainActivity.this, StatisticsActivity.class);
                startActivity(intent);
            }
        });
        
        btnExportReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeacherMainActivity.this, ExportReportActivity.class);
                startActivity(intent);
            }
        });
        
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeacherMainActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
            }
        });
        
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.logout();
                Intent intent = new Intent(TeacherMainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}