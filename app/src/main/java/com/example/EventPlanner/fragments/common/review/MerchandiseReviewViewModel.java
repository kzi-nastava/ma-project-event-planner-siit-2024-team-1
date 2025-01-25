package com.example.EventPlanner.fragments.common.review;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.EventPlanner.clients.ClientUtils;
import com.example.EventPlanner.clients.JwtService;
import com.example.EventPlanner.model.common.merchandiseReview.ReviewMerchandiseRequest;
import com.example.EventPlanner.model.common.merchandiseReview.ReviewMerchandiseResponseDTO;
import com.example.EventPlanner.model.common.merchandiseReview.ReviewType;
import com.example.EventPlanner.model.event.EventDetailsDTO;
import com.example.EventPlanner.model.merchandise.DetailsReviewOverview;
import com.example.EventPlanner.model.merchandise.MerchandiseDetailsDTO;

import java.util.ArrayList;

public class MerchandiseReviewViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<DetailsReviewOverview>> reviews = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isEligibleForReview = new MutableLiveData<>();

    public MerchandiseReviewViewModel() {
        isEligibleForReview.setValue(true);
    }

    public LiveData<ArrayList<DetailsReviewOverview>> getReviews() { return this.reviews; }
    public LiveData<Boolean> getIsEligibleForReview() { return this.isEligibleForReview; }

    public void setReviews(int id, ReviewType reviewType) {
        if(reviewType == ReviewType.MERCHANDISE_REVIEW) {
            Call<MerchandiseDetailsDTO> call =  ClientUtils.merchandiseService.getMerchandiseById(id, JwtService.getIdFromToken());
            call.enqueue(new Callback<MerchandiseDetailsDTO>() {
                @Override
                public void onResponse(Call<MerchandiseDetailsDTO> call, Response<MerchandiseDetailsDTO> response) {
                    if(response.isSuccessful() && response.body() != null) {
                        if(response.body().getReviews() != null && !response.body().getReviews().isEmpty()) {
                            reviews.setValue(new ArrayList<>(response.body().getReviews()));
                        } else {
                            reviews.setValue(new ArrayList<>());
                        }
                    }else {
                        reviews.setValue(new ArrayList<>());
                        Log.e("MerchandiseReviewViewModel", "Failed to fetch merchandise details: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<MerchandiseDetailsDTO> call, Throwable throwable) {
                    reviews.setValue(new ArrayList<>());
                    Log.e("MerchandiseReviewViewModel", "Failed to fetch merchandise details: " + throwable.getMessage());
                }
            });
        }else if(reviewType == ReviewType.EVENT_REVIEW) {
            Call<EventDetailsDTO> call = ClientUtils.eventService.getDetails(id, JwtService.getIdFromToken());
            call.enqueue(new Callback<EventDetailsDTO>() {
                @Override
                public void onResponse(Call<EventDetailsDTO> call, Response<EventDetailsDTO> response) {
                    if(response.isSuccessful() && response.body() != null) {
                        if(response.body().getReviews() != null && !response.body().getReviews().isEmpty()) {
                            reviews.setValue(new ArrayList<>(response.body().getReviews()));
                        } else {
                            reviews.setValue(new ArrayList<>());
                        }
                    }else {
                        reviews.setValue(new ArrayList<>());
                        Log.e("MerchandiseReviewViewModel", "Failed to fetch event details: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<EventDetailsDTO> call, Throwable throwable) {
                    reviews.setValue(new ArrayList<>());
                    Log.e("MerchandiseReviewViewModel", "Failed to fetch event details: " + throwable.getMessage());
                }
            });
        }
    }

    public void leaveReview(int id, ReviewMerchandiseRequest dto) {
        Call<ReviewMerchandiseResponseDTO> call = ClientUtils.reviewService.leaveMerchandiseReview(id, dto);
        call.enqueue(new Callback<ReviewMerchandiseResponseDTO>() {
            @Override
            public void onResponse(Call<ReviewMerchandiseResponseDTO> call, Response<ReviewMerchandiseResponseDTO> response) {
                if(response.isSuccessful() && response.body() != null) {
                    isEligibleForReview.setValue(false);
                }else {
                    Log.e("MerchandiseReviewModel", "Failed to leave review: " + response.message());
                    isEligibleForReview.setValue(true);
                }
            }

            @Override
            public void onFailure(Call<ReviewMerchandiseResponseDTO> call, Throwable throwable) {
                Log.e("MerchandiseReviewModel", "Error leaving review: " + throwable.getMessage());
                isEligibleForReview.setValue(true);
            }
        });

    }

    public void checkIfEligibleForReview(int id, ReviewType reviewType) {
        Call<Boolean> call = ClientUtils.reviewService.isEligibleForReview(id, JwtService.getIdFromToken(), reviewType);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.isSuccessful() && response.body() != null) {
                    isEligibleForReview.setValue(true);
                }else {
                    Log.e("MerchandiseReviewModel", "Failed to check if eligible for review: " + response.message());
                    isEligibleForReview.setValue(false);
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable throwable) {
                Log.e("MerchandiseReviewModel", "Error checking if eligible for review: " + throwable.getMessage());
                isEligibleForReview.setValue(false);
            }
        });
    }
}
