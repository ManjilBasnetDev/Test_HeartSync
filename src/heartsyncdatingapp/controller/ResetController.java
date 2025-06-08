/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package heartsyncdatingapp.controller;

import heartsyncdatingapp.dao.ResetPasswordDAO;
import heartsyncdatingapp.model.User;
import heartsyncdatingapp.view.ResetPassword;

/**
 *
 * @author manjil-basnet
 */
public class ResetController {
    private int userId;
    private ResetPasswordDAO resetPasswordDAO;

    public ResetController() {
        resetPasswordDAO = new ResetPasswordDAO();
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void showResetView() {
        ResetPassword resetView = new ResetPassword(userId);
        resetView.setVisible(true);
    }

    public boolean updatePassword(int userId, String newPassword) {
        try {
            User user = resetPasswordDAO.getUserById(userId);
            if (user != null) {
                user.setPassword(newPassword);
                return resetPasswordDAO.updateUser(user);
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
