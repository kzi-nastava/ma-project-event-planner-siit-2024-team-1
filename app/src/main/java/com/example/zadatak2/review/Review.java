package com.example.zadatak2.review;

import com.example.zadatak2.user.User;

public class Review {
    private int id;
    private User user;
    private String comment;
    private int rating;

    // Constructor
    public Review(int id, User user, String comment, int rating) {
        this.id = id;
        this.user = user;
        this.comment = comment;
        this.rating = rating;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
}


