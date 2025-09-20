package com.example.qld.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qld.R;
import com.example.qld.database.mysql.MySQLManager;
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
    private MySQLManager mysqlManager;
    private SessionManager sessionManager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_report);
        
        // Khởi tạo các view
        btnExportReport = findViewById(R.id.btn_export_report);
        
        // Khởi tạo các manager
        mysqlManager = new MySQLManager(this);
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
        // Lấy tất cả dữ liệu
        mysqlManager.getAllStudents(new MySQLManager.StudentsCallback() {
            @Override
            public void onSuccess(List<Student> students) {
                mysqlManager.getAllSubjects(new MySQLManager.SubjectsCallback() {
                    @Override
                    public void onSuccess(List<Subject> subjects) {
                        mysqlManager.getAllScores(new MySQLManager.ScoresCallback() {
                            @Override
                            public void onSuccess(List<Score> scores) {
                                // Tạo nội dung báo cáo
                                StringBuilder report = new StringBuilder();
                                report.append("BÁO CÁO ĐIỂM\n");
                                report.append("Thời gian xuất: ").append(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date())).append("\n\n");
                                
                                // Danh sách học sinh
                                report.append("DANH SÁCH HỌC SINH:\n");
                                for (Student student : students) {
                                    mysqlManager.getUserById(student.getUserId(), new MySQLManager.UserCallback() {
                                        @Override
                                        public void onSuccess(com.example.qld.models.User user) {
                                            report.append("- ").append(user.getFullName())
                                                    .append(" (Mã: ").append(student.getStudentCode())
                                                    .append(", Lớp: ").append(student.getClassName()).append(")\n");
                                        }
                                        
                                        @Override
                                        public void onError(String error) {
                                            // Handle error
                                        }
                                    });
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
                                    mysqlManager.getStudentById(score.getStudentId(), new MySQLManager.StudentCallback() {
                                        @Override
                                        public void onSuccess(Student student) {
                                            mysqlManager.getSubjectById(score.getSubjectId(), new MySQLManager.SubjectCallback() {
                                                @Override
                                                public void onSuccess(Subject subject) {
                                                    mysqlManager.getUserById(student.getUserId(), new MySQLManager.UserCallback() {
                                                        @Override
                                                        public void onSuccess(com.example.qld.models.User user) {
                                                            mysqlManager.getUserById(score.getTeacherId(), new MySQLManager.UserCallback() {
                                                                @Override
                                                                public void onSuccess(com.example.qld.models.User teacher) {
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
                                                                
                                                                @Override
                                                                public void onError(String error) {
                                                                    // Handle error
                                                                }
                                                            });
                                                        }
                                                        
                                                        @Override
                                                        public void onError(String error) {
                                                            // Handle error
                                                        }
                                                    });
                                                }
                                                
                                                @Override
                                                public void onError(String error) {
                                                    // Handle error
                                                }
                                            });
                                        }
                                        
                                        @Override
                                        public void onError(String error) {
                                            // Handle error
                                        }
                                    });
                                }
                                
                                // Lưu vào file
                                String fileName = "report_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date()) + ".txt";
                                saveToFile(report.toString(), fileName);
                            }
                            
                            @Override
                            public void onError(String error) {
                                runOnUiThread(() -> {
                                    Toast.makeText(ExportReportActivity.this, "Lỗi lấy danh sách điểm: " + error, Toast.LENGTH_SHORT).show();
                                });
                            }
                        });
                    }
                    
                    @Override
                    public void onError(String error) {
                        runOnUiThread(() -> {
                            Toast.makeText(ExportReportActivity.this, "Lỗi lấy danh sách môn học: " + error, Toast.LENGTH_SHORT).show();
                        });
                    }
                });
            }
            
            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(ExportReportActivity.this, "Lỗi lấy danh sách học sinh: " + error, Toast.LENGTH_SHORT).show();
                });
            }
        });
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
            runOnUiThread(() -> {
                Toast.makeText(this, "Xuất báo cáo thành công. File được lưu trong: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
            });
        } catch (IOException e) {
            runOnUiThread(() -> {
                Toast.makeText(this, "Lỗi lưu file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        }
    }
}