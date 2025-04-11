package com.ucoruh.password;

import java.util.Scanner;

/**
 * @brief Singleton class that manages user authentication.
 *
 * This class handles the creation and verification of the master password.
 * It also provides a stub for user-specific functionality.
 */
public class AuthManager {
	/**
	 * @brief Singleton instance of the AuthManager.
	 *
	 * This static field holds the single instance of the AuthManager 
	 * used throughout the application to ensure consistent user authentication.
	 */
	private static AuthManager instance;

	/**
	 * @brief Master password used for authentication.
	 *
	 * This field stores the master password that is used to authenticate the user.
	 */
	private String masterPassword;  // Stores master password


    /**
     * Private constructor to enforce singleton pattern..
     */
    private AuthManager() {
        // Private constructor.
    }

    /**
     * Retrieve the singleton instance.
     * @return AuthManager instance.
     */
    public static AuthManager getInstance() {
        if (instance == null) {
            instance = new AuthManager();
        }
        return instance;
    }
    
    /**
     * Resets the singleton instance.
     * <p>
     * This method is intended for testing purposes only.
     * </p>
     */
    public static void resetInstance() {
        instance = null;
    }
    
    /**
     * Check if the master password is set.
     * @return true if master password is set; otherwise false.
     */
    public boolean isMasterPasswordSet() {
        return masterPassword != null && !masterPassword.isEmpty();
    }
    
    /**
     * Create the master password.
     * @param scanner The Scanner object for user input.
     */
    public void createMasterPassword(Scanner scanner) {
        System.out.print("Set master password: ");
        masterPassword = scanner.nextLine();
        // In production, store a hashed version.
    }
    
    /**
     * Perform user login.
     * @param scanner The Scanner object for user input.
     * @return true if login is successful.
     */
    public boolean login(Scanner scanner) {
        System.out.print("Enter master password to login: ");
        String input = scanner.nextLine();
        return masterPassword.equals(input);
    }
    
    /**
     * Getter for the master password.
     * @return The master password.
     */
    public String getMasterPassword() {
        return masterPassword;
    }
    
    /**
     * Display user-specific menu (stub implementation).
     * @param scanner The Scanner object for user input.
     */
    public void userMenu(Scanner scanner) {
        System.out.println("User menu functionality not yet implemented.");
    }
}
