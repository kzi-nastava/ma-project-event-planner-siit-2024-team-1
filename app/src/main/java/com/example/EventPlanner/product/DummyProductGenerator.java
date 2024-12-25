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

        String[] cities = {"New York", "Los Angeles", "Chicago", "Houston", "Phoenix"};
        String[] streets = {"Main St", "Broadway", "Park Ave", "Oak Lane", "Maple Dr"};
        String[] categories = {"Electronics", "Furniture", "Sports", "Books", "Fashion"};
        String[] titles = {"Premium Item", "Best Seller", "New Arrival", "Featured Product", "Limited Edition"};

        for (int i = 0; i < count; i++) {
            // Create Address
            Address address = new Address(
                    cities[random.nextInt(cities.length)],
                    streets[random.nextInt(streets.length)],
                    String.valueOf(random.nextInt(999) + 1),
                    22.0,  // longitude
                    23.0   // latitude
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
            List<MerchandisePhoto> photos = new ArrayList<>();
            int numPhotos = random.nextInt(3) + 1;
            for (int k = 0; k < numPhotos; k++) {
                photos.add(new MerchandisePhoto(k + 1, "photo_" + (i + 1) + "_" + (k + 1) + ".jpg"));
            }

            // Create Product
            Product product = new Product(
                    i + 1,
                    categories[random.nextInt(categories.length)],
                    "Product Type " + (i + 1),
                    photos.isEmpty() ? null : photos.get(0),  // Assuming the photos list has at least one item
                    titles[random.nextInt(titles.length)] + " " + (i + 1),
                    random.nextDouble() * 5,  // Random rating between 0 and 5
                    address,
                    random.nextDouble() * 1000 + 10,  // Price between 10 and 1010
                    "Detailed description for item " + (i + 1)
            );

            productsList.add(product);
        }

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
