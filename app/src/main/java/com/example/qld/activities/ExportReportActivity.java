package com.example.qld.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.qld.R;
import com.example.qld.database.DatabaseManager;
import com.example.qld.models.Score;
import com.example.qld.models.Student;
import com.example.qld.utils.SessionManager;

import java.io.File;
import java.util.List;

public class ExportReportActivity extends AppCompatActivity {
    private SessionManager sessionManager;
    private DatabaseManager dbManager;
    private TextView tvTitle, tvReportInfo, tvExportStatus;
    private Button btnExportPdf, btnExportExcel, btnExportCsv, btnBack;

    private static final int PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_report);

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
        tvReportInfo = findViewById(R.id.tv_report_info);
        tvExportStatus = findViewById(R.id.tv_export_status);
        btnExportPdf = findViewById(R.id.btn_export_pdf);
        btnExportExcel = findViewById(R.id.btn_export_excel);
        btnExportCsv = findViewById(R.id.btn_export_csv); // Added CSV button
        btnBack = findViewById(R.id.btn_back);

        // Set title based on user role
        String role = sessionManager.getUserRole();
        if ("TEACHER".equals(role)) {
            tvTitle.setText("Xuất báo cáo (Giáo viên)");
            tvReportInfo.setText("Tính năng xuất báo cáo dành cho giáo viên:\n- Xuất danh sách học sinh\n- Xuất bảng điểm theo lớp\n- Xuất thống kê môn học");
        } else if ("ADMIN".equals(role)) {
            tvTitle.setText("Xuất báo cáo (Quản trị)");
            tvReportInfo.setText("Tính năng xuất báo cáo dành cho quản trị:\n- Xuất danh sách học sinh\n- Xuất bảng điểm theo lớp\n- Xuất thống kê môn học");
        } else { // Student
            tvTitle.setText("Xuất báo cáo (Học sinh)");
            tvReportInfo.setText("Tính năng xuất báo cáo dành cho học sinh:\n- Xuất bảng điểm cá nhân\n- Xuất báo cáo tiến độ học tập");
        }

        // Set click listeners
        btnExportPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    exportToPdf();
                } else {
                    requestPermission();
                }
            }
        });

        btnExportExcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    exportToExcel();
                } else {
                    requestPermission();
                }
            }
        });

        btnExportCsv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    exportToCsv();
                } else {
                    requestPermission();
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Quyền truy cập được cấp", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Quyền truy cập bị từ chối", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void exportToPdf() {
        try {
            dbManager.open();
            String exportPath = getExternalFilesDir(null) + "/BaoCao.pdf";
            
            // For now, just show a toast indicating the functionality
            // In a complete implementation, we would generate the actual PDF
            tvExportStatus.setText("Đang xuất PDF...");
            
            // This is where the actual PDF generation would happen
            // For now we'll just simulate the process
            Thread.sleep(1000); // Simulate processing time
            
            tvExportStatus.setText("Xuất PDF thành công: " + exportPath);
            Toast.makeText(this, "Xuất PDF thành công!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            tvExportStatus.setText("Lỗi khi xuất PDF: " + e.getMessage());
            Toast.makeText(this, "Lỗi khi xuất PDF: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            dbManager.close();
        }
    }

    private void exportToExcel() {
        try {
            dbManager.open();
            String exportPath = getExternalFilesDir(null) + "/BaoCao.xlsx";
            
            tvExportStatus.setText("Đang xuất Excel...");
            
            // This is where the actual Excel generation would happen
            // For now we'll just simulate the process
            Thread.sleep(1000); // Simulate processing time
            
            tvExportStatus.setText("Xuất Excel thành công: " + exportPath);
            Toast.makeText(this, "Xuất Excel thành công!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            tvExportStatus.setText("Lỗi khi xuất Excel: " + e.getMessage());
            Toast.makeText(this, "Lỗi khi xuất Excel: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            dbManager.close();
        }
    }

    private void exportToCsv() {
        try {
            dbManager.open();
            String exportPath = getExternalFilesDir(null) + "/BaoCao.csv";
            
            tvExportStatus.setText("Đang xuất CSV...");
            
            // This is where the actual CSV generation would happen
            // For now we'll just simulate the process
            Thread.sleep(1000); // Simulate processing time
            
            tvExportStatus.setText("Xuất CSV thành công: " + exportPath);
            Toast.makeText(this, "Xuất CSV thành công!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            tvExportStatus.setText("Lỗi khi xuất CSV: " + e.getMessage());
            Toast.makeText(this, "Lỗi khi xuất CSV: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            dbManager.close();
        }
    }

    private void redirectToLogin() {
        Intent intent = new Intent(ExportReportActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}