package logintestfinal.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import logintestfinal.database.DatabaseConnection;
import logintestfinal.model.FilterCriteria;
import logintestfinal.model.User;

public class FilterDao {
    
    public List<User> searchUsers(FilterCriteria criteria) {
        List<User> matchedUsers = new ArrayList<>();
        StringBuilder query = new StringBuilder(
            "SELECT * FROM users WHERE 1=1 "
        );
        
        // Build dynamic query based on filter criteria
        if (criteria.getGender() != null && !criteria.getGender().isEmpty()) {
            query.append("AND gender = ? ");
        }
        if (criteria.getEducationLevel() != null && !criteria.getEducationLevel().isEmpty()) {
            query.append("AND education_level = ? ");
        }
        if (criteria.getPreferences() != null && !criteria.getPreferences().isEmpty()) {
            query.append("AND preferences = ? ");
        }
        query.append("AND age <= ? ");
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query.toString())) {
            
            int parameterIndex = 1;
            
            // Set parameters
            if (criteria.getGender() != null && !criteria.getGender().isEmpty()) {
                pstmt.setString(parameterIndex++, criteria.getGender());
            }
            if (criteria.getEducationLevel() != null && !criteria.getEducationLevel().isEmpty()) {
                pstmt.setString(parameterIndex++, criteria.getEducationLevel());
            }
            if (criteria.getPreferences() != null && !criteria.getPreferences().isEmpty()) {
                pstmt.setString(parameterIndex++, criteria.getPreferences());
            }
            pstmt.setInt(parameterIndex, criteria.getAgeRange());
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setGender(rs.getString("gender"));
                user.setAge(rs.getInt("age"));
                user.setEducationLevel(rs.getString("education_level"));
                user.setPreferences(rs.getString("preferences"));
                matchedUsers.add(user);
            }
        } catch (SQLException e) {
            System.err.println("Error searching users: " + e.getMessage());
        }
        
        return matchedUsers;
    }
    
    public boolean validateCriteria(FilterCriteria criteria) {
        if (criteria == null) {
            return false;
        }
        
        // Validate age range
        if (criteria.getAgeRange() < 18 || criteria.getAgeRange() > 78) {
            return false;
        }
        
        // Validate gender
        if (criteria.getGender() != null && !criteria.getGender().isEmpty()) {
            if (!criteria.getGender().matches("Male|Female|Other")) {
                return false;
            }
        }
        
        // Validate education level
        if (criteria.getEducationLevel() != null && !criteria.getEducationLevel().isEmpty()) {
            if (!criteria.getEducationLevel().matches("High School|Bachelor|Ph.D")) {
                return false;
            }
        }
        
        // Validate preferences
        if (criteria.getPreferences() != null && !criteria.getPreferences().isEmpty()) {
            if (!criteria.getPreferences().matches("Relationship|Casual dating|Friendship")) {
                return false;
            }
        }
        
        return true;
    }
} 