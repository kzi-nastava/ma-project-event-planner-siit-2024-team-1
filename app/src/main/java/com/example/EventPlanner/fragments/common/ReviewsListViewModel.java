package com.example.EventPlanner.fragments.common;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.EventPlanner.clients.ClientUtils;
import com.example.EventPlanner.clients.JwtService;
import com.example.EventPlanner.model.common.ReviewOverview;
import com.example.EventPlanner.model.event.EventOverview;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewsListViewModel extends ViewModel {
    private final MutableLiveData<List<ReviewOverview>> reviewOverviewMutableLiveData=new MutableLiveData<>();

    public LiveData<List<ReviewOverview>> getReviews(){
        return reviewOverviewMutableLiveData;
    }

    public void getPendingReviews(){
        Call<List<ReviewOverview>> call = ClientUtils.reviewService.getPendingReviews();
        call.enqueue(new Callback<List<ReviewOverview>>() {
            @Override
            public void onResponse(Call<List<ReviewOverview>> call, Response<List<ReviewOverview>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    reviewOverviewMutableLiveData.postValue(response.body());  // This gets just the list of events

                } else {

                }
            }

            @Override
            public void onFailure(Call<List<ReviewOverview>> call, Throwable t) {
                Log.d("errrrror",t.getMessage());
            }
        });
    }
}
