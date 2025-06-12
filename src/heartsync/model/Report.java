package heartsync.model;

import java.sql.Timestamp;

public class Report {
    private int id;
    private int reporterId;
    private int reportedUserId;
    private String reason;
    private String description;
    private String status;
    private Timestamp createdAt;
    
    // Default constructor
    public Report() {}
    
    // Constructor with main fields
    public Report(int reporterId, int reportedUserId, String reason, String description) {
        this.reporterId = reporterId;
        this.reportedUserId = reportedUserId;
        this.reason = reason;
        this.description = description;
        this.status = "PENDING"; // Default status
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getReporterId() {
        return reporterId;
    }
    
    public void setReporterId(int reporterId) {
        this.reporterId = reporterId;
    }
    
    public int getReportedUserId() {
        return reportedUserId;
    }
    
    public void setReportedUserId(int reportedUserId) {
        this.reportedUserId = reportedUserId;
    }
    
    public String getReason() {
        return reason;
    }
    
    public void setReason(String reason) {
        this.reason = reason;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
} 