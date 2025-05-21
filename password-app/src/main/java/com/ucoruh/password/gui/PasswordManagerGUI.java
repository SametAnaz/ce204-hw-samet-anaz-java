package com.ucoruh.password.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.Scanner;

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
     * Login process
     */
    private void login() {
        char[] password = txtPassword.getPassword();
        String masterPassword = new String(password);
        
        // Convert String to Scanner
        Scanner scanner = new Scanner(masterPassword);
        
        if (authManager.login(scanner)) {
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
                    "Invalid master password. Please try again.", 
                    "Login Failed", 
                    JOptionPane.ERROR_MESSAGE);
        }
        
        // Close Scanner
        scanner.close();
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
        
        // Convert String to Scanner
        Scanner scanner = new Scanner(masterPassword);
        
        // Create master password
        authManager.createMasterPassword(scanner);
        
        JOptionPane.showMessageDialog(this, 
                "Master password created successfully.", 
                "Password Created", 
                JOptionPane.INFORMATION_MESSAGE);
                
        // Update GUI
        contentPane.removeAll();
        createComponents();
        contentPane.revalidate();
        contentPane.repaint();
        
        // Close Scanner
        scanner.close();
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
                JOptionPane.showMessageDialog(this, 
                        "User Authentication screen is under construction.", 
                        "Information", 
                        JOptionPane.INFORMATION_MESSAGE);
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
                JOptionPane.showMessageDialog(this, 
                        "Auto-Login Feature screen is under construction.", 
                        "Information", 
                        JOptionPane.INFORMATION_MESSAGE);
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
     * Show password management screen
     */
    private void showPasswordManagement() {
        // Password management screen creation
        JOptionPane.showMessageDialog(this, 
                "Password Storage & Management screen is under construction.", 
                "Information", 
                JOptionPane.INFORMATION_MESSAGE);
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
} 