package com.ucoruh.password;

import java.util.List;
import java.util.Scanner;

/**
 * Interface for password storage operations.
 */
public interface InterfacePasswordStorage {

    /**
     * Adds a new password entry.
     *
     * @param scanner Scanner for user input
     */
    void add(Scanner scanner);

    /**
     * Displays all saved passwords.
     */
    void view();

    /**
     * Updates a password entry.
     *
     * @param scanner Scanner for user input
     */
    void update(Scanner scanner);

    /**
     * Deletes a password entry.
     *
     * @param scanner Scanner for user input
     */
    void delete(Scanner scanner);

    /**
     * Reads all saved passwords (used internally or for testing).
     *
     * @return List of Password entries
     */
    List<Password> readAll();

    /**
     * Writes all passwords to storage (used internally).
     *
     * @param list List of Password entries
     */
    void writeAll(List<Password> list);
}
