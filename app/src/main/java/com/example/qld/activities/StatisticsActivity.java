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
import com.example.qld.models.Student;
import com.example.qld.models.User;
import com.example.qld.utils.SessionManager;

import java.util.List;

public class StatisticsActivity extends AppCompatActivity {
    private SessionManager sessionManager;
    private DatabaseManager dbManager;
    private TextView tvTitle, tvStatsInfo, tvAverageScore, tvTotalStudents, tvTotalScores;
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
        tvAverageScore = findViewById(R.id.tv_average_score);
        tvTotalStudents = findViewById(R.id.tv_total_students);
        tvTotalScores = findViewById(R.id.tv_total_scores);
        btnBack = findViewById(R.id.btn_back);

        // Customize UI based on user role
        String role = sessionManager.getUserRole();
        if ("TEACHER".equals(role)) {
            tvTitle.setText("Thống kê điểm (Giáo viên)");
            loadTeacherStatistics();
        } else if ("ADMIN".equals(role)) {
            tvTitle.setText("Thống kê điểm (Quản trị)");
            loadTeacherStatistics(); // Admin can view all statistics
        } else { // Student
            tvTitle.setText("Thống kê điểm (Học sinh)");
            loadStudentStatistics();
        }

        // Set click listeners
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void loadTeacherStatistics() {
        try {
            dbManager.open();
            
            // Get total number of students
            List<Student> students = dbManager.getAllStudents();
            int totalStudents = students.size();
            
            // Calculate overall average score
            double overallAverage = dbManager.calculateOverallAverageScore(0); // 0 for all students
            
            // Get total number of scores
            int totalScores = dbManager.getAllScores().size();
            
            // Display statistics
            tvTotalStudents.setText("Tổng số học sinh: " + totalStudents);
            tvTotalScores.setText("Tổng số điểm đã nhập: " + totalScores);
            
            if (overallAverage > 0) {
                tvAverageScore.setText(String.format("Điểm trung bình chung: %.2f", overallAverage));
            } else {
                tvAverageScore.setText("Điểm trung bình chung: Chưa có dữ liệu");
            }
            
            tvStatsInfo.setText("Thống kê hệ thống:\n- Thống kê điểm theo lớp\n- Thống kê điểm theo môn học\n- Biểu đồ điểm số");
            
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi khi tải thống kê: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            dbManager.close();
        }
    }

    private void loadStudentStatistics() {
        try {
            dbManager.open();
            
            // Get current student's ID
            int studentId = getStudentIdByUserId(sessionManager.getUserId());
            
            if (studentId > 0) {
                // Calculate average score for this student
                double averageScore = dbManager.calculateOverallAverageScore(studentId);
                
                // Get number of scores for this student
                int totalScores = dbManager.getScoresByStudentId(studentId).size();
                
                // Display statistics
                tvTotalScores.setText("Tổng số điểm: " + totalScores);
                
                if (averageScore > 0) {
                    tvAverageScore.setText(String.format("Điểm trung bình: %.2f", averageScore));
                } else {
                    tvAverageScore.setText("Điểm trung bình: Chưa có dữ liệu");
                }
                
                tvTotalStudents.setVisibility(View.GONE); // Hide for student view
                tvStatsInfo.setText("Thống kê cá nhân:\n- Điểm trung bình cá nhân\n- Tiến độ học tập\n- So sánh với lớp");
            } else {
                tvStatsInfo.setText("Không tìm thấy thông tin học sinh");
                tvAverageScore.setText("Điểm trung bình: Không xác định");
                tvTotalScores.setText("Tổng số điểm: 0");
            }
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi khi tải thống kê: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            dbManager.close();
        }
    }

    // Helper method to get student ID from user ID
    private int getStudentIdByUserId(int userId) {
        try {
            dbManager.open();
            User user = dbManager.getUserById(userId);
            dbManager.close();
            
            if (user != null) {
                return user.getStudentId(); // Get the student ID from the user record
            }
        } catch (Exception e) {
            // Log or handle the exception
        }
        
        return 0; // Return 0 if not found
    }

    private void redirectToLogin() {
        Intent intent = new Intent(StatisticsActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}