package heartsyncdatingapp.view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
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
    private JTextField usernameField;
    private JTextField passwordField;
    private JTextField confirmField;
    private JTextField favoriteColorField;
    private JTextField firstSchoolField;
    private JTextField dobField;
    private JRadioButton userRadio;
    private JRadioButton adminRadio;
    private JButton continueButton;
    private JButton backButton;
    private JLabel validationLabel;
    private JCheckBox showPassword1;
    private JCheckBox showPassword2;
    private static final int WINDOW_RADIUS = 20;
    private static final int FIELD_WIDTH = 350;
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
            
            // Set look and feel properties first
            UIManager.put("TextField.background", Color.WHITE);
            UIManager.put("PasswordField.background", Color.WHITE);
            UIManager.put("TextField.opaque", true);
            UIManager.put("PasswordField.opaque", true);
            UIManager.put("Panel.background", new Color(255, 219, 227));
            UIManager.put("Panel.opaque", true);
            UIManager.put("ComboBox.background", Color.WHITE);
            UIManager.put("ComboBox.foreground", new Color(33, 33, 33));
            UIManager.put("ComboBox.selectionBackground", new Color(219, 112, 147));
            UIManager.put("ComboBox.selectionForeground", Color.WHITE);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Set minimum size
        setMinimumSize(new Dimension(500, 800));
        
        // Initialize components in the correct order
        setupComboBoxes();  // Create combo boxes first
        initComponents();   // Then initialize other components
        setupTextFields();
        setupListeners();
        setupAccessibility();
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
            favoriteColorField.setBackground(Color.WHITE);
            favoriteColorField.setOpaque(true);
            firstSchoolField.setBackground(Color.WHITE);
            firstSchoolField.setOpaque(true);
            
            // Apply background color to combo boxes
            if (dobField != null) dobField.setBackground(Color.WHITE);
            
            // Force a repaint
            mainPanel.revalidate();
            mainPanel.repaint();
        });
    }

    private void setupAccessibility() {
        // Add ARIA labels
        usernameField.getAccessibleContext().setAccessibleName("Username input field");
        usernameField.getAccessibleContext().setAccessibleDescription("Enter your username");
        
        passwordField.getAccessibleContext().setAccessibleName("Password input field");
        passwordField.getAccessibleContext().setAccessibleDescription("Enter your password");
        
        confirmField.getAccessibleContext().setAccessibleName("Confirm password field");
        confirmField.getAccessibleContext().setAccessibleDescription("Confirm your password");
        
        favoriteColorField.getAccessibleContext().setAccessibleName("Favorite color input");
        favoriteColorField.getAccessibleContext().setAccessibleDescription("Enter your favorite color");
        
        firstSchoolField.getAccessibleContext().setAccessibleName("First school input");
        firstSchoolField.getAccessibleContext().setAccessibleDescription("Enter your first school name");
        
        continueButton.getAccessibleContext().setAccessibleName("Continue button");
        continueButton.getAccessibleContext().setAccessibleDescription("Click to create your account");
        
        backButton.getAccessibleContext().setAccessibleName("Back button");
        backButton.getAccessibleContext().setAccessibleDescription("Click to go back to previous page");
    }

    private void setupTextFields() {
        // Username setup with placeholder
        usernameField.setText("USERNAME");
        usernameField.setForeground(Color.GRAY);
        usernameField.setBackground(Color.WHITE);
        usernameField.setOpaque(true);
        usernameField.setDocument(new LengthRestrictedDocument(50)); // Limit to 50 chars

        // Password setup
        passwordField.setText("Enter password");
        passwordField.setForeground(Color.GRAY);
        passwordField.setBackground(Color.WHITE);
        passwordField.setOpaque(true);

        // Confirm password setup
        confirmField.setText("Confirm password");
        confirmField.setForeground(Color.GRAY);
        confirmField.setBackground(Color.WHITE);
        confirmField.setOpaque(true);

        // Security questions setup
        favoriteColorField.setText("FAVORITE COLOR");
        favoriteColorField.setForeground(Color.GRAY);
        favoriteColorField.setBackground(Color.WHITE);
        favoriteColorField.setOpaque(true);
        favoriteColorField.setDocument(new LengthRestrictedDocument(50));

        firstSchoolField.setText("FIRST SCHOOL");
        firstSchoolField.setForeground(Color.GRAY);
        firstSchoolField.setBackground(Color.WHITE);
        firstSchoolField.setOpaque(true);
        firstSchoolField.setDocument(new LengthRestrictedDocument(50));
    }

    // Add this inner class for character limit
    private class LengthRestrictedDocument extends javax.swing.text.PlainDocument {
        private final int limit;

        LengthRestrictedDocument(int limit) {
            this.limit = limit;
        }

        @Override
        public void insertString(int offs, String str, javax.swing.text.AttributeSet a)
                throws javax.swing.text.BadLocationException {
            if (str == null)
                return;

            if ((getLength() + str.length()) <= limit) {
                super.insertString(offs, str, a);
            }
        }
    }

    private void initComponents() {
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(255, 219, 227));
        
        // Initialize main panel with rounded corners
        mainPanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 219, 227));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), WINDOW_RADIUS, WINDOW_RADIUS);
            }
        };
        mainPanel.setPreferredSize(new Dimension(500, 800));
        mainPanel.setBackground(new Color(255, 219, 227));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setContentPane(mainPanel);

        // Title with modern font and color
        JLabel titleLabel = new JLabel("Create Account", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(new Color(112, 0, 61));
        titleLabel.setBounds(0, 30, 500, 50);
        mainPanel.add(titleLabel);

        // Subtitle
        JLabel subtitleLabel = new JLabel("Join HeartSync Dating Today", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(128, 128, 128));
        subtitleLabel.setBounds(0, 80, 500, 30);
        mainPanel.add(subtitleLabel);

        // Initialize fields with modern styling
        usernameField = createStyledTextField("USERNAME", 120);
        passwordField = createStyledTextField("Enter password", 190);
        confirmField = createStyledTextField("Confirm password", 260);

        // Show password checkboxes with modern styling
        showPassword1 = createStyledCheckbox("Show", 200);
        showPassword2 = createStyledCheckbox("Show", 270);

        // User type selection with modern radio buttons
        JPanel userTypePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        userTypePanel.setOpaque(false);
        userTypePanel.setBounds(50, 330, 400, 40);

        userRadio = createStyledRadioButton("User", true);
        adminRadio = createStyledRadioButton("Admin", false);
        
        ButtonGroup group = new ButtonGroup();
        group.add(userRadio);
        group.add(adminRadio);
        
        userTypePanel.add(userRadio);
        userTypePanel.add(adminRadio);
        mainPanel.add(userTypePanel);

        // Date of birth section with modern styling
        JLabel dobLabel = new JLabel("Date of Birth");
        dobLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        dobLabel.setBounds(50, 380, 200, 30);
        dobLabel.setForeground(new Color(112, 0, 61));
        mainPanel.add(dobLabel);

        // Create DOB text field with placeholder
        dobField = createStyledTextField("YYYY/MM/DD", 410);

        // Age display label
        JLabel ageLabel = new JLabel("Age: ");
        ageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        ageLabel.setForeground(new Color(112, 0, 61));
        ageLabel.setBounds(50, 460, 200, 30);
        mainPanel.add(ageLabel);

        // Security questions
        favoriteColorField = createStyledTextField("FAVORITE COLOR", 500);
        firstSchoolField = createStyledTextField("FIRST SCHOOL", 570);

        // Add document listener to update age
        dobField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateAgeLabel(ageLabel);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateAgeLabel(ageLabel);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateAgeLabel(ageLabel);
            }
        });

        // Modern styled buttons
        continueButton = createStyledButton("Continue", new Color(229, 89, 36));
        continueButton.setBounds(50, 640, 190, 45);
        continueButton.addActionListener(e -> handleContinueButton());
        
        backButton = createStyledButton("Back", new Color(108, 117, 125));
        backButton.setBounds(260, 640, 190, 45);
        backButton.addActionListener(e -> {
            if (homePage != null) {
                homePage.setVisible(true);
            }
            dispose();
        });

        mainPanel.add(continueButton);
        mainPanel.add(backButton);

        // Add validation label at the bottom
        validationLabel = new JLabel();
        validationLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        validationLabel.setForeground(new Color(70, 70, 70));
        validationLabel.setBounds(50, 690, FIELD_WIDTH, 100);
        validationLabel.setVerticalAlignment(SwingConstants.TOP);
        mainPanel.add(validationLabel);

        // Setup all listeners
        setupListeners();

        pack();
        setLocationRelativeTo(null);
    }

    private JTextField createStyledTextField(String placeholder, int yPosition) {
        JTextField field = new JTextField(placeholder);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setForeground(Color.GRAY);
        field.setBackground(Color.WHITE);
        field.setOpaque(true);
        field.setBorder(new CompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            new EmptyBorder(5, 15, 5, 15)
        ));
        field.setBounds(50, yPosition, FIELD_WIDTH, 45);

        // Add focus listener for placeholder behavior
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
                field.setBorder(new CompoundBorder(
                    new LineBorder(new Color(229, 89, 36), 1, true),
                    new EmptyBorder(5, 15, 5, 15)
                ));
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(Color.GRAY);
                }
                field.setBorder(new CompoundBorder(
                    new LineBorder(new Color(200, 200, 200), 1, true),
                    new EmptyBorder(5, 15, 5, 15)
                ));
            }
        });

        mainPanel.add(field);
        return field;
    }

    private JCheckBox createStyledCheckbox(String text, int yPosition) {
        JCheckBox checkbox = new JCheckBox(text);
        checkbox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        checkbox.setForeground(new Color(108, 117, 125));
        checkbox.setOpaque(false);
        checkbox.setCursor(new Cursor(Cursor.HAND_CURSOR));
        checkbox.setFocusPainted(false);
        checkbox.setBounds(410, yPosition, 60, 20);
        mainPanel.add(checkbox);
        return checkbox;
    }

    private JRadioButton createStyledRadioButton(String text, boolean selected) {
        JRadioButton radio = new JRadioButton(text);
        radio.setFont(new Font("Segoe UI", Font.BOLD, 14));
        radio.setForeground(new Color(112, 0, 61));
        radio.setOpaque(false);
        radio.setSelected(selected);
        radio.setCursor(new Cursor(Cursor.HAND_CURSOR));
        radio.setFocusPainted(false);
        return radio;
    }

    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2.setColor(backgroundColor.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(backgroundColor.brighter());
                } else {
                    g2.setColor(backgroundColor);
                }
                
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                
                FontMetrics fm = g2.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(getText())) / 2;
                int textY = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                
                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                g2.drawString(getText(), textX, textY);
                g2.dispose();
            }
        };
        
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void setupValidationLabel() {
        validationLabel = new JLabel();
        validationLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        validationLabel.setForeground(new Color(70, 70, 70));
        validationLabel.setBounds(50, 620, FIELD_WIDTH, 100);
        validationLabel.setVerticalAlignment(SwingConstants.TOP);
        mainPanel.add(validationLabel);
        
        // Add document listeners to all fields
        DocumentListener validationListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                validateFields();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                validateFields();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                validateFields();
            }
        };

        usernameField.getDocument().addDocumentListener(validationListener);
        passwordField.getDocument().addDocumentListener(validationListener);
        confirmField.getDocument().addDocumentListener(validationListener);
        favoriteColorField.getDocument().addDocumentListener(validationListener);
        firstSchoolField.getDocument().addDocumentListener(validationListener);
        dobField.getDocument().addDocumentListener(validationListener);
    }

    private void updatePasswordVisibility(JTextField field, String placeholder, boolean isVisible, String actualValue) {
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
        // Show password checkboxes
        if (showPassword1 != null) {
            showPassword1.addActionListener(e -> {
                if (!passwordField.getText().equals("Enter password")) {
                    isPasswordVisible = showPassword1.isSelected();
                    isUpdatingPassword = true;
                    passwordField.setText(isPasswordVisible ? actualPassword : "•".repeat(actualPassword.length()));
                    isUpdatingPassword = false;
                }
            });
        }
        
        if (showPassword2 != null) {
            showPassword2.addActionListener(e -> {
                if (!confirmField.getText().equals("Confirm password")) {
                    isConfirmPasswordVisible = showPassword2.isSelected();
                    isUpdatingConfirm = true;
                    confirmField.setText(isConfirmPasswordVisible ? actualConfirmPassword : "•".repeat(actualConfirmPassword.length()));
                    isUpdatingConfirm = false;
                }
            });
        }

        // Password field listeners
        passwordField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (!isUpdatingPassword && !passwordField.getText().equals("Enter password")) {
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
                        validateFields();
                    });
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (!isUpdatingPassword && !passwordField.getText().equals("Enter password")) {
                    SwingUtilities.invokeLater(() -> {
                        String text = passwordField.getText();
                        if (text != null && actualPassword.length() > text.length()) {
                            actualPassword = actualPassword.substring(0, text.length());
                        }
                        validateFields();
                    });
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {}
        });

        // Add focus listeners
        passwordField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (passwordField.getText().equals("Enter password")) {
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
                    passwordField.setText("Enter password");
                    actualPassword = "";
                    isUpdatingPassword = false;
                }
            }
        });

        confirmField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (confirmField.getText().equals("Confirm password")) {
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
                    confirmField.setText("Confirm password");
                    actualConfirmPassword = "";
                    isUpdatingConfirm = false;
                }
            }
        });

        // Add document listener for confirm password
        confirmField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (!isUpdatingConfirm && !confirmField.getText().equals("Confirm password")) {
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
                        validateFields();
                    });
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (!isUpdatingConfirm && !confirmField.getText().equals("Confirm password")) {
                    SwingUtilities.invokeLater(() -> {
                        String text = confirmField.getText();
                        if (text != null && actualConfirmPassword.length() > text.length()) {
                            actualConfirmPassword = actualConfirmPassword.substring(0, text.length());
                        }
                        validateFields();
                    });
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {}
        });
    }

    private void updateAgeLabel(JLabel ageLabel) {
        try {
            String dateStr = dobField.getText().trim();
            String[] parts = dateStr.split("/");
            if (parts.length == 3) {
                int year = Integer.parseInt(parts[0]);
                int month = Integer.parseInt(parts[1]);
                int day = Integer.parseInt(parts[2]);
                
                LocalDate dob = LocalDate.of(year, month, day);
                int age = Period.between(dob, LocalDate.now()).getYears();
                
                ageLabel.setText("Age: " + age + " years" + (age < 18 ? " (Must be 18+ to register)" : ""));
                ageLabel.setForeground(age < 18 ? Color.RED : new Color(112, 0, 61));
                continueButton.setEnabled(age >= 18);
            } else {
                ageLabel.setText("Age: Invalid date format");
                ageLabel.setForeground(Color.RED);
                continueButton.setEnabled(false);
            }
        } catch (Exception e) {
            ageLabel.setText("Age: Invalid date");
            ageLabel.setForeground(Color.RED);
            continueButton.setEnabled(false);
        }
    }

    private void validatePassword() {
        // Skip validation if fields contain placeholder text
        if (passwordField.getText().equals("Enter password") || confirmField.getText().equals("Confirm password")) {
            return;
        }

        // Check each requirement using actualPassword instead of the field text
        boolean hasUppercase = actualPassword.matches(".*[A-Z].*");
        boolean hasSpecial = actualPassword.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*");
        boolean hasNumber = actualPassword.matches(".*[0-9].*");
        boolean isLongEnough = actualPassword.length() >= 8;
        boolean passwordsMatch = actualPassword.equals(actualConfirmPassword);

        // Create status icons
        String checkMark = "✓";
        String xMark = "✗";
        
        // Build requirements status
        StringBuilder status = new StringBuilder("<html>");
        status.append(hasUppercase ? "<font color='green'>" + checkMark + "</font>" : "<font color='red'>" + xMark + "</font>")
              .append(" Uppercase letter required<br>");
        status.append(hasSpecial ? "<font color='green'>" + checkMark + "</font>" : "<font color='red'>" + xMark + "</font>")
              .append(" Special character required<br>");
        status.append(hasNumber ? "<font color='green'>" + checkMark + "</font>" : "<font color='red'>" + xMark + "</font>")
              .append(" Number required<br>");
        status.append(isLongEnough ? "<font color='green'>" + checkMark + "</font>" : "<font color='red'>" + xMark + "</font>")
              .append(" At least 8 characters<br>");
        status.append(passwordsMatch ? "<font color='green'>" + checkMark + "</font>" : "<font color='red'>" + xMark + "</font>")
              .append(" Passwords match");
        status.append("</html>");

        validationLabel.setText(status.toString());
        boolean allValid = hasUppercase && hasSpecial && hasNumber && isLongEnough && passwordsMatch;
        
        // Only enable continue button if both password and age requirements are met
        boolean ageValid = false;
        try {
            String dateStr = dobField.getText().trim();
            String[] parts = dateStr.split("/");
            if (parts.length == 3) {
                int year = Integer.parseInt(parts[0]);
                int month = Integer.parseInt(parts[1]);
                int day = Integer.parseInt(parts[2]);
                
                LocalDate dob = LocalDate.of(year, month, day);
                int age = Period.between(dob, LocalDate.now()).getYears();
                ageValid = age >= 18;
            }
        } catch (Exception e) {
            ageValid = false;
        }
        
        continueButton.setEnabled(allValid && ageValid);
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
        if (actualPassword.isEmpty() || actualPassword.equals("Enter password")) {
            JOptionPane.showMessageDialog(this,
                "Please enter a password",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            passwordField.requestFocus();
            return false;
        }

        // Check confirm password
        if (actualConfirmPassword.isEmpty() || actualConfirmPassword.equals("Confirm password")) {
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

        // Validate Date of Birth
        try {
            String dateStr = dobField.getText().trim();
            String[] parts = dateStr.split("/");
            if (parts.length == 3) {
                int year = Integer.parseInt(parts[0]);
                int month = Integer.parseInt(parts[1]);
                int day = Integer.parseInt(parts[2]);
                
                LocalDate dob = LocalDate.of(year, month, day);
                int age = Period.between(dob, LocalDate.now()).getYears();
                
                if (age < 18) {
                    JOptionPane.showMessageDialog(this,
                        "You must be 18 or older to register",
                        "Age Restriction",
                        JOptionPane.WARNING_MESSAGE);
                    return false;
                }
            } else {
                JOptionPane.showMessageDialog(this,
                    "Please enter date in YYYY/MM/DD format",
                    "Invalid Date Format",
                    JOptionPane.WARNING_MESSAGE);
                return false;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Please enter a valid date",
                "Invalid Date",
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        // Validate security questions
        String favoriteColor = favoriteColorField.getText();
        if (favoriteColor.equals("FAVORITE COLOR") || favoriteColor.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter your favorite color",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            favoriteColorField.requestFocus();
            return false;
        }
        
        String firstSchool = firstSchoolField.getText();
        if (firstSchool.equals("FIRST SCHOOL") || firstSchool.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter your first school",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            firstSchoolField.requestFocus();
            return false;
        }
        
        return true;
    }
    
    private void setupComboBoxes() {
        // This method is no longer needed since we're using a text field
    }

    private void validateDate() {
        try {
            String dateStr = dobField.getText().trim();
            String[] parts = dateStr.split("/");
            if (parts.length == 3) {
                int year = Integer.parseInt(parts[0]);
                int month = Integer.parseInt(parts[1]);
                int day = Integer.parseInt(parts[2]);
                
                LocalDate dob = LocalDate.of(year, month, day);
                int age = Period.between(dob, LocalDate.now()).getYears();
                
                if (age < 18) {
                    JOptionPane.showMessageDialog(this,
                        "You must be 18 or older to register",
                        "Age Restriction",
                        JOptionPane.WARNING_MESSAGE);
                    continueButton.setEnabled(false);
                }
            } else {
                JOptionPane.showMessageDialog(this,
                    "Please enter date in YYYY/MM/DD format",
                    "Invalid Date Format",
                    JOptionPane.WARNING_MESSAGE);
                continueButton.setEnabled(false);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Please enter a valid date",
                "Invalid Date",
                JOptionPane.WARNING_MESSAGE);
            continueButton.setEnabled(false);
        }
    }

    private void handleContinueButton() {
        if (!validateForm()) {
            return;
        }

        try {
            // Create user object
            String username = usernameField.getText().trim();
            String password = actualPassword;
            String role = userRadio.isSelected() ? "USER" : "ADMIN";
            
            // Get DOB from text field
            String[] dobParts = dobField.getText().trim().split("/");
            LocalDate dob = LocalDate.of(
                Integer.parseInt(dobParts[0]),
                Integer.parseInt(dobParts[1]),
                Integer.parseInt(dobParts[2])
            );
            
            // Get security questions
            String favoriteColor = favoriteColorField.getText().trim();
            String firstSchool = firstSchoolField.getText().trim();
            
            // Create user and save
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setUserType(role);
            user.setDateOfBirth(dob);
            user.setFavoriteColor(favoriteColor);
            user.setFirstSchool(firstSchool);
            
            // Initialize DAO with proper error handling
            UserRegisterDAO dao = null;
            try {
                dao = new UserRegisterDAO();
                
                // Check if username already exists
                if (dao.getUser(username) != null) {
                    JOptionPane.showMessageDialog(this,
                        "Username already exists. Please choose a different username.",
                        "Username Taken",
                        JOptionPane.WARNING_MESSAGE);
                    usernameField.requestFocus();
                    return;
                }
                
                // Try to create the user
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
                String errorMessage = "Database error: ";
                if (ex.getMessage().contains("Duplicate entry")) {
                    errorMessage = "Username already exists. Please choose a different username.";
                } else {
                    errorMessage += ex.getMessage();
                }
                JOptionPane.showMessageDialog(this,
                    errorMessage,
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            } finally {
                // Close the database connection
                if (dao != null) {
                    dao.closeConnection();
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "Invalid date format. Please use YYYY/MM/DD format.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "An unexpected error occurred: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void validateFields() {
        StringBuilder status = new StringBuilder("<html>");
        boolean allValid = true;

        // Username validation
        String username = usernameField.getText().trim();
        if (username.isEmpty() || username.equals("USERNAME")) {
            status.append("<font color='red'>✗ Username required</font><br>");
            allValid = false;
        } else if (username.length() < 3) {
            status.append("<font color='red'>✗ Username too short</font><br>");
            allValid = false;
        } else {
            status.append("<font color='green'>✓ Username valid</font><br>");
        }

        // Password validation
        if (actualPassword.isEmpty() || actualPassword.equals("Enter password")) {
            status.append("<font color='red'>✗ Password required</font><br>");
            allValid = false;
        } else {
            boolean hasUpperCase = actualPassword.matches(".*[A-Z].*");
            boolean hasSpecialChar = actualPassword.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*");
            boolean hasNumber = actualPassword.matches(".*[0-9].*");
            
            if (!hasUpperCase) {
                status.append("<font color='red'>✗ Password needs uppercase letter</font><br>");
                allValid = false;
            } else {
                status.append("<font color='green'>✓ Uppercase letter included</font><br>");
            }
            if (!hasSpecialChar) {
                status.append("<font color='red'>✗ Password needs special character</font><br>");
                allValid = false;
            } else {
                status.append("<font color='green'>✓ Special character included</font><br>");
            }
            if (!hasNumber) {
                status.append("<font color='red'>✗ Password needs number</font><br>");
                allValid = false;
            } else {
                status.append("<font color='green'>✓ Number included</font><br>");
            }
        }

        // Confirm password validation
        if (!actualPassword.equals(actualConfirmPassword)) {
            status.append("<font color='red'>✗ Passwords don't match</font><br>");
            allValid = false;
        } else if (!actualConfirmPassword.isEmpty() && !actualConfirmPassword.equals("Confirm password")) {
            status.append("<font color='green'>✓ Passwords match</font><br>");
        }

        // Date of birth validation
        String dobText = dobField.getText().trim();
        if (dobText.isEmpty()) {
            status.append("<font color='red'>✗ Date of birth required</font><br>");
            allValid = false;
        } else {
            try {
                String[] parts = dobText.split("/");
                if (parts.length == 3) {
                    int year = Integer.parseInt(parts[0]);
                    int month = Integer.parseInt(parts[1]);
                    int day = Integer.parseInt(parts[2]);
                    LocalDate dob = LocalDate.of(year, month, day);
                    int age = Period.between(dob, LocalDate.now()).getYears();
                    
                    if (age < 18) {
                        status.append("<font color='red'>✗ Must be 18+ to register</font><br>");
                        allValid = false;
                    } else {
                        status.append("<font color='green'>✓ Age verification passed</font><br>");
                    }
                } else {
                    status.append("<font color='red'>✗ Invalid date format (YYYY/MM/DD)</font><br>");
                    allValid = false;
                }
            } catch (Exception e) {
                status.append("<font color='red'>✗ Invalid date</font><br>");
                allValid = false;
            }
        }

        // Security questions validation
        String favoriteColor = favoriteColorField.getText().trim();
        if (favoriteColor.isEmpty() || favoriteColor.equals("FAVORITE COLOR")) {
            status.append("<font color='red'>✗ Favorite color required</font><br>");
            allValid = false;
        } else {
            status.append("<font color='green'>✓ Favorite color provided</font><br>");
        }

        String firstSchool = firstSchoolField.getText().trim();
        if (firstSchool.isEmpty() || firstSchool.equals("FIRST SCHOOL")) {
            status.append("<font color='red'>✗ First school required</font><br>");
            allValid = false;
        } else {
            status.append("<font color='green'>✓ First school provided</font><br>");
        }

        status.append("</html>");
        validationLabel.setText(status.toString());
        continueButton.setEnabled(allValid);
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
