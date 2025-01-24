package com.example.EventPlanner.model.priceList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class UpdatePriceListItemRequest {
    @SerializedName("price")
    @Expose
    private double price;
    @SerializedName("discount")
    @Expose
    private int discount;

    public UpdatePriceListItemRequest() {}

    public UpdatePriceListItemRequest(double price, int discount) {
        this.price = price;
        this.discount = discount;
    }

    public double getPrice() {
        return price;
    }

    public int getDiscount() {
        return discount;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }
}
