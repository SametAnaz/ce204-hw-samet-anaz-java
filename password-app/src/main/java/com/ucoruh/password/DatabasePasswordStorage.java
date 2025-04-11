package com.ucoruh.password;

import java.sql.*;
import java.util.*;

/**
 * @brief SQLite-based implementation of password storage.
 *
 * This class uses an SQLite database to store, retrieve, update, and delete password entries.
 */
public class DatabasePasswordStorage implements InterfacePasswordStorage {
    private static final String DB_URL = "jdbc:sqlite:passwords.db";

    /**
     * @brief Constructs a DatabasePasswordStorage object and initializes the database.
     */
    public DatabasePasswordStorage() {
        createTableIfNotExists();
    }

    /**
     * @brief Retrieves the database URL for the SQLite connection.
     *
     * @return A string containing the SQLite database URL.
     */
    protected String getDatabaseUrl() {
        return DB_URL;
    }

    /**
     * @brief Creates the passwords table in the database if it does not already exist.
     *
     * This method executes an SQL statement to initialize the database structure required
     * to store password entries.
     */
    private void createTableIfNotExists() {
        try (Connection conn = DriverManager.getConnection(getDatabaseUrl());
             Statement stmt = conn.createStatement()) {
            String sql = """
                CREATE TABLE IF NOT EXISTS passwords (
                    service TEXT PRIMARY KEY,
                    username TEXT NOT NULL,
                    password TEXT NOT NULL
                )
                """;
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Error initializing database: " + e.getMessage());
        }
    }

    /**
     * @brief Adds a new password entry to the database using user input.
     *
     * This method reads service, username, and password from the provided Scanner,
     * and inserts the new entry into the passwords table.
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

        try (Connection conn = DriverManager.getConnection(getDatabaseUrl());
             PreparedStatement pstmt = conn.prepareStatement(
                     "INSERT INTO passwords(service, username, password) VALUES(?, ?, ?)")) {
            pstmt.setString(1, service);
            pstmt.setString(2, user);
            pstmt.setString(3, pass);
            pstmt.executeUpdate();
            System.out.println("Saved.");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * @brief Retrieves and displays all password entries from the database.
     *
     * This method executes an SQL query to obtain all records from the passwords table
     * and prints each entry using the Password class's toString() method.
     */
    @Override
    public void view() {
        try (Connection conn = DriverManager.getConnection(getDatabaseUrl());
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM passwords")) {

            while (rs.next()) {
                System.out.println(new Password(
                        rs.getString("service"),
                        rs.getString("username"),
                        rs.getString("password")));
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * @brief Updates an existing password entry in the database with new username and password.
     *
     * This method prompts the user for the service to update, along with the new username
     * and password, and then updates the corresponding record in the database.
     *
     * @param scanner the Scanner object used to obtain user input.
     */
    @Override
    public void update(Scanner scanner) {
        System.out.print("Service to update: ");
        String service = scanner.nextLine();
        System.out.print("New username: ");
        String username = scanner.nextLine();
        System.out.print("New password: ");
        String password = scanner.nextLine();

        try (Connection conn = DriverManager.getConnection(getDatabaseUrl());
             PreparedStatement pstmt = conn.prepareStatement(
                     "UPDATE passwords SET username = ?, password = ? WHERE service = ?")) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, service);
            int affected = pstmt.executeUpdate();
            if (affected > 0) System.out.println("Updated.");
            else System.out.println("Not found.");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * @brief Deletes a password entry from the database based on the service name.
     *
     * This method prompts the user for the service of the entry to delete and removes the
     * corresponding record from the database.
     *
     * @param scanner the Scanner object used to obtain user input.
     */
    @Override
    public void delete(Scanner scanner) {
        System.out.print("Service to delete: ");
        String service = scanner.nextLine();

        try (Connection conn = DriverManager.getConnection(getDatabaseUrl());
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM passwords WHERE service = ?")) {
            pstmt.setString(1, service);
            int affected = pstmt.executeUpdate();
            if (affected > 0) System.out.println("Deleted.");
            else System.out.println("Not found.");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * @brief Reads all password entries from the database.
     *
     * This method retrieves all records from the passwords table, converts each record into a
     * Password object, and returns a list of these objects.
     *
     * @return A List of Password objects representing all stored password entries.
     */
    @Override
    public List<Password> readAll() {
        List<Password> list = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(getDatabaseUrl());
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM passwords")) {

            while (rs.next()) {
                list.add(new Password(
                        rs.getString("service"),
                        rs.getString("username"),
                        rs.getString("password")));
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return list;
    }

    /**
     * @brief Writes a list of password entries to the database.
     *
     * This method clears the existing contents of the passwords table and inserts all password entries
     * from the provided list.
     *
     * @param list A List of Password objects to be written to the database.
     */
    @Override
    public void writeAll(List<Password> list) {
        try (Connection conn = DriverManager.getConnection(getDatabaseUrl());
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM passwords"); // clear all
            for (Password p : list) {
                try (PreparedStatement pstmt = conn.prepareStatement(
                        "INSERT INTO passwords(service, username, password) VALUES (?, ?, ?)")) {
                    pstmt.setString(1, p.getService());
                    pstmt.setString(2, p.getUsername());
                    pstmt.setString(3, p.getPassword());
                    pstmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
