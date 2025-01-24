package com.example.EventPlanner.model.budget;

import com.example.EventPlanner.model.common.Address;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class MerchandiseBudgetDTO {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("type")
    private String type;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("category")
    @Expose
    private BudgetItemCategoryDTO category;
    @SerializedName("rating")
    @Expose
    private Double rating;
    @SerializedName("address")
    @Expose
    private Address address;
    @SerializedName("price")
    @Expose
    private Double price;

    public MerchandiseBudgetDTO() {}

    public MerchandiseBudgetDTO(Integer id, String type, String title, String description, BudgetItemCategoryDTO category, Double rating, Address address, Double price) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.description = description;
        this.category = category;
        this.rating = rating;
        this.address = address;
        this.price = price;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public BudgetItemCategoryDTO getCategory() {
        return category;
    }

    public Double getRating() {
        return rating;
    }

    public Address getAddress() {
        return address;
    }

    public Double getPrice() {
        return price;
    }

    public String getType() {
        return type;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(BudgetItemCategoryDTO category) {
        this.category = category;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
