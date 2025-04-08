package com.ucoruh.password;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Scanner;

import org.junit.Test;

/**
 * @brief Unit tests for the InterfacePasswordStorage interface.
 *
 * This test verifies that the InterfacePasswordStorage declares the
 * expected methods with their corresponding parameter types and return types.
 */
public class InterfacePasswordStorageTest {

    /**
     * @brief Tests that InterfacePasswordStorage declares the correct methods.
     *
     * Verifies methods: add, view, update, delete, readAll, and writeAll.
     */
    @Test
    public void testMethodSignatures() throws NoSuchMethodException {
        Class<?> clazz = InterfacePasswordStorage.class;

        // Test method: void add(Scanner scanner)
        Method addMethod = clazz.getMethod("add", Scanner.class);
        assertEquals("Return type of add() must be void", void.class, addMethod.getReturnType());

        // Test method: void view()
        Method viewMethod = clazz.getMethod("view");
        assertEquals("Return type of view() must be void", void.class, viewMethod.getReturnType());

        // Test method: void update(Scanner scanner)
        Method updateMethod = clazz.getMethod("update", Scanner.class);
        assertEquals("Return type of update() must be void", void.class, updateMethod.getReturnType());

        // Test method: void delete(Scanner scanner)
        Method deleteMethod = clazz.getMethod("delete", Scanner.class);
        assertEquals("Return type of delete() must be void", void.class, deleteMethod.getReturnType());

        // Test method: List<Password> readAll()
        Method readAllMethod = clazz.getMethod("readAll");
        assertEquals("Return type of readAll() must be List", List.class, readAllMethod.getReturnType());

        // Test method: void writeAll(List<Password> list)
        Method writeAllMethod = clazz.getMethod("writeAll", List.class);
        assertEquals("Return type of writeAll() must be void", void.class, writeAllMethod.getReturnType());
    }
}
