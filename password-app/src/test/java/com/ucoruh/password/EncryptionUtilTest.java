package com.ucoruh.password;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * @brief Test class for EncryptionUtil.
 * 
 * This class contains unit tests for the encryption and decryption functionality.
 */
public class EncryptionUtilTest {
    
    /**
     * @brief Tests the encryption and decryption functionality.
     * 
     * This test verifies that a string encrypted with a master password
     * can be correctly decrypted back to the original string.
     */
    @Test
    public void testEncryptDecrypt() throws Exception {
        // Arrange
        String originalData = "test-data-123";
        String masterPassword = "master-password-123";
        
        // Act
        String encrypted = EncryptionUtil.encrypt(originalData, masterPassword);
        String decrypted = EncryptionUtil.decrypt(encrypted, masterPassword);
        
        // Assert
        assertNotEquals("Encrypted data should be different from original", originalData, encrypted);
        assertEquals("Decrypted data should match original", originalData, decrypted);
    }
    
    /**
     * @brief Tests that different master passwords produce different encrypted results.
     */
    @Test
    public void testDifferentMasterPasswords() throws Exception {
        // Arrange
        String data = "test-data-123";
        String masterPassword1 = "master-password-1";
        String masterPassword2 = "master-password-2";
        
        // Act
        String encrypted1 = EncryptionUtil.encrypt(data, masterPassword1);
        String encrypted2 = EncryptionUtil.encrypt(data, masterPassword2);
        
        // Assert
        assertNotEquals("Different master passwords should produce different encrypted results", encrypted1, encrypted2);
    }
    
    /**
     * @brief Tests that the same data with the same master password produces the same encrypted result.
     */
    @Test
    public void testConsistentEncryption() throws Exception {
        // Arrange
        String data = "test-data-123";
        String masterPassword = "master-password-123";
        
        // Act
        String encrypted1 = EncryptionUtil.encrypt(data, masterPassword);
        String encrypted2 = EncryptionUtil.encrypt(data, masterPassword);
        
        // Assert
        assertEquals("Same data and master password should produce the same encrypted result", encrypted1, encrypted2);
    }
    
    /**
     * @brief Tests that wrong master password fails to decrypt correctly.
     */
    @Test
    public void testWrongMasterPassword() throws Exception {
        // Arrange
        String data = "test-data-123";
        String correctMasterPassword = "correct-password";
        String wrongMasterPassword = "wrong-password";
        
        // Act
        String encrypted = EncryptionUtil.encrypt(data, correctMasterPassword);
        
        // Assert
        try {
            EncryptionUtil.decrypt(encrypted, wrongMasterPassword);
            fail("Decryption with wrong master password should throw an exception");
        } catch (Exception e) {
            // Expected exception
        }
    }
    
    /**
     * @brief Tests the hash functionality.
     */
    @Test
    public void testHashString() {
        // Arrange
        String input = "password123";
        
        // Act
        String hash1 = EncryptionUtil.hashString(input);
        String hash2 = EncryptionUtil.hashString(input);
        
        // Assert
        assertNotNull("Hash should not be null", hash1);
        assertNotEquals("Hash should be different from input", input, hash1);
        assertEquals("Same input should produce the same hash", hash1, hash2);
        assertEquals("SHA-256 hash should be 64 characters long (hex string)", 64, hash1.length());
    }
    
    /**
     * @brief Tests empty string encryption and decryption.
     */
    @Test
    public void testEmptyStringEncryptDecrypt() throws Exception {
        // Arrange
        String originalData = "";
        String masterPassword = "master-password-123";
        
        // Act
        String encrypted = EncryptionUtil.encrypt(originalData, masterPassword);
        String decrypted = EncryptionUtil.decrypt(encrypted, masterPassword);
        
        // Assert
        assertNotEquals("Encrypted empty string should not be empty", originalData, encrypted);
        assertEquals("Decrypted data should be empty string", originalData, decrypted);
    }
    
    /**
     * @brief Tests encryption with empty master password.
     */
    @Test
    public void testEmptyMasterPassword() throws Exception {
        // Arrange
        String originalData = "test-data-123";
        String masterPassword = "";
        
        // Act
        String encrypted = EncryptionUtil.encrypt(originalData, masterPassword);
        String decrypted = EncryptionUtil.decrypt(encrypted, masterPassword);
        
        // Assert
        assertNotEquals("Encrypted data should be different from original", originalData, encrypted);
        assertEquals("Decrypted data should match original", originalData, decrypted);
    }
    
    /**
     * @brief Tests hash of empty string.
     */
    @Test
    public void testHashEmptyString() {
        // Arrange
        String input = "";
        
        // Act
        String hash = EncryptionUtil.hashString(input);
        
        // Assert
        assertNotNull("Hash of empty string should not be null", hash);
        assertNotEquals("Hash should not be empty", "", hash);
        assertEquals("SHA-256 hash should be 64 characters long", 64, hash.length());
    }
} 