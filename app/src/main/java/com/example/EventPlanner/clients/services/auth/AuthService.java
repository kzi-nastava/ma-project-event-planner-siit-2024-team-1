package com.example.EventPlanner.clients.services.auth;

import com.example.EventPlanner.model.auth.AuthenticationResponse;
import com.example.EventPlanner.model.auth.LoginRequest;
import com.example.EventPlanner.model.auth.LoginResponse;
import com.example.EventPlanner.model.auth.RegisterEoRequest;
import com.example.EventPlanner.model.auth.RegisterEoResponse;
import com.example.EventPlanner.model.auth.RegisterSpRequest;
import com.example.EventPlanner.model.auth.RegisterSpResponse;
import com.example.EventPlanner.model.merchandise.Category1;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AuthService {
    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginRequest dto);
    @POST("auth/register-eo")
    Call<RegisterEoResponse> registerEo(@Body RegisterEoRequest dto, @Query("promotion") boolean promotion);
    @POST("auth/register-sp")
    Call<RegisterSpResponse> registerSp(@Body RegisterSpRequest dto, @Query("promotion") boolean promotion);
    @GET("auth/activate")
    Call<String> activateUser(@Query("token") int activationToken);

    @POST("auth/fast-register")
    Call<LoginResponse> fastRegister(@Query("email")String email);

    @PUT("auth/deactivate/{id}")
    Call<Boolean> deactivate(@Path("id") int id);

    @POST("auth/refresh_token")
    Call<AuthenticationResponse> refreshToken(@Header("Authorization") String authorization, @Body String refreshToken);
}
