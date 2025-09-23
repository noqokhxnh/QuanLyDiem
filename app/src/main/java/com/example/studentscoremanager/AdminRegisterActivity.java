package com.example.studentscoremanager;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class AdminRegisterActivity extends AppCompatActivity {

    private TextInputEditText edtEmail, edtPassword;
    private MaterialButton btnRegister;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_register);

        dbHelper = new DatabaseHelper(this);

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(v -> doRegister());
    }

    private void doRegister() {
        String email = edtEmail.getText() == null ? "" : edtEmail.getText().toString().trim();
        String password = edtPassword.getText() == null ? "" : edtPassword.getText().toString().trim();
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ", Toast.LENGTH_SHORT).show();
            return;
        }
        // Đăng ký/ghi đè tài khoản admin
        boolean ok = dbHelper.updatePassword("admin", password);
        if (!ok) {
            // Nếu chưa có user admin, tạo mới
            ok = dbHelper.addUser("admin", password, email);
        }
        Toast.makeText(this, ok ? "Thiết lập admin thành công" : "Thất bại", Toast.LENGTH_SHORT).show();
        if (ok) finish();
    }
}


