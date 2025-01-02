package com.example.EventPlanner.model.common;

import com.google.gson.annotations.SerializedName;
import java.time.LocalDateTime;

public class ReviewOverview {
    @SerializedName("id")
    private int id;

    @SerializedName("comment")
    private String comment;

    @SerializedName("rating")
    private int rating;

    @SerializedName("status")
    private String status;

    @SerializedName("reviewedType")
    private String reviewedType;

    @SerializedName("reviewedTitle")
    private String reviewedTitle;

    @SerializedName("reviewerUsername")
    private String reviewerUsername;

    @SerializedName("createdAt")
    private String createdAt; // changed to String for Gson compatibility

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReviewedType() {
        return reviewedType;
    }

    public void setReviewedType(String reviewedType) {
        this.reviewedType = reviewedType;
    }

    public String getReviewedTitle() {
        return reviewedTitle;
    }

    public void setReviewedTitle(String reviewedTitle) {
        this.reviewedTitle = reviewedTitle;
    }

    public String getReviewerUsername() {
        return reviewerUsername;
    }

    public void setReviewerUsername(String reviewerUsername) {
        this.reviewerUsername = reviewerUsername;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
