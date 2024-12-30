package com.example.EventPlanner.model.event;

import javax.annotation.Generated;

import com.example.EventPlanner.model.common.Address;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class EventOverview {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("address")
    @Expose
    private Address address;

    public EventOverview() {
    }

    public EventOverview(Integer id, String type, String title, String date, Address address, String description, Boolean isPublic) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.date = date;
        this.address = address;
        this.description = description;
        this.isPublic = isPublic;
    }

    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("isPublic")
    @Expose
    private Boolean isPublic;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

}
