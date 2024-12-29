package com.example.EventPlanner.model.event;

import java.util.List;

public class CreateEventTypeRequest {

    private String title;
    private String description;
    private boolean active;
    private List<Integer> recommendedCategoryIds;

    // Default constructor
    public CreateEventTypeRequest() {
    }

    // Parameterized constructor
    public CreateEventTypeRequest(String title, String description, boolean active, List<Integer> recommendedCategoryIds) {
        this.title = title;
        this.description = description;
        this.active = active;
        this.recommendedCategoryIds = recommendedCategoryIds;
    }

    // Getters and Setters
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<Integer> getRecommendedCategoryIds() {
        return recommendedCategoryIds;
    }

    public void setRecommendedCategoryIds(List<Integer> recommendedCategoryIds) {
        this.recommendedCategoryIds = recommendedCategoryIds;
    }
}
