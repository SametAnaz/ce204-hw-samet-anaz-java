package com.ucoruh.password;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import org.junit.Test;

/**
 * @brief Unit tests for the PasswordManager class.
 *
 * These tests cover non-interactive credential management and the interactive menu functionality.
 */
public class PasswordManagerTest {

    /**
     * @brief Tests that credentials are added and retrieved correctly.
     *
     * Verifies that addCredential stores the password and getCredential retrieves it;
     * also checks that a non-existent account returns null.
     */
    @Test
    public void testAddAndGetCredential() {
        PasswordManager pm = new PasswordManager("dummyMaster");
        pm.addCredential("testAccount", "testPassword");
        
        // Check valid retrieval.
        assertEquals("testPassword", pm.getCredential("testAccount"));
        
        // Verify retrieval of non-existent account returns null.
        assertNull(pm.getCredential("nonExistingAccount"));
    }

    /**
     * @brief Tests the interactive menu by simulating user input.
     *
     * This test simulates adding a credential and then retrieving it
     * by providing a sequence of inputs through a ByteArrayInputStream.
     * Output is captured via a ByteArrayOutputStream and verified.
     */
    @Test
    public void testMenuInteractive() {
        // Simulated input:
        // 1 -> Choose Add Credential
        // "account1" -> Account name
        // "password1" -> Password
        // 2 -> Choose Retrieve Credential
        // "account1" -> Account name for retrieval
        // 4 -> Exit
        String simulatedInput = "1\naccount1\npassword1\n2\naccount1\n4\n";
        ByteArrayInputStream testInput = new ByteArrayInputStream(simulatedInput.getBytes(StandardCharsets.UTF_8));
        Scanner scanner = new Scanner(testInput);
        
        ByteArrayOutputStream testOutput = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(testOutput);
        
        PasswordManager pm = new PasswordManager("dummyMaster");
        pm.menu(scanner, printStream);
        scanner.close();
        
        String output = testOutput.toString();
        // Check that output contains prompts and messages for added credential.
        assertTrue(output.contains("Enter account name:"));
        assertTrue(output.contains("Credential added."));
        assertTrue(output.contains("Password: password1"));
    }

    /**
     * @brief Tests the interactive menu for handling invalid options.
     *
     * This test simulates an invalid menu option followed by exit,
     * and verifies that the output contains the "Invalid option" message.
     */
    @Test
    public void testMenuInvalidOption() {
        // Simulated input: "invalid" (an invalid menu option) then "4" to exit.
        String simulatedInput = "invalid\n4\n";
        ByteArrayInputStream testInput = new ByteArrayInputStream(simulatedInput.getBytes(StandardCharsets.UTF_8));
        Scanner scanner = new Scanner(testInput);
        
        ByteArrayOutputStream testOutput = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(testOutput);
        
        PasswordManager pm = new PasswordManager("dummyMaster");
        pm.menu(scanner, printStream);
        scanner.close();
        
        String output = testOutput.toString();
        // Verify that the "Invalid option." message is present.
        assertTrue(output.contains("Invalid option."));
    }
}
