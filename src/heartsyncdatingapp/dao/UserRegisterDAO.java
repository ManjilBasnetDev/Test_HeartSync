package heartsyncdatingapp.dao;

import java.sql.Connection;
import java.sql.Date;
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
        String sql = "INSERT INTO users (username, password, user_type, date_of_birth, favorite_color, first_school) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, DatabaseConnection.hashPassword(user.getPassword()));
            stmt.setString(3, user.getUserType());
            stmt.setDate(4, Date.valueOf(user.getDateOfBirth()));
            stmt.setString(5, user.getFavoriteColor());
            stmt.setString(6, user.getFirstSchool());
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                // Get the generated ID
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        user.setId(generatedKeys.getInt(1));
                        return true;
                    }
                }
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Error creating user: " + e.getMessage());
            throw e;
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
            stmt.setString(2, DatabaseConnection.hashPassword(password));
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
            stmt.setString(1, DatabaseConnection.hashPassword(user.getPassword()));
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPhoneNumber());
            stmt.setDate(4, Date.valueOf(user.getDateOfBirth()));
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
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setUserType(rs.getString("user_type"));
        user.setEmail(rs.getString("email"));
        user.setPhoneNumber(rs.getString("phone_number"));
        user.setDateOfBirth(rs.getDate("date_of_birth").toLocalDate());
        user.setGender(rs.getString("gender"));
        user.setInterests(rs.getString("interests"));
        user.setBio(rs.getString("bio"));
        user.setFavoriteColor(rs.getString("favorite_color"));
        user.setFirstSchool(rs.getString("first_school"));
        return user;
    }
    
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
} 