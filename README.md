# Password Manager Application

[![GitHub Release](https://img.shields.io/github/release/SametAnaz/ce204-hw-samet-anaz-java.svg)](https://github.com/SametAnaz/ce204-hw-samet-anaz-java/releases/latest)
[![Build Status](https://img.shields.io/github/workflow/status/SametAnaz/ce204-hw-samet-anaz-java/Build)](https://github.com/SametAnaz/ce204-hw-samet-anaz-java/actions)
[![License](https://img.shields.io/github/license/SametAnaz/ce204-hw-samet-anaz-java)](LICENSE)
[![Java Version](https://img.shields.io/badge/java-17-orange.svg)](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
[![Maven](https://img.shields.io/badge/maven-3.8.1-blue.svg)](https://maven.apache.org/)

## 📋 Table of Contents

- [Overview](#-overview)
- [Features](#-features)
- [System Requirements](#-system-requirements)
- [Installation](#-installation)
- [Usage](#-usage)
- [Architecture](#-architecture)
  - [Core Components](#core-components)
  - [Storage Systems](#storage-systems)
  - [Security Features](#security-features)
  - [UML Diagrams](#uml-diagrams)
  - [GUI Components](#gui-components)
  - [Test Components](#test-components)
- [Development](#-development)
- [Testing](#-testing)
- [Documentation](#-documentation)
- [Contributing](#-contributing)
- [License](#-license)

## 🔍 Overview

The Password Manager Application is a robust, secure, and user-friendly password management solution developed using Java and the Maven framework. It provides comprehensive password management capabilities with a focus on security, usability, and extensibility.

## ✨ Features

- 🔐 Secure password storage with master password protection
- 🎲 Advanced password generation with customizable options
- 👁️ Intuitive password viewing and management
- 📋 Clipboard integration for easy password copying
- 🔄 Auto-login functionality
- 🌐 Cross-platform compatibility
- 🎨 Modern, user-friendly GUI interface
- 🔒 Multiple storage options (File-based and SQLite database)
- 🛡️ Strong encryption for all sensitive data
- 📱 Platform-specific credential management

## 💻 System Requirements

- Java 17 or higher
- Maven 3.8.1 or higher
- SQLite support
- Minimum 2GB RAM
- 100MB free disk space

## 🚀 Installation

1. Clone the repository:
```bash
git clone https://github.com/SametAnaz/ce204-hw-samet-anaz-java.git
```

2. Navigate to the project directory:
```bash
cd ce204-hw-samet-anaz-java
```

3. Build the project:
```bash
mvn clean install
```

4. Run the application:
```bash
java -jar target/password-app-1.0-SNAPSHOT.jar
```

## 🎯 Usage

The application provides both GUI and command-line interfaces. After launching, you'll need to:

1. Set up a master password (first-time use)
2. Log in using your master password
3. Access various features through the main menu:
   - Password Management
   - Password Generation
   - Auto-Login Configuration
   - Platform Settings

### GUI Interface

The graphical interface provides easy access to all features:

- **Main Window**: Central dashboard for all password management operations
- **Add Password**: Create new password entries with service and username
- **View Passwords**: List and search all stored passwords
- **Update Password**: Modify existing password entries
- **Generate Password**: Create strong, customized passwords
- **Delete Password**: Remove unwanted password entries

### Command-Line Interface

For users who prefer terminal-based operations:

```bash
# View all passwords
1. Select option 2 (Secure Storage of Passwords)
2. Select option 1 (View Passwords)

# Add new password
1. Select option 2 (Secure Storage of Passwords)
2. Select option 2 (Add Password)

# Generate password
1. Select option 3 (Password Generator)
2. Enter desired length
```

## 🏗️ Architecture

### Core Components

#### Authentication System
- `AuthManager`: Singleton class managing user authentication
- Master password creation and verification
- Secure password hashing using `EncryptionUtil`

#### Password Management
- `PasswordManager`: Core class handling password operations
- Credential storage and retrieval
- Integration with storage systems

#### Storage Systems
- `InterfacePasswordStorage`: Abstract storage interface
- `FilePasswordStorage`: File-based implementation
- `DatabasePasswordStorage`: SQLite database implementation
- `PasswordStorageFactory`: Factory pattern for storage creation

#### Security Features
- Strong encryption for all stored passwords
- Secure master password handling
- Memory-safe data management
- Clipboard security measures

### UML Diagrams

#### Core System Diagrams
![Class Diagram](assets/UmlClassDiagram.png)
![Sequence Diagram](assets/Squence.png)
![C4 Diagram](assets/C4.png)

#### Architectural Views
![Context Diagram](assets/ContextDiagram.png)
![Container Diagram](assets/Cointainer.png)
![C4 Code Diagram](assets/C4codeDiagram.png)
![Use Case Diagram](assets/UsaCase.png)

### GUI Components

#### Password Management Interface
![Add Password Controller](assets/AddPasswordControllerUML.png)
![Delete Password Controller](assets/DeletePasswordController.png)
![Dialog Controller](assets/DialogController.png)

#### Password Generation Interface
![Generate Password Controller 1](assets/GeneratePasswordController_1.png)
![Generate Password Controller 2](assets/GeneratePasswordController_2.png)
![Generate Password Controller 3](assets/GeneratePasswordController_3.png)

#### Main GUI Components
![PasswordManagerGUI Class 1](assets/PasswordManagerGUI_1.png)
![PasswordManagerGUI Class 2](assets/PasswordManagerGUI_2.png)
![PasswordManagerGUI Class 3](assets/PasswordManagerGUI_3.png)

#### Password Operations Interface
![Update Password Controller 1](assets/UpdatePasswordController_1.png)
![Update Password Controller 2](assets/UpdatePasswordController_2.png)
![Update Password Controller 3](assets/UpdatePasswordController_3.png)
![View Password Controller 1](assets/ViewPasswordController_1.png)
![View Password Controller 2](assets/ViewPasswordController_2.png)
![View Password Controller 3](assets/ViewPasswordController_3.png)

### Test Components

#### Authentication Testing
![Auth Manager Test 1](assets/AuthManagerTestUML_1.png)
![Auth Manager Test 2](assets/AuthManagerTestUML_2.png)
![Auth Manager Test 3](assets/AuthManagerTestUML_3.png)

#### Feature Testing
![Auto Login Manager Test](assets/AutoLoginManagerTest.png)
![Database Password Storage Test](assets/DatabasePasswordStorageTest.png)
![Encryption Util Test](assets/EncryptionUtilTest.png)
![File Password Storage Test](assets/FilePasswordStorageTest.png)
![Password Storage Factory Test](assets/PasswordStorageFactoryTest.png)

## 👨‍💻 Development

The project follows standard Java development practices and uses Maven for dependency management. Key components include:

### Core Classes

```java
// Password Class - Represents a stored password entry
public class Password {
    private String service;    // Service name (e.g., Gmail, Facebook)
    private String username;   // Associated username
    private String password;   // Encrypted password
    // ... methods for access and modification
}

// PasswordManager Class - Manages password operations
public class PasswordManager {
    private final Map<String, String> credentials;
    private final String masterPassword;
    private final InterfacePasswordStorage storage;
    
    // ... methods for password management
}

// AuthManager Class - Handles authentication
public class AuthManager {
    private static AuthManager instance;
    private String masterPassword;
    
    // ... authentication methods
}
```

### Storage Implementation

```java
// File-based Storage
public class FilePasswordStorage implements InterfacePasswordStorage {
    private static final String FILE = "passwords.txt";
    private final String masterPassword;
    
    // ... file operations
}

// Database Storage
public class DatabasePasswordStorage implements InterfacePasswordStorage {
    private static final String DB_URL = "jdbc:sqlite:passwords.db";
    private final String masterPassword;
    
    // ... database operations
}
```

## 🧪 Testing

The project includes comprehensive unit tests and integration tests:

### Unit Tests
- Authentication testing
- Password management testing
- Storage system testing
- Encryption testing
- GUI component testing

### Integration Tests
- End-to-end workflow testing
- Storage system integration
- GUI integration testing

Run tests using:
```bash
mvn test
```

Test coverage reports are generated in the `target/site/jacoco` directory.

## 📚 Documentation

- [Java Installation Guide](Java_Installation_Guide_Windows.md)
- [Maven Installation Guide](Maven_Installation_Guide_Windows.md)
- [API Documentation](target/site/apidocs/index.html)
- [Test Reports](target/site/surefire-report.html)

### Additional Resources
- JavaDoc documentation for all classes
- UML diagrams for architecture visualization
- Test coverage reports
- User guides and tutorials

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

### Development Guidelines
- Follow Java coding standards
- Document all public APIs
- Update UML diagrams for architectural changes

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

Built with ❤️ by [Samet Anaz](https://github.com/SametAnaz)

