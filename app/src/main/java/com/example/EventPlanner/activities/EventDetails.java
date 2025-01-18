package com.example.EventPlanner.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.JsonWriter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.EventPlanner.R;
import com.example.EventPlanner.clients.ClientUtils;
import com.example.EventPlanner.clients.JwtService;
import com.example.EventPlanner.databinding.ActivityEventDetailsBinding;
import com.example.EventPlanner.fragments.activity.ActivityCRUD;
import com.example.EventPlanner.fragments.common.map.MapFragment;
import com.example.EventPlanner.fragments.common.map.MapViewModel;
import com.example.EventPlanner.fragments.event.EventListViewModel;
import com.example.EventPlanner.model.common.Address;
import com.example.EventPlanner.model.common.Review;
import com.example.EventPlanner.model.event.CreatedEventResponse;
import com.example.EventPlanner.model.event.Event;
import com.example.EventPlanner.model.event.EventOverview;
import com.example.EventPlanner.model.event.EventReport;
import com.example.EventPlanner.model.event.EventTypeOverview;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.EventPlanner.model.event.FollowResponse;
import com.example.EventPlanner.model.event.ReviewDTO;
import com.example.EventPlanner.model.event.UserOverview;
import com.example.EventPlanner.services.WebSocketService;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import java.io.File;

public class EventDetails extends AppCompatActivity {
    private ActivityEventDetailsBinding eventFormBinding;
    private EventListViewModel eventListViewModel;
    private boolean isFavorited = false; // Default state
    private ImageButton starButton;
    private MapViewModel mapViewModel;

    private List<UserOverview> participants;
    private List<ReviewDTO> reviews;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventFormBinding = ActivityEventDetailsBinding.inflate(getLayoutInflater());
        setContentView(eventFormBinding.getRoot());

        eventListViewModel = new ViewModelProvider(this).get(EventListViewModel.class);

        EdgeToEdge.enable(this);
        mapViewModel=new ViewModelProvider(EventDetails.this).get(MapViewModel.class);

