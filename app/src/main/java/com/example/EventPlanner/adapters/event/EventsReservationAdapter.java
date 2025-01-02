package com.example.EventPlanner.adapters.event;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.EventPlanner.R;
import com.example.EventPlanner.model.event.EventOverview;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class EventsReservationAdapter extends RecyclerView.Adapter<EventsReservationAdapter.ViewHolder> {
    private List<EventOverview> events;
    private EventOverview selectedEvent;
    private OnEventSelectedListener listener;

    public interface OnEventSelectedListener {
        void onEventSelected(EventOverview event);
    }

    public EventsReservationAdapter(List<EventOverview> events, OnEventSelectedListener listener) {
        this.events = events;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        EventOverview event = events.get(position);


        // Set background based on selection state
        holder.itemView.setBackgroundResource(event.equals(selectedEvent) ?
                R.color.accent_color :
                android.R.color.transparent);

        if (event.getDate() != null && event.getTitle() != null) {
            try {
                DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                LocalDateTime eventDateTime = LocalDateTime.parse(event.getDate(), inputFormatter);
                DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                String formattedDateTime = eventDateTime.format(outputFormatter);
                String text = event.getTitle() + " - " + formattedDateTime;
                holder.textView.setText(text);
            } catch (DateTimeParseException e) {
                holder.textView.setText(event.getTitle() + " - Invalid date");
            }
        } else {
            holder.textView.setText("Unavailable");
        }

        holder.itemView.setOnClickListener(v -> {
            selectedEvent = event;
            notifyDataSetChanged();
            if (listener != null) {
                listener.onEventSelected(event);
            }
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        ViewHolder(View view) {
            super(view);
            textView = view.findViewById(android.R.id.text1);
        }
    }
}
