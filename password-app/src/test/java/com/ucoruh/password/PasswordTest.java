package com.ucoruh.password;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Unit tests for the Password class.
 */
public class PasswordTest {

    @Test
    public void testConstructorAndGetters() {
        Password password = new Password("gmail", "user1", "pass123");
        assertEquals("gmail", password.getService());
        assertEquals("user1", password.getUsername());
        assertEquals("pass123", password.getPassword());
    }

    @Test
    public void testSetters() {
        Password password = new Password("service", "user", "pass");
        password.setUsername("newuser");
        password.setPassword("newpass");

        assertEquals("newuser", password.getUsername());
        assertEquals("newpass", password.getPassword());
    }

    @Test
    public void testToString() {
        Password password = new Password("github", "dev", "secure");
        String result = password.toString();
        assertTrue(result.contains("github"));
        assertTrue(result.contains("dev"));
        assertTrue(result.contains("secure"));
    }
}
