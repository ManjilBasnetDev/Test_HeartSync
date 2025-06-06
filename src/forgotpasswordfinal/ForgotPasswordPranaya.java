package forgotpasswordfinal;

import forgotpasswordfinal.dao.UserDAO;
import forgotpasswordfinal.validation.InputValidator;
import javax.swing.JOptionPane;
import java.sql.SQLException;

public class ForgotPasswordPranaya extends javax.swing.JFrame {

    public ForgotPasswordPranaya() {
        initComponents();
        setLocationRelativeTo(null);
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        forgotPasswordLabel = new javax.swing.JLabel();
        usernameTextField = new javax.swing.JTextField();
        favoriteColorTextField = new javax.swing.JTextField();
        firstSchoolTextField = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 198, 209));

        jPanel4.setBackground(new java.awt.Color(255, 198, 209));

        forgotPasswordLabel.setFont(new java.awt.Font("Segoe UI", 1, 18));
        forgotPasswordLabel.setForeground(new java.awt.Color(112, 0, 61));
        forgotPasswordLabel.setText("FORGOT PASSWORD?");

        usernameTextField.setFont(new java.awt.Font("Segoe UI", 1, 12));
        usernameTextField.setText("USERNAME");
        usernameTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                usernameTextFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                usernameTextFieldFocusLost(evt);
            }
        });

        favoriteColorTextField.setFont(new java.awt.Font("Segoe UI", 1, 12));
        favoriteColorTextField.setText("FAVORITE COLOR?");
        favoriteColorTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                favoriteColorTextFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                favoriteColorTextFieldFocusLost(evt);
            }
        });

        firstSchoolTextField.setFont(new java.awt.Font("Segoe UI", 1, 12));
        firstSchoolTextField.setText("FIRST SCHOOL?");
        firstSchoolTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                firstSchoolTextFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                firstSchoolTextFieldFocusLost(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(217, 249, 221));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 14));
        jButton1.setForeground(new java.awt.Color(112, 0, 61));
        jButton1.setText("Continue ->");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(255, 216, 208));
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 14));
        jButton2.setText("<- Back  ");
        jButton2.addActionListener(this::jButton2ActionPerformed);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(62, 62, 62)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addGap(35, 35, 35)
                        .addComponent(jButton2))
                    .addComponent(usernameTextField)
                    .addComponent(favoriteColorTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE)
                    .addComponent(firstSchoolTextField))
                .addContainerGap(47, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(forgotPasswordLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(73, 73, 73))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(forgotPasswordLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(usernameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(favoriteColorTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(firstSchoolTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap(42, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        String username = InputValidator.sanitizeInput(usernameTextField.getText());
        String favoriteColor = InputValidator.sanitizeInput(favoriteColorTextField.getText());
        String firstSchool = InputValidator.sanitizeInput(firstSchoolTextField.getText());
        if (usernameTextField.getText().isEmpty()){
            JOptionPane.showMessageDialog(this, "Enter Username", "Message", JOptionPane.INFORMATION_MESSAGE);
        if (!InputValidator.isValidUsername(username)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid username (at least 3 characters)","Message", JOptionPane.INFORMATION_MESSAGE);
           return;
        }

        if (!InputValidator.isValidSecurityAnswer(favoriteColor) || !InputValidator.isValidSecurityAnswer(firstSchool)) {
            JOptionPane.showMessageDialog(this, "Please answer both security questions");
            return;
        }

        try {
            UserDAO userDAO = new UserDAO();
            if (userDAO.validateSecurityQuestions(username, favoriteColor, firstSchool)) {
                String newPassword = JOptionPane.showInputDialog(this, "Enter your new password:\n(Must contain uppercase, lowercase, number, and special character)");
                
                if (newPassword != null && InputValidator.isValidPassword(newPassword)) {
                    userDAO.updatePassword(username, newPassword);
                    JOptionPane.showMessageDialog(this, "Password updated successfully!");
                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid password format. Password must be at least 8 characters long and contain uppercase, lowercase, number, and special character.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid security answers or username not found.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage());
            e.printStackTrace();
        }
        }
    }
    
        
    
    
    
       

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        this.dispose();
    }

    private void usernameTextFieldFocusLost(java.awt.event.FocusEvent evt) {
        String Placeholder = "USERNAME";
        if (usernameTextField.getText().isEmpty()) {
            usernameTextField.setText(Placeholder);
        }
    }

    private void favoriteColorTextFieldFocusLost(java.awt.event.FocusEvent evt) {
        String Placeholder = "FAVORITE COLOR?";
        if (favoriteColorTextField.getText().isEmpty()) {
            favoriteColorTextField.setText(Placeholder);
        }
    }

    private void firstSchoolTextFieldFocusLost(java.awt.event.FocusEvent evt) {
        String Placeholder = "FIRST SCHOOL?";
        if (firstSchoolTextField.getText().isEmpty()) {
            firstSchoolTextField.setText(Placeholder);
        }
    }

    private void usernameTextFieldFocusGained(java.awt.event.FocusEvent evt) {
        String Placeholder = "USERNAME";
        if (usernameTextField.getText().equalsIgnoreCase(Placeholder)) {
            usernameTextField.setText("");
        }
    }

    private void firstSchoolTextFieldFocusGained(java.awt.event.FocusEvent evt) {
        String Placeholder = "FIRST SCHOOL?";
        if (firstSchoolTextField.getText().equalsIgnoreCase(Placeholder)) {
            firstSchoolTextField.setText("");
        }
    }

    private void favoriteColorTextFieldFocusGained(java.awt.event.FocusEvent evt) {
        String Placeholder = "FAVORITE COLOR?";
        if (favoriteColorTextField.getText().equalsIgnoreCase(Placeholder)) {
            favoriteColorTextField.setText("");
        }
    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ForgotPasswordPranaya.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> {
            new ForgotPasswordPranaya().setVisible(true);
        });
    }

    private javax.swing.JTextField favoriteColorTextField;
    private javax.swing.JTextField firstSchoolTextField;
    private javax.swing.JLabel forgotPasswordLabel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JTextField usernameTextField;
} 