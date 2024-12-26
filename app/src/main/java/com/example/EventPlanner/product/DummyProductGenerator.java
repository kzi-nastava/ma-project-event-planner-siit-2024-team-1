package com.example.EventPlanner.product;

import com.example.EventPlanner.address.Address;
import com.example.EventPlanner.address.Address1;
import com.example.EventPlanner.eventType.EventType;
import com.example.EventPlanner.merchandise.MerchandisePhoto;
import com.example.EventPlanner.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DummyProductGenerator {

    public static List<Product> createDummyProduct(int count) {
        List<Product> productsList = new ArrayList<>();
        Random random = new Random();

        // Example data
        int id = 1;
        String title = "Product Title";
        String description = "Product Description";
        String specificity = "High-quality material";
        double price = 499.99;
        int discount = 10;
        boolean visible = true;
        boolean available = true;
        int minDuration = 30; // e.g., in minutes
        int maxDuration = 120; // e.g., in minutes
        int reservationDeadline = 24; // e.g., in hours
        int cancellationDeadline = 12; // e.g., in hours
        boolean automaticReservation = false;

// Assuming you have these objects
        List<MerchandisePhoto> merchandisePhotos = new ArrayList<>();
        List<EventType> eventTypes = new ArrayList<>();
        Address address = new Address("Vojvode Putnika", "Rumenka", "8", 22.0, 34.0);
        com.example.EventPlanner.product.Category category = new com.example.EventPlanner.product.Category(1, "Funerality", "Opis", false);

// Creating a Product instance
        Product product = new Product(
                id,
                title,
                description,
                specificity,
                price,
                discount,
                visible,
                available,
                minDuration,
                maxDuration,
                reservationDeadline,
                cancellationDeadline,
                automaticReservation,
                merchandisePhotos,
                eventTypes,
                address,
                category
        );

        productsList.add(product);
        productsList.add(product);

        return productsList;
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
                11.0,
                12.2
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
