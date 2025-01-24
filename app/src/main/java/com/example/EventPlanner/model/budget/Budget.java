package com.example.EventPlanner.model.budget;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class Budget {
    @SerializedName("budgetId")
    @Expose
    private Integer budgetId;
    @SerializedName("maxAmount")
    @Expose
    private Double maxAmount;
    @SerializedName("spentAmount")
    @Expose
    private Double spentAmount;
    @SerializedName("budgetItems")
    @Expose
    private List<BudgetItem> budgetItems;

    public  Budget() {}

    @Override
    public String toString() {
        return this.budgetId + " " + this.maxAmount + " " + this.spentAmount + " " + this.budgetItems;
    }

    public Budget(Integer budgetId, Double maxAmount, Double spentAmount, List<BudgetItem> budgetItems) {
        this.budgetId = budgetId;
        this.maxAmount = maxAmount;
        this.spentAmount = spentAmount;
        this.budgetItems = budgetItems;
    }

    public Integer getBudgetId() {
        return budgetId;
    }

    public Double getMaxAmount() {
        return maxAmount;
    }

    public Double getSpentAmount() {
        return spentAmount;
    }

    public List<BudgetItem> getBudgetItems() {
        return budgetItems;
    }

    public void setBudgetId(Integer budgetId) {
        this.budgetId = budgetId;
    }

    public void setMaxAmount(Double maxAmount) {
        this.maxAmount = maxAmount;
    }

    public void setSpentAmount(Double spentAmount) {
        this.spentAmount = spentAmount;
    }

    public void setBudgetItems(List<BudgetItem> budgetItems) {
        this.budgetItems = budgetItems;
    }
}
