package com.ucoruh.password;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
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
 * Additional tests cover each main menu branch.
 */
public class PasswordAppTest {

    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outputStream;

    /**
     * Reset the AuthManager singleton and set up a new output stream.
     */
    @Before
    public void setUp() {
        // Delete the master password file to ensure clean state
        File file = new File("master-password.txt");
        if (file.exists()) {
            file.delete();
        }
        
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
        
        // Clean up the master password file
        File file = new File("master-password.txt");
        if (file.exists()) {
            file.delete();
        }
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
        String simulatedInput = "testMaster\n" + "testMaster\n" + "0\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(
                simulatedInput.getBytes(StandardCharsets.UTF_8));
        Scanner scanner = new Scanner(inputStream);

        PasswordApp.runApp(scanner, System.out);
        scanner.close();

        String output = outputStream.toString();
        assertTrue("Output should contain MAIN MENU", output.contains("MAIN MENU"));
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
        String simulatedInput = "testMaster\n" + "wrongMaster\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(
                simulatedInput.getBytes(StandardCharsets.UTF_8));
        Scanner scanner = new Scanner(inputStream);

        PasswordApp.runApp(scanner, System.out);
        scanner.close();

        String output = outputStream.toString();
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
        String simulatedInput = "testMaster\n" + "testMaster\n" + "0\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(
                simulatedInput.getBytes(StandardCharsets.UTF_8));
        Scanner scanner = new Scanner(inputStream);

        PasswordApp.runApp(scanner, System.out);
        scanner.close();

        String output = outputStream.toString();
        assertTrue("Output should contain MAIN MENU", output.contains("MAIN MENU"));
    }

    /**
     * @brief Tests main menu option 1: User Authentication.
     *
     * Simulates choosing option 1 and verifies that the user menu output is shown.
     */
    @Test
    public void testMenuOptionUserAuthentication() {
        // Simulated input: set master, login, then choose option 1, then exit.
        String simulatedInput = "testMaster\n" + "testMaster\n" + "1\n" + "0\n" + "0\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(
                simulatedInput.getBytes(StandardCharsets.UTF_8));
        Scanner scanner = new Scanner(inputStream);

        PasswordApp.runApp(scanner, System.out);
        scanner.close();

        String output = outputStream.toString();
        assertTrue("Output should indicate user menu", output.contains("USER AUTHENTICATION MENU"));
    }

    /**
     * @brief Tests main menu option 2: Secure Storage of Passwords.
     *
     * Simulates choosing option 2 and then immediately exiting the inner PasswordManager menu.
     */
    @Test
    public void testMenuOptionSecureStorage() {
        // Simulated input: set master, login, choose option 2, then in inner menu choose 4 to exit, then exit main menu.
        String simulatedInput = "testMaster\n" + "testMaster\n" + "2\n" + "4\n" + "0\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(
                simulatedInput.getBytes(StandardCharsets.UTF_8));
        Scanner scanner = new Scanner(inputStream);

        PasswordApp.runApp(scanner, System.out);
        scanner.close();

        String output = outputStream.toString();
        // Since PasswordManager.menu prints its own menu header, verify its presence.
        assertTrue("Output should contain inner PasswordManager menu", output.contains("PASSWORD MANAGER MENU"));
    }

    /**
     * @brief Tests main menu option 3: Password Generator.
     *
     * Simulates choosing option 3 and entering a desired password length.
     * Verifies that the generated password output is present.
     */
    @Test
    public void testMenuOptionPasswordGenerator() {
        // Simulated input: set master, login, choose option 3, input desired length, then exit.
        String simulatedInput = "testMaster\n" + "testMaster\n" + "3\n" + "8\n" + "0\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(
                simulatedInput.getBytes(StandardCharsets.UTF_8));
        Scanner scanner = new Scanner(inputStream);

        PasswordApp.runApp(scanner, System.out);
        scanner.close();

        String output = outputStream.toString();
        // Expect the password generator to output "Generated Password:".
        assertTrue("Output should contain Generated Password:", output.contains("Generated Password:"));
    }

    /**
     * @brief Tests main menu option 4: Auto-Login Feature.
     *
     * Simulates choosing option 4. Verifies the Auto-Login menu appears.
     */
    @Test
    public void testMenuOptionAutoLoginFeature() {
        // Simulated input: set master, login, choose option 4, then exit.
        String simulatedInput = "testMaster\n" + "testMaster\n" + "4\n" + "0\n" + "0\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(
                simulatedInput.getBytes(StandardCharsets.UTF_8));
        Scanner scanner = new Scanner(inputStream);

        PasswordApp.runApp(scanner, System.out);
        scanner.close();

        String output = outputStream.toString();
        // Check for expected menu heading
        assertTrue("Output should mention Auto-Login Feature", output.contains("AUTO-LOGIN FEATURES"));
    }

    /**
     * @brief Tests main menu option 5: Multi-Platform Compatibility.
     *
     * Simulates choosing option 5. Verifies platform compatibility menu appears.
     */
    @Test
    public void testMenuOptionMultiPlatform() {
        // Simulated input: set master, login, choose option 5, press Enter to continue, then exit.
        String simulatedInput = "testMaster\n" + "testMaster\n" + "5\n" + "\n" + "0\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(
                simulatedInput.getBytes(StandardCharsets.UTF_8));
        Scanner scanner = new Scanner(inputStream);

        PasswordApp.runApp(scanner, System.out);
        scanner.close();

        String output = outputStream.toString();
        // Check for expected heading in the platform menu
        assertTrue("Output should mention Supported platforms", output.contains("PLATFORM COMPATIBILITY"));
    }
}
