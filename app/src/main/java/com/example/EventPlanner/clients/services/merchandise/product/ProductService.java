package com.example.EventPlanner.clients.services.merchandise.product;

import com.example.EventPlanner.model.common.PageResponse;
import com.example.EventPlanner.model.event.EventOverview;
import com.example.EventPlanner.model.merchandise.MerchandiseOverview;

import java.time.LocalDate;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ProductService {
    @GET("products/search")
    Call<PageResponse<MerchandiseOverview>> searchProducts(@Query("userId") int userId,
                                                           @Query("search") String search,
                                                           @Query("priceMin") Double priceMin,
                                                           @Query("priceMax") Double priceMax,
                                                           @Query("durationMin") Integer durationMin,
                                                           @Query("durationMax") Integer durationMax,
                                                           @Query("city") String city,
                                                           @Query("category") String category);
}
