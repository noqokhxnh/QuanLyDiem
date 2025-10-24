package com.example.qld.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.qld.R;
import com.example.qld.utils.SessionManager;

public class ManageSubjectsActivity extends AppCompatActivity {
    private SessionManager sessionManager;
    private TextView tvTitle;
    private Button btnBack;

    /**
     * Phương thức được gọi khi activity được tạo
     * Khởi tạo giao diện, kiểm tra quyền truy cập và thiết lập sự kiện cho các thành phần
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_subjects);

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

        tvTitle = findViewById(R.id.tv_title);
        btnBack = findViewById(R.id.btn_back);

        tvTitle.setText("Quản lý môn học");

        // Set click listeners
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * Phương thức chuyển hướng người dùng đến màn hình đăng nhập
     * Được gọi khi người dùng đăng xuất hoặc không có quyền truy cập
     */
    private void redirectToLogin() {
        Intent intent = new Intent(ManageSubjectsActivity.this, LoginActivity.class);
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