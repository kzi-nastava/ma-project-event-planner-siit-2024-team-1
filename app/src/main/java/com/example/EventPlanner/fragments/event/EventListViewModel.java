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
import com.example.EventPlanner.model.event.CreateEventRequest;
import com.example.EventPlanner.model.event.CreateEventTypeRequest;
import com.example.EventPlanner.model.event.CreatedEventResponse;
import com.example.EventPlanner.model.event.Event;
import com.example.EventPlanner.model.event.EventOverview;
import com.example.EventPlanner.model.event.EventTypeOverview;
import com.example.EventPlanner.model.event.UpdateEventRequest;
import com.example.EventPlanner.model.event.UpdateEventTypeRequest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    public void getFavorites() {
        Call<List<EventOverview>> call = ClientUtils.eventService.getFavorites(JwtService.getIdFromToken());
        call.enqueue(new Callback<List<EventOverview>>() {
            @Override
            public void onResponse(Call<List<EventOverview>> call, Response<List<EventOverview>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    eventsLiveData.postValue(new ArrayList<>(response.body()));  // This gets just the list of events

                } else {

                }
            }

            @Override
            public void onFailure(Call<List<EventOverview>> call, Throwable t) {
                Log.d("jaje",t.getMessage());
            }
        });
    }

    public void getFollowed() {
        Call<List<EventOverview>> call = ClientUtils.eventService.getFollowed(JwtService.getIdFromToken());
        call.enqueue(new Callback<List<EventOverview>>() {
            @Override
            public void onResponse(Call<List<EventOverview>> call, Response<List<EventOverview>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    eventsLiveData.postValue(new ArrayList<>(response.body()));  // This gets just the list of events

                } else {

                }
            }

            @Override
            public void onFailure(Call<List<EventOverview>> call, Throwable t) {
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

    // Add or update an event type
    public void saveEvent(CreateEventRequest dto) {
        Call<CreatedEventResponse> call1 = ClientUtils.eventService.create(dto);
        call1.enqueue(new Callback<CreatedEventResponse>() {
            @Override
            public void onResponse(Call<CreatedEventResponse> call, Response<CreatedEventResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Initialize current list if it's null
                    ArrayList<EventOverview> currentList = eventsLiveData.getValue();
                    if (currentList == null) {
                        currentList = new ArrayList<>();
                    }

                    CreatedEventResponse created = response.body();
                    EventOverview newEvent = new EventOverview(
                            created.getId(),
                            created.getEventType().getTitle(),
                            created.getTitle(),
                            created.getDate(),
                            created.getAddress(),
                            created.getDescription(),
                            created.isPublic()
                    );

                    // Add the new event to the list
                    currentList.add(newEvent);

                    // Update the LiveData with the new list
                    eventsLiveData.setValue(currentList);
                } else {
                    // Handle error cases
                    Log.e("Event Create Error", "Response not successful: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<CreatedEventResponse> call, Throwable throwable) {
                // Handle network errors
                Log.e("Creating Event Failure", "Error: " + throwable.getMessage());
            }
        });
    }

    public void updateEvent(int id, UpdateEventRequest dto) {
        Call<CreatedEventResponse> call = ClientUtils.eventService.update(id, dto); // assuming you have an update endpoint
        call.enqueue(new Callback<CreatedEventResponse>() {
            @Override
            public void onResponse(Call<CreatedEventResponse> call, Response<CreatedEventResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Initialize current list if it's null
                    ArrayList<EventOverview> currentList = eventsLiveData.getValue();
                    if (currentList == null) {
                        currentList = new ArrayList<>();
                    }

                    CreatedEventResponse created = response.body();
                    EventOverview updatedEvent = new EventOverview(
                            created.getId(),
                            created.getEventType().getTitle(),
                            created.getTitle(),
                            created.getDate(),
                            created.getAddress(),
                            created.getDescription(),
                            created.isPublic()
                    );

                    // Find the event by ID and update it in the list
                    for (int i = 0; i < currentList.size(); i++) {
                        if (currentList.get(i).getId() == created.getId()) {
                            currentList.set(i, updatedEvent);  // Replace the old event with the updated one
                            break;
                        }
                    }

                    // Update the LiveData with the updated list
                    eventsLiveData.setValue(currentList);
                } else {
                    Log.e("Event Update Error", "Response not successful: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<CreatedEventResponse> call, Throwable throwable) {
                Log.e("Event Update Failure", "Error: " + throwable.getMessage());
            }
        });
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
