package com.example.EventPlanner.eventType;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.EventPlanner.HomeScreen;
import com.example.EventPlanner.R;
import com.example.EventPlanner.address.Address;
import com.example.EventPlanner.databinding.ActivityEventTypeFormBinding;
import com.example.EventPlanner.databinding.ActivityProductFormBinding;
import com.example.EventPlanner.eventType.EventType;
import com.example.EventPlanner.merchandise.MerchandisePhoto;
import com.example.EventPlanner.product.Category;
import com.example.EventPlanner.product.Product;
import com.example.EventPlanner.product.ProductViewModel;

import java.util.ArrayList;
import java.util.List;

public class EventTypeForm extends AppCompatActivity {
    private ActivityEventTypeFormBinding eventTypeFormBinding;
    private EventTypeViewModel eventTypeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventTypeFormBinding = ActivityEventTypeFormBinding.inflate(getLayoutInflater());
        setContentView(eventTypeFormBinding.getRoot());

        eventTypeViewModel = new ViewModelProvider(this).get(EventTypeViewModel.class);

        EdgeToEdge.enable(this);

        String formType = getIntent().getStringExtra("FORM_TYPE");
        TextView formTitle = eventTypeFormBinding.formTitle;

        // Setup form title and visibility based on form type
        if ("NEW_FORM".equals(formType)) {
            formTitle.setText(R.string.add_event_type);
        } else if ("EDIT_FORM".equals(formType)) {
            formTitle.setText(R.string.edit_event_type);
            int eventTypeId = getIntent().getIntExtra("EVENT_TYPE_ID", -1);
            if (eventTypeId != -1) {
                EventType eventType = eventTypeViewModel.findEventTypeById(eventTypeId);
                Log.d("Naziv proizvoda", eventType.getTitle());
                if (eventType != null) setFields(eventType);
            }
        }

        // Category Spinner Setup
        String[] categoryOptions = {"Options", "Space", "Food", "Drinks", "Music", "Decorations", "Other"};
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categoryOptions);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        // Submit Button Logic
        eventTypeFormBinding.submitEventTypeButton.setOnClickListener(v -> {
            if (isValidInput()) {
                EventType eventType = createEventTypeFromInput();
                if (eventType != null) {
                    eventTypeViewModel.saveEventType(eventType);

                    Intent intent = new Intent(com.example.EventPlanner.eventType.EventTypeForm.this, HomeScreen.class);
                    startActivity(intent);
                }
            }
        });

        // Handle insets for edge-to-edge design
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }

    // Populate fields when editing a product
    private void setFields(EventType eventType) {
        eventTypeFormBinding.eventTypeTitle.setText(eventType.getTitle());
        eventTypeFormBinding.eventTypeDescription.setText(eventType.getDescription());
    }

    // Validate user input
    private boolean isValidInput() {
        return !TextUtils.isEmpty(eventTypeFormBinding.eventTypeTitle.getText()) &&
                !TextUtils.isEmpty(eventTypeFormBinding.eventTypeDescription.getText());
    }

    // Create Product object from user input
    private EventType createEventTypeFromInput() {
        try {
            String title = eventTypeFormBinding.eventTypeTitle.getText().toString();
            String description = eventTypeFormBinding.eventTypeDescription.getText().toString();

            List<Category> categories = new ArrayList<Category>();
            categories.add(new Category(1, "Funerality", "Opis", false));
            categories.add(new Category(1, "Suicidability", "Opis", false));

            return new EventType(1, title, description, true, categories);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }
}
