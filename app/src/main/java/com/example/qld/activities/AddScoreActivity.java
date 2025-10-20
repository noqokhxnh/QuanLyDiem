package com.example.qld.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qld.R;
import com.example.qld.database.DatabaseManager;
import com.example.qld.models.Score;
import com.example.qld.models.Student;
import com.example.qld.models.Subject;
import com.example.qld.utils.Constants;
import com.example.qld.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class AddScoreActivity extends AppCompatActivity {
    private SessionManager sessionManager;
    private DatabaseManager dbManager;
    private Spinner spStudent, spSubject, spScoreType;
    private EditText etScore;
    private Button btnSubmit, btnCancel;
    private TextView tvTitle;

    private List<Student> students;
    private List<Subject> subjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_score);

        // Initialize session and database managers
        sessionManager = new SessionManager(this);
        dbManager = new DatabaseManager(this);

        // Check if user is logged in and is a teacher
        if (!sessionManager.isLoggedIn() || !"TEACHER".equals(sessionManager.getUserRole())) {
            redirectToLogin();
            return;
        }

        // Initialize views
        spStudent = findViewById(R.id.sp_student);
        spSubject = findViewById(R.id.sp_subject);
        spScoreType = findViewById(R.id.sp_score_type);
        etScore = findViewById(R.id.et_score);
        btnSubmit = findViewById(R.id.btn_submit);
        btnCancel = findViewById(R.id.btn_cancel);
        tvTitle = findViewById(R.id.tv_title);

        // Load data
        loadData();

        // Set click listeners
        btnSubmit.setOnClickListener(new View.OnClickListener() {
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

    private void loadData() {
        try {
            dbManager.open();
            
            // Load students
            students = dbManager.getAllStudents();
            List<String> studentNames = new ArrayList<>();
            for (Student student : students) {
                studentNames.add(student.getStudentCode() + " - " + student.getClassName());
            }
            ArrayAdapter<String> studentAdapter = new ArrayAdapter<>(this, 
                android.R.layout.simple_spinner_item, studentNames);
            studentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spStudent.setAdapter(studentAdapter);
            
            // Load subjects
            subjects = dbManager.getAllSubjects();
            List<String> subjectNames = new ArrayList<>();
            for (Subject subject : subjects) {
                subjectNames.add(subject.getSubjectCode() + " - " + subject.getSubjectName());
            }
            ArrayAdapter<String> subjectAdapter = new ArrayAdapter<>(this, 
                android.R.layout.simple_spinner_item, subjectNames);
            subjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spSubject.setAdapter(subjectAdapter);
            
            // Set up score type spinner
            ArrayAdapter<CharSequence> scoreTypeAdapter = ArrayAdapter.createFromResource(this,
                R.array.score_types, android.R.layout.simple_spinner_item);
            scoreTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spScoreType.setAdapter(scoreTypeAdapter);
            
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi khi tải dữ liệu: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            dbManager.close();
        }
    }

    private void addScore() {
        // Validate input
        String scoreStr = etScore.getText().toString().trim();
        if (scoreStr.isEmpty()) {
            etScore.setError("Vui lòng nhập điểm");
            etScore.requestFocus();
            return;
        }

        double scoreValue;
        try {
            scoreValue = Double.parseDouble(scoreStr);
            if (scoreValue < 0 || scoreValue > 10) {
                etScore.setError("Điểm phải từ 0 đến 10");
                etScore.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            etScore.setError("Giá trị điểm không hợp lệ");
            etScore.requestFocus();
            return;
        }

        // Get selected student and subject
        int studentPosition = spStudent.getSelectedItemPosition();
        int subjectPosition = spSubject.getSelectedItemPosition();
        int scoreTypePosition = spScoreType.getSelectedItemPosition();

        if (studentPosition < 0 || subjectPosition < 0 || scoreTypePosition < 0) {
            Toast.makeText(this, "Vui lòng chọn đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            dbManager.open();
            
            // Create score object
            Score score = new Score();
            score.setStudentId(students.get(studentPosition).getId());
            score.setSubjectId(subjects.get(subjectPosition).getId());
            score.setScoreType(convertScoreType(scoreTypePosition));
            score.setScore(scoreValue);
            score.setTeacherId(sessionManager.getUserId()); // Assuming current user is the teacher
            
            // Insert score into database
            long result = dbManager.createScore(score);
            
            if (result != -1) {
                Toast.makeText(this, "Thêm điểm thành công", Toast.LENGTH_SHORT).show();
                finish(); // Close the activity
            } else {
                Toast.makeText(this, "Lỗi khi thêm điểm", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi hệ thống: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            dbManager.close();
        }
    }

    private String convertScoreType(int position) {
        switch (position) {
            case 0: return Constants.SCORE_TYPE_MIENG;
            case 1: return Constants.SCORE_TYPE_15PHUT;
            case 2: return Constants.SCORE_TYPE_1TIET;
            case 3: return Constants.SCORE_TYPE_HOCKY;
            default: return Constants.SCORE_TYPE_MIENG;
        }
    }

    private void redirectToLogin() {
        // For now, just finish the activity
        finish();
    }
}