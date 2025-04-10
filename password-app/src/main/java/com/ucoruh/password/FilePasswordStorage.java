package com.ucoruh.password;

import java.io.*;
import java.util.*;

/**
 * Implementation of InterfacePasswordStorage using file-based storage.
 */
public class FilePasswordStorage implements InterfacePasswordStorage {
    private static final String FILE = "passwords.txt";

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

    @Override
    public void view() {
        List<Password> list = readAll();
        if (list.isEmpty()) {
            System.out.println("No records found.");
        } else {
            list.forEach(System.out::println);
        }
    }

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
