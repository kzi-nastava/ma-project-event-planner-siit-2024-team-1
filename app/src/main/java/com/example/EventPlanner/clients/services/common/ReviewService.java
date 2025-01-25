package com.example.EventPlanner.clients.services.common;

import com.example.EventPlanner.model.common.ReviewOverview;
import com.example.EventPlanner.model.common.merchandiseReview.ReviewMerchandiseRequest;
import com.example.EventPlanner.model.common.merchandiseReview.ReviewMerchandiseResponseDTO;
import com.example.EventPlanner.model.common.merchandiseReview.ReviewType;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ReviewService {
    @GET("reviews/pending")
    Call<List<ReviewOverview>> getPendingReviews();

    @PUT("reviews/{id}/approve")
    Call<ReviewOverview> approveReview(@Path("id")int id);

    @PUT("reviews/{id}/deny")
    Call<ReviewOverview> denyReview(@Path("id")int id);

    @POST("reviews/{id}/add")
    Call<ReviewMerchandiseResponseDTO> leaveMerchandiseReview(@Path("id") int id,
                                                              @Body ReviewMerchandiseRequest dto);

    @GET("reviews/display-review/{id}/{user_id}")
    Call<Boolean> isEligibleForReview(@Path("id") int id,
                                      @Path("user_id") int user_id,
                                      @Query("reviewType") ReviewType reviewType);
}
