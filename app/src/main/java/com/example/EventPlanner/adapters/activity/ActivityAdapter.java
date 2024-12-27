package com.example.EventPlanner.adapters.activity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.EventPlanner.R;
import com.example.EventPlanner.activities.ActivityForm;
import com.example.EventPlanner.model.event.Activity;
import com.example.EventPlanner.model.event.EventType;
import com.example.EventPlanner.activities.EventTypeForm;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.Locale;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder> {
    private ArrayList<Activity> allActivities;
    private Context context;

    public ActivityAdapter(Context context, ArrayList<Activity> activities) {
        this.context = context;
        this.allActivities = activities;
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

        holder.itemView.findViewById(R.id.edit_activity).setOnClickListener(v -> {
            Intent intent = new Intent(context, ActivityForm.class);
            intent.putExtra("FORM_TYPE", "EDIT_FORM");
            intent.putExtra("ACTIVITY_ID", activity.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return allActivities.size();
    }

    static class ActivityViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView activityCard; // Change this to MaterialCardView
        TextView activityTitle;
        TextView activityDescription;
        TextView activityTime;
        TextView street;
        TextView city;
        TextView number;
        TextView coords;

        public ActivityViewHolder(@NonNull View itemView) {
            super(itemView);
            activityCard = itemView.findViewById(R.id.activity_card); // Cast to MaterialCardView
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
            String start = String.format(Locale.getDefault(), "%02d:%02d", activity.getStartTime().getHour(), activity.getStartTime().getMinute());
            String end = String.format(Locale.getDefault(), "%02d:%02d", activity.getEndTime().getHour(), activity.getEndTime().getMinute());
            activityTime.setText(start + " - " + end);
            street.setText("Street: " + activity.getAddress().getStreet());
            city.setText("City: " + activity.getAddress().getCity());
            number.setText("Number: " + activity.getAddress().getNumber().toString());
            coords.setText("Coordinates: " + activity.getAddress().getLatitude().toString() + " " + activity.getAddress().getLongitude().toString());
        }
    }

}
