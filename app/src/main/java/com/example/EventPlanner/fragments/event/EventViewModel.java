package com.example.EventPlanner.fragments.event;

import androidx.lifecycle.ViewModel;

import com.example.EventPlanner.DummyEventGenerator;
import com.example.EventPlanner.model.event.Event;

import java.util.ArrayList;

public class EventViewModel extends ViewModel {
    private ArrayList<Event> events;

    public EventViewModel() {
        // Set products with dummy data for now
        setEvents(new ArrayList<>(DummyEventGenerator.createDummyEvents(5)));
    }

    public ArrayList<Event> getTop() {
        return events;
    }
    // Returns all products
    public ArrayList<Event> getAll() {
        return events;
    }

    // Finds a product by ID
    public Event findEventById(int id) {
        for (Event event : events) {
            if (event.getId() != null && event.getId().equals(id)) {
                return event;
            }
        }
        return null;
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
