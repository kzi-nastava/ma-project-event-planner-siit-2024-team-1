package com.example.EventPlanner.fragments.merchandise.service;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import com.example.EventPlanner.DummyMerchandiseGenerator;
import com.example.EventPlanner.clients.ClientUtils;
import com.example.EventPlanner.model.merchandise.service.CreateServiceRequest;
import com.example.EventPlanner.model.merchandise.service.Service;
import com.example.EventPlanner.model.merchandise.service.ServiceOverview;
import com.example.EventPlanner.model.merchandise.service.UpdateServiceRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceViewModel extends ViewModel {
    public ArrayList<Service> services;
    private final MutableLiveData<ArrayList<ServiceOverview>> serviceLiveData = new MutableLiveData<>();
    private final MutableLiveData<ServiceOverview> selectedService = new MutableLiveData<>();

    public ServiceViewModel() {
        setServices(new ArrayList<Service>(DummyMerchandiseGenerator.createDummyService(5)));
    }

    public ArrayList<Service> getAll() {
        return services;
    }

    public Service findServiceById(int id) {
        for(Service service : services) {
            if(service.getId() == id) {
                return service;
            }
        }
        return null;
    }

    public void setServices(ArrayList<Service> services) {
        this.services = services;
    }

    public LiveData<ArrayList<ServiceOverview>> getServices() { return serviceLiveData; }

    public LiveData<ServiceOverview> getSelectedService() { return selectedService; }

    public void getAllBySp(int spId) {
        Call<List<ServiceOverview>> call = ClientUtils.serviceService.getBySp(spId);
        call.enqueue(new Callback<List<ServiceOverview>>() {
            @Override
            public void onResponse(Call<List<ServiceOverview>> call, Response<List<ServiceOverview>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    serviceLiveData.postValue(new ArrayList<>(response.body()));
                }else {
                    Log.e("ServiceViewModel", "Failed to fetch Services: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<ServiceOverview>> call, Throwable throwable) {
                Log.e("ServiceViewModel", "Error fetching Services", throwable);
            }
        });
    }

    public void findById(int id) {
        Call<ServiceOverview> call = ClientUtils.serviceService.getServiceById(id);
        call.enqueue(new Callback<ServiceOverview>() {
            @Override
            public void onResponse(Call<ServiceOverview> call, Response<ServiceOverview> response) {
                if(response.isSuccessful() && response.body() != null) {
                    selectedService.postValue(response.body());
                }else {
                    Log.e("ServiceViewModel", "Failed to fetch Service by id: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ServiceOverview> call, Throwable throwable) {
                Log.e("ServiceViewModel", "Error fetching Service by id: ", throwable);
            }
        });
    }

    public void createService(CreateServiceRequest dto) {
        Call<ServiceOverview> call = ClientUtils.serviceService.create(dto);
        call.enqueue(new Callback<ServiceOverview>() {
            @Override
            public void onResponse(Call<ServiceOverview> call, Response<ServiceOverview> response) {
                if(response.isSuccessful() && response.body() != null) {
                    ArrayList<ServiceOverview> currentList = serviceLiveData.getValue();
                    if(currentList == null) {
                        currentList = new ArrayList<>();
                    }
                    ServiceOverview createdService = response.body();
                    currentList.add(createdService);
                    serviceLiveData.setValue(currentList);
                }else {
                    Log.e("ServiceViewModel", "Creating service not successful: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ServiceOverview> call, Throwable throwable) {
                Log.e("ServiceViewModel", "Error creating service: " + throwable.getMessage());
            }
        });
    }

    public void updateService(int serviceId, UpdateServiceRequest dto) {
        Call<ServiceOverview> call = ClientUtils.serviceService.update(serviceId, dto);
        call.enqueue(new Callback<ServiceOverview>() {
            @Override
            public void onResponse(Call<ServiceOverview> call, Response<ServiceOverview> response) {
                if(response.isSuccessful() && response.body() != null) {
                    ArrayList<ServiceOverview> currentList = serviceLiveData.getValue();
                    ServiceOverview updatedService = response.body();
                    if(currentList != null) {
                        for(int i = 0; i < currentList.size(); i++) {
                            if(currentList.get(i).getId() == serviceId) {
                                currentList.set(i, updatedService);
                                break;
                            }
                        }
                        serviceLiveData.setValue(currentList);
                    }
                }else {
                    Log.e("ServiceViewModel", "Failed to update service: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ServiceOverview> call, Throwable throwable) {
                Log.e("ServiceViewModel", "Error updating service: " + throwable.getMessage());
            }
        });
    }

    public void deleteService(int serviceId) {
        Call<Void> call = ClientUtils.serviceService.delete(serviceId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()) {
                    ArrayList<ServiceOverview> currentList = serviceLiveData.getValue();
                    if(currentList != null) {
                        for(int i = 0; i < currentList.size(); i++) {
                            if(currentList.get(i).getId() == serviceId) {
                                currentList.remove(i);
                                break;
                            }
                        }
                        serviceLiveData.setValue(currentList);
                    }
                }else {
                    Log.e("ServiceViewModel", "Failed to delete service: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                Log.e("ServiceViewModel", "Error deleting service: " + throwable.getMessage());
            }
        });
    }
}
