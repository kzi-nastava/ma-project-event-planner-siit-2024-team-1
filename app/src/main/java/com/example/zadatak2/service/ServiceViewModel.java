package com.example.zadatak2.service;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.stream.Collectors;

import com.example.zadatak2.merchandise.DummyMerchandiseGenerator;
public class ServiceViewModel extends ViewModel {
    public ArrayList<Service> services;

    public ServiceViewModel() {
        setServices(new ArrayList<Service>(DummyMerchandiseGenerator.createDummyService(5)));
    }

    public ArrayList<Service> getAll() {
        return services;
    }

    public Service findServiceById(int id) {
        for(Service service : services) {
            if(service.getId() == id) {
                return service;
            }
        }
        return null;
    }

    public void setServices(ArrayList<Service> services) {
        this.services = services;
    }
}
