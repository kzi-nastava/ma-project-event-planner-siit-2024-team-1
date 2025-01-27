package com.example.EventPlanner.model.messages;

import com.google.gson.annotations.SerializedName;

public class MessageRequestDTO {
    @SerializedName("content")
    private String content;
    @SerializedName("senderId")
    private Integer senderId;
    @SerializedName("receiverId")
    private Integer receiverId;

    public MessageRequestDTO() {}

    public MessageRequestDTO(String content, Integer senderId, Integer receiverId) {
        this.content = content;
        this.senderId = senderId;
        this.receiverId = receiverId;
    }
}
