package com.ucoruh.password;

import java.io.PrintStream;
import java.util.Scanner;

/**
 * @brief Entry point for the Password Manager console application.
 *
 * This class provides the main method for launching the application and a runApp method
 * which handles the overall flow of the application including authentication and menu operations.
 */
public class PasswordApp {
    /**
     * @brief Runs the application using the provided Scanner and PrintStream.
     *
     * This method initializes user authentication and starts the main menu loop for handling
     * various operations like user authentication, secure storage of passwords, password generation,
     * auto-login feature, and platform management.
     *
     * @param scanner The Scanner object for user input.
     * @param out The PrintStream object for output.
     */
    public static void runApp(Scanner scanner, PrintStream out) {
        AuthManager auth = AuthManager.getInstance();

        if (!auth.isMasterPasswordSet()) {
            out.print("Set master password: ");
            auth.createMasterPassword(scanner);
        }

        out.print("Enter master password to login: ");
        if (auth.login(scanner)) {
            PasswordManager pm = new PasswordManager(auth.getMasterPassword());
            int choice = -1;
            do {
                out.println("==== MAIN MENU ====");
                out.println("1. User Authentication");
                out.println("2. Secure Storage of Passwords");
                out.println("3. Password Generator");
                out.println("4. Auto-Login Feature");
                out.println("5. Multi-Platform Compatibility");
                out.println("0. Exit");
                out.print("Your choice: ");
                String input = scanner.nextLine();
                try {
                    choice = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    out.println("Invalid number.");
                    continue;
                }
                switch (choice) {
                    case 1:
                        auth.userMenu(scanner);
                        break;
                    case 2:
                        pm.menu(scanner, out);
                        break;
                    case 3:
                        PasswordGenerator.generate(scanner);
                        break;
                    case 4:
                        AutoLoginManager.menu(scanner);
                        break;
                    case 5:
                        PlatformManager.showPlatforms();
                        break;
                    case 0:
                        out.println("Exiting...");
                        break;
                    default:
                        out.println("Invalid choice.");
                        break;
                }
            } while (choice != 0);
        } else {
            out.println("Login failed.");
        }
    }

    /**
     * @brief Main method to launch the console application.
     *
     * This method serves as the entry point to the application and initiates the runApp method.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        runApp(scanner, System.out);
        scanner.close();
    }
}
