package com.example.EventPlanner.model.event;

import com.example.EventPlanner.model.common.Address;
import com.example.EventPlanner.model.merchandise.DetailsReviewOverview;
import com.example.EventPlanner.model.user.EventOrganizer;
import com.example.EventPlanner.model.user.EventOrganizerDTO;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;
import java.util.List;

import javax.annotation.Generated;


@Generated("jsonschema2pojo")
public class EventDetailsDTO {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("address")
    @Expose
    private Address address;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("maxParticipants")
    @Expose
    private Integer maxParticipants;
    @SerializedName("eventType")
    @Expose
    private EventTypeOverview eventType;
    @SerializedName("isPublic")
    @Expose
    private Boolean isPublic;
    @SerializedName("organizer")
    @Expose
    private EventOrganizerDTO organizer;
    @SerializedName("reviews")
    @Expose
    private List<DetailsReviewOverview> reviews;

    public List<DetailsReviewOverview> getReviews() {
        return reviews;
    }
}
