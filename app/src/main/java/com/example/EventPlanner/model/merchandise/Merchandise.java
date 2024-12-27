package com.example.EventPlanner.model.merchandise;

import com.example.EventPlanner.model.common.Review;
import com.example.EventPlanner.model.common.Address1;

import java.util.List;

public class Merchandise {
    private int id;
    private String title;
    private String description;
    private String specificity;
    private double price;
    private double discount;
    private boolean visible;
    private boolean available;
    private int minDuration;
    private int maxDuration;
    private int reservationDeadline;
    private int cancelReservation;
    private boolean automaticReservation;
    private boolean deleted;
    private List<String> photos;
    private Category category;
    private Address1 address;
    private List<Review> reviews;

    // Constructor
    public Merchandise(int id, String title, String description, String specificity,
                       double price, double discount, boolean visible, boolean available,
                       int minDuration, int maxDuration, int reservationDeadline,
                       int cancelReservation, boolean automaticReservation, boolean deleted,
                       List<String> photos, Category category, Address1 address, List<Review> reviews) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.specificity = specificity;
        this.price = price;
        this.discount = discount;
        this.visible = visible;
        this.available = available;
        this.minDuration = minDuration;
        this.maxDuration = maxDuration;
        this.reservationDeadline = reservationDeadline;
        this.cancelReservation = cancelReservation;
        this.automaticReservation = automaticReservation;
        this.deleted = deleted;
        this.photos = photos;
        this.category = category;
        this.address = address;
        this.reviews = reviews;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getSpecificity() { return specificity; }
    public void setSpecificity(String specificity) { this.specificity = specificity; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public double getDiscount() { return discount; }
    public void setDiscount(double discount) { this.discount = discount; }

    public boolean isVisible() { return visible; }
    public void setVisible(boolean visible) { this.visible = visible; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    public int getMinDuration() { return minDuration; }
    public void setMinDuration(int minDuration) { this.minDuration = minDuration; }

    public int getMaxDuration() { return maxDuration; }
    public void setMaxDuration(int maxDuration) { this.maxDuration = maxDuration; }

    public int getReservationDeadline() { return reservationDeadline; }
    public void setReservationDeadline(int reservationDeadline) { this.reservationDeadline = reservationDeadline; }

    public int getCancelReservation() { return cancelReservation; }
    public void setCancelReservation(int cancelReservation) { this.cancelReservation = cancelReservation; }

    public boolean isAutomaticReservation() { return automaticReservation; }
    public void setAutomaticReservation(boolean automaticReservation) { this.automaticReservation = automaticReservation; }

    public boolean isDeleted() { return deleted; }
    public void setDeleted(boolean deleted) { this.deleted = deleted; }

    public List<String> getPhotos() { return photos; }
    public void setPhotos(List<String> photos) { this.photos = photos; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public Address1 getAddress() { return address; }
    public void setAddress(Address1 address) { this.address = address; }

    public List<Review> getReviews() { return reviews; }
    public Double getRating(){
        if (reviews == null || reviews.isEmpty()) {
            return 0.0;
        }

        return reviews.stream()
                .mapToDouble(Review::getRating)
                .average()
                .orElse(0.0);
    }
    public void setReviews(List<Review> reviews) { this.reviews = reviews; }
}
