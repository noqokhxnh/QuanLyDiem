package com.example.qld.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qld.R;
import com.example.qld.utils.SessionManager;

public class ExportReportActivity extends AppCompatActivity {
    private SessionManager sessionManager;
    private TextView tvTitle, tvReportInfo;
    private Button btnExportPdf, btnExportExcel, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_report);

        // Initialize session manager
        sessionManager = new SessionManager(this);

        // Check if user is logged in
        if (!sessionManager.isLoggedIn()) {
            redirectToLogin();
            return;
        }

        // Initialize views
        tvTitle = findViewById(R.id.tv_title);
        tvReportInfo = findViewById(R.id.tv_report_info);
        btnExportPdf = findViewById(R.id.btn_export_pdf);
        btnExportExcel = findViewById(R.id.btn_export_excel);
        btnBack = findViewById(R.id.btn_back);

        // Set title based on user role
        if (sessionManager.getUserRole() == 1) { // Teacher
            tvTitle.setText("Xuất báo cáo (Giáo viên)");
            tvReportInfo.setText("Tính năng xuất báo cáo dành cho giáo viên:\n- Xuất danh sách học sinh\n- Xuất bảng điểm theo lớp\n- Xuất thống kê môn học");
        } else { // Student
            tvTitle.setText("Xuất báo cáo (Học sinh)");
            tvReportInfo.setText("Tính năng xuất báo cáo dành cho học sinh:\n- Xuất bảng điểm cá nhân\n- Xuất báo cáo tiến độ học tập");
        }

        // Set click listeners
        btnExportPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ExportReportActivity.this, "Xuất PDF thành công", Toast.LENGTH_SHORT).show();
            }
        });

        btnExportExcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ExportReportActivity.this, "Xuất Excel thành công", Toast.LENGTH_SHORT).show();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void redirectToLogin() {
        Intent intent = new Intent(ExportReportActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}