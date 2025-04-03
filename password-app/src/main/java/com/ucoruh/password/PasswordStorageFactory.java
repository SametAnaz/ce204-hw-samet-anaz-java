package com.ucoruh.password;

/**
 * Factory class to create password storage implementations.
 */
public class PasswordStorageFactory {

    /**
     * Returns a password storage implementation based on the selected type.
     *
     * @param type the storage type (FILE, SQLITE, etc.)
     * @return an implementation of InterfacePasswordStorage
     */
    public static InterfacePasswordStorage create(StorageType type) {
        return switch (type) {
            case FILE -> new FilePasswordStorage();
            case SQLITE -> throw new UnsupportedOperationException("SQLite storage not implemented yet.");
        };
    }
}
