/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package heartsync.controller;

import heartsync.view.resetview;
import heartsync.dao.UserDAO;
import heartsync.model.User;
import javax.swing.JOptionPane;

/**
 *
 * @author HP
 */
public class ResetController {
    private resetview view;
    private UserDAO userDAO;
    private int userId; // Store the user ID for whom we're resetting the password
    
    public ResetController() {
        userDAO = new UserDAO();
    }
    
    public void setView(resetview view) {
        this.view = view;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public boolean resetPassword(String newPassword) {
        // Simple password validation
        if (newPassword.length() < 6) {
            JOptionPane.showMessageDialog(view,
                "Password must be at least 6 characters long",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        try {
            // Get the user from database
            User user = userDAO.getUserById(userId);
            if (user == null) {
                JOptionPane.showMessageDialog(view,
                    "User not found",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
            // Update the password
            user.setPassword(newPassword);
            boolean updated = userDAO.updateUser(user);
            
            if (!updated) {
                JOptionPane.showMessageDialog(view,
                    "Failed to update password",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
            return true;
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view,
                "An error occurred while resetting password: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    public void showResetView() {
        resetview view = new resetview();
        view.setController(this);
        this.setView(view);
        view.setVisible(true);
    }
    
    public void goBack() {
        if (view != null) {
            view.dispose();
            // TODO: You can add navigation to previous screen here
        }
    }
}
