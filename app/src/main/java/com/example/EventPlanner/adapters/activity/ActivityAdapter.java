package com.example.EventPlanner.adapters.activity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.EventPlanner.R;
import com.example.EventPlanner.activities.ActivityForm;
import com.example.EventPlanner.activities.HomeScreen;
import com.example.EventPlanner.clients.ClientUtils;
import com.example.EventPlanner.model.event.Activity;
import com.example.EventPlanner.model.event.EventType;
import com.example.EventPlanner.activities.EventTypeForm;
import com.example.EventPlanner.model.event.EventTypeOverview;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder> {
    private ArrayList<Activity> allActivities;
    private Context context;
    private int eventId; // Add a member variable to hold the event ID

    public ActivityAdapter(Context context, ArrayList<Activity> activities, int eventId) {
        this.context = context;
        this.allActivities = activities;
        this.eventId = eventId;  // Set the event ID when the adapter is created
    }

    @NonNull
    @Override
    public ActivityAdapter.ActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_activity_card, parent, false);
        return new ActivityAdapter.ActivityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityAdapter.ActivityViewHolder holder, int position) {
        Activity activity = allActivities.get(position);
        holder.bind(activity);

        // Use the eventId that was passed to the adapter
        holder.itemView.findViewById(R.id.edit_activity).setOnClickListener(v -> {
            Intent intent = new Intent(context, ActivityForm.class);
            intent.putExtra("FORM_TYPE", "EDIT_FORM");
            intent.putExtra("EVENT_ID", eventId);  // Pass the eventId to the new Activity
            intent.putExtra("ACTIVITY_ID", activity.getId());
            context.startActivity(intent);
        });
        holder.itemView.findViewById(R.id.delete_activity).setOnClickListener(v -> {
            Call<List<Activity>> call1 = ClientUtils.eventService.deleteAgenda(eventId, activity.getId());
            call1.enqueue(new Callback<List<Activity>>() {
                @Override
                public void onResponse(Call<List<Activity>> call, Response<List<Activity>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Intent intent = new Intent(context, HomeScreen.class);
                        context.startActivity(intent);
                    } else {
                        // Handle error cases
                        Log.e("Login Error", "Response not successful: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<List<Activity>> call, Throwable throwable) {
                    // Handle network errors
                    Log.e("Deleting Activity Failure", "Error: " + throwable.getMessage());
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return allActivities.size();
    }

    static class ActivityViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView activityCard;
        TextView activityTitle;
        TextView activityDescription;
        TextView activityTime;
        TextView street;
        TextView city;
        TextView number;
        TextView coords;

        public ActivityViewHolder(@NonNull View itemView) {
            super(itemView);
            activityCard = itemView.findViewById(R.id.activity_card);
            activityTitle = itemView.findViewById(R.id.activity_title);
            activityDescription = itemView.findViewById(R.id.activity_description);
            activityTime = itemView.findViewById(R.id.activity_time_range);
            street = itemView.findViewById(R.id.address_street);
            city = itemView.findViewById(R.id.address_city);
            number = itemView.findViewById(R.id.address_number);
            coords = itemView.findViewById(R.id.address_coordinates);
        }

        public void bind(Activity activity) {
            activityTitle.setText(activity.getTitle());
            activityDescription.setText(activity.getDescription());
            String start = activity.getStartTime();
            String end = activity.getEndTime();
            activityTime.setText(start + " - " + end);
            street.setText("Street: " + activity.getAddress().getStreet());
            city.setText("City: " + activity.getAddress().getCity());
            number.setText("Number: " + activity.getAddress().getNumber().toString());
            coords.setText("Coordinates: " + activity.getAddress().getLatitude().toString() + " " + activity.getAddress().getLongitude().toString());
        }
    }
}

