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

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    /**
     * @brief Tests that the menu method produces the expected output message.
     *
     * This test calls the menu method directly and verifies that it contains
     * the expected "activated" message.
     */
    @Test
    public void testMenuOutput() {
        AutoLoginManager.menu(new Scanner(System.in)); 
        
        String output = outContent.toString();
        assertTrue("Output should mention auto-login activation", 
                  output.contains("Auto-Login Feature activated."));
    }
    
    /**
     * @brief Tests menu method with different scanner input.
     *
     * Verifies that the method works correctly with custom scanner input.
     */
    @Test
    public void testMenuWithCustomInput() {
        // Provide some input, though it's currently not used by the method
        Scanner scanner = new Scanner("any input here\n");
        
        AutoLoginManager.menu(scanner);
        
        String output = outContent.toString();
        assertTrue("Output should contain the activation message regardless of input", 
                  output.contains("Auto-Login Feature activated."));
    }
}
