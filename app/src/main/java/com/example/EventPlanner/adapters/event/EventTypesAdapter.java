package com.example.EventPlanner.adapters.event;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.EventPlanner.R;

import java.util.List;

public class EventTypesAdapter extends RecyclerView.Adapter<EventTypesAdapter.EventTypeViewHolder> {
    private final List<String> eventTypes;

    public EventTypesAdapter(List<String> eventTypes) {
        this.eventTypes = eventTypes;
    }

    @NonNull
    @Override
    public EventTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event_type, parent, false);
        return new EventTypeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventTypeViewHolder holder, int position) {
        String eventType = eventTypes.get(position);
        holder.eventTypeText.setText(eventType);
    }

    @Override
    public int getItemCount() {
        return eventTypes.size();
    }

    static class EventTypeViewHolder extends RecyclerView.ViewHolder {
        TextView eventTypeText;

        EventTypeViewHolder(@NonNull View itemView) {
            super(itemView);
            eventTypeText = itemView.findViewById(R.id.event_type_text);
        }
    }
}
