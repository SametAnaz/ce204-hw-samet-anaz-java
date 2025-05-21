package com.ucoruh.password.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

import com.ucoruh.password.*;

/**
 * Controller for Delete Password dialog
 * Handles functionality for deleting existing passwords
 */
public class DeletePasswordController implements DialogController {
    private PasswordManagerGUI gui;
    private JDialog dialog;
    private AuthManager authManager;
    private List<Password> passwordList;
    private JComboBox<String> comboServices;
    
    /**
     * Constructor
     * @param gui Reference to main GUI
     */
    public DeletePasswordController(PasswordManagerGUI gui) {
        this.gui = gui;
        this.authManager = AuthManager.getInstance();
    }
    
    @Override
    public void showDialog() {
        // Load passwords
        loadPasswords();
        
        if (passwordList.isEmpty()) {
            JOptionPane.showMessageDialog(gui, 
                    "No passwords found to delete.", 
                    "No Passwords", 
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Create dialog
        dialog = new JDialog(gui, "Delete Password", true);
        dialog.setSize(450, 250);
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
        JLabel lblSelect = new JLabel("Select Service/Website to Delete:");
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
        
        // Warning message
        JLabel lblWarning = new JLabel("<html><div style='color:red;'>Warning: This action cannot be undone!</div></html>");
        lblWarning.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.insets = new Insets(20, 5, 5, 5);
        panel.add(lblWarning, gbc);
        
        return panel;
    }
    
    /**
     * Creates the button panel with action buttons
     * @return JPanel containing buttons
     */
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton btnDelete = gui.createStyledButton("Delete", gui.ACCENT_COLOR);
        JButton btnCancel = gui.createStyledButton("Cancel", gui.DARK_COLOR);
        
        buttonPanel.add(btnCancel);
        buttonPanel.add(btnDelete);
        
        // Delete button action
        btnDelete.addActionListener(e -> deletePassword());
        
        // Cancel button action
        btnCancel.addActionListener(e -> closeDialog());
        
        return buttonPanel;
    }
    
    /**
     * Deletes the selected password after confirmation
     */
    private void deletePassword() {
        int selectedIndex = comboServices.getSelectedIndex();
        
        // Confirm deletion
        int confirm = JOptionPane.showConfirmDialog(dialog, 
                "Are you sure you want to delete this password?", 
                "Confirm Deletion", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            // Remove password
            passwordList.remove(selectedIndex);
            
            // Save passwords
            InterfacePasswordStorage storage = PasswordStorageFactory.create(
                    StorageType.FILE, authManager.getMasterPassword());
            storage.writeAll(passwordList);
            
            JOptionPane.showMessageDialog(dialog, 
                    "Password deleted successfully!", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
            
            closeDialog();
        }
    }
} 