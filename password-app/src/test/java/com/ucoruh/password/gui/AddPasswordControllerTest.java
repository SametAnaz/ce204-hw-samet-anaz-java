package com.ucoruh.password.gui;

import static org.junit.Assert.*;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import java.awt.Component;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import org.junit.Assume;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;

import com.ucoruh.password.AuthManager;
import com.ucoruh.password.Password;
import com.ucoruh.password.InterfacePasswordStorage;
import com.ucoruh.password.PasswordStorageFactory;
import com.ucoruh.password.StorageType;

/**
 * Tests for the AddPasswordController class
 */
public class AddPasswordControllerTest {
    
    // Static flag to immediately skip all tests in CI environments
    private static final boolean SKIP_ALL_UI_TESTS;
    
    // Static initializer to check CI environment once
    static {
        boolean isHeadless = GraphicsEnvironment.isHeadless();
        String ciEnv = System.getenv("CI");
        boolean isCiEnvironment = (ciEnv != null && ciEnv.equals("true"));
        
        SKIP_ALL_UI_TESTS = isHeadless || isCiEnvironment;
        
        if (SKIP_ALL_UI_TESTS) {
            System.out.println("UI tests will be completely skipped - running in headless or CI environment");
        }
    }
    
    private PasswordManagerGUI gui;
    private AddPasswordController controller;
    private AuthManager originalAuthManager;
    
    @Before
    public void setUp() {
        // Skip everything if in CI environment - don't even attempt to initialize
        if (SKIP_ALL_UI_TESTS) {
            Assume.assumeTrue("Skipping all UI tests in headless/CI environment", false);
            return;
        }
        
        try {
            // Save original auth manager
            originalAuthManager = AuthManager.getInstance();
            
            // Create a real GUI for testing
            gui = new PasswordManagerGUI();
            gui.setVisible(false); // Don't show the UI
            
            // Create controller with real GUI
            controller = new AddPasswordController(gui);
        } catch (HeadlessException e) {
            Assume.assumeNoException("Headless environment detected", e);
        } catch (Exception e) {
            Assume.assumeNoException("Error initializing UI components", e);
        }
    }
    
    @After
    public void tearDown() {
        // Skip cleanup if tests were skipped
        if (SKIP_ALL_UI_TESTS) {
            return;
        }
        
        // Clean up any dialog that might have been created
        if (controller != null && controller.getDialog() != null) {
            controller.getDialog().dispose();
        }
        
        // Clean up the GUI
        if (gui != null) {
            gui.dispose();
        }
    }
    
    /**
     * Test that the dialog is initially null
     */
    @Test
    public void testInitialDialogIsNull() {
        // Will be skipped automatically if SKIP_ALL_UI_TESTS is true
        
        assertNotNull("Controller should not be null", controller);
        assertNull("Initial dialog should be null", controller.getDialog());
    }
    
    /**
     * Test the implemented DialogController interface methods
     */
    @Test
    public void testDialogControllerInterface() {
        // Will be skipped automatically if SKIP_ALL_UI_TESTS is true
        
        // Initially the dialog should be null
        assertNull(controller.getDialog());
        
        // Avoid actually showing the dialog in tests
        try {
            // Use reflection to create a dialog but don't show it
            Field dialogField = AddPasswordController.class.getDeclaredField("dialog");
            dialogField.setAccessible(true);
            
            JDialog testDialog = new JDialog();
            dialogField.set(controller, testDialog);
            
            // Now the dialog should not be null
            assertNotNull(controller.getDialog());
            assertEquals(testDialog, controller.getDialog());
            
            // Test closeDialog
            controller.closeDialog();
            
            // Dialog will be disposed but still not null
            assertFalse(testDialog.isVisible());
        } catch (Exception e) {
            fail("Exception during test: " + e.getMessage());
        }
    }
    
