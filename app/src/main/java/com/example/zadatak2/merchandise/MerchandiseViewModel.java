package com.example.zadatak2.merchandise;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class MerchandiseViewModel extends ViewModel {
    private ArrayList<Merchandise> merchandises;
    public MerchandiseViewModel(){
        setMerchandises(new ArrayList<>(DummyMerchandiseGenerator.createDummyMerchandise(5)));
    }

    public ArrayList<Merchandise> getMerchandises() {
        return merchandises;
    }

    public void setMerchandises(ArrayList<Merchandise> merchandises) {
        this.merchandises = merchandises;
    }
}
