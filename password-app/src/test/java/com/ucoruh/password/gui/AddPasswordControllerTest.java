package com.ucoruh.password.gui;

import static org.junit.Assert.*;

import javax.swing.JDialog;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import org.junit.Assume;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ucoruh.password.AuthManager;

/**
 * Tests for the AddPasswordController class
 */
public class AddPasswordControllerTest {
    
    private PasswordManagerGUI gui;
    private AddPasswordController controller;
    private AuthManager originalAuthManager;
    
    @Before
    public void setUp() {
        // Skip tests if running in a headless environment
        Assume.assumeFalse("Skipping test in headless environment", 
                          GraphicsEnvironment.isHeadless());
        
        try {
            // Save original auth manager
            originalAuthManager = AuthManager.getInstance();
            
            // Create a real GUI for testing
            gui = new PasswordManagerGUI();
            gui.setVisible(false); // Don't show the UI
            
            // Create controller with real GUI
            controller = new AddPasswordController(gui);
        } catch (HeadlessException e) {
            // If we still get a HeadlessException despite the check above,
            // mark the test as skipped
            Assume.assumeNoException("Headless environment detected", e);
        }
    }
    
    @After
    public void tearDown() {
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
        assertNotNull("Controller should not be null", controller);
        assertNull("Initial dialog should be null", controller.getDialog());
    }
    
    /**
     * Test the implemented DialogController interface methods
     */
    @Test
    public void testDialogControllerInterface() {
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
} 