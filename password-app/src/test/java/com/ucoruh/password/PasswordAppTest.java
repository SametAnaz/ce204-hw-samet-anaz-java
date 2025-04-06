package com.ucoruh.password;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class PasswordAppTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testMainSuccess() {
        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;

        String input = "0\n"; // simulate selecting exit
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        System.setIn(inputStream);
        System.setOut(new PrintStream(outputStream));

        String[] args = {};
        PasswordApp.main(args);

        System.setIn(originalIn);
        System.setOut(originalOut);

        String output = outputStream.toString();
        assertTrue(output.contains("Exiting"));
    }

    @Test
    public void testMainObject() {
        PasswordApp app = new PasswordApp();
        assertNotNull(app);
    }
    
    @Test
    public void testMenuNavigation() {
        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;

        // Simulate successful login + selecting each menu item + exit
        String input = "admin\nadmin\n3\n0\n"; // login success + Password Generator + exit
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        System.setIn(inputStream);
        System.setOut(new PrintStream(outputStream));

        String[] args = {};
        PasswordApp.main(args);

        System.setIn(originalIn);
        System.setOut(originalOut);

        String output = outputStream.toString();
        assertTrue(output.contains("MAIN MENU"));
        assertTrue(output.contains("Exiting"));
    }

    @Test
    public void testMainError() {
        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;

        String input = "x\n0\n"; // invalid input followed by exit
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        System.setIn(inputStream);
        System.setOut(new PrintStream(outputStream));

        String[] args = {};
        PasswordApp.main(args);

        System.setIn(originalIn);
        System.setOut(originalOut);

        String output = outputStream.toString();
        assertTrue(output.contains("Invalid"));
    }
}
