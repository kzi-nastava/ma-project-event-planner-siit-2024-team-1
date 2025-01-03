package com.example.EventPlanner.clients.services.user;

import com.example.EventPlanner.model.event.FollowResponse;
import com.example.EventPlanner.model.user.BlockUserDTO;
import com.example.EventPlanner.model.user.ChangePasswordRequest;
import com.example.EventPlanner.model.user.ChangePasswordResponse;
import com.example.EventPlanner.model.user.GetEoById;
import com.example.EventPlanner.model.user.GetSpById;
import com.example.EventPlanner.model.user.UpdateEoRequest;
import com.example.EventPlanner.model.user.UpdateEoResponse;
import com.example.EventPlanner.model.user.UpdateSpRequest;
import com.example.EventPlanner.model.user.UpdateSpResponse;
import com.example.EventPlanner.model.user.UserOverview;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserService {
    @GET("users/{id}")
    Call<GetEoById> getAuById(@Path("id") int id);
    @GET("users/eo/{id}")
    Call<GetEoById> getEoById(@Path("id") int id);
    @GET("users/sp/{id}")
    Call<GetSpById> getSpById(@Path("id") int id);
    @GET("users/admin/{id}")
    Call<GetEoById> getAdminById(@Path("id") int id);
    @PUT("users/update-eo/{id}")
    Call<UpdateEoResponse> updateEo(@Path("id") int id, @Body UpdateEoRequest dto);
    @PUT("users/update-sp/{id}")
    Call<UpdateSpResponse> updateSp(@Path("id") int id, @Body UpdateSpRequest dto);
    @PUT("users/change-password/{id}")
    Call<ChangePasswordResponse> changePassword(@Path("id") int id, @Body ChangePasswordRequest dto);

    @POST("users/follow-event")
    Call<FollowResponse> followEvent(@Query("userId") int userId,
                                     @Query("eventId")int eventId);

    @GET("users/{id}/chat-users")
    Call<List<UserOverview>> getChatUsers(@Path("id") int id);

    @POST("users/{blockerId}/block/{blockedUserId}")
    Call<BlockUserDTO> blockUser(@Path("blockerId")int blockerId,@Path("blockedUserId") int blockedUserId);
}
