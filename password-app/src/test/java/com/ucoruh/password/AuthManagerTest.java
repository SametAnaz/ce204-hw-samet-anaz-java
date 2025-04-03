package com.ucoruh.password;

import org.junit.*;
import java.io.*;
import java.util.Scanner;

import static org.junit.Assert.*;

/**
 * Unit tests for the AuthManager Singleton class.
 */
public class AuthManagerTest {

    private static final String TEST_FILE = "master-password.txt";
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    private AuthManager auth;

    @Before
    public void setUp() throws Exception {
        System.setOut(new PrintStream(outContent));
        auth = AuthManager.getInstance();
        // Clean file before each test
        File f = new File(TEST_FILE);
        if (f.exists()) f.delete();
    }

    @After
    public void tearDown() throws Exception {
        System.setOut(originalOut);
        File f = new File(TEST_FILE);
        if (f.exists()) f.delete();
    }

    @Test
    public void testSingletonInstance() {
        AuthManager second = AuthManager.getInstance();
        assertSame(auth, second);
    }

    @Test
    public void testCreateMasterPassword() throws IOException {
        String input = "test123\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        auth.createMasterPassword(scanner);

        BufferedReader reader = new BufferedReader(new FileReader(TEST_FILE));
        String content = reader.readLine();
        reader.close();
        assertEquals("test123", content);
    }

    @Test
    public void testIsMasterPasswordSet() throws IOException {
        assertFalse(auth.isMasterPasswordSet());
        // simulate password creation
        FileWriter fw = new FileWriter(TEST_FILE);
        fw.write("abc");
        fw.close();
        assertTrue(auth.isMasterPasswordSet());
    }

    @Test
    public void testLoginSuccess() throws IOException {
        FileWriter fw = new FileWriter(TEST_FILE);
        fw.write("mypassword");
        fw.close();

        Scanner scanner = new Scanner(new ByteArrayInputStream("mypassword\n".getBytes()));
        assertTrue(auth.login(scanner));
    }

    @Test
    public void testLoginFailure() throws IOException {
        FileWriter fw = new FileWriter(TEST_FILE);
        fw.write("realpass");
        fw.close();

        Scanner scanner = new Scanner(new ByteArrayInputStream("wrongpass\n".getBytes()));
        assertFalse(auth.login(scanner));
    }

    @Test
    public void testUserMenuLogoutOption() {
        Scanner scanner = new Scanner(new ByteArrayInputStream("1\n".getBytes()));
        try {
            auth.userMenu(scanner);
            fail("System.exit should be called.");
        } catch (Exception e) {
            // expected behavior
        }
    }
}
