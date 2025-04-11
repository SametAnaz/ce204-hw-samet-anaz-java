package com.ucoruh.password;

/**
 * @brief Represents a stored password entry for a specific service.
 */
public class Password {
    private String service;
    private String username;
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
     * @return the password string.
     */
    public String getPassword() {
        return password;
    }

    /**
     * @brief Sets a new username.
     *
     * @param username the new username to be set for this password entry.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @brief Sets a new password.
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
