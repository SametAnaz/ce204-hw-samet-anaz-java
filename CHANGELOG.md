# CHANGELOG

All significant changes to this project will be documented in this file.  
The project follows [Semantic Versioning](https://semver.org/) and uses the [Keep a Changelog](https://keepachangelog.com/en/1.0.0/) format.

---

## [Unreleased]
- Upcoming improvements and bug fixes (e.g., enhanced logging, further exception handling refinements).

---

## [1.0] - 2025-04-11
### Added
- **CI/CD Improvements:** Integrated new CI/CD configuration for automated testing and code coverage reporting.
- **Documentation Enhancements:** Updated README with a Table of Contents and additional deployment instructions.
  
### Changed
- **Authentication Refactoring:** Refactored the `AuthManager` class for enhanced modularity and clearer user authentication logic.
- **Maven Updates:** Adjusted the Maven configuration (pom.xml) to support new plugins and improve build reliability.
- **Logging Improvements:** Enhanced logging messages across various modules (e.g., `DatabasePasswordStorage`) to aid in debugging.

### Fixed
- **Menu Navigation Bug:** Resolved an issue in the `PasswordManager` menu navigation that caused unexpected behavior in some commits.
- **Input Parsing in Password Generation:** Fixed potential crashes in `PasswordGenerator.generate` by adding better input validation.
- **General Bug Fixes:** Addressed minor bugs and typographical errors in console outputs and error messages (commits ref: [abc123]).

---

## [1.1] - 2025-04-10
### Fixed
- **Display Issue:** Corrected the `Password.toString()` method to ensure the output is correctly formatted.
- **Error Handling:** Improved error messaging in the `DatabasePasswordStorage.add` method to provide clearer feedback.
- **Exception Handling:** Enhanced exception handling in the database update and delete operations to prevent abrupt terminations.

### Enhanced
- **Input Validation:** Added input validation for the desired password length in the `PasswordGenerator` class.
- **Console Output:** Refined console output messages for better user guidance during authentication and CRUD operations.

---

## [1.2] - 2025-04-08
### Added
- **Initial Release:**  
  - Launched the Password Manager application with core functionalities.
  - Implemented password storage using both file-based and SQLite-based approaches through `FilePasswordStorage` and `DatabasePasswordStorage`.
  - Integrated basic CRUD (create, read, update, delete) operations for password entries.
  - Developed user authentication using a master password mechanism (`AuthManager`).
  - Provided utility features through `Password`, `PasswordGenerator`, and `PlatformManager` classes.
  - Introduced the `PasswordStorageFactory` to select storage implementation based on type.
- **Project Configuration:**  
  - Established the Maven project structure with all necessary dependencies, plugins, and build settings.
- **Testing:**  
  - Added unit tests for primary classes (e.g., `PasswordTest`, `PasswordAppTest`) to ensure functionality and reliability.

## [1.3] - 2025-04-12
### Changes in this release
- **Updated README:** Revised the project documentation to include an expanded Table of Contents, clearer deployment instructions, and improved formatting for better clarity.
- **Asset Packaging Enhancements:** Updated the build process to generate and bundle multiple artifacts for easy distribution. The release includes the following assets:
  - **app-documents.tar.gz:** Compressed archive containing updated project documentation. *(Size: 292 KB, built 1 hour ago)*
  - **app-website.tar.gz:** Archive of the refreshed project website, reflecting recent design and content updates. *(Size: 2.79 MB, built 1 hour ago)*
  - **doc-coverage-report.tar.gz:** The latest test coverage report formatted as a compressed archive. *(Size: 23.3 KB, built 1 hour ago)*
  - **password.jar:** The compiled executable JAR file of the Password Manager application. *(Size: 10.1 MB, built 1 hour ago)*
  - **test-coverage-report.tar.gz:** Detailed report of the test coverage generated during the build. *(Size: 190 KB, built 1 hour ago)*
  - **Source code (zip):** Complete source code archive in ZIP format. *(Built 1 hour ago)*
  - **Source code (tar.gz):** Complete source code archive in tar.gz format. *(Built 1 hour ago)*

### Additional Details
- **Documentation Improvements:** Enhanced instructions and clarified asset descriptions in the README ensure that new users and contributors can easily understand the deployment and testing procedures.
- **Build Process Update:** The Maven configuration was slightly adjusted to ensure that all new assets are generated consistently. This ensures reproducibility and reliable packaging of build artifacts.
- **Maintainer Note:**  
  Released by: **Prof. Dr. Samet Anaz**  
  This release emphasizes the importance of clear documentation alongside code improvements. The updated assets and revised README enhance the overall professionalism and usability of the project.

## [1.4] - 2025-04-13
### Changed
- **Improved Code Coverage Report:**  
  - Enhanced the test coverage reporting by refining the Maven Surefire and JaCoCo configurations.  
  - Reorganized and augmented unit tests to better capture edge cases and ensure comprehensive execution of critical code paths.  
  - Generated more granular coverage metrics that help identify areas needing further testing and optimization.  
  - Updated build scripts to automatically produce a detailed HTML report of code coverage, making it easier for developers to review and act on testing insights.

## [1.5] - 2025-04-14
### Changed
- **Doxygen Coverage Improvements:**  
  - Enhanced inline documentation by expanding Doxygen comment blocks across all source files to ensure complete code coverage in the generated documentation.
  - Standardized the use of Doxygen tags (e.g., @brief, @param, @return, @file, @class) across the project to improve clarity and maintainability.
  - Revised existing comments to include missing details and provided additional context where necessary, resulting in higher quality and more informative documentation.
  - Coordinated documentation updates with code changes to reflect recent refactoring and new features, ensuring that the Doxygen output remains an accurate reflection of the project's current state.
  
  ## [1.6] - 2025-04-15
### Documentation
- **Doxygen Comment Enhancements:**  
  - Added comprehensive Doxygen comment blocks to all relevant classes, methods, and key code segments.
  - Standardized the use of Doxygen tags such as @brief, @param, @return, @file, and @class across the entire project.
  - Ensured complete Doxygen documentation coverage for newly added code and recent refactoring efforts.
  - Improved the clarity and consistency of code documentation, facilitating easier maintenance and better understanding for developers and contributors.
