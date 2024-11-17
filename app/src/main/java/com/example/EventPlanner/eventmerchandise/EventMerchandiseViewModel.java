package com.example.EventPlanner.eventmerchandise;

import androidx.lifecycle.ViewModel;

import com.example.EventPlanner.event.DummyEventGenerator;
import com.example.EventPlanner.event.Event;
import com.example.EventPlanner.merchandise.DummyMerchandiseGenerator;
import com.example.EventPlanner.merchandise.Merchandise;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class EventMerchandiseViewModel extends ViewModel {
    private List<Merchandise> merchandises;
    private List<Event> events;
    public EventMerchandiseViewModel(){
        merchandises= DummyMerchandiseGenerator.createDummyMerchandise(5);
        events= DummyEventGenerator.createDummyEvents(10);
    }

    public List<EventMerchandise> getAll(){

        List<EventMerchandise> eventMerchandiseList=new ArrayList<>();
        for (Event event: events
        ) {
            eventMerchandiseList.add(new EventMerchandise(EventMerchandise.EVENT,event));
        }
        for (Merchandise merchandise: merchandises
        ) {
            eventMerchandiseList.add(new EventMerchandise(EventMerchandise.MERCHANDISE,merchandise));
        }
        return eventMerchandiseList;
    }

    public List<EventMerchandise> getTop() {
        List<EventMerchandise> topList = new ArrayList<>();

        List<Merchandise> topMerchandise = merchandises.stream()
                .sorted((m1, m2) -> Double.compare(m2.getRating(), m1.getRating()))
                .limit(5)
                .collect(Collectors.toList());

        Date now = new Date();
        List<Event> topEvents = events.stream()
                .filter(e -> e.getDate().after(now))
                .sorted(Comparator.comparing(Event::getDate))
                .limit(5)
                .collect(Collectors.toList());

        for(Event e : topEvents) {
            topList.add(new EventMerchandise(EventMerchandise.EVENT, e));
        }
        for(Merchandise m : topMerchandise) {
            topList.add(new EventMerchandise(EventMerchandise.MERCHANDISE, m));
        }

        return topList;
    }
}
