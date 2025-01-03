package com.example.EventPlanner.clients.services.merchandise.product;

import com.example.EventPlanner.model.common.PageResponse;
import com.example.EventPlanner.model.event.CreateEventRequest;
import com.example.EventPlanner.model.event.CreatedEventResponse;
import com.example.EventPlanner.model.event.EventOverview;
import com.example.EventPlanner.model.event.UpdateEventRequest;
import com.example.EventPlanner.model.merchandise.MerchandiseOverview;
import com.example.EventPlanner.model.merchandise.product.CreateProductRequest;
import com.example.EventPlanner.model.merchandise.product.CreateProductResponse;
import com.example.EventPlanner.model.merchandise.product.ProductOverview;
import com.example.EventPlanner.model.merchandise.product.UpdateProductRequest;

import java.time.LocalDate;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
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

    @GET("products/sp/{spId}")
    Call<List<ProductOverview>> getBySp(@Path("spId") int spId);

    @GET("products/{id}")
    Call<ProductOverview> getById(@Path("id") int id);

    @POST("products")
    Call<CreateProductResponse> create(@Body CreateProductRequest dto);

    @PUT("products/{id}")
    Call<CreateProductResponse> update(@Path("id") int id, @Body UpdateProductRequest dto);

    @PUT("products/avail/{id}")
    Call<Boolean> avail(@Path("id") int id);

    @PUT("products/show/{id}")
    Call<Boolean> show(@Path("id") int id);

    @DELETE("products/{id}")
    Call<Boolean> delete(@Path("id") int id);
}
