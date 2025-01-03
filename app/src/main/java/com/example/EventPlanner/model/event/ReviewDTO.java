package com.example.EventPlanner.model.event;

public class ReviewDTO {
    private int id;

    private String comment;
    private int rating;
    private boolean status;

    public ReviewDTO(){}
    public ReviewDTO(int id, String comment, int rating, boolean status) {
        this.id = id;
        this.comment = comment;
        this.rating = rating;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
