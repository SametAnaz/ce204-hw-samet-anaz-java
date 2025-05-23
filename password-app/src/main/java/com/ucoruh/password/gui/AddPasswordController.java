package com.ucoruh.password.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

import com.ucoruh.password.*;

/**
 * @brief Controller for Add Password dialog
 * @details Handles functionality for adding new passwords
 */
public class AddPasswordController implements DialogController {
    /** @brief Reference to main GUI */
    private PasswordManagerGUI gui;
    
    /** @brief Dialog window for adding passwords */
    private JDialog dialog;
    
    /** @brief Authentication manager instance */
    private AuthManager authManager;
    
    /** @brief Text field for service/website name */
    private JTextField txtService;
    
    /** @brief Text field for username */
    private JTextField txtUsername;
    
    /** @brief Password field for the password */
    private JPasswordField txtPassword;
    
    /**
     * @brief Constructor for AddPasswordController
     * @param gui Reference to main GUI
     */
    public AddPasswordController(PasswordManagerGUI gui) {
        this.gui = gui;
        this.authManager = AuthManager.getInstance();
    }
    
    /**
     * @brief Shows the dialog for adding a new password
     */
    @Override
    public void showDialog() {
        // Create dialog
        dialog = new JDialog(gui, "Add New Password", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(gui);
        dialog.setLayout(new BorderLayout());
        
        // Create main panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Service field
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(new JLabel("Service/Website:"), gbc);
        
        txtService = new JTextField(20);
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(txtService, gbc);
        
        // Username field
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(new JLabel("Username:"), gbc);
        
        txtUsername = new JTextField(20);
        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(txtUsername, gbc);
        
        // Password field
        gbc.gridx = 0;
        gbc.gridy = 4;
        mainPanel.add(new JLabel("Password:"), gbc);
        
        txtPassword = new JPasswordField(20);
        gbc.gridx = 0;
        gbc.gridy = 5;
        mainPanel.add(txtPassword, gbc);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSave = new JButton("Save");
        JButton btnCancel = new JButton("Cancel");
        
        btnSave.addActionListener(e -> savePassword());
        btnCancel.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        
        dialog.add(mainPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    
    /**
     * @brief Saves the new password
     * @details Validates input and saves the password to storage
     */
    private void savePassword() {
        String service = txtService.getText().trim();
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        
        if (service.isEmpty() || username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(dialog, 
                    "All fields are required.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Get all passwords
        InterfacePasswordStorage storage = PasswordStorageFactory.create(
                StorageType.FILE, authManager.getMasterPassword());
        List<Password> passwordList = storage.readAll();
        
        // Check if service already exists
        for (Password p : passwordList) {
            if (p.getService().equalsIgnoreCase(service)) {
                JOptionPane.showMessageDialog(dialog, 
                        "A password for this service already exists.\nUse the Update Password option to modify it.", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        // Add new password
        passwordList.add(new Password(service, username, password));
        
        // Save passwords
        storage.writeAll(passwordList);
        
        JOptionPane.showMessageDialog(dialog, 
                "Password saved successfully!", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
        
        dialog.dispose();
    }

    /**
     * @brief Gets the dialog managed by this controller
     * @return JDialog instance managed by this controller
     */
    @Override
    public JDialog getDialog() {
        return dialog;
    }

    /**
     * @brief Closes the dialog managed by this controller
     */
    @Override
    public void closeDialog() {
        if (dialog != null) {
            dialog.dispose();
        }
    }
} 