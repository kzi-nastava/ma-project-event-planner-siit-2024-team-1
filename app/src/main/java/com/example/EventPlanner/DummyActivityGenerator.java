package com.example.EventPlanner;

import com.example.EventPlanner.model.common.Address1;
import com.example.EventPlanner.model.event.Activity;
import com.example.EventPlanner.model.event.EventType;
import com.example.EventPlanner.model.common.Address;
import com.example.EventPlanner.model.user.User;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DummyActivityGenerator {

    public static List<Activity> createDummyActivity(int count) {
        List<Activity> activityList = new ArrayList<>();
        Random random = new Random();

        // Example data
        int id = 1;
        String title = "Rucak";
        String description = "Opis aktivnosti rucavanja";
        LocalTime start = LocalTime.now();
        LocalTime end = LocalTime.now();

        String[] cities = {"New York", "Los Angeles", "Chicago", "Houston", "Phoenix"};
        String[] streets = {"Main St", "Broadway", "Park Ave", "Oak Lane", "Maple Dr"};
        String[] categories = {"Electronics", "Furniture", "Sports", "Books", "Fashion"};
        String[] titles = {"Premium Item", "Best Seller", "New Arrival", "Featured Product", "Limited Edition"};

        // Create Address
        Address address = new Address(
                cities[random.nextInt(cities.length)],
                streets[random.nextInt(streets.length)],
                String.valueOf(random.nextInt(999) + 1),
                22.1,  // longitude
                22.3   // latitude
        );

// Creating a Product instance
        Activity activity = new Activity(
                id,
                title,
                description,
                start,
                end,
                address
        );

        activityList.add(activity);
        activityList.add(activity);

        return activityList;
    }
}