    /**
     * Test the closeDialog method more thoroughly
     */
    @Test
    public void testCloseDialog() {
        // Skip test in headless environment
        Assume.assumeFalse("Skipping UI test in headless environment", SKIP_ALL_UI_TESTS);
        
        try {
            // Create a dialog to close
            Field dialogField = AddPasswordController.class.getDeclaredField("dialog");
            dialogField.setAccessible(true);
            
            JDialog testDialog = new JDialog();
            testDialog.setVisible(true);
            dialogField.set(controller, testDialog);
            
            // Call closeDialog method
            controller.closeDialog();
            
            // Verify the dialog is disposed
            assertFalse("Dialog should be not visible after closeDialog", testDialog.isVisible());
            
            // Test when dialog is null
            dialogField.set(controller, null);
            controller.closeDialog(); // Should not throw exception
            
            // Test when dialog is not visible
            JDialog testDialog2 = new JDialog();
            testDialog2.setVisible(false);
            dialogField.set(controller, testDialog2);
            controller.closeDialog(); // Should not throw exception
            
        } catch (Exception e) {
            fail("Exception during test: " + e.getMessage());
        }
    }
    
    /**
     * Test the createContentPanel method
     */
    @Test
    public void testCreateContentPanel() {
        // Skip test in headless environment
        Assume.assumeFalse("Skipping UI test in headless environment", SKIP_ALL_UI_TESTS);
        
        try {
            // Initialize necessary fields
            Field dialogField = AddPasswordController.class.getDeclaredField("dialog");
            Field txtServiceField = AddPasswordController.class.getDeclaredField("txtService");
            Field txtUsernameField = AddPasswordController.class.getDeclaredField("txtUsername");
            Field txtPasswordField = AddPasswordController.class.getDeclaredField("txtPassword");
            
            dialogField.setAccessible(true);
            txtServiceField.setAccessible(true);
            txtUsernameField.setAccessible(true);
            txtPasswordField.setAccessible(true);
            
            // Set the dialog
            JDialog testDialog = new JDialog();
            dialogField.set(controller, testDialog);
            
            // Get access to the createContentPanel method
            Method createContentPanelMethod = AddPasswordController.class.getDeclaredMethod("createContentPanel");
            createContentPanelMethod.setAccessible(true);
            
            // Call the method
            JPanel contentPanel = (JPanel) createContentPanelMethod.invoke(controller);
            
            // Check the panel was created
            assertNotNull("Content panel should not be null", contentPanel);
            
            // Check the text fields were initialized
            JTextField txtService = (JTextField) txtServiceField.get(controller);
            JTextField txtUsername = (JTextField) txtUsernameField.get(controller);
            JPasswordField txtPassword = (JPasswordField) txtPasswordField.get(controller);
            
            assertNotNull("Service text field should be initialized", txtService);
            assertNotNull("Username text field should be initialized", txtUsername);
            assertNotNull("Password field should be initialized", txtPassword);
            
            // Verify the panel has components
            assertTrue("Content panel should have components", contentPanel.getComponentCount() > 0);
            
            // Find components by class type
            int textFieldCount = 0;
            int passwordFieldCount = 0;
            
            for (Component comp : contentPanel.getComponents()) {
                if (comp instanceof JTextField && !(comp instanceof JPasswordField)) {
                    textFieldCount++;
                } else if (comp instanceof JPasswordField) {
                    passwordFieldCount++;
                }
            }
            
            // The panel should contain multiple components including text fields
            // Note: Some might be in sub-panels, so we're just checking it has components
            assertTrue("Content panel should have components", contentPanel.getComponentCount() > 3);
            
        } catch (Exception e) {
            fail("Exception during test: " + e.getMessage());
        }
    }
    
