package com.example.qldiem.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Lớp tiện ích cho xử lý mật khẩu, cung cấp các phương thức mã hóa và xác minh mật khẩu
 *
 * CÔNG DỤNG:
 * - Mã hóa mật khẩu theo chuẩn SHA-256 để lưu an toàn vào cơ sở dữ liệu
 * - Xác minh mật khẩu nhập vào có khớp với mật khẩu đã mã hóa không
 * - Kiểm tra tính hợp lệ của mật khẩu
 *
 * NẾU KHÔNG CÓ LỚP NÀY:
 * - Mật khẩu sẽ được lưu trữ dưới dạng văn bản thuần trong cơ sở dữ liệu
 * - Ứng dụng sẽ có lỗ hổng bảo mật nghiêm trọng
 * - Không thể xác thực người dùng một cách an toàn
 */
public class PasswordUtils {

    /**
     * Hàm mã hóa mật khẩu sử dụng thuật toán SHA-256
     *
     * CÔNG DỤNG:
     * - Chuyển đổi mật khẩu văn bản thường thành chuỗi mã hóa một chiều
     * - Đảm bảo mật khẩu không được lưu trữ dưới dạng có thể đọc được
     * - Dùng để mã hóa mật khẩu trước khi lưu vào cơ sở dữ liệu
     *
     * CÁC BIẾN TRONG HÀM:
     * - digest: đối tượng MessageDigest để thực hiện thuật toán băm
     * - hash: mảng byte chứa kết quả băm của mật khẩu
     * - hexString: chuỗi StringBuilder để xây dựng chuỗi hex từ kết quả băm
     * - b: byte trong mảng hash để chuyển đổi sang hex
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - String password: mật khẩu gốc cần được mã hóa
     *
     * GIÁ TRỊ TRẢ VỀ:
     * - Chuỗi mật khẩu đã mã hóa theo chuẩn SHA-256 (dưới dạng hex)
     * - Trả về mật khẩu gốc nếu thuật toán không tồn tại (trường hợp hiếm)
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
     * Hàm xác minh mật khẩu người dùng nhập vào có khớp với mật khẩu đã mã hóa không
     *
     * CÔNG DỤNG:
     * - So sánh mật khẩu người dùng nhập với mật khẩu đã mã hóa trong cơ sở dữ liệu
     * - Dùng trong quá trình đăng nhập để xác thực người dùng
     * - Không bao giờ giải mã mật khẩu đã lưu, chỉ mã hóa mật khẩu nhập và so sánh
     *
     * CÁC BIẾN TRONG HÀM:
     * - hashed: mật khẩu người dùng nhập sau khi đã mã hóa
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - String plainPassword: mật khẩu do người dùng nhập (chưa mã hóa)
     * - String hashedPassword: mật khẩu đã mã hóa lấy từ cơ sở dữ liệu
     *
     * GIÁ TRỊ TRẢ VỀ:
     * - true: nếu mật khẩu nhập vào khớp với mật khẩu đã mã hóa
     * - false: nếu mật khẩu không khớp
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể xác thực mật khẩu người dùng khi đăng nhập
     * - Không thể so sánh mật khẩu đã mã hóa với mật khẩu nhập vào
     * - Tính năng đăng nhập sẽ không hoạt động
     */
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        String hashed = hashPassword(plainPassword);
        return hashed.equals(hashedPassword);
    }

    /**
     * Hàm kiểm tra tính hợp lệ của mật khẩu theo các tiêu chuẩn cơ bản
     *
     * CÔNG DỤNG:
     * - Kiểm tra mật khẩu có đáp ứng điều kiện tối thiểu không
     * - Đảm bảo mật khẩu có độ dài tối thiểu để tăng cường bảo mật
     * - Dùng để xác thực mật khẩu khi người dùng đăng ký hoặc đổi mật khẩu
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - String password: mật khẩu cần kiểm tra tính hợp lệ
     *
     * GIÁ TRỊ TRẢ VỀ:
     * - true: nếu mật khẩu hợp lệ (không null và có độ dài từ 6 ký tự trở lên)
     * - false: nếu mật khẩu không hợp lệ
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Người dùng có thể đặt mật khẩu quá ngắn, giảm bảo mật
     * - Không có kiểm tra xác thực khi đăng ký hoặc đổi mật khẩu
     * - Ứng dụng dễ bị tấn công do sử dụng mật khẩu yếu
     */
    public static boolean isValidPassword(String password) {
        if (password == null || password.length() < 6) {
            return false;
        }
        return true;
    }
}
