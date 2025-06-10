/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package heartsyncdatingapp.View;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Modern swipe interface for browsing through potential matches.
 * @author manjil-basnet
 */
public class Swipe extends JFrame {
    private static final int WINDOW_RADIUS = 20;
    private static final Color BACKGROUND_COLOR = new Color(255, 216, 227); // Pink background
    private static final Color BUTTON_COLOR = new Color(229, 89, 36); // Orange
    private static final Color LIKE_COLOR = new Color(46, 204, 113); // Green
    private static final Color REJECT_COLOR = new Color(231, 76, 60); // Red
    private static final Color BUTTON_HOVER_OVERLAY = new Color(0, 0, 0, 40); // Semi-transparent black for hover
    private static final Color CLOSE_BUTTON_COLOR = new Color(231, 76, 60); // Red for close button
    
    private final JPanel mainPanel;
    private final JPanel cardPanel;
    private final JLabel imageLabel;
    private final JLabel nameLabel;
    private final JLabel ageLabel;
    private final JLabel bioLabel;
    private final RoundedButton nextButton;
    private final RoundedButton backButton;
    private final RoundedButton likeButton;
    private final RoundedButton rejectButton;
    private final RoundedButton closeButton;
    private final ArrayList<ProfileData> profiles;
    private int currentIndex;
    
    private static class ProfileData {
        String imagePath;
        String name;
        int age;
        String bio;
        
        ProfileData(String imagePath, String name, int age, String bio) {
            this.imagePath = imagePath;
            this.name = name;
            this.age = age;
            this.bio = bio;
        }
    }

    private static class RoundedButton extends JButton {
        private boolean isHovered = false;
        private final Color baseColor;
        private static final int RADIUS = 25; // More rounded corners

        public RoundedButton(String text, Color color) {
            super(text);
            this.baseColor = color;
            setupButton();
        }

        private void setupButton() {
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setFont(new Font("Segoe UI", Font.BOLD, 16));
            setForeground(Color.WHITE);
            setCursor(new Cursor(Cursor.HAND_CURSOR));

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

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Draw background
            if (isHovered) {
                g2.setColor(baseColor.darker());
            } else {
                g2.setColor(baseColor);
            }
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), RADIUS, RADIUS);
            
