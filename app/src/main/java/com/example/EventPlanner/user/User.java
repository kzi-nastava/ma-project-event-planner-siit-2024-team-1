package com.example.EventPlanner.user;

import com.example.EventPlanner.address.Address;

public class User {
    private int id;
    private String name;
    private String surname;
    private Address address;
    private String phoneNumber;
    private String email;
    private String password;
    private String photo;
    private boolean active;

    // Constructor
    public User(int id, String name, String surname, Address address,
                String phoneNumber, String email, String password,
                String photo, boolean active) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.photo = photo;
        this.active = active;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }

    public Address getAddress() { return address; }
    public void setAddress(Address address) { this.address = address; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPhoto() { return photo; }
    public void setPhoto(String photo) { this.photo = photo; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
