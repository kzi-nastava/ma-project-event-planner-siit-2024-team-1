package com.example.EventPlanner.model.event;

import com.example.EventPlanner.model.common.Address;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.time.LocalTime;

import javax.annotation.processing.Generated;

@Generated("jsonschema2pojo")
public class Activity {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("startTime")
    @Expose
    private LocalTime startTime;
    @SerializedName("endTime")
    @Expose
    private LocalTime endTime;
    @SerializedName("address")
    @Expose
    private Address address;

    /**
     * No args constructor for use in serialization
     *
     */
    public Activity() {
    }

    public Activity(Integer id, String title, String description, LocalTime startTime, LocalTime endTime, Address address) {
        super();
        this.id = id;
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.address = address;
    }

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

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

}