package com.example.EventPlanner.clients.services.common;

import com.example.EventPlanner.model.common.ReviewOverview;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ReviewService {
    @GET("reviews/pending")
    Call<List<ReviewOverview>> getPendingReviews();

    @PUT("reviews/{id}/approve")
    Call<ReviewOverview> approveReview(@Path("id")int id);

    @PUT("reviews/{id}/deny")
    Call<ReviewOverview> denyReview(@Path("id")int id);
}
