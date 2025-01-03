package com.example.EventPlanner.clients.services.merchandise;

import com.example.EventPlanner.model.merchandise.Category1;
import com.example.EventPlanner.model.merchandise.CategoryOverview;
import com.example.EventPlanner.model.merchandise.CreateCategoryRequest;
import com.example.EventPlanner.model.merchandise.GetAllCategoriesDTO;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface CategoryService {
    @GET("categories/get/approved")
    Call<List<CategoryOverview>> getApproved();
    @GET("merchandise/categories")
    Call<GetAllCategoriesDTO> getAll();

    @POST("categories/create")
    Call<List<CategoryOverview>> create(@Body CreateCategoryRequest dto);
}
