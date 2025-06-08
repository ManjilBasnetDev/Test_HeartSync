package heartsyncdatingapp.database;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Unified database connection manager for the HeartSync Dating App.
 * Handles all database operations including initialization and connection management.
 */
public class DatabaseConnection {
    private static final Logger LOGGER = Logger.getLogger(DatabaseConnection.class.getName());
    private static Connection connection = null;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final int MINIMUM_AGE = 18;
    private static final int PASSWORD_MIN_LENGTH = 8;
    
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            LOGGER.info("MySQL JDBC Driver loaded successfully");
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "MySQL JDBC Driver not found", e);
            throw new RuntimeException("MySQL JDBC Driver not found", e);
        }
        
        try {
            // Test initial connection and create database if needed
            try (Connection tempConn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/",
                    DatabaseConfig.getDbUser(),
                    DatabaseConfig.getDbPassword())) {
                
                // Create database if it doesn't exist
                try (Statement stmt = tempConn.createStatement()) {
                    stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS TestHeartSync");
                    LOGGER.info("Database 'TestHeartSync' verified/created");
                }
            }
            
            // Initialize tables
            initializeDatabase();
            LOGGER.info("Database initialization completed successfully");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to initialize database", e);
            throw new RuntimeException("Failed to initialize database", e);
        }
    }
    
    public static synchronized Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                connection = DriverManager.getConnection(
                    DatabaseConfig.getDbUrl(),
                    DatabaseConfig.getDbUser(),
                    DatabaseConfig.getDbPassword()
                );
                connection.setAutoCommit(true);
                LOGGER.info("Database connection established successfully");
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Failed to establish database connection", e);
                throw e;
            }
        }
        return connection;
    }
    
    public static void initializeDatabase() throws SQLException {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Create users table with all necessary fields
            String createTableSQL = 
                "CREATE TABLE IF NOT EXISTS users (" +
                "id INT PRIMARY KEY AUTO_INCREMENT, " +
                "username VARCHAR(50) NOT NULL UNIQUE, " +
                "password VARCHAR(255) NOT NULL, " +
                "user_type VARCHAR(10) NOT NULL, " +
                "date_of_birth DATE NOT NULL, " +
                "favorite_color VARCHAR(50) NOT NULL, " +
                "first_school VARCHAR(100) NOT NULL, " +
                "email VARCHAR(100), " +
                "phone_number VARCHAR(20), " +
                "gender VARCHAR(20), " +
                "interests TEXT, " +
                "bio TEXT, " +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, " +
                "INDEX idx_username (username), " +  // Add index for username lookups
                "INDEX idx_email (email)" +          // Add index for email lookups
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci";
            
            stmt.executeUpdate(createTableSQL);
            LOGGER.info("Users table created/verified successfully");
            
            // Create contacts table
            String createContactsTableSQL = 
                "CREATE TABLE IF NOT EXISTS contacts (" +
                "id INT PRIMARY KEY AUTO_INCREMENT, " +
                "full_name VARCHAR(100) NOT NULL, " +
                "email VARCHAR(100) NOT NULL, " +
                "message TEXT NOT NULL, " +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "INDEX idx_email (email)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci";
            
            stmt.executeUpdate(createContactsTableSQL);
            LOGGER.info("Contacts table created/verified successfully");
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating database tables", e);
            throw e;
        }
    }
    
    public static void closeConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                    connection = null;
                    LOGGER.info("Database connection closed successfully");
                }
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Error closing database connection", e);
            }
        }
    }
    
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            if (conn != null && conn.isValid(5)) { // 5 second timeout
                LOGGER.info("Database connection test successful");
                return true;
            }
            LOGGER.warning("Database connection test failed - connection invalid");
            return false;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database connection test failed", e);
            return false;
        }
    }
    
    public static void validateAge(String dateOfBirth) {
        try {
            LocalDate dob = LocalDate.parse(dateOfBirth, DATE_FORMATTER);
            LocalDate now = LocalDate.now();
            int age = Period.between(dob, now).getYears();
            
            if (age < MINIMUM_AGE) {
                throw new IllegalArgumentException("You must be at least " + MINIMUM_AGE + " years old to register.");
            }
            if (dob.isAfter(now)) {
                throw new IllegalArgumentException("Date of birth cannot be in the future.");
            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Please use YYYY-MM-DD format.");
        }
    }
    
    public static LocalDate parseAndValidateDate(String dateOfBirth) throws IllegalArgumentException {
        validateAge(dateOfBirth);
        return LocalDate.parse(dateOfBirth, DATE_FORMATTER);
    }
    
    public static int calculateAge(LocalDate dateOfBirth) {
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }
    
    /**
     * Validates a password against security requirements.
     * @param password The password to validate
     * @throws IllegalArgumentException if the password doesn't meet requirements
     */
    public static void validatePassword(String password) {
        if (password == null || password.length() < PASSWORD_MIN_LENGTH) {
            throw new IllegalArgumentException("Password must be at least " + PASSWORD_MIN_LENGTH + " characters long");
        }
        
        if (!password.matches(".*[A-Z].*")) {
            throw new IllegalArgumentException("Password must contain at least one uppercase letter");
        }
        
        if (!password.matches(".*[0-9].*")) {
            throw new IllegalArgumentException("Password must contain at least one number");
        }
        
        if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) {
            throw new IllegalArgumentException("Password must contain at least one special character");
        }
    }
    
    /**
     * Hash a password using SHA-256.
     * @param password The plain text password
     * @return The hashed password
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.log(Level.SEVERE, "Error hashing password", e);
            throw new RuntimeException("Error hashing password", e);
        }
    }
    
    /**
     * Validates an email address format.
     * @param email The email to validate
     * @throws IllegalArgumentException if the email format is invalid
     */
    public static void validateEmail(String email) {
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }
    
    /**
     * Validates a phone number format.
     * @param phone The phone number to validate
     * @throws IllegalArgumentException if the phone format is invalid
     */
    public static void validatePhone(String phone) {
        if (phone == null || !phone.matches("^\\+?[0-9]{10,15}$")) {
            throw new IllegalArgumentException("Invalid phone number format");
        }
    }
}