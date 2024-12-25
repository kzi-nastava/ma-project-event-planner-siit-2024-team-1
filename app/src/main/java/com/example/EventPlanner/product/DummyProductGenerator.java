package com.example.EventPlanner.product;

import static com.example.EventPlanner.merchandise.DummyMerchandiseGenerator.createDummyUser;

import com.example.EventPlanner.address.Address;
import com.example.EventPlanner.address.Address1;
import com.example.EventPlanner.category.Category;
import com.example.EventPlanner.merchandise.MerchandisePhoto;
import com.example.EventPlanner.review.Review;
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
