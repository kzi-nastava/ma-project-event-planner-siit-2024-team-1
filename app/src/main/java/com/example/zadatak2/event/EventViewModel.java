package com.example.zadatak2.event;

import androidx.lifecycle.ViewModel;

import com.example.zadatak2.merchandise.DummyMerchandiseGenerator;

import java.util.ArrayList;

public class EventViewModel extends ViewModel {
    public ArrayList<Event> events;
    public EventViewModel(){
        events=new ArrayList<>(DummyEventGenerator.createDummyEvents(5));
    }
}
