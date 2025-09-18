package com.example.qld.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qld.R;
import com.example.qld.database.DatabaseManager;
import com.example.qld.models.Score;
import com.example.qld.models.Student;
import com.example.qld.models.Subject;
import com.example.qld.utils.Constants;
import com.example.qld.utils.NotificationUtil;
import com.example.qld.utils.ProgressDialogUtil;
import com.example.qld.utils.SessionManager;

import java.util.List;

/**
 * Activity để thêm điểm cho học sinh
 * Cho phép giáo viên nhập điểm cho học sinh
 */
public class AddScoreActivity extends AppCompatActivity {
    private Spinner spStudents, spSubjects, spScoreTypes;
    private EditText etScoreValue;
    private Button btnAddScore, btnCancel;
    private DatabaseManager dbManager;
    private SessionManager sessionManager;
    private ProgressDialogUtil progressDialogUtil;
    private List<Student> studentList;
    private List<Subject> subjectList;
    private String[] scoreTypes = {
        Constants.SCORE_TYPE_MIENG,
        Constants.SCORE_TYPE_15PHUT,
        Constants.SCORE_TYPE_1TIET,
        Constants.SCORE_TYPE_HOCKY
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_score);
        
        // Initialize views
        spStudents = findViewById(R.id.sp_students);
        spSubjects = findViewById(R.id.sp_subjects);
        spScoreTypes = findViewById(R.id.sp_score_types);
        etScoreValue = findViewById(R.id.et_score_value);
        btnAddScore = findViewById(R.id.btn_add_score);
        btnCancel = findViewById(R.id.btn_cancel);
        
        // Initialize managers
        dbManager = new DatabaseManager(this);
        sessionManager = new SessionManager(this);
        progressDialogUtil = new ProgressDialogUtil();
        
        // Check if user is logged in as teacher
        if (!sessionManager.isLoggedIn() || sessionManager.getUserRole() != 1) {
            // Redirect to login if not logged in or not a teacher
            Intent intent = new Intent(AddScoreActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        
        // Load data
        loadStudents();
        loadSubjects();
        setupScoreTypesSpinner();
        
        // Set click listeners
        btnAddScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addScore();
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
     * Tải danh sách học sinh từ database
     */
    private void loadStudents() {
        try {
            dbManager.open();
            studentList = dbManager.getAllStudents();
            
            // Create adapter for students spinner
            String[] studentNames = new String[studentList.size()];
            for (int i = 0; i < studentList.size(); i++) {
                // Get student's full name from user table
                Student student = studentList.get(i);
                com.example.qld.models.User user = dbManager.getUserById(student.getUserId());
                if (user != null) {
                    studentNames[i] = user.getFullName();
                } else {
                    studentNames[i] = "Unknown Student";
                }
            }
            
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, 
                android.R.layout.simple_spinner_item, studentNames);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spStudents.setAdapter(adapter);
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi tải danh sách học sinh: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            dbManager.close();
        }
    }
    
    /**
     * Tải danh sách môn học từ database
     */
    private void loadSubjects() {
        try {
            dbManager.open();
            subjectList = dbManager.getAllSubjects();
            
            // Create adapter for subjects spinner
            String[] subjectNames = new String[subjectList.size()];
            for (int i = 0; i < subjectList.size(); i++) {
                subjectNames[i] = subjectList.get(i).getSubjectName();
            }
            
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, 
                android.R.layout.simple_spinner_item, subjectNames);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spSubjects.setAdapter(adapter);
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi tải danh sách môn học: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            dbManager.close();
        }
    }
    
    /**
     * Thiết lập spinner loại điểm
     */
    private void setupScoreTypesSpinner() {
        String[] displayScoreTypes = {"Điểm miệng", "Điểm 15 phút", "Điểm 1 tiết", "Điểm học kỳ"};
        
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, 
            android.R.layout.simple_spinner_item, displayScoreTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spScoreTypes.setAdapter(adapter);
    }
    
    /**
     * Thêm điểm mới vào database
     */
    private void addScore() {
        // Validate input
        if (studentList.isEmpty() || subjectList.isEmpty()) {
            Toast.makeText(this, "Không có dữ liệu học sinh hoặc môn học", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (etScoreValue.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập điểm", Toast.LENGTH_SHORT).show();
            return;
        }
        
        try {
            double scoreValue = Double.parseDouble(etScoreValue.getText().toString().trim());
            
            if (scoreValue < 0 || scoreValue > 10) {
                Toast.makeText(this, "Điểm phải từ 0 đến 10", Toast.LENGTH_SHORT).show();
                return;
            }
            
            // Get selected items
            int selectedStudentIndex = spStudents.getSelectedItemPosition();
            int selectedSubjectIndex = spSubjects.getSelectedItemPosition();
            int selectedScoreTypeIndex = spScoreTypes.getSelectedItemPosition();
            
            if (selectedStudentIndex < 0 || selectedSubjectIndex < 0) {
                Toast.makeText(this, "Vui lòng chọn học sinh và môn học", Toast.LENGTH_SHORT).show();
                return;
            }
            
            Student selectedStudent = studentList.get(selectedStudentIndex);
            Subject selectedSubject = subjectList.get(selectedSubjectIndex);
            String selectedScoreType = scoreTypes[selectedScoreTypeIndex];
            
            // Create score object
            Score score = new Score(
                selectedStudent.getId(),
                selectedSubject.getId(),
                selectedScoreType,
                scoreValue,
                sessionManager.getUserId() // Teacher ID
            );
            
            // Show progress dialog
            progressDialogUtil.showProgressDialog(this, "Đang thêm điểm...");
            
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        dbManager.open();
                        long result = dbManager.addScore(score);
                        
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialogUtil.hideProgressDialog();
                                if (result != -1) {
                                    // Send notification to student
                                    sendScoreNotification(selectedStudent.getUserId(), selectedSubject.getSubjectName(), scoreValue);
                                    
                                    Toast.makeText(AddScoreActivity.this, "Thêm điểm thành công", Toast.LENGTH_SHORT).show();
                                    setResult(RESULT_OK);
                                    finish();
                                } else {
                                    Toast.makeText(AddScoreActivity.this, "Lỗi khi thêm điểm", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } catch (Exception e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialogUtil.hideProgressDialog();
                                Toast.makeText(AddScoreActivity.this, "Lỗi thêm điểm: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } finally {
                        dbManager.close();
                    }
                }
            }).start();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Điểm không hợp lệ", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi thêm điểm: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    
    /**
     * Gửi thông báo điểm mới cho học sinh
     */
    private void sendScoreNotification(int studentUserId, String subjectName, double scoreValue) {
        try {
            dbManager.open();
            com.example.qld.models.User studentUser = dbManager.getUserById(studentUserId);
            
            if (studentUser != null) {
                String title = "Điểm mới đã được nhập";
                String message = "Bạn có điểm mới môn " + subjectName + ": " + scoreValue;
                NotificationUtil.showScoreNotification(this, title, message);
            }
        } catch (Exception e) {
            // Handle exception silently
        } finally {
            dbManager.close();
        }
    }
}