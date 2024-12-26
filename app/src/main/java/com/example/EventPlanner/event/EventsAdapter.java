package com.example.EventPlanner.event;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.EventPlanner.R;
import com.example.EventPlanner.eventType.EventTypeForm;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventViewHolder> {
    private ArrayList<Event> events;
    private Context context;

    public EventsAdapter(Context context, ArrayList<Event> events) {
        this.context = context;
        this.events = events;
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
        Event event = events.get(position);

        holder.eventTitle.setText(event.getTitle());
        holder.eventDescription.setText(event.getDescription());
        holder.eventLocation.setText(String.format("%s, %s %s",
                event.getAddress().getCity(),
                event.getAddress().getStreet(),
                event.getAddress().getNumber()));
        holder.eventType.setText(String.format(Locale.getDefault(), "%s", event.getType().getTitle()));
        if (event.getDate() != null) {
            // Format the date as per your requirement (e.g., "MM/dd/yyyy")
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
            String formattedDate = sdf.format(event.getDate());
            holder.eventDate.setText(formattedDate);
        }
        holder.itemView.findViewById(R.id.see_agenda).setOnClickListener(v -> {
            //
        });
        holder.itemView.findViewById(R.id.see_details_button).setOnClickListener(v -> {
            //
        });
        holder.itemView.findViewById(R.id.edit_event).setOnClickListener(v -> {
            Intent intent = new Intent(context, EventTypeForm.class);
            intent.putExtra("FORM_TYPE", "EDIT_FORM");
            intent.putExtra("EVENT_TYPE_ID", event.getId());
            context.startActivity(intent);
        });
        holder.itemView.findViewById(R.id.delete_event).setOnClickListener(v -> {

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