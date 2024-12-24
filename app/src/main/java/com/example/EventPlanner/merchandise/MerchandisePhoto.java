package com.example.EventPlanner.merchandise;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class MerchandisePhoto {

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
    public MerchandisePhoto() {
    }

    public MerchandisePhoto(Integer id, String photo) {
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
