package com.example.studentscoremanager;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class AdminForgotPasswordActivity extends AppCompatActivity {

    private TextInputEditText edtEmail;
    private MaterialButton btnRecover;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_forgot);

        dbHelper = new DatabaseHelper(this);

        edtEmail = findViewById(R.id.edtEmail);
        btnRecover = findViewById(R.id.btnRecover);

        btnRecover.setOnClickListener(v -> doRecover());
    }

    private void doRecover() {
        String email = edtEmail.getText() == null ? "" : edtEmail.getText().toString().trim();
        if (email.isEmpty()) {
            Toast.makeText(this, "Nhập email admin", Toast.LENGTH_SHORT).show();
            return;
        }
        String password = dbHelper.getPasswordByEmail(email);
        if (password != null) {
            Toast.makeText(this, "Mật khẩu: " + password, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Không tìm thấy email", Toast.LENGTH_SHORT).show();
        }
    }
}


