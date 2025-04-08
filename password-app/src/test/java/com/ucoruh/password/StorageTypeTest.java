package com.ucoruh.password;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

/**
 * @brief Unit tests for the StorageType enum.
 *
 * Verifies that StorageType enum contains the expected values.
 */
public class StorageTypeTest {

    /**
     * @brief Tests that StorageType enum contains exactly FILE and SQLITE.
     */
    @Test
    public void testEnumValues() {
        StorageType[] expected = { StorageType.FILE, StorageType.SQLITE };
        StorageType[] actual = StorageType.values();
        assertArrayEquals("StorageType values should match expected order and values", expected, actual);
    }
}
