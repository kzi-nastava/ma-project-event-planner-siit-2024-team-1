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
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MerchandiseViewModel extends ViewModel {
    private ArrayList<Merchandise> merchandises;
    private final MutableLiveData<ArrayList<MerchandiseOverview>> merchandiseLiveData=new MutableLiveData<>();
    private final AtomicInteger pendingResponses = new AtomicInteger(2);
    private final List<MerchandiseOverview> productResults = Collections.synchronizedList(new ArrayList<>());
    private final List<MerchandiseOverview> serviceResults = Collections.synchronizedList(new ArrayList<>());

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
                Log.d("errorTag",t.getMessage());
            }
        });
    }

    public void search(boolean areServicesVisible, boolean areProductsVisible, String searchText,
                       // Product-specific parameters
                       Double productPriceMin, Double productPriceMax,
                       Integer productDurationMin, Integer productDurationMax,
                       String productCity, String productCategory,
                       // Service-specific parameters
                       Double servicePriceMin, Double servicePriceMax,
                       Integer serviceDurationMin, Integer serviceDurationMax,
                       String serviceCity, String serviceCategory) {

        // Reset lists for new search
        productResults.clear();
        serviceResults.clear();

        // Set number of pending responses based on visibility
        int expectedResponses = 0;
        if (areProductsVisible) expectedResponses++;
        if (areServicesVisible) expectedResponses++;
        pendingResponses.set(expectedResponses);

        // If nothing is visible, return empty results immediately
        if (expectedResponses == 0) {
            merchandiseLiveData.postValue(new ArrayList<>());
            return;
        }

        // Products call
        if (areProductsVisible) {
            Call<PageResponse<MerchandiseOverview>> call = ClientUtils.productService
                    .searchProducts(-1, searchText, productPriceMin, productPriceMax,
                            productDurationMin, productDurationMax, productCity, productCategory);

            call.enqueue(new Callback<PageResponse<MerchandiseOverview>>() {
                @Override
                public void onResponse(Call<PageResponse<MerchandiseOverview>> call,
                                       Response<PageResponse<MerchandiseOverview>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        productResults.addAll(response.body().getContent());
                        checkAndCombineResults();
                    } else {
                        handleError();
                    }
                }

                @Override
                public void onFailure(Call<PageResponse<MerchandiseOverview>> call, Throwable t) {
                    Log.d("errorTag", "Products search failed: " + t.getMessage());
                    handleError();
                }
            });
        }

        // Services call
        if (areServicesVisible) {
            Call<PageResponse<MerchandiseOverview>> callTwo = ClientUtils.serviceService
                    .searchServices(-1, searchText, servicePriceMin, servicePriceMax,
                            serviceDurationMin, serviceDurationMax, serviceCity, serviceCategory);

            callTwo.enqueue(new Callback<PageResponse<MerchandiseOverview>>() {
                @Override
                public void onResponse(Call<PageResponse<MerchandiseOverview>> call,
                                       Response<PageResponse<MerchandiseOverview>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        serviceResults.addAll(response.body().getContent());
                        checkAndCombineResults();
                    } else {
                        handleError();
                    }
                }

                @Override
                public void onFailure(Call<PageResponse<MerchandiseOverview>> call, Throwable t) {
                    Log.d("errorTag", "Services search failed: " + t.getMessage());
                    handleError();
                }
            });
        }
    }

    private void checkAndCombineResults() {
        if (pendingResponses.decrementAndGet() == 0) {
            List<MerchandiseOverview> combinedResults = new ArrayList<>();
            combinedResults.addAll(productResults);
            combinedResults.addAll(serviceResults);
            merchandiseLiveData.postValue(new ArrayList<>(combinedResults));
        }
    }

    private void handleError() {
        if (pendingResponses.decrementAndGet() == 0) {
            List<MerchandiseOverview> combinedResults = new ArrayList<>();
            combinedResults.addAll(productResults);
            combinedResults.addAll(serviceResults);
            merchandiseLiveData.postValue(new ArrayList<>(combinedResults));
        }
    }
    public void setMerchandises(ArrayList<Merchandise> merchandises) {
        this.merchandises = merchandises;
    }
}
