package com.ucoruh.password;

import java.util.Random;
import java.util.Scanner;

/**
 * @brief Utility class for generating random passwords.
 *
 * This class provides methods to generate secure random passwords using a predefined
 * set of characters.
 */
public class PasswordGenerator {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()";

    /**
     * @brief Generates a random password of a given length.
     *
     * This method uses a random number generator and a predefined character set to
     * create a password string of the specified length.
     *
     * @param length Desired password length.
     * @return A randomly generated password as a String.
     */
    public static String generatePassword(int length) {
        StringBuilder password = new StringBuilder(length);
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(CHARACTERS.length());
            password.append(CHARACTERS.charAt(index));
        }
        return password.toString();
    }

    /**
     * @brief Prompts for password length and outputs the generated password.
     *
     * This method reads the desired password length from the user input using the provided
     * Scanner, then generates and prints the random password.
     *
     * @param scanner Scanner for user input.
     */
    public static void generate(Scanner scanner) {
        System.out.print("Enter desired password length: ");
        int length = Integer.parseInt(scanner.nextLine());
        String newPassword = generatePassword(length);
        System.out.println("Generated Password: " + newPassword);
    }
}
