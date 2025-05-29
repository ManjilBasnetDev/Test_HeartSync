/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package heartsync;

import heartsync.controller.ResetController;
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
            // Initialize controller
            ResetController resetController = new ResetController();
            
            // Show the reset view
            resetController.showResetView();
        });
    }
    
}
