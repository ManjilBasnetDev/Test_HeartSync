package heartsync.dao;

import heartsync.database.DatabaseConfig;
import heartsync.model.UserProfile;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserProfileDAO {
    
    public boolean createProfile(UserProfile profile) {
        String sql = "INSERT INTO user_profiles (user_id, first_name, last_name, date_of_birth, gender, profile_picture, bio) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, profile.getUserId());
            pstmt.setString(2, profile.getFirstName());
            pstmt.setString(3, profile.getLastName());
            pstmt.setDate(4, profile.getDateOfBirth());
            pstmt.setString(5, profile.getGender());
            pstmt.setString(6, profile.getProfilePicture());
            pstmt.setString(7, profile.getBio());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public UserProfile getProfileByUserId(int userId) {
        String sql = "SELECT * FROM user_profiles WHERE user_id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractProfileFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean updateProfile(UserProfile profile) {
        String sql = "UPDATE user_profiles SET first_name = ?, last_name = ?, date_of_birth = ?, " +
                    "gender = ?, profile_picture = ?, bio = ? WHERE user_id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, profile.getFirstName());
            pstmt.setString(2, profile.getLastName());
            pstmt.setDate(3, profile.getDateOfBirth());
            pstmt.setString(4, profile.getGender());
            pstmt.setString(5, profile.getProfilePicture());
            pstmt.setString(6, profile.getBio());
            pstmt.setInt(7, profile.getUserId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteProfile(int userId) {
        String sql = "DELETE FROM user_profiles WHERE user_id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private UserProfile extractProfileFromResultSet(ResultSet rs) throws SQLException {
        UserProfile profile = new UserProfile();
        profile.setUserId(rs.getInt("user_id"));
        profile.setFirstName(rs.getString("first_name"));
        profile.setLastName(rs.getString("last_name"));
        profile.setDateOfBirth(rs.getDate("date_of_birth"));
        profile.setGender(rs.getString("gender"));
        profile.setProfilePicture(rs.getString("profile_picture"));
        profile.setBio(rs.getString("bio"));
        return profile;
    }
} 