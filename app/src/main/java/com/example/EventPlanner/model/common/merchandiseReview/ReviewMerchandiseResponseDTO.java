package com.example.EventPlanner.model.common.merchandiseReview;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class ReviewMerchandiseResponseDTO {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("rating")
    @Expose
    private Integer rating;
    @SerializedName("reviewerId")
    @Expose
    private Integer reviewerId;
    @SerializedName("status")
    @Expose
    private ReviewStatus status;

    public ReviewMerchandiseResponseDTO() {}
}
