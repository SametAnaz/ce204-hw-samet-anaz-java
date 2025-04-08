package com.ucoruh.password;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @brief Unit tests for the PlatformManager class.
 *
 * Verifies that the showPlatforms() method outputs the expected supported platforms message.
 */
public class PlatformManagerTest {

    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outContent;

    /**
     * Set up the output stream to capture console output.
     */
    @Before
    public void setUp() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    /**
     * Restore the original System.out after each test.
     */
    @After
    public void tearDown() {
        System.setOut(originalOut);
    }

    /**
     * @brief Tests that showPlatforms() prints the correct supported platforms message.
     */
    @Test
    public void testShowPlatforms() {
        PlatformManager.showPlatforms();
        String expected = "Supported platforms: Windows, macOS, Linux, Android, iOS";
        String output = outContent.toString().trim();
        assertEquals("The output of showPlatforms() should match the expected text", expected, output);
    }
}
