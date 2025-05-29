/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package heartsync.controller;

import heartsync.view.resetview;
import javax.swing.JOptionPane;

/**
 *
 * @author HP
 */
public class ResetController {
    private resetview view;
    
    public ResetController() {
        // Initialize without database
    }
    
    public void setView(resetview view) {
        this.view = view;
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
        
        // In a real app, this would save to database
        // For now, just return success
        return true;
    }
    
    public void showResetView() {
        resetview view = new resetview();
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
