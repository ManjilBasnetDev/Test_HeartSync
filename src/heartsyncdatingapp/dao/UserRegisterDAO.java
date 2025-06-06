package heartsyncdatingapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import heartsyncdatingapp.database.DatabaseConnection;
import heartsyncdatingapp.model.User;

public class UserRegisterDAO {
    private Connection connection;
    
    public UserRegisterDAO() throws SQLException {
        connection = DatabaseConnection.getConnection();
    }
    
    public boolean createUser(User user) throws SQLException {
        String sql = "INSERT INTO users (username, password, user_type) VALUES (?, ?, ?)";
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getUserType());
            
            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        }
    }
    
    private boolean usernameExists(String username, Connection conn) throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
    
    public User getUser(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return extractUserFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving user: " + e.getMessage());
        }
        return null;
    }
    
    public boolean validateUser(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            
            return rs.next(); // Returns true if user exists with given credentials
        } catch (SQLException e) {
            System.out.println("Error validating user: " + e.getMessage());
            return false;
        }
    }
    
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                users.add(extractUserFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving users: " + e.getMessage());
        }
        return users;
    }
    
    public boolean updateUser(User user) {
        String sql = "UPDATE users SET password = ?, email = ?, phone_number = ?, " +
                    "date_of_birth = ?, gender = ?, interests = ?, bio = ? WHERE username = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getPassword());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPhoneNumber());
            stmt.setString(4, user.getDateOfBirth());
            stmt.setString(5, user.getGender());
            stmt.setString(6, user.getInterests());
            stmt.setString(7, user.getBio());
            stmt.setString(8, user.getUsername());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating user: " + e.getMessage());
            return false;
        }
    }
    
    public boolean deleteUser(String username) {
        String sql = "DELETE FROM users WHERE username = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting user: " + e.getMessage());
            return false;
        }
    }
    
    private User extractUserFromResultSet(ResultSet rs) throws SQLException {
        return new User(
            rs.getInt("id"),
            rs.getString("username"),
            rs.getString("password"),
            rs.getString("user_type"),
            rs.getString("email"),
            rs.getString("phone_number"),
            rs.getString("date_of_birth"),
            rs.getString("gender"),
            rs.getString("interests"),
            rs.getString("bio")
        );
    }
} 