package com.ucoruh.password;

import java.sql.*;
import java.util.*;

/**
 * SQLite-based implementation of password storage.
 */
public class DatabasePasswordStorage implements InterfacePasswordStorage {

    private static final String DB_URL = "jdbc:sqlite:passwords.db";

    public DatabasePasswordStorage() {
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
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

    @Override
    public void add(Scanner scanner) {
        System.out.print("Service: ");
        String service = scanner.nextLine();
        System.out.print("Username: ");
        String user = scanner.nextLine();
        System.out.print("Password: ");
        String pass = scanner.nextLine();

        try (Connection conn = DriverManager.getConnection(DB_URL);
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

    @Override
    public void view() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
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

    @Override
    public void update(Scanner scanner) {
        System.out.print("Service to update: ");
        String service = scanner.nextLine();
        System.out.print("New username: ");
        String username = scanner.nextLine();
        System.out.print("New password: ");
        String password = scanner.nextLine();

        try (Connection conn = DriverManager.getConnection(DB_URL);
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

    @Override
    public void delete(Scanner scanner) {
        System.out.print("Service to delete: ");
        String service = scanner.nextLine();

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM passwords WHERE service = ?")) {
            pstmt.setString(1, service);
            int affected = pstmt.executeUpdate();
            if (affected > 0) System.out.println("Deleted.");
            else System.out.println("Not found.");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Override
    public List<Password> readAll() {
        List<Password> list = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL);
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

    @Override
    public void writeAll(List<Password> list) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
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
