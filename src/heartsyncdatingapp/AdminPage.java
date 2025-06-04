package heartsyncdatingapp;

import heartsyncdatingapp.dao.UserRegisterDAO;
import heartsyncdatingapp.model.User;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class AdminPage extends JFrame {
    private JTable userTable;
    private DefaultTableModel tableModel;
    private UserRegisterDAO userDAO;
    
    public AdminPage() {
        setTitle("HeartSync Admin Panel");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        
        // Initialize DAO
        userDAO = new UserRegisterDAO();
        
        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(255, 240, 245));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Add header
        JLabel headerLabel = new JLabel("Admin Dashboard", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        mainPanel.add(headerLabel, BorderLayout.NORTH);
        
        // Create table
        String[] columns = {"ID", "Username", "User Type", "Email", "Phone", "Gender", "Actions"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == columns.length - 1; // Only allow editing of the Actions column
            }
        };
        userTable = new JTable(tableModel);
        userTable.setFillsViewportHeight(true);
        
        // Add table to scroll pane
        JScrollPane scrollPane = new JScrollPane(userTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Create buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonsPanel.setOpaque(false);
        
        JButton refreshButton = new JButton("Refresh");
        JButton addUserButton = new JButton("Add User");
        
        buttonsPanel.add(refreshButton);
        buttonsPanel.add(addUserButton);
        
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);
        
        // Add action listeners
        refreshButton.addActionListener(e -> refreshUserTable());
        addUserButton.addActionListener(e -> showAddUserDialog());
        
        // Set content pane
        setContentPane(mainPanel);
        
        // Initial load of user data
        refreshUserTable();
    }
    
    private void refreshUserTable() {
        tableModel.setRowCount(0); // Clear existing data
        
        List<User> users = userDAO.getAllUsers();
        for (User user : users) {
            Object[] rowData = {
                user.getId(),
                user.getUsername(),
                user.getUserType(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getGender(),
                "Delete"
            };
            tableModel.addRow(rowData);
        }
    }
    
    private void showAddUserDialog() {
        JDialog dialog = new JDialog(this, "Add New User", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JTextField emailField = new JTextField();
        JComboBox<String> userTypeCombo = new JComboBox<>(new String[]{"USER", "ADMIN"});
        
        formPanel.add(new JLabel("Username:"));
        formPanel.add(usernameField);
        formPanel.add(new JLabel("Password:"));
        formPanel.add(passwordField);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);
        formPanel.add(new JLabel("User Type:"));
        formPanel.add(userTypeCombo);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        saveButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String email = emailField.getText();
            String userType = (String) userTypeCombo.getSelectedItem();
            
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(dialog,
                    "Username and password are required",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            User newUser = new User(username, password, userType);
            newUser.setEmail(email);
            
            if (userDAO.createUser(newUser)) {
                JOptionPane.showMessageDialog(dialog,
                    "User created successfully",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
                refreshUserTable();
            } else {
                JOptionPane.showMessageDialog(dialog,
                    "Failed to create user",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
} 