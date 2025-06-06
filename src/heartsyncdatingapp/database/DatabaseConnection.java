package heartsyncdatingapp.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Unified database connection manager for the HeartSync Dating App.
 * Handles all database operations including initialization and connection management.
 */
public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "heartsync_db";
    private static final String USERNAME = "manjil";
    private static final String PASSWORD = "3023";
    private static boolean driverLoaded = false;
    private static Connection connection = null;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final int MINIMUM_AGE = 18;
    
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            driverLoaded = true;
            initializeDatabase();
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }
    
    public static Connection getConnection() throws SQLException {
        if (!driverLoaded) {
            throw new SQLException("MySQL JDBC Driver not loaded");
        }
        
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL + DB_NAME, USERNAME, PASSWORD);
        }
        
        return connection;
    }
    
    private static void initializeDatabase() throws SQLException {
        if (!driverLoaded) {
            return;
        }
        
        try {
            // First try connecting to MySQL server
            try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
                try (Statement stmt = conn.createStatement()) {
                    // Create database if it doesn't exist
                    stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_NAME);
                    
                    // Use the database
                    stmt.executeUpdate("USE " + DB_NAME);
                    
                    // Create users table with all necessary fields
                    String createUsersTableSQL = """
                        CREATE TABLE IF NOT EXISTS users (
                            id INT PRIMARY KEY AUTO_INCREMENT,
                            username VARCHAR(50) NOT NULL UNIQUE,
                            password VARCHAR(255) NOT NULL,
                            user_type ENUM('USER', 'ADMIN') NOT NULL DEFAULT 'USER',
                            email VARCHAR(100) UNIQUE,
                            phone_number VARCHAR(20),
                            date_of_birth DATE,
                            gender VARCHAR(10),
                            interests TEXT,
                            bio TEXT,
                            account_status ENUM('ACTIVE', 'INACTIVE', 'SUSPENDED') DEFAULT 'ACTIVE',
                            last_login TIMESTAMP NULL,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
                        )
                    """;
                    stmt.executeUpdate(createUsersTableSQL);
                    
                    // Create user_profiles table for detailed information
                    String createUserProfilesTableSQL = """
                        CREATE TABLE IF NOT EXISTS user_profiles (
                            id INT PRIMARY KEY AUTO_INCREMENT,
                            user_id INT NOT NULL UNIQUE,
                            full_name VARCHAR(100),
                            height INT,
                            weight INT,
                            country VARCHAR(100),
                            address TEXT,
                            qualification VARCHAR(255),
                            occupation VARCHAR(255),
                            religion VARCHAR(100),
                            ethnicity VARCHAR(100),
                            relationship_goal VARCHAR(100),
                            interests TEXT,
                            languages TEXT,
                            about_me TEXT,
                            profile_pic_path VARCHAR(255),
                            preferences TEXT,
                            profile_status ENUM('INCOMPLETE', 'COMPLETE') DEFAULT 'INCOMPLETE',
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                            FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
                        )
                    """;
                    stmt.executeUpdate(createUserProfilesTableSQL);
                    
                    // Create user_hobbies table
                    String createHobbiesTableSQL = """
                        CREATE TABLE IF NOT EXISTS user_hobbies (
                            id INT PRIMARY KEY AUTO_INCREMENT,
                            user_id INT NOT NULL,
                            hobby VARCHAR(100) NOT NULL,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
                        )
                    """;
                    stmt.executeUpdate(createHobbiesTableSQL);
                    
                    // Create contacts table with enhanced fields
                    String createContactsTableSQL = """
                        CREATE TABLE IF NOT EXISTS contacts (
                            id INT PRIMARY KEY AUTO_INCREMENT,
                            full_name VARCHAR(100) NOT NULL,
                            email VARCHAR(100) NOT NULL,
                            phone_number VARCHAR(20),
                            subject VARCHAR(200),
                            message TEXT NOT NULL,
                            status ENUM('NEW', 'READ', 'REPLIED') DEFAULT 'NEW',
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
                        )
                    """;
                    stmt.executeUpdate(createContactsTableSQL);
                    
                    // Create matches table with enhanced fields
                    String createMatchesTableSQL = """
                        CREATE TABLE IF NOT EXISTS matches (
                            id INT PRIMARY KEY AUTO_INCREMENT,
                            user1_id INT NOT NULL,
                            user2_id INT NOT NULL,
                            match_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            status ENUM('PENDING', 'ACCEPTED', 'REJECTED', 'BLOCKED') DEFAULT 'PENDING',
                            compatibility_score DECIMAL(5,2),
                            notes TEXT,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                            FOREIGN KEY (user1_id) REFERENCES users(id) ON DELETE CASCADE,
                            FOREIGN KEY (user2_id) REFERENCES users(id) ON DELETE CASCADE
                        )
                    """;
                    stmt.executeUpdate(createMatchesTableSQL);
                    
                    // Create messages table with enhanced fields
                    String createMessagesTableSQL = """
                        CREATE TABLE IF NOT EXISTS messages (
                            id INT PRIMARY KEY AUTO_INCREMENT,
                            sender_id INT NOT NULL,
                            receiver_id INT NOT NULL,
                            message_text TEXT NOT NULL,
                            message_type ENUM('TEXT', 'IMAGE', 'FILE') DEFAULT 'TEXT',
                            attachment_path VARCHAR(255),
                            is_read BOOLEAN DEFAULT FALSE,
                            sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            read_at TIMESTAMP NULL,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                            FOREIGN KEY (sender_id) REFERENCES users(id) ON DELETE CASCADE,
                            FOREIGN KEY (receiver_id) REFERENCES users(id) ON DELETE CASCADE
                        )
                    """;
                    stmt.executeUpdate(createMessagesTableSQL);

                    // Insert default admin user if not exists
                    String insertAdminSQL = """
                        INSERT IGNORE INTO users (username, password, user_type, email)
                        VALUES ('admin', 'admin123', 'ADMIN', 'admin@heartsync.com')
                    """;
                    stmt.executeUpdate(insertAdminSQL);
                    
                    System.out.println("Database and tables initialized successfully");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
            throw e;
        }
    }
    
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
    
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && conn.isValid(5); // 5 second timeout
        } catch (SQLException e) {
            String error = "Database connection test failed: " + e.getMessage();
            System.err.println(error);
            e.printStackTrace();
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
}