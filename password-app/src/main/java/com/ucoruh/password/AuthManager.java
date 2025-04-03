package com.ucoruh.password;

import java.io.*;
import java.util.Scanner;

/**
 * @brief Singleton class responsible for user authentication and master password management.
 */
public class AuthManager {
    private static final String MASTER_PASSWORD_FILE = "master-password.txt";
    private static AuthManager instance;

    // Private constructor for Singleton
    private AuthManager() {}

    /**
     * Returns the singleton instance of AuthManager.
     *
     * @return AuthManager instance
     */
    public static AuthManager getInstance() {
        if (instance == null) {
            instance = new AuthManager();
        }
        return instance;
    }

    /**
     * Checks if a master password has already been set.
     *
     * @return true if master password exists, false otherwise
     */
    public boolean isMasterPasswordSet() {
        return new File(MASTER_PASSWORD_FILE).exists();
    }

    /**
     * Prompts the user to create a new master password and saves it to a file.
     *
     * @param scanner Scanner object for reading input
     */
    public void createMasterPassword(Scanner scanner) {
        System.out.print("Create a new master password: ");
        String pass = scanner.nextLine();
        try (FileWriter fw = new FileWriter(MASTER_PASSWORD_FILE)) {
            fw.write(pass);
            System.out.println("Password saved.");
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Authenticates the user by verifying the entered master password.
     *
     * @param scanner Scanner object for reading input
     * @return true if authentication is successful, false otherwise
     */
    public boolean login(Scanner scanner) {
        System.out.print("Enter master password: ");
        String input = scanner.nextLine();
        try (BufferedReader reader = new BufferedReader(new FileReader(MASTER_PASSWORD_FILE))) {
            return input.equals(reader.readLine());
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Displays the user authentication menu.
     *
     * @param scanner Scanner object for reading input
     */
    public void userMenu(Scanner scanner) {
        System.out.println("""
                --- User Authentication ---
                1. Logout
                0. Back
                """);
        int choice = scanner.nextInt(); scanner.nextLine();
        if (choice == 1) {
            System.out.println("Logged out.");
            System.exit(0);
        }
    }
}
