package usersetup.database;

public class TestConnection {
    public static void main(String[] args) {
        try {
            // Try loading the driver class explicitly
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MySQL JDBC driver loaded successfully!");
            
            // Get database connection
            DatabaseConnection connection = DatabaseConnection.getInstance();
            connection.getConnection();
            System.out.println("Successfully connected to the database!");
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 