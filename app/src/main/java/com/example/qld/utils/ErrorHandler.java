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
     * 
     * Cách thức hoạt động:
     * 1. Tạo một Toast với thông báo lỗi
     * 2. Thêm tiền tố "Lỗi: " vào thông báo để người dùng dễ nhận biết
     * 3. Hiển thị Toast trong thời gian dài (LENGTH_LONG)
     * 
     * @param context Context của ứng dụng (Activity, Service, Application, v.v.)
     *                Được sử dụng để tạo Toast
     * @param message Thông báo lỗi cần hiển thị (chuỗi không được null hoặc rỗng)
     *                Ví dụ: "Không thể kết nối đến máy chủ"
     * 
     * Ví dụ: Nếu message là "Không thể kết nối đến máy chủ", phương thức sẽ hiển thị Toast với nội dung "Lỗi: Không thể kết nối đến máy chủ"
     */
    public static void showErrorToast(Context context, String message) {
        Toast.makeText(context, "Lỗi: " + message, Toast.LENGTH_LONG).show();
    }

    /**
     * Hiển thị thông báo lỗi bằng Snackbar
     * 
     * Cách thức hoạt động:
     * 1. Tạo một Snackbar với thông báo lỗi
     * 2. Thêm tiền tố "Lỗi: " vào thông báo để người dùng dễ nhận biết
     * 3. Thiết lập màu nền đỏ cho Snackbar để phân biệt với các loại thông báo khác
     * 4. Thiết lập màu chữ trắng để tăng độ tương phản
     * 5. Hiển thị Snackbar trong thời gian dài (LENGTH_LONG)
     * 
     * @param view View để gắn snackbar vào (thường là root view của activity/fragment)
     *             Snackbar sẽ xuất hiện ở đáy của view này
     * @param message Thông báo lỗi cần hiển thị (chuỗi không được null hoặc rỗng)
     *                Ví dụ: "Tên đăng nhập hoặc mật khẩu không đúng"
     * 
     * Ví dụ: Nếu message là "Tên đăng nhập hoặc mật khẩu không đúng", phương thức sẽ hiển thị Snackbar với nội dung "Lỗi: Tên đăng nhập hoặc mật khẩu không đúng"
     */
    public static void showErrorSnackbar(View view, String message) {
        Snackbar.make(view, "Lỗi: " + message, Snackbar.LENGTH_LONG)
                .setBackgroundTint(ContextCompat.getColor(view.getContext(), R.color.error_color))
                .setTextColor(Color.WHITE)
                .show();
    }

    /**
     * Hiển thị thông báo thông tin bằng Snackbar
     * 
     * Cách thức hoạt động:
     * 1. Tạo một Snackbar với thông báo thông tin
     * 2. Thiết lập màu nền xanh lam cho Snackbar để phân biệt với các loại thông báo khác
     * 3. Thiết lập màu chữ trắng để tăng độ tương phản
     * 4. Hiển thị Snackbar trong thời gian dài (LENGTH_LONG)
     * 
     * @param view View để gắn snackbar vào (thường là root view của activity/fragment)
     *             Snackbar sẽ xuất hiện ở đáy của view này
     * @param message Thông báo thông tin cần hiển thị (chuỗi không được null hoặc rỗng)
     *                Ví dụ: "Đang tải dữ liệu..."
     * 
     * Ví dụ: Nếu message là "Đang tải dữ liệu...", phương thức sẽ hiển thị Snackbar với nội dung "Đang tải dữ liệu..."
     */
    public static void showInfoSnackbar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setBackgroundTint(ContextCompat.getColor(view.getContext(), R.color.info_color))
                .setTextColor(Color.WHITE)
                .show();
    }

    /**
     * Hiển thị thông báo thành công bằng Snackbar
     * 
     * Cách thức hoạt động:
     * 1. Tạo một Snackbar với thông báo thành công
     * 2. Thiết lập màu nền xanh lá cho Snackbar để thể hiện kết quả tích cực
     * 3. Thiết lập màu chữ trắng để tăng độ tương phản
     * 4. Hiển thị Snackbar trong thời gian dài (LENGTH_LONG)
     * 
     * @param view View để gắn snackbar vào (thường là root view của activity/fragment)
     *             Snackbar sẽ xuất hiện ở đáy của view này
     * @param message Thông báo thành công cần hiển thị (chuỗi không được null hoặc rỗng)
     *                Ví dụ: "Đăng nhập thành công"
     * 
     * Ví dụ: Nếu message là "Đăng nhập thành công", phương thức sẽ hiển thị Snackbar với nội dung "Đăng nhập thành công"
     */
    public static void showSuccessSnackbar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setBackgroundTint(ContextCompat.getColor(view.getContext(), R.color.success_color))
                .setTextColor(Color.WHITE)
                .show();
    }
}