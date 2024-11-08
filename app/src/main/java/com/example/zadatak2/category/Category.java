package com.example.zadatak2.category;

public class Category {
    private int id;
    private String title;
    private String description;
    private boolean pending;

    // Constructor
    public Category(int id, String title, String description, boolean pending) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.pending = pending;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isPending() { return pending; }
    public void setPending(boolean pending) { this.pending = pending; }
}

