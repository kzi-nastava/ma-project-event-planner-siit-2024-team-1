package com.example.EventPlanner.model.user;

import javax.annotation.Generated;

import com.example.EventPlanner.model.common.Address;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@Generated("jsonschema2pojo")
public class GetSpById {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("surname")
    @Expose
    private String surname;
    @SerializedName("phoneNumber")
    @Expose
    private String phoneNumber;
    @SerializedName("address")
    @Expose
    private Address address;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("photo")
    @Expose
    private String photo;

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
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

    @SerializedName("company")
    @Expose
    private String company;
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
    public GetSpById() {
    }

    public GetSpById(String name, String surname, String phoneNumber, Address address, String email, String photo, String company, String description, List<BusinessPhoto> photos) {
        super();
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.email = email;
        this.photo = photo;
        this.company = company;
        this.description = description;
        this.photos = photos;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}