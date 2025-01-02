package com.example.EventPlanner.model.user;

import com.google.gson.annotations.SerializedName;

public class UserReportOverview {

    @SerializedName("id")
    private int id;

    @SerializedName("reportedUserId")
    private int reportedUserId;

    @SerializedName("reporterId")
    private int reporterId;

    @SerializedName("reporterEmail")
    private String reporterEmail;

    @SerializedName("reportedUserName")
    private String reportedUserName;

    @SerializedName("reportedUserSurname")
    private String reportedUserSurname;

    @SerializedName("reportedUserEmail")
    private String reportedUserEmail;

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

    public String getReporterEmail() {
        return reporterEmail;
    }

    public void setReporterEmail(String reporterEmail) {
        this.reporterEmail = reporterEmail;
    }

    public String getReportedUserName() {
        return reportedUserName;
    }

    public void setReportedUserName(String reportedUserName) {
        this.reportedUserName = reportedUserName;
    }

    public String getReportedUserSurname() {
        return reportedUserSurname;
    }

    public void setReportedUserSurname(String reportedUserSurname) {
        this.reportedUserSurname = reportedUserSurname;
    }

    public String getReportedUserEmail() {
        return reportedUserEmail;
    }

    public void setReportedUserEmail(String reportedUserEmail) {
        this.reportedUserEmail = reportedUserEmail;
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

