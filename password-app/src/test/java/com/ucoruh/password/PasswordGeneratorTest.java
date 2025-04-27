package com.ucoruh.password;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @brief Unit tests for the PasswordGenerator class.
 */
public class PasswordGeneratorTest {

    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @After
    public void tearDown() {
        System.setOut(originalOut);
    }

    /**
     * Tests the generate method with user input for length.
     */
    @Test
    public void testGenerate() {
        Scanner scanner = new Scanner("12\n");
        PasswordGenerator.generate(scanner);
        String output = outputStream.toString();
        
        assertTrue(output.contains("Enter desired password length"));
        assertTrue(output.contains("Generated Password:"));
    }

    /**
     * Tests the interactive generate method with explicit Scanner input.
     */
    @Test
    public void testGenerateWithScanner() {
        String input = "8\n";
        Scanner scanner = new Scanner(input);
        PasswordGenerator.generate(scanner);
        String output = outputStream.toString();
        
        assertFalse(output.isEmpty());
        assertTrue(output.contains("Generated Password:"));
    }
    
    /**
     * Tests that generatePassword creates passwords of the requested length.
     */
    @Test
    public void testGeneratePasswordLength() {
        // Test different lengths
        int[] testLengths = {5, 10, 15, 20};
        
        for (int length : testLengths) {
            String password = PasswordGenerator.generatePassword(length);
            assertEquals("Generated password should be of requested length", length, password.length());
        }
    }
    
    /**
     * Tests that generatePassword creates different passwords on consecutive calls.
     */
    @Test
    public void testGeneratePasswordUniqueness() {
        int length = 10;
        String password1 = PasswordGenerator.generatePassword(length);
        String password2 = PasswordGenerator.generatePassword(length);
        String password3 = PasswordGenerator.generatePassword(length);
        
        // Check all are different (this is statistical, but highly likely with secure RNG)
        assertFalse("Consecutive passwords should be different", 
                   password1.equals(password2) && password2.equals(password3));
    }
    
    /**
     * Tests that generatePassword creates passwords with expected character classes.
     */
    @Test
    public void testGeneratePasswordCharacterClasses() {
        // Generate a reasonably long password to expect all character classes
        String password = PasswordGenerator.generatePassword(100);
        
        // Check for presence of different character classes
        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;
        
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUppercase = true;
            else if (Character.isLowerCase(c)) hasLowercase = true;
            else if (Character.isDigit(c)) hasDigit = true;
            else hasSpecial = true;
        }
        
        assertTrue("Password should contain uppercase letters", hasUppercase);
        assertTrue("Password should contain lowercase letters", hasLowercase);
        assertTrue("Password should contain digits", hasDigit);
        assertTrue("Password should contain special characters", hasSpecial);
    }
    
    /**
     * Tests handling of zero-length password requests.
     */
    @Test
    public void testGeneratePasswordZeroLength() {
        String password = PasswordGenerator.generatePassword(0);
        assertEquals("Zero-length password should be empty string", 0, password.length());
    }
    
    /**
     * Tests handling of negative-length password requests.
     */
    @Test
    public void testGeneratePasswordNegativeLength() {
        String password = PasswordGenerator.generatePassword(-5);
        assertEquals("Negative-length password should be empty string", 0, password.length());
    }
    
    /**
     * Tests that generate method handles number format exception gracefully.
     */
    @Test
    public void testGenerateWithInvalidInput() {
        // Simulate invalid input (non-numeric)
        String input = "not-a-number\n";
        Scanner scanner = new Scanner(input);
        
        // This should not throw an exception now, as it's caught internally
        PasswordGenerator.generate(scanner);
        
        String output = outputStream.toString();
        assertTrue("Output should contain error message", 
                   output.contains("Invalid input") || output.contains("valid number"));
    }
}
