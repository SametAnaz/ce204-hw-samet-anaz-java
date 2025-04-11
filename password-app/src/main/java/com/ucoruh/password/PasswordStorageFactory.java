package com.ucoruh.password;

/**
 * @brief Factory class to create password storage implementations.
 *
 * This class provides a static factory method to return an appropriate instance of InterfacePasswordStorage
 * based on the provided storage type. It supports different storage implementations such as file-based storage
 * and SQLite-based storage.
 */
public class PasswordStorageFactory {

    /**
     * @brief Returns a password storage implementation based on the selected type.
     *
     * This method uses a switch expression to determine which implementation of InterfacePasswordStorage
     * to instantiate depending on the given storage type.
     *
     * @param type the storage type (e.g., FILE, SQLITE, etc.).
     * @return An instance of InterfacePasswordStorage corresponding to the provided storage type.
     */
    public static InterfacePasswordStorage create(StorageType type) {
        return switch (type) {
            case FILE -> new FilePasswordStorage();
            case SQLITE -> new DatabasePasswordStorage();
        };
    }
}
