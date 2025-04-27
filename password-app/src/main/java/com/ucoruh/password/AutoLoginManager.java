/**
 * @file AutoLoginManager.java
 * @brief Provides auto-login functionality for the password manager.
 *
 * This class manages auto-login settings for different services and provides
 * methods to enable or disable auto-login functionality for specific accounts.
 *
 * @class AutoLoginManager
 * The class that provides auto-login functionality.
 */
package com.ucoruh.password;

import java.io.*;
import java.util.*;

/**
 * @brief Manages auto-login settings for password entries.
 *
 * This class provides functionality to enable or disable auto-login for specific services
 * and maintains a collection of services for which auto-login is enabled.
 */
public class AutoLoginManager {
    private static final String AUTO_LOGIN_FILE = "autologin.txt";
    private static Set<String> autoLoginServices = new HashSet<>();
    private static boolean initialized = false;
    
    /**
     * @brief Initializes the AutoLoginManager by loading auto-login settings.
     *
     * This method loads the auto-login settings from the file if it exists.
     */
    private static void initialize() {
        if (initialized) return;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(AUTO_LOGIN_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                autoLoginServices.add(line.trim());
            }
        } catch (IOException e) {
            // File might not exist yet, that's OK
        }
        
        initialized = true;
    }
    
    /**
     * @brief Saves the auto-login settings to a file.
     *
     * This method writes the current auto-login settings to the file.
     */
    private static void saveSettings() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(AUTO_LOGIN_FILE))) {
            for (String service : autoLoginServices) {
                writer.println(service);
            }
        } catch (IOException e) {
            System.out.println("Error saving auto-login settings: " + e.getMessage());
        }
    }
    
    /**
     * @brief Checks if auto-login is enabled for a service.
     *
     * @param service The service name to check.
     * @return true if auto-login is enabled for the service, false otherwise.
     */
    public static boolean isAutoLoginEnabled(String service) {
        initialize();
        return autoLoginServices.contains(service);
    }
    
    /**
     * @brief Enables auto-login for a service.
     *
     * @param service The service name to enable auto-login for.
     */
    public static void enableAutoLogin(String service) {
        initialize();
        autoLoginServices.add(service);
        saveSettings();
    }
    
    /**
     * @brief Disables auto-login for a service.
     *
     * @param service The service name to disable auto-login for.
     */
    public static void disableAutoLogin(String service) {
        initialize();
        autoLoginServices.remove(service);
        saveSettings();
    }
    
    /**
     * @brief Simulates auto-login to a service.
     *
     * This method checks if auto-login is enabled for the given service and,
     * if it is, retrieves the password for the service and simulates a login.
     *
     * @param service The service name to auto-login to.
     * @param passwordManager The PasswordManager instance to use.
     * @return true if auto-login was successful, false otherwise.
     */
    public static boolean autoLogin(String service, PasswordManager passwordManager) {
        if (isAutoLoginEnabled(service)) {
            String password = passwordManager.getCredential(service);
            if (password != null) {
                System.out.println("Auto-logging in to " + service + "...");
                // Simulate login
                System.out.println("Successfully logged in to " + service);
                return true;
            }
        }
        return false;
    }
    
    /**
     * @brief Displays the auto-login menu and processes user input.
     *
     * This method shows options to enable or disable auto-login for services
     * and processes the user's selection.
     *
     * @param scanner The Scanner object for user input.
     * @param passwordManager The PasswordManager instance to use.
     */
    public static void menu(Scanner scanner, PasswordManager passwordManager) {
        initialize();
        
        boolean back = false;
        while (!back) {
            System.out.println("\n==== AUTO-LOGIN FEATURES ====");
            System.out.println("1. Enable Auto-Login for a service");
            System.out.println("2. Disable Auto-Login for a service");
            System.out.println("3. Show services with Auto-Login enabled");
            System.out.println("4. Simulate Auto-Login for a service");
            System.out.println("0. Back to Main Menu");
            System.out.print("Your choice: ");
            
            String input = scanner.nextLine();
            try {
                int choice = Integer.parseInt(input);
                switch (choice) {
                    case 1:
                        enableAutoLoginMenu(scanner, passwordManager);
                        break;
                    case 2:
                        disableAutoLoginMenu(scanner);
                        break;
                    case 3:
                        showAutoLoginServices();
                        break;
                    case 4:
                        simulateAutoLogin(scanner, passwordManager);
                        break;
                    case 0:
                        back = true;
                        break;
                    default:
                        System.out.println("Invalid choice.");
                        break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid number.");
            }
        }
    }
    
    /**
     * @brief Menu to enable auto-login for a service.
     *
     * @param scanner The Scanner object for user input.
     * @param passwordManager The PasswordManager instance to use.
     */
    private static void enableAutoLoginMenu(Scanner scanner, PasswordManager passwordManager) {
        System.out.print("Enter service name to enable auto-login: ");
        String service = scanner.nextLine();
        
        // Check if the service exists in the password manager
        if (passwordManager.getCredential(service) != null) {
            enableAutoLogin(service);
            System.out.println("Auto-login enabled for " + service);
        } else {
            System.out.println("Service not found. Please add this service to your passwords first.");
        }
    }
    
    /**
     * @brief Menu to disable auto-login for a service.
     *
     * @param scanner The Scanner object for user input.
     */
    private static void disableAutoLoginMenu(Scanner scanner) {
        System.out.print("Enter service name to disable auto-login: ");
        String service = scanner.nextLine();
        
        if (isAutoLoginEnabled(service)) {
            disableAutoLogin(service);
            System.out.println("Auto-login disabled for " + service);
        } else {
            System.out.println("Auto-login was not enabled for " + service);
        }
    }
    
    /**
     * @brief Displays all services with auto-login enabled.
     */
    private static void showAutoLoginServices() {
        System.out.println("\nServices with Auto-Login enabled:");
        if (autoLoginServices.isEmpty()) {
            System.out.println("None");
        } else {
            for (String service : autoLoginServices) {
                System.out.println("- " + service);
            }
        }
    }
    
    /**
     * @brief Simulates auto-login for a service.
     *
     * @param scanner The Scanner object for user input.
     * @param passwordManager The PasswordManager instance to use.
     */
    private static void simulateAutoLogin(Scanner scanner, PasswordManager passwordManager) {
        System.out.print("Enter service name to auto-login: ");
        String service = scanner.nextLine();
        
        if (!autoLogin(service, passwordManager)) {
            System.out.println("Auto-login failed for " + service);
        }
    }
}
