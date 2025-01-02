package com.example.EventPlanner.model.user;


import java.util.List;
import javax.annotation.Generated;

import com.example.EventPlanner.model.common.Address;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class UpdateSpResponse {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("surname")
    @Expose
    private String surname;
    @SerializedName("phoneNumber")
    @Expose
    private String phoneNumber;
    @SerializedName("address")
    @Expose
    private Address address;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("role")
    @Expose
    private String role;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("photos")
    @Expose
    private List<BusinessPhoto> photos;

    /**
     * No args constructor for use in serialization
     *
     */
    public UpdateSpResponse() {
    }

    public UpdateSpResponse(Integer id, String name, String message, String surname, String phoneNumber, Address address, String photo, String role, String description, List<BusinessPhoto> photos) {
        super();
        this.id = id;
        this.name = name;
        this.message = message;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.photo = photo;
        this.role = role;
        this.description = description;
        this.photos = photos;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<BusinessPhoto> getPhotos() {
        return photos;
    }

    public void setPhotos(List<BusinessPhoto> photos) {
        this.photos = photos;
    }
}
