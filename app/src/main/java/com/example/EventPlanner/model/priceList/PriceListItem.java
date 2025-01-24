package com.example.EventPlanner.model.priceList;

import com.example.EventPlanner.fragments.priceList.PriceListItemList;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class PriceListItem {
    @SerializedName("merchandiseId")
    @Expose
    private int merchandiseId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("price")
    @Expose
    private double price;
    @SerializedName("discount")
    @Expose
    private int discount;
    @SerializedName("discountedPrice")
    @Expose
    private double discountedPrice;

    public PriceListItem() {}

    public PriceListItem(int merchandiseId, String title, double price, int discount, double discountedPrice) {
        this.merchandiseId = merchandiseId;
        this.title = title;
        this.price = price;
        this.discount = discount;
        this.discountedPrice = discountedPrice;
    }

    public int getMerchandiseId() {
        return merchandiseId;
    }

    public String getTitle() {
        return title;
    }

    public double getPrice() {
        return price;
    }

    public int getDiscount() {
        return discount;
    }

    public double getDiscountedPrice() {
        return discountedPrice;
    }

    public void setMerchandiseId(int merchandiseId) {
        this.merchandiseId = merchandiseId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public void setDiscountedPrice(double discountedPrice) {
        this.discountedPrice = discountedPrice;
    }
}
