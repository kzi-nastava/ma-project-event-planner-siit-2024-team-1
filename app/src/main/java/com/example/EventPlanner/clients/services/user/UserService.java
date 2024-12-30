package com.example.EventPlanner.clients.services.user;

import com.example.EventPlanner.model.event.FollowResponse;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserService {
    @POST("users/follow-event")
    Call<FollowResponse> followEvent(@Query("userId") int userId,
                                     @Query("eventId")int eventId);
}
