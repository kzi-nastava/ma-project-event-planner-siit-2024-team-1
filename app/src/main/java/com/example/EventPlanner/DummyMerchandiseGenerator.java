package com.example.EventPlanner;

import com.example.EventPlanner.model.merchandise.Merchandise;
import com.example.EventPlanner.model.common.Address;
import com.example.EventPlanner.model.common.Address1;
import com.example.EventPlanner.model.merchandise.Category;
import com.example.EventPlanner.model.merchandise.product.Product1;
import com.example.EventPlanner.model.common.Review;
import com.example.EventPlanner.model.merchandise.service.Service;
import com.example.EventPlanner.model.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DummyMerchandiseGenerator {
    public static List<Product1> createDummyProduct(int count) {
        List<Product1>productsList = new ArrayList<>();
        Random random = new Random();

        String[] cities = {"New York", "Los Angeles", "Chicago", "Houston", "Phoenix"};
        String[] streets = {"Main St", "Broadway", "Park Ave", "Oak Lane", "Maple Dr"};
        String[] categories = {"Electronics", "Furniture", "Sports", "Books", "Fashion"};
        String[] titles = {"Premium Item", "Best Seller", "New Arrival", "Featured Product", "Limited Edition"};

        for (int i = 0; i < count; i++) {
            // Create Address
            Address1 address = new Address1(
                    cities[random.nextInt(cities.length)],
                    streets[random.nextInt(streets.length)],
                    String.valueOf(random.nextInt(999) + 1),
                    String.format("%.6f", random.nextDouble() * 180 - 90),  // longitude
                    String.format("%.6f", random.nextDouble() * 180 - 90)   // latitude
            );

            // Create Category
            Category category = new Category(
                    i + 1,
                    categories[random.nextInt(categories.length)],
                    "Description for " + categories[random.nextInt(categories.length)],
                    random.nextBoolean()
            );

            // Create some reviews
            List<Review> reviews = new ArrayList<>();
            int numReviews = random.nextInt(5) + 1;
            for (int j = 0; j < numReviews; j++) {
                User user = createDummyUser(j);
                Review review = new Review(
                        j + 1,
                        user,
                        "Great product! Review #" + (j + 1),
                        random.nextInt(5) + 1
                );
                reviews.add(review);
            }

            // Create photo list
            List<String> photos = new ArrayList<>();
            int numPhotos = random.nextInt(3) + 1;
            for (int k = 0; k < numPhotos; k++) {
                photos.add("photo_" + (i + 1) + "_" + (k + 1) + ".jpg");
            }

            // Create Merchandise
            Product1 product = new Product1(
                    i + 1,
                    titles[random.nextInt(titles.length)] + " " + (i + 1),
                    "Detailed description for item " + (i + 1),
                    "Specific details for item " + (i + 1),
                    random.nextDouble() * 1000 + 10,  // price between 10 and 1010
                    random.nextDouble() * 0.5,        // discount between 0 and 0.5 (50%)
                    random.nextBoolean(),             // visible
                    random.nextBoolean(),             // available
                    random.nextInt(5) + 1,            // minDuration
                    random.nextInt(10) + 6,           // maxDuration
                    random.nextInt(24) + 1,           // reservationDeadline
                    random.nextInt(48) + 24,          // cancelReservation
                    random.nextBoolean(),             // automaticReservation
                    false,                            // deleted
                    photos,
                    category,
                    address,
                    reviews
            );

            productsList.add(product);
        }

        return productsList;
    }

    public static List<Service> createDummyService(int count) {
        List<Service> servicesList = new ArrayList<>();
        Random random = new Random();

        String[] cities = {"New York", "Los Angeles", "Chicago", "Houston", "Phoenix"};
        String[] streets = {"Main St", "Broadway", "Park Ave", "Oak Lane", "Maple Dr"};
        String[] categories = {"Electronics", "Furniture", "Sports", "Books", "Fashion"};
        String[] titles = {"Premium Item", "Best Seller", "New Arrival", "Featured Product", "Limited Edition"};

        for (int i = 0; i < count; i++) {
            // Create Address
            Address1 address = new Address1(
                    cities[random.nextInt(cities.length)],
                    streets[random.nextInt(streets.length)],
                    String.valueOf(random.nextInt(999) + 1),
                    String.format("%.6f", random.nextDouble() * 180 - 90),  // longitude
                    String.format("%.6f", random.nextDouble() * 180 - 90)   // latitude
            );

            // Create Category
            Category category = new Category(
                    i + 1,
                    categories[random.nextInt(categories.length)],
                    "Description for " + categories[random.nextInt(categories.length)],
                    random.nextBoolean()
            );

            // Create some reviews
            List<Review> reviews = new ArrayList<>();
            int numReviews = random.nextInt(5) + 1;
            for (int j = 0; j < numReviews; j++) {
                User user = createDummyUser(j);
                Review review = new Review(
                        j + 1,
                        user,
                        "Great product! Review #" + (j + 1),
                        random.nextInt(5) + 1
                );
                reviews.add(review);
            }

            // Create photo list
            List<String> photos = new ArrayList<>();
            int numPhotos = random.nextInt(3) + 1;
            for (int k = 0; k < numPhotos; k++) {
                photos.add("photo_" + (i + 1) + "_" + (k + 1) + ".jpg");
            }

            // Create Merchandise
            Service service = new Service(
                    i + 1,
                    titles[random.nextInt(titles.length)] + " " + (i + 1),
                    "Detailed description for item " + (i + 1),
                    "Specific details for item " + (i + 1),
                    random.nextDouble() * 1000 + 10,  // price between 10 and 1010
                    random.nextDouble() * 0.5,        // discount between 0 and 0.5 (50%)
                    random.nextBoolean(),             // visible
                    random.nextBoolean(),             // available
                    random.nextInt(5) + 1,            // minDuration
                    random.nextInt(10) + 6,           // maxDuration
                    random.nextInt(24) + 1,           // reservationDeadline
                    random.nextInt(48) + 24,          // cancelReservation
                    random.nextBoolean(),             // automaticReservation
                    false,                            // deleted
                    photos,
                    category,
                    address,
                    reviews
            );

            servicesList.add(service);
        }

        return servicesList;
    }

    public static List<Merchandise> createDummyMerchandise(int count){
        List<Merchandise> merchandiseList=new ArrayList<>();
        merchandiseList.addAll(createDummyProduct(5));
        merchandiseList.addAll(createDummyService(5));
        return  merchandiseList;
    }

    public static User createDummyUser(int id) {
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
