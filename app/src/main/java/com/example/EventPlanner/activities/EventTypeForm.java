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
import com.example.EventPlanner.activities.HomeScreen;
import com.example.EventPlanner.clients.ClientUtils;
import com.example.EventPlanner.databinding.ActivityEventTypeFormBinding;
import com.example.EventPlanner.fragments.eventtype.EventTypeViewModel;
import com.example.EventPlanner.model.event.CreateEventTypeRequest;
import com.example.EventPlanner.model.event.EventTypeOverview;
import com.example.EventPlanner.model.event.UpdateEventTypeRequest;
import com.example.EventPlanner.model.merchandise.CategoryOverview;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventTypeForm extends AppCompatActivity {
    private ActivityEventTypeFormBinding eventTypeFormBinding;
    private EventTypeViewModel eventTypeViewModel;
    private Spinner multiSelectSpinner;

    private List<CategoryOverview> categoryList = new ArrayList<>();
    private boolean[] selectedItems;
    private String[] categoryNames;

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
            eventTypeFormBinding.eventTypeTitle.setEnabled(true);
        } else if ("EDIT_FORM".equals(formType)) {
            formTitle.setText(R.string.edit_event_type);
            eventTypeFormBinding.eventTypeTitle.setEnabled(false);
            int eventTypeId = getIntent().getIntExtra("EVENT_TYPE_ID", -1);
            if (eventTypeId != -1) {
                eventTypeViewModel.getSelectedEventType().observe(this, eventType -> {
                    if (eventType != null) {
                        setFields(eventType);
                    }
                });
                eventTypeViewModel.findEventTypeById(eventTypeId);
            }
        }

        // Multi-select spinner setup
        multiSelectSpinner = findViewById(R.id.multiselect_category_spinner);
        fetchCategoriesAndSetupSpinner();

        eventTypeFormBinding.submitEventTypeButton.setOnClickListener(v -> {
            if (isValidInput()) {
                CreateEventTypeRequest eventType = createEventTypeFromInput();
                if (eventType != null) {
                    if ("NEW_FORM".equals(formType)) {
                        eventTypeViewModel.saveEventType(eventType);
                    } else if ("EDIT_FORM".equals(formType)) {
                        // If it's an edit form, you can update the event type
                        int eventTypeId = getIntent().getIntExtra("EVENT_TYPE_ID", -1);
                        eventTypeViewModel.updateEventType(eventTypeId, new UpdateEventTypeRequest(eventType.getDescription(), eventType.isActive(), eventType.getRecommendedCategoryIds()));
                    }

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
    }

    // Populate fields when editing an event type
    private void setFields(EventTypeOverview eventType) {
        // Set title and description
        eventTypeFormBinding.eventTypeTitle.setText(eventType.getTitle());
        eventTypeFormBinding.eventTypeDescription.setText(eventType.getDescription());

        // Pre-select categories
        if (categoryList != null && categoryNames != null) {
            // Reset selectedItems array
            selectedItems = new boolean[categoryList.size()];

            // Get IDs of categories associated with the event type
            List<Integer> eventCategoryIds = new ArrayList<>();
            for (CategoryOverview cat: eventType.getRecommendedCategories()
                 ) {
                eventCategoryIds.add(cat.getId());
            }

            // Mark matching categories as selected
            for (int i = 0; i < categoryList.size(); i++) {
                if (eventCategoryIds.contains(categoryList.get(i).getId())) {
                    selectedItems[i] = true;
                }
            }

            // Update the spinner to display selected categories
            StringBuilder selectedCategories = new StringBuilder();
            for (int i = 0; i < selectedItems.length; i++) {
                if (selectedItems[i]) {
                    selectedCategories.append(categoryNames[i]).append(", ");
                }
            }
            if (selectedCategories.length() > 0) {
                selectedCategories.setLength(selectedCategories.length() - 2); // Remove trailing comma and space
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{selectedCategories.toString()});
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            multiSelectSpinner.setAdapter(adapter);
        }
    }

    // Fetch categories and populate the multi-select spinner
    private void fetchCategoriesAndSetupSpinner() {
        Call<List<CategoryOverview>> call = ClientUtils.categoryService.getApproved();
        call.enqueue(new Callback<List<CategoryOverview>>() {
            @Override
            public void onResponse(Call<List<CategoryOverview>> call, Response<List<CategoryOverview>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categoryList = response.body();
                    categoryNames = categoryList.stream().map(CategoryOverview::getTitle).toArray(String[]::new);
                    selectedItems = new boolean[categoryList.size()];
                    setupMultiSelectSpinner();
                } else {
                    Log.e("EventTypeForm", "Failed to fetch categories: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<CategoryOverview>> call, Throwable t) {
                Log.e("EventTypeForm", "Error fetching categories", t);
            }
        });
    }

    // Setup the multi-select spinner
    private void setupMultiSelectSpinner() {
        multiSelectSpinner.setOnTouchListener((v, event) -> {
            showMultiSelectDialog();
            return true;
        });

        // Initial adapter to display a placeholder
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{"Select Categories"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        multiSelectSpinner.setAdapter(adapter);
    }

    // Show the multi-select dialog
    private void showMultiSelectDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Recommended Categories");

        builder.setMultiChoiceItems(categoryNames, selectedItems, (dialog, which, isChecked) -> {
            selectedItems[which] = isChecked;
        });

        builder.setPositiveButton("OK", (dialog, id) -> {
            StringBuilder selectedCategories = new StringBuilder();
            for (int i = 0; i < selectedItems.length; i++) {
                if (selectedItems[i]) {
                    selectedCategories.append(categoryNames[i]).append(", ");
                }
            }
            if (selectedCategories.length() > 0) {
                selectedCategories.setLength(selectedCategories.length() - 2); // Remove trailing comma and space
            }

            // Update the spinner with the selected categories
            ArrayAdapter<String> adapter = new ArrayAdapter<>(EventTypeForm.this, android.R.layout.simple_spinner_item, new String[]{selectedCategories.toString()});
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            multiSelectSpinner.setAdapter(adapter);
        });

        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }

    // Validate user input
    private boolean isValidInput() {
        return !TextUtils.isEmpty(eventTypeFormBinding.eventTypeTitle.getText()) &&
                !TextUtils.isEmpty(eventTypeFormBinding.eventTypeDescription.getText());
    }

    // Create Product object from user input
    private CreateEventTypeRequest createEventTypeFromInput() {
        try {
            String title = eventTypeFormBinding.eventTypeTitle.getText().toString();
            String description = eventTypeFormBinding.eventTypeDescription.getText().toString();

            List<Integer> categoryIds = new ArrayList<>();
            for (int i = 0; i < selectedItems.length; i++) {
                if (selectedItems[i]) {
                    categoryIds.add(categoryList.get(i).getId());
                }
            }

            return new CreateEventTypeRequest(title, description, true, categoryIds);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }
}
