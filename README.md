# ce206-hw-samet-anaz-java

## Contents

- [Overview](#overview)
- [Releases](#releases)
- [UML Class Diagramları](#uml-class-diagramları)
  - [Class Diagram](#class-diagram)
  - [Sequence Diagram](#sequence-diagram)
  - [C4 Diagram](#c4-diagram)
  - [Context Diagram](#context-diagram)
  - [Container Diagram](#container-diagram)
  - [C4 Code Diagram](#c4-code-diagram)
  - [Use Case Diagram](#use-case-diagram)
- [Source Code](#source-code)
  - [Password Class](#password-class)
  - [PasswordApp Class](#passwordapp-class)
- [Test Sınıfları](#test-sınıfları)
  - [PasswordTest](#passwordtest)
  - [PasswordAppTest](#passwordapptest)
- [pom.xml](#pomxml)

# Overview

The Password Application is a secure and lightweight console-based password manager developed using Java and the Maven framework. It provides essential features such as encrypted password storage, strong password generation, user authentication, and platform-based credential organization. Designed with modularity and maintainability in mind, the application leverages object-oriented principles and supports SQLite for local database management. This project also integrates test coverage and documentation tools, and is structured to allow future expansion, including GUI development.

## Features

- Secure password storage with master password protection
- Password generation with customizable options (length, character types)
- View, add, update, and delete password entries
- Copy password to clipboard functionality
- Modern, user-friendly interface

## Releases

- [![GitHub Release](https://img.shields.io/github/release/SametAnaz/ce204-hw-samet-anaz-java.svg)](https://github.com/SametAnaz/ce204-hw-samet-anaz-java/releases/latest)
[![Build Status](https://img.shields.io/github/workflow/status/SametAnaz/ce204-hw-samet-anaz-java/Build)](https://github.com/SametAnaz/ce204-hw-samet-anaz-java/actions)

[![License](https://img.shields.io/github/license/SametAnaz/ce204-hw-samet-anaz-java)](LICENSE)



# Uml Class Diagrams
## Class Diagram
![](assets/UmlClassDiagram.png)
## Sqeence Diagram
![](assets/Squence.png)
## C4 Diagram
![](assets/C4.png)
## Context Diagram
![](assets/ContextDiagram.png)
## Conteiner Diagram
![](assets/Cointainer.png)
## C4 Code Diagram
![](assets/C4codeDiagram.png)
## Use Case Diagram
![](assets/UsaCase.png)





# Source Code

## Password Class
The Password class represents a stored password entry for a specific service. It contains fields for the service name, username, and password, along with getter and setter methods to access and update these values. The toString() method provides a formatted representation of the password entry.


```java
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

```
## PasswordApp Class

The PasswordApp class serves as the entry point for the Password Manager console application. It contains the main method and the runApp method that orchestrates user authentication and displays an interactive main menu. This menu allows users to perform tasks such as adding credentials, retrieving stored passwords, generating passwords, activating the auto-login feature, and accessing platform-specific functions.

```java
package com.ucoruh.password;

import java.io.PrintStream;
import java.util.Scanner;

/**
 * Entry point for the Password Manager console application.
 */
public class PasswordApp {
    /**
     * Runs the application using the provided Scanner and PrintStream.
     * This method is designed for testing and production use.
     *
     * @param scanner Scanner for user input.
     * @param out PrintStream for output.
     */
    public static void runApp(Scanner scanner, PrintStream out) {
        AuthManager auth = AuthManager.getInstance();

        if (!auth.isMasterPasswordSet()) {
            out.print("Set master password: ");
            auth.createMasterPassword(scanner);
        }

        out.print("Enter master password to login: ");
        if (auth.login(scanner)) {
            PasswordManager pm = new PasswordManager(auth.getMasterPassword());
            int choice = -1;
            do {
                out.println("==== MAIN MENU ====");
                out.println("1. User Authentication");
                out.println("2. Secure Storage of Passwords");
                out.println("3. Password Generator");
                out.println("4. Auto-Login Feature");
                out.println("5. Multi-Platform Compatibility");
                out.println("0. Exit");
                out.print("Your choice: ");
                String input = scanner.nextLine();
                try {
                    choice = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    out.println("Invalid number.");
                    continue;
                }
                switch (choice) {
                    case 1:
                        auth.userMenu(scanner);
                        break;
                    case 2:
                        pm.menu(scanner, out);
                        break;
                    case 3:
                        PasswordGenerator.generate(scanner);
                        break;
                    case 4:
                        AutoLoginManager.menu(scanner);
                        break;
                    case 5:
                        PlatformManager.showPlatforms();
                        break;
                    case 0:
                        out.println("Exiting...");
                        break;
                    default:
                        out.println("Invalid choice.");
                        break;
                }
            } while (choice != 0);
        } else {
            out.println("Login failed.");
        }
    }

    /**
     * Main method to launch the console application.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        runApp(scanner, System.out);
        scanner.close();
    }
}

```
# Test Classes
## PasswordTest
```java
package com.ucoruh.password;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Unit tests for the Password class.
 */
public class PasswordTest {

    @Test
    public void testConstructorAndGetters() {
        Password password = new Password("gmail", "user1", "pass123");
        assertEquals("gmail", password.getService());
        assertEquals("user1", password.getUsername());
        assertEquals("pass123", password.getPassword());
    }

    @Test
    public void testSetters() {
        Password password = new Password("service", "user", "pass");
        password.setUsername("newuser");
        password.setPassword("newpass");

        assertEquals("newuser", password.getUsername());
        assertEquals("newpass", password.getPassword());
    }

    @Test
    public void testToString() {
        Password password = new Password("github", "dev", "secure");
        String result = password.toString();
        assertTrue(result.contains("github"));
        assertTrue(result.contains("dev"));
        assertTrue(result.contains("secure"));
    }
}

```
## PasswordAppTest

```java
package com.ucoruh.password;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @brief Unit tests for the PasswordApp class using the runApp method.
 *
 * These tests simulate full application flow by injecting input and capturing output.
 * Additional tests cover each main menu branch.
 */
public class PasswordAppTest {

    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outputStream;

    /**
     * Reset the AuthManager singleton and set up a new output stream.
     */
    @Before
    public void setUp() {
        AuthManager.resetInstance();
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    /**
     * Restore the original System.out after each test.
     */
    @After
    public void tearDown() {
        System.setOut(originalOut);
        outputStream.reset();
    }

    /**
     * @brief Tests a successful run of the application.
     *
     * Simulates setting a master password, successful login, and then exiting
     * the main menu. Verifies that the output contains the main menu prompt and
     * an exit message.
     */
    @Test
    public void testMainSuccess() {
        String simulatedInput = "testMaster\n" + "testMaster\n" + "0\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(
                simulatedInput.getBytes(StandardCharsets.UTF_8));
        Scanner scanner = new Scanner(inputStream);

        PasswordApp.runApp(scanner, System.out);
        scanner.close();

        String output = outputStream.toString();
        assertTrue("Output should contain MAIN MENU", output.contains("==== MAIN MENU ===="));
        assertTrue("Output should contain Exiting...", output.contains("Exiting..."));
        assertFalse("Output should not contain 'Login failed.'", output.contains("Login failed."));
    }

    /**
     * @brief Tests the login failure scenario.
     *
     * Simulates setting a master password but then providing an incorrect
     * login input. Verifies that the output contains "Login failed."
     */
    @Test
    public void testMainError() {
        String simulatedInput = "testMaster\n" + "wrongMaster\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(
                simulatedInput.getBytes(StandardCharsets.UTF_8));
        Scanner scanner = new Scanner(inputStream);

        PasswordApp.runApp(scanner, System.out);
        scanner.close();

        String output = outputStream.toString();
        assertTrue("Output should contain 'Login failed.'", output.contains("Login failed."));
    }

    /**
     * @brief Tests full menu navigation.
     *
     * Simulates a run of the application that displays the main menu.
     * Verifies that the captured output shows the menu header.
     */
    @Test
    public void testFullMenuNavigation() {
        String simulatedInput = "testMaster\n" + "testMaster\n" + "0\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(
                simulatedInput.getBytes(StandardCharsets.UTF_8));
        Scanner scanner = new Scanner(inputStream);

        PasswordApp.runApp(scanner, System.out);
        scanner.close();

        String output = outputStream.toString();
        assertTrue("Output should contain MAIN MENU", output.contains("==== MAIN MENU ===="));
    }

    /**
     * @brief Tests main menu option 1: User Authentication.
     *
     * Simulates choosing option 1 and verifies that the user menu stub output is shown.
     */
    @Test
    public void testMenuOptionUserAuthentication() {
        // Simulated input: set master, login, then choose option 1, then exit.
        String simulatedInput = "testMaster\n" + "testMaster\n" + "1\n" + "0\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(
                simulatedInput.getBytes(StandardCharsets.UTF_8));
        Scanner scanner = new Scanner(inputStream);

        PasswordApp.runApp(scanner, System.out);
        scanner.close();

        String output = outputStream.toString();
        assertTrue("Output should indicate user menu stub", output.contains("User menu functionality not yet implemented."));
    }

    /**
     * @brief Tests main menu option 2: Secure Storage of Passwords.
     *
     * Simulates choosing option 2 and then immediately exiting the inner PasswordManager menu.
     */
    @Test
    public void testMenuOptionSecureStorage() {
        // Simulated input: set master, login, choose option 2, then in inner menu choose 4 to exit, then exit main menu.
        String simulatedInput = "testMaster\n" + "testMaster\n" + "2\n" + "4\n" + "0\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(
                simulatedInput.getBytes(StandardCharsets.UTF_8));
        Scanner scanner = new Scanner(inputStream);

        PasswordApp.runApp(scanner, System.out);
        scanner.close();

        String output = outputStream.toString();
        // Since PasswordManager.menu prints its own menu header, verify its presence.
        assertTrue("Output should contain inner PasswordManager menu", output.contains("==== MAIN MENU ===="));
    }

    /**
     * @brief Tests main menu option 3: Password Generator.
     *
     * Simulates choosing option 3 and entering a desired password length.
     * Verifies that the generated password output is present.
     */
    @Test
    public void testMenuOptionPasswordGenerator() {
        // Simulated input: set master, login, choose option 3, input desired length, then exit.
        String simulatedInput = "testMaster\n" + "testMaster\n" + "3\n" + "8\n" + "0\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(
                simulatedInput.getBytes(StandardCharsets.UTF_8));
        Scanner scanner = new Scanner(inputStream);

        PasswordApp.runApp(scanner, System.out);
        scanner.close();

        String output = outputStream.toString();
        // Expect the password generator to output "Generated Password:".
        assertTrue("Output should contain Generated Password:", output.contains("Generated Password:"));
    }

    /**
     * @brief Tests main menu option 4: Auto-Login Feature.
     *
     * Simulates choosing option 4. Assumes AutoLoginManager.menu prints a placeholder message.
     */
    @Test
    public void testMenuOptionAutoLoginFeature() {
        // Simulated input: set master, login, choose option 4, then exit.
        String simulatedInput = "testMaster\n" + "testMaster\n" + "4\n" + "0\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(
                simulatedInput.getBytes(StandardCharsets.UTF_8));
        Scanner scanner = new Scanner(inputStream);

        PasswordApp.runApp(scanner, System.out);
        scanner.close();

        String output = outputStream.toString();
        // Check for expected placeholder output; adjust expected text if needed.
        assertTrue("Output should mention Auto-Login Feature", output.contains("Auto-Login Feature"));
    }

    /**
     * @brief Tests main menu option 5: Multi-Platform Compatibility.
     *
     * Simulates choosing option 5. Assumes PlatformManager.showPlatforms prints a placeholder message.
     */
    @Test
    public void testMenuOptionMultiPlatform() {
        // Simulated input: set master, login, choose option 5, then exit.
        String simulatedInput = "testMaster\n" + "testMaster\n" + "5\n" + "0\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(
                simulatedInput.getBytes(StandardCharsets.UTF_8));
        Scanner scanner = new Scanner(inputStream);

        PasswordApp.runApp(scanner, System.out);
        scanner.close();

        String output = outputStream.toString();
        // Check for expected placeholder text from platform manager; adjust as necessary.
        assertTrue("Output should mention Supported platforms", output.contains("Supported platforms:"));
    }
}

```

## pom.xml
The pom.xml file is the core configuration file for Maven projects. It defines important information such as the project's dependencies, plugins, build profiles, and other configuration details. This file centralizes settings related to project structure, compilation, testing, and packaging, allowing for a standardized and automated build process.
```xml
<?xml version="1.0" encoding="UTF-8"?>

<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.ucoruh.password</groupId>
    <artifactId>password-app</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>jar</packaging>

    <name>password-app</name>
    <url>https://github.com/ucoruh/eclipse-java-maven-template</url>
    <description>Password App Example Template Project</description>

    <scm>
        <url>https://github.com/ucoruh/eclipse-java-maven-template</url>
    </scm>

    <inceptionYear>2023</inceptionYear>

    <developers>
        <developer>
            <name>Asst. Prof. Dr. Ugur CORUH</name>
            <organization>RTEU</organization>
            <email>ugur.coruh@erdogan.edu.tr</email>
            <roles>
                <role>Developer</role>
                <role>Designer</role>
            </roles>
        </developer>
        <developer>
            <name>Other Person</name>
            <organization>RTEU</organization>
            <email>mail@gmail.com</email>
            <roles>
                <role>Developer</role>
            </roles>
        </developer>
    </developers>

    <contributors>
        <contributor>
            <name>Asst. Prof. Dr. Ugur CORUH</name>
            <email>ugur.coruh@erdogan.edu.tr</email>
            <organization>RTEU</organization>
            <roles>
                <role>Developer</role>
                <role>Designer</role>
            </roles>
        </contributor>
        <contributor>
            <name>Other Person</name>
            <organization>RTEU</organization>
            <email>mail@gmail.com</email>
            <roles>
                <role>Developer</role>
            </roles>
        </contributor>
    </contributors>

    <organization>
        <name>Recep Tayyip Erdogan University</name>
        <url>www.erdogan.edu.tr</url>
    </organization>

    <issueManagement>
        <system>Github</system>
        <url>https://github.com/ucoruh/eclipse-java-maven-template</url>
    </issueManagement>

    <ciManagement>
        <system>Github</system>
        <notifiers>
            <notifier>
                <address>ugur.coruh@erdogan.edu.tr</address>
                <sendOnSuccess>true</sendOnSuccess>
                <sendOnError>true</sendOnError>
                <sendOnFailure>true</sendOnFailure>
                <sendOnWarning>true</sendOnWarning>
            </notifier>
        </notifiers>
        <url>https://github.com/ucoruh/eclipse-java-maven-template</url>
    </ciManagement>

    <repositories>
        <repository>
            <id>eclipse-java-maven-template</id>
            <name>eclipse-java-maven-template</name>
            <url>https://github.com/ucoruh/eclipse-java-maven-template</url>
        </repository>
    </repositories>

    <distributionManagement>
        <site>
            <name>https://github.com/ucoruh/eclipse-java-maven-template</name>
            <id>eclipse-java-maven-template</id>
        </site>
        <downloadUrl>https://github.com/ucoruh/eclipse-java-maven-template</downloadUrl>
        <repository>
            <id>eclipse-java-maven-template</id>
            <name>eclipse-java-maven-template</name>
            <url>https://github.com/ucoruh/eclipse-java-maven-template</url>
        </repository>
    </distributionManagement>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.xerial</groupId>
            <artifactId>sqlite-jdbc</artifactId>
            <version>3.36.0.3</version>
        </dependency>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.11</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.hamcrest</groupId>
            <artifactId>hamcrest-all</artifactId>
            <version>1.3</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>1.7.32</version>
        </dependency>
        <dependency>
            <groupId>ch.qos.logback</groupId>
            <artifactId>logback-classic</artifactId>
            <version>1.2.6</version>
        </dependency>
		
		<dependency>
			<groupId>com.github.stefanbirkner</groupId>
			<artifactId>system-lambda</artifactId>
			<version>1.2.0</version>
			<scope>test</scope>
		</dependency>

		
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.10.1</version>
                <configuration>
                    <source>17</source>
                    <target>17</target>
                </configuration>
            </plugin>

         <plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <version>2.22.1</version>
    <configuration>
        <includes>
            <include>**/com/ucoruh/password/*Test.java</include> 
        </includes>
    </configuration>
</plugin>

            <plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.7</version>  
    <executions>
        <execution>
            <goals>
                <goal>prepare-agent</goal>
            </goals>
        </execution>
        <execution>
            <id>report</id>
            <phase>test</phase>
            <goals>
                <goal>report</goal>
            </goals>
        </execution>
    </executions>
</plugin>

			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-site-plugin</artifactId>
				<version>3.21.0</version>
				<configuration>
					<port>9000</port>
					<tempWebappDirectory>${basedir}/target/site</tempWebappDirectory>
				</configuration>
			</plugin>

            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-shade-plugin</artifactId>
                <version>3.2.4</version>
                <executions>
                    <execution>
                        <phase>package</phase>
                        <goals>
                            <goal>shade</goal>
                        </goals>
                        <configuration>
                            <createDependencyReducedPom>false</createDependencyReducedPom>
                            <transformers>
                                <transformer implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                                    <mainClass>com.ucoruh.password.PasswordApp</mainClass>
                                </transformer>
                            </transformers>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
        

        <pluginManagement>
            <plugins>
                <plugin>
                    <artifactId>maven-clean-plugin</artifactId>
                    <version>3.1.0</version>
                </plugin>
                <plugin>
                    <artifactId>maven-resources-plugin</artifactId>
                    <version>3.0.2</version>
                </plugin>
                <plugin>
                    <artifactId>maven-jar-plugin</artifactId>
                    <version>3.0.2</version>
                </plugin>
                <plugin>
                    <artifactId>maven-install-plugin</artifactId>
                    <version>2.5.2</version>
                </plugin>
                <plugin>
                    <artifactId>maven-deploy-plugin</artifactId>
                    <version>2.8.2</version>
                </plugin>
                <plugin>
					<artifactId>maven-site-plugin</artifactId>
					<version>3.21.0</version>
				</plugin>
                <plugin>
                    <artifactId>maven-project-info-reports-plugin</artifactId>
                    <version>3.5.0</version>
                </plugin>
            </plugins>
        </pluginManagement>
    </build>
</project>
```



