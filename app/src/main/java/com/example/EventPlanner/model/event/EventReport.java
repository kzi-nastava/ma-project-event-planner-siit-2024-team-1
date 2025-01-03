package com.example.EventPlanner.model.event;

import com.example.EventPlanner.activities.EventDetails;
import com.example.EventPlanner.model.common.Review;

import java.util.List;

public class EventReport {
    private List<UserOverview> participants;
    private List<ReviewDTO> reviews;

    public EventReport(){

    }
    public EventReport(List<UserOverview> participants, List<ReviewDTO> reviews) {
        this.participants = participants;
        this.reviews = reviews;
    }

    public List<UserOverview> getParticipants() {
        return participants;
    }

    public void setParticipants(List<UserOverview> participants) {
        this.participants = participants;
    }

    public List<ReviewDTO> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewDTO> reviews) {
        this.reviews = reviews;
    }

    public int[] getGradeCounts() {
        int[] gradeCounts = new int[5];
        for (ReviewDTO review : reviews) {
            gradeCounts[review.getRating() - 1]++;
        }
        return gradeCounts;
    }
}
