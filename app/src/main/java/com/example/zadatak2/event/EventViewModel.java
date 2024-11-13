package com.example.zadatak2.event;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class EventViewModel extends ViewModel {
    private ArrayList<Event> events;
    public EventViewModel(){
        setEvents(new ArrayList<>(DummyEventGenerator.createDummyEvents(5)));
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }
}
