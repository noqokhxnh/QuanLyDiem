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
import com.example.qld.database.DatabaseManager;
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
    private DatabaseManager dbManager;
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
        dbManager = new DatabaseManager(this);
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
        try {
            dbManager.open();
            
            // Get student by user ID
            Student student = dbManager.getStudentByUserId(sessionManager.getUserId());
            
            if (student != null) {
                // Get scores for this student
                scoreList = dbManager.getScoresByStudentId(student.getId());
                filteredScoreList = new ArrayList<>(scoreList);
                
                scoreAdapter = new ScoreAdapter(this, filteredScoreList, dbManager);
                rvScoreList.setAdapter(scoreAdapter);
                
                // Calculate and display average score
                double average = dbManager.calculateAverageScore(student.getId());
                DecimalFormat df = new DecimalFormat("#.##");
                tvAverageScore.setText(df.format(average));
            } else {
                Toast.makeText(this, "Không tìm thấy thông tin học sinh", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi tải danh sách điểm: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            dbManager.close();
        }
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