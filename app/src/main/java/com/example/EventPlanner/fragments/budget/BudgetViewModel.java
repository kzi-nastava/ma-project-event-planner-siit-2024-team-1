package com.example.EventPlanner.fragments.budget;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.EventPlanner.clients.ClientUtils;
import com.example.EventPlanner.model.budget.Budget;
import com.example.EventPlanner.model.budget.BudgetItem;
import com.example.EventPlanner.model.budget.CreateBudgetDTO;
import com.example.EventPlanner.model.budget.UpdateBudgetDTO;
import com.example.EventPlanner.model.merchandise.MerchandiseOverview;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BudgetViewModel extends ViewModel {
    private final MutableLiveData<Budget> budgetLiveData = new MutableLiveData<>();

    public BudgetViewModel() {}

    public LiveData<Budget> getBudget() { return this.budgetLiveData; }

    public void getBudgetByEvent(int eventId) {
        Call<Budget> call = ClientUtils.budgetService.getBudgetByEvent(eventId);
        call.enqueue(new Callback<Budget>() {
            @Override
            public void onResponse(Call<Budget> call, Response<Budget> response) {
                if(response.isSuccessful() && response.body() != null) {
                    Budget budget = response.body();
                    budgetLiveData.setValue(budget);
                }else {
                    Log.e("BudgetViewModel", "Failed to fetch budget: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Budget> call, Throwable throwable) {
                Log.e("BudgetViewModel", "Error fetching budget: " + throwable.getMessage());
            }
        });
    }

    public void createBudgetItem(int budgetId, CreateBudgetDTO dto) {
        Call<Budget> call = ClientUtils.budgetService.createBudgetItem(budgetId, dto);
        call.enqueue(new Callback<Budget>() {
            @Override
            public void onResponse(Call<Budget> call, Response<Budget> response) {
                if(response.isSuccessful() && response.body() != null) {
                    Budget budget = response.body();
                    budgetLiveData.setValue(budget);
                }else {
                    Log.e("BudgetViewModel", "Failed to create budget item: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Budget> call, Throwable throwable) {
                Log.e("BudgetViewModel", "Error creating budget item: " + throwable.getMessage());
            }
        });
    }

    public void updateBudgetItem(int budgetItemId, UpdateBudgetDTO dto) {
        Call<Budget> call = ClientUtils.budgetService.updateBudgetItem(budgetItemId, dto);
        call.enqueue(new Callback<Budget>() {
            @Override
            public void onResponse(Call<Budget> call, Response<Budget> response) {
                if(response.isSuccessful() && response.body() != null) {
                    Budget budget = response.body();
                    budgetLiveData.setValue(budget);
                }else {
                    Log.e("BudgetViewModel", "Failed to update budget item: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Budget> call, Throwable throwable) {
                Log.e("BudgetViewModel", "Failed to update budget item: " + throwable.getMessage());
            }
        });
    }

    public void deleteBudgetItem(int budgetId, int budgetItemId) {
        Call<Budget> call = ClientUtils.budgetService.deleteBudgetItem(budgetId, budgetItemId);
        call.enqueue(new Callback<Budget>() {
            @Override
            public void onResponse(Call<Budget> call, Response<Budget> response) {
                if(response.isSuccessful() && response.body() != null) {
                    Budget budget = response.body();
                    budgetLiveData.setValue(budget);
                }else {
                    Log.e("BudgetViewModel", "Failed to delete budget item: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Budget> call, Throwable throwable) {
                Log.e("BudgetViewModel", "Failed to delete budget item: " + throwable.getMessage());
            }
        });
    }
}
