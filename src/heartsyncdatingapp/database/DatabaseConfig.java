package heartsyncdatingapp.database;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Configuration class for database connection settings.
 * Loads settings from a properties file or environment variables.
 */
public class DatabaseConfig {
    private static final String CONFIG_FILE = "database.properties";
    private static Properties properties = new Properties();
    
    static {
        loadConfig();
    }
    
    private static void loadConfig() {
        try {
            // Try to load from properties file first
            try (FileInputStream fis = new FileInputStream(CONFIG_FILE)) {
                properties.load(fis);
            } catch (IOException e) {
                // If file not found, use environment variables or defaults
                properties.setProperty("db.url", getEnvOrDefault("DB_URL", "jdbc:mysql://localhost:3306/TestHeartSync"));
                properties.setProperty("db.user", getEnvOrDefault("DB_USER", "manjil"));
                properties.setProperty("db.password", getEnvOrDefault("DB_PASSWORD", "3023"));
            }
        } catch (Exception e) {
            System.err.println("Error loading database configuration: " + e.getMessage());
            // Use hardcoded defaults as last resort
            properties.setProperty("db.url", "jdbc:mysql://localhost:3306/TestHeartSync");
            properties.setProperty("db.user", "manjil");
            properties.setProperty("db.password", "3023");
        }
    }
    
    private static String getEnvOrDefault(String envVar, String defaultValue) {
        String value = System.getenv(envVar);
        return value != null ? value : defaultValue;
    }
    
    public static String getDbUrl() {
        return properties.getProperty("db.url");
    }
    
    public static String getDbUser() {
        return properties.getProperty("db.user");
    }
    
    public static String getDbPassword() {
        return properties.getProperty("db.password");
    }
} 