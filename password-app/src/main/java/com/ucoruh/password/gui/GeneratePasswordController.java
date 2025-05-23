package com.ucoruh.password.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ItemListener;
import java.util.List;

import com.ucoruh.password.*;

/**
 * @brief Controller for Generate Password dialog
 * @details Handles functionality for generating and saving passwords
 */
public class GeneratePasswordController implements DialogController {
    /** @brief Reference to main GUI */
    private PasswordManagerGUI gui;
    
    /** @brief Dialog window for generating passwords */
    private JDialog dialog;
    
    /** @brief Authentication manager instance */
    private AuthManager authManager;
    
    // UI Components
    /** @brief Text field for service/website name */
    private JTextField txtService;
    
    /** @brief Text field for username */
    private JTextField txtUsername;
    
    /** @brief Text field for generated password */
    private JTextField txtGenerated;
    
    /** @brief Slider for password length */
    private JSlider sliderLength;
    
    /** @brief Checkbox for uppercase letters */
    private JCheckBox chkUppercase;
    
    /** @brief Checkbox for lowercase letters */
    private JCheckBox chkLowercase;
    
    /** @brief Checkbox for digits */
    private JCheckBox chkDigits;
    
    /** @brief Checkbox for special characters */
    private JCheckBox chkSpecial;
    
    /**
     * @brief Constructor for GeneratePasswordController
     * @param gui Reference to main GUI
     */
    public GeneratePasswordController(PasswordManagerGUI gui) {
        this.gui = gui;
        this.authManager = AuthManager.getInstance();
    }
    
    /**
     * @brief Shows the dialog for generating a password
     */
    @Override
    public void showDialog() {
        // Create dialog
        dialog = new JDialog(gui, "Generate and Save Password", true);
        dialog.setSize(450, 550);  // Increased height to accommodate additional controls
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
    
    /**
     * @brief Closes the generate password dialog
     */
    @Override
    public void closeDialog() {
        if (dialog != null && dialog.isVisible()) {
            dialog.dispose();
        }
    }
    
    /**
     * @brief Gets the dialog instance
     * @return JDialog instance
     */
    @Override
    public JDialog getDialog() {
        return dialog;
    }
    
    /**
     * @brief Creates the content panel with form fields
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
        
        // Character options panel
        JPanel optionsPanel = new JPanel(new GridLayout(2, 2, 10, 5));
        optionsPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), 
            "Character Types", 
            TitledBorder.LEFT, 
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 14)));
        optionsPanel.setBackground(Color.WHITE);
        
        chkUppercase = new JCheckBox("Uppercase Letters (A-Z)");
        chkUppercase.setSelected(true);
        chkUppercase.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        chkUppercase.setBackground(Color.WHITE);
        
        chkLowercase = new JCheckBox("Lowercase Letters (a-z)");
        chkLowercase.setSelected(true);
        chkLowercase.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        chkLowercase.setBackground(Color.WHITE);
        
        chkDigits = new JCheckBox("Digits (0-9)");
        chkDigits.setSelected(true);
        chkDigits.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        chkDigits.setBackground(Color.WHITE);
        
        chkSpecial = new JCheckBox("Special Characters (!@#$...)");
        chkSpecial.setSelected(true);
        chkSpecial.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        chkSpecial.setBackground(Color.WHITE);
        
        optionsPanel.add(chkUppercase);
        optionsPanel.add(chkLowercase);
        optionsPanel.add(chkDigits);
        optionsPanel.add(chkSpecial);
        
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.insets = new Insets(15, 5, 5, 5);
        panel.add(optionsPanel, gbc);
        
        // Checkbox state change listener to ensure at least one is selected
        ItemListener checkBoxListener = e -> {
            // Check if any checkbox is selected
            if (!chkUppercase.isSelected() && !chkLowercase.isSelected() && 
                !chkDigits.isSelected() && !chkSpecial.isSelected()) {
                // If this is the last checkbox being unchecked, prevent it
                ((JCheckBox)e.getSource()).setSelected(true);
                JOptionPane.showMessageDialog(dialog, 
                    "At least one character type must be selected.", 
                    "Warning", 
                    JOptionPane.WARNING_MESSAGE);
            }
        };
        
        // Add the listener to all checkboxes
        chkUppercase.addItemListener(checkBoxListener);
        chkLowercase.addItemListener(checkBoxListener);
        chkDigits.addItemListener(checkBoxListener);
        chkSpecial.addItemListener(checkBoxListener);
        
        // Generated password field
        JLabel lblGenerated = new JLabel("Generated Password:");
        lblGenerated.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 7;
        panel.add(lblGenerated, gbc);
        
        txtGenerated = new JTextField(20);
        txtGenerated.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtGenerated.setEditable(false);
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel.add(txtGenerated, gbc);
        
        // Generate button
        JButton btnGenerate = gui.createStyledButton("Generate", PasswordManagerGUI.SECONDARY_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.insets = new Insets(15, 5, 5, 5);
        panel.add(btnGenerate, gbc);
        
        // Generate button action
        btnGenerate.addActionListener(e -> {
            generatePassword();
        });
        
        // Generate an initial password with all options
        generatePassword();
        
        return panel;
    }
    
    /**
     * @brief Generates a password based on current settings
     */
    private void generatePassword() {
        int length = sliderLength.getValue();
        boolean includeUppercase = chkUppercase.isSelected();
        boolean includeLowercase = chkLowercase.isSelected();
        boolean includeDigits = chkDigits.isSelected();
        boolean includeSpecial = chkSpecial.isSelected();
        
        String generatedPassword = PasswordGenerator.generatePassword(
            length, includeUppercase, includeLowercase, includeDigits, includeSpecial);
        txtGenerated.setText(generatedPassword);
    }
    
    /**
     * @brief Creates the button panel with action buttons
     * @return JPanel containing buttons
     */
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton btnSave = gui.createStyledButton("Save", PasswordManagerGUI.PRIMARY_COLOR);
        JButton btnCancel = gui.createStyledButton("Cancel", PasswordManagerGUI.DARK_COLOR);
        
        buttonPanel.add(btnCancel);
        buttonPanel.add(btnSave);
        
        // Save button action
        btnSave.addActionListener(e -> savePassword());
        
        // Cancel button action
        btnCancel.addActionListener(e -> closeDialog());
        
        return buttonPanel;
    }
    
    /**
     * @brief Saves the generated password
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