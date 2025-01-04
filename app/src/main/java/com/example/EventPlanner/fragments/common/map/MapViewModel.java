package com.example.EventPlanner.fragments.common.map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.EventPlanner.activities.EventDetails;
import com.example.EventPlanner.model.event.CreatedEventResponse;
import com.example.EventPlanner.model.event.EventOverview;
import com.example.EventPlanner.model.merchandise.MerchandiseDetail;
import com.example.EventPlanner.model.merchandise.MerchandiseOverview;

import java.util.ArrayList;
import java.util.List;

public class MapViewModel extends ViewModel {
    private final MutableLiveData<List<EventOverview>> eventOverviewMutableLiveData=new MutableLiveData<>();
    private final MutableLiveData<List<MerchandiseOverview>> merchandiseOverviewMutableLiveData=new MutableLiveData<>();

    public LiveData<List<EventOverview>> getEvents(){
        return eventOverviewMutableLiveData;
    }

    public MutableLiveData<List<MerchandiseOverview>> getMerchandise() {
        return merchandiseOverviewMutableLiveData;
    }

    public void setEvents(List<EventOverview> events){
        eventOverviewMutableLiveData.postValue(events);
    }

    public void setMerchandise(List<MerchandiseOverview> merchandise){
        merchandiseOverviewMutableLiveData.postValue(merchandise);
    }

    public void setEvents(CreatedEventResponse event){
        EventOverview eventOverview=new EventOverview(event.getId(),event.getEventType().getTitle(),event.getTitle(),event.getDate(),event.getAddress(),event.getDescription(),event.isPublic());
        List<EventOverview> events=new ArrayList<>();
        events.add(eventOverview);
        eventOverviewMutableLiveData.postValue(events);
    }

    public void setMerchandise(MerchandiseDetail merchandiseDetail){
        MerchandiseOverview merchandiseOverview=new MerchandiseOverview(merchandiseDetail.getId(),merchandiseDetail.getCategory().getTitle(),merchandiseDetail.getType(),merchandiseDetail.getMerchandisePhotos(),merchandiseDetail.getTitle(),merchandiseDetail.getRating(),merchandiseDetail.getAddress(),merchandiseDetail.getPrice(),merchandiseDetail.getDescription());
        List<MerchandiseOverview> merchandiseOverviews=new ArrayList<>();
        merchandiseOverviews.add(merchandiseOverview);
        merchandiseOverviewMutableLiveData.postValue(merchandiseOverviews);

    }
}
