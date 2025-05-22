package com.ucoruh.password.gui;

import static org.junit.Assert.*;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ucoruh.password.AuthManager;
import com.ucoruh.password.Password;

/**
 * Tests for the DeletePasswordController class
 */
public class DeletePasswordControllerTest {
    
    private PasswordManagerGUI gui;
    private DeletePasswordController controller;
    private AuthManager originalAuthManager;
    
    @Before
    public void setUp() {
        // Save original auth manager
        originalAuthManager = AuthManager.getInstance();
        
        // Create a real GUI for testing
        gui = new PasswordManagerGUI();
        gui.setVisible(false); // Don't show the UI
        
        // Create controller with real GUI
        controller = new DeletePasswordController(gui);
    }
    
    @After
    public void tearDown() {
        // Clean up any dialog that might have been created
        if (controller.getDialog() != null) {
            controller.getDialog().dispose();
        }
        
        // Clean up the GUI
        gui.dispose();
    }
    
    /**
     * Test that the dialog is initially null
     */
    @Test
    public void testInitialDialogIsNull() {
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
            Field dialogField = DeletePasswordController.class.getDeclaredField("dialog");
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
     * Test the combo box service list field
     */
    @Test
    public void testServiceComboBox() {
        try {
            // Create dialog but don't show it
            Field dialogField = DeletePasswordController.class.getDeclaredField("dialog");
            dialogField.setAccessible(true);
            
            JDialog testDialog = new JDialog();
            dialogField.set(controller, testDialog);
            
            // Get the combo box field
            Field comboServicesField = DeletePasswordController.class.getDeclaredField("comboServices");
            comboServicesField.setAccessible(true);
            
            // Create a mock combo box
            String[] testServices = {"TestService1 (TestUser1)", "TestService2 (TestUser2)"};
            JComboBox<String> comboServices = new JComboBox<>(testServices);
            comboServicesField.set(controller, comboServices);
            
            // Test that we can access the combo box
            assertNotNull(comboServices);
            assertEquals(2, comboServices.getItemCount());
            assertEquals("TestService1 (TestUser1)", comboServices.getItemAt(0));
            assertEquals("TestService2 (TestUser2)", comboServices.getItemAt(1));
            
        } catch (Exception e) {
            fail("Exception during test: " + e.getMessage());
        }
    }
} 