package com.example.EventPlanner.model.user;

import com.example.EventPlanner.model.auth.Role;
import com.example.EventPlanner.model.common.Address;

public class UpdateEoRequest {
    private String name;

    public UpdateEoRequest(String name, String surname, String phoneNumber, Address address, String password, String photo, Role role) {
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.password = password;
        this.photo = photo;
        this.role = role;
    }

    private String surname;
    private String phoneNumber;
    private Address address;
    private String password;
    private String photo;
    private Role role;

    // Default constructor
    public UpdateEoRequest() {}

    // Getters and Setters
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}