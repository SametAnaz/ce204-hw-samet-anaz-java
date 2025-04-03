package com.ucoruh.password;

import org.junit.*;
import java.io.*;
import java.util.*;

import static org.junit.Assert.*;

/**
 * Unit tests for FilePasswordStorage class.
 */
public class FilePasswordStorageTest {

    private final String TEST_FILE = "passwords.txt";
    private final FilePasswordStorage storage = new FilePasswordStorage();

    @Before
    public void setUp() {
        File f = new File(TEST_FILE);
        if (f.exists()) f.delete();
    }

    @After
    public void tearDown() {
        File f = new File(TEST_FILE);
        if (f.exists()) f.delete();
    }

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

    @Test
    public void testViewDoesNotThrow() {
        storage.writeAll(List.of(new Password("a", "b", "c")));
        storage.view(); // visual only — no assert needed
    }
}
