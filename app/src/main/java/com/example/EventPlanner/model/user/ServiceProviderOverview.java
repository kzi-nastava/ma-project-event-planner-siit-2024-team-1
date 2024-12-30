package com.example.EventPlanner.model.user;

import com.example.EventPlanner.model.common.Address;
import com.example.EventPlanner.model.event.EventTypeOverview;
import com.example.EventPlanner.model.merchandise.CategoryOverview;
import com.example.EventPlanner.model.merchandise.MerchandisePhoto;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ServiceProviderOverview {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("surname")
    @Expose
    private String surname;

    public ServiceProviderOverview() {
    }

    public ServiceProviderOverview(Integer id, String name, String surname) {
        super();
        this.id = id;
        this.name = name;
        this.surname = surname;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}
