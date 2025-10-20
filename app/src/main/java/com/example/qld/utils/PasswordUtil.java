package com.example.qld.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class PasswordUtil {

    private static final String HASH_ALGORITHM = "SHA-256";
    private static final int SALT_LENGTH = 32; // 32 bytes = 256 bits

    /**
     * Hashes a password with a randomly generated salt
     * @param password The plain text password
     * @return A string containing the salt and hashed password separated by ':'
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
     * Verifies a password against a hashed password
     * @param password The plain text password
     * @param hashedPassword The previously hashed password (format: salt:hashed)
     * @return true if the password matches, false otherwise
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
     * Converts a hexadecimal string to a byte array
     * @param hexString The hexadecimal string
     * @return The byte array representation
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