package forgotpasswordfinal.validation;

public class InputValidator {
    
    public static boolean isValidUsername(String username) {
        return username != null && !username.trim().isEmpty() && username.length() >= 3;
    }
    
    public static boolean isValidPassword(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        
        boolean hasUpperCase = false;
        boolean hasLowerCase = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;
        
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpperCase = true;
            else if (Character.isLowerCase(c)) hasLowerCase = true;
            else if (Character.isDigit(c)) hasDigit = true;
            else hasSpecialChar = true;
        }
        
        return hasUpperCase && hasLowerCase && hasDigit && hasSpecialChar;
    }
    
    public static boolean isValidSecurityAnswer(String answer) {
        return answer != null && !answer.trim().isEmpty() && answer.length() >= 2;
    }
    
    public static String sanitizeInput(String input) {
        if (input == null) return "";
        // Remove any HTML tags
        input = input.replaceAll("<[^>]*>", "");
        // Remove any SQL injection attempts
        input = input.replaceAll("['\"\\\\]", "");
        return input.trim();
    }
} 