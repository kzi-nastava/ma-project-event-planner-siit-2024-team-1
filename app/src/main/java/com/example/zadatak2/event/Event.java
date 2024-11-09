package com.example.zadatak2.event;

import com.example.zadatak2.address.Address;

import java.util.Date;

public class Event {
    private Long id;
    private String title;
    private String description;
    private int maxParticipants;
    private boolean isPublic;
    private Address address;
    private Date date;
    private EventType type;

    // Default constructor
    public Event() {}

    // Constructor with all fields
    public Event(Long id, String title, String description, int maxParticipants,
                 boolean isPublic, Address address, Date date, EventType type) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.maxParticipants = maxParticipants;
        this.isPublic = isPublic;
        this.address = address;
        this.date = date;
        this.type = type;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }
}