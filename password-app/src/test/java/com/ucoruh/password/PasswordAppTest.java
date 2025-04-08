package com.ucoruh.password;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @brief Unit tests for the PasswordApp class using the runApp method.
 *
 * These tests simulate full application flow by injecting input and capturing output.
 */
public class PasswordAppTest {
    
    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outputStream;

    /**
     * Reset the AuthManager singleton and set up a new output stream.
     */
    @Before
    public void setUp() {
        AuthManager.resetInstance();
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    /**
     * Restore the original System.out after each test.
     */
    @After
    public void tearDown() {
        System.setOut(originalOut);
        outputStream.reset();
    }

    /**
     * @brief Tests a successful run of the application.
     *
     * Simulates setting a master password, successful login, and then exiting
     * the main menu. Verifies that the output contains the main menu prompt and
     * an exit message.
     */
    @Test
    public void testMainSuccess() {
        // Input sequence:
        // "testMaster" - for setting the master password,
        // "testMaster" - for successful login,
        // "0"         - to exit the main menu.
        String simulatedInput = "testMaster\n" + "testMaster\n" + "0\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(
                simulatedInput.getBytes(StandardCharsets.UTF_8));
        Scanner scanner = new Scanner(inputStream);
        
        PasswordApp.runApp(scanner, System.out);
        scanner.close();
        
        String output = outputStream.toString();
        // Check that output contains the main menu prompt and exit message.
        assertTrue("Output should contain MAIN MENU", output.contains("==== MAIN MENU ===="));
        assertTrue("Output should contain Exiting...", output.contains("Exiting..."));
        assertFalse("Output should not contain 'Login failed.'", output.contains("Login failed."));
    }
    
    /**
     * @brief Tests the login failure scenario.
     *
     * Simulates setting a master password but then providing an incorrect
     * login input. Verifies that the output contains "Login failed."
     */
    @Test
    public void testMainError() {
        // Input sequence:
        // "testMaster" - for setting the master password,
        // "wrongMaster" - for an incorrect login attempt.
        String simulatedInput = "testMaster\n" + "wrongMaster\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(
                simulatedInput.getBytes(StandardCharsets.UTF_8));
        Scanner scanner = new Scanner(inputStream);
        
        PasswordApp.runApp(scanner, System.out);
        scanner.close();
        
        String output = outputStream.toString();
        // Verify that output includes the login failure message.
        assertTrue("Output should contain 'Login failed.'", output.contains("Login failed."));
    }
    
    /**
     * @brief Tests full menu navigation.
     *
     * Simulates a run of the application that displays the main menu.
     * Verifies that the captured output shows the menu header.
     */
    @Test
    public void testFullMenuNavigation() {
        // Input sequence:
        // "testMaster" - for setting the master password,
        // "testMaster" - for successful login,
        // "0"         - to exit directly.
        String simulatedInput = "testMaster\n" + "testMaster\n" + "0\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(
                simulatedInput.getBytes(StandardCharsets.UTF_8));
        Scanner scanner = new Scanner(inputStream);
        
        PasswordApp.runApp(scanner, System.out);
        scanner.close();
        
        String output = outputStream.toString();
        // Check that the output contains the main menu header.
        assertTrue("Output should contain MAIN MENU", output.contains("==== MAIN MENU ===="));
    }
}
