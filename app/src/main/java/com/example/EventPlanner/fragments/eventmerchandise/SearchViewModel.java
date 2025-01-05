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

    // Visibility toggles
    private final MutableLiveData<Boolean> showEvents = new MutableLiveData<>(true);
    private final MutableLiveData<Boolean> showServices = new MutableLiveData<>(true);
    private final MutableLiveData<Boolean> showProducts = new MutableLiveData<>(true);

    // Service filter properties
    private final MutableLiveData<Double> servicePriceMin = new MutableLiveData<>();
    private final MutableLiveData<Double> servicePriceMax = new MutableLiveData<>();
    private final MutableLiveData<String> serviceCategory = new MutableLiveData<>();
    private final MutableLiveData<Integer> serviceDurationMin = new MutableLiveData<>();
    private final MutableLiveData<Integer> serviceDurationMax = new MutableLiveData<>();
    private final MutableLiveData<String> serviceCity = new MutableLiveData<>();

    // Product filter properties
    private final MutableLiveData<Double> productPriceMin = new MutableLiveData<>();
    private final MutableLiveData<Double> productPriceMax = new MutableLiveData<>();
    private final MutableLiveData<String> productCategory = new MutableLiveData<>();
    private final MutableLiveData<Integer> productDurationMin = new MutableLiveData<>();
    private final MutableLiveData<Integer> productDurationMax = new MutableLiveData<>();
    private final MutableLiveData<String> productCity = new MutableLiveData<>();
    private final MutableLiveData<String> eventSortBy=new MutableLiveData<>();
    private final MutableLiveData<String> merchandiseSortBy=new MutableLiveData<>("title");
    private final MutableLiveData<Boolean> merchandiseSortByAscending = new MutableLiveData<>(true);



    public void resetSearch(){
        setSearchText(null);
        // Reset ViewModel values
        setShowEvents(true);
        setShowServices(true);
        setShowProducts(true);
        setType(null);
        setCity(null);
        setStartDate(null);
        setEndDate(null);

        // Reset service filters
        setServicePriceMin(null);
        setServicePriceMax(null);
        setServiceCategory(null);
        setServiceDurationMin(null);
        setServiceDurationMax(null);
        setServiceCity(null);

        // Reset product filters
        setProductPriceMin(null);
        setProductPriceMax(null);
        setProductCategory(null);
        setProductDurationMin(null);
        setProductDurationMax(null);
        setProductCity(null);

    }
    // Visibility toggle getters and setters
    public LiveData<Boolean> getShowEvents() {
        return showEvents;
    }

    public void setShowEvents(Boolean show) {
        showEvents.setValue(show);
    }

    public LiveData<Boolean> getShowServices() {
        return showServices;
    }

    public void setShowServices(Boolean show) {
        showServices.setValue(show);
    }

    public LiveData<Boolean> getShowProducts() {
        return showProducts;
    }

    public void setShowProducts(Boolean show) {
        showProducts.setValue(show);
    }

    // Existing getters and setters
    public LiveData<String> getSearchText() {
        return searchText;
    }

    public void setSearchText(String newSearchText) {
        searchText.setValue(newSearchText);
    }

    public LiveData<LocalDate> getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate newStartDate) {
        startDate.setValue(newStartDate);
    }

    public LiveData<LocalDate> getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate newEndDate) {
        endDate.setValue(newEndDate);
    }

    public LiveData<String> getType() {
        return type;
    }

    public void setType(String newType) {
        type.setValue(newType);
    }

    public LiveData<String> getCity() {
        return city;
    }

    public void setCity(String newCity) {
        city.setValue(newCity);
    }

    // Service filter getters and setters
    public LiveData<Double> getServicePriceMin() {
        return servicePriceMin;
    }

    public void setServicePriceMin(Double price) {
        servicePriceMin.setValue(price);
    }

    public LiveData<Double> getServicePriceMax() {
        return servicePriceMax;
    }

    public void setServicePriceMax(Double price) {
        servicePriceMax.setValue(price);
    }

    public LiveData<String> getServiceCategory() {
        return serviceCategory;
    }

    public void setServiceCategory(String category) {
        serviceCategory.setValue(category);
    }

    public LiveData<Integer> getServiceDurationMin() {
        return serviceDurationMin;
    }

    public void setServiceDurationMin(Integer duration) {
        serviceDurationMin.setValue(duration);
    }

    public LiveData<Integer> getServiceDurationMax() {
        return serviceDurationMax;
    }

    public void setServiceDurationMax(Integer duration) {
        serviceDurationMax.setValue(duration);
    }

    public LiveData<String> getServiceCity() {
        return serviceCity;
    }

    public void setServiceCity(String city) {
        serviceCity.setValue(city);
    }

    // Product filter getters and setters
    public LiveData<Double> getProductPriceMin() {
        return productPriceMin;
    }

    public void setProductPriceMin(Double price) {
        productPriceMin.setValue(price);
    }

    public LiveData<Double> getProductPriceMax() {
        return productPriceMax;
    }

    public void setProductPriceMax(Double price) {
        productPriceMax.setValue(price);
    }

    public LiveData<String> getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String category) {
        productCategory.setValue(category);
    }

    public LiveData<Integer> getProductDurationMin() {
        return productDurationMin;
    }

    public void setProductDurationMin(Integer duration) {
        productDurationMin.setValue(duration);
    }

    public LiveData<Integer> getProductDurationMax() {
        return productDurationMax;
    }

    public void setProductDurationMax(Integer duration) {
        productDurationMax.setValue(duration);
    }

    public LiveData<String> getProductCity() {
        return productCity;
    }

    public void setProductCity(String city) {
        productCity.setValue(city);
    }
    public LiveData<String> getEventSortBy() {
        return eventSortBy;
    }

    public void setEventSortBy(String newEventSortBy) {
        eventSortBy.setValue(newEventSortBy);
    }

    public LiveData<String> getMerchandiseSortBy() {
        return merchandiseSortBy;
    }

    public void setMerchandiseSortBy(String newMerchandiseSortBy) {
        merchandiseSortBy.setValue(newMerchandiseSortBy);
    }

    public LiveData<Boolean> getMerchandiseSortByAscending() {
        return merchandiseSortByAscending;
    }

    public void setMerchandiseSortByAscending(Boolean ascending) {
        merchandiseSortByAscending.setValue(ascending);
    }
}