package com.example.EventPlanner.model.event;

import com.example.EventPlanner.model.merchandise.Merchandise;

public class EventMerchandise {

    public static final int EVENT=0;
    public static final int MERCHANDISE=1;
    private Merchandise merchandise;
    private Event event;
    private int viewType;

    public Merchandise getMerchandise() {
        return merchandise;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public void setMerchandise(Merchandise merchandise) {
        this.merchandise = merchandise;
    }

    public EventMerchandise(int viewType, Merchandise merchandise) {
        this.merchandise = merchandise;
        this.viewType=viewType;
    }

    public EventMerchandise(int viewType,Event event) {
        this.event=event;
        this.viewType=viewType;

    }
}
