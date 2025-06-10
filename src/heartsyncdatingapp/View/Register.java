package heartsyncdatingapp.View;

import heartsyncdatingapp.AdminPage;
import heartsyncdatingapp.dao.UserRegisterDAO;
import heartsyncdatingapp.model.User;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;

public class Register extends JFrame {
    private static final int WINDOW_RADIUS = 35;
    private static final int FIELD_RADIUS = 35;
    private static final int BUTTON_RADIUS = 35;
    private JPanel mainPanel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmField;
    private JButton continueButton;
    private JButton backButton;
    private JRadioButton userRadio;
    private JRadioButton adminRadio;
    private JCheckBox showPassword1;
    private JCheckBox showPassword2;
    private Point mouseDownCompCoords;

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
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBounds(0, 40, 400, 40);
        
        // Username field
        usernameField = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), FIELD_RADIUS, FIELD_RADIUS);
                super.paintComponent(g);
            }
        };
        usernameField.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        usernameField.setBounds(50, 100, 300, 45);
        usernameField.setOpaque(false);
        
        // Password field
        passwordField = new JPasswordField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), FIELD_RADIUS, FIELD_RADIUS);
                super.paintComponent(g);
            }
        };
        passwordField.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        passwordField.setBounds(50, 160, 300, 45);
        passwordField.setOpaque(false);
        
        // Show password checkboxes
        showPassword1 = new JCheckBox("Show");
        showPassword2 = new JCheckBox("Show");
        showPassword1.setBounds(360, 170, 20, 20);
        showPassword2.setBounds(360, 230, 20, 20);
        showPassword1.setOpaque(false);
        showPassword2.setOpaque(false);
        
        // Confirm password field
        confirmField = new JPasswordField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), FIELD_RADIUS, FIELD_RADIUS);
                super.paintComponent(g);
            }
        };
        confirmField.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        confirmField.setBounds(50, 220, 300, 45);
        confirmField.setOpaque(false);
        
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
        
        // Add mouse listener to admin radio button for immediate response
        adminRadio.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Admin radio clicked"); // Debug message
                adminRadio.setSelected(true);
                userRadio.setSelected(false);
                
                // Create and position admin page before showing it
                AdminPage adminPage = new AdminPage();
                Point location = getLocation();
                adminPage.setLocation(location.x, location.y);
                
                // Ensure smooth transition
                adminPage.setVisible(true);
                SwingUtilities.invokeLater(() -> {
                    dispose(); // Dispose after admin page is visible
                });
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
        
        // Set placeholder text
        usernameField.setText("USERNAME");
        usernameField.setForeground(Color.GRAY);
        passwordField.setText("PASSWORD");
        passwordField.setForeground(Color.GRAY);
        passwordField.setEchoChar((char)0);
        confirmField.setText("CONFIRM");
        confirmField.setForeground(Color.GRAY);
        confirmField.setEchoChar((char)0);
        
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
                if (String.valueOf(passwordField.getPassword()).equals("PASSWORD")) {
                    passwordField.setText("");
                    passwordField.setForeground(Color.BLACK);
                    passwordField.setEchoChar('•');
                }
            }
            
            @Override
            public void focusLost(FocusEvent evt) {
                if (String.valueOf(passwordField.getPassword()).isEmpty()) {
                    passwordField.setText("PASSWORD");
                    passwordField.setForeground(Color.GRAY);
                    passwordField.setEchoChar((char)0);
                }
            }
        });
        
        // Confirm password field focus listeners
        confirmField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent evt) {
                if (String.valueOf(confirmField.getPassword()).equals("CONFIRM")) {
                    confirmField.setText("");
                    confirmField.setForeground(Color.BLACK);
                    confirmField.setEchoChar('•');
                }
            }
            
            @Override
            public void focusLost(FocusEvent evt) {
                if (String.valueOf(confirmField.getPassword()).isEmpty()) {
                    confirmField.setText("CONFIRM");
                    confirmField.setForeground(Color.GRAY);
                    confirmField.setEchoChar((char)0);
                }
            }
        });
        
        // Show password checkboxes
        showPassword1.addActionListener(e -> {
            String pass = String.valueOf(passwordField.getPassword());
            if (showPassword1.isSelected()) {
                if (!pass.equals("PASSWORD")) {
                    passwordField.setEchoChar((char)0);
                }
            } else {
                if (!pass.equals("PASSWORD")) {
                    passwordField.setEchoChar('•');
                }
            }
        });
        
        showPassword2.addActionListener(e -> {
            String confirm = String.valueOf(confirmField.getPassword());
            if (showPassword2.isSelected()) {
                if (!confirm.equals("CONFIRM")) {
                    confirmField.setEchoChar((char)0);
                }
            } else {
                if (!confirm.equals("CONFIRM")) {
                    confirmField.setEchoChar('•');
                }
            }
        });

        // Continue button action listener
        continueButton.addActionListener(e -> {
            if (validateForm()) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String userType = userRadio.isSelected() ? "USER" : "ADMIN";
                
                User newUser = new User(username, password, userType);
                UserRegisterDAO userDAO = new UserRegisterDAO();
                
                if (userDAO.createUser(newUser)) {
                    JOptionPane.showMessageDialog(this, 
                        "Registration successful!\nUsername: " + username,
                        "Success", 
                        JOptionPane.INFORMATION_MESSAGE);
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
        String password = String.valueOf(passwordField.getPassword());
        if (password.equals("PASSWORD") || password.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter a password",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            passwordField.requestFocus();
            return false;
        }

        // Check confirm password
        String confirm = String.valueOf(confirmField.getPassword());
        if (confirm.equals("CONFIRM") || confirm.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please confirm your password",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            confirmField.requestFocus();
            return false;
        }

        if (!password.equals(confirm)) {
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