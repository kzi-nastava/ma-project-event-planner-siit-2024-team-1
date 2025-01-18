package com.example.EventPlanner.model.merchandise;
import javax.annotation.Generated;

import com.example.EventPlanner.model.common.Address;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@Generated("jsonschema2pojo")
public class MerchandiseOverview {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("photos")
    @Expose
    private List<MerchandisePhoto> photos;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("rating")
    @Expose
    private Double rating;
    @SerializedName("address")
    @Expose
    private Address address;
    @SerializedName("price")
    @Expose
    private Double price;
    @SerializedName("description")
    @Expose
    private String description;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<MerchandisePhoto> getPhotos() {
        return photos;
    }

    public void setPhotos(List<MerchandisePhoto> photos) {
        this.photos = photos;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }
    public MerchandiseOverview(){}
    public MerchandiseOverview(Integer id, String category, String type, List<MerchandisePhoto> photos, String title, Double rating, Address address, Double price, String description) {
        this.id = id;
        this.category = category;
        this.type = type;
        this.photos = photos;
        this.title = title;
        this.rating = rating;
        this.address = address;
        this.price = price;
        this.description = description;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
