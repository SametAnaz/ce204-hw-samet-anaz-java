package com.ucoruh.password.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.List;

import com.ucoruh.password.*;

/**
 * @brief Controller for View Passwords dialog
 * @details Handles functionality for viewing stored passwords
 */
public class ViewPasswordController implements DialogController {
    /** @brief Reference to main GUI */
    private PasswordManagerGUI gui;
    
    /** @brief Dialog window for viewing passwords */
    private JDialog dialog;
    
    /** @brief Authentication manager instance */
    private AuthManager authManager;
    
    /** @brief List of stored passwords */
    private List<Password> passwordList;
    
    /** @brief Table for displaying passwords */
    private JTable table;
    
    /**
     * @brief Constructor for ViewPasswordController
     * @param gui Reference to main GUI
     */
    public ViewPasswordController(PasswordManagerGUI gui) {
        this.gui = gui;
        this.authManager = AuthManager.getInstance();
    }
    
    /**
     * @brief Shows the dialog for viewing passwords
     */
    @Override
    public void showDialog() {
        // Create dialog
        dialog = new JDialog(gui, "All Passwords", true);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(gui);
        dialog.setLayout(new BorderLayout());
        
        // Load passwords
        loadPasswords();
        
        // Create table
        JScrollPane scrollPane = createPasswordTable();
        
        // Create button panel
        JPanel buttonPanel = createButtonPanel();
        
        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    
    /**
     * @brief Closes the view passwords dialog
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
     * @brief Loads passwords from storage
     */
    private void loadPasswords() {
        // Get all passwords
        InterfacePasswordStorage storage = PasswordStorageFactory.create(
                StorageType.FILE, authManager.getMasterPassword());
        passwordList = storage.readAll();
    }
    
    /**
     * @brief Creates the password table
     * @return JScrollPane containing password table
     */
    private JScrollPane createPasswordTable() {
        // Create table model
        String[] columnNames = {"Service/Website", "Username", "Password"};
        Object[][] data = new Object[passwordList.size()][3];
        
        for (int i = 0; i < passwordList.size(); i++) {
            Password password = passwordList.get(i);
            data[i][0] = password.getService();
            data[i][1] = password.getUsername();
            data[i][2] = "•••••••••";
        }
        
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        return scrollPane;
    }
    
    /**
     * @brief Creates the button panel with action buttons
     * @return JPanel containing buttons
     */
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JButton btnShow = gui.createStyledButton("Show Password", PasswordManagerGUI.PRIMARY_COLOR);
        JButton btnClose = gui.createStyledButton("Close", PasswordManagerGUI.DARK_COLOR);
        
        buttonPanel.add(btnShow);
        buttonPanel.add(btnClose);
        
        // Show password button action
        btnShow.addActionListener(e -> showPassword());
        
        // Close button action
        btnClose.addActionListener(e -> closeDialog());
        
        return buttonPanel;
    }
    
    /**
     * @brief Shows the selected password with a copy button
     */
    private void showPassword() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String service = (String) table.getValueAt(selectedRow, 0);
            String username = (String) table.getValueAt(selectedRow, 1);
            
            // Find the password
            for (Password password : passwordList) {
                if (password.getService().equals(service) && 
                    password.getUsername().equals(username)) {
                    
                    // Create custom dialog with copy option
                    String passwordText = password.getPassword();
                    JDialog passwordDialog = new JDialog(dialog, "Password", true);
                    passwordDialog.setLayout(new BorderLayout());
                    passwordDialog.setSize(400, 150);
                    passwordDialog.setLocationRelativeTo(dialog);
                    
                    // Message panel
                    JPanel messagePanel = new JPanel(new BorderLayout());
                    messagePanel.setBorder(new EmptyBorder(15, 15, 15, 15));
                    messagePanel.setBackground(Color.WHITE);
                    
                    JLabel passwordLabel = new JLabel(
                        "<html>Password for <b>" + service + "</b>: " + passwordText + "</html>");
                    passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                    messagePanel.add(passwordLabel, BorderLayout.CENTER);
                    
                    // Dialog button panel
                    JPanel dialogButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                    dialogButtonPanel.setBackground(Color.WHITE);
                    
                    JButton copyButton = gui.createStyledButton("Copy Password", PasswordManagerGUI.PRIMARY_COLOR);
                    JButton okButton = gui.createStyledButton("OK", PasswordManagerGUI.DARK_COLOR);
                    
                    dialogButtonPanel.add(copyButton);
                    dialogButtonPanel.add(okButton);
                    
                    // Copy button action
                    copyButton.addActionListener(copyEvent -> {
                        // Copy to clipboard
                        StringSelection selection = new StringSelection(passwordText);
                        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                        clipboard.setContents(selection, null);
                        
                        JOptionPane.showMessageDialog(passwordDialog, 
                                "Password copied to clipboard!", 
                                "Success", 
                                JOptionPane.INFORMATION_MESSAGE);
                    });
                    
                    // OK button action
                    okButton.addActionListener(okEvent -> passwordDialog.dispose());
                    
                    passwordDialog.add(messagePanel, BorderLayout.CENTER);
                    passwordDialog.add(dialogButtonPanel, BorderLayout.SOUTH);
                    passwordDialog.setVisible(true);
                    break;
                }
            }
        } else {
            JOptionPane.showMessageDialog(dialog, 
                    "Please select a password to show.", 
                    "No Selection", 
                    JOptionPane.WARNING_MESSAGE);
        }
    }
} 