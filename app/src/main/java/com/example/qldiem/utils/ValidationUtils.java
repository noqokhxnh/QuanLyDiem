package com.example.qldiem.utils;

import android.text.TextUtils;
import android.util.Patterns;

/**
 * Utility class cho validation dữ liệu đầu vào
 */
public class ValidationUtils {

    /**
     * Kiểm tra chuỗi có rỗng không
     */
    public static boolean isEmpty(String text) {
        return TextUtils.isEmpty(text) || text.trim().isEmpty();
    }

    /**
     * Kiểm tra email hợp lệ
     */
    public static boolean isValidEmail(String email) {
        return !isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * Kiểm tra mã sinh viên hợp lệ
     * Format: Chỉ chứa chữ và số, độ dài 3-20 ký tự
     */
    public static boolean isValidMaSV(String maSv) {
        if (isEmpty(maSv)) return false;
        return maSv.matches("^[A-Za-z0-9]{3,20}$");
    }

    /**
     * Kiểm tra mã lớp hợp lệ
     */
    public static boolean isValidMaLop(String maLop) {
        if (isEmpty(maLop)) return false;
        return maLop.matches("^[A-Za-z0-9]{2,15}$");
    }

    /**
     * Kiểm tra mã môn học hợp lệ
     */
    public static boolean isValidMaMH(String maMH) {
        if (isEmpty(maMH)) return false;
        return maMH.matches("^[A-Za-z0-9]{2,15}$");
    }

    /**
     * Kiểm tra mã chuyên ngành hợp lệ
     */
    public static boolean isValidMaChuyenNganh(String maCN) {
        if (isEmpty(maCN)) return false;
        return maCN.matches("^[A-Za-z0-9]{2,15}$");
    }

    /**
     * Kiểm tra tên hợp lệ
     * Cho phép chữ cái, khoảng trắng, và một số ký tự tiếng Việt
     */
    public static boolean isValidName(String name) {
        if (isEmpty(name)) return false;
        return name.length() >= 2 && name.length() <= 100;
    }

    /**
     * Kiểm tra điểm hợp lệ (0-10)
     */
    public static boolean isValidDiem(float diem) {
        return diem >= 0 && diem <= 10;
    }

    /**
     * Kiểm tra điểm hợp lệ từ chuỗi
     */
    public static boolean isValidDiem(String diemStr) {
        try {
            float diem = Float.parseFloat(diemStr);
            return isValidDiem(diem);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Kiểm tra username hợp lệ
     * Chỉ chứa chữ cái, số và dấu gạch dưới, độ dài 3-20
     */
    public static boolean isValidUsername(String username) {
        if (isEmpty(username)) return false;
        return username.matches("^[A-Za-z0-9_]{3,20}$");
    }

    /**
     * Kiểm tra ngày tháng hợp lệ (format: yyyy-MM-dd)
     */
    public static boolean isValidDate(String date) {
        if (isEmpty(date)) return false;
        return date.matches("^\\d{4}-\\d{2}-\\d{2}$");
    }

    /**
     * Hàm kiểm tra tính hợp lệ của mật khẩu theo tiêu chuẩn độ dài
     *
     * CÔNG DỤNG:
     * - Kiểm tra mật khẩu có đáp ứng điều kiện độ dài tối thiểu không
     * - Đảm bảo mật khẩu có độ dài tối thiểu 6 ký tự để tăng cường bảo mật
     * - Dùng để xác thực mật khẩu khi người dùng đăng ký hoặc đổi mật khẩu
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - String password: mật khẩu cần kiểm tra tính hợp lệ
     *
     * GIÁ TRỊ TRẢ VỀ:
     * - true: nếu mật khẩu hợp lệ (không rỗng và có độ dài từ 6 ký tự trở lên)
     * - false: nếu mật khẩu không hợp lệ (rỗng hoặc quá ngắn)
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Người dùng có thể đặt mật khẩu quá ngắn, giảm bảo mật
     * - Không có kiểm tra xác thực khi đăng ký hoặc đổi mật khẩu
     * - Ứng dụng dễ bị tấn công do sử dụng mật khẩu yếu
     */
    public static boolean isValidPassword(String password) {
        if (isEmpty(password)) return false;
        return password.length() >= 6;
    }

    /**
     * Lấy thông báo lỗi cho validation
     */
    public static String getErrorMessage(String fieldName, String errorType) {
        switch (errorType) {
            case "empty":
                return fieldName + " không được để trống";
            case "invalid":
                return fieldName + " không hợp lệ";
            case "exists":
                return fieldName + " đã tồn tại";
            case "notfound":
                return fieldName + " không tồn tại";
            default:
                return "Lỗi không xác định";
        }
    }
}
