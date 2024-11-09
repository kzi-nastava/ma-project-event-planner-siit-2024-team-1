package com.example.zadatak2.merchandise;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class MerchandiseViewModel extends ViewModel {
    public ArrayList<Merchandise> merchandises;
    public MerchandiseViewModel(){
        merchandises=new ArrayList<>(DummyMerchandiseGenerator.createDummyMerchandise(5));
    }
}
