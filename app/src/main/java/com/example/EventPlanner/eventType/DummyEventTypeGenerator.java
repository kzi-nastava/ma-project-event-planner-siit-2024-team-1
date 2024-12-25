package com.example.EventPlanner.eventType;

import com.example.EventPlanner.address.Address;
import com.example.EventPlanner.address.Address1;
import com.example.EventPlanner.eventType.EventType;
import com.example.EventPlanner.merchandise.MerchandisePhoto;
import com.example.EventPlanner.product.Product;
import com.example.EventPlanner.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DummyEventTypeGenerator {

    public static List<EventType> createDummyEventType(int count) {
        List<EventType> eventTypesList = new ArrayList<>();
        Random random = new Random();

        // Example data
        int id = 1;
        String title = "Tech Conference";
        String description = "Orgijanje";

// Creating a Product instance
        EventType eventType = new EventType(
                id,
                title,
                description,
                true,
                null
        );

        eventTypesList.add(eventType);
        eventTypesList.add(eventType);

        return eventTypesList;
    }

    // Helper method to create a dummy user
    private static User createDummyUser(int id) {
        String[] firstNames = {"John", "Jane", "Mike", "Sarah", "David"};
        String[] lastNames = {"Smith", "Johnson", "Williams", "Brown", "Jones"};
        Random random = new Random();

        Address1 address = new Address1(
                "City" + (id + 1),
                "Street" + (id + 1),
                String.valueOf(random.nextInt(999) + 1),
                String.format("%.6f", random.nextDouble() * 180 - 90),
                String.format("%.6f", random.nextDouble() * 180 - 90)
        );

        return new User(
                id + 1,
                firstNames[random.nextInt(firstNames.length)],
                lastNames[random.nextInt(lastNames.length)],
                address,
                String.format("+1-%03d-%03d-%04d",
                        random.nextInt(1000),
                        random.nextInt(1000),
                        random.nextInt(10000)),
                "user" + (id + 1) + "@example.com",
                "password" + (id + 1),
                "profile_" + (id + 1) + ".jpg",
                true
        );
    }
}
