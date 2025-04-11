package com.ucoruh.password;

/**
 * @brief Enum for supported storage types.
 *
 * This enum defines the types of storage available for password storage,
 * including file-based and SQLite-based implementations.
 */
public enum StorageType {
    /**
     * @brief Represents file-based storage.
     */
    FILE,
    
    /**
     * @brief Represents SQLite-based storage.
     */
    SQLITE
}