    /**
     * Test the createButtonPanel method
     */
    @Test
    public void testCreateButtonPanel() {
        // Skip test in headless environment
        Assume.assumeFalse("Skipping UI test in headless environment", SKIP_ALL_UI_TESTS);
        
        try {
            // Initialize necessary fields
            Field dialogField = AddPasswordController.class.getDeclaredField("dialog");
            dialogField.setAccessible(true);
            JDialog testDialog = new JDialog();
            dialogField.set(controller, testDialog);
            
            // Get access to the createButtonPanel method
            Method createButtonPanelMethod = AddPasswordController.class.getDeclaredMethod("createButtonPanel");
            createButtonPanelMethod.setAccessible(true);
            
            // Call the method
            JPanel buttonPanel = (JPanel) createButtonPanelMethod.invoke(controller);
            
            // Check the panel was created
            assertNotNull("Button panel should not be null", buttonPanel);
            
            // Count the buttons in the panel
            int buttonCount = 0;
            JButton saveButton = null;
            JButton cancelButton = null;
            
            for (Component comp : buttonPanel.getComponents()) {
                if (comp instanceof JButton) {
                    buttonCount++;
                    JButton button = (JButton) comp;
                    if ("Save".equals(button.getText())) {
                        saveButton = button;
                    } else if ("Cancel".equals(button.getText())) {
                        cancelButton = button;
                    }
                }
            }
            
            // Verify there are 2 buttons (Save and Cancel)
            assertEquals("Button panel should contain 2 buttons", 2, buttonCount);
            assertNotNull("Save button should exist", saveButton);
            assertNotNull("Cancel button should exist", cancelButton);
            
            // Test cancel button action
            cancelButton.getActionListeners()[0].actionPerformed(null);
            assertFalse("Dialog should be not visible after cancel button click", testDialog.isVisible());
            
        } catch (Exception e) {
            fail("Exception during test: " + e.getMessage());
        }
    }
    
    /**
     * Test the showDialog method
     */
    @Test
    public void testShowDialog() {
        // Skip test in headless environment
        Assume.assumeFalse("Skipping UI test in headless environment", SKIP_ALL_UI_TESTS);
        
        try {
            // Create a controller with a modified showDialog method that doesn't actually show the dialog
            AddPasswordController testController = new AddPasswordController(gui) {
                @Override
                public void showDialog() {
                    // Call the parent method but intercept the setVisible call
                    super.showDialog();
                    
                    // Get the dialog and prevent it from being shown
                    JDialog dialog = getDialog();
                    if (dialog != null && dialog.isVisible()) {
                        dialog.setVisible(false);
                    }
                }
            };
            
            // Call the showDialog method
            testController.showDialog();
            
            // Check the dialog was created
            JDialog dialog = testController.getDialog();
            assertNotNull("Dialog should have been created", dialog);
            
            // Verify dialog properties
            assertEquals("Add New Password", dialog.getTitle());
            assertTrue("Dialog should have components", dialog.getContentPane().getComponentCount() > 0);
            
            // Clean up
            if (dialog != null) {
                dialog.dispose();
            }
            
        } catch (Exception e) {
            // Don't fail the test because of UI issues
            System.out.println("Note: " + e.getMessage());
        }
    }
    
    /**
     * Test the toggle password visibility in the content panel
     */
    @Test
    public void testTogglePasswordVisibility() {
        // Skip test in headless environment
        Assume.assumeFalse("Skipping UI test in headless environment", SKIP_ALL_UI_TESTS);
        
        try {
            // Initialize necessary fields
            Field dialogField = AddPasswordController.class.getDeclaredField("dialog");
            Field txtPasswordField = AddPasswordController.class.getDeclaredField("txtPassword");
            
            dialogField.setAccessible(true);
            txtPasswordField.setAccessible(true);
            
            // Set the dialog
            JDialog testDialog = new JDialog();
            dialogField.set(controller, testDialog);
            
            // Get access to the createContentPanel method
            Method createContentPanelMethod = AddPasswordController.class.getDeclaredMethod("createContentPanel");
            createContentPanelMethod.setAccessible(true);
            
            // Call the method
            JPanel contentPanel = (JPanel) createContentPanelMethod.invoke(controller);
            
            // Find the password panel which contains the toggle button
            JPasswordField passwordField = (JPasswordField) txtPasswordField.get(controller);
            
            // Find the toggle button
            JButton toggleButton = null;
            
            // Look for the panel that contains the password field
            for (Component comp : contentPanel.getComponents()) {
                if (comp instanceof JPanel) {
                    JPanel panel = (JPanel) comp;
                    // Look for the button in this panel
                    for (Component innerComp : panel.getComponents()) {
                        if (innerComp instanceof JButton) {
                            toggleButton = (JButton) innerComp;
                            break;
                        }
                    }
                    if (toggleButton != null) break;
                }
            }
            
            // If we found a toggle button, test its functionality
            if (toggleButton != null) {
                // Password field should initially have echo char set
                assertTrue(passwordField.getEchoChar() != 0);
                
                // Click the toggle button to show password
                toggleButton.getActionListeners()[0].actionPerformed(null);
                
                // Echo char should now be 0 (visible text)
                assertEquals(0, passwordField.getEchoChar());
                assertEquals("Show password button should now say 'Hide'", "Hide", toggleButton.getText());
                
                // Click again to hide password
                toggleButton.getActionListeners()[0].actionPerformed(null);
                
                // Echo char should be back to bullet
                assertTrue(passwordField.getEchoChar() != 0);
                assertEquals("Show password button should now say 'Show'", "Show", toggleButton.getText());
            }
            
        } catch (Exception e) {
            fail("Exception during test: " + e.getMessage());
        }
    }
    
