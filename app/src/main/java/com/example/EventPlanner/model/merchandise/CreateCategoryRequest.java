package com.example.EventPlanner.model.merchandise;

public class CreateCategoryRequest {
    private String title;
    private String description;
    private boolean pending;

    public CreateCategoryRequest(){}
    public CreateCategoryRequest(String title, String description, boolean pending) {
        this.title = title;
        this.description = description;
        this.pending = pending;
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

    public boolean isPending() {
        return pending;
    }

    public void setPending(boolean pending) {
        this.pending = pending;
    }
}
