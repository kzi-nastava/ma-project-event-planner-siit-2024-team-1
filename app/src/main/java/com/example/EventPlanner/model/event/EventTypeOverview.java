package com.example.EventPlanner.model.event;

import com.example.EventPlanner.model.merchandise.CategoryOverview;

import java.util.List;

public class EventTypeOverview {

    private Integer id;
    private String title;
    private String description;
    private boolean active;
    private List<CategoryOverview> recommendedCategories;

    // Default constructor
    public EventTypeOverview() {
    }

    // Parameterized constructor
    public EventTypeOverview(int Integer, String title, String description, boolean active, List<CategoryOverview> recommendedCategories) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.active = active;
        this.recommendedCategories = recommendedCategories;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<CategoryOverview> getRecommendedCategories() {
        return recommendedCategories;
    }

    public void setRecommendedCategories(List<CategoryOverview> recommendedCategories) {
        this.recommendedCategories = recommendedCategories;
    }
}
