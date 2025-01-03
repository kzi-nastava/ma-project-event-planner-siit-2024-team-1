package com.example.EventPlanner.model.common;

import java.time.LocalDateTime;

public class Notification {
    private int id;
    private String content;
    private boolean read;
    private String date;

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public boolean isRead() { return read; }
    public void setRead(boolean read) { this.read = read; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
}
