package com.example.EventPlanner.model.user;

import com.google.gson.annotations.SerializedName;

public class UserReport {

    @SerializedName("reportedUserId")
    private int reportedUserId;

    @SerializedName("reporterId")
    private int reporterId;

    @SerializedName("reason")
    private String reason;

    // Getters and setters (if not using Lombok)
    public int getReportedUserId() {
        return reportedUserId;
    }

    public void setReportedUserId(int reportedUserId) {
        this.reportedUserId = reportedUserId;
    }

    public int getReporterId() {
        return reporterId;
    }

    public void setReporterId(int reporterId) {
        this.reporterId = reporterId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
