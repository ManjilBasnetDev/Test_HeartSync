package heartsyncdatingapp.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 * Database connection manager for MySQL database.
 * Handles database creation, connection management, and connection testing.
 */
public class MySqlConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "datingapp";
    private static final String USERNAME = "manjil";
    private static final String PASSWORD = "3023";
    
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
                    
                    // Create users table
                    String createUsersTableSQL = """
                        CREATE TABLE IF NOT EXISTS users (
                            id INT PRIMARY KEY AUTO_INCREMENT,
                            username VARCHAR(50) NOT NULL UNIQUE,
                            password VARCHAR(100) NOT NULL,
                            email VARCHAR(100) NOT NULL,
                            security_question VARCHAR(200),
                            security_answer VARCHAR(200),
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                        )
                    """;
                    stmt.executeUpdate(createUsersTableSQL);
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
        try {
            String fullUrl = URL + DB_NAME;
            Connection conn = DriverManager.getConnection(fullUrl, USERNAME, PASSWORD);
            if (conn == null) {
                throw new SQLException("Failed to establish database connection.");
            }
            return conn;
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
    
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && conn.isValid(5); // 5 second timeout
        } catch (SQLException e) {
            String error = "Database connection test failed: " + e.getMessage();
            System.err.println(error);
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                error,
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
} 