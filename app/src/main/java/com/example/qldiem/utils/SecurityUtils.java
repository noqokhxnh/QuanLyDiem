package com.example.qldiem.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utility class cho các chức năng bảo mật - cung cấp các phương thức bảo vệ dữ liệu người dùng
 *
 * CÔNG DỤNG:
 * - Cung cấp các phương thức bảo mật để bảo vệ thông tin người dùng
 * - Mã hóa mật khẩu sử dụng thuật toán SHA-256 để lưu trữ an toàn
 * - Kiểm tra độ mạnh của mật khẩu để đảm bảo an toàn
 * - Làm sạch dữ liệu đầu vào để tránh SQL injection
 *
 * CÁC PHƯƠNG THỨC CHÍNH:
 * - hashPassword: Mã hóa mật khẩu sử dụng SHA-256
 * - isPasswordStrong: Kiểm tra độ mạnh của mật khẩu
 * - sanitizeInput: Làm sạch dữ liệu đầu vào để tránh SQL injection
 *
 * NẾU KHÔNG CÓ CLASS NÀY:
 * - Mật khẩu sẽ được lưu trữ dưới dạng văn bản thuần
 * - Ứng dụng sẽ có lỗ hổng bảo mật nghiêm trọng
 * - Không thể kiểm tra độ mạnh của mật khẩu người dùng
 * - Dễ bị tấn công SQL injection
 */
public class SecurityUtils {

    /**
     * Hàm mã hóa mật khẩu sử dụng thuật toán SHA-256
     *
     * CÔNG DỤNG:
     * - Chuyển đổi mật khẩu văn bản thường thành chuỗi mã hóa một chiều
     * - Đảm bảo mật khẩu không được lưu trữ dưới dạng có thể đọc được
     * - Dùng để mã hóa mật khẩu trước khi lưu vào cơ sở dữ liệu
     *
     * CÁC BIẾN TRONG HÀM:
     * - digest: đối tượng MessageDigest để thực hiện thuật toán băm SHA-256
     * - hash: mảng byte chứa kết quả băm của mật khẩu
     * - hexString: StringBuilder để xây dựng chuỗi hex từ kết quả băm
     * - b: byte trong mảng hash để chuyển đổi sang hex
     * - hex: chuỗi hex đại diện cho một byte
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - String password: mật khẩu gốc cần được mã hóa
     *
     * GIÁ TRỊ TRẢ VỀ:
     * - String: chuỗi mật khẩu đã mã hóa theo chuẩn SHA-256 (dưới dạng hex)
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Mật khẩu sẽ được lưu trữ dưới dạng văn bản thường
     * - Dữ liệu người dùng không được bảo vệ nếu cơ sở dữ liệu bị xâm nhập
     * - Ứng dụng vi phạm các tiêu chuẩn bảo mật cơ bản
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return password; // Fallback nếu không mã hóa được
        }
    }

    /**
     * Hàm kiểm tra độ mạnh của mật khẩu
     *
     * CÔNG DỤNG:
     * - Kiểm tra mật khẩu có đáp ứng điều kiện độ mạnh tối thiểu không
     * - Đảm bảo mật khẩu có độ dài tối thiểu để tăng cường bảo mật
     * - Dùng để xác thực mật khẩu khi người dùng đăng ký hoặc đổi mật khẩu
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - String password: mật khẩu cần kiểm tra độ mạnh
     *
     * GIÁ TRỊ TRẢ VỀ:
     * - boolean: true nếu mật khẩu đủ mạnh (không null và có độ dài từ 6 ký tự trở lên)
     * - boolean: false nếu mật khẩu không đủ mạnh
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Người dùng có thể đặt mật khẩu quá ngắn, giảm bảo mật
     * - Không có kiểm tra xác thực khi đăng ký hoặc đổi mật khẩu
     * - Ứng dụng dễ bị tấn công do sử dụng mật khẩu yếu
     */
    public static boolean isPasswordStrong(String password) {
        if (password == null || password.length() < 6) {
            return false;
        }
        // Có thể thêm các điều kiện khác: chữ hoa, chữ thường, số, ký tự đặc biệt
        return true;
    }

    /**
     * Hàm làm sạch dữ liệu đầu vào để tránh SQL injection
     *
     * CÔNG DỤNG:
     * - Loại bỏ các ký tự đặc biệt nguy hiểm có thể gây ra SQL injection
     * - Làm sạch chuỗi đầu vào trước khi sử dụng trong truy vấn cơ sở dữ liệu
     * - Dùng để tăng cường bảo mật khi xử lý dữ liệu người dùng
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - String input: chuỗi đầu vào cần được làm sạch
     *
     * GIÁ TRỊ TRẢ VỀ:
     * - String: chuỗi đã được làm sạch (loại bỏ ký tự đặc biệt nguy hiểm)
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Dữ liệu đầu vào không được làm sạch, có thể gây ra SQL injection
     * - Ứng dụng dễ bị tấn công SQL injection
     * - Bảo mật cơ sở dữ liệu bị ảnh hưởng
     */
    public static String sanitizeInput(String input) {
        if (input == null) return "";
        return input.replaceAll("[';\"\\\\]", "");
    }
}
