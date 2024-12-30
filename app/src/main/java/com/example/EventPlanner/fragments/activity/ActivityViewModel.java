package com.example.EventPlanner.fragments.activity;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.EventPlanner.DummyActivityGenerator;
import com.example.EventPlanner.DummyEventGenerator;
import com.example.EventPlanner.clients.ClientUtils;
import com.example.EventPlanner.model.common.PageResponse;
import com.example.EventPlanner.model.event.Activity;
import com.example.EventPlanner.model.event.CreateActivityRequest;
import com.example.EventPlanner.model.event.CreateEventTypeRequest;
import com.example.EventPlanner.model.event.Event;
import com.example.EventPlanner.model.event.EventTypeOverview;
import com.example.EventPlanner.model.event.UpdateEventTypeRequest;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityViewModel extends ViewModel {
    private ArrayList<Activity> activities;

    private final MutableLiveData<ArrayList<Activity>> activitiesLiveData = new MutableLiveData<>();
    private final MutableLiveData<Activity> selectedActivity = new MutableLiveData<>();
    public ActivityViewModel() {
        // Set products with dummy data for now
        setActivities(new ArrayList<>(DummyActivityGenerator.createDummyActivity(5)));
    }

    public LiveData<ArrayList<Activity>> getActivities(){
        return activitiesLiveData;
    }

    public LiveData<Activity> getSelectedActivity() {
        return selectedActivity;
    }

    // Fetch all event types from the server
    public void getAll(int eventId) {
        Call<List<Activity>> call = ClientUtils.eventService.getAgenda(eventId);
        call.enqueue(new Callback<List<Activity>>() {
            @Override
            public void onResponse(Call<List<Activity>> call, Response<List<Activity>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    activitiesLiveData.postValue(new ArrayList<>(response.body()));
                } else {
                    Log.e("ActivityViewModel", "Failed to fetch agenda: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Activity>> call, Throwable t) {
                Log.e("EventTypeViewModel", "Error fetching agenda", t);
            }
        });
    }

    // Find an event type by ID
    public void findActivityById(int id) {
        Call<Activity> call = ClientUtils.eventService.getActivity(id);
        call.enqueue(new Callback<Activity>() {
            @Override
            public void onResponse(Call<Activity> call, Response<Activity> response) {
                if (response.isSuccessful() && response.body() != null) {
                    selectedActivity.postValue(response.body());
                } else {
                    Log.e("ActivityViewModel", "Failed to fetch Activity by ID: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Activity> call, Throwable t) {
                Log.e("ActivityViewModel", "Error fetching Activity by ID", t);
            }
        });
    }

    // Add or update an event type
    public void saveActivity(int eventId, CreateActivityRequest dto) {
        Call<Activity> call1 = ClientUtils.eventService.updateAgenda(eventId, dto);
        call1.enqueue(new Callback<Activity>() {
            @Override
            public void onResponse(Call<Activity> call, Response<Activity> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ArrayList<Activity> currentList = activitiesLiveData.getValue();
                    if (currentList == null) {
                        currentList = new ArrayList<>();
                    }
                    currentList.add(response.body());
                    activitiesLiveData.setValue(currentList);
                } else {
                    // Handle error cases
                    Log.e("Activity Create Error", "Response not successful: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Activity> call, Throwable throwable) {
                // Handle network errors
                Log.e("Activity create Failure", "Error: " + throwable.getMessage());
            }
        });
    }

    public void updateActivity(int activityId, CreateActivityRequest dto) {
        Call<Activity> call = ClientUtils.eventService.updateActivity(activityId, dto); // assuming you have an update endpoint
        call.enqueue(new Callback<Activity>() {
            @Override
            public void onResponse(Call<Activity> call, Response<Activity> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Update the list of event types
                    ArrayList<Activity> currentList = activitiesLiveData.getValue();
                    if (currentList != null) {
                        // Replace the old event type with the updated one
                        for (int i = 0; i < currentList.size(); i++) {
                            if (currentList.get(i).getId() == response.body().getId()) {
                                currentList.set(i, response.body());
                                break;
                            }
                        }
                        activitiesLiveData.setValue(currentList);
                    }
                } else {
                    Log.e("ActivityViewModel", "Failed to update Activity: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Activity> call, Throwable t) {
                Log.e("ActivityViewModel", "Error updating Activity", t);
            }
        });
    }

    public void deleteActivity(int eventId, int activityId) {
        // Call the delete API using both eventId and activityId
        Call<List<Activity>> call = ClientUtils.eventService.deleteAgenda(eventId, activityId);  // Assuming you have a delete endpoint that takes both IDs
        call.enqueue(new Callback<List<Activity>>() {
            @Override
            public void onResponse(Call<List<Activity>> call, Response<List<Activity>> response) {
                if (response.isSuccessful()) {
                    // Get the current list of activities
                    ArrayList<Activity> currentList = activitiesLiveData.getValue();
                    if (currentList != null) {
                        // Find and remove the activity with the given activityId
                        for (int i = 0; i < currentList.size(); i++) {
                            if (currentList.get(i).getId() == activityId) {
                                currentList.remove(i);  // Remove the activity from the list
                                break;
                            }
                        }
                        // Update the LiveData with the new list
                        activitiesLiveData.setValue(currentList);
                    }
                } else {
                    // Handle error cases
                    Log.e("Activity Delete Error", "Response not successful: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Activity>> call, Throwable throwable) {
                // Handle network errors
                Log.e("Activity Delete Failure", "Error: " + throwable.getMessage());
            }
        });
    }

    // Set the list of products
    public void setActivities(ArrayList<Activity> activities) {
        this.activities = activities;
    }
}