        MapFragment mapFragment = MapFragment.newInstance(true, false,false);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.eventFormMapViewFragmentView,mapFragment)
                .commit();

        Integer eventId = getIntent().getIntExtra("EVENT_ID", -1);
        if (eventId != -1) {
            eventListViewModel.getSelectedEvent().observe(this, event -> {
                if (event != null) {
                    setFields(event);
                    mapViewModel.setEvents(event);
                    eventFormBinding.floatingActionButton.setOnClickListener(v->{
                        Intent intent = new Intent(this, MessengerActivity.class);
                        intent.putExtra("USER_ID", event.getOrganizer().getId());
                        startActivity(intent);
                    });
                }

            });
            eventListViewModel.findEventById(eventId);
        }

        starButton = findViewById(R.id.star_button);

        Call<List<EventOverview>> call1 = ClientUtils.eventService.getFavorites(JwtService.getIdFromToken());
        call1.enqueue(new Callback<List<EventOverview>>() {
            @Override
            public void onResponse(Call<List<EventOverview>> call, Response<List<EventOverview>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if(response.body().stream().filter(x -> x.getId() == eventId).findAny().isPresent()){
                        isFavorited = true;
                    }
                    else{
                        isFavorited = false;
                    }
                    updateStarIcon();
                } else {
                    // Handle error cases
                    Log.e("Favorizing Event Error", "Response not successful: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<EventOverview>> call, Throwable throwable) {
                // Handle network errors
                Log.e("Favorizing Event Failure", "Error: " + throwable.getMessage());
            }
        });

        if(JwtService.getIdFromToken() == -1){
            starButton.setVisibility(View.GONE);
        }

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

        if(JwtService.getIdFromToken() == -1){
            followEventButton.setVisibility(View.GONE);
        }

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
            ClientUtils.userService.followEvent(JwtService.getIdFromToken(), eventId).enqueue(new Callback<FollowResponse>() {
                @Override
                public void onResponse(Call<FollowResponse> call, Response<FollowResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Intent intent = new Intent(getBaseContext(), HomeScreen.class);
                        startActivity(intent);
                        // Generate PDF with the event report
                    } else {
                        Log.e("PDF Generation", "Failed to fetch report: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<FollowResponse> call, Throwable t) {
                    Log.e("PDF Generation", "Error fetching report: " + t.getMessage());
                }
            });
        });

        Button statsButton = (Button) eventFormBinding.stats;
        Button pdfButton = (Button) eventFormBinding.generatePdfButton;

        if(!JwtService.getRoleFromToken().equals("A")){
            statsButton.setVisibility(View.GONE);
            pdfButton.setVisibility(View.GONE);
        }

        // Set listeners for buttons
        statsButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, EventStatsActivity.class);
            intent.putExtra("EVENT_ID", eventId);
            startActivity(intent);
        });

        pdfButton.setOnClickListener(v -> {
            if (eventId != -1) {
                // Fetch event report from the backend using the event ID
                ClientUtils.eventService.getEventReport(eventId).enqueue(new Callback<EventReport>() {
                    @Override
                    public void onResponse(Call<EventReport> call, Response<EventReport> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            EventReport report = response.body();
                            generatePdf(report); // Generate PDF with the event report
                        } else {
                            Log.e("PDF Generation", "Failed to fetch report: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<EventReport> call, Throwable t) {
                        Log.e("PDF Generation", "Error fetching report: " + t.getMessage());
                    }
                });
            }
        });
        int notificationId = getIntent().getIntExtra("NOTIFICATION_ID", -1);
        if (notificationId != -1) {
            // Mark notification as read
            Intent markReadIntent = new Intent(this, WebSocketService.class);
            markReadIntent.setAction(WebSocketService.ACTION_MARK_READ);
            markReadIntent.putExtra("NOTIFICATION_ID", notificationId);
            startService(markReadIntent);
        }





    }


    private void generatePdf(EventReport report) {
        // Get the Downloads directory
        String downloadsDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();

        // Set the path for the generated PDF
        String filePath = downloadsDirectory + "/EventReport.pdf";

        try {
            // Initialize PdfWriter and PdfDocument
            PdfWriter writer = new PdfWriter(filePath);
            PdfDocument pdfDocument = new PdfDocument(writer);
            Document document = new Document(pdfDocument);

            // Add Event Info
            document.add(new Paragraph("Event Title: " + eventFormBinding.eventTitle.getText().toString()));
            document.add(new Paragraph("Event Description: " + eventFormBinding.eventDescription.getText().toString()));
            document.add(new Paragraph(eventFormBinding.eventDate.getText().toString()));
            document.add(new Paragraph("Address: " + eventFormBinding.eventAddress.getText().toString()));

            // Add Participants
            document.add(new Paragraph("\nParticipants:"));
            for (UserOverview participant : report.getParticipants()) {
                document.add(new Paragraph(participant.getFirstName() + " " + participant.getLastName() +
                        " - " + participant.getEmail()));
            }

            // Add Reviews
            document.add(new Paragraph("\nReviews:"));
            for (ReviewDTO review : report.getReviews()) {
                document.add(new Paragraph("Rating: " + review.getRating() + " - " + review.getComment()));
            }

            // Add Grade Counts (Optional, based on the grades of the reviews)
            int[] gradeCounts = report.getGradeCounts();
            document.add(new Paragraph("\nGrade Distribution:"));
            for (int i = 0; i < gradeCounts.length; i++) {
                document.add(new Paragraph("Rating " + (i + 1) + ": " + gradeCounts[i] + " reviews"));
            }

            // Close the document
            document.close();
            Log.i("PDF Generation", "PDF created at: " + filePath);

            // Open the generated PDF file
            openPdf(filePath);
        } catch (Exception e) {
            Log.e("PDF Generation", "Error creating PDF: " + e.getMessage());
        }
    }

    private void openPdf(String filePath) {
        // Create a file object for the generated PDF
        File pdfFile = new File(filePath);

        // Create a Uri object from the file
        Uri pdfUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // For Android Nougat and above, use FileProvider to share the file
            pdfUri = FileProvider.getUriForFile(this, "com.example.EventPlanner.fileprovider", pdfFile);
        } else {
            pdfUri = Uri.fromFile(pdfFile);
        }

        // Create an intent to open the PDF file
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(pdfUri, "application/pdf");
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY); // To avoid saving history
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // Grant read permission for the URI

        // Start the intent to open the PDF
        startActivity(intent);
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