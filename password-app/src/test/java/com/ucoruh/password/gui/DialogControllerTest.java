package com.ucoruh.password.gui;

import static org.junit.Assert.*;

import javax.swing.JDialog;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the DialogController interface
 */
public class DialogControllerTest {
    
    /**
     * Mock implementation of DialogController for testing
     */
    private class MockDialogController implements DialogController {
        private JDialog dialog;
        private boolean dialogShown = false;
        private boolean dialogClosed = false;
        
        @Override
        public void showDialog() {
            dialog = new JDialog();
            dialogShown = true;
        }
        
        @Override
        public void closeDialog() {
            if (dialog != null) {
                dialogClosed = true;
                dialog.dispose();
            }
        }
        
        @Override
        public JDialog getDialog() {
            return dialog;
        }
        
        public boolean isDialogShown() {
            return dialogShown;
        }
        
        public boolean isDialogClosed() {
            return dialogClosed;
        }
    }
    
    private MockDialogController controller;
    
    @Before
    public void setUp() {
        controller = new MockDialogController();
    }
    
    /**
     * Test that showDialog creates a dialog and marks it as shown
     */
    @Test
    public void testShowDialog() {
        assertNull("Dialog should be null before showing", controller.getDialog());
        assertFalse("Dialog should not be marked as shown", controller.isDialogShown());
        
        controller.showDialog();
        
        assertNotNull("Dialog should not be null after showing", controller.getDialog());
        assertTrue("Dialog should be marked as shown", controller.isDialogShown());
    }
    
    /**
     * Test that closeDialog closes the dialog and marks it as closed
     */
    @Test
    public void testCloseDialog() {
        controller.showDialog();
        assertFalse("Dialog should not be marked as closed", controller.isDialogClosed());
        
        controller.closeDialog();
        
        assertTrue("Dialog should be marked as closed", controller.isDialogClosed());
    }
    
    /**
     * Test that getDialog returns the current dialog
     */
    @Test
    public void testGetDialog() {
        assertNull("Dialog should be null before showing", controller.getDialog());
        
        controller.showDialog();
        JDialog dialog = controller.getDialog();
        
        assertNotNull("Dialog should not be null after showing", dialog);
    }
} 