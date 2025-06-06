package heartsyncdatingapp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import heartsyncdatingapp.dao.UserRegisterDAO;
import heartsyncdatingapp.model.User;

public class AdminPage extends JFrame {
    private JTable userTable;
    private DefaultTableModel tableModel;
    private UserRegisterDAO userDAO;
    
    public AdminPage() {
        setTitle("HeartSync Admin Panel");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        
        try {
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
                    return column == columns.length - 1;
                }
                
                @Override
                public Class<?> getColumnClass(int column) {
                    return column == columns.length - 1 ? JButton.class : Object.class;
                }
            };
            
            userTable = new JTable(tableModel);
            userTable.setFillsViewportHeight(true);
            
            // Set up the action column
            TableColumn actionColumn = userTable.getColumnModel().getColumn(columns.length - 1);
            actionColumn.setCellRenderer(new ButtonRenderer());
            actionColumn.setCellEditor(new ButtonEditor(new JCheckBox()));
            
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
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error initializing admin panel: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            dispose();
        }
    }
    
    private void refreshUserTable() {
        try {
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
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error refreshing user table: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteUser(int row) {
        try {
            String userId = String.valueOf(tableModel.getValueAt(row, 0));
            String username = (String) tableModel.getValueAt(row, 1);
            
            int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete user '" + username + "'?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
                
            if (choice == JOptionPane.YES_OPTION) {
                if (userDAO.deleteUser(userId)) {
                    tableModel.removeRow(row);
                    JOptionPane.showMessageDialog(this,
                        "User deleted successfully",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Failed to delete user",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error deleting user: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Custom button renderer for the action column
    private class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }
    
    // Custom button editor for the action column
    private class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private boolean isPushed;
        private int currentRow;
        
        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            currentRow = row;
            isPushed = true;
            return button;
        }
        
        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                if (label.equals("Delete")) {
                    deleteUser(currentRow);
                }
            }
            isPushed = false;
            return label;
        }
        
        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
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
            
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setPassword(password);
            newUser.setUserType(userType);
            newUser.setEmail(email);
            
            try {
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
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(dialog,
                    "Database error: " + ex.getMessage(),
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