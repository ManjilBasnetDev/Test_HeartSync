package logintestfinal.model;

public class FilterCriteria {
    private String gender;
    private String educationLevel;
    private String preferences;
    private int ageRange;

    public FilterCriteria() {
    }

    public FilterCriteria(String gender, String educationLevel, String preferences, int ageRange) {
        this.gender = gender;
        this.educationLevel = educationLevel;
        this.preferences = preferences;
        this.ageRange = ageRange;
    }

    // Getters and Setters
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEducationLevel() {
        return educationLevel;
    }

    public void setEducationLevel(String educationLevel) {
        this.educationLevel = educationLevel;
    }

    public String getPreferences() {
        return preferences;
    }

    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }

    public int getAgeRange() {
        return ageRange;
    }

    public void setAgeRange(int ageRange) {
        this.ageRange = ageRange;
    }
} 