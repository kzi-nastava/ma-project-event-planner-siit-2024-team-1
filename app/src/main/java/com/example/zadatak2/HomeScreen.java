package com.example.zadatak2;

import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zadatak2.databinding.ActivityHomeScreenBinding;
import com.example.zadatak2.event.Event;
import com.example.zadatak2.event.EventViewModel;
import com.example.zadatak2.event.EventsList;
import com.example.zadatak2.merchandise.Merchandise;
import com.example.zadatak2.merchandise.MerchandiseList;
import com.example.zadatak2.merchandise.MerchandiseViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeScreen extends AppCompatActivity {
    private MerchandiseViewModel merchandiseViewModel;
    private EventViewModel eventViewModel;
    ActivityHomeScreenBinding activityHomeScreenBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        activityHomeScreenBinding=ActivityHomeScreenBinding.inflate(getLayoutInflater());
        setContentView(activityHomeScreenBinding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        List<EventMerchandise> eventMerchandiseList=new ArrayList<>();
        merchandiseViewModel=new ViewModelProvider(this).get(MerchandiseViewModel.class);
        eventViewModel=new ViewModelProvider(this).get(EventViewModel.class);
        for (Merchandise merchandise:merchandiseViewModel.merchandises
             ) {
            eventMerchandiseList.add(new EventMerchandise(EventMerchandise.MERCHANDISE,merchandise));
        }
        for (Event event:eventViewModel.events
        ) {
            eventMerchandiseList.add(new EventMerchandise(EventMerchandise.EVENT,event));
        }
        EventsMerchandiseAdapter adapter= new EventsMerchandiseAdapter(eventMerchandiseList);
        RecyclerView recyclerView = findViewById(R.id.events_merchandise_recycler_view);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}