package com.example.EventPlanner.clients.services.merchandise;

import com.example.EventPlanner.model.merchandise.Category1;
import com.example.EventPlanner.model.merchandise.CategoryOverview;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CategoryService {
    @GET("categories/get/approved")
    Call<List<CategoryOverview>> getApproved();
}
