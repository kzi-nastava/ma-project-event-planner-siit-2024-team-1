package com.example.EventPlanner.model.merchandise;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class DetailsReviewOverview {
    @SerializedName("reviewersUserName")
    @Expose
    private String reviewersUserName;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("rating")
    @Expose
    private Integer rating;
}
