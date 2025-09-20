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
import com.example.qld.database.mysql.MySQLManager;
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
    private MySQLManager mysqlManager;
    private SessionManager sessionManager;
    private ProgressDialogUtil progressDialogUtil;
    private int selectedYear, selectedMonth, selectedDay;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);
        
        // Khởi tạo các view
        etFullName = findViewById(R.id.et_full_name);
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        etStudentCode = findViewById(R.id.et_student_code);
        etClassName = findViewById(R.id.et_class_name);
        etBirthDate = findViewById(R.id.et_birth_date);
        btnSelectDate = findViewById(R.id.btn_select_date);
        btnAddStudent = findViewById(R.id.btn_add_student);
        btnCancel = findViewById(R.id.btn_cancel);
        
        // Khởi tạo các manager
        mysqlManager = new MySQLManager(this);
        sessionManager = new SessionManager(this);
        progressDialogUtil = new ProgressDialogUtil();
        
        // Kiểm tra xem người dùng đã đăng nhập chưa và có phải giáo viên không
        if (!sessionManager.isLoggedIn() || sessionManager.getUserRole() != 1) {
            // Chuyển hướng đến màn hình đăng nhập nếu chưa đăng nhập hoặc không phải giáo viên
            Intent intent = new Intent(AddStudentActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        
        // Đặt ngày hiện tại làm mặc định
        Calendar calendar = Calendar.getInstance();
        selectedYear = calendar.get(Calendar.YEAR);
        selectedMonth = calendar.get(Calendar.MONTH);
        selectedDay = calendar.get(Calendar.DAY_OF_MONTH);
        updateDateDisplay();
        
        // Thiết lập các sự kiện click
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
        
        // Kiểm tra dữ liệu đầu vào
        if (fullName.isEmpty() || username.isEmpty() || password.isEmpty() || 
            studentCode.isEmpty() || className.isEmpty() || birthDate.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Hiển thị dialog tiến trình
        progressDialogUtil.showProgressDialog(this, "Đang thêm học sinh...");
        
        // Kiểm tra xem tên đăng nhập đã tồn tại chưa
        mysqlManager.getUserByUsername(username, new MySQLManager.UserCallback() {
            @Override
            public void onSuccess(User user) {
                if (user != null) {
                    runOnUiThread(() -> {
                        progressDialogUtil.hideProgressDialog();
                        Toast.makeText(AddStudentActivity.this, "Tên đăng nhập đã tồn tại", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    // Thêm người dùng trước
                    User newUser = new User(username, password, 0, fullName); // role 0 = học sinh
                    mysqlManager.addUser(newUser, new MySQLManager.UserCallback() {
                        @Override
                        public void onSuccess(User user) {
                            // Thêm bản ghi học sinh
                            Student student = new Student(user.getId(), studentCode, className, birthDate);
                            mysqlManager.addStudent(student, new MySQLManager.StudentCallback() {
                                @Override
                                public void onSuccess(Student student) {
                                    runOnUiThread(() -> {
                                        progressDialogUtil.hideProgressDialog();
                                        Toast.makeText(AddStudentActivity.this, "Thêm học sinh thành công", Toast.LENGTH_SHORT).show();
                                        setResult(RESULT_OK);
                                        finish();
                                    });
                                }
                                
                                @Override
                                public void onError(String error) {
                                    // Hoàn tác việc tạo người dùng nếu việc tạo học sinh thất bại
                                    mysqlManager.deleteUser(user.getId(), new MySQLManager.UserCallback() {
                                        @Override
                                        public void onSuccess(User user) {
                                            // User deleted
                                        }
                                        
                                        @Override
                                        public void onError(String error) {
                                            // Error deleting user
                                        }
                                    });
                                    
                                    runOnUiThread(() -> {
                                        progressDialogUtil.hideProgressDialog();
                                        Toast.makeText(AddStudentActivity.this, "Lỗi khi thêm thông tin học sinh: " + error, Toast.LENGTH_SHORT).show();
                                    });
                                }
                            });
                        }
                        
                        @Override
                        public void onError(String error) {
                            runOnUiThread(() -> {
                                progressDialogUtil.hideProgressDialog();
                                Toast.makeText(AddStudentActivity.this, "Lỗi khi thêm người dùng: " + error, Toast.LENGTH_SHORT).show();
                            });
                        }
                    });
                }
            }
            
            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    progressDialogUtil.hideProgressDialog();
                    Toast.makeText(AddStudentActivity.this, "Lỗi kiểm tra tên đăng nhập: " + error, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}