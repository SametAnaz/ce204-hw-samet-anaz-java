package com.ucoruh.password;

import org.junit.*;
import java.io.*;
import java.util.*;

import static org.junit.Assert.*;

/**
 * @brief Unit tests for FilePasswordStorage class.
 *
 * These tests verify the functionality of reading, writing, adding, viewing, updating, and deleting
 * passwords using the file-based storage implementation.
 */
public class FilePasswordStorageTest {

    private final String TEST_FILE = "passwords.txt";
    private final FilePasswordStorage storage = new FilePasswordStorage();
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @Before
    public void setUp() {
        File f = new File(TEST_FILE);
        if (f.exists()) f.delete();
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void tearDown() {
        File f = new File(TEST_FILE);
        if (f.exists()) f.delete();
        System.setOut(originalOut);
    }

    /**
     * Tests adding a password entry and reading it back.
     */
    @Test
    public void testAddAndReadAll() {
        String input = "testService\nuser1\npass1\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        storage.add(scanner);

        List<Password> list = storage.readAll();
        assertEquals(1, list.size());
        assertEquals("testService", list.get(0).getService());
        assertEquals("user1", list.get(0).getUsername());
        assertEquals("pass1", list.get(0).getPassword());
    }

    /**
     * Tests updating an existing password entry.
     */
    @Test
    public void testUpdate() {
        // setup initial entry
        storage.writeAll(List.of(new Password("email", "olduser", "oldpass")));

        String input = "email\nnewuser\nnewpass\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        storage.update(scanner);

        List<Password> list = storage.readAll();
        assertEquals("newuser", list.get(0).getUsername());
        assertEquals("newpass", list.get(0).getPassword());
    }
    
    /**
     * Tests updating a non-existent password entry.
     */
    @Test
    public void testUpdateNonExistent() {
        // Setup initial entry
        storage.writeAll(List.of(new Password("email", "olduser", "oldpass")));
        
        String input = "nonexistent\nnewuser\nnewpass\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        storage.update(scanner);
        
        String output = outContent.toString();
        assertTrue(output.contains("Not found"));
        
        // Original entry should still be intact
        List<Password> list = storage.readAll();
        assertEquals(1, list.size());
        assertEquals("email", list.get(0).getService());
    }

    /**
     * Tests deleting a password entry.
     */
    @Test
    public void testDelete() {
        storage.writeAll(List.of(
                new Password("gmail", "u1", "p1"),
                new Password("dropbox", "u2", "p2")
        ));

        String input = "gmail\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        storage.delete(scanner);

        List<Password> list = storage.readAll();
        assertEquals(1, list.size());
        assertEquals("dropbox", list.get(0).getService());
    }
    
    /**
     * Tests deleting a non-existent password entry.
     */
    @Test
    public void testDeleteNonExistent() {
        storage.writeAll(List.of(new Password("gmail", "u1", "p1")));
        
        String input = "nonexistent\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        storage.delete(scanner);
        
        String output = outContent.toString();
        assertTrue(output.contains("Not found"));
        
        // Original entry should still be intact
        List<Password> list = storage.readAll();
        assertEquals(1, list.size());
    }

    /**
     * Tests viewing password entries with content.
     */
    @Test
    public void testViewWithContent() {
        storage.writeAll(List.of(new Password("gmail", "user1", "pass1")));
        storage.view();
        
        String output = outContent.toString();
        assertTrue(output.contains("gmail"));
        assertTrue(output.contains("user1"));
        assertTrue(output.contains("pass1"));
    }
    
    /**
     * Tests viewing password entries when no entries exist.
     */
    @Test
    public void testViewWithNoContent() {
        storage.view();
        
        String output = outContent.toString();
        assertTrue(output.contains("No records found"));
    }
    
    /**
     * Tests reading from a non-existent file.
     */
    @Test
    public void testReadAllWithNoFile() {
        File f = new File(TEST_FILE);
        if (f.exists()) f.delete();
        
        List<Password> list = storage.readAll();
        assertTrue("Should return empty list when file doesn't exist", list.isEmpty());
    }
    
    /**
     * Tests reading malformed lines from the password file.
     */
    @Test
    public void testReadAllWithMalformedData() throws IOException {
        // Create a file with some malformed lines
        try (FileWriter writer = new FileWriter(TEST_FILE)) {
            writer.write("validService,validUser,validPass\n");
            writer.write("malformed\n");  // Missing username and password
            writer.write("another,validUser\n");  // Missing password
            writer.write("valid,valid,valid\n");
        }
        
        List<Password> list = storage.readAll();
        assertEquals("Should only read valid entries", 2, list.size());
        assertEquals("First entry should be parsed correctly", "validService", list.get(0).getService());
        assertEquals("Last entry should be parsed correctly", "valid", list.get(1).getService());
    }
    
    /**
     * Tests adding a password entry when file write fails.
     */
    @Test
    public void testAddWithIOException() throws IOException {
        File mockFile = new File(TEST_FILE);
        mockFile.createNewFile();
        mockFile.setReadOnly();  // Make file read-only to force write failure
        
        String input = "testService\nuser1\npass1\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        storage.add(scanner);
        
        String output = outContent.toString();
        assertTrue(output.contains("Error:") || output.contains("denied"));
        
        mockFile.setWritable(true);  // Restore write permission for cleanup
    }
}
