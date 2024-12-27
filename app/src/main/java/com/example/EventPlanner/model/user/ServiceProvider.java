package com.example.EventPlanner.model.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class ServiceProvider extends User {
    @SerializedName("company")
    @Expose
    private String company;
    @SerializedName("description")
    @Expose
    private String description;
}
