package com.example.qld.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qld.R;
import com.example.qld.adapters.ScoreAdapter;
import com.example.qld.database.DatabaseManager;
import com.example.qld.models.Score;
import com.example.qld.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class ManageScoresActivity extends AppCompatActivity {
    private SessionManager sessionManager;
    private DatabaseManager dbManager;
    private RecyclerView rvScoreList;
    private ScoreAdapter scoreAdapter;
    private Button btnAddScore, btnBack;
    private TextView tvTitle;
    private EditText etSearch;

    private List<Score> allScores; // Store all scores for search

    /**
     * Phương thức được gọi khi activity được tạo
     * Khởi tạo giao diện, kiểm tra quyền truy cập và tải danh sách điểm
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_scores);

        // Initialize session and database managers
        sessionManager = new SessionManager(this);
        dbManager = new DatabaseManager(this);

        // Check if user is logged in and is a teacher
        if (!sessionManager.isLoggedIn() || !"TEACHER".equals(sessionManager.getUserRole())) {
            redirectToLogin();
            return;
        }

        // Initialize views
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        
        tvTitle = findViewById(R.id.tv_title);
        rvScoreList = findViewById(R.id.rv_score_list);
        btnAddScore = findViewById(R.id.btn_add_score);
        btnBack = findViewById(R.id.btn_back);
        etSearch = findViewById(R.id.et_search); // Added search edit text

        // Set up RecyclerView
        rvScoreList.setLayoutManager(new LinearLayoutManager(this));

        // Load scores
        loadScores();

        // Set up search functionality
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterScores(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Set click listeners
        btnAddScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageScoresActivity.this, AddScoreActivity.class);
                startActivity(intent);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * Tải danh sách điểm từ cơ sở dữ liệu và hiển thị trong RecyclerView
     */
    private void loadScores() {
        try {
            dbManager.open();
            allScores = dbManager.getAllScores();
            
            scoreAdapter = new ScoreAdapter(this, allScores);
            rvScoreList.setAdapter(scoreAdapter);
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi khi tải danh sách điểm: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            dbManager.close();
        }
    }

    /**
     * Lọc danh sách điểm theo chuỗi tìm kiếm
     * @param query Chuỗi tìm kiếm để lọc danh sách
     */
    private void filterScores(String query) {
        List<Score> filteredList = new ArrayList<>();
        
        if (query.isEmpty()) {
            filteredList = allScores;
        } else {
            String lowerCaseQuery = query.toLowerCase();
            
            for (Score score : allScores) {
                // In a real implementation, we would search by student name, subject, etc.
                // For now, searching by score value and type as an example
                if (String.valueOf(score.getScore()).toLowerCase().contains(lowerCaseQuery)) {
                    filteredList.add(score);
                } else if (score.getScoreType() != null && score.getScoreType().toLowerCase().contains(lowerCaseQuery)) {
                    filteredList.add(score);
                }
            }
        }
        
        scoreAdapter = new ScoreAdapter(this, filteredList);
        rvScoreList.setAdapter(scoreAdapter);
    }

    /**
     * Phương thức chuyển hướng người dùng đến màn hình đăng nhập
     * Được gọi khi người dùng đăng xuất hoặc không có quyền truy cập
     */
    private void redirectToLogin() {
        Intent intent = new Intent(ManageScoresActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}