            // Add subtle gradient effect
            if (isHovered) {
                g2.setColor(BUTTON_HOVER_OVERLAY);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), RADIUS, RADIUS);
            }
            
            super.paintComponent(g);
        }
    }

    public Swipe() {
        setTitle("HeartSync - Find Matches");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 900);
        setLocationRelativeTo(null);
        setUndecorated(true);
        
        // Main panel with rounded corners
        mainPanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BACKGROUND_COLOR);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), WINDOW_RADIUS, WINDOW_RADIUS);
            }
        };
        mainPanel.setOpaque(false);
        
        // Add close button
        closeButton = new RoundedButton("X", CLOSE_BUTTON_COLOR) {
            private boolean isHovered = false;

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

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (isHovered) {
                    g2.setColor(CLOSE_BUTTON_COLOR.darker());
                } else {
                    g2.setColor(CLOSE_BUTTON_COLOR);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight()); // Make it circular
                
                // Add hover effect
                if (isHovered) {
                    g2.setColor(BUTTON_HOVER_OVERLAY);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
                }
                
                super.paintComponent(g);
            }
        };
        closeButton.setBounds(650, 10, 30, 30);
        closeButton.setFont(new Font("Segoe UI", Font.BOLD, 16)); // Slightly larger font
        closeButton.addActionListener(e -> dispose());
        mainPanel.add(closeButton);
        
        // Card panel for profile display
        cardPanel = new JPanel(null);
        cardPanel.setOpaque(false);
        cardPanel.setBounds(50, 50, 600, 800);
        
        // Profile image
        imageLabel = new JLabel();
        imageLabel.setBounds(50, 20, 500, 500);
        imageLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Profile info
        nameLabel = new JLabel();
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        nameLabel.setForeground(new Color(51, 51, 51));
        nameLabel.setBounds(50, 540, 300, 30);
        
        ageLabel = new JLabel();
        ageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        ageLabel.setForeground(new Color(102, 102, 102));
        ageLabel.setBounds(50, 575, 100, 25);
        
        bioLabel = new JLabel();
        bioLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        bioLabel.setForeground(new Color(102, 102, 102));
        bioLabel.setBounds(50, 610, 500, 60);
        
        // Position all buttons with wider spacing
        rejectButton = new RoundedButton("✕ Pass", REJECT_COLOR);
        rejectButton.setBounds(50, 700, 120, 45);
        
        backButton = new RoundedButton("← Back", BUTTON_COLOR);
        backButton.setBounds(190, 700, 120, 45);
        
        nextButton = new RoundedButton("Next →", BUTTON_COLOR);
        nextButton.setBounds(330, 700, 120, 45);
        
        likeButton = new RoundedButton("♥ Like", LIKE_COLOR);
        likeButton.setBounds(470, 700, 120, 45);
        
        // Add components
        cardPanel.add(imageLabel);
        cardPanel.add(nameLabel);
        cardPanel.add(ageLabel);
        cardPanel.add(bioLabel);
        cardPanel.add(nextButton);
        cardPanel.add(backButton);
        cardPanel.add(likeButton);
        cardPanel.add(rejectButton);
        
        mainPanel.add(cardPanel);
        setContentPane(mainPanel);
        
        // Setup window shape
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), WINDOW_RADIUS, WINDOW_RADIUS));
        
        // Initialize profiles
        profiles = new ArrayList<>();
        setupProfiles();
        setupListeners();
        showCurrentProfile();
    }
    
    private void setupProfiles() {
        // Add sample profiles - in a real app, these would come from a database
        profiles.add(new ProfileData(
            "/ImagePicker/RajeshHamalPhoto.png",
            "Rajesh Hamal",
            45,
            "Mahanayak of Nepali Film Industry. Actor, Model, and Philanthropist."
        ));
        profiles.add(new ProfileData(
            "/ImagePicker/RajeshHamalPhoto2.jpg",
            "Rajesh Hamal",
            45,
            "Award-winning actor with over 200 films. Loves adventure and traveling."
        ));
        currentIndex = 0;
    }
    
    private void setupListeners() {
        // Window dragging
        MouseAdapter dragListener = new MouseAdapter() {
            private Point mouseDownCompCoords;
            
            @Override
            public void mousePressed(MouseEvent e) {
                mouseDownCompCoords = e.getPoint();
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                mouseDownCompCoords = null;
            }
            
            @Override
            public void mouseDragged(MouseEvent e) {
                if (mouseDownCompCoords != null) {
                    Point currCoords = e.getLocationOnScreen();
                    setLocation(currCoords.x - mouseDownCompCoords.x, 
                              currCoords.y - mouseDownCompCoords.y);
                }
            }
        };
        
        mainPanel.addMouseListener(dragListener);
        mainPanel.addMouseMotionListener(dragListener);
        
        // Button actions
        nextButton.addActionListener(e -> showNextProfile());
        backButton.addActionListener(e -> showPreviousProfile());
        likeButton.addActionListener(e -> likeCurrentProfile());
        rejectButton.addActionListener(e -> rejectCurrentProfile());
    }
    
    private void showCurrentProfile() {
        if (currentIndex >= 0 && currentIndex < profiles.size()) {
            ProfileData profile = profiles.get(currentIndex);
            
            // Load and scale image
            try {
                java.net.URL imageUrl = getClass().getResource(profile.imagePath);
                if (imageUrl != null) {
                    ImageIcon icon = new ImageIcon(imageUrl);
                    Image img = icon.getImage().getScaledInstance(
                        imageLabel.getWidth(), 
                        imageLabel.getHeight(), 
                        Image.SCALE_SMOOTH
                    );
                    imageLabel.setIcon(new ImageIcon(img));
                }
            } catch (Exception e) {
                e.printStackTrace();
                imageLabel.setIcon(null);
                JOptionPane.showMessageDialog(this,
                    "Error loading image: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
            
            // Update profile info
            nameLabel.setText(profile.name);
            ageLabel.setText(profile.age + " years");
            bioLabel.setText("<html>" + profile.bio + "</html>");
            
            // Update button states
            backButton.setEnabled(currentIndex > 0);
            nextButton.setEnabled(currentIndex < profiles.size() - 1);
        }
    }
    
    private void showNextProfile() {
        if (currentIndex < profiles.size() - 1) {
            currentIndex++;
            showCurrentProfile();
        } else {
            JOptionPane.showMessageDialog(this,
                "No more profiles to show!",
                "End of List",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void showPreviousProfile() {
        if (currentIndex > 0) {
            currentIndex--;
            showCurrentProfile();
        } else {
            JOptionPane.showMessageDialog(this,
                "This is the first profile!",
                "First Profile",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void likeCurrentProfile() {
        ProfileData profile = profiles.get(currentIndex);
        JOptionPane.showMessageDialog(this,
            "You liked " + profile.name + "!\nMatch functionality coming soon.",
            "Liked",
            JOptionPane.INFORMATION_MESSAGE);
        showNextProfile();
    }
    
    private void rejectCurrentProfile() {
        ProfileData profile = profiles.get(currentIndex);
        JOptionPane.showMessageDialog(this,
            "You passed on " + profile.name + ".",
            "Passed",
            JOptionPane.INFORMATION_MESSAGE);
        showNextProfile();
    }
<<<<<<< HEAD
    
=======

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Swipe.class.getName());

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cardPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagePicker/RajeshHamalPhoto.png"))); // NOI18N

        jButton1.setText("Next");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Back");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Like");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Reject");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout cardPanelLayout = new javax.swing.GroupLayout(cardPanel);
        cardPanel.setLayout(cardPanelLayout);
        cardPanelLayout.setHorizontalGroup(
            cardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardPanelLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(cardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(cardPanelLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1)
                        .addGap(51, 51, 51))
                    .addGroup(cardPanelLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addContainerGap(33, Short.MAX_VALUE))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cardPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton4)
                .addGap(36, 36, 36)
                .addComponent(jButton3)
                .addGap(91, 91, 91))
        );
        cardPanelLayout.setVerticalGroup(
            cardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardPanelLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(cardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addGap(18, 18, 18)
                .addGroup(cardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3)
                    .addComponent(jButton4))
                .addContainerGap(65, Short.MAX_VALUE))
        );

        jButton5.setText("View Profile");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setText("Message");

        jButton7.setText("More");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cardPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton5)
                    .addComponent(jButton7)
                    .addComponent(jButton6))
                .addContainerGap(9, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cardPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(91, 91, 91)
                .addComponent(jButton5)
                .addGap(127, 127, 127)
                .addComponent(jButton6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton7)
                .addGap(162, 162, 162))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton5ActionPerformed

    /**
     * @param args the command line arguments
     */
>>>>>>> origin/main
    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            Swipe frame = new Swipe();
            frame.setVisible(true);
        });
    }
<<<<<<< HEAD
=======

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel cardPanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
>>>>>>> origin/main
}
