package com.ucoruh.password;

import java.util.Scanner;

/**
 * Delegates password operations to a selected storage implementation
 * created via PasswordStorageFactory.
 */
public class PasswordManager {

    // Storage implementation created using Factory Pattern
    private static final InterfacePasswordStorage storage =
        PasswordStorageFactory.create(StorageType.FILE);

    /**
     * Displays the password manager menu and routes operations
     * to the current storage implementation.
     *
     * @param scanner Scanner instance for reading user input
     */
    public static void menu(Scanner scanner) {
        System.out.println("""
                --- Secure Storage ---
                1. Add Password
                2. View Passwords
                3. Update Password
                4. Delete Password
                0. Back
                """);
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1 -> storage.add(scanner);
            case 2 -> storage.view();
            case 3 -> storage.update(scanner);
            case 4 -> storage.delete(scanner);
            case 0 -> {} // Back
            default -> System.out.println("Invalid selection.");
        }
    }
}
