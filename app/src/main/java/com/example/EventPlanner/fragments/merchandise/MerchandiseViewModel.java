package com.example.EventPlanner.fragments.merchandise;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.EventPlanner.DummyMerchandiseGenerator;
import com.example.EventPlanner.clients.ClientUtils;
import com.example.EventPlanner.clients.JwtService;
import com.example.EventPlanner.model.common.PageResponse;
import com.example.EventPlanner.model.event.EventOverview;
import com.example.EventPlanner.model.merchandise.Merchandise;
import com.example.EventPlanner.model.merchandise.MerchandiseDetailsDTO;
import com.example.EventPlanner.model.merchandise.MerchandiseOverview;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    private final MutableLiveData<MerchandiseDetailsDTO> merchandiseDetails = new MutableLiveData<>();

    public LiveData<ArrayList<MerchandiseOverview>> getMerchandise(){
        return merchandiseLiveData;
    }

    public MerchandiseViewModel(){
        setMerchandises(new ArrayList<>(DummyMerchandiseGenerator.createDummyMerchandise(5)));
    }

    public ArrayList<Merchandise> getAll() {
        return merchandises;
    }

    public MutableLiveData<MerchandiseDetailsDTO> getMerchandiseDetails() { return merchandiseDetails; }

    public void getTop() {
        Call<ArrayList<MerchandiseOverview>> call = ClientUtils.merchandiseService.getTop(JwtService.getIdFromToken());
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
                       String serviceCity, String serviceCategory,String sortBy,boolean ascending) {

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
                    .searchProducts(JwtService.getIdFromToken(), searchText, productPriceMin, productPriceMax,
                            productDurationMin, productDurationMax, productCity, productCategory);

            call.enqueue(new Callback<PageResponse<MerchandiseOverview>>() {
                @Override
                public void onResponse(Call<PageResponse<MerchandiseOverview>> call,
                                       Response<PageResponse<MerchandiseOverview>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        productResults.addAll(response.body().getContent());
                        checkAndCombineResults(sortBy,ascending);
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
                    .searchServices(JwtService.getIdFromToken(), searchText, servicePriceMin, servicePriceMax,
                            serviceDurationMin, serviceDurationMax, serviceCity, serviceCategory);

            callTwo.enqueue(new Callback<PageResponse<MerchandiseOverview>>() {
                @Override
                public void onResponse(Call<PageResponse<MerchandiseOverview>> call,
                                       Response<PageResponse<MerchandiseOverview>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        serviceResults.addAll(response.body().getContent());
                        checkAndCombineResults(sortBy,ascending);
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

    public void merchandiseDetails(int merchandiseId) {
        Call<MerchandiseDetailsDTO> call = ClientUtils.merchandiseService.getMerchandiseById(merchandiseId, JwtService.getIdFromToken());
        call.enqueue(new Callback<MerchandiseDetailsDTO>() {
            @Override
            public void onResponse(Call<MerchandiseDetailsDTO> call, Response<MerchandiseDetailsDTO> response) {
                if(response.isSuccessful() && response.body() != null) {
                    MerchandiseDetailsDTO merchandise = response.body();
                    merchandiseDetails.setValue(merchandise);
                }else {
                    Log.e("MerchandiseViewModel", "Failed to fetch detailed merchandise: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<MerchandiseDetailsDTO> call, Throwable throwable) {
                Log.e("MerchandiseViewModel", "Error fetching detailed merchandise: " + throwable.getMessage());
            }
        });
    }

    private void checkAndCombineResults(String sortBy, boolean ascending) {
        if (pendingResponses.decrementAndGet() == 0) {
            List<MerchandiseOverview> combinedResults = new ArrayList<>();
            combinedResults.addAll(productResults);
            combinedResults.addAll(serviceResults);

            Comparator<MerchandiseOverview> comparator = new Comparator<MerchandiseOverview>() {
                @Override
                public int compare(MerchandiseOverview m1, MerchandiseOverview m2) {
                    int result;
                    switch (sortBy) {
                        case "title":
                            result = compareNullable(m1.getTitle(), m2.getTitle());
                            break;
                        case "description":
                            result = compareNullable(m1.getDescription(), m2.getDescription());
                            break;
                        case "price":
                            result = compareNullable(m1.getPrice(), m2.getPrice());
                            break;
                        case "rating":
                            result = compareNullable(m1.getRating(), m2.getRating());
                            break;
                        case "category":
                            result = compareNullable(m1.getCategory(), m2.getCategory());
                            break;
                        case "type":
                            result = compareNullable(m1.getType(), m2.getType());
                            break;
                        default:
                            result = compareNullable(m1.getTitle(), m2.getTitle());
                    }
                    return ascending ? result : -result;
                }

                private <T extends Comparable<T>> int compareNullable(T o1, T o2) {
                    if (o1 == null && o2 == null) return 0;
                    if (o1 == null) return 1;
                    if (o2 == null) return -1;
                    return o1.compareTo(o2);
                }
            };

            Collections.sort(combinedResults, comparator);
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
