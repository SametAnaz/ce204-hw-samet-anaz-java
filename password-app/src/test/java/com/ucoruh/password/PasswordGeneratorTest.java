package com.ucoruh.password;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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

    @Test
    public void testGenerate() {
        Scanner scanner = new Scanner("12\ntrue\ntrue\ntrue\ntrue\n");
        PasswordGenerator.generate(scanner);
        String output = outputStream.toString();
        assertTrue(output.toLowerCase().contains("password") || output.contains(":"));
    }

    @Test
    public void testGenerateWithScanner() {
        String input = "12\ntrue\ntrue\ntrue\nfalse\n";
        Scanner scanner = new Scanner(input);
        PasswordGenerator.generate(scanner);
        String output = outputStream.toString();
        assertFalse(output.isEmpty());
    }
}
