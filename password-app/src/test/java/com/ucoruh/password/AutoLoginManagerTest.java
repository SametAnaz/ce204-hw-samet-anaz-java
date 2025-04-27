package com.ucoruh.password;

import java.util.Scanner;
import org.junit.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.*;

/**
 * @brief Unit tests for the AutoLoginManager class.
 *
 * Verifies that the AutoLoginManager's menu method produces the expected output.
 */
public class AutoLoginManagerTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private PasswordManager passwordManager;
    private static final String TEST_FILE = "autologin.txt";

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        passwordManager = new PasswordManager("test-master-password");
        
        // Add a test credential to password manager
        passwordManager.addCredential("TestService", "testPassword");
        
        // Make sure the auto login file doesn't exist
        try {
            Files.deleteIfExists(Paths.get(TEST_FILE));
        } catch (IOException e) {
            // Ignore
        }
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
        
        // Clean up auto login file
        try {
            Files.deleteIfExists(Paths.get(TEST_FILE));
        } catch (IOException e) {
            // Ignore
        }
    }

    /**
     * @brief Tests the menu method with a basic input.
     *
     * This test provides input to select option 0 (back to main menu) and verifies
     * that the menu is displayed correctly.
     */
    @Test
    public void testMenuBasicNavigation() {
        // Simulate input to navigate back to main menu
        String input = "0\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        
        AutoLoginManager.menu(scanner, passwordManager);
        
        String output = outContent.toString();
        assertTrue("Output should contain auto-login menu header", 
                  output.contains("AUTO-LOGIN FEATURES"));
        assertTrue("Output should contain option to enable auto-login",
                  output.contains("Enable Auto-Login"));
    }
    
    /**
     * @brief Tests showing services with auto-login enabled when none exist.
     */
    @Test
    public void testShowServicesEmpty() {
        // Reset the AutoLoginManager state by reflection
        try {
            // First clear the set
            java.lang.reflect.Field field = AutoLoginManager.class.getDeclaredField("autoLoginServices");
            field.setAccessible(true);
            java.util.Set<String> services = (java.util.Set<String>) field.get(null);
            services.clear();
            
            // Then reset initialized flag
            field = AutoLoginManager.class.getDeclaredField("initialized");
            field.setAccessible(true);
            field.set(null, false);
            
            // Make sure no services are enabled for auto-login
            try {
                Files.deleteIfExists(Paths.get(TEST_FILE));
            } catch (IOException e) {
                // Ignore
            }
            
            // Reset output
            outContent.reset();
            
            // Directly call the showAutoLoginServices method through reflection
            java.lang.reflect.Method method = AutoLoginManager.class.getDeclaredMethod("showAutoLoginServices");
            method.setAccessible(true);
            method.invoke(null);
            
            String output = outContent.toString();
            assertTrue("Output should contain 'None'", output.contains("None"));
        } catch (Exception e) {
            fail("Failed to test showAutoLoginServices: " + e.getMessage());
        }
    }
    
    /**
     * @brief Tests enabling auto-login for a service.
     */
    @Test
    public void testEnableAutoLogin() {
        // First enable auto-login for a service
        AutoLoginManager.enableAutoLogin("TestService");
        
        // Then check if it's enabled
        assertTrue("TestService should have auto-login enabled", 
                  AutoLoginManager.isAutoLoginEnabled("TestService"));
    }
    
    /**
     * @brief Tests disabling auto-login for a service.
     */
    @Test
    public void testDisableAutoLogin() {
        // First enable auto-login for a service
        AutoLoginManager.enableAutoLogin("TestService");
        
        // Then disable it
        AutoLoginManager.disableAutoLogin("TestService");
        
        // Then check if it's disabled
        assertFalse("TestService should have auto-login disabled", 
                   AutoLoginManager.isAutoLoginEnabled("TestService"));
    }
    
    /**
     * @brief Tests auto-login for a service.
     */
    @Test
    public void testAutoLogin() {
        // First enable auto-login for our test service that has a credential
        AutoLoginManager.enableAutoLogin("TestService");
        
        // Then try to auto-login
        boolean result = AutoLoginManager.autoLogin("TestService", passwordManager);
        
        // Auto-login should be successful
        assertTrue("Auto-login should be successful", result);
        assertTrue("Output should contain 'Successfully logged in'", 
                  outContent.toString().contains("Successfully logged in"));
    }
    
    /**
     * @brief Tests auto-login failure for a non-existent service.
     */
    @Test
    public void testAutoLoginFailure() {
        // Try to auto-login to a service that isn't enabled
        boolean result = AutoLoginManager.autoLogin("NonExistentService", passwordManager);
        
        // Auto-login should fail
        assertFalse("Auto-login should fail", result);
    }
    
    /**
     * @brief Tests enabling auto-login through the menu.
     */
    @Test
    public void testEnableAutoLoginMenu() {
        // Simulate input to enable auto-login for TestService
        String input = "1\nTestService\n0\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        
        AutoLoginManager.menu(scanner, passwordManager);
        
        String output = outContent.toString();
        assertTrue("Output should confirm auto-login was enabled", 
                  output.contains("Auto-login enabled for TestService"));
    }
    
    /**
     * @brief Tests enabling auto-login for a non-existent service.
     */
    @Test
    public void testEnableAutoLoginMenuNonExistentService() {
        // Simulate input to try to enable auto-login for a non-existent service
        String input = "1\nNonExistentService\n0\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        
        AutoLoginManager.menu(scanner, passwordManager);
        
        String output = outContent.toString();
        assertTrue("Output should indicate service wasn't found", 
                  output.contains("Service not found"));
    }
    
    /**
     * @brief Tests disabling auto-login through the menu.
     */
    @Test
    public void testDisableAutoLoginMenu() {
        // First enable auto-login
        AutoLoginManager.enableAutoLogin("TestService");
        
        outContent.reset();
        
        // Simulate input to disable auto-login
        String input = "2\nTestService\n0\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        
        AutoLoginManager.menu(scanner, passwordManager);
        
        String output = outContent.toString();
        assertTrue("Output should confirm auto-login was disabled", 
                  output.contains("Auto-login disabled for TestService"));
    }
    
    /**
     * @brief Tests disabling auto-login for a service that doesn't have it enabled.
     */
    @Test
    public void testDisableAutoLoginMenuNotEnabled() {
        // Simulate input to try to disable auto-login for a service that doesn't have it enabled
        String input = "2\nTestService\n0\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        
        AutoLoginManager.menu(scanner, passwordManager);
        
        String output = outContent.toString();
        assertTrue("Output should indicate auto-login wasn't enabled", 
                  output.contains("Auto-login was not enabled"));
    }
    
    /**
     * @brief Tests simulating auto-login through the menu.
     */
    @Test
    public void testSimulateAutoLoginMenu() {
        // First enable auto-login
        AutoLoginManager.enableAutoLogin("TestService");
        
        outContent.reset();
        
        // Simulate input to simulate auto-login
        String input = "4\nTestService\n0\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        
        AutoLoginManager.menu(scanner, passwordManager);
        
        String output = outContent.toString();
        assertTrue("Output should indicate successful auto-login", 
                  output.contains("Successfully logged in"));
    }
    
    /**
     * @brief Tests handling invalid input in the menu.
     */
    @Test
    public void testMenuInvalidInput() {
        // Simulate invalid input (non-numeric)
        String input = "invalid\n0\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        
        AutoLoginManager.menu(scanner, passwordManager);
        
        String output = outContent.toString();
        assertTrue("Output should indicate invalid number", 
                  output.contains("Invalid number"));
    }
}
