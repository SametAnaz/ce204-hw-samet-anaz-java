package com.ucoruh.password;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * @brief Main class for the Password Manager application.
 *
 * Manages secure storage and retrieval of credentials using a master password.
 */
public class PasswordManager {
    private final Map<String, String> credentials;
    private final String masterPassword;

    /**
     * Constructor initializing the manager with a master password.
     * @param masterPassword Master password used for encryption/decryption.
     */
    public PasswordManager(String masterPassword) {
        this.masterPassword = masterPassword;
        this.credentials = new HashMap<>();
        loadCredentials();
    }

    /**
     * Load credentials from file.
     * (Dummy implementation for demonstration)
     */
    private void loadCredentials() {
        // In production, load and decrypt file data.
    }

    /**
     * Save credentials to file.
     * (Dummy implementation for demonstration)
     */
    private void saveCredentials() {
        // In production, encrypt and store credentials.
    }

    /**
     * Add a new credential.
     * @param account Account name.
     * @param password Password for the account.
     */
    public void addCredential(String account, String password) {
        credentials.put(account, password);
        saveCredentials();
    }

    /**
     * Retrieve a credential.
     * @param account Account name.
     * @return Password if account exists; otherwise, null.
     */
    public String getCredential(String account) {
        return credentials.get(account);
    }

    /**
     * Displays the interactive menu and processes user input.
     * Uses dependency injection for Scanner and PrintStream to enable unit testing.
     * @param scanner The Scanner object for reading user input.
     * @param out The PrintStream object for writing output.
     */
    public void menu(Scanner scanner, PrintStream out) {
        boolean exit = false;
        while (!exit) {
            out.println("\n1. Add Credential");
            out.println("2. Retrieve Credential");
            out.println("3. Generate Password");
            out.println("4. Exit");
            out.print("Choose an option: ");
            String option = scanner.nextLine();

            switch (option) {
                case "1":
                    out.print("Enter account name: ");
                    String account = scanner.nextLine();
                    out.print("Enter password: ");
                    String password = scanner.nextLine();
                    addCredential(account, password);
                    out.println("Credential added.");
                    break;
                case "2":
                    out.print("Enter account name: ");
                    account = scanner.nextLine();
                    String storedPwd = getCredential(account);
                    if (storedPwd != null) {
                        out.println("Password: " + storedPwd);
                    } else {
                        out.println("No credential found.");
                    }
                    break;
                case "3":
                    out.print("Enter desired password length: ");
                    int length = Integer.parseInt(scanner.nextLine());
                    String newPassword = PasswordGenerator.generatePassword(length);
                    out.println("Generated Password: " + newPassword);
                    break;
                case "4":
                    exit = true;
                    break;
                default:
                    out.println("Invalid option.");
                    break;
            }
        }
    }

    /**
     * Runs the application using the provided Scanner and PrintStream.
     * This method allows testing of the main application flow.
     * @param scanner Scanner for user input.
     * @param out PrintStream for output.
     */
    public static void runApp(Scanner scanner, PrintStream out) {
        out.print("Enter master password: ");
        String masterPwd = scanner.nextLine();
        PasswordManager pm = new PasswordManager(masterPwd);
        pm.menu(scanner, out);
    }

    /**
     * Main method to launch the console application.
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        runApp(scanner, System.out);
        scanner.close();
    }
}
