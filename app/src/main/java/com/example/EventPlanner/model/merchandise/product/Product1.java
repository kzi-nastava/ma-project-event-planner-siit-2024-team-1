package com.example.EventPlanner.model.merchandise.product;

import com.example.EventPlanner.model.common.Review;
import com.example.EventPlanner.model.common.Address1;
import com.example.EventPlanner.model.merchandise.Category;
import com.example.EventPlanner.model.merchandise.Merchandise;

import java.util.List;

public class Product1 extends Merchandise {
    public Product1(int id, String title, String description, String specificity, double price, double discount, boolean visible, boolean available, int minDuration, int maxDuration, int reservationDeadline, int cancelReservation, boolean automaticReservation, boolean deleted, List<String> photos, Category category, Address1 address, List<Review> reviews) {
        super(id, title, description, specificity, price, discount, visible, available, minDuration, maxDuration, reservationDeadline, cancelReservation, automaticReservation, deleted, photos, category, address, reviews);
    }
}
