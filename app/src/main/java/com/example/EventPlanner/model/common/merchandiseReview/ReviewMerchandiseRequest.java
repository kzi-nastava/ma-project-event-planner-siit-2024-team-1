package com.example.EventPlanner.model.common.merchandiseReview;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class ReviewMerchandiseRequest {
    @SerializedName("reviewerId")
    @Expose
    private Integer reviewerId;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("raing")
    @Expose
    private Integer rating;
    @SerializedName("type")
    @Expose
    private String type;

    public ReviewMerchandiseRequest() {}

    public ReviewMerchandiseRequest(Integer reviewerId, String comment, Integer rating, String type) {
        this.reviewerId = reviewerId;
        this.comment = comment;
        this.rating = rating;
        this.type = type;
    }

    public Integer getReviewerId() {
        return reviewerId;
    }

    public String getComment() {
        return comment;
    }

    public Integer getRating() {
        return rating;
    }

    public String getType() {
        return type;
    }

    public void setReviewerId(Integer reviewerId) {
        this.reviewerId = reviewerId;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public void setType(String type) {
        this.type = type;
    }
}
