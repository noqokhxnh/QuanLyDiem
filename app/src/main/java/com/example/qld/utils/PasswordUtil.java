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
     * @param password Mật khẩu dạng văn bản thường
     * @return Một chuỗi chứa salt và mật khẩu đã băm được phân tách bởi ':'
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
     * @param password Mật khẩu dạng văn bản thường
     * @param hashedPassword Mật khẩu đã băm trước đó (định dạng: salt:hashed)
     * @return true nếu mật khẩu khớp, false nếu không
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
     * Chuyển đổi chuỗi thập lục phân thành mảng byte
     * @param hexString Chuỗi thập lục phân
     * @return Biểu diễn mảng byte
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