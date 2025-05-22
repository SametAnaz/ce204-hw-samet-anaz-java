package com.ucoruh.password.gui;

import javax.swing.JDialog;

/**
 * @brief Interface for all dialog controllers
 * @details Provides a common interface for showing and closing dialogs
 */
public interface DialogController {
    /**
     * @brief Shows the dialog managed by this controller
     */
    void showDialog();
    
    /**
     * @brief Closes the dialog managed by this controller
     */
    void closeDialog();
    
    /**
     * @brief Returns the dialog managed by this controller
     * @return JDialog instance managed by this controller
     */
    JDialog getDialog();
} 