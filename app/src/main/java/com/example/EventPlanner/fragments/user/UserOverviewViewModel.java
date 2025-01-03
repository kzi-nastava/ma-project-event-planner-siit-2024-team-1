package com.example.EventPlanner.fragments.user;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.EventPlanner.clients.ClientUtils;
import com.example.EventPlanner.clients.JwtService;
import com.example.EventPlanner.model.user.UserOverview;
import com.example.EventPlanner.model.user.UserReportOverview;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserOverviewViewModel extends ViewModel {
    private final MutableLiveData<List<UserOverview>> userOverviewMutableLiveData =new MutableLiveData<>();

    public LiveData<List<UserOverview>> getUsers(){
        return userOverviewMutableLiveData;
    }

    public void getChatUsers(){
        Call<List<UserOverview>> call = ClientUtils.userService.getChatUsers(JwtService.getIdFromToken());
        call.enqueue(new Callback<List<UserOverview>>() {
            @Override
            public void onResponse(Call<List<UserOverview>> call, Response<List<UserOverview>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    userOverviewMutableLiveData.postValue(response.body());  // This gets just the list of events

                } else {

                }
            }

            @Override
            public void onFailure(Call<List<UserOverview>> call, Throwable t) {
                Log.d("errrrror",t.getMessage());
            }
        });
    }
}
