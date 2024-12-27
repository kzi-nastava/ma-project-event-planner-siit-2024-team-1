package com.example.EventPlanner.fragments.activity;

import androidx.lifecycle.ViewModel;

import com.example.EventPlanner.DummyActivityGenerator;
import com.example.EventPlanner.DummyEventGenerator;
import com.example.EventPlanner.model.event.Activity;
import com.example.EventPlanner.model.event.Event;

import java.util.ArrayList;

public class ActivityViewModel extends ViewModel {
    private ArrayList<Activity> activities;

    public ActivityViewModel() {
        // Set products with dummy data for now
        setActivities(new ArrayList<>(DummyActivityGenerator.createDummyActivity(5)));
    }
    // Returns all products
    public ArrayList<Activity> getAll() {
        return activities;
    }

    // Finds a product by ID
    public Activity findActivityById(int id) {
        for (Activity activity : activities) {
            if (activity.getId() != null && activity.getId().equals(id)) {
                return activity;
            }
        }
        return null;
    }

    // Adds a new product
    public void saveActivity(Activity activity) {
        if (activity.getId() == null) {
            // Assign a new ID (for simplicity, this just increments the size)
            activity.setId(activities.size() + 1);  // Assuming IDs are sequential
        }
        activities.add(activity);
    }

    // Deletes a product
    public void deleteActivity(Activity activity) {
        activities.remove(activity);
    }

    // Set the list of products
    public void setActivities(ArrayList<Activity> activities) {
        this.activities = activities;
    }
}