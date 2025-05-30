package heartsync.database;

import heartsync.model.User;
import heartsync.model.UserProfile;
import heartsync.dao.UserDAO;
import heartsync.dao.UserProfileDAO;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseTest {
    public static void main(String[] args) {
        try {
            // Initialize database
            System.out.println("Initializing database...");
            DatabaseConfig.initializeDatabase();
            
            // Test database connection
            try (Connection conn = DatabaseConfig.getConnection()) {
                System.out.println("Database connection successful!");
            }
            
            // Test user creation
            UserDAO userDao = new UserDAO();
            User testUser = new User("testuser", "Test123", "test@example.com");
            
            System.out.println("Creating test user...");
            boolean userCreated = userDao.createUser(testUser);
            System.out.println("User created: " + userCreated);
            
            // Get the created user
            User retrievedUser = userDao.getUserByUsername("testuser");
            if (retrievedUser != null) {
                System.out.println("Retrieved user: " + retrievedUser.getUsername());
                
                // Test profile creation
                UserProfileDAO profileDao = new UserProfileDAO();
                UserProfile testProfile = new UserProfile(retrievedUser.getId(), "John", "Doe");
                
                System.out.println("Creating test profile...");
                boolean profileCreated = profileDao.createProfile(testProfile);
                System.out.println("Profile created: " + profileCreated);
                
                // Get the created profile
                UserProfile retrievedProfile = profileDao.getProfileByUserId(retrievedUser.getId());
                if (retrievedProfile != null) {
                    System.out.println("Retrieved profile: " + retrievedProfile.getFirstName() + " " + retrievedProfile.getLastName());
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("General error: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 