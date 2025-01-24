package com.example.EventPlanner.clients.services.budget;

import android.provider.CallLog;

import com.example.EventPlanner.model.budget.Budget;
import com.example.EventPlanner.model.budget.CreateBudgetDTO;
import com.example.EventPlanner.model.budget.UpdateBudgetDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface BudgetService {
    @GET("budget/event/{eventId}")
    Call<Budget> getBudgetByEvent(@Path("eventId") int eventId);

    @POST("budget/create/{budgetId}")
    Call<Budget> createBudgetItem(@Path("budgetId") int budgetId, @Body CreateBudgetDTO createBudgetDTO);

    @PUT("budget/update/{budgetItemId}")
    Call<Budget> updateBudgetItem(@Path("budgetItemId") int budgetItemId, @Body UpdateBudgetDTO updateBudgetDTO);

    @PUT("budget/delete/{budgetId}/{budgetItemId}")
    Call<Budget> deleteBudgetItem(@Path("budgetId") int budgetId, @Path("budgetItemId") int budgetItemId);
}
