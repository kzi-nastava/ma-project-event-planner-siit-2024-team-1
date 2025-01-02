package com.example.EventPlanner.clients.services.photo;

import com.example.EventPlanner.model.auth.LoginRequest;
import com.example.EventPlanner.model.auth.LoginResponse;
import com.example.EventPlanner.model.auth.RegisterEoRequest;
import com.example.EventPlanner.model.auth.RegisterEoResponse;
import com.example.EventPlanner.model.auth.RegisterSpRequest;
import com.example.EventPlanner.model.auth.RegisterSpResponse;

import javax.annotation.Resource;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

public interface PhotoService {

    @GET("photos/{filename}")
    Call<ResponseBody> getPhoto(@Path("filename") String filename);

    // Upload User Photo
    @Multipart
    @POST("photos/user/{id}")
    Call<Void> uploadUserPhoto(@Part MultipartBody.Part file, @Path("id") int userId);

    // Upload Business Photo
    @Multipart
    @POST("photos/business")
    Call<Integer> uploadBusinessPhoto(@Part MultipartBody.Part file);

    // Upload Merchandise Photo
    @Multipart
    @POST("photos/merchandise")
    Call<Integer> uploadMerchandisePhoto(@Part MultipartBody.Part file);

    // Delete Merchandise Photo
    @DELETE("photos/{mercId}/merchandise/{id}")
    Call<Void> deleteMerchandisePhoto(
            @Path("id") int id,
            @Path("mercId") int mercId,
            @Query("edit") boolean edit
    );

    // Delete User Photo
    @DELETE("photos/user/{id}")
    Call<Void> deleteUserPhoto(@Path("id") int id);

    // Delete Business Photo
    @DELETE("photos/{spId}/business/{id}")
    Call<Void> deleteBusinessPhoto(
            @Path("id") int id,
            @Path("spId") int spId,
            @Query("edit") boolean edit
    );
}
