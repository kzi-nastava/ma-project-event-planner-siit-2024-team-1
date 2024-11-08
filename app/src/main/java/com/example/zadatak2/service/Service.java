package com.example.zadatak2.service;

import com.example.zadatak2.address.Address;
import com.example.zadatak2.category.Category;
import com.example.zadatak2.merchandise.Merchandise;
import com.example.zadatak2.review.Review;

import java.util.List;

public class Service extends Merchandise {
    public Service(int id, String title, String description, String specificity, double price, double discount, boolean visible, boolean available, int minDuration, int maxDuration, int reservationDeadline, int cancelReservation, boolean automaticReservation, boolean deleted, List<String> photos, Category category, Address address, List<Review> reviews) {
        super(id, title, description, specificity, price, discount, visible, available, minDuration, maxDuration, reservationDeadline, cancelReservation, automaticReservation, deleted, photos, category, address, reviews);
    }
}
