package com.example.EventPlanner.activities;

import android.content.Intent;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.EventPlanner.R;
import com.example.EventPlanner.adapters.event.EventOverviewAdapter;
import com.example.EventPlanner.adapters.event.EventsReservationAdapter;
import com.example.EventPlanner.adapters.merchandise.service.TimeslotsAdapter;
import com.example.EventPlanner.clients.ClientUtils;
import com.example.EventPlanner.clients.JwtService;
import com.example.EventPlanner.databinding.ActivityBookReservationBinding;
import com.example.EventPlanner.fragments.event.EventListViewModel;
import com.example.EventPlanner.model.common.ErrorResponseDto;
import com.example.EventPlanner.model.event.EventOverview;
import com.example.EventPlanner.model.merchandise.service.ReservationRequest;
import com.example.EventPlanner.model.merchandise.service.ReservationResponse;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

public class BookReservationActivity extends AppCompatActivity {

    private TextInputEditText startTimeInput;
    private TextInputEditText endTimeInput;
    private Calendar selectedStartCalendar;
    private Calendar selectedEndCalendar;
    private BookReservationViewModel bookReservationViewModel;
    private ActivityBookReservationBinding activityBookReservationBinding;
    private EventListViewModel eventListViewModel;
    private int merchandiseId;
    private EventOverview selectedEvent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        activityBookReservationBinding=ActivityBookReservationBinding.inflate(getLayoutInflater());
        setContentView(activityBookReservationBinding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initializeViews();
        setupDateTimeInputs();
        Intent intent=getIntent();
        merchandiseId=intent.getIntExtra("MERCHANDISE_ID",-1);
        bookReservationViewModel=new ViewModelProvider(BookReservationActivity.this).get(BookReservationViewModel.class);
        eventListViewModel=new ViewModelProvider(BookReservationActivity.this).get(EventListViewModel.class);

        Button reserveBtn=activityBookReservationBinding.reserveButton;
        reserveBtn.setOnClickListener(v->submitReservation());
        RecyclerView timeslotsRecyclerView=activityBookReservationBinding.timeslotsRecyclerView;
        RecyclerView eventsRecyclerView=activityBookReservationBinding.eventsRecyclerView;
        timeslotsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        bookReservationViewModel.getTimeslots().observe(BookReservationActivity.this, timeslots->{
            TimeslotsAdapter timeslotsAdapter=new TimeslotsAdapter(timeslots);
            timeslotsRecyclerView.setAdapter(timeslotsAdapter);
            timeslotsAdapter.notifyDataSetChanged();
        });
        bookReservationViewModel.getTimeslots(merchandiseId);

        eventListViewModel.getEvents().observe(BookReservationActivity.this, events->{
            EventsReservationAdapter eventsReservationAdapter=new EventsReservationAdapter(events, event -> {
                // Handle event selection here
                selectedEvent = event;
            });
            eventsRecyclerView.setAdapter(eventsReservationAdapter);
            eventsReservationAdapter.notifyDataSetChanged();
        });
        eventListViewModel.getByEo();
    }

    private void initializeViews() {
        startTimeInput = findViewById(R.id.startTimeInput);
        endTimeInput = findViewById(R.id.endTimeInput);
    }

    private void setupDateTimeInputs() {
        startTimeInput.setOnClickListener(v -> showDateTimePicker(true));
        endTimeInput.setOnClickListener(v -> showDateTimePicker(false));
    }

    private void showDateTimePicker(boolean isStartTime) {
        // Create date picker
        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(isStartTime ? "Select start date" : "Select end date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();

        // Handle date selection
        datePicker.addOnPositiveButtonClickListener(selection -> {
            // Initialize calendar with selected date
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            calendar.setTimeInMillis(selection);

            // Create time picker
            MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_24H)
                    .setHour(12)
                    .setMinute(0)
                    .setTitleText(isStartTime ? "Select start time" : "Select end time")
                    .build();

            // Handle time selection
            timePicker.addOnPositiveButtonClickListener(view -> {
                calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                calendar.set(Calendar.MINUTE, timePicker.getMinute());

                // Store the selected date/time
                if (isStartTime) {
                    selectedStartCalendar = calendar;
                    startTimeInput.setText(formatDateTime(Date.from(calendar.getTime().toInstant().minus(Duration.ofHours(1)))));
                } else {
                    selectedEndCalendar = calendar;
                    endTimeInput.setText(formatDateTime(Date.from(calendar.getTime().toInstant().minus(Duration.ofHours(1)))));
                }

                validateDateTimeSelection();
            });

            // Show time picker after date is selected
            timePicker.show(getSupportFragmentManager(), "time_picker");
        });

        // Show date picker
        datePicker.show(getSupportFragmentManager(), "date_picker");
    }

    private String formatDateTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
        return sdf.format(date);
    }

    private void validateDateTimeSelection() {
        if (selectedStartCalendar != null && selectedEndCalendar != null) {
            if (selectedEndCalendar.before(selectedStartCalendar)) {
                Toast.makeText(this, "End time cannot be before start time", Toast.LENGTH_SHORT).show();
                selectedEndCalendar = null;
                endTimeInput.setText("");
            }
        }
    }

    private void submitReservation() {
        if (selectedEvent == null || selectedStartCalendar == null) {
            Toast.makeText(this, "Please select an event and start time", Toast.LENGTH_SHORT).show();
            return;
        }

        ReservationRequest request = new ReservationRequest();
        request.setEventId(selectedEvent.getId());
        request.setOrganizerId(JwtService.getIdFromToken());

        // Format dates as strings in ISO-8601 format
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        isoFormat.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));

        request.setStartTime(isoFormat.format(selectedStartCalendar.getTime()));

        if (selectedEndCalendar != null) {
            request.setEndTime(isoFormat.format(selectedEndCalendar.getTime()));
        }

        ClientUtils.serviceService.reserveService(merchandiseId, request).enqueue(new Callback<ReservationResponse>() {
            @Override
            public void onResponse(Call<ReservationResponse> call, Response<ReservationResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(BookReservationActivity.this,
                            "Reservation confirmed with " + response.body().getProviderEmail(),
                            Toast.LENGTH_SHORT).show();
                    bookReservationViewModel.getTimeslots(merchandiseId);
                } else {
                    try {
                        Converter<ResponseBody, ErrorResponseDto> converter = ClientUtils.retrofit.responseBodyConverter(
                                ErrorResponseDto.class, new Annotation[0]);
                        ErrorResponseDto errorResponse = converter.convert(response.errorBody());

                        if (errorResponse != null) {
                            Toast.makeText(BookReservationActivity.this, errorResponse.getMessage(), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(BookReservationActivity.this, "Unknown error occurred", Toast.LENGTH_LONG).show();
                        }
                    } catch (IOException e) {
                        Toast.makeText(BookReservationActivity.this, "Error parsing server response", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ReservationResponse> call, Throwable t) {
                Toast.makeText(BookReservationActivity.this,
                        "Network error",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}