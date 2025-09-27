package com.example.studentscoremanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class AdminDashboardActivity extends AppCompatActivity {

    private SharedPreferencesHelper prefsHelper;
    private DatabaseHelper dbHelper;
    private TextView tvKpiStudents, tvKpiNotifications, tvKpiSchedules, tvKpiTimetables;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_dashboard);

        prefsHelper = new SharedPreferencesHelper(this);
        dbHelper = new DatabaseHelper(this);
        if (!"giangvien".equalsIgnoreCase(prefsHelper.getCurrentUsername())) {
            Toast.makeText(this, "Vui lòng đăng nhập giảng viên", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, AdminLoginActivity.class));
            finish();
            return;
        }

        tvKpiStudents = findViewById(R.id.tvKpiStudents);
        tvKpiNotifications = findViewById(R.id.tvKpiNotifications);
        tvKpiSchedules = findViewById(R.id.tvKpiSchedules);
        tvKpiTimetables = findViewById(R.id.tvKpiTimetables);

        MaterialButton btnAddStudent = findViewById(R.id.btnAddStudent);
        MaterialButton btnAddScore = findViewById(R.id.btnAddScore);
        MaterialButton btnViewStudentUI = findViewById(R.id.btnViewStudentUI);
        MaterialButton btnViewScoresUI = findViewById(R.id.btnViewScoresUI);
        MaterialButton btnViewStudentList = findViewById(R.id.btnViewStudentList);
        MaterialButton btnManageSchedules = findViewById(R.id.btnManageSchedules);
        MaterialButton btnManageNotifications = findViewById(R.id.btnManageNotifications);
        MaterialButton btnManageAllScores = findViewById(R.id.btnManageAllScores);
        MaterialButton btnManageTimetables = findViewById(R.id.btnManageTimetables);
        MaterialButton btnLogout = findViewById(R.id.btnLogout);

        btnAddStudent.setOnClickListener(v -> startActivity(new Intent(this, AdminAddStudentActivity.class)));
        btnAddScore.setOnClickListener(v -> startActivity(new Intent(this, AdminAddScoreActivity.class)));
        btnViewStudentUI.setOnClickListener(v -> {
            Intent i = new Intent(this, StudentMainActivity.class);
            i.putExtra("open_fragment", "students");
            startActivity(i);
        });
        btnViewScoresUI.setOnClickListener(v -> {
            Intent i = new Intent(this, StudentMainActivity.class);
            i.putExtra("open_fragment", "scores");
            startActivity(i);
        });
        btnManageSchedules.setOnClickListener(v -> startActivity(new Intent(this, AdminScheduleActivity.class)));
        btnManageNotifications.setOnClickListener(v -> startActivity(new Intent(this, AdminNotificationActivity.class)));
        btnManageTimetables.setOnClickListener(v -> startActivity(new Intent(this, AdminTimetableActivity.class)));
        btnManageAllScores.setOnClickListener(v -> startActivity(new Intent(this, AdminAllScoresActivity.class)));
        btnViewStudentList.setOnClickListener(v -> startActivity(new Intent(this, AdminStudentListActivity.class)));
        btnLogout.setOnClickListener(v -> showLogoutDialog());

        refreshKpis();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshKpis();
    }

    private void refreshKpis() {
        if (tvKpiStudents != null) tvKpiStudents.setText(String.valueOf(dbHelper.getStudentsCount()));
        if (tvKpiNotifications != null) tvKpiNotifications.setText(String.valueOf(dbHelper.getNotificationsCount()));
        if (tvKpiSchedules != null) tvKpiSchedules.setText(String.valueOf(dbHelper.getSchedulesCount()));
        if (tvKpiTimetables != null) tvKpiTimetables.setText(String.valueOf(dbHelper.getTimetablesCount()));
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Đăng xuất")
                .setMessage("Bạn có chắc chắn muốn đăng xuất?")
                .setPositiveButton("Đăng xuất", (dialog, which) -> {
                    prefsHelper.logout();
                    Toast.makeText(this, "Đã đăng xuất", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, LoginActivity.class));
                    finish();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}


