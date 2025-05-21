package com.ucoruh.password.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

import com.ucoruh.password.*;

/**
 * Controller for Add Password dialog
 * Handles functionality for adding new passwords
 */
public class AddPasswordController implements DialogController {
    private PasswordManagerGUI gui;
    private JDialog dialog;
    private AuthManager authManager;
    
    // UI Components
    private JTextField txtService;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    
    /**
     * Constructor
     * @param gui Reference to main GUI
     */
    public AddPasswordController(PasswordManagerGUI gui) {
        this.gui = gui;
        this.authManager = AuthManager.getInstance();
    }
    
    @Override
    public void showDialog() {
        // Create dialog
        dialog = new JDialog(gui, "Add New Password", true);
        dialog.setSize(450, 400);
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
        
        // Service field
        JLabel lblService = new JLabel("Service/Website:");
        lblService.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(lblService, gbc);
        
        txtService = new JTextField(20);
        txtService.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(txtService, gbc);
        
        // Username field
        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(lblUsername, gbc);
        
        txtUsername = new JTextField(20);
        txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(txtUsername, gbc);
        
        // Password field
        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 4;
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
        gbc.gridy = 5;
        panel.add(passwordPanel, gbc);
        
        return panel;
    }
    
    /**
     * Creates the button panel with action buttons
     * @return JPanel containing buttons
     */
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton btnSave = gui.createStyledButton("Save", gui.PRIMARY_COLOR);
        JButton btnCancel = gui.createStyledButton("Cancel", gui.DARK_COLOR);
        
        buttonPanel.add(btnCancel);
        buttonPanel.add(btnSave);
        
        // Save button action
        btnSave.addActionListener(e -> savePassword());
        
        // Cancel button action
        btnCancel.addActionListener(e -> closeDialog());
        
        return buttonPanel;
    }
    
    /**
     * Saves the password to storage
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
        List<Password> passwords = storage.readAll();
        
        // Check if service already exists
        boolean updated = false;
        for (Password p : passwords) {
            if (p.getService().equalsIgnoreCase(service)) {
                p.setUsername(username);
                p.setPassword(password);
                updated = true;
                break;
            }
        }
        
        // If not found, add new password
        if (!updated) {
            passwords.add(new Password(service, username, password));
        }
        
        // Save passwords
        storage.writeAll(passwords);
        
        JOptionPane.showMessageDialog(dialog, 
                "Password saved successfully!", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
        
        closeDialog();
    }
} 