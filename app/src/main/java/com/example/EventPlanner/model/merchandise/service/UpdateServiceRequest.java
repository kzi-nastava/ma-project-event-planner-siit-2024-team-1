package com.example.EventPlanner.model.merchandise.service;

import com.example.EventPlanner.model.common.Address;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class UpdateServiceRequest {
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("specificity")
    @Expose
    private String specificity;
    @SerializedName("price")
    @Expose
    private Double price;
    @SerializedName("discount")
    @Expose
    private Integer discount;
    @SerializedName("eventTypesIds")
    @Expose
    private List<Integer> eventTypesIds;
    @SerializedName("minDuration")
    @Expose
    private Integer minDuration;
    @SerializedName("maxDuration")
    @Expose
    private Integer maxDuration;
    @SerializedName("reservationDeadline")
    @Expose
    private Integer reservationDeadline;
    @SerializedName("cancellationDeadline")
    @Expose
    private Integer cancellationDeadline;
    @SerializedName("automaticReservation")
    @Expose
    private Boolean automaticReservation;
    @SerializedName("visible")
    @Expose
    private Boolean visible;
    @SerializedName("available")
    @Expose
    private Boolean available;
    @SerializedName("merchandisePhotos")
    @Expose
    private List<Integer> merchandisePhotos;
    @SerializedName("serviceProviderId")
    @Expose
    private Integer serviceProviderId;
    @SerializedName("address")
    @Expose
    private Address address;

    public UpdateServiceRequest() { }

    public UpdateServiceRequest(String title, String description, String specificity, Double price, Integer discount, List<Integer> eventTypesIds, Integer minDuration, Integer maxDuration, Integer reservationDeadline, Integer cancellationDeadline, Boolean automaticReservation, Boolean visible, Boolean available, List<Integer> merchandisePhotos, Integer serviceProviderId, Address address) {
        this.title = title;
        this.description = description;
        this.specificity = specificity;
        this.price = price;
        this.discount = discount;
        this.eventTypesIds = eventTypesIds;
        this.minDuration = minDuration;
        this.maxDuration = maxDuration;
        this.reservationDeadline = reservationDeadline;
        this.cancellationDeadline = cancellationDeadline;
        this.automaticReservation = automaticReservation;
        this.visible = visible;
        this.available = available;
        this.merchandisePhotos = merchandisePhotos;
        this.serviceProviderId = serviceProviderId;
        this.address = address;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getSpecificity() {
        return specificity;
    }

    public Double getPrice() {
        return price;
    }

    public Integer getDiscount() {
        return discount;
    }

    public List<Integer> getEventTypesIds() {
        return eventTypesIds;
    }

    public Integer getMinDuration() {
        return minDuration;
    }

    public Integer getMaxDuration() {
        return maxDuration;
    }

    public Integer getReservationDeadline() {
        return reservationDeadline;
    }

    public Integer getCancellationDeadline() {
        return cancellationDeadline;
    }

    public Boolean getAutomaticReservation() {
        return automaticReservation;
    }

    public Boolean getVisible() {
        return visible;
    }

    public Boolean getAvailable() {
        return available;
    }

    public List<Integer> getMerchandisePhotos() {
        return merchandisePhotos;
    }

    public Integer getServiceProviderId() {
        return serviceProviderId;
    }

    public Address getAddress() {
        return address;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSpecificity(String specificity) {
        this.specificity = specificity;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public void setEventTypesIds(List<Integer> eventTypesIds) {
        this.eventTypesIds = eventTypesIds;
    }

    public void setMinDuration(Integer minDuration) {
        this.minDuration = minDuration;
    }

    public void setMaxDuration(Integer maxDuration) {
        this.maxDuration = maxDuration;
    }

    public void setReservationDeadline(Integer reservationDeadline) {
        this.reservationDeadline = reservationDeadline;
    }

    public void setCancellationDeadline(Integer cancellationDeadline) {
        this.cancellationDeadline = cancellationDeadline;
    }

    public void setAutomaticReservation(Boolean automaticReservation) {
        this.automaticReservation = automaticReservation;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public void setMerchandisePhotos(List<Integer> merchandisePhotos) {
        this.merchandisePhotos = merchandisePhotos;
    }

    public void setServiceProviderId(Integer serviceProviderId) {
        this.serviceProviderId = serviceProviderId;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
