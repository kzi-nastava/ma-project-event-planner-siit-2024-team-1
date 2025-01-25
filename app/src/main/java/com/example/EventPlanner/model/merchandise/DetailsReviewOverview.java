package com.example.EventPlanner.model.merchandise;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class DetailsReviewOverview {
    @SerializedName("reviewersUsername")
    @Expose
    private String reviewersUsername;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("rating")
    @Expose
    private Integer rating;

    public DetailsReviewOverview() {}
    public DetailsReviewOverview(String reviewersUsername, String comment, Integer rating) {
        this.reviewersUsername = reviewersUsername;
        this.comment = comment;
        this.rating = rating;
    }

    public String getReviewersUsername() {
        return reviewersUsername;
    }

    public String getComment() {
        return comment;
    }

    public Integer getRating() {
        return rating;
    }

    public void setReviewersUsername(String reviewersUsername) {
        this.reviewersUsername = reviewersUsername;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
}
