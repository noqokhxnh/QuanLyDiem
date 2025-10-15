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

public class StudentMainActivity extends AppCompatActivity {
    private SessionManager sessionManager;
    private TextView tvWelcome;
    private Button btnViewScores, btnViewReports, btnChangePassword, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);

        // Initialize session manager
        sessionManager = new SessionManager(this);

        // Check if user is logged in
        if (!sessionManager.isLoggedIn() || sessionManager.getUserRole() != 0) {
            redirectToLogin();
            return;
        }

        // Initialize views
        tvWelcome = findViewById(R.id.tv_welcome);
        btnViewScores = findViewById(R.id.btn_view_scores);
        btnViewReports = findViewById(R.id.btn_view_reports);
        btnChangePassword = findViewById(R.id.btn_change_password);
        btnLogout = findViewById(R.id.btn_logout);

        // Set welcome message
        String fullName = sessionManager.getFullName();
        tvWelcome.setText("Xin chào, " + fullName + "!");

        // Set button click listeners with null checks
        if (btnViewScores != null) {
            btnViewScores.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(StudentMainActivity.this, ViewScoresActivity.class);
                    intent.putExtra("STUDENT_ID", sessionManager.getUserId());
                    startActivity(intent);
                }
            });
        }

        if (btnViewReports != null) {
            btnViewReports.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(StudentMainActivity.this, StatisticsActivity.class));
                }
            });
        }

        if (btnChangePassword != null) {
            btnChangePassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(StudentMainActivity.this, ChangePasswordActivity.class));
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
        Intent intent = new Intent(StudentMainActivity.this, LoginActivity.class);
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