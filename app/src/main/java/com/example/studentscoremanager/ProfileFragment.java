package com.example.studentscoremanager;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

public class ProfileFragment extends Fragment {

    private DatabaseHelper dbHelper;
    private SharedPreferencesHelper prefsHelper;
    private TextView tvStudentName, tvStudentId, tvStudentClass;
    private String currentStudentId = "20201234"; // Mặc định

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        dbHelper = new DatabaseHelper(getContext());
        prefsHelper = new SharedPreferencesHelper(getContext());
        // Lấy MSSV từ session (được gán = username khi sinh viên đăng nhập)
        String sid = prefsHelper.getCurrentStudentId();
        if (sid != null && !sid.isEmpty()) {
            currentStudentId = sid;
        }

        // Ánh xạ views
        tvStudentName = view.findViewById(R.id.tvStudentName);
        tvStudentId = view.findViewById(R.id.tvStudentId);
        tvStudentClass = view.findViewById(R.id.tvStudentClass);

        setupClickListeners(view);
        loadProfileInfo();

        return view;
    }

    private void setupClickListeners(View view) {
        // Edit Profile: chỉ cho phép đổi mật khẩu, ẩn chỉnh sửa thông tin
        View btnEdit = view.findViewById(R.id.btnEditProfile);
        if (btnEdit != null) btnEdit.setVisibility(View.GONE);

        // Change Password
        view.findViewById(R.id.btnChangePassword).setOnClickListener(v -> showChangePasswordDialog());

        // Help
        view.findViewById(R.id.btnHelp).setOnClickListener(v -> showHelpDialog());

        // Logout
        view.findViewById(R.id.btnLogout).setOnClickListener(v -> showLogoutDialog());
    }

    private void loadProfileInfo() {
        Cursor studentCursor = dbHelper.getStudentById(currentStudentId);
        if (studentCursor != null && studentCursor.moveToFirst()) {
            String fullName = studentCursor.getString(studentCursor.getColumnIndexOrThrow(DatabaseHelper.COL_FULL_NAME));
            String studentId = studentCursor.getString(studentCursor.getColumnIndexOrThrow(DatabaseHelper.COL_STUDENT_ID));
            String className = studentCursor.getString(studentCursor.getColumnIndexOrThrow(DatabaseHelper.COL_CLASS));
            
            tvStudentName.setText(fullName);
            tvStudentId.setText("MSSV: " + studentId);
            tvStudentClass.setText("Lớp: " + className);
            
            studentCursor.close();
        }
    }

    private void showEditProfileDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Chỉnh sửa thông tin");

        // Tạo view với các trường input
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_edit_profile, null);
        TextInputEditText edtFullName = dialogView.findViewById(R.id.edtFullName);
        TextInputEditText edtPhone = dialogView.findViewById(R.id.edtPhone);
        TextInputEditText edtEmail = dialogView.findViewById(R.id.edtEmail);

        // Load dữ liệu hiện tại
        Cursor cursor = dbHelper.getStudentById(currentStudentId);
        if (cursor != null && cursor.moveToFirst()) {
            edtFullName.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_FULL_NAME)));
            edtPhone.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PHONE)));
            edtEmail.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_STUDENT_EMAIL)));
            cursor.close();
        }

        builder.setView(dialogView);

        builder.setPositiveButton("Lưu", (dialog, which) -> {
            String fullName = edtFullName.getText() != null ? edtFullName.getText().toString().trim() : "";
            String phone = edtPhone.getText() != null ? edtPhone.getText().toString().trim() : "";
            String email = edtEmail.getText() != null ? edtEmail.getText().toString().trim() : "";

            if (fullName.isEmpty() || phone.isEmpty() || email.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            // Cập nhật thông tin trong database
            boolean success = updateStudentInfo(fullName, phone, email);
            if (success) {
                Toast.makeText(getContext(), "Cập nhật thông tin thành công!", Toast.LENGTH_SHORT).show();
                loadProfileInfo(); // Reload thông tin
            } else {
                Toast.makeText(getContext(), "Có lỗi xảy ra khi cập nhật", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void showChangePasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Đổi mật khẩu");

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_change_password, null);
        TextInputEditText edtCurrentPassword = dialogView.findViewById(R.id.edtCurrentPassword);
        TextInputEditText edtNewPassword = dialogView.findViewById(R.id.edtNewPassword);
        TextInputEditText edtConfirmPassword = dialogView.findViewById(R.id.edtConfirmPassword);

        builder.setView(dialogView);

        builder.setPositiveButton("Đổi", (dialog, which) -> {
            String currentPassword = edtCurrentPassword.getText() != null ? edtCurrentPassword.getText().toString().trim() : "";
            String newPassword = edtNewPassword.getText() != null ? edtNewPassword.getText().toString().trim() : "";
            String confirmPassword = edtConfirmPassword.getText() != null ? edtConfirmPassword.getText().toString().trim() : "";

            if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                Toast.makeText(getContext(), "Mật khẩu mới không khớp", Toast.LENGTH_SHORT).show();
                return;
            }

            // Lấy username hiện tại từ SharedPreferences
            String currentUsername = prefsHelper.getCurrentUsername();
            
            // Kiểm tra mật khẩu hiện tại từ database
            if (!dbHelper.checkUser(currentUsername, currentPassword)) {
                Toast.makeText(getContext(), "Mật khẩu hiện tại không đúng", Toast.LENGTH_SHORT).show();
                return;
            }

            // Cập nhật mật khẩu trong database
            boolean success = updatePassword(newPassword);
            if (success) {
                // Đồng bộ mật khẩu mới vào SharedPreferences
                prefsHelper.updatePassword(newPassword);
                
                Toast.makeText(getContext(), "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                
                // Không cần đăng xuất nữa vì đã đồng bộ
                // User có thể tiếp tục sử dụng app với mật khẩu mới
            } else {
                Toast.makeText(getContext(), "Có lỗi xảy ra khi đổi mật khẩu", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void showHelpDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("Trợ giúp")
                .setMessage("Ứng dụng quản lý điểm sinh viên giúp bạn:\n\n" +
                        "• Xem thông tin cá nhân\n" +
                        "• Theo dõi điểm số học tập\n" +
                        "• Quản lý tài khoản\n" +
                        "• Cập nhật thông tin cá nhân\n" +
                        "• Đổi mật khẩu\n\n" +
                        "Liên hệ: support@studentscoremanager.com")
                .setPositiveButton("OK", null)
                .show();
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("Đăng xuất")
                .setMessage("Bạn có chắc chắn muốn đăng xuất?")
                .setPositiveButton("Đăng xuất", (dialog, which) -> {
                    // Xóa thông tin đăng nhập khỏi SharedPreferences
                    prefsHelper.logout();
                    
                    // Quay lại màn hình đăng nhập
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    getActivity().finish();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    // Cập nhật thông tin sinh viên
    private boolean updateStudentInfo(String fullName, String phone, String email) {
        // Tạo method update trong DatabaseHelper
        return dbHelper.updateStudentInfo(currentStudentId, fullName, phone, email);
    }

    // Cập nhật mật khẩu
    private boolean updatePassword(String newPassword) {
        // Lấy username hiện tại từ SharedPreferences
        String currentUsername = prefsHelper.getCurrentUsername();
        return dbHelper.updatePassword(currentUsername, newPassword);
    }
}
