package com.ucoruh.password;

/**
 * Represents a stored password entry for a specific service.
 */
public class Password {
    private String service;
    private String username;
    private String password;

    /**
     * Constructs a Password object with service, username, and password.
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

    public String getService() {
        return service;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Service: " + service + " | Username: " + username + " | Password: " + password;
    }
}
