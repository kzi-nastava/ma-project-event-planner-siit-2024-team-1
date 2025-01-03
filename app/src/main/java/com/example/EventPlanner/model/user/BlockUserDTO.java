package com.example.EventPlanner.model.user;

import com.google.gson.annotations.SerializedName;

public class BlockUserDTO {

    @SerializedName("blockerId")
    private int blockerId;

    @SerializedName("blockedId")
    private int blockedId;

    // Getters and setters (if not using Lombok)
    public int getBlockerId() {
        return blockerId;
    }

    public void setBlockerId(int blockerId) {
        this.blockerId = blockerId;
    }

    public int getBlockedId() {
        return blockedId;
    }

    public void setBlockedId(int blockedId) {
        this.blockedId = blockedId;
    }
}
