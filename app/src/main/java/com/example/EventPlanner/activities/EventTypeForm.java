package com.example.EventPlanner.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;
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

import com.example.EventPlanner.R;
import com.example.EventPlanner.databinding.ActivityEventTypeFormBinding;
import com.example.EventPlanner.fragments.eventtype.EventTypeViewModel;
import com.example.EventPlanner.model.event.EventType;
import com.example.EventPlanner.model.merchandise.Category2;

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

                    Intent intent = new Intent(EventTypeForm.this, HomeScreen.class);
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


        multiSelectSpinner = findViewById(R.id.multiselect_category_spinner);

        // Set the adapter to the Spinner (empty or initial item)
        ArrayAdapter<String> categoriesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{"Select Recommended Categories"});
        categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        multiSelectSpinner.setAdapter(categoriesAdapter);

        // Set an item click listener for the Spinner
        multiSelectSpinner.setOnTouchListener((v, event) -> {
            showMultiSelectDialog();
            return true;
        });
    }

    // Populate fields when editing a product
    private void setFields(EventType eventType) {
        eventTypeFormBinding.eventTypeTitle.setText(eventType.getTitle());
        eventTypeFormBinding.eventTypeDescription.setText(eventType.getDescription());
    }
    private Spinner multiSelectSpinner;
    private String[] categories = {"Suicidability", "Funerality", "Somethingality"};
    private boolean[] selectedItems = new boolean[categories.length];
    private void showMultiSelectDialog() {
        // Create the dialog for multi-select
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Recommended Categories");

        // Create checkboxes dynamically for each event type
        builder.setMultiChoiceItems(categories, selectedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                selectedItems[which] = isChecked;
            }
        });

        // Positive button (OK) to capture selected items
        builder.setPositiveButton("OK", (dialog, id) -> {
            StringBuilder selectedCategories = new StringBuilder();
            for (int i = 0; i < selectedItems.length; i++) {
                if (selectedItems[i]) {
                    selectedCategories.append(categories[i]).append(", ");
                }
            }
            if (selectedCategories.length() > 0) {
                selectedCategories.setLength(selectedCategories.length() - 2); // Remove trailing comma and space
            }

            // Update the Spinner with the selected event types
            ArrayAdapter<String> adapter = new ArrayAdapter<>(EventTypeForm.this, android.R.layout.simple_spinner_item, new String[]{selectedCategories.toString()});
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            multiSelectSpinner.setAdapter(adapter);
            Toast.makeText(EventTypeForm.this, "Selected: " + selectedCategories.toString(), Toast.LENGTH_SHORT).show();
        });

        // Negative button (Cancel)
        builder.setNegativeButton("Cancel", null);

        // Show the dialog
        builder.create().show();
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

            List<Category2> categories = new ArrayList<Category2>();
            categories.add(new Category2(1, "Funerality", "Opis", false));
            categories.add(new Category2(1, "Suicidability", "Opis", false));

            return new EventType(1, title, description, true, categories);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }
}
