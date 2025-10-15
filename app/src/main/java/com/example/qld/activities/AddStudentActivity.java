package com.example.qld.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qld.R;
import com.example.qld.database.DatabaseManager;
import com.example.qld.models.Student;
import com.example.qld.models.User;
import com.example.qld.utils.SessionManager;

public class AddStudentActivity extends AppCompatActivity {
    private SessionManager sessionManager;
    private DatabaseManager dbManager;
    private EditText etFullName, etUsername, etPassword, etStudentCode, etClassName, etBirthDate;
    private Button btnAddStudent, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        // Initialize session and database managers
        sessionManager = new SessionManager(this);
        dbManager = new DatabaseManager(this);

        // Check if user is logged in and is a teacher
        if (!sessionManager.isLoggedIn() || sessionManager.getUserRole() != 1) {
            redirectToLogin();
            return;
        }

        // Initialize views
        etFullName = findViewById(R.id.et_full_name);
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        etStudentCode = findViewById(R.id.et_student_code);
        etClassName = findViewById(R.id.et_class_name);
        etBirthDate = findViewById(R.id.et_birth_date);
        btnAddStudent = findViewById(R.id.btn_add_student);
        btnBack = findViewById(R.id.btn_back);

        // Set click listeners
        btnAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addStudent();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void addStudent() {
        String fullName = etFullName.getText().toString().trim();
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String studentCode = etStudentCode.getText().toString().trim();
        String className = etClassName.getText().toString().trim();
        String birthDate = etBirthDate.getText().toString().trim();

        // Validate input
        if (fullName.isEmpty()) {
            etFullName.setError("Vui lòng nhập họ tên");
            etFullName.requestFocus();
            return;
        }

        if (username.isEmpty()) {
            etUsername.setError("Vui lòng nhập tên đăng nhập");
            etUsername.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            etPassword.setError("Vui lòng nhập mật khẩu");
            etPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            etPassword.setError("Mật khẩu phải có ít nhất 6 ký tự");
            etPassword.requestFocus();
            return;
        }

        if (studentCode.isEmpty()) {
            etStudentCode.setError("Vui lòng nhập mã học sinh");
            etStudentCode.requestFocus();
            return;
        }

        if (className.isEmpty()) {
            etClassName.setError("Vui lòng nhập lớp");
            etClassName.requestFocus();
            return;
        }

        try {
            dbManager.open();
            
            // First, create the user account
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setRole(0); // Student role
            user.setFullName(fullName);
            
            long userId = dbManager.createUser(user);
            
            if (userId == -1) {
                Toast.makeText(this, "Lỗi khi tạo tài khoản học sinh", Toast.LENGTH_SHORT).show();
                return;
            }
            
            // Then, create the student record
            Student student = new Student();
            student.setUserId((int) userId);
            student.setStudentCode(studentCode);
            student.setClassName(className);
            student.setBirthDate(birthDate);
            
            long result = dbManager.createStudent(student);
            
            if (result != -1) {
                Toast.makeText(this, "Thêm học sinh thành công", Toast.LENGTH_SHORT).show();
                finish(); // Close the activity
            } else {
                // Rollback: delete the user if student creation failed
                dbManager.deleteStudent((int) userId);
                Toast.makeText(this, "Lỗi khi thêm học sinh", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi hệ thống: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            dbManager.close();
        }
    }

    private void redirectToLogin() {
        Intent intent = new Intent(AddStudentActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}