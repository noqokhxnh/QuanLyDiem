package com.example.qld.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qld.R;
import com.example.qld.adapters.ScoreAdapter;
import com.example.qld.database.DatabaseManager;
import com.example.qld.models.Score;
import com.example.qld.models.Student;
import com.example.qld.utils.SessionManager;

import java.util.List;

public class ViewScoresActivity extends AppCompatActivity {
    private SessionManager sessionManager;
    private DatabaseManager dbManager;
    private RecyclerView rvScoreList;
    private ScoreAdapter scoreAdapter;
    private Button btnBack;
    private TextView tvTitle, tvStudentInfo, tvAverageScore;

    private int studentId;

    /**
     * Phương thức được gọi khi activity được tạo
     * Khởi tạo giao diện, kiểm tra quyền truy cập và tải dữ liệu điểm cho học sinh
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_scores);

        // Initialize session and database managers
        sessionManager = new SessionManager(this);
        dbManager = new DatabaseManager(this);

        // Check if user is logged in
        if (!sessionManager.isLoggedIn()) {
            redirectToLogin();
            return;
        }

        // Get student ID from intent
        studentId = getIntent().getIntExtra("STUDENT_ID", sessionManager.getUserId());

        // Initialize views
        tvTitle = findViewById(R.id.tv_title);
        tvStudentInfo = findViewById(R.id.tv_student_info);
        tvAverageScore = findViewById(R.id.tv_average_score);
        rvScoreList = findViewById(R.id.rv_score_list);
        btnBack = findViewById(R.id.btn_back);

        // Set up RecyclerView
        rvScoreList.setLayoutManager(new LinearLayoutManager(this));

        // Load scores
        loadScores();

        // Load student info
        loadStudentInfo();

        // Calculate and display average score
        calculateAndDisplayAverage();

        // Set click listeners
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * Tải danh sách điểm của học sinh từ cơ sở dữ liệu và hiển thị trong RecyclerView
     */
    private void loadScores() {
        try {
            dbManager.open();
            List<Score> scores = dbManager.getScoresByStudentId(studentId);
            
            scoreAdapter = new ScoreAdapter(this, scores);
            rvScoreList.setAdapter(scoreAdapter);
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi khi tải danh sách điểm: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            dbManager.close();
        }
    }

    /**
     * Tải thông tin học sinh từ cơ sở dữ liệu và hiển thị lên giao diện
     */
    private void loadStudentInfo() {
        try {
            dbManager.open();
            Student student = dbManager.getStudentById(studentId);
            if (student != null) {
                // If we don't have a direct way to get student name, we can get it via user info
                // For now, just showing the student ID
                tvStudentInfo.setText("Mã học sinh: " + student.getStudentCode() + 
                                    " | Lớp: " + student.getClassName());
            } else {
                tvStudentInfo.setText("Thông tin học sinh");
            }
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi khi tải thông tin học sinh: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            dbManager.close();
        }
    }

    /**
     * Tính toán và hiển thị điểm trung bình của học sinh
     */
    private void calculateAndDisplayAverage() {
        try {
            dbManager.open();
            double average = dbManager.calculateOverallAverageScore(studentId);
            tvAverageScore.setText(String.format("Điểm trung bình: %.2f", average));
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi khi tính điểm trung bình: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            dbManager.close();
        }
    }

    /**
     * Phương thức chuyển hướng người dùng đến màn hình đăng nhập
     * Được gọi khi người dùng đăng xuất hoặc không có quyền truy cập
     */
    private void redirectToLogin() {
        Intent intent = new Intent(ViewScoresActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}