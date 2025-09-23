package com.example.studentscoremanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class ForgotPassword extends AppCompatActivity {

    TextInputEditText edtEmailReset;
    MaterialButton btnSendReset;
    TextView tvBackToLogin;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_pass);

        edtEmailReset = findViewById(R.id.edtEmailReset);
        btnSendReset = findViewById(R.id.btnSendReset);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);
        dbHelper = new DatabaseHelper(this);

        // Nút gửi yêu cầu đặt lại mật khẩu
        btnSendReset.setOnClickListener(v -> {
            String email = edtEmailReset.getText().toString().trim();
            if (email.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
                return;
            }

            // Kiểm tra email có tồn tại trong DB không
            String password = dbHelper.getPasswordByEmail(email);
            if (password != null) {
                Toast.makeText(this, "Mật khẩu của bạn là: " + password, Toast.LENGTH_LONG).show();
                // Có thể điều hướng lại login
                startActivity(new Intent(ForgotPassword.this, LoginActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Email không tồn tại trong hệ thống!", Toast.LENGTH_SHORT).show();
            }
        });

        // Quay lại đăng nhập
        tvBackToLogin.setOnClickListener(v -> {
            startActivity(new Intent(ForgotPassword.this, LoginActivity.class));
            finish();
        });
    }
}
