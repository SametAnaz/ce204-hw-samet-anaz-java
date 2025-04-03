package com.ucoruh.password;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.Scanner;

import static org.junit.Assert.*;

public class PasswordGeneratorTest {

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
    public void testGenerate() {
        // Arrange
        Scanner scanner = new Scanner(System.in);

        // Act
        PasswordGenerator.generate(scanner);
        String output = outContent.toString().trim();

        // Assert
        assertTrue("Output should contain 'Sifre:'", output.contains("Sifr:"));

        String[] parts = output.split("Şifre: ");
        assertEquals("Şifre çıktı formatı yanlış", 2, parts.length);

        String password = parts[1];
        assertEquals("Şifre 12 karakter olmalı", 12, password.length());

        String validChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxz0123456789!@#$%&";
        for (char c : password.toCharArray()) {
            assertTrue("Şifre geçersiz karakter içeriyor: " + c, validChars.contains(String.valueOf(c)));
        }
    }
    @Test
    public void testConstructorCoverage() {
        // 🔽 Sınıfın instance'ı oluşturularak constructor test edilir
        PasswordGenerator generator = new PasswordGenerator();
        assertNotNull(generator);  // Ek olarak null olmadığını da kontrol edelim
    }
}
