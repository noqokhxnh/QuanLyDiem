package com.example.qld.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qld.R;
import com.example.qld.database.DatabaseManager;
import com.example.qld.utils.SessionManager;

public class StatisticsActivity extends AppCompatActivity {
    private SessionManager sessionManager;
    private DatabaseManager dbManager;
    private TextView tvTitle, tvStatsInfo;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        // Initialize session and database managers
        sessionManager = new SessionManager(this);
        dbManager = new DatabaseManager(this);

        // Check if user is logged in
        if (!sessionManager.isLoggedIn()) {
            redirectToLogin();
            return;
        }

        // Initialize views
        tvTitle = findViewById(R.id.tv_title);
        tvStatsInfo = findViewById(R.id.tv_stats_info);
        btnBack = findViewById(R.id.btn_back);

        // Customize UI based on user role
        if (sessionManager.getUserRole() == 1) { // Teacher
            tvTitle.setText("Thống kê điểm (Giáo viên)");
            tvStatsInfo.setText("Tính năng thống kê dành cho giáo viên:\n- Thống kê điểm theo lớp\n- Thống kê điểm theo môn học\n- Biểu đồ điểm số");
        } else { // Student
            tvTitle.setText("Thống kê điểm (Học sinh)");
            tvStatsInfo.setText("Tính năng thống kê dành cho học sinh:\n- Điểm trung bình cá nhân\n- Tiến độ học tập\n- So sánh với lớp");
        }

        // Set click listeners
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void redirectToLogin() {
        Intent intent = new Intent(StatisticsActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}