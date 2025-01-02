package com.example.EventPlanner.model.user;

import com.google.gson.annotations.SerializedName;

public class UserSuspension {

    @SerializedName("id")
    private Long id;

    @SerializedName("userId")
    private int userId;

    @SerializedName("startTime")
    private String startTime; // Using String for JSON compatibility

    @SerializedName("endTime")
    private String endTime; // Using String for JSON compatibility

    @SerializedName("reason")
    private String reason;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}

