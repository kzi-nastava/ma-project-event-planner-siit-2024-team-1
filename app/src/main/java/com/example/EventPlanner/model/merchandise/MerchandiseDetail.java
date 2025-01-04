package com.example.EventPlanner.model.merchandise;

import com.example.EventPlanner.model.common.Address;
import com.example.EventPlanner.model.event.EventTypeOverview;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class MerchandiseDetail {
    @SerializedName("id")
    private int id;

    @SerializedName("title")
    private String title;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getSpecificity() {
        return specificity;
    }

    public void setSpecificity(String specificity) {
        this.specificity = specificity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public int getMinDuration() {
        return minDuration;
    }

    public void setMinDuration(int minDuration) {
        this.minDuration = minDuration;
    }

    public int getMaxDuration() {
        return maxDuration;
    }

    public void setMaxDuration(int maxDuration) {
        this.maxDuration = maxDuration;
    }

    public int getReservationDeadline() {
        return reservationDeadline;
    }

    public void setReservationDeadline(int reservationDeadline) {
        this.reservationDeadline = reservationDeadline;
    }

    public int getCancellationDeadline() {
        return cancellationDeadline;
    }

    public void setCancellationDeadline(int cancellationDeadline) {
        this.cancellationDeadline = cancellationDeadline;
    }

    public List<MerchandisePhoto> getMerchandisePhotos() {
        return merchandisePhotos;
    }

    public void setMerchandisePhotos(List<MerchandisePhoto> merchandisePhotos) {
        this.merchandisePhotos = merchandisePhotos;
    }

    public List<MerchandiseReviewOverview> getReviews() {
        return reviews;
    }

    public void setReviews(List<MerchandiseReviewOverview> reviews) {
        this.reviews = reviews;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public CategoryOverview getCategory() {
        return category;
    }

    public void setCategory(CategoryOverview category) {
        this.category = category;
    }

    public List<EventTypeOverview> getEventTypes() {
        return eventTypes;
    }

    public void setEventTypes(List<EventTypeOverview> eventTypes) {
        this.eventTypes = eventTypes;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getServiceProviderId() {
        return serviceProviderId;
    }

    public void setServiceProviderId(int serviceProviderId) {
        this.serviceProviderId = serviceProviderId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @SerializedName("description")
    private String description;

    @SerializedName("specificity")
    private String specificity;

    @SerializedName("price")
    private double price;

    @SerializedName("discount")
    private int discount;

    @SerializedName("visible")
    private boolean visible;

    @SerializedName("available")
    private boolean available;

    @SerializedName("minDuration")
    private int minDuration;

    @SerializedName("maxDuration")
    private int maxDuration;

    @SerializedName("reservationDeadline")
    private int reservationDeadline;

    @SerializedName("cancellationDeadline")
    private int cancellationDeadline;

    @SerializedName("merchandisePhotos")
    private List<MerchandisePhoto> merchandisePhotos;

    @SerializedName("reviews")
    private List<MerchandiseReviewOverview> reviews;

    @SerializedName("address")
    private Address address;

    @SerializedName("category")
    private CategoryOverview category;

    @SerializedName("eventTypes")
    private List<EventTypeOverview> eventTypes;

    @SerializedName("rating")
    private double rating;

    @SerializedName("serviceProviderId")
    private int serviceProviderId;

    @SerializedName("type")
    private String type;
}

