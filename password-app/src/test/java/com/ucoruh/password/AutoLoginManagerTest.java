package com.ucoruh.password;

import java.util.Scanner;
import org.junit.*;
import java.io.*;

import static org.junit.Assert.*;

/**
 * Unit test for AutoLoginManager class.
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

    @Test
    public void testMenuOutput() {
        String input = "1\n"; // simulate "Enable Auto-Login"
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Scanner scanner = new Scanner(System.in);

        AutoLoginManager.menu(scanner);

        String output = outContent.toString();
        assertTrue(output.contains("Auto-login enabled."));
    }
}
