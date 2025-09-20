package com.example.qld.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.qld.R;
import com.example.qld.database.mysql.MySQLManager;
import com.example.qld.models.Student;
import com.example.qld.utils.SessionManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity để quản lý danh sách học sinh
 * Cho phép xem, thêm, sửa, xóa học sinh
 */
public class ManageStudentsActivity extends AppCompatActivity {
    private RecyclerView rvStudentList;
    private TextView tvEmptyList;
    private FloatingActionButton fabAddStudent;
    private EditText etSearchStudent;
    private MySQLManager mysqlManager;
    private SessionManager sessionManager;
    private StudentAdapter studentAdapter;
    private List<Student> studentList;
    private List<Student> filteredStudentList;
    private SwipeRefreshLayout swipeRefresh;
    
    private static final int REGISTER_STUDENT_REQUEST = 1;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_students);
        
        // Initialize views
        rvStudentList = findViewById(R.id.rv_student_list);
        tvEmptyList = findViewById(R.id.tv_empty_list);
        fabAddStudent = findViewById(R.id.fab_add_student);
        etSearchStudent = findViewById(R.id.et_search_student);
        swipeRefresh = findViewById(R.id.swipe_refresh);
        
        // Initialize managers
        mysqlManager = new MySQLManager(this);
        sessionManager = new SessionManager(this);
        
        // Check if user is logged in as teacher
        if (!sessionManager.isLoggedIn() || sessionManager.getUserRole() != 1) {
            // Redirect to login if not logged in or not a teacher
            Intent intent = new Intent(ManageStudentsActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        
        // Set up RecyclerView
        rvStudentList.setLayoutManager(new LinearLayoutManager(this));
        
        // Load students
        loadStudents();
        
        // Set up swipe refresh
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadStudents();
            }
        });
        
        // Set up search functionality
        etSearchStudent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterStudents(s.toString());
            }
            
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        
        // Set click listeners
        fabAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open RegisterStudentActivity
                Intent intent = new Intent(ManageStudentsActivity.this, RegisterStudentActivity.class);
                startActivityForResult(intent, REGISTER_STUDENT_REQUEST);
            }
        });
        
        // Set up navigation
        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    
    /**
     * Tải danh sách học sinh từ database và hiển thị lên RecyclerView
     */
    private void loadStudents() {
        // Show loading indicator
        swipeRefresh.setRefreshing(true);
        
        mysqlManager.getAllStudents(new MySQLManager.StudentsCallback() {
            @Override
            public void onSuccess(List<Student> students) {
                runOnUiThread(() -> {
                    studentList = students;
                    filteredStudentList = new ArrayList<>(studentList);
                    updateStudentList();
                    swipeRefresh.setRefreshing(false);
                });
            }
            
            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(ManageStudentsActivity.this, "Lỗi tải danh sách học sinh: " + error, Toast.LENGTH_SHORT).show();
                    swipeRefresh.setRefreshing(false);
                });
            }
        });
    }
    
    /**
     * Lọc danh sách học sinh theo từ khóa tìm kiếm
     * @param query từ khóa tìm kiếm
     */
    private void filterStudents(String query) {
        if (studentList == null) return;
        
        filteredStudentList.clear();
        
        if (query.isEmpty()) {
            filteredStudentList.addAll(studentList);
        } else {
            query = query.toLowerCase().trim();
            for (Student student : studentList) {
                // Get student's full name
                mysqlManager.getStudentById(student.getId(), new MySQLManager.StudentCallback() {
                    @Override
                    public void onSuccess(Student student) {
                        // This would need to be handled differently since we're making async calls
                        // For now, we'll just filter by student code
                        String studentCode = student.getStudentCode().toLowerCase();
                        if (studentCode.contains(query)) {
                            filteredStudentList.add(student);
                        }
                    }
                    
                    @Override
                    public void onError(String error) {
                        // Handle error
                    }
                });
            }
        }
        
        updateStudentList();
    }
    
    /**
     * Cập nhật danh sách học sinh hiển thị trên RecyclerView
     */
    private void updateStudentList() {
        if (filteredStudentList.isEmpty()) {
            tvEmptyList.setVisibility(View.VISIBLE);
            rvStudentList.setVisibility(View.GONE);
        } else {
            tvEmptyList.setVisibility(View.GONE);
            rvStudentList.setVisibility(View.VISIBLE);
            
            studentAdapter = new StudentAdapter(this, filteredStudentList, mysqlManager);
            rvStudentList.setAdapter(studentAdapter);
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // Refresh the student list when returning to this activity
        loadStudents();
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == REGISTER_STUDENT_REQUEST) {
            if (resultCode == RESULT_OK) {
                // Refresh the student list after registering a new student
                loadStudents();
                // Clear search text
                etSearchStudent.setText("");
            }
        }
    }
}