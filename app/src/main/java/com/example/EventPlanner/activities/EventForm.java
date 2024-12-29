package com.example.EventPlanner.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.EventPlanner.R;
import com.example.EventPlanner.fragments.event.EventListViewModel;
import com.example.EventPlanner.fragments.eventtype.EventTypeViewModel;
import com.example.EventPlanner.model.common.Address;
import com.example.EventPlanner.databinding.ActivityEventFormBinding;
import com.example.EventPlanner.model.event.CreatedEventResponse;
import com.example.EventPlanner.model.event.EventOverview;
import com.example.EventPlanner.model.event.EventType;
import com.example.EventPlanner.model.event.Event;
import com.example.EventPlanner.model.event.EventTypeOverview;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EventForm extends AppCompatActivity {
    private ActivityEventFormBinding eventFormBinding;
    private EventListViewModel eventListViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventFormBinding = ActivityEventFormBinding.inflate(getLayoutInflater());
        setContentView(eventFormBinding.getRoot());

        eventListViewModel = new ViewModelProvider(this).get(EventListViewModel.class);

        EdgeToEdge.enable(this);

        String formType = getIntent().getStringExtra("FORM_TYPE");
        TextView formTitle = eventFormBinding.formTitle;


        findViewById(R.id.event_date).setOnClickListener(view -> showDatePickerDialog());

        // Setup form title and visibility based on form type
        if ("NEW_FORM".equals(formType)) {
            formTitle.setText(R.string.add_event);
        } else if ("EDIT_FORM".equals(formType)) {
            formTitle.setText(R.string.edit_event);
            Integer eventId = getIntent().getIntExtra("EVENT_ID", -1);
            if (eventId != -1) {
                formTitle.setText(R.string.edit_event);
                eventListViewModel.getSelectedEvent().observe(this, event -> {
                    if (event != null) {
                        setFields(event);
                    }
                });
                eventListViewModel.findEventById(eventId);
            }
        }



        // Submit Button Logic
        eventFormBinding.submitEventButton.setOnClickListener(v -> {
            if (isValidInput()) {
                Event event = createEventFromInput();
                if (event != null) {
                    eventListViewModel.saveEvent(event);

                    Intent intent = new Intent(EventForm.this, HomeScreen.class);
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

        eventListViewModel = new ViewModelProvider(this).get(EventListViewModel.class);
        EventTypeViewModel eventTypeViewModel = new ViewModelProvider(this).get(EventTypeViewModel.class);

        EdgeToEdge.enable(this);

        // Spinner for event types
        Spinner eventTypesSpinner = eventFormBinding.eventTypesSpinner;

        eventTypeViewModel.getEventTypes().observe(this, eventTypes -> {
            // Create an ArrayAdapter with EventTypeOverview objects
            ArrayAdapter<EventTypeOverview> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item, eventTypes);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            eventFormBinding.eventTypesSpinner.setAdapter(adapter);
        });

        // Fetch event types
        eventTypeViewModel.getAll();

        // Handle the spinner item selection (optional)
        eventTypesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedEventType = parentView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle the case when no item is selected
            }
        });
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Update the EditText with the selected date
                    String date = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
                    eventFormBinding.eventDate.setText(date);
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    // Populate fields when editing a product
    private void setFields(CreatedEventResponse event) {
        eventFormBinding.eventTitle.setText(event.getTitle());
        eventFormBinding.eventDescription.setText(event.getDescription());
        eventFormBinding.maxParticipants.setText(String.valueOf(event.getMaxParticipants()));
        eventFormBinding.eventDate.setText(String.valueOf(event.getDate()));
        eventFormBinding.city.setText(event.getAddress().getCity());
        eventFormBinding.street.setText(event.getAddress().getStreet());
        eventFormBinding.number.setText(event.getAddress().getNumber());
        eventFormBinding.latitude.setText(String.valueOf(event.getAddress().getLatitude()));
        eventFormBinding.longitude.setText(String.valueOf(event.getAddress().getLongitude()));
        eventFormBinding.radioPublic.setChecked(event.isPublic());

        // Pre-select the event type by ID
        Integer eventTypeId = event.getEventType().getId();
        Spinner eventTypesSpinner = eventFormBinding.eventTypesSpinner;

        // Assuming you already have an adapter with a list of EventTypeOverview objects
        ArrayAdapter<EventTypeOverview> adapter = (ArrayAdapter<EventTypeOverview>) eventTypesSpinner.getAdapter();
        if (adapter != null) {
            for (int i = 0; i < adapter.getCount(); i++) {
                EventTypeOverview eventType = adapter.getItem(i);
                if (eventType != null && eventType.getId().equals(eventTypeId)) {
                    eventTypesSpinner.setSelection(i);
                    break;
                }
            }
        }
    }

    // Validate user input
    private boolean isValidInput() {
        return !TextUtils.isEmpty(eventFormBinding.eventTitle.getText()) &&
                !TextUtils.isEmpty(eventFormBinding.eventDescription.getText()) &&
                !TextUtils.isEmpty(eventFormBinding.maxParticipants.getText()) &&
                !TextUtils.isEmpty(eventFormBinding.eventDate.getText()) &&
                !TextUtils.isEmpty(eventFormBinding.city.getText()) &&
                !TextUtils.isEmpty(eventFormBinding.street.getText())&&
                !TextUtils.isEmpty(eventFormBinding.number.getText())&&
                !TextUtils.isEmpty(eventFormBinding.latitude.getText())&&
                !TextUtils.isEmpty(eventFormBinding.longitude.getText());
    }

    // Create Product object from user input
    private Event createEventFromInput() {
        try {
            String title = eventFormBinding.eventTitle.getText().toString();
            String description = eventFormBinding.eventDescription.getText().toString();
            Integer maxParticipants = Integer.parseInt(eventFormBinding.maxParticipants.getText().toString());
            String legacyDateStr = eventFormBinding.eventDate.getText().toString();;
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date date = sdf.parse(legacyDateStr);
            String city = eventFormBinding.city.getText().toString();
            String street = eventFormBinding.street.getText().toString();
            String number = eventFormBinding.number.getText().toString();
            double latitude = Double.parseDouble(eventFormBinding.latitude.getText().toString());
            double longitude = Double.parseDouble(eventFormBinding.longitude.getText().toString());

            EventType eventType = new EventType(1, "Funerality", "Opis", true, null);

            Address address = new Address(city, street, number, latitude, longitude);

            return new Event(1, title, description, maxParticipants, true, address, date, eventType);
            } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
