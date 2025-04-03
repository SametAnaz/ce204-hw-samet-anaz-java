package com.ucoruh.password;

import java.util.Scanner;

/**
 * Manages auto-login feature settings through the console menu.
 */
public class AutoLoginManager {

    /**
     * Displays the auto-login settings menu and handles user input.
     *
     * @param scanner Scanner instance for reading user input
     */
    public static void menu(Scanner scanner) {
        System.out.println("""
                --- Auto-Login ---
                1. Enable Auto-Login
                2. Disable Auto-Login
                0. Back
                """);
        int c = scanner.nextInt(); scanner.nextLine();
        if (c == 1) System.out.println("Auto-login enabled.");
        else if (c == 2) System.out.println("Auto-login disabled.");
    }
}
