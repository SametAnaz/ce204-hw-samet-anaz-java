package com.ucoruh.password;

import static com.github.stefanbirkner.systemlambda.SystemLambda.tapSystemOut;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @brief Unit tests for the PlatformManager class using SystemLambda.
 *
 * This test suite verifies the functionality of the PlatformManager class
 * including output capture and platform compatibility checking.
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
    
    /**
     * @brief Tests isPlatformSupported() method for a supported platform.
     */
    @Test
    public void testIsPlatformSupportedWithSupportedPlatform() {
        boolean result = PlatformManager.isPlatformSupported("Windows");
        assertTrue("Windows should be a supported platform", result);
    }
    
    /**
     * @brief Tests isPlatformSupported() method for an unsupported platform.
     */
    @Test
    public void testIsPlatformSupportedWithUnsupportedPlatform() {
        boolean result = PlatformManager.isPlatformSupported("BeOS");
        assertFalse("BeOS should not be a supported platform", result);
    }
    
    /**
     * @brief Tests isPlatformSupported() method with case-sensitive check.
     */
    @Test
    public void testIsPlatformSupportedCaseSensitive() {
        boolean result = PlatformManager.isPlatformSupported("windows");
        assertFalse("'windows' (lowercase) should not match 'Windows'", result);
    }
    
    /**
     * @brief Tests getCurrentPlatform() returns a non-null value.
     */
    @Test
    public void testGetCurrentPlatform() {
        String platform = PlatformManager.getCurrentPlatform();
        assertNotNull("Current platform should not be null", platform);
    }
    
    /**
     * @brief Tests getCurrentPlatform() returns a valid platform name.
     */
    @Test
    public void testGetCurrentPlatformValidity() {
        String platform = PlatformManager.getCurrentPlatform();
        assertTrue("Current platform should be one of the expected values or 'Unknown'",
                  platform.equals("Windows") || 
                  platform.equals("macOS") || 
                  platform.equals("Linux") || 
                  platform.equals("Unknown"));
    }
    
    /**
     * @brief Tests getNumberOfSupportedPlatforms() returns the expected count.
     */
    @Test
    public void testGetNumberOfSupportedPlatforms() {
        int count = PlatformManager.getNumberOfSupportedPlatforms();
        assertEquals("There should be 5 supported platforms", 5, count);
    }
}
