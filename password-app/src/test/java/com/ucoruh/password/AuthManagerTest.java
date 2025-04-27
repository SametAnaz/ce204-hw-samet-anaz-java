package com.ucoruh.password;

import static org.junit.Assert.*;
import java.util.Scanner;
import java.io.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @brief Unit tests for the AuthManager class.
 */
public class AuthManagerTest {
    
    private AuthManager auth;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    
    /**
     * Setup before each test.
     */
    @Before
    public void setUp() {
        // Remove existing master password file for clean tests
        File file = new File("master-password.txt");
        if (file.exists()) {
            file.delete();
        }
        
        AuthManager.resetInstance();
        auth = AuthManager.getInstance();
        System.setOut(new PrintStream(outContent));
    }
    
    /**
     * Cleanup after each test.
     */
    @After
    public void tearDown() {
        System.setOut(originalOut);
        AuthManager.resetInstance();
        
        // Clean up test file after tests
        File file = new File("master-password.txt");
        if (file.exists()) {
            file.delete();
        }
    }
    
    /**
     * Tests creation of master password and login functionality.
     */
    @Test
    public void testCreateAndLogin() {
        // Simulate user input: first setting then verifying the master password.
        String simulatedInput = "testPassword\ntestPassword\n";
        Scanner scanner = new Scanner(simulatedInput);
        
        auth.createMasterPassword(scanner);
        boolean loginSuccessful = auth.login(scanner);
        
        assertTrue("User should be able to login with the correct master password", loginSuccessful);
        scanner.close();
    }
    
    /**
     * Tests failed login with incorrect password.
     */
    @Test
    public void testLoginFailure() {
        // Set a master password
        Scanner setScanner = new Scanner("correctPassword\n");
        auth.createMasterPassword(setScanner);
        setScanner.close();
        
        // Try to login with wrong password
        Scanner loginScanner = new Scanner("wrongPassword\n");
        boolean loginResult = auth.login(loginScanner);
        loginScanner.close();
        
        assertFalse("Login should fail with incorrect password", loginResult);
    }
    
    /**
     * Tests isMasterPasswordSet method for false case.
     */
    @Test
    public void testIsMasterPasswordSetFalse() {
        // Force a clean state by deleting the file and resetting
        File file = new File("master-password.txt");
        if (file.exists()) {
            file.delete();
        }
        AuthManager.resetInstance();
        AuthManager freshAuth = AuthManager.getInstance();
        assertFalse("New AuthManager instance should not have master password set", freshAuth.isMasterPasswordSet());
    }
    
    /**
     * Tests isMasterPasswordSet method for true case.
     */
    @Test
    public void testIsMasterPasswordSetTrue() {
        // Set master password
        Scanner scanner = new Scanner("somePassword\n");
        auth.createMasterPassword(scanner);
        scanner.close();
        
        assertTrue("AuthManager should have master password set after createMasterPassword", auth.isMasterPasswordSet());
    }
    
    /**
     * Tests the resetInstance method.
     */
    @Test
    public void testResetInstance() {
        // Set master password in first instance
        Scanner scanner = new Scanner("testPassword\n");
        auth.createMasterPassword(scanner);
        scanner.close();
        
        // Reset instance and delete the file
        AuthManager.resetInstance();
        File file = new File("master-password.txt");
        if (file.exists()) {
            file.delete();
        }
        AuthManager newAuth = AuthManager.getInstance();
        
        assertFalse("New instance after reset should not have master password set", newAuth.isMasterPasswordSet());
    }
    
    /**
     * Tests getMasterPassword method.
     */
    @Test
    public void testGetMasterPassword() {
        String password = "secretMaster";
        Scanner scanner = new Scanner(password + "\n");
        auth.createMasterPassword(scanner);
        
        // Login to get the actual master password (not the hash)
        scanner = new Scanner(password + "\n");
        auth.login(scanner);
        scanner.close();
        
        assertEquals("getMasterPassword should return the correct master password", password, auth.getMasterPassword());
    }
    
    /**
     * Tests userMenu method output.
     */
    @Test
    public void testUserMenu() {
        // First set up the master password
        String password = "testPassword";
        Scanner setupScanner = new Scanner(password + "\n");
        auth.createMasterPassword(setupScanner);
        setupScanner.close();
        
        // Provide input for the menu: select option 0 (back to main menu)
        Scanner scanner = new Scanner("0\n");
        auth.userMenu(scanner);
        scanner.close();
        
        String output = outContent.toString();
        assertTrue("userMenu should output USER AUTHENTICATION MENU", 
                output.contains("USER AUTHENTICATION MENU"));
    }
}