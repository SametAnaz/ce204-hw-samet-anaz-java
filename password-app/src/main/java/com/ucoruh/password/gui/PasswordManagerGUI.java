package com.ucoruh.password.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

import com.ucoruh.password.*;

/**
 * Main GUI class for the Password Manager application
 */
public class PasswordManagerGUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private PasswordManager passwordManager;
    private AuthManager authManager;
    
    // Modern color palette
    private static final Color PRIMARY_COLOR = new Color(26, 115, 232);      // Google Blue
    private static final Color SECONDARY_COLOR = new Color(52, 168, 83);     // Google Green
    private static final Color ACCENT_COLOR = new Color(234, 67, 53);        // Google Red
    private static final Color DARK_COLOR = new Color(66, 66, 66);           // Dark gray
    private static final Color LIGHT_COLOR = new Color(245, 245, 245);       // Light gray
    private static final Color TEXT_COLOR = new Color(33, 33, 33);           // Text color
    private static final Color HEADER_BG = new Color(33, 33, 33);            // Header background

    /**
     * Launch the application
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            // Global style settings for Swing components
            UIManager.put("Button.arc", 8);
            UIManager.put("Component.arc", 8);
            UIManager.put("ProgressBar.arc", 8);
            UIManager.put("TextComponent.arc", 8);
            
            // Font settings
            Font defaultFont = new Font("Segoe UI", Font.PLAIN, 14);
            UIManager.put("Button.font", defaultFont);
            UIManager.put("Label.font", defaultFont);
            UIManager.put("TextField.font", defaultFont);
            UIManager.put("PasswordField.font", defaultFont);
            UIManager.put("TextArea.font", defaultFont);
            UIManager.put("ComboBox.font", defaultFont);
            UIManager.put("CheckBox.font", defaultFont);
            UIManager.put("RadioButton.font", defaultFont);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    PasswordManagerGUI frame = new PasswordManagerGUI();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the GUI
     */
    public PasswordManagerGUI() {
        setTitle("Password Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 900, 650);
        setMinimumSize(new Dimension(900, 650));
        
        // Initialize Authentication Manager
        authManager = AuthManager.getInstance();
        
        createComponents();
        
        // Clean up resources when window is closed
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // PasswordManager doesn't have an explicit savePasswords() method,
                // passwords are saved only when changed
                System.out.println("Application closing...");
            }
        });
        
        // Center on screen
        setLocationRelativeTo(null);
    }
    
    /**
     * Create GUI components
     */
    private void createComponents() {
        contentPane = new JPanel();
        contentPane.setBackground(LIGHT_COLOR);
        contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));
        
        // Top panel
        JPanel topPanel = new JPanel();
        topPanel.setBackground(HEADER_BG);
        topPanel.setPreferredSize(new Dimension(900, 70));
        contentPane.add(topPanel, BorderLayout.NORTH);
        topPanel.setLayout(new BorderLayout(0, 0));
        
        JLabel lblTitle = new JLabel("Password Manager");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setBorder(new EmptyBorder(10, 0, 10, 0));
        topPanel.add(lblTitle, BorderLayout.CENTER);
        
        // Login panel
        JPanel loginPanel = createLoginPanel();
        contentPane.add(loginPanel, BorderLayout.CENTER);
    }
    
    /**
     * Create login panel
     */
    private JPanel createLoginPanel() {
        JPanel loginContainer = new JPanel(new GridBagLayout());
        loginContainer.setBackground(LIGHT_COLOR);
        
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridBagLayout());
        loginPanel.setBackground(Color.WHITE);
        loginPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(224, 224, 224), 1, true),
                new EmptyBorder(30, 40, 30, 40)));
        
        // Shadow effect for panel
        loginPanel.setBorder(BorderFactory.createCompoundBorder(
            loginPanel.getBorder(),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Welcome message
        JLabel lblWelcome = new JLabel("Welcome to Password Manager");
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblWelcome.setForeground(DARK_COLOR);
        lblWelcome.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 8, 20, 8);
        loginPanel.add(lblWelcome, gbc);
        
        // Master password field
        JLabel lblUsername = new JLabel("Master Password:");
        lblUsername.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblUsername.setForeground(DARK_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 8, 5, 8);
        loginPanel.add(lblUsername, gbc);
        
        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtPassword.setMargin(new Insets(8, 8, 8, 8));
        txtPassword.setPreferredSize(new Dimension(300, 40));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 8, 20, 8);
        loginPanel.add(txtPassword, gbc);
        
        // Login button
        JButton btnLogin = createStyledButton("Login", PRIMARY_COLOR);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnLogin.setPreferredSize(new Dimension(300, 45));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 8, 5, 8);
        loginPanel.add(btnLogin, gbc);
        
        // Button click event
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
        
        // Create master password button
        JButton btnCreateMaster = createStyledButton("Create Master Password", SECONDARY_COLOR);
        btnCreateMaster.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnCreateMaster.setPreferredSize(new Dimension(300, 45));
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 8, 5, 8);
        loginPanel.add(btnCreateMaster, gbc);
        
        // Create master password button click event
        btnCreateMaster.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createMasterPassword();
            }
        });
        
        // Master password check
        if (authManager.isMasterPasswordSet()) {
            btnCreateMaster.setEnabled(false);
            lblWelcome.setText("Please Enter Your Master Password");
        } else {
            btnLogin.setEnabled(false);
            lblWelcome.setText("You Need to Create a Master Password");
        }
        
        // Enter key for login
        txtPassword.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (btnLogin.isEnabled()) {
                        login();
                    } else if (btnCreateMaster.isEnabled()) {
                        createMasterPassword();
                    }
                }
            }
        });
        
        // Add login panel to container
        loginContainer.add(loginPanel);
        
        return loginContainer;
    }
    
    /**
     * Create styled button
     */
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMargin(new Insets(10, 15, 10, 15));
        
        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(darken(bgColor, 0.1f));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                button.setBackground(darken(bgColor, 0.2f));
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                button.setBackground(darken(bgColor, 0.1f));
            }
        });
        
        return button;
    }
    
    /**
     * Darken color
     */
    private Color darken(Color color, float fraction) {
        int red = Math.max(0, Math.round(color.getRed() * (1 - fraction)));
        int green = Math.max(0, Math.round(color.getGreen() * (1 - fraction)));
        int blue = Math.max(0, Math.round(color.getBlue() * (1 - fraction)));
        return new Color(red, green, blue);
    }
    
    /**
     * Perform login
     */
    private void login() {
        char[] password = txtPassword.getPassword();
        String masterPassword = new String(password);
        
        if (masterPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                    "Please enter a master password.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        System.out.println("Attempting to login with password: " + masterPassword);
        System.out.println("Is master password set: " + authManager.isMasterPasswordSet());
        
        // Convert String to Scanner
        Scanner scanner = new Scanner(masterPassword);
        
        try {
            boolean loginSuccess = authManager.login(scanner);
            System.out.println("Login result: " + loginSuccess);
            
            if (loginSuccess) {
                JOptionPane.showMessageDialog(this, 
                        "Login successful!", 
                        "Success", 
                        JOptionPane.INFORMATION_MESSAGE);
                        
                // Initialize Password Manager with master password
                passwordManager = new PasswordManager(authManager.getMasterPassword());
                
                // Show main menu
                showMainMenu();
            } else {
                JOptionPane.showMessageDialog(this, 
                        "Invalid master password. Please try again.\nIf this problem persists, try deleting the master-password.txt file and restarting the application.", 
                        "Login Failed", 
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            System.out.println("Login error: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                    "An error occurred during login: " + e.getMessage(), 
                    "Login Error", 
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            // Ensure Scanner is closed
            scanner.close();
        }
    }
    
    /**
     * Create master password
     */
    private void createMasterPassword() {
        char[] password = txtPassword.getPassword();
        String masterPassword = new String(password);
        
        if (masterPassword.length() < 6) {
            JOptionPane.showMessageDialog(this, 
                    "Master password must be at least 6 characters long.", 
                    "Password Too Short", 
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        System.out.println("Creating master password: " + masterPassword);
        
        // Convert String to Scanner
        Scanner scanner = new Scanner(masterPassword);
        
        try {
            // Create master password
            authManager.createMasterPassword(scanner);
            
            System.out.println("Master password created successfully");
            System.out.println("Is master password set: " + authManager.isMasterPasswordSet());
            
            JOptionPane.showMessageDialog(this, 
                    "Master password created successfully.", 
                    "Password Created", 
                    JOptionPane.INFORMATION_MESSAGE);
                    
            // Update GUI
            contentPane.removeAll();
            createComponents();
            contentPane.revalidate();
            contentPane.repaint();
        } catch (Exception e) {
            System.out.println("Error creating master password: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                    "An error occurred while creating the master password: " + e.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            // Ensure Scanner is closed
            scanner.close();
        }
    }
    
    /**
     * Show main menu
     */
    private void showMainMenu() {
        // Clear existing content
        contentPane.removeAll();
        
        // Top panel
        JPanel topPanel = new JPanel();
        topPanel.setBackground(HEADER_BG);
        topPanel.setPreferredSize(new Dimension(900, 70));
        contentPane.add(topPanel, BorderLayout.NORTH);
        topPanel.setLayout(new BorderLayout(0, 0));
        
        JLabel lblTitle = new JLabel("Password Manager - Main Menu");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setBorder(new EmptyBorder(10, 0, 10, 0));
        topPanel.add(lblTitle, BorderLayout.CENTER);
        
        // Main menu panel
        JPanel mainContainer = new JPanel();
        mainContainer.setBackground(LIGHT_COLOR);
        mainContainer.setLayout(new GridBagLayout());
        contentPane.add(mainContainer, BorderLayout.CENTER);
        
        JPanel menuPanel = new JPanel();
        menuPanel.setBackground(Color.WHITE);
        menuPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(224, 224, 224), 1, true),
                new EmptyBorder(40, 40, 40, 40)));
        menuPanel.setLayout(new GridLayout(5, 1, 0, 20));
        mainContainer.add(menuPanel);
        
        // Menu buttons
        String[] menuItems = {
            "User Authentication",
            "Password Storage & Management",
            "Password Generator",
            "Auto-Login Feature",
            "Multi-Platform Compatibility"
        };
        
        Color[] buttonColors = {
            PRIMARY_COLOR,
            new Color(142, 36, 170),  // Purple
            SECONDARY_COLOR,
            new Color(255, 143, 0),   // Orange
            new Color(0, 131, 143)    // Teal
        };
        
        for (int i = 0; i < menuItems.length; i++) {
            JButton button = createStyledButton(menuItems[i], buttonColors[i]);
            button.setPreferredSize(new Dimension(400, 60));
            final int menuIndex = i;
            
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    handleMenuSelection(menuIndex);
                }
            });
            
            menuPanel.add(button);
        }
        
        // Refresh panel
        contentPane.revalidate();
        contentPane.repaint();
    }
    
    /**
     * Handle menu selection
     */
    private void handleMenuSelection(int menuIndex) {
        switch (menuIndex) {
            case 0:
                // User Authentication
                showUserAuthentication();
                break;
            case 1:
                // Password Storage & Management
                showPasswordManagement();
                break;
            case 2:
                // Password Generator
                showPasswordGenerator();
                break;
            case 3:
                // Auto-Login Feature
                showAutoLoginFeature();
                break;
            case 4:
                // Multi-Platform Compatibility
                JOptionPane.showMessageDialog(this, 
                        "This password manager is compatible with the following platforms:\n" +
                        "- Windows\n" +
                        "- macOS\n" +
                        "- Linux\n" +
                        "- Android\n" +
                        "- iOS", 
                        "Platform Compatibility", 
                        JOptionPane.INFORMATION_MESSAGE);
                break;
        }
    }
    
    /**
     * Show user authentication screen
     */
    private void showUserAuthentication() {
        // Clear existing content
        contentPane.removeAll();
        
        // Top panel
        JPanel topPanel = new JPanel();
        topPanel.setBackground(HEADER_BG);
        topPanel.setPreferredSize(new Dimension(900, 70));
        contentPane.add(topPanel, BorderLayout.NORTH);
        topPanel.setLayout(new BorderLayout(0, 0));
        
        JLabel lblTitle = new JLabel("User Authentication");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setBorder(new EmptyBorder(10, 0, 10, 0));
        topPanel.add(lblTitle, BorderLayout.CENTER);
        
        // Back button
        JButton btnBack = new JButton("Back");
        btnBack.setForeground(Color.WHITE);
        btnBack.setBackground(ACCENT_COLOR);
        btnBack.setBorderPainted(false);
        btnBack.setFocusPainted(false);
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnBack.setMargin(new Insets(10, 15, 10, 15));
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showMainMenu();
            }
        });
        btnBack.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnBack.setBackground(darken(ACCENT_COLOR, 0.1f));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                btnBack.setBackground(ACCENT_COLOR);
            }
        });
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnPanel.setBackground(HEADER_BG);
        btnPanel.add(btnBack);
        topPanel.add(btnPanel, BorderLayout.WEST);
        
        // Main content
        JPanel mainContainer = new JPanel();
        mainContainer.setBackground(LIGHT_COLOR);
        mainContainer.setLayout(new GridBagLayout());
        contentPane.add(mainContainer, BorderLayout.CENTER);
        
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(224, 224, 224), 1, true),
                new EmptyBorder(40, 40, 40, 40)));
        centerPanel.setLayout(new GridBagLayout());
        mainContainer.add(centerPanel);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Current password
        JLabel lblCurrentPassword = new JLabel("Current Master Password:");
        lblCurrentPassword.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblCurrentPassword.setForeground(DARK_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        centerPanel.add(lblCurrentPassword, gbc);
        
        JPasswordField txtCurrentPassword = new JPasswordField();
        txtCurrentPassword.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtCurrentPassword.setMargin(new Insets(8, 8, 8, 8));
        txtCurrentPassword.setPreferredSize(new Dimension(350, 40));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        centerPanel.add(txtCurrentPassword, gbc);
        
        // New password
        JLabel lblNewPassword = new JLabel("New Master Password:");
        lblNewPassword.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblNewPassword.setForeground(DARK_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(30, 10, 10, 10);
        centerPanel.add(lblNewPassword, gbc);
        
        JPasswordField txtNewPassword = new JPasswordField();
        txtNewPassword.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtNewPassword.setMargin(new Insets(8, 8, 8, 8));
        txtNewPassword.setPreferredSize(new Dimension(350, 40));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 10, 10, 10);
        centerPanel.add(txtNewPassword, gbc);
        
        // Confirm new password
        JLabel lblConfirmPassword = new JLabel("Confirm New Master Password:");
        lblConfirmPassword.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblConfirmPassword.setForeground(DARK_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        centerPanel.add(lblConfirmPassword, gbc);
        
        JPasswordField txtConfirmPassword = new JPasswordField();
        txtConfirmPassword.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtConfirmPassword.setMargin(new Insets(8, 8, 8, 8));
        txtConfirmPassword.setPreferredSize(new Dimension(350, 40));
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        centerPanel.add(txtConfirmPassword, gbc);
        
        // Change password button
        JButton btnChangePassword = createStyledButton("Change Master Password", PRIMARY_COLOR);
        btnChangePassword.setPreferredSize(new Dimension(250, 45));
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(30, 10, 10, 10);
        centerPanel.add(btnChangePassword, gbc);
        
        // Change password button click event
        btnChangePassword.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Get current password
                String currentPassword = new String(txtCurrentPassword.getPassword());
                
                // Validate current password
                Scanner scanner = new Scanner(currentPassword);
                if (!authManager.login(scanner)) {
                    JOptionPane.showMessageDialog(PasswordManagerGUI.this, 
                            "Current password is incorrect.", 
                            "Error", 
                            JOptionPane.ERROR_MESSAGE);
                    scanner.close();
                    return;
                }
                scanner.close();
                
                // Validate new password
                String newPassword = new String(txtNewPassword.getPassword());
                String confirmPassword = new String(txtConfirmPassword.getPassword());
                
                if (newPassword.length() < 6) {
                    JOptionPane.showMessageDialog(PasswordManagerGUI.this, 
                            "New password must be at least 6 characters long.", 
                            "Error", 
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (!newPassword.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(PasswordManagerGUI.this, 
                            "New passwords do not match.", 
                            "Error", 
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Change master password
                Scanner newScanner = new Scanner(newPassword);
                authManager.createMasterPassword(newScanner);
                newScanner.close();
                
                JOptionPane.showMessageDialog(PasswordManagerGUI.this, 
                        "Master password changed successfully.", 
                        "Success", 
                        JOptionPane.INFORMATION_MESSAGE);
                
                // Update password manager with new master password
                passwordManager = new PasswordManager(authManager.getMasterPassword());
                
                // Clear fields
                txtCurrentPassword.setText("");
                txtNewPassword.setText("");
                txtConfirmPassword.setText("");
            }
        });
        
        // Refresh panel
        contentPane.revalidate();
        contentPane.repaint();
    }
    
    /**
     * Show password management screen
     */
    private void showPasswordManagement() {
        // Clear existing content
        contentPane.removeAll();
        
        // Top panel
        JPanel topPanel = new JPanel();
        topPanel.setBackground(HEADER_BG);
        topPanel.setPreferredSize(new Dimension(900, 70));
        contentPane.add(topPanel, BorderLayout.NORTH);
        topPanel.setLayout(new BorderLayout(0, 0));
        
        JLabel lblTitle = new JLabel("Password Storage & Management");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setBorder(new EmptyBorder(10, 0, 10, 0));
        topPanel.add(lblTitle, BorderLayout.CENTER);
        
        // Back button
        JButton btnBack = new JButton("Back");
        btnBack.setForeground(Color.WHITE);
        btnBack.setBackground(ACCENT_COLOR);
        btnBack.setBorderPainted(false);
        btnBack.setFocusPainted(false);
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnBack.setMargin(new Insets(10, 15, 10, 15));
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showMainMenu();
            }
        });
        btnBack.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnBack.setBackground(darken(ACCENT_COLOR, 0.1f));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                btnBack.setBackground(ACCENT_COLOR);
            }
        });
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnPanel.setBackground(HEADER_BG);
        btnPanel.add(btnBack);
        topPanel.add(btnPanel, BorderLayout.WEST);
        
        // Main container
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(LIGHT_COLOR);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        contentPane.add(mainPanel, BorderLayout.CENTER);
        
        // Display simple placeholder message
        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.setBackground(Color.WHITE);
        messagePanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(224, 224, 224), 1, true),
                new EmptyBorder(40, 40, 40, 40)));
        
        JLabel lblMessage = new JLabel("<html><div style='text-align: center;'>Password Storage & Management<br><br>" +
                "This screen allows you to manage your stored passwords.<br><br>" +
                "You can add, view, update, and delete passwords securely.</div></html>");
        lblMessage.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblMessage.setForeground(DARK_COLOR);
        lblMessage.setHorizontalAlignment(SwingConstants.CENTER);
        messagePanel.add(lblMessage, BorderLayout.CENTER);
        
        // Add buttons to access password management functions
        JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 0, 20));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(new EmptyBorder(30, 60, 30, 60));
        
        JButton btnAddPassword = createStyledButton("Add New Password", PRIMARY_COLOR);
        JButton btnViewPasswords = createStyledButton("View All Passwords", SECONDARY_COLOR);
        JButton btnUpdatePassword = createStyledButton("Update Password", new Color(142, 36, 170)); // Purple
        JButton btnDeletePassword = createStyledButton("Delete Password", ACCENT_COLOR);
        JButton btnGeneratePassword = createStyledButton("Generate and Save Password", new Color(255, 143, 0)); // Orange
        
        buttonPanel.add(btnAddPassword);
        buttonPanel.add(btnViewPasswords);
        buttonPanel.add(btnUpdatePassword);
        buttonPanel.add(btnDeletePassword);
        buttonPanel.add(btnGeneratePassword);
        
        messagePanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.add(messagePanel, BorderLayout.CENTER);
        
        // Button actions
        btnAddPassword.addActionListener(e -> {
            showAddPasswordDialog();
        });
        
        btnViewPasswords.addActionListener(e -> {
            showViewPasswordsDialog();
        });
        
        btnUpdatePassword.addActionListener(e -> {
            showUpdatePasswordDialog();
        });
        
        btnDeletePassword.addActionListener(e -> {
            showDeletePasswordDialog();
        });
        
        btnGeneratePassword.addActionListener(e -> {
            showGeneratePasswordDialog();
        });
        
        // Refresh panel
        contentPane.revalidate();
        contentPane.repaint();
    }
    
    /**
     * Show dialog to add a new password
     */
    private void showAddPasswordDialog() {
        JDialog dialog = new JDialog(this, "Add New Password", true);
        dialog.setSize(450, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        
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
        
        JTextField txtService = new JTextField(20);
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
        
        JTextField txtUsername = new JTextField(20);
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
        
        JPasswordField txtPassword = new JPasswordField(20);
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
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton btnSave = createStyledButton("Save", PRIMARY_COLOR);
        JButton btnCancel = createStyledButton("Cancel", DARK_COLOR);
        
        buttonPanel.add(btnCancel);
        buttonPanel.add(btnSave);
        
        // Save button action
        btnSave.addActionListener(e -> {
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
            
            dialog.dispose();
        });
        
        // Cancel button action
        btnCancel.addActionListener(e -> dialog.dispose());
        
        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    
    /**
     * Show dialog to view all passwords
     */
    private void showViewPasswordsDialog() {
        JDialog dialog = new JDialog(this, "All Passwords", true);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        
        // Get all passwords
        InterfacePasswordStorage storage = PasswordStorageFactory.create(
                StorageType.FILE, authManager.getMasterPassword());
        List<Password> passwordList = storage.readAll();
        
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
        
        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JButton btnShow = createStyledButton("Show Password", PRIMARY_COLOR);
        JButton btnClose = createStyledButton("Close", DARK_COLOR);
        
        buttonPanel.add(btnShow);
        buttonPanel.add(btnClose);
        
        // Show password button action
        btnShow.addActionListener(e -> {
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
                        
                        JButton copyButton = createStyledButton("Copy Password", PRIMARY_COLOR);
                        JButton okButton = createStyledButton("OK", DARK_COLOR);
                        
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
        });
        
        // Close button action
        btnClose.addActionListener(e -> dialog.dispose());
        
        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    
    /**
     * Show dialog to update a password
     */
    private void showUpdatePasswordDialog() {
        // Get all passwords
        InterfacePasswordStorage storage = PasswordStorageFactory.create(
                StorageType.FILE, authManager.getMasterPassword());
        List<Password> passwordList = storage.readAll();
        
        if (passwordList.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                    "No passwords found to update.", 
                    "No Passwords", 
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Create dialog
        JDialog dialog = new JDialog(this, "Update Password", true);
        dialog.setSize(450, 450);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        
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
        
        JComboBox<String> comboServices = new JComboBox<>(services);
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
        
        JPasswordField txtPassword = new JPasswordField(20);
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
        JButton btnGenerate = createStyledButton("Generate Password", SECONDARY_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.insets = new Insets(15, 5, 5, 5);
        panel.add(btnGenerate, gbc);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton btnUpdate = createStyledButton("Update", PRIMARY_COLOR);
        JButton btnCancel = createStyledButton("Cancel", DARK_COLOR);
        
        buttonPanel.add(btnCancel);
        buttonPanel.add(btnUpdate);
        
        // Generate password button action
        btnGenerate.addActionListener(e -> {
            String generatedPassword = PasswordGenerator.generatePassword(12);
            txtPassword.setText(generatedPassword);
        });
        
        // Update button action
        btnUpdate.addActionListener(e -> {
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
            storage.writeAll(passwordList);
            
            JOptionPane.showMessageDialog(dialog, 
                    "Password updated successfully!", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
            
            dialog.dispose();
        });
        
        // Cancel button action
        btnCancel.addActionListener(e -> dialog.dispose());
        
        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    
    /**
     * Show dialog to delete a password
     */
    private void showDeletePasswordDialog() {
        // Get all passwords
        InterfacePasswordStorage storage = PasswordStorageFactory.create(
                StorageType.FILE, authManager.getMasterPassword());
        List<Password> passwordList = storage.readAll();
        
        if (passwordList.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                    "No passwords found to delete.", 
                    "No Passwords", 
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Create dialog
        JDialog dialog = new JDialog(this, "Delete Password", true);
        dialog.setSize(450, 250);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        
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
        
        JComboBox<String> comboServices = new JComboBox<>(services);
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
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton btnDelete = createStyledButton("Delete", ACCENT_COLOR);
        JButton btnCancel = createStyledButton("Cancel", DARK_COLOR);
        
        buttonPanel.add(btnCancel);
        buttonPanel.add(btnDelete);
        
        // Delete button action
        btnDelete.addActionListener(e -> {
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
                storage.writeAll(passwordList);
                
                JOptionPane.showMessageDialog(dialog, 
                        "Password deleted successfully!", 
                        "Success", 
                        JOptionPane.INFORMATION_MESSAGE);
                
                dialog.dispose();
            }
        });
        
        // Cancel button action
        btnCancel.addActionListener(e -> dialog.dispose());
        
        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    
    /**
     * Show dialog to generate and save a password
     */
    private void showGeneratePasswordDialog() {
        // Create dialog
        JDialog dialog = new JDialog(this, "Generate and Save Password", true);
        dialog.setSize(450, 450);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        
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
        
        JTextField txtService = new JTextField(20);
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
        
        JTextField txtUsername = new JTextField(20);
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
        
        JSlider sliderLength = new JSlider(JSlider.HORIZONTAL, 8, 32, 16);
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
        
        JTextField txtGenerated = new JTextField(20);
        txtGenerated.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtGenerated.setEditable(false);
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel.add(txtGenerated, gbc);
        
        // Generate button
        JButton btnGenerate = createStyledButton("Generate", SECONDARY_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.insets = new Insets(15, 5, 5, 5);
        panel.add(btnGenerate, gbc);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton btnSave = createStyledButton("Save", PRIMARY_COLOR);
        JButton btnCancel = createStyledButton("Cancel", DARK_COLOR);
        
        buttonPanel.add(btnCancel);
        buttonPanel.add(btnSave);
        
        // Generate button action
        btnGenerate.addActionListener(e -> {
            int length = sliderLength.getValue();
            String generatedPassword = PasswordGenerator.generatePassword(length);
            txtGenerated.setText(generatedPassword);
        });
        
        // Save button action
        btnSave.addActionListener(e -> {
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
            
            dialog.dispose();
        });
        
        // Cancel button action
        btnCancel.addActionListener(e -> dialog.dispose());
        
        // Generate an initial password
        btnGenerate.doClick();
        
        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    
    /**
     * Show password generator screen
     */
    private void showPasswordGenerator() {
        // Clear existing content
        contentPane.removeAll();
        
        // Top panel
        JPanel topPanel = new JPanel();
        topPanel.setBackground(HEADER_BG);
        topPanel.setPreferredSize(new Dimension(900, 70));
        contentPane.add(topPanel, BorderLayout.NORTH);
        topPanel.setLayout(new BorderLayout(0, 0));
        
        JLabel lblTitle = new JLabel("Password Generator");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setBorder(new EmptyBorder(10, 0, 10, 0));
        topPanel.add(lblTitle, BorderLayout.CENTER);
        
        // Back button
        JButton btnBack = new JButton("Back");
        btnBack.setForeground(Color.WHITE);
        btnBack.setBackground(ACCENT_COLOR);
        btnBack.setBorderPainted(false);
        btnBack.setFocusPainted(false);
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnBack.setMargin(new Insets(10, 15, 10, 15));
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showMainMenu();
            }
        });
        btnBack.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnBack.setBackground(darken(ACCENT_COLOR, 0.1f));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                btnBack.setBackground(ACCENT_COLOR);
            }
        });
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnPanel.setBackground(HEADER_BG);
        btnPanel.add(btnBack);
        topPanel.add(btnPanel, BorderLayout.WEST);
        
        // Main content
        JPanel mainContainer = new JPanel();
        mainContainer.setBackground(LIGHT_COLOR);
        mainContainer.setLayout(new GridBagLayout());
        contentPane.add(mainContainer, BorderLayout.CENTER);
        
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(224, 224, 224), 1, true),
                new EmptyBorder(40, 40, 40, 40)));
        centerPanel.setLayout(new GridBagLayout());
        mainContainer.add(centerPanel);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Password length
        JLabel lblLength = new JLabel("Password Length:");
        lblLength.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblLength.setForeground(DARK_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        centerPanel.add(lblLength, gbc);
        
        JSpinner spinnerLength = new JSpinner(new SpinnerNumberModel(12, 4, 32, 1));
        spinnerLength.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        JComponent editor = spinnerLength.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            JSpinner.DefaultEditor spinnerEditor = (JSpinner.DefaultEditor)editor;
            spinnerEditor.getTextField().setHorizontalAlignment(JTextField.CENTER);
            spinnerEditor.getTextField().setFont(new Font("Segoe UI", Font.PLAIN, 16));
        }
        spinnerLength.setPreferredSize(new Dimension(100, 40));
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        centerPanel.add(spinnerLength, gbc);
        
        // Generated password
        JLabel lblGeneratedPassword = new JLabel("Generated Password:");
        lblGeneratedPassword.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblGeneratedPassword.setForeground(DARK_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(30, 10, 10, 10);
        centerPanel.add(lblGeneratedPassword, gbc);
        
        JTextField txtGeneratedPassword = new JTextField();
        txtGeneratedPassword.setFont(new Font("Courier New", Font.BOLD, 18));
        txtGeneratedPassword.setEditable(false);
        txtGeneratedPassword.setHorizontalAlignment(JTextField.CENTER);
        txtGeneratedPassword.setMargin(new Insets(10, 10, 10, 10));
        txtGeneratedPassword.setPreferredSize(new Dimension(400, 45));
        txtGeneratedPassword.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(224, 224, 224), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        txtGeneratedPassword.setBackground(new Color(250, 250, 250));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 10, 20, 10);
        centerPanel.add(txtGeneratedPassword, gbc);
        
        // Copy button
        JButton btnCopy = createStyledButton("Copy", PRIMARY_COLOR);
        btnCopy.setPreferredSize(new Dimension(120, 45));
        btnCopy.setEnabled(false);
        btnCopy.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                txtGeneratedPassword.selectAll();
                txtGeneratedPassword.copy();
                JOptionPane.showMessageDialog(PasswordManagerGUI.this, 
                        "Password copied to clipboard!", 
                        "Information", 
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.weightx = 0.5;
        gbc.insets = new Insets(10, 10, 10, 5);
        centerPanel.add(btnCopy, gbc);
        
        // Generate password button
        JButton btnGenerate = createStyledButton("Generate Password", SECONDARY_COLOR);
        btnGenerate.setPreferredSize(new Dimension(200, 45));
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.weightx = 0.5;
        gbc.insets = new Insets(10, 5, 10, 10);
        centerPanel.add(btnGenerate, gbc);
        
        // Generate password button click event
        btnGenerate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int length = (int) spinnerLength.getValue();
                String password = PasswordGenerator.generatePassword(length);
                txtGeneratedPassword.setText(password);
                btnCopy.setEnabled(true);
            }
        });
        
        // Refresh panel
        contentPane.revalidate();
        contentPane.repaint();
    }
    
    /**
     * Show auto-login feature screen
     */
    private void showAutoLoginFeature() {
        // Clear existing content
        contentPane.removeAll();
        
        // Top panel
        JPanel topPanel = new JPanel();
        topPanel.setBackground(HEADER_BG);
        topPanel.setPreferredSize(new Dimension(900, 70));
        contentPane.add(topPanel, BorderLayout.NORTH);
        topPanel.setLayout(new BorderLayout(0, 0));
        
        JLabel lblTitle = new JLabel("Auto-Login Feature");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setBorder(new EmptyBorder(10, 0, 10, 0));
        topPanel.add(lblTitle, BorderLayout.CENTER);
        
        // Back button
        JButton btnBack = new JButton("Back");
        btnBack.setForeground(Color.WHITE);
        btnBack.setBackground(ACCENT_COLOR);
        btnBack.setBorderPainted(false);
        btnBack.setFocusPainted(false);
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnBack.setMargin(new Insets(10, 15, 10, 15));
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showMainMenu();
            }
        });
        btnBack.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnBack.setBackground(darken(ACCENT_COLOR, 0.1f));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                btnBack.setBackground(ACCENT_COLOR);
            }
        });
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnPanel.setBackground(HEADER_BG);
        btnPanel.add(btnBack);
        topPanel.add(btnPanel, BorderLayout.WEST);
        
        // Main container
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(LIGHT_COLOR);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        contentPane.add(mainPanel, BorderLayout.CENTER);
        
        // Auto-login panel
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout(0, 20));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(224, 224, 224), 1, true),
                new EmptyBorder(30, 30, 30, 30)));
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        // Information panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BorderLayout(0, 15));
        infoPanel.setBackground(Color.WHITE);
        centerPanel.add(infoPanel, BorderLayout.NORTH);
        
        JLabel lblInfo = new JLabel("Auto-Login Feature");
        lblInfo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblInfo.setForeground(DARK_COLOR);
        infoPanel.add(lblInfo, BorderLayout.NORTH);
        
        JTextArea txtInfo = new JTextArea(
            "The Auto-Login feature allows the password manager to automatically fill in " +
            "login credentials for websites or applications. Select a service from your saved " +
            "credentials and click the 'Auto Login' button to use this feature.\n\n" +
            "This feature is only available for supported browsers and applications."
        );
        txtInfo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtInfo.setEditable(false);
        txtInfo.setWrapStyleWord(true);
        txtInfo.setLineWrap(true);
        txtInfo.setBackground(Color.WHITE);
        txtInfo.setBorder(null);
        txtInfo.setMargin(new Insets(10, 0, 10, 0));
        infoPanel.add(txtInfo, BorderLayout.CENTER);
        
        // Password selection panel
        JPanel selectionPanel = new JPanel();
        selectionPanel.setLayout(new GridBagLayout());
        selectionPanel.setBackground(Color.WHITE);
        centerPanel.add(selectionPanel, BorderLayout.CENTER);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Service selection
        JLabel lblService = new JLabel("Select Service:");
        lblService.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblService.setForeground(DARK_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        selectionPanel.add(lblService, gbc);
        
        // Sample service list
        String[] services = {"Gmail", "Facebook", "Twitter", "LinkedIn", "Amazon"};
        JComboBox<String> comboServices = new JComboBox<>(services);
        comboServices.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboServices.setPreferredSize(new Dimension(400, 40));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        selectionPanel.add(comboServices, gbc);
        
        // Browser selection
        JLabel lblBrowser = new JLabel("Select Browser:");
        lblBrowser.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblBrowser.setForeground(DARK_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        gbc.insets = new Insets(30, 10, 10, 10);
        selectionPanel.add(lblBrowser, gbc);
        
        String[] browsers = {"Chrome", "Firefox", "Edge", "Safari", "Opera"};
        JComboBox<String> comboBrowsers = new JComboBox<>(browsers);
        comboBrowsers.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboBrowsers.setPreferredSize(new Dimension(400, 40));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(10, 10, 10, 10);
        selectionPanel.add(comboBrowsers, gbc);
        
        // Auto login button
        JButton btnAutoLogin = createStyledButton("Launch Auto-Login", PRIMARY_COLOR);
        btnAutoLogin.setPreferredSize(new Dimension(200, 45));
        btnAutoLogin.setFont(new Font("Segoe UI", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.insets = new Insets(30, 10, 10, 10);
        selectionPanel.add(btnAutoLogin, gbc);
        
        // Auto-login button action
        btnAutoLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedService = (String) comboServices.getSelectedItem();
                String selectedBrowser = (String) comboBrowsers.getSelectedItem();
                
                if (selectedService != null) {
                    // Create an instance of AutoLoginManager
                    AutoLoginManager manager = new AutoLoginManager();
                    
                    // Show success dialog
                    JOptionPane.showMessageDialog(
                        PasswordManagerGUI.this,
                        "Auto-login initiated for " + selectedService + " using " + selectedBrowser + ".\n" +
                        "The browser will be launched and credentials will be automatically filled.",
                        "Auto-Login",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                } else {
                    JOptionPane.showMessageDialog(
                        PasswordManagerGUI.this,
                        "No service selected or no credentials available.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });
        
        // Bottom info panel
        JPanel securityPanel = new JPanel();
        securityPanel.setLayout(new BorderLayout());
        securityPanel.setBackground(new Color(253, 237, 237));
        securityPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(250, 210, 210), 1, true),
                new EmptyBorder(15, 15, 15, 15)));
        centerPanel.add(securityPanel, BorderLayout.SOUTH);
        
        JLabel lblSecurity = new JLabel("Security Note");
        lblSecurity.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblSecurity.setForeground(new Color(180, 0, 0));
        securityPanel.add(lblSecurity, BorderLayout.NORTH);
        
        JTextArea txtSecurity = new JTextArea(
            "Auto-login features require granting certain permissions to the password manager. " +
            "For security reasons, always ensure that you're using this feature on trusted devices " +
            "and connections. The password manager will never share your credentials with third parties."
        );
        txtSecurity.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSecurity.setEditable(false);
        txtSecurity.setWrapStyleWord(true);
        txtSecurity.setLineWrap(true);
        txtSecurity.setBackground(new Color(253, 237, 237));
        txtSecurity.setBorder(null);
        txtSecurity.setMargin(new Insets(10, 0, 0, 0));
        securityPanel.add(txtSecurity, BorderLayout.CENTER);
        
        // Refresh panel
        contentPane.revalidate();
        contentPane.repaint();
    }
} 