package com.example.EventPlanner.service;

import com.example.EventPlanner.address.Address;
import com.example.EventPlanner.address.Address1;
import com.example.EventPlanner.category.Category;
import com.example.EventPlanner.merchandise.Merchandise;
import com.example.EventPlanner.review.Review;

import java.util.List;

public class Service extends Merchandise {
    public Service(int id, String title, String description, String specificity, double price, double discount, boolean visible, boolean available, int minDuration, int maxDuration, int reservationDeadline, int cancelReservation, boolean automaticReservation, boolean deleted, List<String> photos, Category category, Address1 address, List<Review> reviews) {
        super(id, title, description, specificity, price, discount, visible, available, minDuration, maxDuration, reservationDeadline, cancelReservation, automaticReservation, deleted, photos, category, address, reviews);
    }
}
