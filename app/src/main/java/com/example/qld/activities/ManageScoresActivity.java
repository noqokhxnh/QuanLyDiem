package com.example.qld.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.qld.R;
import com.example.qld.database.DatabaseManager;
import com.example.qld.models.Score;
import com.example.qld.models.Subject;
import com.example.qld.utils.SessionManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Activity để quản lý điểm số của học sinh
 * Cho phép giáo viên nhập điểm, xem, sửa, xóa điểm
 */
public class ManageScoresActivity extends AppCompatActivity {
    private RecyclerView rvScoreList;
    private TextView tvEmptyList;
    private FloatingActionButton fabAddScore;
    private EditText etSearchScore;
    private Button btnFilterSubject, btnSort;
    private DatabaseManager dbManager;
    private SessionManager sessionManager;
    private ScoreAdapter scoreAdapter;
    private List<Score> scoreList;
    private List<Score> filteredScoreList;
    private SwipeRefreshLayout swipeRefresh;
    
    // Filter and sort variables
    private int selectedSubjectId = -1; // -1 means no filter
    private int sortOption = 0; // 0 = default, 1 = alphabetical, 2 = by score
    
    private static final int ADD_SCORE_REQUEST = 1;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_scores);
        
        // Initialize views
        rvScoreList = findViewById(R.id.rv_score_list);
        tvEmptyList = findViewById(R.id.tv_empty_list);
        fabAddScore = findViewById(R.id.fab_add_score);
        etSearchScore = findViewById(R.id.et_search_score);
        btnFilterSubject = findViewById(R.id.btn_filter_subject);
        btnSort = findViewById(R.id.btn_sort);
        swipeRefresh = findViewById(R.id.swipe_refresh);
        
        // Initialize managers
        dbManager = new DatabaseManager(this);
        sessionManager = new SessionManager(this);
        
        // Check if user is logged in as teacher
        if (!sessionManager.isLoggedIn() || sessionManager.getUserRole() != 1) {
            // Redirect to login if not logged in or not a teacher
            Intent intent = new Intent(ManageScoresActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        
        // Set up RecyclerView
        rvScoreList.setLayoutManager(new LinearLayoutManager(this));
        
        // Load scores
        loadScores();
        
        // Set up swipe refresh
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadScores();
            }
        });
        
        // Set up search functionality
        etSearchScore.addTextChangedListener(new TextWatcher() {
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
        
        // Set up filter button
        btnFilterSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSubjectFilterDialog();
            }
        });
        
        // Set up sort button
        btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSortDialog();
            }
        });
        
        // Set up navigation
        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        
        // Set click listeners
        fabAddScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open AddScoreActivity
                Intent intent = new Intent(ManageScoresActivity.this, AddScoreActivity.class);
                startActivityForResult(intent, ADD_SCORE_REQUEST);
            }
        });
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    
    /**
     * Tải danh sách điểm từ database và hiển thị lên RecyclerView
     */
    private void loadScores() {
        try {
            dbManager.open();
            scoreList = dbManager.getAllScores();
            filteredScoreList = new ArrayList<>(scoreList);
            
            updateScoreList();
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi tải danh sách điểm: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            dbManager.close();
            if (swipeRefresh.isRefreshing()) {
                swipeRefresh.setRefreshing(false);
            }
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
                // Get student and subject names
                String studentName = "";
                String subjectName = "";
                
                try {
                    dbManager.open();
                    
                    // Get student name
                    com.example.qld.models.Student student = dbManager.getStudentById(score.getStudentId());
                    if (student != null) {
                        com.example.qld.models.User user = dbManager.getUserById(student.getUserId());
                        if (user != null) {
                            studentName = user.getFullName().toLowerCase();
                        }
                    }
                    
                    // Get subject name
                    com.example.qld.models.Subject subject = dbManager.getSubjectById(score.getSubjectId());
                    if (subject != null) {
                        subjectName = subject.getSubjectName().toLowerCase();
                    }
                } catch (Exception e) {
                    // Handle exception
                } finally {
                    dbManager.close();
                }
                
                // Check if query matches student name or subject name
                if (studentName.contains(query) || subjectName.contains(query)) {
                    filteredScoreList.add(score);
                }
            }
        }
        
        // Apply subject filter if selected
        if (selectedSubjectId != -1) {
            filterBySubject(selectedSubjectId);
        }
        
        // Apply sorting
        applySorting();
        
        updateScoreList();
    }
    
    /**
     * Lọc danh sách điểm theo môn học
     * @param subjectId ID của môn học
     */
    private void filterBySubject(int subjectId) {
        if (filteredScoreList == null) return;
        
        List<Score> tempList = new ArrayList<>(filteredScoreList);
        filteredScoreList.clear();
        
        for (Score score : tempList) {
            if (score.getSubjectId() == subjectId) {
                filteredScoreList.add(score);
            }
        }
    }
    
    /**
     * Áp dụng sắp xếp danh sách điểm
     */
    private void applySorting() {
        if (filteredScoreList == null) return;
        
        switch (sortOption) {
            case 1: // Alphabetical
                Collections.sort(filteredScoreList, new Comparator<Score>() {
                    @Override
                    public int compare(Score s1, Score s2) {
                        String studentName1 = getStudentName(s1.getStudentId());
                        String studentName2 = getStudentName(s2.getStudentId());
                        return studentName1.compareToIgnoreCase(studentName2);
                    }
                });
                break;
            case 2: // By score
                Collections.sort(filteredScoreList, new Comparator<Score>() {
                    @Override
                    public int compare(Score s1, Score s2) {
                        return Double.compare(s2.getScore(), s1.getScore()); // Descending order
                    }
                });
                break;
            default:
                // No sorting
                break;
        }
    }
    
    /**
     * Lấy tên học sinh từ ID
     * @param studentId ID của học sinh
     * @return Tên học sinh
     */
    private String getStudentName(int studentId) {
        try {
            dbManager.open();
            com.example.qld.models.Student student = dbManager.getStudentById(studentId);
            if (student != null) {
                com.example.qld.models.User user = dbManager.getUserById(student.getUserId());
                if (user != null) {
                    return user.getFullName();
                }
            }
        } catch (Exception e) {
            // Handle exception
        } finally {
            dbManager.close();
        }
        return "";
    }
    
    /**
     * Hiển thị dialog lọc theo môn học
     */
    private void showSubjectFilterDialog() {
        try {
            dbManager.open();
            List<Subject> subjects = dbManager.getAllSubjects();
            
            String[] subjectNames = new String[subjects.size() + 1];
            int[] subjectIds = new int[subjects.size() + 1];
            
            subjectNames[0] = "Tất cả môn học";
            subjectIds[0] = -1;
            
            for (int i = 0; i < subjects.size(); i++) {
                subjectNames[i + 1] = subjects.get(i).getSubjectName();
                subjectIds[i + 1] = subjects.get(i).getId();
            }
            
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Lọc theo môn học");
            builder.setItems(subjectNames, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    selectedSubjectId = subjectIds[which];
                    btnFilterSubject.setText(which == 0 ? "Lọc theo môn" : subjectNames[which]);
                    
                    // Re-apply filters
                    filterScores(etSearchScore.getText().toString());
                }
            });
            builder.show();
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi tải danh sách môn học: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            dbManager.close();
        }
    }
    
    /**
     * Hiển thị dialog sắp xếp
     */
    private void showSortDialog() {
        String[] sortOptions = {"Mặc định", "Theo tên ", "Theo điểm "};
        
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sắp xếp danh sách");
        builder.setItems(sortOptions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sortOption = which;
                btnSort.setText(sortOptions[which]);
                
                // Re-apply filters and sorting
                filterScores(etSearchScore.getText().toString());
            }
        });
        builder.show();
    }
    
    /**
     * Cập nhật danh sách điểm hiển thị trên RecyclerView
     */
    private void updateScoreList() {
        if (filteredScoreList.isEmpty()) {
            tvEmptyList.setVisibility(View.VISIBLE);
            rvScoreList.setVisibility(View.GONE);
        } else {
            tvEmptyList.setVisibility(View.GONE);
            rvScoreList.setVisibility(View.VISIBLE);
            
            scoreAdapter = new ScoreAdapter(this, filteredScoreList, dbManager);
            rvScoreList.setAdapter(scoreAdapter);
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // Refresh the score list when returning to this activity
        loadScores();
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == ADD_SCORE_REQUEST) {
            if (resultCode == RESULT_OK) {
                // Refresh the score list after adding a new score
                loadScores();
                // Clear search text
                etSearchScore.setText("");
                // Reset filters
                selectedSubjectId = -1;
                btnFilterSubject.setText("Lọc theo môn");
            }
        }
    }
}