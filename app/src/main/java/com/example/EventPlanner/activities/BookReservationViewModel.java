package com.example.EventPlanner.activities;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.EventPlanner.clients.ClientUtils;
import com.example.EventPlanner.model.common.PageResponse;
import com.example.EventPlanner.model.event.EventOverview;
import com.example.EventPlanner.model.merchandise.service.Timeslot;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookReservationViewModel extends ViewModel {
    private final MutableLiveData<List<Timeslot>> timeslotMutableLiveData=new MutableLiveData<>();

    public LiveData<List<Timeslot>> getTimeslots() {
        return timeslotMutableLiveData;
    }

    public void getTimeslots(int merchandiseId){
        Call<List<Timeslot>> call = ClientUtils.serviceService.getTimeslots(merchandiseId);
        call.enqueue(new Callback<List<Timeslot>>() {
            @Override
            public void onResponse(Call<List<Timeslot>> call, Response<List<Timeslot>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    timeslotMutableLiveData.postValue(response.body());  // This gets just the list of events

                } else {

                }
            }

            @Override
            public void onFailure(Call<List<Timeslot>> call, Throwable t) {
                Log.d("jaje",t.getMessage());
            }
        });
    }
}
