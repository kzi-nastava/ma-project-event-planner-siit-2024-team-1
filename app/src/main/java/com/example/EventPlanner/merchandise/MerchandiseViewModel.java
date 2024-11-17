package com.example.EventPlanner.merchandise;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class MerchandiseViewModel extends ViewModel {
    private ArrayList<Merchandise> merchandises;
    public MerchandiseViewModel(){
        setMerchandises(new ArrayList<>(DummyMerchandiseGenerator.createDummyMerchandise(5)));
    }

    public ArrayList<Merchandise> getAll() {
        return merchandises;
    }

    public ArrayList<Merchandise> getTop() {
        return merchandises.stream()
                .sorted((m1, m2) -> Double.compare(m2.getRating(), m1.getRating()))
                .limit(5)
                .collect(Collectors.toCollection(ArrayList::new));
    }
    public void setMerchandises(ArrayList<Merchandise> merchandises) {
        this.merchandises = merchandises;
    }
}
