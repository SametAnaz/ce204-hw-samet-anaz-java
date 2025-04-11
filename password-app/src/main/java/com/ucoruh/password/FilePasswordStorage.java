package com.ucoruh.password;

import java.io.*;
import java.util.*;

/**
 * @brief Implementation of InterfacePasswordStorage using file-based storage.
 *
 * This class provides file-based operations to store, retrieve, update, and delete password entries.
 */
public class FilePasswordStorage implements InterfacePasswordStorage {
    private static final String FILE = "passwords.txt";

    /**
     * @brief Adds a new password entry to the file-based storage.
     *
     * This method prompts the user to enter the service, username, and password, then creates a Password
     * object and appends its details to the passwords file.
     *
     * @param scanner the Scanner object used to obtain user input.
     */
    @Override
    public void add(Scanner scanner) {
        System.out.print("Service: ");
        String service = scanner.nextLine();
        System.out.print("Username: ");
        String user = scanner.nextLine();
        System.out.print("Password: ");
        String pass = scanner.nextLine();

        Password p = new Password(service, user, pass);

        try (FileWriter fw = new FileWriter(FILE, true)) {
            fw.write(p.getService() + "," + p.getUsername() + "," + p.getPassword() + "\n");
            System.out.println("Saved.");
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * @brief Displays all stored password entries.
     *
     * This method reads all password entries from the file and prints them to the console.
     */
    @Override
    public void view() {
        List<Password> list = readAll();
        if (list.isEmpty()) {
            System.out.println("No records found.");
        } else {
            list.forEach(System.out::println);
        }
    }

    /**
     * @brief Updates an existing password entry.
     *
     * This method prompts the user for the service to update and, if found, updates its username and password.
     *
     * @param scanner the Scanner object used to obtain user input for the update.
     */
    @Override
    public void update(Scanner scanner) {
        List<Password> list = readAll();
        System.out.print("Service to update: ");
        String target = scanner.nextLine();
        boolean updated = false;

        for (Password p : list) {
            if (p.getService().equalsIgnoreCase(target)) {
                System.out.print("New username: ");
                p.setUsername(scanner.nextLine());
                System.out.print("New password: ");
                p.setPassword(scanner.nextLine());
                updated = true;
                break;
            }
        }

        if (updated) {
            writeAll(list);
            System.out.println("Updated.");
        } else {
            System.out.println("Not found.");
        }
    }

    /**
     * @brief Deletes a password entry from the file-based storage.
     *
     * This method prompts the user for the service name of the entry to delete and removes the entry from the file.
     *
     * @param scanner the Scanner object used to obtain user input for deletion.
     */
    @Override
    public void delete(Scanner scanner) {
        List<Password> list = readAll();
        System.out.print("Service to delete: ");
        String target = scanner.nextLine();
        boolean removed = list.removeIf(p -> p.getService().equalsIgnoreCase(target));

        if (removed) {
            writeAll(list);
            System.out.println("Deleted.");
        } else {
            System.out.println("Not found.");
        }
    }

    /**
     * @brief Reads all password entries from the file.
     *
     * This method opens the file and reads each line, converting them into Password objects. All valid entries
     * are returned as a list.
     *
     * @return a List of Password objects representing the stored password entries.
     */
    @Override
    public List<Password> readAll() {
        List<Password> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    list.add(new Password(parts[0], parts[1], parts[2]));
                }
            }
        } catch (IOException ignored) {}
        return list;
    }

    /**
     * @brief Writes the list of password entries to the file.
     *
     * This method clears the existing content of the file and writes all password entries from the provided list.
     *
     * @param list a List of Password objects to be written to the file.
     */
    @Override
    public void writeAll(List<Password> list) {
        try (FileWriter writer = new FileWriter(FILE)) {
            for (Password p : list) {
                writer.write(p.getService() + "," + p.getUsername() + "," + p.getPassword() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
