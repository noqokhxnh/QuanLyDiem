package com.example.studentscoremanager;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class StudentMainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Log.d("StudentMainActivity", "onCreate started");
        
        try {
            setContentView(R.layout.activity_student_main);
            Log.d("StudentMainActivity", "setContentView successful");

            bottomNavigationView = findViewById(R.id.student_bottom_navigation);
            Log.d("StudentMainActivity", "bottomNavigationView found");

            // Mặc định hiển thị HomeFragment, cho phép mở fragment cụ thể qua extra
            if (savedInstanceState == null) {
                String open = getIntent().getStringExtra("open_fragment");
                Fragment initial = null;
                if ("students".equalsIgnoreCase(open)) initial = new StudentsFragment();
                else if ("scores".equalsIgnoreCase(open)) initial = new ScoresFragment();
                else initial = new HomeFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.student_fragment_container, initial)
                        .commit();
            }

            // Xử lý khi chọn menu
            bottomNavigationView.setOnItemSelectedListener(item -> {
                try {
                    Fragment selectedFragment = null;

                    int itemId = item.getItemId();
                    if (itemId == R.id.nav_home) {
                        selectedFragment = new HomeFragment();
                    } else if (itemId == R.id.nav_scores) {
                        selectedFragment = new ScoresFragment();
                    } else if (itemId == R.id.nav_schedule) {
                        selectedFragment = new ScheduleFragment();
                    } else if (itemId == R.id.nav_notifications) {
                        selectedFragment = new NotificationsFragment();
                    } else if (itemId == R.id.nav_profile) {
                        selectedFragment = new ProfileFragment();
                    }

                    if (selectedFragment != null) {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.student_fragment_container, selectedFragment)
                                .commit();
                    }

                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            });
        } catch (Exception e) {
            Log.e("StudentMainActivity", "Error in onCreate", e);
            Toast.makeText(this, "Lỗi khởi tạo ứng dụng: " + e.getMessage(), Toast.LENGTH_LONG).show();
            // Không gọi finish() để tránh đóng app, chỉ log lỗi
        }
    }
}
