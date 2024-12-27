package com.example.EventPlanner.clients.services.event;

import com.example.EventPlanner.model.common.PageResponse;
import com.example.EventPlanner.model.event.EventOverview;

import java.time.LocalDate;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface EventService {
    @GET("events/top")
    Call<PageResponse<EventOverview>> getTop(@Query("userId") int userId);
    @GET("events/search")
    Call<PageResponse<EventOverview>> searchEvents(@Query("userId") int userId,
                                                   @Query("search") String search,
                                                   @Query("startDate")LocalDate startDate,
                                                   @Query("endDate") LocalDate endDate,
                                                   @Query("type") String type,
                                                   @Query("city") String city);
}