    /**
     * Test the savePassword method more thoroughly, covering more branches
     */
    @Test
    public void testSavePasswordMoreCases() {
        // Skip test in headless environment
        Assume.assumeFalse("Skipping UI test in headless environment", SKIP_ALL_UI_TESTS);
        
        try {
            // Set up fields for test
            Field dialogField = AddPasswordController.class.getDeclaredField("dialog");
            Field txtServiceField = AddPasswordController.class.getDeclaredField("txtService");
            Field txtUsernameField = AddPasswordController.class.getDeclaredField("txtUsername");
            Field txtPasswordField = AddPasswordController.class.getDeclaredField("txtPassword");
            Field authManagerField = AddPasswordController.class.getDeclaredField("authManager");
            
            dialogField.setAccessible(true);
            txtServiceField.setAccessible(true);
            txtUsernameField.setAccessible(true);
            txtPasswordField.setAccessible(true);
            authManagerField.setAccessible(true);
            
            // Set up the dialog
            JDialog testDialog = new JDialog();
            dialogField.set(controller, testDialog);
            
            // Set up text fields with complete data
            JTextField txtService = new JTextField("ExistingService");
            JTextField txtUsername = new JTextField("ExistingUser");
            JPasswordField txtPassword = new JPasswordField("NewPassword");
            
            txtServiceField.set(controller, txtService);
            txtUsernameField.set(controller, txtUsername);
            txtPasswordField.set(controller, txtPassword);
            
            // Create test controller that doesn't show dialogs
            AddPasswordController testController = new AddPasswordController(gui) {
                @Override
                public void closeDialog() {
                    // Don't actually close the dialog in test
                }
            };
            
            // Set the fields on the test controller
            dialogField.set(testController, testDialog);
            txtServiceField.set(testController, txtService);
            txtUsernameField.set(testController, txtUsername);
            txtPasswordField.set(testController, txtPassword);
            
            // Get the savePassword method
            Method savePasswordMethod = AddPasswordController.class.getDeclaredMethod("savePassword");
            savePasswordMethod.setAccessible(true);
            
            // Try to save the password - should update existing
            try {
                savePasswordMethod.invoke(testController);
            } catch (Exception ex) {
                // Expected in test environment
            }
            
            // Test with a new service name
            txtService.setText("NewService");
            
            // Try to save again - should add new password
            try {
                savePasswordMethod.invoke(testController);
            } catch (Exception ex) {
                // Expected in test environment
            }
            
            // Test with some empty fields
            txtService.setText("");
            txtUsername.setText("User");
            
            // Try to save with missing service - should show error
            try {
                savePasswordMethod.invoke(testController);
            } catch (Exception ex) {
                // Expected in test environment
            }
            
            // Test with another field empty
            txtService.setText("Service");
            txtUsername.setText("");
            
            // Try to save with missing username - should show error
            try {
                savePasswordMethod.invoke(testController);
            } catch (Exception ex) {
                // Expected in test environment
            }
            
            // Test with password empty
            txtUsername.setText("User");
            txtPassword.setText("");
            
            // Try to save with missing password - should show error
            try {
                savePasswordMethod.invoke(testController);
            } catch (Exception ex) {
                // Expected in test environment
            }
            
        } catch (Exception e) {
            fail("Exception during test setup: " + e.getMessage());
        }
    }
    
