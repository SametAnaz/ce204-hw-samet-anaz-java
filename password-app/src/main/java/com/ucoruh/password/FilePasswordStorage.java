package com.ucoruh.password;

import java.io.*;
import java.util.*;

/**
 * @brief Implementation of InterfacePasswordStorage using file-based storage.
 *
 * This class provides file-based operations to store, retrieve, update, and delete password entries.
 * All password data is encrypted using the master password.
 */
public class FilePasswordStorage implements InterfacePasswordStorage {
	/**
	 * @brief File name used for file-based password storage.
	 *
	 * This static final field holds the file name where password entries are stored 
	 * when using a file-based storage mechanism.
	 */
	private static final String FILE = "passwords.txt";
	
	/**
	 * @brief The master password used for encryption/decryption.
	 */
	private final String masterPassword;
	
	/**
	 * @brief Constructor that initializes storage with the master password.
	 * 
	 * @param masterPassword The master password for encryption/decryption.
	 */
	public FilePasswordStorage(String masterPassword) {
		this.masterPassword = masterPassword;
	}

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
		List<Password> passwords = readAll();
		
		// Check if service already exists
		boolean exists = passwords.stream()
			.anyMatch(pwd -> pwd.getService().equalsIgnoreCase(service));
		
		if (exists) {
			System.out.println("A password for this service already exists. Use update option to modify it.");
			return;
		}
		
		passwords.add(p);
		writeAll(passwords);
		System.out.println("Password saved successfully.");
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
			for (int i = 0; i < list.size(); i++) {
				System.out.println((i + 1) + ". " + list.get(i));
			}
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
		if (list.isEmpty()) {
			System.out.println("No records found.");
			return;
		}
		
		System.out.print("Service to update: ");
		String target = scanner.nextLine();
		boolean updated = false;

		for (Password p : list) {
			if (p.getService().equalsIgnoreCase(target)) {
				System.out.print("New username (leave blank to keep current '" + p.getUsername() + "'): ");
				String username = scanner.nextLine();
				if (!username.trim().isEmpty()) {
					p.setUsername(username);
				}
				
				System.out.print("New password (leave blank to keep current): ");
				String password = scanner.nextLine();
				if (!password.trim().isEmpty()) {
					p.setPassword(password);
				}
				
				updated = true;
				break;
			}
		}

		if (updated) {
			writeAll(list);
			System.out.println("Password updated successfully.");
		} else {
			System.out.println("Service not found.");
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
		if (list.isEmpty()) {
			System.out.println("No records found.");
			return;
		}
		
		System.out.print("Service to delete: ");
		String target = scanner.nextLine();
		boolean removed = list.removeIf(p -> p.getService().equalsIgnoreCase(target));

		if (removed) {
			writeAll(list);
			System.out.println("Password deleted successfully.");
		} else {
			System.out.println("Service not found.");
		}
	}

	/**
	 * @brief Reads all password entries from the file.
	 *
	 * This method opens the file and reads each line, decrypts the data,
	 * and converts it into Password objects. All valid entries are returned as a list.
	 *
	 * @return a List of Password objects representing the stored password entries.
	 */
	@Override
	public List<Password> readAll() {
		List<Password> list = new ArrayList<>();
		File file = new File(FILE);
		if (!file.exists()) {
			return list;
		}
		
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = reader.readLine()) != null) {
				try {
					// Decrypt the line
					String decrypted = EncryptionUtil.decrypt(line, masterPassword);
					String[] parts = decrypted.split(",");
					if (parts.length == 3) {
						list.add(new Password(parts[0], parts[1], parts[2]));
					}
				} catch (Exception e) {
					// Skip lines that cannot be decrypted
					System.out.println("Warning: Could not decrypt a password entry.");
				}
			}
		} catch (IOException e) {
			System.out.println("Error reading password file: " + e.getMessage());
		}
		return list;
	}

	/**
	 * @brief Writes the list of password entries to the file.
	 *
	 * This method clears the existing content of the file and writes all password entries
	 * from the provided list, encrypting each entry.
	 *
	 * @param list a List of Password objects to be written to the file.
	 */
	@Override
	public void writeAll(List<Password> list) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE))) {
			for (Password p : list) {
				String data = p.getService() + "," + p.getUsername() + "," + p.getPassword();
				try {
					// Encrypt the data
					String encrypted = EncryptionUtil.encrypt(data, masterPassword);
					writer.write(encrypted);
					writer.newLine();
				} catch (Exception e) {
					System.out.println("Error encrypting password for " + p.getService() + ": " + e.getMessage());
				}
			}
		} catch (IOException e) {
			System.out.println("Error writing to password file: " + e.getMessage());
		}
	}
}
