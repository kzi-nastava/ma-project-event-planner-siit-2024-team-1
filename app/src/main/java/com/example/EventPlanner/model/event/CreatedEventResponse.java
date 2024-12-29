package com.example.EventPlanner.model.event;

import com.example.EventPlanner.model.common.Address;
import com.example.EventPlanner.model.merchandise.product.GetProductByIdResponse;
import com.example.EventPlanner.model.merchandise.service.GetServiceByIdResponse;
import com.example.EventPlanner.model.user.EventOrganizer;
import com.example.EventPlanner.model.user.EventOrganizerDTO;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class CreatedEventResponse {

    @SerializedName("id")
    private int id;

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    @SerializedName("maxParticipants")
    private int maxParticipants;

    @SerializedName("isPublic")
    private boolean isPublic;

    @SerializedName("date")
    private String date; // LocalDateTime as String

    @SerializedName("address")
    private Address address; // AddressDTO POJO

    @SerializedName("eventType")
    private EventTypeOverview eventType; // EventTypeOverviewDTO POJO

    @SerializedName("products")
    private List<GetProductByIdResponse> products; // List of GetProductByIdResponseDTO POJOs

    @SerializedName("services")
    private List<GetServiceByIdResponse> services; // List of GetServiceByIdResponseDTO POJOs

    @SerializedName("organizer")
    private EventOrganizerDTO organizer; // EventOrganizerDTO POJO

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

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean isPublic) {
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

    public EventTypeOverview getEventType() {
        return eventType;
    }

    public void setEventType(EventTypeOverview eventType) {
        this.eventType = eventType;
    }

    public List<GetProductByIdResponse> getProducts() {
        return products;
    }

    public void setProducts(List<GetProductByIdResponse> products) {
        this.products = products;
    }

    public List<GetServiceByIdResponse> getServices() {
        return services;
    }

    public void setServices(List<GetServiceByIdResponse> services) {
        this.services = services;
    }

    public EventOrganizerDTO getOrganizer() {
        return organizer;
    }

    public void setOrganizer(EventOrganizerDTO organizer) {
        this.organizer = organizer;
    }
}