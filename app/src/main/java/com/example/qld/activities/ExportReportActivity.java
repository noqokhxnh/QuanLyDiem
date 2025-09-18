package com.example.qld.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qld.R;
import com.example.qld.database.DatabaseManager;
import com.example.qld.models.Score;
import com.example.qld.models.Student;
import com.example.qld.models.Subject;
import com.example.qld.utils.SessionManager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Activity để xuất báo cáo điểm cho giáo viên
 * Cho phép xuất báo cáo dưới dạng file text
 */
public class ExportReportActivity extends AppCompatActivity {
    private Button btnExportReport;
    private DatabaseManager dbManager;
    private SessionManager sessionManager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_report);
        
        // Khởi tạo các view
        btnExportReport = findViewById(R.id.btn_export_report);
        
        // Khởi tạo các manager
        dbManager = new DatabaseManager(this);
        sessionManager = new SessionManager(this);
        
        // Kiểm tra xem người dùng đã đăng nhập chưa và có phải giáo viên không
        if (!sessionManager.isLoggedIn() || sessionManager.getUserRole() != 1) {
            // Chuyển hướng đến màn hình đăng nhập nếu chưa đăng nhập hoặc không phải giáo viên
            Intent intent = new Intent(ExportReportActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        
        // Thiết lập thanh điều hướng
        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        
        // Thiết lập các sự kiện click
        btnExportReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exportReport();
            }
        });
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    
    /**
     * Xuất báo cáo điểm ra file
     */
    private void exportReport() {
        try {
            dbManager.open();
            
            // Lấy tất cả dữ liệu
            List<Student> students = dbManager.getAllStudents();
            List<Subject> subjects = dbManager.getAllSubjects();
            List<Score> scores = dbManager.getAllScores();
            
            // Tạo nội dung báo cáo
            StringBuilder report = new StringBuilder();
            report.append("BÁO CÁO ĐIỂM\n");
            report.append("Thời gian xuất: ").append(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date())).append("\n\n");
            
            // Danh sách học sinh
            report.append("DANH SÁCH HỌC SINH:\n");
            for (Student student : students) {
                com.example.qld.models.User user = dbManager.getUserById(student.getUserId());
                if (user != null) {
                    report.append("- ").append(user.getFullName())
                            .append(" (Mã: ").append(student.getStudentCode())
                            .append(", Lớp: ").append(student.getClassName()).append(")\n");
                }
            }
            
            report.append("\n");
            
            // Danh sách môn học
            report.append("DANH SÁCH MÔN HỌC:\n");
            for (Subject subject : subjects) {
                report.append("- ").append(subject.getSubjectName())
                        .append(" (Mã: ").append(subject.getSubjectCode()).append(")\n");
            }
            
            report.append("\n");
            
            // Danh sách điểm
            report.append("DANH SÁCH ĐIỂM:\n");
            for (Score score : scores) {
                Student student = dbManager.getStudentById(score.getStudentId());
                Subject subject = dbManager.getSubjectById(score.getSubjectId());
                com.example.qld.models.User user = dbManager.getUserById(student.getUserId());
                com.example.qld.models.User teacher = dbManager.getUserById(score.getTeacherId());
                
                String studentName = (user != null) ? user.getFullName() : "Unknown";
                String subjectName = (subject != null) ? subject.getSubjectName() : "Unknown";
                String teacherName = (teacher != null) ? teacher.getFullName() : "Unknown";
                String scoreType = getScoreTypeDisplay(score.getScoreType());
                
                report.append("- Học sinh: ").append(studentName)
                        .append(", Môn: ").append(subjectName)
                        .append(", Loại: ").append(scoreType)
                        .append(", Điểm: ").append(score.getScore())
                        .append(", Giáo viên: ").append(teacherName)
                        .append(", Ngày: ").append(score.getDateCreated()).append("\n");
            }
            
            // Lưu vào file
            String fileName = "report_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date()) + ".txt";
            saveToFile(report.toString(), fileName);
            
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi xuất báo cáo: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            dbManager.close();
        }
    }
    
    /**
     * Chuyển loại điểm sang dạng hiển thị tiếng Việt
     */
    private String getScoreTypeDisplay(String scoreType) {
        switch (scoreType) {
            case "mieng":
                return "Miệng";
            case "15phut":
                return "15 phút";
            case "1tiet":
                return "1 tiết";
            case "hocky":
                return "Học kỳ";
            default:
                return scoreType;
        }
    }
    
    /**
     * Lưu nội dung vào file trong thư mục Downloads
     */
    private void saveToFile(String content, String fileName) {
        try {
            // Sử dụng thư mục Downloads
            File downloadsDir = android.os.Environment.getExternalStoragePublicDirectory(android.os.Environment.DIRECTORY_DOWNLOADS);
            File file = new File(downloadsDir, fileName);
            FileWriter writer = new FileWriter(file);
            writer.write(content);
            writer.close();
            
            // Hiển thị thông báo thành công với vị trí file
            Toast.makeText(this, "Xuất báo cáo thành công. File được lưu trong: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(this, "Lỗi lưu file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}