package com.example.EventPlanner.clients.services.merchandise;

import com.example.EventPlanner.model.merchandise.MerchandiseDetailsDTO;
import com.example.EventPlanner.model.merchandise.MerchandiseOverview;
import com.example.EventPlanner.model.merchandise.product.ProductOverview;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MerchandiseService {
    @GET("merchandise/top")
    Call<ArrayList<MerchandiseOverview>> getTop(@Query("userId") int userId);

    @GET("merchandise/{id}/favorite")
    Call<List<MerchandiseOverview>> getFavorites(@Path("id") int id);

    @GET("merchandise/{id}")
    Call<MerchandiseDetailsDTO> getMerchandiseById(@Path("id") int id, @Query("userId") int userId);
}
