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
import com.example.qld.adapters.StudentAdapter;
import com.example.qld.database.DatabaseManager;
import com.example.qld.models.Student;
import com.example.qld.utils.SessionManager;

import java.util.List;

public class ManageStudentsActivity extends AppCompatActivity {
    private SessionManager sessionManager;
    private DatabaseManager dbManager;
    private RecyclerView rvStudentList;
    private StudentAdapter studentAdapter;
    private Button btnAddStudent, btnBack;
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_students);

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
        rvStudentList = findViewById(R.id.rv_student_list);
        btnAddStudent = findViewById(R.id.btn_add_student);
        btnBack = findViewById(R.id.btn_back);

        // Set up RecyclerView
        rvStudentList.setLayoutManager(new LinearLayoutManager(this));
        
        // Load students
        loadStudents();

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

    private void loadStudents() {
        try {
            dbManager.open();
            List<Student> students = dbManager.getAllStudents();
            
            studentAdapter = new StudentAdapter(this, students);
            rvStudentList.setAdapter(studentAdapter);
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi khi tải danh sách học sinh: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            dbManager.close();
        }
    }

    private void redirectToLogin() {
        Intent intent = new Intent(ManageStudentsActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}