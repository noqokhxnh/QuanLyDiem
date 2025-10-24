package com.example.qld.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qld.R;
import com.example.qld.utils.SessionManager;

public class TeacherMainActivity extends AppCompatActivity {
    private SessionManager sessionManager;
    private TextView tvWelcome;
    private View cardManageStudents, cardRegisterStudents, cardManageScores, 
                 cardViewReports, cardChangePassword, cardBackupImport, cardLogout;

    /**
     * Phương thức được gọi khi activity được tạo
     * Khởi tạo giao diện, kiểm tra quyền truy cập và thiết lập sự kiện cho các thành phần
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_main);

        // Initialize session manager
        sessionManager = new SessionManager(this);

        // Check if user is logged in and is a teacher
        if (!sessionManager.isLoggedIn() || !"TEACHER".equals(sessionManager.getUserRole())) {
            redirectToLogin();
            return;
        }

        // Initialize views
        tvWelcome = findViewById(R.id.tv_welcome);
        cardManageStudents = findViewById(R.id.card_manage_students);
        cardRegisterStudents = findViewById(R.id.card_register_students);
        cardManageScores = findViewById(R.id.card_manage_scores);
        cardViewReports = findViewById(R.id.card_view_reports);
        cardChangePassword = findViewById(R.id.card_change_password);
        cardBackupImport = findViewById(R.id.card_backup_import);
        cardLogout = findViewById(R.id.card_logout);

        // Set welcome message
        String fullName = sessionManager.getFullName();
        tvWelcome.setText("Xin chào, " + fullName + "!");

        // Set card click listeners
        cardManageStudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TeacherMainActivity.this, ManageStudentsActivity.class));
            }
        });

        cardRegisterStudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TeacherMainActivity.this, AddStudentActivity.class));
            }
        });

        cardManageScores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TeacherMainActivity.this, ManageScoresActivity.class));
            }
        });

        cardViewReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TeacherMainActivity.this, StatisticsActivity.class));
            }
        });

        cardChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TeacherMainActivity.this, ChangePasswordActivity.class));
            }
        });

        cardBackupImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TeacherMainActivity.this, BackupImportActivity.class));
            }
        });

        cardLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.logoutUser();
                redirectToLogin();
            }
        });
    }

    /**
     * Phương thức chuyển hướng người dùng đến màn hình đăng nhập
     * Được gọi khi người dùng đăng xuất hoặc không có quyền truy cập
     */
    private void redirectToLogin() {
        Intent intent = new Intent(TeacherMainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    /**
     * Phương thức tạo menu cho activity
     * @param menu Menu sẽ được tạo
     * @return True nếu thành công
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Phương thức xử lý sự kiện khi người dùng chọn một mục trong menu
     * @param item Mục được chọn trong menu
     * @return True nếu sự kiện được xử lý
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            sessionManager.logoutUser();
            redirectToLogin();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}