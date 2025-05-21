package com.ucoruh.password.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

import com.ucoruh.password.*;

/**
 * Controller for Update Password dialog
 * Handles functionality for updating existing passwords
 */
public class UpdatePasswordController implements DialogController {
    private PasswordManagerGUI gui;
    private JDialog dialog;
    private AuthManager authManager;
    private List<Password> passwordList;
    private JComboBox<String> comboServices;
    private JPasswordField txtPassword;
    
    /**
     * Constructor
     * @param gui Reference to main GUI
     */
    public UpdatePasswordController(PasswordManagerGUI gui) {
        this.gui = gui;
        this.authManager = AuthManager.getInstance();
    }
    
    @Override
    public void showDialog() {
        // Load passwords
        loadPasswords();
        
        if (passwordList.isEmpty()) {
            JOptionPane.showMessageDialog(gui, 
                    "No passwords found to update.", 
                    "No Passwords", 
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Create dialog
        dialog = new JDialog(gui, "Update Password", true);
        dialog.setSize(450, 450);
        dialog.setLocationRelativeTo(gui);
        dialog.setLayout(new BorderLayout());
        
        // Create content panel
        JPanel panel = createContentPanel();
        
        // Create button panel
        JPanel buttonPanel = createButtonPanel();
        
        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    
    @Override
    public void closeDialog() {
        if (dialog != null && dialog.isVisible()) {
            dialog.dispose();
        }
    }
    
    @Override
    public JDialog getDialog() {
        return dialog;
    }
    
    /**
     * Loads passwords from storage
     */
    private void loadPasswords() {
        // Get all passwords
        InterfacePasswordStorage storage = PasswordStorageFactory.create(
                StorageType.FILE, authManager.getMasterPassword());
        passwordList = storage.readAll();
    }
    
    /**
     * Creates the content panel with form fields
     * @return JPanel containing form fields
     */
    private JPanel createContentPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Service selection
        JLabel lblSelect = new JLabel("Select Service/Website:");
        lblSelect.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(lblSelect, gbc);
        
        // Create service list
        String[] services = new String[passwordList.size()];
        for (int i = 0; i < passwordList.size(); i++) {
            Password password = passwordList.get(i);
            services[i] = password.getService() + " (" + password.getUsername() + ")";
        }
        
        comboServices = new JComboBox<>(services);
        comboServices.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(comboServices, gbc);
        
        // Password field
        JLabel lblPassword = new JLabel("New Password:");
        lblPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(lblPassword, gbc);
        
        JPanel passwordPanel = new JPanel(new BorderLayout());
        passwordPanel.setBackground(Color.WHITE);
        
        txtPassword = new JPasswordField(20);
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordPanel.add(txtPassword, BorderLayout.CENTER);
        
        JButton btnToggle = new JButton("Show");
        btnToggle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnToggle.addActionListener(e -> {
            if (txtPassword.getEchoChar() == 0) {
                txtPassword.setEchoChar('•');
                btnToggle.setText("Show");
            } else {
                txtPassword.setEchoChar((char) 0);
                btnToggle.setText("Hide");
            }
        });
        passwordPanel.add(btnToggle, BorderLayout.EAST);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(passwordPanel, gbc);
        
        // Generate password option
        JButton btnGenerate = gui.createStyledButton("Generate Password", gui.SECONDARY_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.insets = new Insets(15, 5, 5, 5);
        panel.add(btnGenerate, gbc);
        
        // Generate password button action
        btnGenerate.addActionListener(e -> {
            String generatedPassword = PasswordGenerator.generatePassword(12);
            txtPassword.setText(generatedPassword);
        });
        
        return panel;
    }
    
    /**
     * Creates the button panel with action buttons
     * @return JPanel containing buttons
     */
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton btnUpdate = gui.createStyledButton("Update", gui.PRIMARY_COLOR);
        JButton btnCancel = gui.createStyledButton("Cancel", gui.DARK_COLOR);
        
        buttonPanel.add(btnCancel);
        buttonPanel.add(btnUpdate);
        
        // Update button action
        btnUpdate.addActionListener(e -> updatePassword());
        
        // Cancel button action
        btnCancel.addActionListener(e -> closeDialog());
        
        return buttonPanel;
    }
    
    /**
     * Updates the selected password
     */
    private void updatePassword() {
        int selectedIndex = comboServices.getSelectedIndex();
        String newPassword = new String(txtPassword.getPassword());
        
        if (newPassword.isEmpty()) {
            JOptionPane.showMessageDialog(dialog, 
                    "Please enter a new password.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Update password
        Password selectedPassword = passwordList.get(selectedIndex);
        selectedPassword.setPassword(newPassword);
        
        // Save passwords
        InterfacePasswordStorage storage = PasswordStorageFactory.create(
                StorageType.FILE, authManager.getMasterPassword());
        storage.writeAll(passwordList);
        
        JOptionPane.showMessageDialog(dialog, 
                "Password updated successfully!", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
        
        closeDialog();
    }
} 