package com.example.EventPlanner.fragments.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.EventPlanner.R;
import com.example.EventPlanner.activities.ActivityForm;
import com.example.EventPlanner.databinding.FragmentActivityCrudBinding;
import com.example.EventPlanner.fragments.activity.ActivityList;

public class ActivityCRUD extends AppCompatActivity {

    private FragmentActivityCrudBinding activityCrudBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityCrudBinding = FragmentActivityCrudBinding.inflate(getLayoutInflater());
        setContentView(activityCrudBinding.getRoot());

        int eventId = getIntent().getIntExtra("EVENT_ID", -1);
        // Initialize Activity List Fragment
        ActivityList activityList = new ActivityList(eventId);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerViewActivity, activityList).commit();

        // Add new activity button listener
        Button addActivityButton = activityCrudBinding.addActivityButton;
        if (addActivityButton != null) {
            addActivityButton.setOnClickListener((v) -> {
                Intent intent = new Intent(ActivityCRUD.this, ActivityForm.class);
                intent.putExtra("FORM_TYPE", "NEW_FORM");
                intent.putExtra("EVENT_ID", eventId);
                startActivity(intent);
            });
        } else {
            Log.e("ActivityCRUD", "addActivityButton is null");
        }

        Button generatePdfButton = activityCrudBinding.agendaPdf;
        if (generatePdfButton != null) {
            generatePdfButton.setOnClickListener((v) -> {
                ActivityList activityListFragment = (ActivityList) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerViewActivity);
                if (activityListFragment != null) {
                    activityListFragment.generatePdf(eventId); // Pass event title dynamically
                }
            });
        }
    }
}