package com.example.EventPlanner.clients.services.merchandise;

import com.example.EventPlanner.model.merchandise.Category1;
import com.example.EventPlanner.model.merchandise.CategoryOverview;
import com.example.EventPlanner.model.merchandise.CreateCategoryRequest;
import com.example.EventPlanner.model.merchandise.GetAllCategoriesDTO;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CategoryService {
    @GET("categories/get/approved")
    Call<List<CategoryOverview>> getApproved();
    @GET("merchandise/categories")
    Call<GetAllCategoriesDTO> getAll();
    @GET("categories/get/pending")
    Call<List<CategoryOverview>> getPending();
    @POST("categories/create")
    Call<CategoryOverview> create(@Body CreateCategoryRequest dto);
    @PUT("categories/approve/{categoryId}")
    Call<CategoryOverview> approveCategory(@Path("categoryId") int categoryId);
    @PUT("categories/update/{categoryId}")
    Call<CategoryOverview> updateCategory(@Path("categoryId") int categoryId, @Body CreateCategoryRequest dto);
    @DELETE("categories/delete/{categoryId}")
    Call<ResponseBody> deleteCategory(@Path("categoryId") int categoryId);
}
