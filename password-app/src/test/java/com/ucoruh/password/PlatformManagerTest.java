package com.ucoruh.password;

import static com.github.stefanbirkner.systemlambda.SystemLambda.tapSystemOut;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @brief Unit tests for the PlatformManager class using SystemLambda.
 *
 * This test captures the output of the showPlatforms() static method and 
 * compares it with the expected string.
 */
public class PlatformManagerTest {

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
     * @brief Tests that showPlatforms() prints the correct supported platforms message.
     *
     * Uses SystemLambda's tapSystemOut() to capture System.out output.
     */
    @Test
    public void testShowPlatformsUsingSystemLambda() throws Exception {
        String output = tapSystemOut(() -> PlatformManager.showPlatforms());
        String expected = "Supported platforms: Windows, macOS, Linux, Android, iOS";
        assertEquals("The output of showPlatforms() should match the expected text", expected, output.trim());
    }
    
    /**
     * @brief Tests showPlatforms() output using standard System.out capture.
     *
     * This test uses the setUp() and tearDown() methods to capture and verify
     * the output of the method without using external libraries.
     */
    @Test
    public void testShowPlatformsStandardCapture() {
        PlatformManager.showPlatforms();
        
        String output = outputStream.toString().trim();
        assertTrue(output.contains("Supported platforms"));
        assertTrue(output.contains("Windows"));
        assertTrue(output.contains("macOS"));
        assertTrue(output.contains("Linux"));
        assertTrue(output.contains("Android"));
        assertTrue(output.contains("iOS"));
    }
}
