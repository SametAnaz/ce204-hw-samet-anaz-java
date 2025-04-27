package com.ucoruh.password;

import java.util.Scanner;
import org.junit.*;
import java.io.*;

import static org.junit.Assert.*;

/**
 * @brief Unit tests for the AutoLoginManager class.
 *
 * Verifies that the AutoLoginManager's menu method produces the expected output.
 */
public class AutoLoginManagerTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private PasswordManager passwordManager;

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        passwordManager = new PasswordManager("test-master-password");
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    /**
     * @brief Tests the menu method with a basic input.
     *
     * This test provides input to select option 0 (back to main menu) and verifies
     * that the menu is displayed correctly.
     */
    @Test
    public void testMenuBasicNavigation() {
        // Simulate input to navigate back to main menu
        String input = "0\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        
        AutoLoginManager.menu(scanner, passwordManager);
        
        String output = outContent.toString();
        assertTrue("Output should contain auto-login menu header", 
                  output.contains("AUTO-LOGIN FEATURES"));
        assertTrue("Output should contain option to enable auto-login",
                  output.contains("Enable Auto-Login"));
    }
    
    /**
     * @brief Tests showing services with auto-login enabled when none exist.
     *
     * Simulates selecting option 3 (show services) and verifies the output
     * indicates no services have auto-login enabled.
     */
    @Test
    public void testShowServicesEmpty() {
        // Simulate input to select option 3 (show services) then 0 (back)
        String input = "3\n0\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        
        AutoLoginManager.menu(scanner, passwordManager);
        
        String output = outContent.toString();
        assertTrue("Output should indicate no services have auto-login enabled", 
                  output.contains("None"));
    }
}
