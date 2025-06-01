/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package heartsync;

import heartsync.controller.ResetController;
import heartsync.database.DatabaseConfig;
import heartsync.dao.UserDAO;
import heartsync.model.User;
import javax.swing.SwingUtilities;

/**
 *
 * @author HP
 */
public class Heartsync {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Initialize database
                DatabaseConfig.initializeDatabase();
                
                // Create a test user if it doesn't exist
                UserDAO userDAO = new UserDAO();
                User testUser = userDAO.getUserByUsername("testuser");
                
                if (testUser == null) {
                    testUser = new User("testuser", "oldpassword", "test@example.com");
                    userDAO.createUser(testUser);
                    testUser = userDAO.getUserByUsername("testuser"); // Get the user with ID
                }
                
                // Initialize controller with test user
                ResetController resetController = new ResetController();
                resetController.setUserId(testUser.getId());
                
                // Show the reset view
                resetController.showResetView();
                
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Failed to initialize application: " + e.getMessage());
            }
        });
    }
    
}
