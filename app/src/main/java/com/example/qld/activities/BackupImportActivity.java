package com.example.qld.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.qld.R;
import com.example.qld.database.DatabaseManager;
import com.example.qld.models.Score;
import com.example.qld.models.Student;
import com.example.qld.models.Subject;
import com.example.qld.models.User;
import com.example.qld.utils.SessionManager;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class BackupImportActivity extends AppCompatActivity {
    private SessionManager sessionManager;
    private DatabaseManager dbManager;
    private TextView tvBackupInfo;
    private Button btnBackupStudents, btnBackupScores, btnImportData, btnBack;

    private static final int PERMISSION_REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup_import);

        // Initialize session and database managers
        sessionManager = new SessionManager(this);
        dbManager = new DatabaseManager(this);

        // Check if user is logged in and is a teacher
        if (!sessionManager.isLoggedIn() || !"TEACHER".equals(sessionManager.getUserRole())) {
            redirectToLogin();
            return;
        }

        // Initialize views
        tvBackupInfo = findViewById(R.id.tv_backup_info);
        btnBackupStudents = findViewById(R.id.btn_backup_students);
        btnBackupScores = findViewById(R.id.btn_backup_scores);
        btnImportData = findViewById(R.id.btn_import_data);
        btnBack = findViewById(R.id.btn_back);

        // Set up UI
        tvBackupInfo.setText("Tính năng sao lưu dữ liệu:\n- Sao lưu danh sách học sinh\n- Sao lưu bảng điểm\n- Nhập dữ liệu từ file");

        // Set click listeners
        btnBackupStudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    backupStudents();
                } else {
                    requestPermission();
                }
            }
        });

        btnBackupScores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    backupScores();
                } else {
                    requestPermission();
                }
            }
        });

        btnImportData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // For now, just show a toast - actual import would require file picker
                Toast.makeText(BackupImportActivity.this, "Tính năng nhập dữ liệu sẽ được phát triển tiếp", Toast.LENGTH_SHORT).show();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    private void backupStudents() {
        try {
            dbManager.open();
            List<Student> students = dbManager.getAllStudentsWithUserInfo();
            
            if (students.isEmpty()) {
                Toast.makeText(this, "Không có dữ liệu học sinh để sao lưu", Toast.LENGTH_SHORT).show();
                return;
            }
            
            // Create CSV file for students
            String fileName = "Students_Backup_" + System.currentTimeMillis() + ".csv";
            String filePath = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) + "/" + fileName;
            
            File file = new File(filePath);
            FileWriter outputfile = new FileWriter(file);
            CSVWriter writer = new CSVWriter(outputfile);
            
            // Create header
            String[] header = {"ID", "Mã học sinh", "Họ tên", "Lớp", "Điểm TB"};
            writer.writeNext(header);
            
            // Write student data
            for (Student student : students) {
                String[] data = {
                    String.valueOf(student.getId()),
                    student.getStudentCode(),
                    student.getFullName(),
                    student.getClassName(),
                    String.valueOf(student.getAverage())
                };
                writer.writeNext(data);
            }
            
            writer.close();
            outputfile.close();
            
            Toast.makeText(this, "Sao lưu học sinh thành công: " + filePath, Toast.LENGTH_LONG).show();
            
        } catch (IOException e) {
            Toast.makeText(this, "Lỗi khi sao lưu học sinh: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            dbManager.close();
        }
    }

    private void backupScores() {
        try {
            dbManager.open();
            List<Score> scores = dbManager.getAllScores();
            
            if (scores.isEmpty()) {
                Toast.makeText(this, "Không có dữ liệu điểm để sao lưu", Toast.LENGTH_SHORT).show();
                return;
            }
            
            // Create CSV file for scores
            String fileName = "Scores_Backup_" + System.currentTimeMillis() + ".csv";
            String filePath = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) + "/" + fileName;
            
            File file = new File(filePath);
            FileWriter outputfile = new FileWriter(file);
            CSVWriter writer = new CSVWriter(outputfile);
            
            // Create header
            String[] header = {"ID", "ID học sinh", "ID môn học", "Loại điểm", "Điểm", "Ngày tạo", "ID giáo viên"};
            writer.writeNext(header);
            
            // Write score data
            for (Score score : scores) {
                String[] data = {
                    String.valueOf(score.getId()),
                    String.valueOf(score.getStudentId()),
                    String.valueOf(score.getSubjectId()),
                    score.getScoreType(),
                    String.valueOf(score.getScore()),
                    score.getDateCreated() != null ? score.getDateCreated() : "",
                    String.valueOf(score.getTeacherId())
                };
                writer.writeNext(data);
            }
            
            writer.close();
            outputfile.close();
            
            Toast.makeText(this, "Sao lưu điểm thành công: " + filePath, Toast.LENGTH_LONG).show();
            
        } catch (IOException e) {
            Toast.makeText(this, "Lỗi khi sao lưu điểm: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            dbManager.close();
        }
    }

    private void redirectToLogin() {
        Intent intent = new Intent(BackupImportActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}