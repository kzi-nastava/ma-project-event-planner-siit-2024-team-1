package com.example.EventPlanner.fragments.priceList;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.EventPlanner.clients.ClientUtils;
import com.example.EventPlanner.clients.JwtService;
import com.example.EventPlanner.model.priceList.PriceListItem;
import com.example.EventPlanner.model.priceList.UpdatePriceListItemRequest;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PriceListViewModel extends ViewModel {
    private MutableLiveData<ArrayList<PriceListItem>> priceList = new MutableLiveData<>();

    public PriceListViewModel() {}

    public LiveData<ArrayList<PriceListItem>> getPriceListItems() { return this.priceList; }

    public void getPriceList() {
        Call<List<PriceListItem>> priceListCall = ClientUtils.priceListService.getPriceList(JwtService.getIdFromToken());
        priceListCall.enqueue(new Callback<List<PriceListItem>>() {
            @Override
            public void onResponse(Call<List<PriceListItem>> call, Response<List<PriceListItem>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    priceList.setValue(new ArrayList<>(response.body()));
                }else {
                    Log.e("PriceListViewModel", "Failed to fetch price list: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<PriceListItem>> call, Throwable throwable) {
                Log.e("PriceListViewModel", "Error fetching price list: " + throwable.getMessage());
            }
        });
    }

    public void updatePriceListItem(int merchandiseId, UpdatePriceListItemRequest dto) {
        int spId = JwtService.getIdFromToken();
        Call<List<PriceListItem>> priceListCall = ClientUtils.priceListService.updatePriceListItem(merchandiseId, spId, dto);
        priceListCall.enqueue(new Callback<List<PriceListItem>>() {
            @Override
            public void onResponse(Call<List<PriceListItem>> call, Response<List<PriceListItem>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    priceList.setValue(new ArrayList<>(response.body()));
                }else {
                    Log.e("PriceListViewModel", "Failed to update price list item: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<PriceListItem>> call, Throwable throwable) {
                Log.e("PriceListViewModel", "Error updating price list item: " + throwable.getMessage());
            }
        });
    }
}
