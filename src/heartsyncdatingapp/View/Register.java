package heartsyncdatingapp.View;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.RoundRectangle2D;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import heartsyncdatingapp.AdminPage;
import heartsyncdatingapp.dao.UserRegisterDAO;
import heartsyncdatingapp.model.LoginFinal;
import heartsyncdatingapp.model.User;

public class Register extends JFrame {
    private static final int WINDOW_RADIUS = 35;
    private static final int FIELD_RADIUS = 35;
    private static final int BUTTON_RADIUS = 35;
    private JPanel mainPanel;
    private JTextArea usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmField;
    private JButton continueButton;
    private JButton backButton;
    private JRadioButton userRadio;
    private JRadioButton adminRadio;
    private JCheckBox showPassword1;
    private JCheckBox showPassword2;
    private Point mouseDownCompCoords;
    
    // Password validation indicators
    private JLabel upperCaseLabel;
    private JLabel numberLabel;
    private JLabel specialCharLabel;
    private JLabel matchLabel;
    private ImageIcon tickIcon;
    private ImageIcon crossIcon;
    
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
        
        // Load icons for validation
        tickIcon = new ImageIcon(getClass().getResource("/ImagePicker/tick.png")) {
            @Override
            public int getIconWidth() {
                return 15;
            }
            
            @Override
            public int getIconHeight() {
                return 15;
            }
            
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 180, 0)); // Green color
                g2.setStroke(new BasicStroke(2));
                // Draw checkmark
                g2.drawLine(x + 2, y + 7, x + 6, y + 11);
                g2.drawLine(x + 6, y + 11, x + 13, y + 4);
            }
        };
        
        crossIcon = new ImageIcon(getClass().getResource("/ImagePicker/cross.png")) {
            @Override
            public int getIconWidth() {
                return 15;
            }
            
            @Override
            public int getIconHeight() {
                return 15;
            }
            
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(220, 0, 0)); // Red color
                g2.setStroke(new BasicStroke(2));
                // Draw X
                g2.drawLine(x + 3, y + 3, x + 12, y + 12);
                g2.drawLine(x + 12, y + 3, x + 3, y + 12);
            }
        };
        
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
        mainPanel.setPreferredSize(new Dimension(400, 480)); // Adjusted height for new layout
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
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", 0, 14));
        passwordField.setBackground(Color.WHITE);
        passwordField.setForeground(Color.BLACK);
        passwordField.setEchoChar('•');
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)));
        passwordField.setBounds(50, 160, 300, 45);
        
        // Show password checkboxes with updated text
        showPassword1 = new JCheckBox("Show Password");
        showPassword2 = new JCheckBox("Show Password");
        showPassword1.setBounds(360, 170, 120, 20);
        showPassword2.setBounds(360, 230, 120, 20);
        showPassword1.setOpaque(false);
        showPassword2.setOpaque(false);
        showPassword1.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        showPassword2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        // Confirm password field
        confirmField = new JPasswordField();
        confirmField.setFont(new Font("Segoe UI", 0, 14));
        confirmField.setBackground(Color.WHITE);
        confirmField.setForeground(Color.BLACK);
        confirmField.setEchoChar('•');
        confirmField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)));
        confirmField.setBounds(50, 220, 300, 45);
        
        // Radio buttons
        userRadio = new JRadioButton("USER");
        adminRadio = new JRadioButton("ADMIN");
        ButtonGroup group = new ButtonGroup();
        group.add(userRadio);
        group.add(adminRadio);
        userRadio.setSelected(true);
        
        userRadio.setBounds(50, 370, 80, 25);
        adminRadio.setBounds(200, 370, 80, 25);
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
                    
                    if (accessCode != null && accessCode.equals("3023")) {
                        // Open admin page
                        AdminPage adminPage = new AdminPage();
                        adminPage.setLocationRelativeTo(null);
                        adminPage.setVisible(true);
                        dispose(); // Close the registration page
                    } else {
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
        continueButton.setBounds(50, 410, 145, 45);
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
        backButton.setBounds(205, 410, 145, 45);
        backButton.setContentAreaFilled(false);
        
        // Password validation labels
        upperCaseLabel = new JLabel("Uppercase Letter", crossIcon, JLabel.LEFT);
        upperCaseLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        upperCaseLabel.setForeground(new Color(100, 100, 100));
        upperCaseLabel.setBounds(50, 280, 200, 20);
        
        numberLabel = new JLabel("Number", crossIcon, JLabel.LEFT);
        numberLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        numberLabel.setForeground(new Color(100, 100, 100));
        numberLabel.setBounds(50, 305, 200, 20);
        
        specialCharLabel = new JLabel("Special Character", crossIcon, JLabel.LEFT);
        specialCharLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        specialCharLabel.setForeground(new Color(100, 100, 100));
        specialCharLabel.setBounds(50, 330, 200, 20);
        
        matchLabel = new JLabel("Passwords Match", crossIcon, JLabel.LEFT);
        matchLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        matchLabel.setForeground(new Color(100, 100, 100));
        matchLabel.setBounds(50, 355, 200, 20);

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
        mainPanel.add(upperCaseLabel);
        mainPanel.add(numberLabel);
        mainPanel.add(specialCharLabel);
        mainPanel.add(matchLabel);
        
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
                if (new String(((JPasswordField)passwordField).getPassword()).equals("PASSWORD")) {
                    passwordField.setText("");
                    passwordField.setForeground(Color.BLACK);
                }
            }
            
            @Override
            public void focusLost(FocusEvent evt) {
                String pwd = new String(((JPasswordField)passwordField).getPassword());
                if (pwd.isEmpty()) {
                    actualPassword = "";
                    passwordField.setText("PASSWORD");
                    passwordField.setForeground(Color.GRAY);
                    validatePassword();
                }
            }
        });

        // Confirm password field focus listeners
        confirmField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent evt) {
                if (new String(((JPasswordField)confirmField).getPassword()).equals("CONFIRM")) {
                    confirmField.setText("");
                    confirmField.setForeground(Color.BLACK);
                }
            }
            
            @Override
            public void focusLost(FocusEvent evt) {
                String pwd = new String(((JPasswordField)confirmField).getPassword());
                if (pwd.isEmpty()) {
                    actualConfirmPassword = "";
                    confirmField.setText("CONFIRM");
                    confirmField.setForeground(Color.GRAY);
                    validatePassword();
                }
            }
        });

        // Document listeners for password fields
        passwordField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { handlePasswordChange(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { handlePasswordChange(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { handlePasswordChange(); }
        });

        confirmField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { handleConfirmPasswordChange(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { handleConfirmPasswordChange(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { handleConfirmPasswordChange(); }
        });

        // Show password checkboxes
        showPassword1.addActionListener(e -> {
            if (showPassword1.isSelected()) {
                ((JPasswordField)passwordField).setEchoChar((char)0); // Show characters
            } else {
                ((JPasswordField)passwordField).setEchoChar('•'); // Hide with bullets
            }
        });
        
        showPassword2.addActionListener(e -> {
            if (showPassword2.isSelected()) {
                ((JPasswordField)confirmField).setEchoChar((char)0); // Show characters
            } else {
                ((JPasswordField)confirmField).setEchoChar('•'); // Hide with bullets
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

    private void handlePasswordChange() {
        String pwd = new String(((JPasswordField)passwordField).getPassword());
        if (!pwd.equals("PASSWORD")) {
            actualPassword = pwd;
            validatePassword();
        }
    }

    private void handleConfirmPasswordChange() {
        String pwd = new String(((JPasswordField)confirmField).getPassword());
        if (!pwd.equals("CONFIRM")) {
            actualConfirmPassword = pwd;
            validatePassword();
        }
    }

    private void validatePassword() {
        String password = actualPassword;
        String confirmPassword = actualConfirmPassword;
        
        // Check for uppercase
        boolean hasUpperCase = password.matches(".*[A-Z].*");
        upperCaseLabel.setIcon(hasUpperCase ? tickIcon : crossIcon);
        upperCaseLabel.setForeground(hasUpperCase ? new Color(0, 150, 0) : new Color(200, 0, 0));
        
        // Check for number
        boolean hasNumber = password.matches(".*\\d.*");
        numberLabel.setIcon(hasNumber ? tickIcon : crossIcon);
        numberLabel.setForeground(hasNumber ? new Color(0, 150, 0) : new Color(200, 0, 0));
        
        // Check for special character
        boolean hasSpecial = password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*");
        specialCharLabel.setIcon(hasSpecial ? tickIcon : crossIcon);
        specialCharLabel.setForeground(hasSpecial ? new Color(0, 150, 0) : new Color(200, 0, 0));
        
        // Check if passwords match
        boolean passwordsMatch = !password.isEmpty() && password.equals(confirmPassword);
        matchLabel.setIcon(passwordsMatch ? tickIcon : crossIcon);
        matchLabel.setForeground(passwordsMatch ? new Color(0, 150, 0) : new Color(200, 0, 0));
        
        // Enable/disable continue button based on all conditions being met
        boolean allValid = hasUpperCase && hasNumber && hasSpecial && passwordsMatch;
        continueButton.setEnabled(allValid);
        continueButton.setBackground(allValid ? new Color(204, 255, 204) : new Color(240, 240, 240));

        // Debug output
        System.out.println("Password: " + password);
        System.out.println("Has Uppercase: " + hasUpperCase);
        System.out.println("Has Number: " + hasNumber);
        System.out.println("Has Special: " + hasSpecial);
        System.out.println("Passwords Match: " + passwordsMatch);
    }
}