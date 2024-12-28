package com.example.EventPlanner.clients.services.merchandise.service;

import com.example.EventPlanner.model.common.PageResponse;
import com.example.EventPlanner.model.event.EventOverview;
import com.example.EventPlanner.model.merchandise.MerchandiseOverview;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ServiceService {
    @GET("services/search")
    Call<PageResponse<MerchandiseOverview>> searchServices(@Query("userId") int userId,
                                                           @Query("search") String search,
                                                           @Query("priceMin") Double priceMin,
                                                           @Query("priceMax") Double priceMax,
                                                           @Query("durationMin") Integer durationMin,
                                                           @Query("durationMax") Integer durationMax,
                                                           @Query("city") String city,
                                                           @Query("category") String category);
}
