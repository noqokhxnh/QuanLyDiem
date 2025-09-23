package com.example.studentscoremanager;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class AdminAddStudentActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_add_student);
        dbHelper = new DatabaseHelper(this);

        TextInputEditText edtStudentId = findViewById(R.id.edtStudentId);
        TextInputEditText edtFullName = findViewById(R.id.edtFullName);
        TextInputEditText edtClassName = findViewById(R.id.edtClassName);
        TextInputEditText edtFaculty = findViewById(R.id.edtFaculty);
        TextInputEditText edtPhone = findViewById(R.id.edtPhone);
        TextInputEditText edtEmail = findViewById(R.id.edtEmail);
        MaterialButton btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(v -> {
            String id = textOf(edtStudentId);
            String name = textOf(edtFullName);
            String className = textOf(edtClassName);
            String faculty = textOf(edtFaculty);
            String phone = textOf(edtPhone);
            String email = textOf(edtEmail);
            if (id.isEmpty() || name.isEmpty()) {
                Toast.makeText(this, "Mã SV và Họ tên là bắt buộc", Toast.LENGTH_SHORT).show();
                return;
            }
            boolean ok = dbHelper.addStudent(id, name, className, faculty, phone, email);
            Toast.makeText(this, ok ? "Đã thêm sinh viên" : "Thêm thất bại (trùng mã?)", Toast.LENGTH_SHORT).show();
            if (ok) finish();
        });
    }

    private String textOf(TextInputEditText e) {
        return e.getText() == null ? "" : e.getText().toString().trim();
    }
}


