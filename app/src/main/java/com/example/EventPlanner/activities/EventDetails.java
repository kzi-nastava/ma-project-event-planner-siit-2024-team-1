package com.example.EventPlanner.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.JsonWriter;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.EventPlanner.R;
import com.example.EventPlanner.clients.ClientUtils;
import com.example.EventPlanner.clients.JwtService;
import com.example.EventPlanner.databinding.ActivityEventDetailsBinding;
import com.example.EventPlanner.fragments.activity.ActivityCRUD;
import com.example.EventPlanner.fragments.event.EventListViewModel;
import com.example.EventPlanner.model.event.CreatedEventResponse;
import com.example.EventPlanner.model.event.Event;
import com.example.EventPlanner.model.event.EventTypeOverview;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventDetails extends AppCompatActivity {
    private ActivityEventDetailsBinding eventFormBinding;
    private EventListViewModel eventListViewModel;
    private boolean isFavorited = false; // Default state
    private ImageButton starButton;
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

        starButton = findViewById(R.id.star_button);

        // Set initial state based on event details
        updateStarIcon();

        // Handle star button click
        starButton.setOnClickListener(view -> {
            isFavorited = !isFavorited; // Toggle the favorite state
            updateStarIcon();
            saveFavoriteState(); // Save state to backend or local storage
        });

        // Handle insets for edge-to-edge design
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        Button agendaButton = (Button) eventFormBinding.seeAgenda;
        Button followEventButton = (Button) eventFormBinding.followEvent;
        // Set listeners for buttons
        agendaButton.setOnClickListener(v -> {
            // Handle the "Edit" button click
            // You can navigate to an edit screen or show an edit dialog
            Intent intent = new Intent(this, ActivityCRUD.class);
            intent.putExtra("EVENT_ID", eventId);
            startActivity(intent);
        });


        // Set listeners for buttons
        followEventButton.setOnClickListener(v -> {

        });
    }

    private void updateStarIcon() {
        if (isFavorited) {
            starButton.setImageResource(R.drawable.ic_star_filled); // Use filled star
        } else {
            starButton.setImageResource(R.drawable.ic_star_border); // Use empty star
        }
    }

    private void saveFavoriteState() {
        Call<Boolean> call1 = ClientUtils.eventService.favorizeEvent(getIntent().getIntExtra("EVENT_ID", -1), JwtService.getIdFromToken());
        call1.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful() && response.body() != null) {

                } else {
                    // Handle error cases
                    Log.e("Favorizing Event Error", "Response not successful: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable throwable) {
                // Handle network errors
                Log.e("Favorizing Event Failure", "Error: " + throwable.getMessage());
            }
        });
    }

    // Populate fields when editing a product
    private void setFields(CreatedEventResponse event) {
        eventFormBinding.eventTitle.setText(event.getTitle());
        eventFormBinding.eventDescription.setText(event.getDescription());
        eventFormBinding.publicity.setText(event.isPublic() ? "Public" : "Private");

        DateTimeFormatter inputFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        // Parse the input date string
        LocalDateTime date = LocalDateTime.parse(event.getDate(), inputFormatter);

        // Format the date to the desired format
        String formattedDate = date.format(outputFormatter);

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
    }
}