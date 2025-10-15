package com.example.qld.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qld.R;
import com.example.qld.utils.SessionManager;

public class TeacherMainActivity extends AppCompatActivity {
    private SessionManager sessionManager;
    private TextView tvWelcome;
    private Button btnManageStudents, btnRegisterStudents, btnManageScores, btnViewReports, btnChangePassword, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_main);

        // Initialize session manager
        sessionManager = new SessionManager(this);

        // Check if user is logged in
        if (!sessionManager.isLoggedIn() || sessionManager.getUserRole() != 1) {
            redirectToLogin();
            return;
        }

        // Initialize views
        tvWelcome = findViewById(R.id.tv_welcome);
        btnManageStudents = findViewById(R.id.btn_manage_students);
        btnRegisterStudents = findViewById(R.id.btn_register_students);
        btnManageScores = findViewById(R.id.btn_manage_scores);
        btnViewReports = findViewById(R.id.btn_view_reports);
        btnChangePassword = findViewById(R.id.btn_change_password);
        btnLogout = findViewById(R.id.btn_logout);

        // Set welcome message
        String fullName = sessionManager.getFullName();
        tvWelcome.setText("Xin chào, " + fullName + "!");

        // Set button click listeners with null checks
        if (btnManageStudents != null) {
            btnManageStudents.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(TeacherMainActivity.this, ManageStudentsActivity.class));
                }
            });
        }

        if (btnRegisterStudents != null) {
            btnRegisterStudents.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(TeacherMainActivity.this, AddStudentActivity.class));
                }
            });
        }

        if (btnManageScores != null) {
            btnManageScores.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(TeacherMainActivity.this, ManageScoresActivity.class));
                }
            });
        }

        if (btnViewReports != null) {
            btnViewReports.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(TeacherMainActivity.this, StatisticsActivity.class));
                }
            });
        }

        if (btnChangePassword != null) {
            btnChangePassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(TeacherMainActivity.this, ChangePasswordActivity.class));
                }
            });
        }

        if (btnLogout != null) {
            btnLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sessionManager.logoutUser();
                    redirectToLogin();
                }
            });
        }
    }

    private void redirectToLogin() {
        Intent intent = new Intent(TeacherMainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

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