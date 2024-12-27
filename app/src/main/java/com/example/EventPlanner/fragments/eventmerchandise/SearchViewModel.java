package com.example.EventPlanner.fragments.eventmerchandise;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.time.LocalDate;

public class SearchViewModel extends ViewModel {
    private final MutableLiveData<String> searchText = new MutableLiveData<>();
    private final MutableLiveData<LocalDate> startDate = new MutableLiveData<>();
    private final MutableLiveData<LocalDate> endDate = new MutableLiveData<>();
    private final MutableLiveData<String> type = new MutableLiveData<>();
    private final MutableLiveData<String> city = new MutableLiveData<>();

    // Getter and Setter for searchText
    public LiveData<String> getSearchText() {
        return searchText;
    }

    public void setSearchText(String newSearchText) {
        searchText.postValue(newSearchText);
    }

    // Getter and Setter for startDate
    public LiveData<LocalDate> getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate newStartDate) {
        startDate.postValue(newStartDate);
    }

    // Getter and Setter for endDate
    public LiveData<LocalDate> getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate newEndDate) {
        endDate.postValue(newEndDate);
    }

    // Getter and Setter for type
    public LiveData<String> getType() {
        return type;
    }

    public void setType(String newType) {
        type.postValue(newType);
    }

    // Getter and Setter for city
    public LiveData<String> getCity() {
        return city;
    }

    public void setCity(String newCity) {
        city.postValue(newCity);
    }
}
