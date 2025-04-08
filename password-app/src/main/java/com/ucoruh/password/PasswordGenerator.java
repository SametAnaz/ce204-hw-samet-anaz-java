package com.ucoruh.password;

import java.util.Random;
import java.util.Scanner;

/**
 * @brief Utility class for generating random passwords.
 */
public class PasswordGenerator {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()";

    /**
     * Generates a random password of a given length.
     * @param length Desired password length.
     * @return A randomly generated password.
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
     * Prompts for password length and outputs the generated password.
     * @param scanner Scanner for user input.
     */
    public static void generate(Scanner scanner) {
        System.out.print("Enter desired password length: ");
        int length = Integer.parseInt(scanner.nextLine());
        String newPassword = generatePassword(length);
        System.out.println("Generated Password: " + newPassword);
    }
}
