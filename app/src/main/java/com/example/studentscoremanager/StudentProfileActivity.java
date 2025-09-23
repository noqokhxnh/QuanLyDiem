package com.example.studentscoremanager;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;

public class StudentProfileActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private String currentStudentId;
    
    // Views
    private TextView tvStudentId, tvFullName, tvClass, tvFaculty, tvPhone, tvEmail, tvHometown, tvBirthYear;
    private MaterialCardView cardPersonalInfo, cardAcademicInfo, cardContactInfo;
    private Button btnEditProfile;
    private ImageView imgAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);

        // Lấy student ID từ SharedPreferences
        SharedPreferencesHelper prefsHelper = new SharedPreferencesHelper(this);
        currentStudentId = prefsHelper.getCurrentStudentId();
        
        if (currentStudentId == null || currentStudentId.isEmpty()) {
            Toast.makeText(this, "Không tìm thấy thông tin sinh viên", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        dbHelper = new DatabaseHelper(this);
        
        // Khởi tạo views
        initViews();
        
        // Thiết lập toolbar
        setupToolbar();
        
        // Load thông tin sinh viên
        loadStudentInfo();
        
        // Thiết lập click listeners
        setupClickListeners();
    }

    private void initViews() {
        tvStudentId = findViewById(R.id.tvStudentId);
        tvFullName = findViewById(R.id.tvFullName);
        tvClass = findViewById(R.id.tvClass);
        tvFaculty = findViewById(R.id.tvFaculty);
        tvPhone = findViewById(R.id.tvPhone);
        tvEmail = findViewById(R.id.tvEmail);
        tvHometown = findViewById(R.id.tvHometown);
        tvBirthYear = findViewById(R.id.tvBirthYear);
        
        cardPersonalInfo = findViewById(R.id.cardPersonalInfo);
        cardAcademicInfo = findViewById(R.id.cardAcademicInfo);
        cardContactInfo = findViewById(R.id.cardContactInfo);
        
        btnEditProfile = findViewById(R.id.btnEditProfile);
        imgAvatar = findViewById(R.id.imgAvatar);
    }

    private void setupToolbar() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Thông tin cá nhân");
        }
    }

    private void loadStudentInfo() {
        Cursor cursor = dbHelper.getStudentById(currentStudentId);
        if (cursor != null && cursor.moveToFirst()) {
            tvStudentId.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_STUDENT_ID)));
            tvFullName.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_FULL_NAME)));
            tvClass.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CLASS)));
            tvFaculty.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_FACULTY)));
            tvPhone.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PHONE)));
            tvEmail.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_STUDENT_EMAIL)));
            
            // Thông tin mới
            String hometown = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_HOMETOWN));
            int birthYear = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_BIRTH_YEAR));
            
            tvHometown.setText(hometown != null && !hometown.isEmpty() ? hometown : "Chưa cập nhật");
            tvBirthYear.setText(birthYear > 0 ? String.valueOf(birthYear) : "Chưa cập nhật");
            
            cursor.close();
        } else {
            Toast.makeText(this, "Không tìm thấy thông tin sinh viên", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setupClickListeners() {
        btnEditProfile.setOnClickListener(v -> showEditDialog());
    }

    private void showEditDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_profile, null);
        builder.setView(dialogView);

        // Lấy views từ dialog
        EditText edtFullName = dialogView.findViewById(R.id.edtFullName);
        EditText edtPhone = dialogView.findViewById(R.id.edtPhone);
        EditText edtEmail = dialogView.findViewById(R.id.edtEmail);
        EditText edtHometown = dialogView.findViewById(R.id.edtHometown);
        EditText edtBirthYear = dialogView.findViewById(R.id.edtBirthYear);

        // Điền dữ liệu hiện tại
        edtFullName.setText(tvFullName.getText().toString());
        edtPhone.setText(tvPhone.getText().toString());
        edtEmail.setText(tvEmail.getText().toString());
        edtHometown.setText(tvHometown.getText().toString().equals("Chưa cập nhật") ? "" : tvHometown.getText().toString());
        edtBirthYear.setText(tvBirthYear.getText().toString().equals("Chưa cập nhật") ? "" : tvBirthYear.getText().toString());

        builder.setPositiveButton("Lưu", (dialog, which) -> {
            String fullName = edtFullName.getText().toString().trim();
            String phone = edtPhone.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();
            String hometown = edtHometown.getText().toString().trim();
            String birthYearStr = edtBirthYear.getText().toString().trim();

            if (fullName.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập họ tên", Toast.LENGTH_SHORT).show();
                return;
            }

            if (phone.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập số điện thoại", Toast.LENGTH_SHORT).show();
                return;
            }

            if (email.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
                return;
            }

            int birthYear = 0;
            if (!birthYearStr.isEmpty()) {
                try {
                    birthYear = Integer.parseInt(birthYearStr);
                    if (birthYear < 1900 || birthYear > 2010) {
                        Toast.makeText(this, "Năm sinh không hợp lệ", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Năm sinh không hợp lệ", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            // Cập nhật database
            boolean success = dbHelper.updateStudentProfile(currentStudentId, fullName, phone, email, hometown, birthYear);
            
            if (success) {
                Toast.makeText(this, "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
                loadStudentInfo(); // Reload thông tin
            } else {
                Toast.makeText(this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Hủy", null);
        builder.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
