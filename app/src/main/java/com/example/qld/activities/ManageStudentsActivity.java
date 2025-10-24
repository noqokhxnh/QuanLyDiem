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
import com.example.qld.adapters.StudentAdapter;
import com.example.qld.database.DatabaseManager;
import com.example.qld.models.Student;
import com.example.qld.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class ManageStudentsActivity extends AppCompatActivity {
    private SessionManager sessionManager;
    private DatabaseManager dbManager;
    private RecyclerView rvStudentList;
    private StudentAdapter studentAdapter;
    private Button btnAddStudent, btnBack;
    private TextView tvTitle;
    private EditText etSearch;

    private List<Student> allStudents; // Store all students for search

    /**
     * Phương thức được gọi khi activity được tạo
     * Khởi tạo giao diện, kiểm tra quyền truy cập và tải danh sách học sinh
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_students);

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
        rvStudentList = findViewById(R.id.rv_student_list);
        btnAddStudent = findViewById(R.id.btn_add_student);
        btnBack = findViewById(R.id.btn_back);
        etSearch = findViewById(R.id.et_search); // Added search edit text

        // Set up RecyclerView
        rvStudentList.setLayoutManager(new LinearLayoutManager(this));
        
        // Load students
        loadStudents();

        // Set up search functionality
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterStudents(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Set click listeners
        btnAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageStudentsActivity.this, AddStudentActivity.class);
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
     * Tải danh sách học sinh từ cơ sở dữ liệu và hiển thị trong RecyclerView
     */
    private void loadStudents() {
        try {
            dbManager.open();
            allStudents = dbManager.getAllStudentsWithUserInfo();
            
            studentAdapter = new StudentAdapter(this, allStudents);
            rvStudentList.setAdapter(studentAdapter);
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi khi tải danh sách học sinh: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            dbManager.close();
        }
    }

    /**
     * Lọc danh sách học sinh theo chuỗi tìm kiếm
     * @param query Chuỗi tìm kiếm để lọc danh sách
     */
    private void filterStudents(String query) {
        List<Student> filteredList = new ArrayList<>();
        
        if (query.isEmpty()) {
            filteredList = allStudents;
        } else {
            String lowerCaseQuery = query.toLowerCase();
            
            for (Student student : allStudents) {
                if (student.getFullName() != null && student.getFullName().toLowerCase().contains(lowerCaseQuery)) {
                    filteredList.add(student);
                } else if (student.getStudentCode() != null && student.getStudentCode().toLowerCase().contains(lowerCaseQuery)) {
                    filteredList.add(student);
                } else if (student.getClassName() != null && student.getClassName().toLowerCase().contains(lowerCaseQuery)) {
                    filteredList.add(student);
                }
            }
        }
        
        studentAdapter = new StudentAdapter(this, filteredList);
        rvStudentList.setAdapter(studentAdapter);
    }

    /**
     * Phương thức chuyển hướng người dùng đến màn hình đăng nhập
     * Được gọi khi người dùng đăng xuất hoặc không có quyền truy cập
     */
    private void redirectToLogin() {
        Intent intent = new Intent(ManageStudentsActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}