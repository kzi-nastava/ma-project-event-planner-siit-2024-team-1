package com.example.EventPlanner.model.event;

import java.util.List;

public class UpdateEventTypeRequest {

    private String description;
    private boolean active;
    private List<Integer> recommendedCategoryIds;

    // Default constructor
    public UpdateEventTypeRequest() {
    }

    // Parameterized constructor
    public UpdateEventTypeRequest(String description, boolean active, List<Integer> recommendedCategoryIds) {
        this.description = description;
        this.active = active;
        this.recommendedCategoryIds = recommendedCategoryIds;
    }

    // Getters and Setters
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