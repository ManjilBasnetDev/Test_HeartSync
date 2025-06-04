package heartsyncdatingapp.View;

import heartsyncdatingapp.dao.UserRegisterDAO;
import heartsyncdatingapp.model.LoginFinal;
import heartsyncdatingapp.model.User;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class Register extends JFrame {
    private static final int WINDOW_RADIUS = 35;
    private static final int FIELD_RADIUS = 35;
    private static final int BUTTON_RADIUS = 35;
    private JPanel mainPanel;
    private JTextArea usernameField;
    private JTextArea passwordField;
    private JTextArea confirmField;
    private JButton continueButton;
    private JButton backButton;
    private JRadioButton userRadio;
    private JRadioButton adminRadio;
    private JCheckBox showPassword1;
    private JCheckBox showPassword2;
    private Point mouseDownCompCoords;
    
    // Add variables to store actual passwords
    private String actualPassword = "";
    private String actualConfirmPassword = "";

    public Register() {
        initComponents();
        setupListeners();
        setupWindowDragging();
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), WINDOW_RADIUS, WINDOW_RADIUS));
    }

    private void initComponents() {
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Main panel with pink background and rounded corners
        mainPanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 192, 203));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), WINDOW_RADIUS, WINDOW_RADIUS);
            }
        };
        mainPanel.setPreferredSize(new Dimension(400, 450));
        mainPanel.setOpaque(false);
        
        // Title
        JLabel titleLabel = new JLabel("CREATE ACCOUNT");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBounds(0, 40, 400, 40);
        
        // Username field
        usernameField = new JTextArea();
        usernameField.setFont(new Font("Segoe UI", 0, 14));
        usernameField.setBackground(Color.WHITE);
        usernameField.setForeground(Color.GRAY);
        usernameField.setLineWrap(true);
        usernameField.setWrapStyleWord(true);
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)));
        usernameField.setRows(1);
        usernameField.setBounds(50, 100, 300, 45);
        
        // Password field
        passwordField = new JTextArea();
        passwordField.setFont(new Font("Segoe UI", 0, 14));
        passwordField.setBackground(Color.WHITE);
        passwordField.setForeground(Color.GRAY);
        passwordField.setLineWrap(true);
        passwordField.setWrapStyleWord(true);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)));
        passwordField.setRows(1);
        passwordField.setBounds(50, 160, 300, 45);
        
        // Show password checkboxes
        showPassword1 = new JCheckBox("Show");
        showPassword2 = new JCheckBox("Show");
        showPassword1.setBounds(360, 170, 20, 20);
        showPassword2.setBounds(360, 230, 20, 20);
        showPassword1.setOpaque(false);
        showPassword2.setOpaque(false);
        
        // Confirm password field
        confirmField = new JTextArea();
        confirmField.setFont(new Font("Segoe UI", 0, 14));
        confirmField.setBackground(Color.WHITE);
        confirmField.setForeground(Color.GRAY);
        confirmField.setLineWrap(true);
        confirmField.setWrapStyleWord(true);
        confirmField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)));
        confirmField.setRows(1);
        confirmField.setBounds(50, 220, 300, 45);
        
        // Radio buttons
        userRadio = new JRadioButton("USER");
        adminRadio = new JRadioButton("ADMIN");
        ButtonGroup group = new ButtonGroup();
        group.add(userRadio);
        group.add(adminRadio);
        userRadio.setSelected(true);
        
        userRadio.setBounds(50, 280, 80, 25);
        adminRadio.setBounds(200, 280, 80, 25);
        userRadio.setOpaque(false);
        adminRadio.setOpaque(false);
        userRadio.setFont(new Font("Arial", Font.PLAIN, 14));
        adminRadio.setFont(new Font("Arial", Font.PLAIN, 14));
        userRadio.setForeground(Color.BLACK);
        adminRadio.setForeground(Color.BLACK);
        
        // Add mouse listener to admin radio button for immediate response
        adminRadio.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int choice = JOptionPane.showConfirmDialog(Register.this,
                    "Admin registration requires special authorization.\nDo you have an admin access code?",
                    "Admin Registration",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
                
                if (choice == JOptionPane.YES_OPTION) {
                    String accessCode = JOptionPane.showInputDialog(Register.this,
                        "Please enter the admin access code:",
                        "Admin Access Code",
                        JOptionPane.QUESTION_MESSAGE);
                    
                    if (accessCode == null || !accessCode.equals("3023")) {
                        JOptionPane.showMessageDialog(Register.this,
                            "Invalid access code. Switching to regular user registration.",
                            "Access Denied",
                            JOptionPane.ERROR_MESSAGE);
                        userRadio.setSelected(true);
                    }
                } else {
                    userRadio.setSelected(true);
                }
            }
        });
        
        // Continue button
        continueButton = new JButton("CONTINUE -->") {
            private boolean isHovered = false;
            
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Darker shade when hovered
                if (isHovered) {
                    g2.setColor(new Color(153, 255, 153)); // Darker green
                } else {
                    g2.setColor(new Color(204, 255, 204)); // Light green
                }
                
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), BUTTON_RADIUS, BUTTON_RADIUS);
                super.paintComponent(g);
            }
            
            {
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        isHovered = true;
                        repaint();
                    }
                    
                    @Override
                    public void mouseExited(MouseEvent e) {
                        isHovered = false;
                        repaint();
                    }
                });
            }
        };
        continueButton.setBorder(null);
        continueButton.setForeground(new Color(0, 100, 0));
        continueButton.setFont(new Font("Arial", Font.BOLD, 14));
        continueButton.setBounds(50, 340, 145, 45);
        continueButton.setContentAreaFilled(false);
        
        // Back button
        backButton = new JButton("BACK -->") {
            private boolean isHovered = false;
            
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Darker shade when hovered
                if (isHovered) {
                    g2.setColor(new Color(255, 102, 102)); // Darker red
                } else {
                    g2.setColor(new Color(255, 127, 127)); // Coral red
                }
                
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), BUTTON_RADIUS, BUTTON_RADIUS);
                super.paintComponent(g);
            }
            
            {
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        isHovered = true;
                        repaint();
                    }
                    
                    @Override
                    public void mouseExited(MouseEvent e) {
                        isHovered = false;
                        repaint();
                    }
                });
            }
        };
        backButton.setBorder(null);
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setBounds(205, 340, 145, 45);
        backButton.setContentAreaFilled(false);
        
        // Add components
        mainPanel.add(titleLabel);
        mainPanel.add(usernameField);
        mainPanel.add(passwordField);
        mainPanel.add(confirmField);
        mainPanel.add(showPassword1);
        mainPanel.add(showPassword2);
        mainPanel.add(userRadio);
        mainPanel.add(adminRadio);
        mainPanel.add(continueButton);
        mainPanel.add(backButton);
        
        // Set placeholder text with black color
        usernameField.setText("USERNAME");
        usernameField.setForeground(Color.GRAY);
        passwordField.setText("PASSWORD");
        passwordField.setForeground(Color.GRAY);
        confirmField.setText("CONFIRM");
        confirmField.setForeground(Color.GRAY);
        
        // Add main panel to frame
        setContentPane(mainPanel);
        pack();
        setLocationRelativeTo(null);
    }
    
    private void setupListeners() {
        // Username field focus listeners
        usernameField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent evt) {
                if (usernameField.getText().equals("USERNAME")) {
                    usernameField.setText("");
                    usernameField.setForeground(Color.BLACK);
                }
            }
            
            @Override
            public void focusLost(FocusEvent evt) {
                if (usernameField.getText().isEmpty()) {
                    usernameField.setText("USERNAME");
                    usernameField.setForeground(Color.GRAY);
                }
            }
        });
        
        // Password field focus listeners
        passwordField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent evt) {
                if (passwordField.getText().equals("PASSWORD")) {
                    passwordField.setText("");
                    passwordField.setForeground(Color.BLACK);
                }
            }
            
            @Override
            public void focusLost(FocusEvent evt) {
                if (passwordField.getText().isEmpty()) {
                    passwordField.setText("PASSWORD");
                    passwordField.setForeground(Color.GRAY);
                    actualPassword = "";
                } else if (!passwordField.getText().equals("PASSWORD")) {
                    actualPassword = passwordField.getText();
                    if (!showPassword1.isSelected()) {
                        // Show bullets
                        StringBuilder hidden = new StringBuilder();
                        for (int i = 0; i < actualPassword.length(); i++) {
                            hidden.append("•");
                        }
                        passwordField.setText(hidden.toString());
                    }
                }
            }
        });
        
        // Confirm password field focus listeners
        confirmField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent evt) {
                if (confirmField.getText().equals("CONFIRM")) {
                    confirmField.setText("");
                    confirmField.setForeground(Color.BLACK);
                }
            }
            
            @Override
            public void focusLost(FocusEvent evt) {
                if (confirmField.getText().isEmpty()) {
                    confirmField.setText("CONFIRM");
                    confirmField.setForeground(Color.GRAY);
                    actualConfirmPassword = "";
                } else if (!confirmField.getText().equals("CONFIRM")) {
                    actualConfirmPassword = confirmField.getText();
                    if (!showPassword2.isSelected()) {
                        // Show bullets
                        StringBuilder hidden = new StringBuilder();
                        for (int i = 0; i < actualConfirmPassword.length(); i++) {
                            hidden.append("•");
                        }
                        confirmField.setText(hidden.toString());
                    }
                }
            }
        });
        
        // Show password checkboxes
        showPassword1.addActionListener(e -> {
            if (!passwordField.getText().equals("PASSWORD")) {
                if (showPassword1.isSelected()) {
                    // Show actual password
                    passwordField.setText(actualPassword);
                } else {
                    // Show bullets
                    StringBuilder hidden = new StringBuilder();
                    for (int i = 0; i < actualPassword.length(); i++) {
                        hidden.append("•");
                    }
                    passwordField.setText(hidden.toString());
                }
            }
        });
        
        showPassword2.addActionListener(e -> {
            if (!confirmField.getText().equals("CONFIRM")) {
                if (showPassword2.isSelected()) {
                    // Show actual password
                    confirmField.setText(actualConfirmPassword);
                } else {
                    // Show bullets
                    StringBuilder hidden = new StringBuilder();
                    for (int i = 0; i < actualConfirmPassword.length(); i++) {
                        hidden.append("•");
                    }
                    confirmField.setText(hidden.toString());
                }
            }
        });

        // Continue button action listener
        continueButton.addActionListener(e -> {
            if (validateForm()) {
                String username = usernameField.getText();
                String password = actualPassword; // Use actual password instead of field text
                String userType = userRadio.isSelected() ? "USER" : "ADMIN";
                
                // Create a new user object
                User newUser = new User(username, password, userType);
                
                // Create user in database
                UserRegisterDAO userDAO = new UserRegisterDAO();
                
                // Check if username already exists
                if (userDAO.userExists(username)) {
                    JOptionPane.showMessageDialog(this,
                        "Username already exists. Please choose a different username.",
                        "Registration Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (userDAO.createUser(newUser)) {
                    JOptionPane.showMessageDialog(this, 
                        "Registration successful!\nPlease login with your credentials.",
                        "Success", 
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    // Open login page
                    LoginFinal loginPage = new LoginFinal();
                    loginPage.setLocationRelativeTo(null);
                    loginPage.setVisible(true);
                    
                    // Close register page
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Registration failed. Please try again.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Back button action listener
        backButton.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to go back?\nAll entered data will be lost.",
                "Confirm",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
            
            if (choice == JOptionPane.YES_OPTION) {
                dispose();
            }
        });
    }
    
    private boolean validateForm() {
        // Check username
        String username = usernameField.getText();
        if (username.equals("USERNAME") || username.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter a username",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            usernameField.requestFocus();
            return false;
        }

        // Check password
        if (actualPassword.isEmpty() || actualPassword.equals("PASSWORD")) {
            JOptionPane.showMessageDialog(this,
                "Please enter a password",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            passwordField.requestFocus();
            return false;
        }

        // Check confirm password
        if (actualConfirmPassword.isEmpty() || actualConfirmPassword.equals("CONFIRM")) {
            JOptionPane.showMessageDialog(this,
                "Please confirm your password",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            confirmField.requestFocus();
            return false;
        }

        if (!actualPassword.equals(actualConfirmPassword)) {
            JOptionPane.showMessageDialog(this,
                "Passwords do not match",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            confirmField.requestFocus();
            return false;
        }

        return true;
    }
    
    private void setupWindowDragging() {
        mainPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                mouseDownCompCoords = null;
                setCursor(Cursor.getDefaultCursor());
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                mouseDownCompCoords = e.getPoint();
                setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
            }
        });
        
        mainPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (mouseDownCompCoords != null) {
                    Point currCoords = e.getLocationOnScreen();
                    setLocation(currCoords.x - mouseDownCompCoords.x, 
                              currCoords.y - mouseDownCompCoords.y);
                }
            }
            
            @Override
            public void mouseMoved(MouseEvent e) {
                if (e.getY() < 50) { // Only allow dragging from the top area
                    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                } else {
                    setCursor(Cursor.getDefaultCursor());
                }
            }
        });
    }
}