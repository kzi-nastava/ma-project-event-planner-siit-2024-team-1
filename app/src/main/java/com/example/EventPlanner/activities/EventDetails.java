package com.example.EventPlanner.activities;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.EventPlanner.R;
import com.example.EventPlanner.databinding.ActivityEventDetailsBinding;
import com.example.EventPlanner.fragments.event.EventListViewModel;
import com.example.EventPlanner.model.event.CreatedEventResponse;
import com.example.EventPlanner.model.event.Event;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class EventDetails extends AppCompatActivity {
    private ActivityEventDetailsBinding eventFormBinding;
    private EventListViewModel eventListViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventFormBinding = ActivityEventDetailsBinding.inflate(getLayoutInflater());
        setContentView(eventFormBinding.getRoot());

        eventListViewModel = new ViewModelProvider(this).get(EventListViewModel.class);

        EdgeToEdge.enable(this);

        Integer eventId = getIntent().getIntExtra("EVENT_ID", -1);
        if (eventId != -1) {
            eventListViewModel.getSelectedEvent().observe(this, event -> {
                if (event != null) {
                    setFields(event);
                }
            });
            eventListViewModel.findEventById(eventId);
        }

        // Handle insets for edge-to-edge design
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Populate fields when editing a product
    private void setFields(CreatedEventResponse event) {
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
        eventFormBinding.eventType.setText(event.getEventType().getTitle());
        eventFormBinding.maxParticipants.setText("Max Participants: " + String.valueOf(event.getMaxParticipants()));
        eventFormBinding.organizerName.setText("Organizer:" + "\n" + event.getOrganizer().getName() + " " +
                event.getOrganizer().getSurname() + " - " + event.getOrganizer().getEmail());
        eventFormBinding.organizerName.setText("Organizer:" + "\nDzon do - johndoe@gmail.com");
    }
}