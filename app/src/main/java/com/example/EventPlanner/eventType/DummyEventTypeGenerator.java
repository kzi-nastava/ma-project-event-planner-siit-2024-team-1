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

        Address address = new Address(
                "City" + (id + 1),
                "Street" + (id + 1),
                String.valueOf(random.nextInt(999) + 1),
                22.0,
                11.5
        );

        // Create a User object using the constructor
        return new User(
                1,                                // id
                "John",                           // name
                "Doe",                            // surname
                "+123456789",                     // phoneNumber
                address,                          // address
                "johndoe",                        // username
                "password123",                    // password
                "https://example.com/photo.jpg",  // photo
                2,                                // role (e.g., Admin or User role ID)
                "ROLE_USER",                      // authorities
                true,                             // active
                "abc123activationToken",          // activationToken
                "2024-12-31T23:59:59Z"           // tokenExpiration
        );
    }
}