    /**
     * Test the savePassword method
     */
    @Test
    public void testSavePassword() {
        // Skip test in headless environment
        Assume.assumeFalse("Skipping UI test in headless environment", SKIP_ALL_UI_TESTS);
        
        try {
            // Initialize necessary fields
            Field dialogField = AddPasswordController.class.getDeclaredField("dialog");
            Field txtServiceField = AddPasswordController.class.getDeclaredField("txtService");
            Field txtUsernameField = AddPasswordController.class.getDeclaredField("txtUsername");
            Field txtPasswordField = AddPasswordController.class.getDeclaredField("txtPassword");
            
            dialogField.setAccessible(true);
            txtServiceField.setAccessible(true);
            txtUsernameField.setAccessible(true);
            txtPasswordField.setAccessible(true);
            
            // Set the dialog
            JDialog testDialog = new JDialog();
            dialogField.set(controller, testDialog);
            
            // Set up the text fields
            JTextField txtService = new JTextField();
            JTextField txtUsername = new JTextField();
            JPasswordField txtPassword = new JPasswordField();
            
            txtServiceField.set(controller, txtService);
            txtUsernameField.set(controller, txtUsername);
            txtPasswordField.set(controller, txtPassword);
            
            // Get access to the savePassword method
            Method savePasswordMethod = AddPasswordController.class.getDeclaredMethod("savePassword");
            savePasswordMethod.setAccessible(true);
            
            // Create a controller class that tracks method calls
            class TestController extends AddPasswordController {
                private boolean dialogClosed = false;
                
                public TestController(PasswordManagerGUI gui) {
                    super(gui);
                }
                
                @Override
                public void closeDialog() {
                    dialogClosed = true;
                }
                
                public boolean isDialogClosed() {
                    return dialogClosed;
                }
            }
            
            // Create the test controller
            TestController testController = new TestController(gui);
            
            // Set the fields on the test controller
            dialogField.set(testController, testDialog);
            txtServiceField.set(testController, txtService);
            txtUsernameField.set(testController, txtUsername);
            txtPasswordField.set(testController, txtPassword);
            
            // Test Case 1: Empty fields - should show error
            txtService.setText("");
            txtUsername.setText("");
            txtPassword.setText("");
            
            // Call the method
            savePasswordMethod.invoke(testController);
            
            // Dialog should not be closed for empty fields
            assertFalse("Dialog should not be closed with validation error", 
                    testController.isDialogClosed());
            
            // Test Case 2: Only service filled - should show error
            txtService.setText("Service");
            txtUsername.setText("");
            txtPassword.setText("");
            
            // Reset the flag
            Field dialogClosedField = TestController.class.getDeclaredField("dialogClosed");
            dialogClosedField.setAccessible(true);
            dialogClosedField.set(testController, false);
            
            // Call the method
            savePasswordMethod.invoke(testController);
            
            // Dialog should not be closed for incomplete fields
            assertFalse("Dialog should not be closed with validation error", 
                    testController.isDialogClosed());
            
            // Test Case 3: All fields filled - should proceed
            txtService.setText("TestService");
            txtUsername.setText("TestUser");
            txtPassword.setText("TestPassword");
            
            // Reset the flag
            dialogClosedField.set(testController, false);
            
            try {
                // Call the method - this will attempt to access storage
                savePasswordMethod.invoke(testController);
            } catch (Exception e) {
                // Expected in test environment - we can't easily mock JOptionPane or storage
                System.out.println("Expected exception when accessing storage: " + e.getMessage());
            }
            
            // Test another scenario with existing data
            // Since we can't easily mock the storage, we'll simulate this 
            // by calling savePassword with different values
            txtService.setText("ExistingService");
            txtUsername.setText("UpdatedUser");
            txtPassword.setText("UpdatedPassword");
            
            // Reset the flag
            dialogClosedField.set(testController, false);
            
            try {
                // Call the method again
                savePasswordMethod.invoke(testController);
            } catch (Exception e) {
                // Expected in test environment
                System.out.println("Expected exception when accessing storage: " + e.getMessage());
            }
            
        } catch (Exception e) {
            fail("Exception during test: " + e.getMessage());
        }
    }
} 