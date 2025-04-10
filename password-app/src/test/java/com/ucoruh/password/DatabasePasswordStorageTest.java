package com.ucoruh.password;

import org.junit.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.junit.Assert.*;

/**
 * @brief Unit tests for the DatabasePasswordStorage class.
 *
 * These tests verify the functionality of writing, reading, adding,
 * updating, deleting, and viewing passwords using the SQLite-based storage.
 */
public class DatabasePasswordStorageTest {

    private static final String TEST_DB_PATH = "test-passwords.db";
    private static final String TEST_DB_URL = "jdbc:sqlite:" + TEST_DB_PATH;
    private DatabasePasswordStorage storage;

    /**
     * Anonymous subclass to override the database URL for testing.
     */
    private static class TestableDatabasePasswordStorage extends DatabasePasswordStorage {
        @Override
        protected String getDatabaseUrl() {
            return TEST_DB_URL;
        }
    }

    @Before
    public void setUp() {
        File dbFile = new File(TEST_DB_PATH);
        if (dbFile.exists()) dbFile.delete();
        storage = new TestableDatabasePasswordStorage();
    }

    /**
     * @brief Tests writeAll() and readAll() functionality.
     *
     * Verifies that writing a list of passwords and reading them back works as expected.
     */
    @Test
    public void testWriteAndReadAll() {
        List<Password> input = Arrays.asList(
            new Password("gmail", "user1", "pass1"),
            new Password("github", "user2", "pass2")
        );

        storage.writeAll(input);
        List<Password> result = storage.readAll();

        assertEquals(2, result.size());

        Password gmail = result.stream().filter(p -> p.getService().equals("gmail")).findFirst().orElse(null);
        assertNotNull(gmail);
        assertEquals("user1", gmail.getUsername());
        assertEquals("pass1", gmail.getPassword());

        Password github = result.stream().filter(p -> p.getService().equals("github")).findFirst().orElse(null);
        assertNotNull(github);
        assertEquals("user2", github.getUsername());
        assertEquals("pass2", github.getPassword());
    }

    /**
     * @brief Tests overwriting existing data via writeAll().
     *
     * Ensures that data is correctly overwritten when writeAll() is called multiple times.
     */
    @Test
    public void testOverwriteData() {
        List<Password> list1 = Collections.singletonList(new Password("netflix", "user", "1234"));
        storage.writeAll(list1);

        List<Password> list2 = Collections.singletonList(new Password("netflix", "admin", "abcd"));
        storage.writeAll(list2);

        List<Password> result = storage.readAll();
        assertEquals(1, result.size());
        assertEquals("admin", result.get(0).getUsername());
        assertEquals("abcd", result.get(0).getPassword());
    }

    /**
     * @brief Tests the add() method.
     *
     * Simulates input for adding a credential and verifies that it is stored.
     */
    @Test
    public void testAddFunction() {
        // Simulated input: Service, Username, Password.
        String simulatedInput = "testService\n" + "testUser\n" + "testPass\n";
        ByteArrayInputStream inStream = new ByteArrayInputStream(simulatedInput.getBytes(StandardCharsets.UTF_8));
        Scanner scanner = new Scanner(inStream);
        
        storage.add(scanner);
        scanner.close();
        
        List<Password> result = storage.readAll();
        assertEquals(1, result.size());
        Password record = result.get(0);
        assertEquals("testService", record.getService());
        assertEquals("testUser", record.getUsername());
        assertEquals("testPass", record.getPassword());
    }

    /**
     * @brief Tests the update() method.
     *
     * Inserts an initial record then simulates input to update it,
     * verifying that the update is correctly applied.
     */
    @Test
    public void testUpdateFunction() {
        // Insert initial record.
        List<Password> initial = Collections.singletonList(new Password("updateService", "oldUser", "oldPass"));
        storage.writeAll(initial);
        
        // Simulated input for update: Service, New Username, New Password.
        String simulatedInput = "updateService\n" + "newUser\n" + "newPass\n";
        ByteArrayInputStream inStream = new ByteArrayInputStream(simulatedInput.getBytes(StandardCharsets.UTF_8));
        Scanner scanner = new Scanner(inStream);
        
        storage.update(scanner);
        scanner.close();
        
        // Verify the record was updated.
        List<Password> result = storage.readAll();
        assertEquals(1, result.size());
        Password updated = result.get(0);
        assertEquals("updateService", updated.getService());
        assertEquals("newUser", updated.getUsername());
        assertEquals("newPass", updated.getPassword());
    }

    /**
     * @brief Tests the delete() method.
     *
     * Inserts a record and then simulates its deletion,
     * verifying that the record is removed.
     */
    @Test
    public void testDeleteFunction() {
        // Insert a record to be deleted.
        List<Password> initial = Collections.singletonList(new Password("deleteService", "userDel", "passDel"));
        storage.writeAll(initial);
        
        // Simulated input for delete: Service.
        String simulatedInput = "deleteService\n";
        ByteArrayInputStream inStream = new ByteArrayInputStream(simulatedInput.getBytes(StandardCharsets.UTF_8));
        Scanner scanner = new Scanner(inStream);
        
        storage.delete(scanner);
        scanner.close();
        
        // Verify that record is deleted.
        List<Password> result = storage.readAll();
        assertEquals(0, result.size());
    }

    /**
     * @brief Tests the view() method.
     *
     * Inserts several records, calls view(), and captures the output to ensure
     * that the printed data contains the expected credential details.
     */
    @Test
    public void testViewFunction() {
        List<Password> initial = Arrays.asList(
            new Password("viewService1", "user1", "pass1"),
            new Password("viewService2", "user2", "pass2")
        );
        storage.writeAll(initial);
        
        // Capture output from view() via System.out redirection.
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        PrintStream testOut = new PrintStream(outStream);
        PrintStream originalOut = System.out;
        System.setOut(testOut);
        
        storage.view();
        System.out.flush();
        System.setOut(originalOut);
        
        String output = outStream.toString();
        // Verify that output contains details of both records.
        assertTrue(output.contains("viewService1"));
        assertTrue(output.contains("user1"));
        assertTrue(output.contains("pass1"));
        assertTrue(output.contains("viewService2"));
        assertTrue(output.contains("user2"));
        assertTrue(output.contains("pass2"));
    }

    @After
    public void tearDown() {
        File dbFile = new File(TEST_DB_PATH);
        if (dbFile.exists()) dbFile.delete();
    }
}
