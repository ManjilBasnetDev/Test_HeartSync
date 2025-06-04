package heartsyncdatingapp.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
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
    private static final String DB_NAME = "heart_sync_db";
    private static final String USERNAME = "manjil";
    private static final String PASSWORD = "3023";
    private static Connection connection = null;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final int MINIMUM_AGE = 18;
    private static boolean driverLoaded = false;
    
    static {
        loadDriver();
        initializeDatabase();
    }
    
    private static void loadDriver() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            driverLoaded = true;
            System.out.println("MySQL JDBC Driver loaded successfully");
        } catch (ClassNotFoundException e) {
            String error = "MySQL JDBC Driver not found.\nPlease ensure mysql-connector-j-8.0.33.jar is in the lib directory.";
            System.err.println(error);
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                error,
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private static void initializeDatabase() {
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
                    
                    // Create users table with proper date_of_birth format
                    String createUsersTableSQL = """
                        CREATE TABLE IF NOT EXISTS users (
                            id INT PRIMARY KEY AUTO_INCREMENT,
                            username VARCHAR(50) NOT NULL UNIQUE,
                            password VARCHAR(100) NOT NULL,
                            user_type VARCHAR(10) NOT NULL DEFAULT 'USER',
                            email VARCHAR(100),
                            phone_number VARCHAR(20),
                            date_of_birth DATE NOT NULL,
                            gender VARCHAR(10),
                            interests TEXT,
                            bio TEXT,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
                        )
                    """;
                    stmt.executeUpdate(createUsersTableSQL);
                    
                    // Create contacts table
                    String createContactsTableSQL = """
                        CREATE TABLE IF NOT EXISTS contacts (
                            id INT PRIMARY KEY AUTO_INCREMENT,
                            full_name VARCHAR(100) NOT NULL,
                            email VARCHAR(100) NOT NULL,
                            message TEXT NOT NULL,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                        )
                    """;
                    stmt.executeUpdate(createContactsTableSQL);
                    
                    // Create matches table for future use
                    String createMatchesTableSQL = """
                        CREATE TABLE IF NOT EXISTS matches (
                            id INT PRIMARY KEY AUTO_INCREMENT,
                            user1_id INT NOT NULL,
                            user2_id INT NOT NULL,
                            match_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            status ENUM('PENDING', 'ACCEPTED', 'REJECTED') DEFAULT 'PENDING',
                            FOREIGN KEY (user1_id) REFERENCES users(id),
                            FOREIGN KEY (user2_id) REFERENCES users(id)
                        )
                    """;
                    stmt.executeUpdate(createMatchesTableSQL);
                    
                    // Create messages table for future use
                    String createMessagesTableSQL = """
                        CREATE TABLE IF NOT EXISTS messages (
                            id INT PRIMARY KEY AUTO_INCREMENT,
                            sender_id INT NOT NULL,
                            receiver_id INT NOT NULL,
                            message_text TEXT NOT NULL,
                            sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            read_at TIMESTAMP NULL,
                            FOREIGN KEY (sender_id) REFERENCES users(id),
                            FOREIGN KEY (receiver_id) REFERENCES users(id)
                        )
                    """;
                    stmt.executeUpdate(createMessagesTableSQL);
                    
                    System.out.println("Database and tables initialized successfully");
                }
            }
        } catch (SQLException e) {
            String error = "Database initialization error: " + e.getMessage();
            System.err.println(error);
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                error,
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public static Connection getConnection() throws SQLException {
        if (!driverLoaded) {
            throw new SQLException("MySQL JDBC Driver not loaded");
        }
        
        if (connection == null || connection.isClosed()) {
            try {
                String fullUrl = URL + DB_NAME;
                connection = DriverManager.getConnection(fullUrl, USERNAME, PASSWORD);
                if (connection == null) {
                    throw new SQLException("Failed to establish database connection.");
                }
                System.out.println("Database connected successfully");
            } catch (SQLException e) {
                String error = "Database connection error: " + e.getMessage();
                System.err.println(error);
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,
                    error,
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
                throw e;
            }
        }
        return connection;
    }
    
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("Database connection closed");
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