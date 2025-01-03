package com.example.EventPlanner.clients.services.eventType;

import com.example.EventPlanner.model.auth.LoginRequest;
import com.example.EventPlanner.model.auth.LoginResponse;
import com.example.EventPlanner.model.auth.RegisterEoRequest;
import com.example.EventPlanner.model.auth.RegisterEoResponse;
import com.example.EventPlanner.model.auth.RegisterSpRequest;
import com.example.EventPlanner.model.auth.RegisterSpResponse;
import com.example.EventPlanner.model.common.PageResponse;
import com.example.EventPlanner.model.event.CreateEventTypeRequest;
import com.example.EventPlanner.model.event.EventOverview;
import com.example.EventPlanner.model.event.EventTypeOverview;
import com.example.EventPlanner.model.event.UpdateEventTypeRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface EventTypeService {
    @GET("event-types/all")
    Call<PageResponse<EventTypeOverview>> getAll();
    @GET("event-types/all-wp")
    Call<List<EventTypeOverview>> getAllWithoutPagination();

    @GET("event-types/all-active-wp")
    Call<List<EventTypeOverview>> getAllActiveWithoutPagination();
    @GET("event-types/{id}")
    Call<EventTypeOverview> getById(@Path("id") int id);
    @POST("event-types")
    Call<EventTypeOverview> create(@Body CreateEventTypeRequest dto);
    @PUT("event-types/{id}")
    Call<EventTypeOverview> update(@Path("id") int id, @Body UpdateEventTypeRequest dto);
    @PUT("event-types/deactivate/{id}")
    Call<EventTypeOverview> deactivate(@Path("id") int id);
}
