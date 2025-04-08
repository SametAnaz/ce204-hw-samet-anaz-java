package com.ucoruh.password;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import org.junit.Test;

/**
 * @brief Unit tests for the PasswordManager class.
 *
 * These tests cover non-interactive credential management, the interactive menu,
 * the Generate Password (case "3"), and the main() method via runApp.
 */
public class PasswordManagerTest {

    /**
     * Tests that credentials are added and retrieved correctly.
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
     * Tests the interactive menu by simulating user input for add and retrieve actions.
     */
    @Test
    public void testMenuInteractive() {
        // Simulated input:
        // Option "1": add credential: account "account1", password "password1"
        // Option "2": retrieve credential: account "account1"
        // Option "4": exit.
        String simulatedInput = "1\naccount1\npassword1\n2\naccount1\n4\n";
        ByteArrayInputStream testInput = new ByteArrayInputStream(simulatedInput.getBytes(StandardCharsets.UTF_8));
        Scanner scanner = new Scanner(testInput);
        
        ByteArrayOutputStream testOutput = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(testOutput);
        
        PasswordManager pm = new PasswordManager("dummyMaster");
        pm.menu(scanner, printStream);
        scanner.close();
        
        String output = testOutput.toString();
        assertTrue(output.contains("Enter account name:"));
        assertTrue(output.contains("Credential added."));
        assertTrue(output.contains("Password: password1"));
    }

    /**
     * Tests the interactive menu for handling invalid options.
     */
    @Test
    public void testMenuInvalidOption() {
        // Simulated input: an invalid option then exit.
        String simulatedInput = "invalid\n4\n";
        ByteArrayInputStream testInput = new ByteArrayInputStream(simulatedInput.getBytes(StandardCharsets.UTF_8));
        Scanner scanner = new Scanner(testInput);
        
        ByteArrayOutputStream testOutput = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(testOutput);
        
        PasswordManager pm = new PasswordManager("dummyMaster");
        pm.menu(scanner, printStream);
        scanner.close();
        
        String output = testOutput.toString();
        assertTrue(output.contains("Invalid option."));
    }

    /**
     * Tests the Generate Password functionality (case "3") in the interactive menu.
     */
    @Test
    public void testMenuCase3() {
        // Simulated input:
        // Master password: "dummy"
        // Option "3" for Generate Password, then length "8", then option "4" to exit.
        String simulatedInput = "dummy\n3\n8\n4\n";
        ByteArrayInputStream inStream = new ByteArrayInputStream(simulatedInput.getBytes(StandardCharsets.UTF_8));
        Scanner scanner = new Scanner(inStream);
        
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outStream);
        
        // Use runApp() to include the master password prompt.
        PasswordManager.runApp(scanner, printStream);
        scanner.close();
        
        String output = outStream.toString();
        assertTrue("Output should contain Generated Password:", output.contains("Generated Password:"));
    }

    /**
     * Tests the main method functionality via the runApp() method.
     */
    @Test
    public void testMainMethod() {
        // Simulated input:
        // Master password: "dummy"
        // Then option "4" to exit.
        String simulatedInput = "dummy\n4\n";
        ByteArrayInputStream inStream = new ByteArrayInputStream(simulatedInput.getBytes(StandardCharsets.UTF_8));
        Scanner scanner = new Scanner(inStream);
        
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outStream);
        
        // Instead of calling main(), use runApp() directly.
        PasswordManager.runApp(scanner, printStream);
        scanner.close();
        
        String output = outStream.toString();
        // Verify the output contains the initial prompt and a menu element.
        assertTrue("Output should contain 'Enter master password:'", output.contains("Enter master password:"));
        assertTrue("Output should contain '1. Add Credential'", output.contains("1. Add Credential"));
    }
}
