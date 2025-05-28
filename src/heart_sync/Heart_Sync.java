/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package heart_sync;

import heart_sync.view.Register;
import javax.swing.SwingUtilities;

/**
 *
 * @author rohitsharma
 */
public class Heart_Sync {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Register register = new Register();
            register.setLocationRelativeTo(null); // Center on screen
            register.setVisible(true);
        });
    }
    
}
