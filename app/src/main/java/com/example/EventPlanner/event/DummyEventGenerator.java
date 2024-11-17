package com.example.EventPlanner.event;

import com.example.EventPlanner.address.Address;

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
        List<EventType> eventTypeList = createEventTypes(eventTypes);

        for (int i = 0; i < count; i++) {
            // Create Address
            Address address = new Address(
                    cities[random.nextInt(cities.length)],
                    streets[random.nextInt(streets.length)],
                    String.valueOf(random.nextInt(999) + 1),
                    String.format("%.6f", random.nextDouble() * 180 - 90),  // longitude
                    String.format("%.6f", random.nextDouble() * 180 - 90)   // latitude
            );


            Date eventDate = getRandomFutureDate(180);

            // Create Event
            Event event = new Event(
                    (long) (i + 1),
                    titles[random.nextInt(titles.length)] + " " + (i + 1),
                    descriptions[random.nextInt(descriptions.length)],
                    20 + random.nextInt(181), // maxParticipants between 20 and 200
                    random.nextBoolean(),     // isPublic
                    address,
                    eventDate,
                    eventTypeList.get(random.nextInt(eventTypeList.size()))
            );

            eventsList.add(event);
        }

        return eventsList;
    }

    private static List<EventType> createEventTypes(String[] types) {
        List<EventType> eventTypes = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < types.length; i++) {
            EventType eventType = new EventType(
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
}
