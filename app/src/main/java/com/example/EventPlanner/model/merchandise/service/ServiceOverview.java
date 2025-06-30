package com.example.EventPlanner.model.merchandise.service;

import com.example.EventPlanner.model.common.Address;
import com.example.EventPlanner.model.event.EventTypeOverview;
import com.example.EventPlanner.model.merchandise.CategoryOverview;
import com.example.EventPlanner.model.merchandise.MerchandisePhoto;

import java.util.List;

import javax.annotation.processing.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class ServiceOverview {
    @SerializedName("id")
    @Expose
    private Integer id;
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
    @SerializedName("visible")
    @Expose
    private Boolean visible;
    @SerializedName("available")
    @Expose
    private Boolean available;
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
    @SerializedName("merchandisePhotos")
    @Expose
    private List<MerchandisePhoto> merchandisePhotos;
    @SerializedName("eventTypes")
    @Expose
    private List<EventTypeOverview> eventTypes;
    @SerializedName("address")
    @Expose
    private Address address;
    @SerializedName("category")
    @Expose
    private CategoryOverview category;
    @SerializedName("serviceProviderId")
    @Expose
    private Integer serviceProviderId;

    public ServiceOverview() {}

    public ServiceOverview(Integer id, String title, String specificity, String description, Double price, Integer discount, Boolean available, Boolean visible, Integer minDuration, Integer maxDuration, Integer reservationDeadline, Integer cancellationDeadline, Boolean automaticReservation, List<MerchandisePhoto> merchandisePhotos, List<EventTypeOverview> eventTypes, Address address, CategoryOverview category) {
        this.id = id;
        this.title = title;
        this.specificity = specificity;
        this.description = description;
        this.price = price;
        this.discount = discount;
        this.available = available;
        this.visible = visible;
        this.minDuration = minDuration;
        this.maxDuration = maxDuration;
        this.reservationDeadline = reservationDeadline;
        this.cancellationDeadline = cancellationDeadline;
        this.automaticReservation = automaticReservation;
        this.merchandisePhotos = merchandisePhotos;
        this.eventTypes = eventTypes;
        this.address = address;
        this.category = category;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
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

    public void setMerchandisePhotos(List<MerchandisePhoto> merchandisePhotos) {
        this.merchandisePhotos = merchandisePhotos;
    }

    public void setEventTypes(List<EventTypeOverview> eventTypes) {
        this.eventTypes = eventTypes;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setCategory(CategoryOverview category) {
        this.category = category;
    }

    public Integer getId() {
        return id;
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

    public Boolean getVisible() {
        return visible;
    }

    public Boolean getAvailable() {
        return available;
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

    public List<EventTypeOverview> getEventTypes() {
        return eventTypes;
    }

    public Address getAddress() {
        return address;
    }

    public List<MerchandisePhoto> getMerchandisePhotos() {
        return merchandisePhotos;
    }

    public Boolean getAutomaticReservation() {
        return automaticReservation;
    }

    public CategoryOverview getCategory() {
        return category;
    }
}
