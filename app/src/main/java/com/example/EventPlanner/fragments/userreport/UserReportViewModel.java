package com.example.EventPlanner.fragments.userreport;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.EventPlanner.clients.ClientUtils;
import com.example.EventPlanner.model.user.UserReportOverview;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserReportViewModel extends ViewModel {
    private final MutableLiveData<List<UserReportOverview>> userReportOverviewMutableLiveData =new MutableLiveData<>();

    public LiveData<List<UserReportOverview>> getReports(){
        return userReportOverviewMutableLiveData;
    }

    public void getPendingReports(){
        Call<List<UserReportOverview>> call = ClientUtils.userReportService.getPendingReports();
        call.enqueue(new Callback<List<UserReportOverview>>() {
            @Override
            public void onResponse(Call<List<UserReportOverview>> call, Response<List<UserReportOverview>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    userReportOverviewMutableLiveData.postValue(response.body());  // This gets just the list of events

                } else {

                }
            }

            @Override
            public void onFailure(Call<List<UserReportOverview>> call, Throwable t) {
                Log.d("errrrror",t.getMessage());
            }
        });
    }
}
