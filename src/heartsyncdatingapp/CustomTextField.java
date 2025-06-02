package heartsyncdatingapp;

import java.awt.*;
import javax.swing.*;

/**
 * A custom text field component that ensures a white background.
 */
public class CustomTextField extends JTextField {
    
    /**
     * Creates a new text field with the specified number of columns.
     *
     * @param columns the number of columns to use in calculating the preferred width
     */
    public CustomTextField(int columns) {
        super(columns);
        setOpaque(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(g);
    }
} 