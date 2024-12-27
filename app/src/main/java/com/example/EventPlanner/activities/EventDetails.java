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
import com.example.EventPlanner.databinding.ActivityEventDetailsBinding;
import com.example.EventPlanner.fragments.event.EventViewModel;
import com.example.EventPlanner.model.common.Address;
import com.example.EventPlanner.databinding.ActivityEventFormBinding;
import com.example.EventPlanner.model.event.EventType;
import com.example.EventPlanner.model.event.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EventDetails extends AppCompatActivity {
    private ActivityEventDetailsBinding eventFormBinding;
    private EventViewModel eventViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventFormBinding = ActivityEventDetailsBinding.inflate(getLayoutInflater());
        setContentView(eventFormBinding.getRoot());

        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);

        EdgeToEdge.enable(this);

        Integer eventId = getIntent().getIntExtra("EVENT_ID", -1);
        if (eventId != -1) {
            Event event = eventViewModel.findEventById(eventId);
            Log.d("Naziv proizvoda", event.getTitle());
            if (eventId != null) setFields(event);
        }

        // Handle insets for edge-to-edge design
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Populate fields when editing a product
    private void setFields(Event event) {
        eventFormBinding.eventTitle.setText(event.getTitle());
        eventFormBinding.eventDescription.setText(event.getDescription());
        eventFormBinding.publicity.setText(event.isPublic() ? "Public" : "Private");
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        String formattedDate = sdf.format(event.getDate());
        eventFormBinding.eventDate.setText("Date: " + formattedDate);
        eventFormBinding.eventAddress.setText(
                event.getAddress().getCity() + ", " + event.getAddress().getStreet() + " "
                + event.getAddress().getNumber() + "\nCoordinates: " +
                        event.getAddress().getLatitude().toString() + " " +
                        event.getAddress().getLongitude().toString()

        );
        eventFormBinding.eventType.setText(event.getType().getTitle());
        eventFormBinding.maxParticipants.setText("Max Participants: " + String.valueOf(event.getMaxParticipants()));
        eventFormBinding.organizerName.setText("Organizer:" + "\nDzon do - johndoe@gmail.com");
    }
}