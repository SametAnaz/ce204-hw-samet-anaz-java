package com.ucoruh.password;

/**
 * @file Password.java
 * @class Password
 * @brief Represents a stored password entry for a specific service.
 *
 * This class provides a structure to store and manage credentials such as
 * service name, username, and password. You can retrieve or update these
 * fields through the provided getter and setter methods.
 */
public class Password {
    /**
     * @brief The name of the service (e.g., Gmail, Facebook).
     */
    private String service;

    /**
     * @brief The associated username for the service.
     */
    private String username;

    /**
     * @brief The password string for the service.
     */
    private String password;

    /**
     * @brief Constructs a Password object with service, username, and password.
     *
     * @param service the service name (e.g., Gmail, Facebook)
     * @param username the associated username
     * @param password the password string
     */
    public Password(String service, String username, String password) {
        this.service = service;
        this.username = username;
        this.password = password;
    }

    /**
     * @brief Returns the service name.
     *
     * @return the service name associated with this password entry.
     */
    public String getService() {
        return service;
    }

    /**
     * @brief Returns the username.
     *
     * @return the username associated with this password entry.
     */
    public String getUsername() {
        return username;
    }

    /**
     * @brief Returns the password string.
     *
     * @return the password string associated with this password entry.
     */
    public String getPassword() {
        return password;
    }

    /**
     * @brief Sets a new username for this password entry.
     *
     * @param username the new username to be set for this password entry.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @brief Sets a new password for this password entry.
     *
     * @param password the new password string to be set for this password entry.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @brief Returns a string representation of the Password object.
     *
     * @return a string with service, username, and password details.
     */
    @Override
    public String toString() {
        return "Service: " + service + " | Username: " + username + " | Password: " + password;
    }
}
