package com.example.qld.utils;

import org.junit.Test;
import static org.junit.Assert.*;

public class PasswordUtilTest {

    @Test
    public void testPasswordHashingAndVerification() {
        String password = "testPassword123";
        
        // Test that a password can be hashed
        String hashedPassword = PasswordUtil.hashPassword(password);
        assertNotNull("Hashed password should not be null", hashedPassword);
        assertTrue("Hashed password should contain a salt and hash separated by colon", hashedPassword.contains(":"));
        
        // Test that the password can be verified against the hash
        boolean isValid = PasswordUtil.verifyPassword(password, hashedPassword);
        assertTrue("Password should verify correctly against its hash", isValid);
        
        // Test that an incorrect password fails verification
        boolean isInvalid = PasswordUtil.verifyPassword("wrongPassword", hashedPassword);
        assertFalse("Incorrect password should not verify against the hash", isInvalid);
    }

    @Test
    public void testDifferentPasswordsHaveDifferentHashes() {
        String password1 = "password1";
        String password2 = "password2";
        
        String hash1 = PasswordUtil.hashPassword(password1);
        String hash2 = PasswordUtil.hashPassword(password2);
        
        assertNotEquals("Different passwords should have different hashes", hash1, hash2);
    }

    @Test
    public void testSamePasswordHasDifferentHashes() {
        // Due to random salt generation, the same password should have different hashes each time
        String password = "samePassword123";
        
        String hash1 = PasswordUtil.hashPassword(password);
        String hash2 = PasswordUtil.hashPassword(password);
        
        assertNotEquals("Same password should have different hashes due to random salt", hash1, hash2);
        
        // But both should verify correctly
        assertTrue("First hash should verify the password", PasswordUtil.verifyPassword(password, hash1));
        assertTrue("Second hash should verify the password", PasswordUtil.verifyPassword(password, hash2));
    }
}