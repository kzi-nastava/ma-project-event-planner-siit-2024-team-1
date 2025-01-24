package com.example.EventPlanner.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.EventPlanner.R;
import com.example.EventPlanner.clients.ClientUtils;
import com.example.EventPlanner.clients.JwtService;
import com.example.EventPlanner.databinding.ActivityBuyProductBinding;
import com.example.EventPlanner.fragments.event.EventListViewModel;
import com.example.EventPlanner.fragments.merchandise.product.ProductViewModel;
import com.example.EventPlanner.model.common.PageResponse;
import com.example.EventPlanner.model.event.EventOverview;

import org.slf4j.spi.LocationAwareLogger;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuyProductActivity extends AppCompatActivity {
    private ActivityBuyProductBinding buyProductBinding;
    private EventListViewModel eventListViewModel;
    private int merchandiseId;
    private int eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        buyProductBinding = ActivityBuyProductBinding.inflate(getLayoutInflater());
        eventListViewModel = new ViewModelProvider(this).get(EventListViewModel.class);
        setContentView(buyProductBinding.getRoot());

        Intent intent = getIntent();
        merchandiseId = intent.getIntExtra("MERCHANDISE_ID", -1);

        Spinner eventSpinner = buyProductBinding.eventSpinner;
        List<EventOverview> eventOptions = new ArrayList<>();
        eventListViewModel.getEvents().observe(this, events -> {
            if(events != null && !events.isEmpty()) {
                eventOptions.clear();
                eventOptions.addAll(events);
                ArrayAdapter<EventOverview> adapter = new ArrayAdapter<>(
                            BuyProductActivity.this, android.R.layout.simple_spinner_item, eventOptions
                );
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                eventSpinner.setAdapter(adapter);
            }
        });
        eventListViewModel.getByEo();

        this.eventId = intent.getIntExtra("EVENT_ID", -1);
        if(eventId != -1 && !eventOptions.isEmpty()) {
            int selectedIndex = findEventIndexById(eventOptions, eventId);
            if(selectedIndex != -1) {
                eventSpinner.setSelection(selectedIndex);
            }
        }

        eventSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                eventId = eventOptions.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        buyProductBinding.buyProductBtn.setOnClickListener(v -> buyProduct(merchandiseId, eventId));
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private int findEventIndexById(List<EventOverview> eventOptions, int eventId) {
        for(int i = 0; i < eventOptions.size(); i++) {
            if(eventOptions.get(i).getId() == eventId) {
                return i;
            }
        }
        return -1;
    }
    private void buyProduct(int productId, int eventId) {
        Call<ResponseBody> call = ClientUtils.productService.buyProduct(productId, eventId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    //TODO: Maybe change later to load budget of event related to item purchase
                    Intent intent = new Intent(BuyProductActivity.this, BudgetActivity.class);
                    intent.putExtra("EVENT_ID", eventId);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(BuyProductActivity.this, ProductDetailsActivity.class);
                    intent.putExtra("MERCHANDISE_ID", productId);
                    startActivity(intent);
                    Log.e("BuyProduct", "Failed to buy product: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.e("BuyProduct", "Error buying product: " + throwable.getMessage());
            }
        });
    }
}