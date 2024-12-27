package com.example.EventPlanner.clients.services.merchandise;

import com.example.EventPlanner.model.merchandise.Category1;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Category1Service {
    @GET("categories/get/approved")
    Call<ArrayList<Category1>> getAll();
}
