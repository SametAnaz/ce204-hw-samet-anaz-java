package com.ucoruh.password;

import static org.junit.Assert.assertTrue;
import java.util.Scanner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @brief Unit tests for the AuthManager class.
 */
public class AuthManagerTest {
    
    private AuthManager auth;
    
    /**
     * Setup before each test.
     */
    @Before
    public void setUp() {
        // Removed deprecated SecurityManager references.
        auth = AuthManager.getInstance();
    }
    
    /**
     * Cleanup after each test.
     */
    @After
    public void tearDown() {
        // Additional teardown tasks, if necessary.
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
}