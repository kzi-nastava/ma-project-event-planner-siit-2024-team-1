package com.example.EventPlanner.fragments.eventtype;

import androidx.lifecycle.ViewModel;

import com.example.EventPlanner.DummyEventTypeGenerator;
import com.example.EventPlanner.model.event.EventType;

import java.util.ArrayList;

public class EventTypeViewModel extends ViewModel {
    private ArrayList<EventType> eventTypes;

    public EventTypeViewModel() {
        // Set products with dummy data for now
        setEventTypes(new ArrayList<>(DummyEventTypeGenerator.createDummyEventType(5)));
    }

    // Returns all products
    public ArrayList<EventType> getAll() {
        return eventTypes;
    }

    // Finds a product by ID
    public EventType findEventTypeById(int id) {
        for (EventType eventType : eventTypes) {
            if (eventType.getId() != null && eventType.getId().equals(id)) {
                return eventType;
            }
        }
        return null;
    }

    // Adds a new product
    public void saveEventType(EventType eventType) {
        if (eventType.getId() == null) {
            // Assign a new ID (for simplicity, this just increments the size)
            eventType.setId(eventTypes.size() + 1);  // Assuming IDs are sequential
        }
        eventTypes.add(eventType);
    }

    // Deletes a product
    public void deleteEventType(EventType eventType) {
        eventTypes.remove(eventType);
    }

    // Set the list of products
    public void setEventTypes(ArrayList<EventType> eventTypes) {
        this.eventTypes = eventTypes;
    }
}

