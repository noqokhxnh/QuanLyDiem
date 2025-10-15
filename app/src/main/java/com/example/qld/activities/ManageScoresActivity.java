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
import com.example.qld.utils.SessionManager;

import java.util.List;

public class ManageScoresActivity extends AppCompatActivity {
    private SessionManager sessionManager;
    private DatabaseManager dbManager;
    private RecyclerView rvScoreList;
    private ScoreAdapter scoreAdapter;
    private Button btnAddScore, btnBack;
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_scores);

        // Initialize session and database managers
        sessionManager = new SessionManager(this);
        dbManager = new DatabaseManager(this);

        // Check if user is logged in and is a teacher
        if (!sessionManager.isLoggedIn() || sessionManager.getUserRole() != 1) {
            redirectToLogin();
            return;
        }

        // Initialize views
        tvTitle = findViewById(R.id.tv_title);
        rvScoreList = findViewById(R.id.rv_score_list);
        btnAddScore = findViewById(R.id.btn_add_score);
        btnBack = findViewById(R.id.btn_back);

        // Set up RecyclerView
        rvScoreList.setLayoutManager(new LinearLayoutManager(this));

        // Load scores
        loadScores();

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

    private void loadScores() {
        try {
            dbManager.open();
            List<Score> scores = dbManager.getAllScores();
            
            scoreAdapter = new ScoreAdapter(this, scores);
            rvScoreList.setAdapter(scoreAdapter);
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi khi tải danh sách điểm: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            dbManager.close();
        }
    }

    private void redirectToLogin() {
        Intent intent = new Intent(ManageScoresActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}