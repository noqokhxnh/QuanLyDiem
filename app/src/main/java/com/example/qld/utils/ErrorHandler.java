package com.example.qld.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.example.qld.R;
import com.google.android.material.snackbar.Snackbar;

/**
 * Lớp tiện ích để xử lý lỗi và hiển thị thông báo cho người dùng
 * Bao gồm các phương thức để hiển thị Toast và Snackbar với các loại thông báo khác nhau
 */
public class ErrorHandler {

    /**
     * Hiển thị thông báo lỗi bằng Toast
     * @param context Context của ứng dụng
     * @param message Thông báo lỗi cần hiển thị
     */
    public static void showErrorToast(Context context, String message) {
        Toast.makeText(context, "Lỗi: " + message, Toast.LENGTH_LONG).show();
    }

    /**
     * Hiển thị thông báo lỗi bằng Snackbar
     * @param view View để gắn snackbar vào
     * @param message Thông báo lỗi cần hiển thị
     */
    public static void showErrorSnackbar(View view, String message) {
        Snackbar.make(view, "Lỗi: " + message, Snackbar.LENGTH_LONG)
                .setBackgroundTint(ContextCompat.getColor(view.getContext(), R.color.error_color))
                .setTextColor(Color.WHITE)
                .show();
    }

    /**
     * Hiển thị thông báo thông tin bằng Snackbar
     * @param view View để gắn snackbar vào
     * @param message Thông báo thông tin cần hiển thị
     */
    public static void showInfoSnackbar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setBackgroundTint(ContextCompat.getColor(view.getContext(), R.color.info_color))
                .setTextColor(Color.WHITE)
                .show();
    }

    /**
     * Hiển thị thông báo thành công bằng Snackbar
     * @param view View để gắn snackbar vào
     * @param message Thông báo thành công cần hiển thị
     */
    public static void showSuccessSnackbar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setBackgroundTint(ContextCompat.getColor(view.getContext(), R.color.success_color))
                .setTextColor(Color.WHITE)
                .show();
    }
}