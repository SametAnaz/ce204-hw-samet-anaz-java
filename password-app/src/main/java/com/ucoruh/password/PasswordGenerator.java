package com.ucoruh.password;

import java.util.Random;
import java.util.Scanner;

/**
 * @file PasswordGenerator.java
 * @class PasswordGenerator
 * @brief Utility class for generating random passwords.
 * @details This class provides methods to generate secure random passwords using configurable
 * character sets.
 */
public class PasswordGenerator {

    /**
     * @brief Set of uppercase characters used for password generation
     * @param chars A-Z uppercase letters
     */
    private static final String UPPERCASE_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /**
     * @brief Set of lowercase characters used for password generation
     * @param chars a-z lowercase letters
     */
    private static final String LOWERCASE_CHARS = "abcdefghijklmnopqrstuvwxyz";

    /**
     * @brief Set of numeric digits used for password generation
     * @param chars 0-9 digits
     */
    private static final String DIGIT_CHARS = "0123456789";

    /**
     * @brief Set of special characters used for password generation
     * @param chars Special characters including !@#$%^&*()_-+=<>?/[]{}|
     */
    private static final String SPECIAL_CHARS = "!@#$%^&*()_-+=<>?/[]{}|";

    /**
     * @brief Combined set of all characters used for password generation
     * @param chars Combination of uppercase, lowercase, digits, and special characters
     */
    private static final String CHARACTERS = UPPERCASE_CHARS + LOWERCASE_CHARS + DIGIT_CHARS + SPECIAL_CHARS;

    /**
     * @brief Generates a random password of a given length
     * @details This method uses a random number generator and a predefined character set to
     * create a password string of the specified length. If a negative length is provided,
     * an empty string is returned.
     *
     * @param length The desired length of the password
     * @return String A randomly generated password
     */
    public static String generatePassword(int length) {
        // For backward compatibility, use all character sets
        return generatePassword(length, true, true, true, true);
    }
    
    /**
     * @brief Generates a random password with specific character sets
     * @details This method allows selective inclusion of character sets (uppercase letters,
     * lowercase letters, digits, and special characters) in the generated password.
     * At least one character set must be included.
     *
     * @param length The desired length of the password
     * @param includeUppercase Whether to include uppercase letters (A-Z)
     * @param includeLowercase Whether to include lowercase letters (a-z)
     * @param includeDigits Whether to include digits (0-9)
     * @param includeSpecial Whether to include special characters
     * @return String A randomly generated password meeting the specified criteria
     */
    public static String generatePassword(int length, boolean includeUppercase,
                                         boolean includeLowercase, boolean includeDigits,
                                         boolean includeSpecial) {
        // Return empty string for zero or negative length
        if (length <= 0) {
            return "";
        }
        
        // Build the character set based on includes
        StringBuilder charSetBuilder = new StringBuilder();
        if (includeUppercase) charSetBuilder.append(UPPERCASE_CHARS);
        if (includeLowercase) charSetBuilder.append(LOWERCASE_CHARS);
        if (includeDigits) charSetBuilder.append(DIGIT_CHARS);
        if (includeSpecial) charSetBuilder.append(SPECIAL_CHARS);
        
        // If no character set is selected, use lowercase as default
        String charSet = charSetBuilder.length() > 0 ? charSetBuilder.toString() : LOWERCASE_CHARS;
        
        // Generate the password
        StringBuilder password = new StringBuilder(length);
        Random random = new Random();
        
        // Ensure at least one character from each selected char set
        if (length >= 1 && includeUppercase && password.length() < length) {
            password.append(UPPERCASE_CHARS.charAt(random.nextInt(UPPERCASE_CHARS.length())));
        }
        if (length >= 2 && includeLowercase && password.length() < length) {
            password.append(LOWERCASE_CHARS.charAt(random.nextInt(LOWERCASE_CHARS.length())));
        }
        if (length >= 3 && includeDigits && password.length() < length) {
            password.append(DIGIT_CHARS.charAt(random.nextInt(DIGIT_CHARS.length())));
        }
        if (length >= 4 && includeSpecial && password.length() < length) {
            password.append(SPECIAL_CHARS.charAt(random.nextInt(SPECIAL_CHARS.length())));
        }
        
        // Fill the rest with random characters from the selected set
        while (password.length() < length) {
            int index = random.nextInt(charSet.length());
            password.append(charSet.charAt(index));
        }
        
        // Shuffle the characters to avoid predictable patterns
        char[] passChars = password.toString().toCharArray();
        for (int i = passChars.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char temp = passChars[i];
            passChars[i] = passChars[j];
            passChars[j] = temp;
        }
        
        return new String(passChars);
    }

    /**
     * @brief Prompts user for password length and generates a password
     * @details This method reads the desired password length from the user input using the provided
     * Scanner, then generates and prints the random password.
     *
     * @param scanner Scanner object for reading user input
     */
    public static void generate(Scanner scanner) {
        System.out.print("Enter desired password length: ");
        try {
            int length = Integer.parseInt(scanner.nextLine());
            String newPassword = generatePassword(length);
            System.out.println("Generated Password: " + newPassword);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
        }
    }
}
