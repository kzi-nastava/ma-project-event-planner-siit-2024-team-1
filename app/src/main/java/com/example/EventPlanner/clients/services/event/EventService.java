package com.example.EventPlanner.clients.services.event;

import com.example.EventPlanner.model.common.PageResponse;
import com.example.EventPlanner.model.event.Activity;
import com.example.EventPlanner.model.event.CreateActivityRequest;
import com.example.EventPlanner.model.event.CreateEventRequest;
import com.example.EventPlanner.model.event.CreatedEventResponse;
import com.example.EventPlanner.model.event.EventOverview;
import com.example.EventPlanner.model.event.EventReport;
import com.example.EventPlanner.model.event.InvitationResponse;
import com.example.EventPlanner.model.event.UpdateEventRequest;

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

public interface EventService {
    @GET("events/top")
    Call<PageResponse<EventOverview>> getTop(@Query("userId") int userId);
    @GET("events/eo/{eoId}")
    Call<PageResponse<EventOverview>> getByEo(@Path("eoId") int eoId);
    @GET("events/{userId}/favorite")
    Call<List<EventOverview>> getFavorites(@Path("userId") int userId);
    @GET("events/{id}")
    Call<CreatedEventResponse> getById(@Path("id") int id);

    @GET("events/{id}/agenda")
    Call<List<Activity>> getAgenda(@Path("id") int id);

    @GET("events/activities/{id}")
    Call<Activity> getActivity(@Path("id") int id);
    @GET("events/report/{id}")
    Call<EventReport> getEventReport(@Path("id") int id);
    @GET("events/search")
    Call<PageResponse<EventOverview>> searchEvents(@Query("userId") int userId,
                                                   @Query("search") String search,
                                                   @Query("startDate")LocalDate startDate,
                                                   @Query("endDate") LocalDate endDate,
                                                   @Query("type") String type,
                                                   @Query("city") String city,
                                                   @Query("sort") String sort);
    @POST("events/invite")
    Call<InvitationResponse> sendInvite(@Query("email") String email,
                                        @Query("eventId") int eventId);

    @POST("events")
    Call<CreatedEventResponse> create(@Body CreateEventRequest dto);

    @PUT("events/{id}")
    Call<CreatedEventResponse> update(@Path("id") int id, @Body UpdateEventRequest dto);

    @PUT("events/{id}/agenda")
    Call<Activity> updateAgenda(@Path("id") int id, @Body CreateActivityRequest dto);

    @PUT("events/agenda/{activityId}")
    Call<Activity> updateActivity(@Path("activityId") int activityId, @Body CreateActivityRequest dto);
    @DELETE("events/{eventId}/agenda/{activityId}")
    Call<List<Activity>> deleteAgenda(@Path("eventId") int eventId, @Path("activityId") int activityId);

    @POST("events/{eventId}/add-to-favorites/{userId}")
    Call<Boolean> favorizeEvent(@Path("eventId") int eventId, @Path("userId") int userId);
}
