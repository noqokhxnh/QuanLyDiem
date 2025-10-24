package com.example.qld.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Lớp tiện ích để xử lý mật khẩu
 * Bao gồm hàm băm mật khẩu và xác thực mật khẩu
 */
public class PasswordUtil {

    private static final String HASH_ALGORITHM = "SHA-256";
    private static final int SALT_LENGTH = 32; // 32 bytes = 256 bits

    /**
     * Băm mật khẩu với salt được tạo ngẫu nhiên
     * 
     * Cách thức hoạt động:
     * 1. Tạo một chuỗi salt ngẫu nhiên có độ dài 32 byte
     * 2. Kết hợp mật khẩu với salt và băm bằng thuật toán SHA-256
     * 3. Chuyển đổi salt và mật khẩu đã băm sang định dạng hex
     * 4. Trả về chuỗi theo định dạng "salt:hashed_password"
     * 
     * @param password Mật khẩu dạng văn bản thường (chuỗi không được null hoặc rỗng)
     * @return Một chuỗi chứa salt và mật khẩu đã băm được phân tách bởi ':'
     *         Định dạng: "hex_salt:hex_hashed_password" (vd: "a1b2c3...:d4e5f6...")
     * @throws RuntimeException Nếu thuật toán băm không được hỗ trợ
     * 
     * Ví dụ: Nếu password là "mật khẩu", phương thức sẽ trả về một chuỗi như:
     * "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8:f7c3bc1d808e04732adf679965ccc34ca7ae3441"
     */
    public static String hashPassword(String password) {
        try {
            // Generate a random salt
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[SALT_LENGTH];
            random.nextBytes(salt);

            // Hash the password with the salt
            MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
            digest.update(salt);
            byte[] hashedPassword = digest.digest(password.getBytes());

            // Combine salt and hashed password
            StringBuilder sb = new StringBuilder();
            for (byte b : salt) {
                sb.append(String.format("%02x", b));
            }
            sb.append(":");
            for (byte b : hashedPassword) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    /**
     * Xác thực mật khẩu với mật khẩu đã băm
     * 
     * Cách thức hoạt động:
     * 1. Tách chuỗi mật khẩu đã băm thành salt và hash (dựa vào dấu ':')
     * 2. Băm lại mật khẩu do người dùng cung cấp sử dụng cùng salt
     * 3. So sánh hash kết quả với hash đã lưu trữ
     * 4. Trả về kết quả so sánh
     * 
     * @param password Mật khẩu dạng văn bản thường do người dùng nhập (chuỗi không được null)
     * @param hashedPassword Mật khẩu đã băm trước đó lưu trong cơ sở dữ liệu (định dạng: "salt:hashed", chuỗi không được null)
     * @return true nếu mật khẩu khớp (hash trùng nhau), false nếu không khớp hoặc có lỗi
     * 
     * Ví dụ: Nếu password là "mật khẩu" và hashedPassword là "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8:f7c3bc1d808e04732adf679965ccc34ca7ae3441"
     * thì phương thức sẽ trả về true nếu mật khẩu khớp, false nếu không khớp
     */
    public static boolean verifyPassword(String password, String hashedPassword) {
        if (password == null || hashedPassword == null) {
            return false;
        }

        try {
            // Split the stored hashed password to get the salt and hash
            String[] parts = hashedPassword.split(":");
            if (parts.length != 2) {
                return false; // Invalid format
            }

            // Extract salt and hash
            byte[] salt = hexStringToByteArray(parts[0]);
            byte[] storedHash = hexStringToByteArray(parts[1]);

            // Hash the provided password with the stored salt
            MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
            digest.update(salt);
            byte[] providedHash = digest.digest(password.getBytes());

            // Compare the hashes
            return MessageDigest.isEqual(storedHash, providedHash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error verifying password", e);
        }
    }

    /**
     * Chuyển đổi chuỗi thập lục phân (hex) thành mảng byte
     * 
     * Cách thức hoạt động:
     * 1. Chia chuỗi hex thành từng cặp ký tự (mỗi cặp đại diện cho 1 byte)
     * 2. Chuyển đổi từng cặp ký tự thành giá trị số nguyên
     * 3. Chuyển đổi giá trị số nguyên thành byte
     * 4. Trả về mảng byte kết quả
     * 
     * @param hexString Chuỗi thập lục phân (chứa các ký tự 0-9, a-f, A-F, độ dài phải chẵn)
     *                  Ví dụ: "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8"
     * @return Biểu diễn mảng byte của chuỗi hex, hoặc mảng rỗng nếu chuỗi rỗng
     * 
     * Ví dụ: Nếu hexString là "616263", phương thức sẽ trả về mảng byte tương ứng với chuỗi "abc"
     */
    private static byte[] hexStringToByteArray(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i+1), 16));
        }
        return data;
    }
}