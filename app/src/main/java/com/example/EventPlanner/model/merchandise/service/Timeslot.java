package com.example.EventPlanner.model.merchandise.service;

import com.google.gson.annotations.SerializedName;

public class Timeslot {

    @SerializedName("id")
    private Long id;

    @SerializedName("startTime")
    private String startTime; // Use String to represent LocalDateTime

    @SerializedName("endTime")
    private String endTime; // Use String to represent LocalDateTime

    // Default constructor
    public Timeslot() {
    }

    // Constructor for initializing with startTime and endTime
    public Timeslot(String startTime, String endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
