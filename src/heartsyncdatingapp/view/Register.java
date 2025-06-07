package heartsyncdatingapp.view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import heartsyncdatingapp.HomePage;
import heartsyncdatingapp.controller.UserProfileController;
import heartsyncdatingapp.dao.UserRegisterDAO;
import heartsyncdatingapp.model.User;
import heartsyncdatingapp.model.UserProfile;

public class Register extends JFrame {
    private JPanel mainPanel;
    private JTextArea usernameField;
    private JTextArea passwordField;
    private JTextArea confirmField;
    private JRadioButton userRadio;
    private JRadioButton adminRadio;
    private JButton continueButton;
    private JButton backButton;
    private JLabel validationLabel;
    private JCheckBox showPassword1;
    private JCheckBox showPassword2;
    private static final int WINDOW_RADIUS = 20;
    private static final int FIELD_WIDTH = 350;  // Increased field width
    private HomePage homePage;
    private String actualPassword = "";
    private String actualConfirmPassword = "";
    private boolean isPasswordVisible = false;
    private boolean isConfirmPasswordVisible = false;
    private boolean isUpdatingPassword = false;
    private boolean isUpdatingConfirm = false;

    public Register() {
        try {
            // Set system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Set look and feel properties first
        UIManager.put("TextField.background", Color.WHITE);
        UIManager.put("PasswordField.background", Color.WHITE);
        UIManager.put("TextField.opaque", true);
        UIManager.put("PasswordField.opaque", true);
        UIManager.put("Panel.background", new Color(255, 219, 227));
        UIManager.put("Panel.opaque", true);
        
        initComponents();
        setupListeners();
        setupTextFields();
        pack();
        setLocationRelativeTo(null);
        
        // Store reference to HomePage
        for (Window window : Window.getWindows()) {
            if (window instanceof HomePage) {
                homePage = (HomePage) window;
                break;
            }
        }
        
        // Final styling enforcement
        SwingUtilities.invokeLater(() -> {
            mainPanel.setBackground(new Color(255, 219, 227));
            mainPanel.setOpaque(true);
            usernameField.setBackground(Color.WHITE);
            usernameField.setOpaque(true);
            passwordField.setBackground(Color.WHITE);
            passwordField.setOpaque(true);
            confirmField.setBackground(Color.WHITE);
            confirmField.setOpaque(true);
            
            // Force a repaint
            mainPanel.revalidate();
            mainPanel.repaint();
        });
    }

    private void setupTextFields() {
        // Username setup
        usernameField.setText("USERNAME");
        usernameField.setForeground(Color.GRAY);
        usernameField.setBackground(Color.WHITE);
        usernameField.setOpaque(true);

        // Password setup
        passwordField.setText("PASSWORD");
        passwordField.setForeground(Color.GRAY);
        passwordField.setBackground(Color.WHITE);
        passwordField.setOpaque(true);
        passwordField.setWrapStyleWord(true);
        passwordField.setLineWrap(true);

        // Confirm password setup
        confirmField.setText("CONFIRM");
        confirmField.setForeground(Color.GRAY);
        confirmField.setBackground(Color.WHITE);
        confirmField.setOpaque(true);
        confirmField.setWrapStyleWord(true);
        confirmField.setLineWrap(true);
    }

    private void initComponents() {
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(255, 219, 227));
        
        // Main panel with pink background and rounded corners
        mainPanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 219, 227));  // Pink color from homepage
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), WINDOW_RADIUS, WINDOW_RADIUS);
                super.paintComponent(g);
            }
        };
        mainPanel.setPreferredSize(new Dimension(450, 480));  // Set fixed size
        mainPanel.setSize(new Dimension(450, 480));  // Add this line
        mainPanel.setBackground(new Color(255, 219, 227));
        mainPanel.setOpaque(true);
        setContentPane(mainPanel);  // Use setContentPane instead of add
        
        // Title
        JLabel titleLabel = new JLabel("CREATE ACCOUNT");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBounds(0, 40, 450, 40);  // Adjusted width
        
        // Username field styled like login page
        usernameField = new JTextArea();
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameField.setBackground(Color.WHITE);
        usernameField.setForeground(Color.GRAY);
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200)),
            new EmptyBorder(10, 15, 10, 15)));
        usernameField.setOpaque(true);
        usernameField.setText("USERNAME");
        usernameField.setLineWrap(true);
        usernameField.setWrapStyleWord(true);
        usernameField.setRows(1);
        usernameField.setBounds(50, 100, FIELD_WIDTH, 45);
        
        // Password field styled like login page
        passwordField = new JTextArea();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setBackground(Color.WHITE);
        passwordField.setForeground(Color.GRAY);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200)),
            new EmptyBorder(10, 15, 10, 15)));
        passwordField.setOpaque(true);
        passwordField.setText("PASSWORD");
        passwordField.setLineWrap(true);
        passwordField.setWrapStyleWord(true);
        passwordField.setRows(1);
        passwordField.setBounds(50, 160, FIELD_WIDTH, 45);
        
        // Confirm password field styled like login page
        confirmField = new JTextArea();
        confirmField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        confirmField.setBackground(Color.WHITE);
        confirmField.setForeground(Color.GRAY);
        confirmField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200)),
            new EmptyBorder(10, 15, 10, 15)));
        confirmField.setOpaque(true);
        confirmField.setText("CONFIRM");
        confirmField.setLineWrap(true);
        confirmField.setWrapStyleWord(true);
        confirmField.setRows(1);
        confirmField.setBounds(50, 220, FIELD_WIDTH, 45);
        
        // Show password checkboxes with improved styling
        showPassword1 = new JCheckBox("Show");
        showPassword2 = new JCheckBox("Show");
        showPassword1.setBounds(410, 170, 60, 20);  // Adjusted position and width
        showPassword2.setBounds(410, 230, 60, 20);  // Adjusted position and width
        showPassword1.setOpaque(false);
        showPassword2.setOpaque(false);
        showPassword1.setForeground(new Color(108, 117, 125));
        showPassword2.setForeground(new Color(108, 117, 125));
        showPassword1.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        showPassword2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        showPassword1.setCursor(new Cursor(Cursor.HAND_CURSOR));
        showPassword2.setCursor(new Cursor(Cursor.HAND_CURSOR));
        showPassword1.setFocusPainted(false);
        showPassword2.setFocusPainted(false);
        
        // Add validation panel with background
        JPanel validationPanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 200)); // Semi-transparent white
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            }
        };
        validationPanel.setBounds(50, 270, FIELD_WIDTH, 50);
        validationPanel.setOpaque(false);
        mainPanel.add(validationPanel);

        // Add validation status label
        validationLabel = new JLabel("Password requirements:", SwingConstants.LEFT);
        validationLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        validationLabel.setBounds(10, 5, 280, 15);
        validationLabel.setForeground(Color.GRAY);
        validationPanel.add(validationLabel);

        // Add requirements label with initial X marks
        JLabel requirementsLabel = new JLabel();
        requirementsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        requirementsLabel.setBounds(10, 25, 280, 20);
        requirementsLabel.setForeground(Color.GRAY);
        requirementsLabel.setText("<html><font color='red'>✗</font> Uppercase " +
                                "<font color='red'>✗</font> Special " +
                                "<font color='red'>✗</font> Number " +
                                "<font color='red'>✗</font> Match</html>");
        validationPanel.add(requirementsLabel);
        
        // Radio buttons with improved styling
        userRadio = new JRadioButton("USER");
        adminRadio = new JRadioButton("ADMIN");
        ButtonGroup group = new ButtonGroup();
        group.add(userRadio);
        group.add(adminRadio);
        
        // Style radio buttons
        userRadio.setFont(new Font("Segoe UI", Font.BOLD, 12));
        adminRadio.setFont(new Font("Segoe UI", Font.BOLD, 12));
        userRadio.setForeground(Color.BLACK);
        adminRadio.setForeground(Color.BLACK);
        userRadio.setOpaque(false);
        adminRadio.setOpaque(false);
        userRadio.setBounds(50, 330, 80, 30);
        adminRadio.setBounds(140, 330, 80, 30);
        userRadio.setSelected(true);
        
        // Continue button with orange background like homepage
        continueButton = new JButton("Continue") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2.setColor(new Color(200, 70, 20));
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(240, 100, 50));
                } else {
                    g2.setColor(new Color(229, 89, 36));
                }
                
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                
                String text = getText();
                FontMetrics fm = g2.getFontMetrics();
                int width = fm.stringWidth(text);
                int height = fm.getHeight();
                int x = (getWidth() - width) / 2;
                int y = (getHeight() - height) / 2 + fm.getAscent();
                
                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                g2.drawString(text, x, y);
                g2.dispose();
            }
        };
        continueButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        continueButton.setForeground(Color.WHITE);
        continueButton.setBounds(50, 370, 170, 45);  // Adjusted width
        continueButton.setBorderPainted(false);
        continueButton.setContentAreaFilled(false);
        continueButton.setFocusPainted(false);
        continueButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Back button with gray background
        backButton = new JButton("Back") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2.setColor(new Color(80, 90, 100));
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(120, 130, 140));
                } else {
                    g2.setColor(new Color(108, 117, 125));
                }
                
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                
                String text = getText();
                FontMetrics fm = g2.getFontMetrics();
                int width = fm.stringWidth(text);
                int height = fm.getHeight();
                int x = (getWidth() - width) / 2;
                int y = (getHeight() - height) / 2 + fm.getAscent();
                
                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                g2.drawString(text, x, y);
                g2.dispose();
            }
        };
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backButton.setForeground(Color.WHITE);
        backButton.setBounds(230, 370, 170, 45);  // Adjusted width and position
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
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
        
        // Add main panel to frame
        setContentPane(mainPanel);
        pack();
        setLocationRelativeTo(null);
    }
    
    private void updatePasswordVisibility(JTextArea field, String placeholder, boolean isVisible, String actualValue) {
        if (field.getText().equals(placeholder)) {
            return;
        }
        
        if (isVisible) {
            field.setText(actualValue);
        } else {
            StringBuilder hidden = new StringBuilder();
            for (int i = 0; i < actualValue.length(); i++) {
                hidden.append("•");
            }
            field.setText(hidden.toString());
        }
    }

    private void setupListeners() {
        // Username field focus listeners
        usernameField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (usernameField.getText().equals("USERNAME")) {
                    usernameField.setText("");
                    usernameField.setForeground(Color.BLACK);
                }
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                if (usernameField.getText().isEmpty()) {
                    usernameField.setForeground(Color.GRAY);
                    usernameField.setText("USERNAME");
                }
            }
        });

        // Password field listeners
        passwordField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (!isUpdatingPassword && !passwordField.getText().equals("PASSWORD")) {
                    SwingUtilities.invokeLater(() -> {
                        String text = passwordField.getText();
                        if (text != null && !text.isEmpty()) {
                            if (isPasswordVisible) {
                                actualPassword = text;
                            } else {
                                if (text.length() > actualPassword.length()) {
                                    actualPassword += text.substring(actualPassword.length());
                                } else {
                                    actualPassword = actualPassword.substring(0, text.length());
                                }
                            }
                            
                            if (!isPasswordVisible) {
                                isUpdatingPassword = true;
                                passwordField.setText("•".repeat(actualPassword.length()));
                                isUpdatingPassword = false;
                            }
                        }
                        validatePassword();
                    });
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (!isUpdatingPassword && !passwordField.getText().equals("PASSWORD")) {
                    SwingUtilities.invokeLater(() -> {
                        String text = passwordField.getText();
                        if (text != null && actualPassword.length() > text.length()) {
                            actualPassword = actualPassword.substring(0, text.length());
                        }
                        validatePassword();
                    });
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {}
        });

        // Confirm password field listeners
        confirmField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (!isUpdatingConfirm && !confirmField.getText().equals("CONFIRM")) {
                    SwingUtilities.invokeLater(() -> {
                        String text = confirmField.getText();
                        if (text != null && !text.isEmpty()) {
                            if (isConfirmPasswordVisible) {
                                actualConfirmPassword = text;
                            } else {
                                if (text.length() > actualConfirmPassword.length()) {
                                    actualConfirmPassword += text.substring(actualConfirmPassword.length());
                                } else {
                                    actualConfirmPassword = actualConfirmPassword.substring(0, text.length());
                                }
                            }
                            
                            if (!isConfirmPasswordVisible) {
                                isUpdatingConfirm = true;
                                confirmField.setText("•".repeat(actualConfirmPassword.length()));
                                isUpdatingConfirm = false;
                            }
                        }
                        validatePassword();
                    });
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (!isUpdatingConfirm && !confirmField.getText().equals("CONFIRM")) {
                    SwingUtilities.invokeLater(() -> {
                        String text = confirmField.getText();
                        if (text != null && actualConfirmPassword.length() > text.length()) {
                            actualConfirmPassword = actualConfirmPassword.substring(0, text.length());
                        }
                        validatePassword();
                    });
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {}
        });

        // Password field focus listeners
        passwordField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (passwordField.getText().equals("PASSWORD")) {
                    isUpdatingPassword = true;
                    passwordField.setText("");
                    isUpdatingPassword = false;
                    passwordField.setForeground(Color.BLACK);
                }
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                if (passwordField.getText().isEmpty()) {
                    isUpdatingPassword = true;
                    passwordField.setForeground(Color.GRAY);
                    passwordField.setText("PASSWORD");
                    actualPassword = "";
                    isUpdatingPassword = false;
                }
            }
        });

        // Confirm password field focus listeners
        confirmField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (confirmField.getText().equals("CONFIRM")) {
                    isUpdatingConfirm = true;
                    confirmField.setText("");
                    isUpdatingConfirm = false;
                    confirmField.setForeground(Color.BLACK);
                }
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                if (confirmField.getText().isEmpty()) {
                    isUpdatingConfirm = true;
                    confirmField.setForeground(Color.GRAY);
                    confirmField.setText("CONFIRM");
                    actualConfirmPassword = "";
                    isUpdatingConfirm = false;
                }
            }
        });

        // Show password checkboxes
        showPassword1.addActionListener(e -> {
            if (!passwordField.getText().equals("PASSWORD")) {
                isPasswordVisible = showPassword1.isSelected();
                isUpdatingPassword = true;
                passwordField.setText(isPasswordVisible ? actualPassword : "•".repeat(actualPassword.length()));
                isUpdatingPassword = false;
            }
        });
        
        showPassword2.addActionListener(e -> {
            if (!confirmField.getText().equals("CONFIRM")) {
                isConfirmPasswordVisible = showPassword2.isSelected();
                isUpdatingConfirm = true;
                confirmField.setText(isConfirmPasswordVisible ? actualConfirmPassword : "•".repeat(actualConfirmPassword.length()));
                isUpdatingConfirm = false;
            }
        });

        // Continue button action
        continueButton.addActionListener(e -> {
            if (!validateForm()) {
                return;
            }

            String username = usernameField.getText();
            String password = actualPassword;
            String role = userRadio.isSelected() ? "USER" : "ADMIN";
            
            // Create user and save
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setUserType(role);
            
            try {
                UserRegisterDAO dao = new UserRegisterDAO();
                if (dao.createUser(user)) {
                    JOptionPane.showMessageDialog(this,
                        "Account created successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    if (role.equals("USER")) {
                        // Create MVC components for profile setup only for regular users
                        UserProfile model = new UserProfile();
                        UserProfileController controller = new UserProfileController(model, username);
                        ProfileSetupView view = new ProfileSetupView(controller);
                        
                        // Close registration window and show profile setup
                        dispose();
                        view.setVisible(true);
                    } else {
                        // For admin users, just close the registration window
                        dispose();
                        // You might want to show admin dashboard here in the future
                    }
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Error creating account. Please try again.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this,
                    "Error creating account: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        // Back button action
        backButton.addActionListener(e -> {
            if (homePage != null) {
                homePage.setVisible(true);
            }
            dispose();
        });
    }

    private void validatePassword() {
        // Skip validation if fields contain placeholder text
        if (passwordField.getText().equals("PASSWORD") || confirmField.getText().equals("CONFIRM")) {
            return;
        }

        // Check each requirement using actualPassword instead of the field text
        boolean hasUppercase = actualPassword.matches(".*[A-Z].*");
        boolean hasSpecial = actualPassword.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*");
        boolean hasNumber = actualPassword.matches(".*[0-9].*");
        boolean passwordsMatch = actualPassword.equals(actualConfirmPassword);

        // Create status icons
        String checkMark = "✓";
        String xMark = "✗";
        
        // Build requirements status
        StringBuilder reqStatus = new StringBuilder("<html>");
        reqStatus.append(hasUppercase ? "<font color='green'>" + checkMark + "</font>" : "<font color='red'>" + xMark + "</font>");
        reqStatus.append(" Uppercase ");
        
        reqStatus.append(hasSpecial ? "<font color='green'>" + checkMark + "</font>" : "<font color='red'>" + xMark + "</font>");
        reqStatus.append(" Special ");
        
        reqStatus.append(hasNumber ? "<font color='green'>" + checkMark + "</font>" : "<font color='red'>" + xMark + "</font>");
        reqStatus.append(" Number ");
        
        reqStatus.append(passwordsMatch ? "<font color='green'>" + checkMark + "</font>" : "<font color='red'>" + xMark + "</font>");
        reqStatus.append(" Match");
        reqStatus.append("</html>");

        // Update labels
        JLabel requirementsLabel = (JLabel) ((JPanel) validationLabel.getParent()).getComponent(1);
        requirementsLabel.setText(reqStatus.toString());

        // Update main validation label
        boolean allValid = hasUppercase && hasSpecial && hasNumber && passwordsMatch;
        
        if (allValid) {
            validationLabel.setText("✅ Password is valid!");
            validationLabel.setForeground(new Color(0, 150, 0));
        } else {
            validationLabel.setText("Password requirements:");
            validationLabel.setForeground(Color.GRAY);
        }
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

        // Check password - use actualPassword instead of field text
        if (actualPassword.isEmpty() || passwordField.getText().equals("PASSWORD")) {
            JOptionPane.showMessageDialog(this,
                "Please enter a password",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            passwordField.requestFocus();
            return false;
        }

        // Check confirm password
        if (actualConfirmPassword.isEmpty() || confirmField.getText().equals("CONFIRM")) {
            JOptionPane.showMessageDialog(this,
                "Please confirm your password",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            confirmField.requestFocus();
            return false;
        }

        // Validate password requirements using actualPassword
        if (!actualPassword.matches(".*[A-Z].*")) {
            JOptionPane.showMessageDialog(this,
                "Password must contain at least one uppercase letter",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            passwordField.requestFocus();
            return false;
        }

        if (!actualPassword.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) {
            JOptionPane.showMessageDialog(this,
                "Password must contain at least one special character",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            passwordField.requestFocus();
            return false;
        }

        if (!actualPassword.matches(".*[0-9].*")) {
            JOptionPane.showMessageDialog(this,
                "Password must contain at least one number",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            passwordField.requestFocus();
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
    
    public static void main(String[] args) {
        try {
            // Set system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            // Override default colors
            UIManager.put("TextField.background", Color.WHITE);
            UIManager.put("PasswordField.background", Color.WHITE);
            UIManager.put("TextField.opaque", true);
            UIManager.put("PasswordField.opaque", true);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            Register register = new Register();
            register.setVisible(true);
        });
    }
}
