package com.ucoruh.password;

import org.junit.*;
import java.io.*;

import static org.junit.Assert.*;

/**
 * Unit test for PlatformManager class.
 */
public class PlatformManagerTest {

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

    @Test
    public void testShowPlatforms() {
        PlatformManager.showPlatforms();
        String output = outContent.toString();
        assertTrue(output.contains("Windows"));
        assertTrue(output.contains("Linux"));
    }
}
