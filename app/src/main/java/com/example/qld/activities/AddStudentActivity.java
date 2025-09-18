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
import com.example.qld.utils.ProgressDialogUtil;
import com.example.qld.utils.SessionManager;

import java.util.Calendar;

/**
 * Activity để thêm mới học sinh
 * Cho phép giáo viên nhập thông tin học sinh mới
 */
public class AddStudentActivity extends AppCompatActivity {
    private EditText etFullName, etUsername, etPassword, etStudentCode, etClassName, etBirthDate;
    private Button btnSelectDate, btnAddStudent, btnCancel;
    private DatabaseManager dbManager;
    private SessionManager sessionManager;
    private ProgressDialogUtil progressDialogUtil;
    private int selectedYear, selectedMonth, selectedDay;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);
        
        // Initialize views
        etFullName = findViewById(R.id.et_full_name);
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        etStudentCode = findViewById(R.id.et_student_code);
        etClassName = findViewById(R.id.et_class_name);
        etBirthDate = findViewById(R.id.et_birth_date);
        btnSelectDate = findViewById(R.id.btn_select_date);
        btnAddStudent = findViewById(R.id.btn_add_student);
        btnCancel = findViewById(R.id.btn_cancel);
        
        // Initialize managers
        dbManager = new DatabaseManager(this);
        sessionManager = new SessionManager(this);
        progressDialogUtil = new ProgressDialogUtil();
        
        // Check if user is logged in as teacher
        if (!sessionManager.isLoggedIn() || sessionManager.getUserRole() != 1) {
            // Redirect to login if not logged in or not a teacher
            Intent intent = new Intent(AddStudentActivity.this, LoginActivity.class);
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
        
        btnAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addStudent();
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
     * Thêm học sinh mới vào database
     */
    private void addStudent() {
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
        
        // Show progress dialog
        progressDialogUtil.showProgressDialog(this, "Đang thêm học sinh...");
        
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    dbManager.open();
                    
                    // Check if username already exists
                    // This is a simplified check - in a real app you'd need a more robust solution
                    // For now, we'll just try to add the user
                    
                    // Add user first
                    User user = new User(username, password, 0, fullName); // role 0 = student
                    long userId = dbManager.addUser(user);
                    
                    if (userId != -1) {
                        // Add student record
                        Student student = new Student((int) userId, studentCode, className, birthDate);
                        long studentId = dbManager.addStudent(student);
                        
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialogUtil.hideProgressDialog();
                                if (studentId != -1) {
                                    Toast.makeText(AddStudentActivity.this, "Thêm học sinh thành công", Toast.LENGTH_SHORT).show();
                                    setResult(RESULT_OK);
                                    finish();
                                } else {
                                    // Rollback user creation if student creation fails
                                    // In a real app, you'd use transactions
                                    Toast.makeText(AddStudentActivity.this, "Lỗi khi thêm thông tin học sinh", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialogUtil.hideProgressDialog();
                                Toast.makeText(AddStudentActivity.this, "Tên đăng nhập đã tồn tại", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialogUtil.hideProgressDialog();
                            Toast.makeText(AddStudentActivity.this, "Lỗi thêm học sinh: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } finally {
                    dbManager.close();
                }
            }
        }).start();
    }
}