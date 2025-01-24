package com.example.EventPlanner.model.budget;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class BudgetItem {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("category")
    @Expose
    private BudgetItemCategoryDTO category;
    @SerializedName("maxAmount")
    @Expose
    private Double maxAmount;
    @SerializedName("amountSpent")
    @Expose
    private Double amountSpent;
    @SerializedName("merchandise")
    @Expose
    private MerchandiseBudgetDTO merchandise;

    public BudgetItem() {}

    @Override
    public String toString() {
        return this.id + " " + this.category.getTitle() + " " + this.maxAmount + " " + this.amountSpent;
    }

    public BudgetItem(Integer id, BudgetItemCategoryDTO category, Double maxAmount, Double amountSpent, MerchandiseBudgetDTO merchandise) {
        this.id = id;
        this.category = category;
        this.maxAmount = maxAmount;
        this.amountSpent = amountSpent;
        this.merchandise = merchandise;
    }

    public Integer getId() {
        return id;
    }

    public BudgetItemCategoryDTO getCategory() {
        return category;
    }

    public Double getMaxAmount() {
        return maxAmount;
    }

    public Double getAmountSpent() {
        return amountSpent;
    }

    public MerchandiseBudgetDTO getMerchandise() {
        return merchandise;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setCategory(BudgetItemCategoryDTO category) {
        this.category = category;
    }

    public void setMaxAmount(Double maxAmount) {
        this.maxAmount = maxAmount;
    }

    public void setAmountSpent(Double amountSpent) {
        this.amountSpent = amountSpent;
    }

    public void setMerchandise(MerchandiseBudgetDTO merchandise) {
        this.merchandise = merchandise;
    }
}
