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
     * @brief Constructor initializing the manager with a master password.
     *
     * Initializes the credentials map and loads stored credentials.
     *
     * @param masterPassword Master password used for encryption/decryption.
     */
    public PasswordManager(String masterPassword) {
        this.masterPassword = masterPassword;
        this.credentials = new HashMap<>();
        loadCredentials();
    }

    /**
     * @brief Loads credentials from file.
     *
     * Dummy implementation for demonstration purposes.
     * In production, this method should load and decrypt file data.
     */
    private void loadCredentials() {
        // In production, load and decrypt file data.
    }

    /**
     * @brief Saves credentials to file.
     *
     * Dummy implementation for demonstration purposes.
     * In production, this method should encrypt and store credentials.
     */
    private void saveCredentials() {
        // In production, encrypt and store credentials.
    }

    /**
     * @brief Adds a new credential.
     *
     * Inserts the credential for the given account into the internal storage and saves it.
     *
     * @param account Account name.
     * @param password Password for the account.
     */
    public void addCredential(String account, String password) {
        credentials.put(account, password);
        saveCredentials();
    }

    /**
     * @brief Retrieves a credential.
     *
     * Fetches the password associated with the specified account.
     *
     * @param account Account name.
     * @return Password if account exists; otherwise, returns null.
     */
    public String getCredential(String account) {
        return credentials.get(account);
    }

    /**
     * @brief Displays the interactive menu and processes user input.
     *
     * Uses dependency injection for Scanner and PrintStream to enable unit testing.
     * Provides options to add, retrieve credentials, generate passwords, or exit.
     *
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
     * @brief Runs the application using the provided Scanner and PrintStream.
     *
     * Initiates the application by requesting the master password and
     * then invoking the interactive menu.
     *
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
     * @brief Main method to launch the console application.
     *
     * Entry point of the application. Initializes input and output streams,
     * then invokes the runApp method.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        runApp(scanner, System.out);
        scanner.close();
    }
}
