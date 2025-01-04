package com.example.EventPlanner.model.merchandise;

import com.google.gson.annotations.SerializedName;

public class MerchandiseReviewOverview {
    @SerializedName("reviewersUsername")
    private String reviewersUsername;

    @SerializedName("comment")
    private String comment;

    @SerializedName("rating")
    private int rating;

    // Getters and setters (if needed)
    public String getReviewersUsername() {
        return reviewersUsername;
    }

    public void setReviewersUsername(String reviewersUsername) {
        this.reviewersUsername = reviewersUsername;
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
}

