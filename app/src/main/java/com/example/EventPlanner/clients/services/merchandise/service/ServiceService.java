package com.example.EventPlanner.clients.services.merchandise.service;

import com.example.EventPlanner.model.common.PageResponse;
import com.example.EventPlanner.model.event.EventOverview;
import com.example.EventPlanner.model.merchandise.MerchandiseOverview;
import com.example.EventPlanner.model.merchandise.service.ReservationRequest;
import com.example.EventPlanner.model.merchandise.service.ReservationResponse;
import com.example.EventPlanner.model.merchandise.service.Timeslot;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
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
    @GET("services/{serviceId}/timeslots")
    Call<List<Timeslot>> getTimeslots(@Path("serviceId") int serviceId);

    @POST("services/{serviceId}/reserve")
    Call<ReservationResponse> reserveService(@Path("serviceId") int serviceId, @Body ReservationRequest reservationRequest);
}
