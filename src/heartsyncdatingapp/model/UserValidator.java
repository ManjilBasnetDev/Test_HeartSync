package heartsyncdatingapp.model;

/**
 * Validator class for User model.
 * Contains all validation logic for user-related data.
 */
public class UserValidator {
    public static String sanitizeInput(String input) {
        if (input == null) return "";
        return input.trim();
    }
    
    public static boolean isValidUsername(String username) {
        if (username == null || username.trim().isEmpty()) return false;
        return username.trim().length() >= 3;
    }
    
    public static boolean isValidPassword(String password) {
        if (password == null || password.length() < 8) return false;
        
        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasNumber = false;
        boolean hasSpecial = false;
        
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isLowerCase(c)) hasLower = true;
            else if (Character.isDigit(c)) hasNumber = true;
            else if ("!@#$%^&*()_+-=[]{}|;:,.<>?".indexOf(c) >= 0) hasSpecial = true;
        }
        
        return hasUpper && hasLower && hasNumber && hasSpecial;
    }
    
    public static boolean isValidSecurityAnswer(String answer) {
        if (answer == null || answer.trim().isEmpty()) return false;
        if (answer.equals("FAVORITE COLOR?") || answer.equals("FIRST SCHOOL?")) return false;
        return answer.trim().length() >= 2;
    }
    
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) return false;
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
    
    public static boolean isValidPhoneNumber(String phone) {
        if (phone == null || phone.trim().isEmpty()) return false;
        return phone.matches("^\\+?[0-9]{10,15}$");
    }
    
    public static String getPasswordRequirements() {
        return "Password must:\n" +
               "- Be at least 8 characters long\n" +
               "- Contain at least one uppercase letter\n" +
               "- Contain at least one lowercase letter\n" +
               "- Contain at least one number\n" +
               "- Contain at least one special character (!@#$%^&*()_+-=[]{}|;:,.<>?)";
    }
} 