package com.example.EventPlanner.model.merchandise.service;

import com.google.gson.annotations.SerializedName;

public class ReservationRequest {
    @SerializedName("eventId")
    private int eventId;

    @SerializedName("organizerId")
    private int organizerId;

    @SerializedName("startTime")
    private String startTime; // Changed to String

    @SerializedName("endTime")
    private String endTime; // Changed to String (optional)

    // Getters and setters
    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(int organizerId) {
        this.organizerId = organizerId;
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
}
