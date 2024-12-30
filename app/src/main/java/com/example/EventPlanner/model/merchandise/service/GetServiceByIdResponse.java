package com.example.EventPlanner.model.merchandise.service;

import com.example.EventPlanner.model.common.Address;
import com.example.EventPlanner.model.event.EventType;
import com.example.EventPlanner.model.merchandise.Category;
import com.example.EventPlanner.model.merchandise.MerchandisePhoto;
import com.example.EventPlanner.model.user.ServiceProvider;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetServiceByIdResponse {

    @SerializedName("id")
    private int id;

    @SerializedName("title")
    private String title;

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

    @SerializedName("automaticReservation")
    private boolean automaticReservation;

    @SerializedName("deleted")
    private boolean deleted;

    @SerializedName("serviceProvider")
    private ServiceProvider serviceProvider;

    @SerializedName("photos")
    private List<MerchandisePhoto> photos;

    @SerializedName("eventTypes")
    private List<EventType> eventTypes;

    @SerializedName("address")
    private Address address;

    @SerializedName("category")
    private Category category;

    @SerializedName("timeslots")
    private List<Timeslot> timeslots;

    // Getters and Setters

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

    public boolean isAutomaticReservation() {
        return automaticReservation;
    }

    public void setAutomaticReservation(boolean automaticReservation) {
        this.automaticReservation = automaticReservation;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public List<MerchandisePhoto> getPhotos() {
        return photos;
    }

    public void setPhotos(List<MerchandisePhoto> photos) {
        this.photos = photos;
    }

    public List<EventType> getEventTypes() {
        return eventTypes;
    }

    public void setEventTypes(List<EventType> eventTypes) {
        this.eventTypes = eventTypes;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<Timeslot> getTimeslots() {
        return timeslots;
    }

    public void setTimeslots(List<Timeslot> timeslots) {
        this.timeslots = timeslots;
    }
}