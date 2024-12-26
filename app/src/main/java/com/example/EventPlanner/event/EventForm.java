package com.example.EventPlanner.event;

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
import com.example.EventPlanner.databinding.ActivityEventFormBinding;
import com.example.EventPlanner.databinding.ActivityProductFormBinding;
import com.example.EventPlanner.eventType.EventType;
import com.example.EventPlanner.merchandise.MerchandisePhoto;
import com.example.EventPlanner.product.Category;
import com.example.EventPlanner.product.Product;
import com.example.EventPlanner.product.ProductViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EventForm extends AppCompatActivity {
    private ActivityEventFormBinding eventFormBinding;
    private EventViewModel eventViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventFormBinding = ActivityEventFormBinding.inflate(getLayoutInflater());
        setContentView(eventFormBinding.getRoot());

        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);

        EdgeToEdge.enable(this);

        String formType = getIntent().getStringExtra("FORM_TYPE");
        TextView formTitle = eventFormBinding.formTitle;

        // Setup form title and visibility based on form type
        if ("NEW_FORM".equals(formType)) {
            formTitle.setText(R.string.add_event);
        } else if ("EDIT_FORM".equals(formType)) {
            formTitle.setText(R.string.edit_event);
            Integer eventId = getIntent().getIntExtra("EVENT_ID", -1);
            if (eventId != -1) {
                Event event = eventViewModel.findEventById(eventId);
                Log.d("Naziv proizvoda", event.getTitle());
                if (eventId != null) setFields(event);
            }
        }



        // Submit Button Logic
        eventFormBinding.submitEventButton.setOnClickListener(v -> {
            if (isValidInput()) {
                Event event = createEventFromInput();
                if (event != null) {
                    eventViewModel.saveEvent(event);

                    Intent intent = new Intent(com.example.EventPlanner.event.EventForm.this, HomeScreen.class);
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

        // Get reference to the Spinner
        Spinner eventTypesSpinner = findViewById(R.id.event_types_spinner);

// Create an ArrayAdapter for the Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.event_type_array, android.R.layout.simple_spinner_item);

// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

// Set the adapter to the Spinner
        eventTypesSpinner.setAdapter(adapter);

// Handle the item selection (optional)
        eventTypesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedEventType = parentView.getItemAtPosition(position).toString();
                Toast.makeText(com.example.EventPlanner.event.EventForm.this, "Selected Category: " + selectedEventType, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle the case when no item is selected (optional)
            }
        });
    }

    // Populate fields when editing a product
    private void setFields(Event event) {
        eventFormBinding.eventTitle.setText(event.getTitle());
        eventFormBinding.eventDescription.setText(event.getDescription());
        eventFormBinding.maxParticipants.setText(event.getMaxParticipants());
        eventFormBinding.eventDate.setText(String.valueOf(event.getDate()));
        eventFormBinding.city.setText(event.getAddress().getCity());
        eventFormBinding.street.setText(event.getAddress().getStreet());
        eventFormBinding.number.setText(event.getAddress().getNumber());
        eventFormBinding.latitude.setText(String.valueOf(event.getAddress().getLatitude()));
        eventFormBinding.longitude.setText(String.valueOf(event.getAddress().getLongitude()));
        eventFormBinding.radioPublic.setChecked(event.isPublic());
        eventFormBinding.radioPublic.setChecked(!event.isPublic());
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
