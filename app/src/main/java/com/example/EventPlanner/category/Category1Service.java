package com.example.EventPlanner.category;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Category1Service {
    @GET("categories/get/approved")
    Call<ArrayList<Category1>> getAll();
}
