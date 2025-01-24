package com.example.EventPlanner.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.EventPlanner.R;
import com.example.EventPlanner.clients.ClientUtils;
import com.example.EventPlanner.databinding.ActivityShowRecommendedMerchandiseBinding;
import com.example.EventPlanner.fragments.event.EventsList;
import com.example.EventPlanner.fragments.merchandise.MerchandiseList;
import com.example.EventPlanner.model.merchandise.MerchandiseOverview;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowRecommendedMerchandise extends AppCompatActivity {
    private ActivityShowRecommendedMerchandiseBinding merchandiseBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        merchandiseBinding = ActivityShowRecommendedMerchandiseBinding.inflate(getLayoutInflater());
        setContentView(merchandiseBinding.getRoot());

        Intent intent = getIntent();
        int categoryId = intent.getIntExtra("CATEGORY_ID", -1);
        double maxPrice = intent.getDoubleExtra("MAX_PRICE", 0);
        int eventId = intent.getIntExtra("EVENT_ID", -1);
        Bundle bundle = new Bundle();
        bundle.putString("type", "category");
        bundle.putInt("categoryId", categoryId);
        bundle.putDouble("maxPrice", maxPrice);
        bundle.putInt("eventId", eventId);
        EventsList eventsList=new EventsList();
        eventsList.setArguments(bundle);
        MerchandiseList merchandiseList=new MerchandiseList();
        merchandiseList.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerViewMerchandise, merchandiseList)
                .commit();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}