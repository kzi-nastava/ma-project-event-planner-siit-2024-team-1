package com.example.EventPlanner.event;

import androidx.lifecycle.ViewModel;

import com.example.EventPlanner.eventType.DummyEventTypeGenerator;
import com.example.EventPlanner.eventType.EventType;
import com.example.EventPlanner.merchandise.DummyMerchandiseGenerator;
import com.example.EventPlanner.product.DummyProductGenerator;
import com.example.EventPlanner.product.Product;

import java.util.ArrayList;

public class EventViewModel extends ViewModel {
    private ArrayList<com.example.EventPlanner.event.Event> events;

    public EventViewModel() {
        // Set products with dummy data for now
        setEvents(new ArrayList<>(DummyEventGenerator.createDummyEvents(5)));
    }

    public ArrayList<com.example.EventPlanner.event.Event> getTop() {
        return events;
    }
    // Returns all products
    public ArrayList<com.example.EventPlanner.event.Event> getAll() {
        return events;
    }

    // Finds a product by ID
    public com.example.EventPlanner.event.Event findEventById(int id) {
        for (com.example.EventPlanner.event.Event event : events) {
            if (event.getId() != null && event.getId().equals(id)) {
                return event;
            }
        }
        return null;
    }

    // Adds a new product
    public void saveEvent(com.example.EventPlanner.event.Event event) {
        if (event.getId() == null) {
            // Assign a new ID (for simplicity, this just increments the size)
            event.setId(events.size() + 1);  // Assuming IDs are sequential
        }
        events.add(event);
    }

    // Deletes a product
    public void deleteEvent(com.example.EventPlanner.event.Event event) {
        events.remove(event);
    }

    // Set the list of products
    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }
}
