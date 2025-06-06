package forgotpasswordfinal.model;

public class User {
    private int id;
    private String username;
    private String password;
    private String favoriteColor;
    private String firstSchool;
    
    public User() {}
    
    public User(String username, String password, String favoriteColor, String firstSchool) {
        this.username = username;
        this.password = password;
        this.favoriteColor = favoriteColor;
        this.firstSchool = firstSchool;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getFavoriteColor() {
        return favoriteColor;
    }
    
    public void setFavoriteColor(String favoriteColor) {
        this.favoriteColor = favoriteColor;
    }
    
    public String getFirstSchool() {
        return firstSchool;
    }
    
    public void setFirstSchool(String firstSchool) {
        this.firstSchool = firstSchool;
    }
} 