package com.example.qld.activities;

import android.app.DatePickerDialog;
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

import java.util.Calendar;

/**
 * Activity để đăng ký học sinh mới
 * Chỉ giáo viên mới có thể đăng ký học sinh mới
 */
public class RegisterStudentActivity extends AppCompatActivity {
    private EditText etFullName, etUsername, etPassword, etStudentCode, etClassName, etBirthDate;
    private Button btnSelectDate, btnRegisterStudent, btnCancel;
    private DatabaseManager dbManager;
    private SessionManager sessionManager;
    private int selectedYear, selectedMonth, selectedDay;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_student);
        
        // Initialize views
        etFullName = findViewById(R.id.et_full_name);
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        etStudentCode = findViewById(R.id.et_student_code);
        etClassName = findViewById(R.id.et_class_name);
        etBirthDate = findViewById(R.id.et_birth_date);
        btnSelectDate = findViewById(R.id.btn_select_date);
        btnRegisterStudent = findViewById(R.id.btn_register_student);
        btnCancel = findViewById(R.id.btn_cancel);
        
        // Initialize managers
        dbManager = new DatabaseManager(this);
        sessionManager = new SessionManager(this);
        
        // Check if user is logged in as teacher
        if (!sessionManager.isLoggedIn() || sessionManager.getUserRole() != 1) {
            // Redirect to login if not logged in or not a teacher
            Intent intent = new Intent(RegisterStudentActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        
        // Set current date as default
        Calendar calendar = Calendar.getInstance();
        selectedYear = calendar.get(Calendar.YEAR);
        selectedMonth = calendar.get(Calendar.MONTH);
        selectedDay = calendar.get(Calendar.DAY_OF_MONTH);
        updateDateDisplay();
        
        // Set click listeners
        btnSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
        
        btnRegisterStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerStudent();
            }
        });
        
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    
    /**
     * Hiển thị dialog chọn ngày sinh
     */
    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    selectedYear = year;
                    selectedMonth = month;
                    selectedDay = dayOfMonth;
                    updateDateDisplay();
                },
                selectedYear,
                selectedMonth,
                selectedDay
        );
        datePickerDialog.show();
    }
    
    /**
     * Cập nhật hiển thị ngày sinh đã chọn
     */
    private void updateDateDisplay() {
        String date = selectedYear + "-" + String.format("%02d", (selectedMonth + 1)) + "-" + String.format("%02d", selectedDay);
        etBirthDate.setText(date);
    }
    
    /**
     * Đăng ký học sinh mới vào hệ thống
     */
    private void registerStudent() {
        String fullName = etFullName.getText().toString().trim();
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String studentCode = etStudentCode.getText().toString().trim();
        String className = etClassName.getText().toString().trim();
        String birthDate = etBirthDate.getText().toString().trim();
        
        // Validate input
        if (fullName.isEmpty() || username.isEmpty() || password.isEmpty() || 
            studentCode.isEmpty() || className.isEmpty() || birthDate.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Check password length
        if (password.length() < 6) {
            Toast.makeText(this, "Mật khẩu phải có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show();
            return;
        }
        
        try {
            dbManager.open();
            
            // Check if username already exists
            User existingUser = dbManager.getUserByUsername(username);
            if (existingUser != null) {
                Toast.makeText(this, "Tên đăng nhập đã tồn tại", Toast.LENGTH_SHORT).show();
                return;
            }
            
            // Add user first (student role = 0)
            User user = new User(username, password, 0, fullName); // role 0 = student
            long userId = dbManager.addUser(user);
            
            if (userId != -1) {
                // Add student record
                Student student = new Student((int) userId, studentCode, className, birthDate);
                long studentId = dbManager.addStudent(student);
                
                if (studentId != -1) {
                    Toast.makeText(this, "Đăng ký học sinh thành công", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    // Rollback user creation if student creation fails
                    dbManager.deleteUser((int) userId);
                    Toast.makeText(this, "Lỗi khi tạo thông tin học sinh", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Lỗi khi tạo tài khoản người dùng", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi đăng ký học sinh: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            dbManager.close();
        }
    }
}