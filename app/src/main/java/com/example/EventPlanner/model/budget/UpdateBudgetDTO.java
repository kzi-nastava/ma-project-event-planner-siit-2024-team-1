package com.example.EventPlanner.model.budget;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class UpdateBudgetDTO {
    @SerializedName("budgetId")
    @Expose
    private Integer budgetId;
    @SerializedName("price")
    @Expose
    private Double price;

    public UpdateBudgetDTO() {}
    public UpdateBudgetDTO(Integer budgetId, Double price) {
        this.budgetId = budgetId;
        this.price = price;
    }

    public Integer getBudgetId() {
        return budgetId;
    }

    public Double getPrice() {
        return price;
    }

    public void setBudgetId(Integer budgetId) {
        this.budgetId = budgetId;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
