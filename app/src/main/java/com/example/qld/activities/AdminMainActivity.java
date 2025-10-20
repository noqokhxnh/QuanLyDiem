package com.example.qld.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.qld.R;
import com.example.qld.utils.SessionManager;

public class AdminMainActivity extends AppCompatActivity {
    private SessionManager sessionManager;
    private TextView tvWelcome;
    private View cardManageUsers, cardManageStudents, cardManageSubjects, 
                 cardManageScores, cardViewReports, cardChangePassword, 
                 cardBackupImport, cardLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        // Initialize session manager
        sessionManager = new SessionManager(this);

        // Check if user is logged in and is an admin
        if (!sessionManager.isLoggedIn() || !"ADMIN".equals(sessionManager.getUserRole())) {
            redirectToLogin();
            return;
        }

        // Initialize views
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        
        tvWelcome = findViewById(R.id.tv_welcome);
        cardManageUsers = findViewById(R.id.card_manage_users);
        cardManageStudents = findViewById(R.id.card_manage_students);
        cardManageSubjects = findViewById(R.id.card_manage_subjects);
        cardManageScores = findViewById(R.id.card_manage_scores);
        cardViewReports = findViewById(R.id.card_view_reports);
        cardChangePassword = findViewById(R.id.card_change_password);
        cardBackupImport = findViewById(R.id.card_backup_import);
        cardLogout = findViewById(R.id.card_logout);

        // Set welcome message
        String fullName = sessionManager.getFullName();
        tvWelcome.setText("Xin chào, " + fullName + "!");

        // Set card click listeners
        cardManageUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Implement manage users functionality
                startActivity(new Intent(AdminMainActivity.this, ManageUsersActivity.class));
            }
        });

        cardManageStudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminMainActivity.this, ManageStudentsActivity.class));
            }
        });

        cardManageSubjects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Implement manage subjects functionality
                startActivity(new Intent(AdminMainActivity.this, ManageSubjectsActivity.class));
            }
        });

        cardManageScores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminMainActivity.this, ManageScoresActivity.class));
            }
        });

        cardViewReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminMainActivity.this, StatisticsActivity.class));
            }
        });

        cardChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminMainActivity.this, ChangePasswordActivity.class));
            }
        });

        cardBackupImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminMainActivity.this, BackupImportActivity.class));
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

    private void redirectToLogin() {
        Intent intent = new Intent(AdminMainActivity.this, LoginActivity.class);
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