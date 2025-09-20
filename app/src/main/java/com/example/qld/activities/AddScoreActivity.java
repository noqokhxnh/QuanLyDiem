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
import com.example.qld.database.mysql.MySQLManager;
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
    private MySQLManager mysqlManager;
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
        mysqlManager = new MySQLManager(this);
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
        mysqlManager.getAllStudents(new MySQLManager.StudentsCallback() {
            @Override
            public void onSuccess(List<Student> students) {
                runOnUiThread(() -> {
                    studentList = students;
                    
                    // Create adapter for students spinner
                    String[] studentNames = new String[studentList.size()];
                    for (int i = 0; i < studentList.size(); i++) {
                        Student student = studentList.get(i);
                        // Store a placeholder, we'll update with real names later
                        studentNames[i] = "Loading...";
                    }
                    
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(AddScoreActivity.this, 
                        android.R.layout.simple_spinner_item, studentNames);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spStudents.setAdapter(adapter);
                    
                    // Update with real names
                    for (int i = 0; i < studentList.size(); i++) {
                        final int index = i;
                        Student student = studentList.get(i);
                        mysqlManager.getUserById(student.getUserId(), new MySQLManager.UserCallback() {
                            @Override
                            public void onSuccess(com.example.qld.models.User user) {
                                runOnUiThread(() -> {
                                    studentNames[index] = user.getFullName();
                                    adapter.notifyDataSetChanged();
                                });
                            }
                            
                            @Override
                            public void onError(String error) {
                                runOnUiThread(() -> {
                                    studentNames[index] = "Unknown Student";
                                    adapter.notifyDataSetChanged();
                                });
                            }
                        });
                    }
                });
            }
            
            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(AddScoreActivity.this, error, Toast.LENGTH_LONG).show();
                });
            }
        });
    }
    
    /**
     * Tải danh sách môn học từ database
     */
    private void loadSubjects() {
        mysqlManager.getAllSubjects(new MySQLManager.SubjectsCallback() {
            @Override
            public void onSuccess(List<Subject> subjects) {
                runOnUiThread(() -> {
                    subjectList = subjects;
                    
                    // Create adapter for subjects spinner
                    String[] subjectNames = new String[subjectList.size()];
                    for (int i = 0; i < subjectList.size(); i++) {
                        subjectNames[i] = subjectList.get(i).getSubjectName();
                    }
                    
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(AddScoreActivity.this, 
                        android.R.layout.simple_spinner_item, subjectNames);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spSubjects.setAdapter(adapter);
                });
            }
            
            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(AddScoreActivity.this, error, Toast.LENGTH_LONG).show();
                });
            }
        });
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
            
            mysqlManager.addScore(score, new MySQLManager.ScoreCallback() {
                @Override
                public void onSuccess(Score score) {
                    runOnUiThread(() -> {
                        progressDialogUtil.hideProgressDialog();
                        // Send notification to student
                        sendScoreNotification(selectedStudent.getUserId(), selectedSubject.getSubjectName(), scoreValue);
                        
                        Toast.makeText(AddScoreActivity.this, "Thêm điểm thành công", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    });
                }
                
                @Override
                public void onError(String error) {
                    runOnUiThread(() -> {
                        progressDialogUtil.hideProgressDialog();
                        Toast.makeText(AddScoreActivity.this, error, Toast.LENGTH_LONG).show();
                    });
                }
            });
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
        mysqlManager.getUserById(studentUserId, new MySQLManager.UserCallback() {
            @Override
            public void onSuccess(com.example.qld.models.User studentUser) {
                runOnUiThread(() -> {
                    String title = "Điểm mới đã được nhập";
                    String message = "Bạn có điểm mới môn " + subjectName + ": " + scoreValue;
                    NotificationUtil.showScoreNotification(AddScoreActivity.this, title, message);
                });
            }
            
            @Override
            public void onError(String error) {
                // Handle error silently
            }
        });
    }
}