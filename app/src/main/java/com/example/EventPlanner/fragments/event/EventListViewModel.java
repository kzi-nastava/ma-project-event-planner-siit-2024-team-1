package com.example.EventPlanner.fragments.event;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.EventPlanner.DummyEventGenerator;
import com.example.EventPlanner.clients.ClientUtils;
import com.example.EventPlanner.clients.JwtService;
import com.example.EventPlanner.clients.TokenManager;
import com.example.EventPlanner.model.common.PageResponse;
import com.example.EventPlanner.model.event.CreatedEventResponse;
import com.example.EventPlanner.model.event.Event;
import com.example.EventPlanner.model.event.EventOverview;
import com.example.EventPlanner.model.event.EventTypeOverview;

import java.time.LocalDate;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventListViewModel extends ViewModel {
    public ArrayList<Event> events;
    private final MutableLiveData<ArrayList<EventOverview>> eventsLiveData=new MutableLiveData<>();
    private final MutableLiveData<CreatedEventResponse> selectedEvent = new MutableLiveData<>();
    public EventListViewModel() {
        // Set products with dummy data for now
        setEvents(new ArrayList<>(DummyEventGenerator.createDummyEvents(5)));
    }

    public LiveData<ArrayList<EventOverview>> getEvents(){
        return eventsLiveData;
    }

    public void getTop() {
        Call<PageResponse<EventOverview>> call = ClientUtils.eventService.getTop(-1);
        call.enqueue(new Callback<PageResponse<EventOverview>>() {
            @Override
            public void onResponse(Call<PageResponse<EventOverview>> call, Response<PageResponse<EventOverview>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    eventsLiveData.postValue(new ArrayList<>(response.body().getContent()));  // This gets just the list of events

                } else {

                }
            }

            @Override
            public void onFailure(Call<PageResponse<EventOverview>> call, Throwable t) {
                Log.d("jaje",t.getMessage());
            }
        });
    }
    // Returns all products
    public void search(boolean areEventsVisible,String searchText, LocalDate startDate, LocalDate endDate, String type,String city,String sortBy) {
        if(!areEventsVisible){
            eventsLiveData.postValue(new ArrayList<>());
            return;
        }
        Call<PageResponse<EventOverview>> call = ClientUtils.eventService.searchEvents(-1,searchText,startDate,endDate,type,city,sortBy);
        call.enqueue(new Callback<PageResponse<EventOverview>>() {
            @Override
            public void onResponse(Call<PageResponse<EventOverview>> call, Response<PageResponse<EventOverview>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    eventsLiveData.postValue(new ArrayList<>(response.body().getContent()));  // This gets just the list of events

                } else {

                }
            }

            @Override
            public void onFailure(Call<PageResponse<EventOverview>> call, Throwable t) {
                Log.d("ja",t.getMessage());
            }
        });
    }

    public void getByEo() {
        Call<PageResponse<EventOverview>> call = ClientUtils.eventService.getByEo(JwtService.getIdFromToken());
        call.enqueue(new Callback<PageResponse<EventOverview>>() {
            @Override
            public void onResponse(Call<PageResponse<EventOverview>> call, Response<PageResponse<EventOverview>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    eventsLiveData.postValue(new ArrayList<>(response.body().getContent()));  // This gets just the list of events

                } else {

                }
            }

            @Override
            public void onFailure(Call<PageResponse<EventOverview>> call, Throwable t) {
                Log.d("jaje",t.getMessage());
            }
        });
    }
    public LiveData<CreatedEventResponse> getSelectedEvent() {
        return selectedEvent;
    }
    // Find an event type by ID
    public void findEventById(int id) {
        Call<CreatedEventResponse> call = ClientUtils.eventService.getById(id);
        call.enqueue(new Callback<CreatedEventResponse>() {
            @Override
            public void onResponse(Call<CreatedEventResponse> call, Response<CreatedEventResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    selectedEvent.postValue(response.body());
                } else {
                    Log.e("EventViewModel", "Failed to fetch event by ID: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<CreatedEventResponse> call, Throwable t) {
                Log.e("EventViewModel", "Error fetching event by ID", t);
            }
        });
    }

    // Adds a new product
    public void saveEvent(Event event) {
        if (event.getId() == null) {
            // Assign a new ID (for simplicity, this just increments the size)
            event.setId(events.size() + 1);  // Assuming IDs are sequential
        }
        events.add(event);
    }

    // Deletes a product
    public void deleteEvent(Event event) {
        events.remove(event);
    }

    // Set the list of products
    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }
}
