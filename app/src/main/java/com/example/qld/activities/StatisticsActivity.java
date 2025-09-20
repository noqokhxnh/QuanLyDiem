package com.example.qld.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qld.R;
import com.example.qld.database.mysql.MySQLManager;
import com.example.qld.models.Score;
import com.example.qld.models.Student;
import com.example.qld.models.Subject;
import com.example.qld.utils.SessionManager;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Activity để hiển thị thống kê điểm cho giáo viên
 * Cho phép xem thống kê điểm theo lớp/môn
 */
public class StatisticsActivity extends AppCompatActivity {
    private TextView tvStatistics;
    private MySQLManager mysqlManager;
    private SessionManager sessionManager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        
        // Initialize views
        tvStatistics = findViewById(R.id.tv_statistics);
        
        // Initialize managers
        mysqlManager = new MySQLManager(this);
        sessionManager = new SessionManager(this);
        
        // Check if user is logged in as teacher
        if (!sessionManager.isLoggedIn() || sessionManager.getUserRole() != 1) {
            // Redirect to login if not logged in or not a teacher
            Intent intent = new Intent(StatisticsActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        
        // Set up navigation
        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        
        // Load statistics
        loadStatistics();
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    
    /**
     * Tải và hiển thị thống kê điểm
     */
    private void loadStatistics() {
        mysqlManager.getAllStudents(new MySQLManager.StudentsCallback() {
            @Override
            public void onSuccess(List<Student> students) {
                mysqlManager.getAllSubjects(new MySQLManager.SubjectsCallback() {
                    @Override
                    public void onSuccess(List<Subject> subjects) {
                        mysqlManager.getAllScores(new MySQLManager.ScoresCallback() {
                            @Override
                            public void onSuccess(List<Score> scores) {
                                // Build statistics string
                                StringBuilder stats = new StringBuilder();
                                stats.append("THỐNG KÊ ĐIỂM\n\n");
                                
                                // Overall statistics
                                stats.append("Tổng số học sinh: ").append(students.size()).append("\n");
                                stats.append("Tổng số môn học: ").append(subjects.size()).append("\n");
                                stats.append("Tổng số điểm đã nhập: ").append(scores.size()).append("\n\n");
                                
                                // Calculate class average
                                if (!scores.isEmpty()) {
                                    double total = 0;
                                    for (Score score : scores) {
                                        total += score.getScore();
                                    }
                                    double classAverage = total / scores.size();
                                    DecimalFormat df = new DecimalFormat("#.##");
                                    stats.append("Điểm trung bình toàn lớp: ").append(df.format(classAverage)).append("\n\n");
                                }
                                
                                // Subject averages
                                stats.append("ĐIỂM TRUNG BÌNH THEO MÔN:\n");
                                for (Subject subject : subjects) {
                                    List<Score> subjectScores = getScoresBySubject(scores, subject.getId());
                                    if (!subjectScores.isEmpty()) {
                                        double total = 0;
                                        for (Score score : subjectScores) {
                                            total += score.getScore();
                                        }
                                        double subjectAverage = total / subjectScores.size();
                                        DecimalFormat df = new DecimalFormat("#.##");
                                        stats.append("- ").append(subject.getSubjectName()).append(": ").append(df.format(subjectAverage)).append("\n");
                                    }
                                }
                                
                                // Student averages
                                stats.append("\nĐIỂM TRUNG BÌNH THEO HỌC SINH:\n");
                                for (Student student : students) {
                                    mysqlManager.getUserById(student.getUserId(), new MySQLManager.UserCallback() {
                                        @Override
                                        public void onSuccess(com.example.qld.models.User user) {
                                            List<Score> studentScores = getScoresByStudent(scores, student.getId());
                                            if (!studentScores.isEmpty()) {
                                                double total = 0;
                                                for (Score score : studentScores) {
                                                    total += score.getScore();
                                                }
                                                double studentAverage = total / studentScores.size();
                                                DecimalFormat df = new DecimalFormat("#.##");
                                                stats.append("- ").append(user.getFullName()).append(": ").append(df.format(studentAverage)).append("\n");
                                            }
                                        }
                                        
                                        @Override
                                        public void onError(String error) {
                                            // Handle error
                                        }
                                    });
                                }
                                
                                runOnUiThread(() -> {
                                    tvStatistics.setText(stats.toString());
                                });
                            }
                            
                            @Override
                            public void onError(String error) {
                                runOnUiThread(() -> {
                                    Toast.makeText(StatisticsActivity.this, "Lỗi tải danh sách điểm: " + error, Toast.LENGTH_SHORT).show();
                                });
                            }
                        });
                    }
                    
                    @Override
                    public void onError(String error) {
                        runOnUiThread(() -> {
                            Toast.makeText(StatisticsActivity.this, "Lỗi tải danh sách môn học: " + error, Toast.LENGTH_SHORT).show();
                        });
                    }
                });
            }
            
            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(StatisticsActivity.this, "Lỗi tải danh sách học sinh: " + error, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
    
    /**
     * Lấy danh sách điểm theo môn học
     */
    private List<Score> getScoresBySubject(List<Score> scores, int subjectId) {
        List<Score> result = new ArrayList<>();
        for (Score score : scores) {
            if (score.getSubjectId() == subjectId) {
                result.add(score);
            }
        }
        return result;
    }
    
    /**
     * Lấy danh sách điểm theo học sinh
     */
    private List<Score> getScoresByStudent(List<Score> scores, int studentId) {
        List<Score> result = new ArrayList<>();
        for (Score score : scores) {
            if (score.getStudentId() == studentId) {
                result.add(score);
            }
        }
        return result;
    }
}