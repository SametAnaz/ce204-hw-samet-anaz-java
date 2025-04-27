package com.ucoruh.password;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * @brief Main class for the Password Manager application.
 *
 * Manages secure storage and retrieval of credentials using a master password.
 */
public class PasswordManager {
	/**
	 * @brief Stores the association between account names and their corresponding passwords.
	 *
	 * This final map holds the credentials for different accounts and is used to manage password data.
	 */
	private final Map<String, String> credentials;

	/**
	 * @brief The master password used for authentication.
	 *
	 * This final field stores the master password that is utilized for user authentication and securing the credentials.
	 */
	private final String masterPassword;
	
	/**
	 * @brief The storage implementation for passwords.
	 */
	private final InterfacePasswordStorage storage;

	/**
	 * @brief Constructor initializing the manager with a master password.
	 *
	 * Initializes the credentials map and loads stored credentials.
	 *
	 * @param masterPassword Master password used for encryption/decryption.
	 */
	public PasswordManager(String masterPassword) {
		this.masterPassword = masterPassword;
		this.credentials = new HashMap<>();
		this.storage = PasswordStorageFactory.create(StorageType.FILE, masterPassword);
		loadCredentials();
	}
	
	/**
	 * @brief Constructor with specified storage type.
	 *
	 * @param masterPassword Master password used for encryption/decryption.
	 * @param storageType The type of storage to use.
	 */
	public PasswordManager(String masterPassword, StorageType storageType) {
		this.masterPassword = masterPassword;
		this.credentials = new HashMap<>();
		this.storage = PasswordStorageFactory.create(storageType, masterPassword);
		loadCredentials();
	}

	/**
	 * @brief Loads credentials from storage.
	 */
	private void loadCredentials() {
		List<Password> passwordList = storage.readAll();
		credentials.clear();
		for (Password p : passwordList) {
			credentials.put(p.getService(), p.getPassword());
		}
	}

	/**
	 * @brief Adds a new credential.
	 *
	 * Inserts the credential for the given account into the internal storage and saves it.
	 *
	 * @param account Account name.
	 * @param password Password for the account.
	 */
	public void addCredential(String account, String password) {
		credentials.put(account, password);
		
		// Create a password list and save it
		List<Password> passwordList = storage.readAll();
		boolean updated = false;
		
		// Check if the account already exists
		for (Password p : passwordList) {
			if (p.getService().equalsIgnoreCase(account)) {
				p.setPassword(password);
				updated = true;
				break;
			}
		}
		
		// If not found, add new entry
		if (!updated) {
			passwordList.add(new Password(account, "default_user", password));
		}
		
		storage.writeAll(passwordList);
	}

	/**
	 * @brief Retrieves a credential.
	 *
	 * Fetches the password associated with the specified account.
	 *
	 * @param account Account name.
	 * @return Password if account exists; otherwise, returns null.
	 */
	public String getCredential(String account) {
		// Reload credentials to ensure we have the latest
		loadCredentials();
		return credentials.get(account);
	}

	/**
	 * @brief Displays the interactive menu and processes user input.
	 *
	 * Uses dependency injection for Scanner and PrintStream to enable unit testing.
	 * Provides options to add, retrieve credentials, generate passwords, or exit.
	 *
	 * @param scanner The Scanner object for reading user input.
	 * @param out The PrintStream object for writing output.
	 */
	public void menu(Scanner scanner, PrintStream out) {
		boolean back = false;
		while (!back) {
			out.println("\n==== PASSWORD STORAGE MENU ====");
			out.println("1. Add New Password");
			out.println("2. View All Passwords");
			out.println("3. Update Password");
			out.println("4. Delete Password");
			out.println("5. Generate and Save Password");
			out.println("0. Back to Main Menu");
			out.print("Your choice: ");
			
			String input = scanner.nextLine();
			try {
				int choice = Integer.parseInt(input);
				switch (choice) {
					case 1:
						storage.add(scanner);
						break;
					case 2:
						storage.view();
						break;
					case 3:
						storage.update(scanner);
						break;
					case 4:
						storage.delete(scanner);
						break;
					case 5:
						generateAndSavePassword(scanner, out);
						break;
					case 0:
						back = true;
						break;
					default:
						out.println("Invalid choice.");
						break;
				}
				// Reload credentials after operations
				loadCredentials();
			} catch (NumberFormatException e) {
				out.println("Invalid number.");
			}
		}
	}
	
	/**
	 * @brief Generates a new password and saves it for a service.
	 *
	 * @param scanner The Scanner object for user input.
	 * @param out The PrintStream object for output.
	 */
	private void generateAndSavePassword(Scanner scanner, PrintStream out) {
		out.print("Enter service name: ");
		String service = scanner.nextLine();
		out.print("Enter username: ");
		String username = scanner.nextLine();
		out.print("Enter desired password length: ");
		
		try {
			int length = Integer.parseInt(scanner.nextLine());
			if (length <= 0) {
				out.println("Password length must be greater than 0.");
				return;
			}
			
			String password = PasswordGenerator.generatePassword(length);
			out.println("Generated Password: " + password);
			
			List<Password> passwords = storage.readAll();
			boolean updated = false;
			
			// Check if service already exists
			for (Password p : passwords) {
				if (p.getService().equalsIgnoreCase(service)) {
					p.setUsername(username);
					p.setPassword(password);
					updated = true;
					break;
				}
			}
			
			// If not found, add new entry
			if (!updated) {
				passwords.add(new Password(service, username, password));
			}
			
			storage.writeAll(passwords);
			credentials.put(service, password);
			out.println("Password saved successfully.");
			
		} catch (NumberFormatException e) {
			out.println("Invalid number.");
		}
	}

	/**
	 * @brief Runs the application using the provided Scanner and PrintStream.
	 *
	 * Initiates the application by requesting the master password and
	 * then invoking the interactive menu.
	 *
	 * @param scanner Scanner for user input.
	 * @param out PrintStream for output.
	 */
	public static void runApp(Scanner scanner, PrintStream out) {
		out.print("Enter master password: ");
		String masterPwd = scanner.nextLine();
		PasswordManager pm = new PasswordManager(masterPwd);
		pm.menu(scanner, out);
	}

	/**
	 * @brief Main method to launch the console application.
	 *
	 * Entry point of the application. Initializes input and output streams,
	 * then invokes the runApp method.
	 *
	 * @param args Command-line arguments.
	 */
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		runApp(scanner, System.out);
		scanner.close();
	}
}
