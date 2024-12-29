package com.example.EventPlanner.adapters.event;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.EventPlanner.R;
import com.example.EventPlanner.activities.EventDetails;
import com.example.EventPlanner.activities.EventForm;
import com.example.EventPlanner.activities.EventTypeForm;
import com.example.EventPlanner.activities.SendInvitationScreen;
import com.example.EventPlanner.fragments.activity.ActivityCRUD;
import com.example.EventPlanner.model.event.Event;
import com.example.EventPlanner.model.event.EventOverview;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

public class EventOverviewAdapter extends RecyclerView.Adapter<EventOverviewAdapter.EventViewHolder> {
    private ArrayList<EventOverview> events;
    private Context context;
    private Activity activity;
    private FragmentManager fragmentManager;


    public EventOverviewAdapter(Context context, ArrayList<EventOverview> events) {
        this.context = context;
        this.events = events;
    }

    public EventOverviewAdapter(Activity context, FragmentManager aFragmentManager, ArrayList<EventOverview> aEvents){
        super();
        events = aEvents;
        activity = context;
        fragmentManager = aFragmentManager;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_event_card, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        EventOverview event = events.get(position);

        holder.eventTitle.setText(event.getTitle());
        holder.eventDescription.setText(event.getDescription());
        String formattedAddress=String.format("%s, %s %s",
                event.getAddress().getCity(),
                event.getAddress().getStreet(),
                event.getAddress().getNumber());
        holder.eventLocation.setText(formattedAddress);
        Log.d("adresa", formattedAddress);
        holder.eventType.setText(String.format(Locale.getDefault(), "%s", event.getType()));
        if (event.getDate() != null) {
            // Format the date as per your requirement (e.g., "MM/dd/yyyy")
            String inputDate = event.getDate();

            // Parse the input string to LocalDateTime
            LocalDateTime dateTime = LocalDateTime.parse(inputDate);

            // Define the desired output format
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            // Format the date to the desired string
            String formattedDate = dateTime.format(formatter);
            holder.eventDate.setText(formattedDate);
        }
        holder.itemView.findViewById(R.id.see_agenda).setOnClickListener(v -> {
            Intent intent = new Intent(context, ActivityCRUD.class);
            intent.putExtra("EVENT_ID", event.getId());
            context.startActivity(intent);
        });
        holder.itemView.findViewById(R.id.see_details_button).setOnClickListener(v -> {
            Intent intent = new Intent(context, EventDetails.class);
            intent.putExtra("EVENT_ID", event.getId());
            context.startActivity(intent);
        });
        holder.itemView.findViewById(R.id.edit_event).setOnClickListener(v -> {
            Intent intent = new Intent(context, EventForm.class);
            intent.putExtra("FORM_TYPE", "EDIT_FORM");
            intent.putExtra("EVENT_ID", event.getId());
            context.startActivity(intent);
        });
        holder.itemView.findViewById(R.id.delete_event).setOnClickListener(v -> {

        });
        holder.itemView.findViewById(R.id.btn_invite).setOnClickListener(v -> {
            Intent intent = new Intent(context, SendInvitationScreen.class);
            intent.putExtra("EVENT_ID", event.getId());
            context.startActivity(intent);
        });

        holder.eventCard.setOnClickListener(v -> {
            Toast.makeText(context, event.getTitle(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {
        LinearLayout eventCard;
        TextView eventTitle;
        TextView eventDescription;
        TextView eventLocation;
        TextView eventType;

        TextView eventDate;
        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            eventCard = itemView.findViewById(R.id.event_card);
            eventTitle = itemView.findViewById(R.id.event_title);
            eventDescription = itemView.findViewById(R.id.event_description);
            eventLocation = itemView.findViewById(R.id.event_location);
            eventType = itemView.findViewById(R.id.event_type);
            eventDate=itemView.findViewById(R.id.event_date);
        }
    }
}
