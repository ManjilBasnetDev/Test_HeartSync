package heartsyncdatingapp;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import heartsyncdatingapp.controller.UserProfileController;
import heartsyncdatingapp.model.UserProfile;
import heartsyncdatingapp.view.ProfileSetupView;

/**
 *
 * @author rohitsharma
 */
public class UserSetup {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            // Set system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {
        }

        // Create MVC components
        UserProfile model = new UserProfile();
        // Use a default username for direct profile setup
        String defaultUsername = "guest_" + System.currentTimeMillis();
        UserProfileController controller = new UserProfileController(model, defaultUsername);
        
        // Create and show the main view
        SwingUtilities.invokeLater(() -> {
            ProfileSetupView view = new ProfileSetupView(controller);
            view.setVisible(true);
        });
    }
    
    private static ImageIcon loadImageIcon(String resourcePath) {
        try {
            // Add leading slash to make it an absolute path in the classpath
            InputStream is = UserSetup.class.getClassLoader().getResourceAsStream("images/" + resourcePath);
            if (is != null) {
                byte[] imageBytes = is.readAllBytes();
                return new ImageIcon(imageBytes);
            }
        } catch (IOException e) {
            System.err.println("Error loading image from resources: " + resourcePath);
        }
        // Return a simple colored rectangle as fallback
        int width = 100;
        int height = 100;
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bi.createGraphics();
        g.setColor(new Color(219, 112, 147)); // Pink color
        g.fillRect(0, 0, width, height);
        g.dispose();
        return new ImageIcon(bi);
    }
}