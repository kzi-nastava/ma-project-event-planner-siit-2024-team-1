package com.example.EventPlanner;

import com.example.EventPlanner.model.common.Address;
import com.example.EventPlanner.model.merchandise.Category2;
import com.example.EventPlanner.model.event.Event;
import com.example.EventPlanner.model.event.EventType;
import com.example.EventPlanner.model.event.EventType1;
import com.example.EventPlanner.model.merchandise.MerchandisePhoto;
import com.example.EventPlanner.model.merchandise.product.Product;
import com.example.EventPlanner.model.user.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class DummyEventGenerator {
    public static List<Event> createDummyEvents(int count) {
        List<Event> eventsList = new ArrayList<>();
        Random random = new Random();

        // Sample data arrays
        String[] cities = {"New York", "Los Angeles", "Chicago", "Houston", "Phoenix"};
        String[] streets = {"Main St", "Broadway", "Park Ave", "Oak Lane", "Maple Dr"};
        String[] eventTypes = {"Conference", "Workshop", "Seminar", "Networking", "Training"};
        String[] titles = {"Tech Summit", "Business Workshop", "Leadership Seminar", "Industry Meetup", "Professional Training"};
        String[] descriptions = {
                "Join us for an exciting event focused on the latest industry trends",
                "Learn from industry experts in this hands-on session",
                "Connect with professionals and expand your network",
                "Discover new opportunities and insights in your field",
                "Enhance your skills with practical workshops"
        };

        // Create event types first
        List<EventType1> eventTypeList = createEventTypes(eventTypes);

        for (int i = 0; i < count; i++) {
            // Create Address
            Address address = new Address(
                    cities[random.nextInt(cities.length)],
                    streets[random.nextInt(streets.length)],
                    String.valueOf(random.nextInt(999) + 1),
                    22.1,  // longitude
                    22.1   // latitude
            );


            Date eventDate = getRandomFutureDate(180);

            // Create Event
            Event event = new Event(
                    (i + 1),
                    titles[random.nextInt(titles.length)] + " " + (i + 1),
                    descriptions[random.nextInt(descriptions.length)],
                    20 + random.nextInt(181), // maxParticipants between 20 and 200
                    random.nextBoolean(),     // isPublic
                    address,
                    eventDate,
                    new EventType(1, "Tech konferencija", "Desc", true, null)
            );

            eventsList.add(event);
        }

        return eventsList;
    }

    private static List<EventType1> createEventTypes(String[] types) {
        List<EventType1> eventTypes = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < types.length; i++) {
            EventType1 eventType = new EventType1(
                    (long) (i + 1),
                    types[i],
                    "Description for " + types[i] + " events",
                    random.nextBoolean()
            );
            eventTypes.add(eventType);
        }

        return eventTypes;
    }

    // Utility method to get a random future date within a range
    private static Date getRandomFutureDate(int maxDaysAhead) {
        Random random = new Random();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, random.nextInt(maxDaysAhead));
        return calendar.getTime();
    }

    public static class DummyProductGenerator {

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
            Category2 category = new Category2(1, "Funerality", "Opis", false);

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
}
