package com.ucoruh.password;

import static com.github.stefanbirkner.systemlambda.SystemLambda.tapSystemOut;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * @brief Unit tests for the PlatformManager class using SystemLambda.
 *
 * This test captures the output of the showPlatforms() static method and 
 * compares it with the expected string.
 */
public class PlatformManagerTest {

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
}
