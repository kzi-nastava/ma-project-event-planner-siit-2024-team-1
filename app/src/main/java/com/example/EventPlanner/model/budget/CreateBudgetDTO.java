package com.example.EventPlanner.model.budget;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class CreateBudgetDTO {
    @SerializedName("categoryId")
    @Expose
    private Integer categoryId;
    @SerializedName("maxAmount")
    @Expose
    private Double maxAmount;

    public CreateBudgetDTO() {}

    public CreateBudgetDTO(Integer categoryId, Double maxAmount) {
        this.categoryId = categoryId;
        this.maxAmount = maxAmount;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public Double getMaxAmount() {
        return maxAmount;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public void setMaxAmount(Double maxAmount) {
        this.maxAmount = maxAmount;
    }
}
