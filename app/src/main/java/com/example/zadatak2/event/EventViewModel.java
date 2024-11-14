package com.example.zadatak2.event;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Date;
import java.util.stream.Collectors;

public class EventViewModel extends ViewModel {
    private ArrayList<Event> events;
    public EventViewModel(){
        setEvents(new ArrayList<>(DummyEventGenerator.createDummyEvents(15)));
    }

    public ArrayList<Event> getAll() {
        return events;
    }

    public ArrayList<Event> getTop() {
        Date now = new Date();
        return events.stream()
                .filter(e -> e.getDate().after(now))
                .sorted(Comparator.comparing(Event::getDate))
                .limit(5)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }
}
