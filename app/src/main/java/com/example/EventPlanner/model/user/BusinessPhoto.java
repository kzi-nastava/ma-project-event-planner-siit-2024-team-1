package com.example.EventPlanner.model.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BusinessPhoto {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("photo")
    @Expose
    private String photo;

    /**
     * No args constructor for use in serialization
     *
     */
    public BusinessPhoto() {
    }

    public BusinessPhoto(Integer id, String photo) {
        super();
        this.id = id;
        this.photo = photo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
