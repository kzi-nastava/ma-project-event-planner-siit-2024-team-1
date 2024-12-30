package com.example.EventPlanner.model.event;

import java.util.List;
import javax.annotation.Generated;

import com.example.EventPlanner.model.common.Address;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class UpdateEventRequest {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("maxParticipants")
    @Expose
    private Integer maxParticipants;
    @SerializedName("isPublic")
    @Expose
    private Boolean isPublic;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("address")
    @Expose
    private Address address;
    @SerializedName("eventTypeId")
    @Expose
    private Integer eventTypeId;
    @SerializedName("productIds")
    @Expose
    private List<Integer> productIds;
    @SerializedName("serviceIds")
    @Expose
    private List<Integer> serviceIds;

    /**
     * No args constructor for use in serialization
     *
     */
    public UpdateEventRequest() {
    }

    public UpdateEventRequest(String title, String description, Integer maxParticipants, Boolean isPublic, String date, Address address, Integer eventTypeId, List<Integer> productIds, List<Integer> serviceIds) {
        super();
        this.title = title;
        this.description = description;
        this.maxParticipants = maxParticipants;
        this.isPublic = isPublic;
        this.date = date;
        this.address = address;
        this.eventTypeId = eventTypeId;
        this.productIds = productIds;
        this.serviceIds = serviceIds;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(Integer maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Integer getEventTypeId() {
        return eventTypeId;
    }

    public void setEventTypeId(Integer eventTypeId) {
        this.eventTypeId = eventTypeId;
    }

    public List<Integer> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<Integer> productIds) {
        this.productIds = productIds;
    }

    public List<Integer> getServiceIds() {
        return serviceIds;
    }

    public void setServiceIds(List<Integer> serviceIds) {
        this.serviceIds = serviceIds;
    }
}