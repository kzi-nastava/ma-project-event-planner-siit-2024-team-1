package com.example.EventPlanner.clients.services.merchandise;

import com.example.EventPlanner.model.merchandise.MerchandiseOverview;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MerchandiseService {
    @GET("merchandise/top")
    Call<ArrayList<MerchandiseOverview>> getTop(@Query("userId") int userId);
}
