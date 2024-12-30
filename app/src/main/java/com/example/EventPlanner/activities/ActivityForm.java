package com.example.EventPlanner.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
import com.example.EventPlanner.activities.HomeScreen;
import com.example.EventPlanner.fragments.activity.ActivityViewModel;
import com.example.EventPlanner.model.common.Address;
import com.example.EventPlanner.databinding.ActivityActivityFormBinding;
import com.example.EventPlanner.model.event.Activity;
import com.example.EventPlanner.model.event.CreateActivityRequest;
import com.example.EventPlanner.model.event.CreateEventTypeRequest;
import com.example.EventPlanner.model.event.UpdateEventTypeRequest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ActivityForm extends AppCompatActivity {
    private ActivityActivityFormBinding activityFormBinding;
    private ActivityViewModel activityViewModel;

    private EditText startTime, endTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityFormBinding = ActivityActivityFormBinding.inflate(getLayoutInflater());
        setContentView(activityFormBinding.getRoot());

        activityViewModel = new ViewModelProvider(this).get(ActivityViewModel.class);

        EdgeToEdge.enable(this);

        String formType = getIntent().getStringExtra("FORM_TYPE");
        TextView formTitle = activityFormBinding.formTitle;


        // Setup form title and visibility based on form type
        if ("NEW_FORM".equals(formType)) {
            formTitle.setText(R.string.add_activity);
        } else if ("EDIT_FORM".equals(formType)) {
            formTitle.setText(R.string.edit_activity);
            int activityId = getIntent().getIntExtra("ACTIVITY_ID", -1);
            if (activityId != -1) {
                activityViewModel.getSelectedActivity().observe(this, eventType -> {
                    if (eventType != null) {
                        setFields(eventType);
                    }
                });
                activityViewModel.findActivityById(activityId);
            }
        }

        startTime = findViewById(R.id.activity_start);
        endTime = findViewById(R.id.activity_end);

        startTime.setOnClickListener(view -> showTimePickerDialog(startTime));
        endTime.setOnClickListener(view -> showTimePickerDialog(endTime));

        // Submit Button Logic
        activityFormBinding.submitActivityButton.setOnClickListener(v -> {
            if (isValidInput()) {
                CreateActivityRequest activity = createActivityFromInput();
                if (activity != null) {
                    if ("NEW_FORM".equals(formType)) {
                        int eventId = getIntent().getIntExtra("EVENT_ID", -1);
                        activityViewModel.saveActivity(eventId, activity);
                    } else if ("EDIT_FORM".equals(formType)) {
                        // If it's an edit form, you can update the event type
                        int activityId = getIntent().getIntExtra("ACTIVITY_ID", -1);
                        activityViewModel.updateActivity(activityId, new CreateActivityRequest(activity.getTitle(), activity.getDescription(), activity.getStartTime().toString(), activity.getEndTime().toString(), activity.getAddress()));
                    }

                    Intent intent = new Intent(ActivityForm.this, HomeScreen.class);
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

    private void showTimePickerDialog(EditText targetEditText) {
        int hour = 12;  // Default hour
        int minute = 0; // Default minute

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, selectedHour, selectedMinute) -> {
                    // Format the time and set it to the EditText
                    String time = String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute);
                    targetEditText.setText(time);
                },
                hour, minute, true // Use true for 24-hour format
        );
        timePickerDialog.show();
    }

    // Populate fields when editing a product
    private void setFields(com.example.EventPlanner.model.event.Activity activity) {
        activityFormBinding.activityTitle.setText(activity.getTitle());
        activityFormBinding.activityDescription.setText(activity.getDescription());
        activityFormBinding.city.setText(activity.getAddress().getCity());
        activityFormBinding.street.setText(activity.getAddress().getStreet());
        activityFormBinding.number.setText(activity.getAddress().getNumber());
        activityFormBinding.latitude.setText(String.valueOf(activity.getAddress().getLatitude()));
        activityFormBinding.longitude.setText(String.valueOf(activity.getAddress().getLongitude()));
        String start = activity.getStartTime().substring(0, activity.getStartTime().length() - 3);
        String end = activity.getEndTime().substring(0, activity.getEndTime().length() - 3);
        activityFormBinding.activityStart.setText(start);
        activityFormBinding.activityEnd.setText(end);
    }

    // Validate user input
    private boolean isValidInput() {
        return !TextUtils.isEmpty(activityFormBinding.activityTitle.getText()) &&
                !TextUtils.isEmpty(activityFormBinding.activityDescription.getText()) &&
                !TextUtils.isEmpty(activityFormBinding.city.getText()) &&
                !TextUtils.isEmpty(activityFormBinding.street.getText())&&
                !TextUtils.isEmpty(activityFormBinding.number.getText())&&
                !TextUtils.isEmpty(activityFormBinding.latitude.getText())&&
                !TextUtils.isEmpty(activityFormBinding.longitude.getText()) &&
                !TextUtils.isEmpty(activityFormBinding.activityStart.getText()) &&
                !TextUtils.isEmpty(activityFormBinding.activityEnd.getText());
    }

    // Create Product object from user input
    private CreateActivityRequest createActivityFromInput() {
        try {
            String title = activityFormBinding.activityTitle.getText().toString();
            String description = activityFormBinding.activityDescription.getText().toString();
            String city = activityFormBinding.city.getText().toString();
            String street = activityFormBinding.street.getText().toString();
            String number = activityFormBinding.number.getText().toString();
            double latitude = Double.parseDouble(activityFormBinding.latitude.getText().toString());
            double longitude = Double.parseDouble(activityFormBinding.longitude.getText().toString());


            String start = activityFormBinding.activityStart.getText().toString();
            String end = activityFormBinding.activityEnd.getText().toString();

            Address address = new Address(city, street, number, latitude, longitude);

            return new CreateActivityRequest(title, description, start, end, address);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }
}
