package com.example.studentscoremanager;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class AdminAddScoreActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_add_score);
        dbHelper = new DatabaseHelper(this);

        TextInputEditText edtStudentId = findViewById(R.id.edtStudentId);
        TextInputEditText edtSubject = findViewById(R.id.edtSubject);
        TextInputEditText edtMidterm = findViewById(R.id.edtMidterm);
        TextInputEditText edtFinal = findViewById(R.id.edtFinal);
        TextInputEditText edtSemester = findViewById(R.id.edtSemester);
        TextInputEditText edtYear = findViewById(R.id.edtYear);
        MaterialButton btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(v -> {
            String id = textOf(edtStudentId);
            String subject = textOf(edtSubject);
            double mid = parseD(textOf(edtMidterm));
            double fin = parseD(textOf(edtFinal));
            String semester = textOf(edtSemester);
            String year = textOf(edtYear);
            if (id.isEmpty() || subject.isEmpty()) {
                Toast.makeText(this, "Mã SV và Môn học bắt buộc", Toast.LENGTH_SHORT).show();
                return;
            }
            boolean ok = dbHelper.addScore(id, subject, mid, fin, semester, year);
            Toast.makeText(this, ok ? "Đã thêm điểm" : "Thêm điểm thất bại", Toast.LENGTH_SHORT).show();
            if (ok) finish();
        });
    }

    private String textOf(TextInputEditText e) {
        return e.getText() == null ? "" : e.getText().toString().trim();
    }
    private double parseD(String s) {
        try { return s.isEmpty() ? 0.0 : Double.parseDouble(s); } catch (Exception e) { return 0.0; }
    }
}


