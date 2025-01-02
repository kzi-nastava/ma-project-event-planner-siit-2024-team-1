package com.example.EventPlanner.model.user;

import com.google.gson.annotations.SerializedName;

public class UserReportResponse {

    @SerializedName("id")
    private int id;

    @SerializedName("reportedUserId")
    private int reportedUserId;

    @SerializedName("reporterId")
    private int reporterId;

    @SerializedName("reason")
    private String reason;

    @SerializedName("status")
    private String status; // Assuming this is an enum or valid type

    // Getters and setters (if not using Lombok)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
