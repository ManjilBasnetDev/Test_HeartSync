package heartsyncdatingapp.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

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
    
    static {
        initializeDatabase();
    }
    
    private static void initializeDatabase() {
        try {
            // First try connecting to MySQL server
            try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
                try (Statement stmt = conn.createStatement()) {
                    // Create database if it doesn't exist
                    stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_NAME);
                    
                    // Use the database
                    stmt.executeUpdate("USE " + DB_NAME);
                    
                    // Create users table
                    String createUsersTableSQL = """
                        CREATE TABLE IF NOT EXISTS users (
                            id INT PRIMARY KEY AUTO_INCREMENT,
                            username VARCHAR(50) NOT NULL UNIQUE,
                            password VARCHAR(100) NOT NULL,
                            user_type VARCHAR(10) NOT NULL DEFAULT 'USER',
                            email VARCHAR(100),
                            phone_number VARCHAR(20),
                            date_of_birth VARCHAR(20),
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
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                String fullUrl = URL + DB_NAME;
                connection = DriverManager.getConnection(fullUrl, USERNAME, PASSWORD);
                if (connection == null) {
                    throw new SQLException("Failed to establish database connection.");
                }
                System.out.println("Database connected successfully");
            } catch (ClassNotFoundException e) {
                throw new SQLException("Database driver not found", e);
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
} 