package com.example.qld.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qld.R;
import com.example.qld.database.mysql.MySQLManager;
import com.example.qld.models.Score;
import com.example.qld.models.Student;
import com.example.qld.utils.SessionManager;
import com.google.android.material.card.MaterialCardView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Activity để học sinh xem điểm của mình
 * Hiển thị danh sách điểm và điểm trung bình
 */
public class ViewScoresActivity extends AppCompatActivity {
    private RecyclerView rvScoreList;
    private TextView tvAverageScore;
    private MaterialCardView cardAverage;
    private EditText etSearch;
    private MySQLManager mysqlManager;
    private SessionManager sessionManager;
    private ScoreAdapter scoreAdapter;
    private List<Score> scoreList;
    private List<Score> filteredScoreList;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_scores);
        
        // Initialize views
        rvScoreList = findViewById(R.id.rv_score_list);
        tvAverageScore = findViewById(R.id.tv_average_score);
        cardAverage = findViewById(R.id.card_average);
        etSearch = findViewById(R.id.et_search);
        
        // Initialize managers
        mysqlManager = new MySQLManager(this);
        sessionManager = new SessionManager(this);
        
        // Check if user is logged in as student
        if (!sessionManager.isLoggedIn() || sessionManager.getUserRole() != 0) {
            // Redirect to login if not logged in or not a student
            Intent intent = new Intent(ViewScoresActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        
        // Set up RecyclerView
        rvScoreList.setLayoutManager(new LinearLayoutManager(this));
        
        // Set up navigation
        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        
        // Load scores
        loadScores();
        
        // Set up search functionality
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterScores(s.toString());
            }
            
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    
    /**
     * Tải danh sách điểm của học sinh từ database và hiển thị lên RecyclerView
     * Đồng thời tính toán và hiển thị điểm trung bình
     */
    private void loadScores() {
        mysqlManager.getStudentByUserId(sessionManager.getUserId(), new MySQLManager.StudentCallback() {
            @Override
            public void onSuccess(Student student) {
                if (student != null) {
                    mysqlManager.getScoresByStudentId(student.getId(), new MySQLManager.ScoresCallback() {
                        @Override
                        public void onSuccess(List<Score> scores) {
                            runOnUiThread(() -> {
                                scoreList = scores;
                                filteredScoreList = new ArrayList<>(scoreList);
                                
                                scoreAdapter = new ScoreAdapter(ViewScoresActivity.this, filteredScoreList, mysqlManager);
                                rvScoreList.setAdapter(scoreAdapter);
                                
                                // Calculate and display average score
                                calculateAndDisplayAverage(student.getId());
                            });
                        }
                        
                        @Override
                        public void onError(String error) {
                            runOnUiThread(() -> {
                                Toast.makeText(ViewScoresActivity.this, "Lỗi tải danh sách điểm: " + error, Toast.LENGTH_SHORT).show();
                            });
                        }
                    });
                } else {
                    runOnUiThread(() -> {
                        Toast.makeText(ViewScoresActivity.this, "Không tìm thấy thông tin học sinh", Toast.LENGTH_SHORT).show();
                    });
                }
            }
            
            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(ViewScoresActivity.this, "Lỗi tải thông tin học sinh: " + error, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
    
    /**
     * Tính toán và hiển thị điểm trung bình
     * @param studentId ID của học sinh
     */
    private void calculateAndDisplayAverage(int studentId) {
        mysqlManager.calculateAverageScore(studentId, new MySQLManager.AverageScoreCallback() {
            @Override
            public void onSuccess(double average) {
                runOnUiThread(() -> {
                    DecimalFormat df = new DecimalFormat("#.##");
                    tvAverageScore.setText(df.format(average));
                });
            }
            
            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(ViewScoresActivity.this, "Lỗi tính điểm trung bình: " + error, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
    
    /**
     * Lọc danh sách điểm theo từ khóa tìm kiếm
     * @param query từ khóa tìm kiếm
     */
    private void filterScores(String query) {
        if (scoreList == null) return;
        
        filteredScoreList.clear();
        
        if (query.isEmpty()) {
            filteredScoreList.addAll(scoreList);
        } else {
            query = query.toLowerCase().trim();
            for (Score score : scoreList) {
                // In a real app, you would filter by subject name or other criteria
                // For now, we'll just show all scores
                filteredScoreList.add(score);
            }
        }
        
        scoreAdapter.notifyDataSetChanged();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // Refresh the score list when returning to this activity
        loadScores();
    }
}