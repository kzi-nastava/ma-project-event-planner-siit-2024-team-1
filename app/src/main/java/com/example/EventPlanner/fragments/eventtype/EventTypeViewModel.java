package com.example.EventPlanner.fragments.eventtype;

import android.content.Intent;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.EventPlanner.activities.HomeScreen;
import com.example.EventPlanner.activities.LoginScreen;
import com.example.EventPlanner.clients.ClientUtils;
import com.example.EventPlanner.clients.JwtService;
import com.example.EventPlanner.model.auth.LoginResponse;
import com.example.EventPlanner.model.common.PageResponse;
import com.example.EventPlanner.model.event.CreateEventTypeRequest;
import com.example.EventPlanner.model.event.EventOverview;
import com.example.EventPlanner.model.event.EventTypeOverview;
import com.example.EventPlanner.model.event.UpdateEventTypeRequest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventTypeViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<EventTypeOverview>> eventTypes = new MutableLiveData<>();
    private final MutableLiveData<EventTypeOverview> selectedEventType = new MutableLiveData<>();

    public EventTypeViewModel() {
        // Initialize eventTypes LiveData with an empty list
        eventTypes.setValue(new ArrayList<>());
    }

    // Get LiveData for observing event types
    public MutableLiveData<ArrayList<EventTypeOverview>> getEventTypesLiveData() {
        return eventTypes;
    }

    public LiveData<ArrayList<EventTypeOverview>> getEventTypes(){
        return eventTypes;
    }

    public LiveData<EventTypeOverview> getSelectedEventType() {
        return selectedEventType;
    }

    // Fetch all event types from the server
    public void getAll() {
        Call<PageResponse<EventTypeOverview>> call = ClientUtils.eventTypeService.getAll();
        call.enqueue(new Callback<PageResponse<EventTypeOverview>>() {
            @Override
            public void onResponse(Call<PageResponse<EventTypeOverview>> call, Response<PageResponse<EventTypeOverview>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    eventTypes.postValue(new ArrayList<>(response.body().getContent()));
                } else {
                    Log.e("EventTypeViewModel", "Failed to fetch event types: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<PageResponse<EventTypeOverview>> call, Throwable t) {
                Log.e("EventTypeViewModel", "Error fetching event types", t);
            }
        });
    }

    // Fetch all event types from the server
    public void getAllWp() {
        Call<List<EventTypeOverview>> call = ClientUtils.eventTypeService.getAllActiveWithoutPagination();
        call.enqueue(new Callback<List<EventTypeOverview>>() {
            @Override
            public void onResponse(Call<List<EventTypeOverview>> call, Response<List<EventTypeOverview>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    eventTypes.postValue(new ArrayList<>(response.body()));
                } else {
                    Log.e("EventTypeViewModel", "Failed to fetch event types: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<EventTypeOverview>> call, Throwable t) {
                Log.e("EventTypeViewModel", "Error fetching event types", t);
            }
        });
    }

    // Find an event type by ID
    public void findEventTypeById(int id) {
        Call<EventTypeOverview> call = ClientUtils.eventTypeService.getById(id);
        call.enqueue(new Callback<EventTypeOverview>() {
            @Override
            public void onResponse(Call<EventTypeOverview> call, Response<EventTypeOverview> response) {
                if (response.isSuccessful() && response.body() != null) {
                    selectedEventType.postValue(response.body());
                } else {
                    Log.e("EventTypeViewModel", "Failed to fetch event type by ID: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<EventTypeOverview> call, Throwable t) {
                Log.e("EventTypeViewModel", "Error fetching event type by ID", t);
            }
        });
    }

    // Add or update an event type
    public void saveEventType(CreateEventTypeRequest dto) {
        Call<EventTypeOverview> call1 = ClientUtils.eventTypeService.create(dto);
        call1.enqueue(new Callback<EventTypeOverview>() {
            @Override
            public void onResponse(Call<EventTypeOverview> call, Response<EventTypeOverview> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ArrayList<EventTypeOverview> currentList = eventTypes.getValue();
                    if (currentList == null) {
                        currentList = new ArrayList<>();
                    }
                    currentList.add(response.body());
                    eventTypes.setValue(currentList);
                } else {
                    // Handle error cases
                    Log.e("Event Type Create Error", "Response not successful: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<EventTypeOverview> call, Throwable throwable) {
                // Handle network errors
                Log.e("Login Failure", "Error: " + throwable.getMessage());
            }
        });
    }

    public void updateEventType(int id, UpdateEventTypeRequest dto) {
        Call<EventTypeOverview> call = ClientUtils.eventTypeService.update(id, dto); // assuming you have an update endpoint
        call.enqueue(new Callback<EventTypeOverview>() {
            @Override
            public void onResponse(Call<EventTypeOverview> call, Response<EventTypeOverview> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Update the list of event types
                    ArrayList<EventTypeOverview> currentList = eventTypes.getValue();
                    if (currentList != null) {
                        // Replace the old event type with the updated one
                        for (int i = 0; i < currentList.size(); i++) {
                            if (currentList.get(i).getId() == response.body().getId()) {
                                currentList.set(i, response.body());
                                break;
                            }
                        }
                        eventTypes.setValue(currentList);
                    }
                } else {
                    Log.e("EventTypeViewModel", "Failed to update event type: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<EventTypeOverview> call, Throwable t) {
                Log.e("EventTypeViewModel", "Error updating event type", t);
            }
        });
    }

    // Delete an event type
    public void deleteEventType(EventTypeOverview eventType) {
        ArrayList<EventTypeOverview> eventTypeList = eventTypes.getValue();
        if (eventTypeList != null) {
            eventTypeList.remove(eventType);
            eventTypes.postValue(eventTypeList);
        }
    }

    // Set the list of event types
    public void setEventTypes(ArrayList<EventTypeOverview> eventTypes) {
        this.eventTypes.postValue(eventTypes);
    }
}
