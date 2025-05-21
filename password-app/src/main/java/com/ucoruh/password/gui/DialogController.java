package com.ucoruh.password.gui;

import javax.swing.JDialog;

/**
 * Interface for all dialog controllers
 * Provides a common interface for showing and closing dialogs
 */
public interface DialogController {
    /**
     * Shows the dialog managed by this controller
     */
    void showDialog();
    
    /**
     * Closes the dialog managed by this controller
     */
    void closeDialog();
    
    /**
     * Returns the dialog managed by this controller
     * @return JDialog instance
     */
    JDialog getDialog();
} 