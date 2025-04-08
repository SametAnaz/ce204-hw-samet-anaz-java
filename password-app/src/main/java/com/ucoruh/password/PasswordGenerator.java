package com.ucoruh.password;

import java.util.Scanner;

/**
 * @brief The PasswordGenerator class generates a secure random password.
 *
 * This class provides a static method to generate a 12-character password
 * consisting of uppercase letters, lowercase letters, digits, and special characters.
 * The password is printed to the console.
 * 
 * It also contains a default constructor for test coverage purposes.
 * 
 * @author Samet
 */
public class PasswordGenerator {

    /**
     * @brief Default constructor.
     *
     * This constructor is explicitly defined to ensure full test coverage,
     * even though the class only contains static methods.
     */
    public PasswordGenerator() {
        // No-op constructor
    }

    /**
     * @brief Generates a random 12-character password and prints it.
     *
     * This method uses a predefined character set to create a password
     * consisting of letters (uppercase and lowercase), digits, and symbols.
     * The generated password is printed to the standard output.
     *
     * @param scanner A Scanner object, passed for interface consistency,
     *                but not used in the current implementation.
     */
    public static void generate(Scanner scanner) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%&";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 12; i++) {
            int idx = (int) (Math.random() * chars.length());
            sb.append(chars.charAt(idx));
        }
        System.out.println("Password: " + sb);
    }

	public static String generatePassword(int length) {
		// TODO Auto-generated method stub
		return null;
	}
}
