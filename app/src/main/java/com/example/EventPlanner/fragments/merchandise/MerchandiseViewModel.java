package com.example.EventPlanner.fragments.merchandise;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.EventPlanner.DummyMerchandiseGenerator;
import com.example.EventPlanner.clients.ClientUtils;
import com.example.EventPlanner.model.common.PageResponse;
import com.example.EventPlanner.model.event.EventOverview;
import com.example.EventPlanner.model.merchandise.Merchandise;
import com.example.EventPlanner.model.merchandise.MerchandiseOverview;

import java.util.ArrayList;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MerchandiseViewModel extends ViewModel {
    private ArrayList<Merchandise> merchandises;
    private final MutableLiveData<ArrayList<MerchandiseOverview>> merchandiseLiveData=new MutableLiveData<>();

    public LiveData<ArrayList<MerchandiseOverview>> getMerchandise(){
        return merchandiseLiveData;
    }

    public MerchandiseViewModel(){
        setMerchandises(new ArrayList<>(DummyMerchandiseGenerator.createDummyMerchandise(5)));
    }

    public ArrayList<Merchandise> getAll() {
        return merchandises;
    }

    public void getTop() {
        Call<ArrayList<MerchandiseOverview>> call = ClientUtils.merchandiseService.getTop(-1);
        call.enqueue(new Callback<ArrayList<MerchandiseOverview>>() {
            @Override
            public void onResponse(Call<ArrayList<MerchandiseOverview>> call, Response<ArrayList<MerchandiseOverview>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    merchandiseLiveData.postValue(response.body());  // This gets just the list of events

                } else {

                }
            }

            @Override
            public void onFailure(Call<ArrayList<MerchandiseOverview>> call, Throwable t) {
                Log.d("jaje",t.getMessage());
            }
        });
    }
    public void setMerchandises(ArrayList<Merchandise> merchandises) {
        this.merchandises = merchandises;
    }
}
