package com.ucoruh.password;

import org.junit.*;
import java.io.File;
import java.util.*;

import static org.junit.Assert.*;

public class DatabasePasswordStorageTest {

    private static final String TEST_DB_PATH = "test-passwords.db";
    private static final String TEST_DB_URL = "jdbc:sqlite:" + TEST_DB_PATH;
    private DatabasePasswordStorage storage;

    // Anonymous subclass to override database URL for testing
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

    @After
    public void tearDown() {
        File dbFile = new File(TEST_DB_PATH);
        if (dbFile.exists()) dbFile.delete();
    }
}
