package com.ucoruh.password;

import java.util.Scanner;

/**
 * Entry point for the Password Manager console application.
 */
public class PasswordApp {

    /**
     * Starts the main menu loop and handles user actions.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AuthManager auth = AuthManager.getInstance();

        if (!auth.isMasterPasswordSet()) {
            auth.createMasterPassword(scanner);
        }

        if (auth.login(scanner)) {
            int choice;
            do {
                System.out.println("""
                        \n==== MAIN MENU ====
                        1. User Authentication
                        2. Secure Storage of Passwords
                        3. Password Generator
                        4. Auto-Login Feature
                        5. Multi-Platform Compatibility
                        0. Exit
                        Your choice:""");
                choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> auth.userMenu(scanner);
                    case 2 -> PasswordManager.menu(scanner);
                    case 3 -> PasswordGenerator.generate(scanner);
                    case 4 -> AutoLoginManager.menu(scanner);
                    case 5 -> PlatformManager.showPlatforms();
                    case 0 -> System.out.println("Exiting...");
                    default -> System.out.println("Invalid choice.");
                }

            } while (choice != 0);
        } else {
            System.out.println("Login failed.");
        }

        scanner.close();
    }
}
