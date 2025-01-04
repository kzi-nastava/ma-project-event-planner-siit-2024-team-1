package com.example.EventPlanner.model.messages;

import com.google.gson.annotations.SerializedName;
import java.time.LocalDateTime;

public class MessageDTO {

    @SerializedName("id")
    private int id;

    @SerializedName("content")
    private String content;

    @SerializedName("sentTime")
    private String sentTime;

    @SerializedName("senderId")
    private int senderId;

    @SerializedName("recipientId")
    private int recipientId;

    // Constructors
    public MessageDTO() {}

    public MessageDTO(int id, String content, String sentTime, int senderId, int recipientId) {
        this.id = id;
        this.content = content;
        this.sentTime = sentTime;
        this.senderId = senderId;
        this.recipientId = recipientId;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSentTime() {
        return sentTime;
    }

    public void setSentTime(String sentTime) {
        this.sentTime = sentTime;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(int recipientId) {
        this.recipientId = recipientId;
    }
}

