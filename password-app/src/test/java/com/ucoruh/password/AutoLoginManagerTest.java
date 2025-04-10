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
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        AutoLoginManager.menu(new Scanner(System.in)); 
        // veya PasswordApp.runApp(...) içinde "4" seçerek AutoLoginManager.menu çağrısını tetiklemek

        System.setOut(originalOut);
        String output = outputStream.toString();

        // Beklenen metin, AutoLoginManager.menu() içindeki çıktıyla aynı olmalı
        assertTrue("Output should mention auto-login", output.contains("Auto-Login Feature activated."));
    }

}
