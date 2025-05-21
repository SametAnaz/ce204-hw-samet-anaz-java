package com.ucoruh.password.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

import com.ucoruh.password.*;

/**
 * Controller for Generate Password dialog
 * Handles functionality for generating and saving passwords
 */
public class GeneratePasswordController implements DialogController {
    private PasswordManagerGUI gui;
    private JDialog dialog;
    private AuthManager authManager;
    
    // UI Components
    private JTextField txtService;
    private JTextField txtUsername;
    private JTextField txtGenerated;
    private JSlider sliderLength;
    
    /**
     * Constructor
     * @param gui Reference to main GUI
     */
    public GeneratePasswordController(PasswordManagerGUI gui) {
        this.gui = gui;
        this.authManager = AuthManager.getInstance();
    }
    
    @Override
    public void showDialog() {
        // Create dialog
        dialog = new JDialog(gui, "Generate and Save Password", true);
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
        
        // Password length
        JLabel lblLength = new JLabel("Password Length:");
        lblLength.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(lblLength, gbc);
        
        sliderLength = new JSlider(JSlider.HORIZONTAL, 8, 32, 16);
        sliderLength.setMajorTickSpacing(4);
        sliderLength.setMinorTickSpacing(1);
        sliderLength.setPaintTicks(true);
        sliderLength.setPaintLabels(true);
        sliderLength.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sliderLength.setBackground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(sliderLength, gbc);
        
        // Generated password field
        JLabel lblGenerated = new JLabel("Generated Password:");
        lblGenerated.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.insets = new Insets(15, 5, 5, 5);
        panel.add(lblGenerated, gbc);
        
        txtGenerated = new JTextField(20);
        txtGenerated.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtGenerated.setEditable(false);
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel.add(txtGenerated, gbc);
        
        // Generate button
        JButton btnGenerate = gui.createStyledButton("Generate", gui.SECONDARY_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.insets = new Insets(15, 5, 5, 5);
        panel.add(btnGenerate, gbc);
        
        // Generate button action
        btnGenerate.addActionListener(e -> {
            int length = sliderLength.getValue();
            String generatedPassword = PasswordGenerator.generatePassword(length);
            txtGenerated.setText(generatedPassword);
        });
        
        // Generate an initial password
        String initialPassword = PasswordGenerator.generatePassword(16);
        txtGenerated.setText(initialPassword);
        
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
     * Saves the generated password
     */
    private void savePassword() {
        String service = txtService.getText().trim();
        String username = txtUsername.getText().trim();
        String password = txtGenerated.getText();
        
        if (service.isEmpty() || username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(dialog, 
                    "Service, username, and password are required.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Get all passwords
        InterfacePasswordStorage storage = PasswordStorageFactory.create(
                StorageType.FILE, authManager.getMasterPassword());
        List<Password> passwordList = storage.readAll();
        
        // Check if service already exists
        boolean updated = false;
        for (Password p : passwordList) {
            if (p.getService().equalsIgnoreCase(service)) {
                p.setUsername(username);
                p.setPassword(password);
                updated = true;
                break;
            }
        }
        
        // If not found, add new password
        if (!updated) {
            passwordList.add(new Password(service, username, password));
        }
        
        // Save passwords
        storage.writeAll(passwordList);
        
        JOptionPane.showMessageDialog(dialog, 
                "Password saved successfully!", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
        
        closeDialog();
    }
} 