package com.example.EventPlanner.adapters.event;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.EventPlanner.R;
import com.example.EventPlanner.model.event.EventType;
import com.example.EventPlanner.activities.EventTypeForm;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class EventTypeAdapter extends RecyclerView.Adapter<EventTypeAdapter.EventTypeViewHolder> {
    private ArrayList<EventType> allEventTypes;
    private Context context;

    public EventTypeAdapter(Context context, ArrayList<EventType> eventTypes) {
        this.context = context;
        this.allEventTypes = eventTypes;
    }

    @NonNull
    @Override
    public EventTypeAdapter.EventTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_event_type_card, parent, false);
        return new EventTypeAdapter.EventTypeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventTypeAdapter.EventTypeViewHolder holder, int position) {
        EventType eventType = allEventTypes.get(position);
        holder.bind(eventType);
        if(eventType.getActive()){
            holder.eventTypeActive.setText("Active");
        }else{
            holder.eventTypeActive.setText("Inactive");
        }

        holder.itemView.findViewById(R.id.edit_event_type).setOnClickListener(v -> {
            Intent intent = new Intent(context, EventTypeForm.class);
            intent.putExtra("FORM_TYPE", "EDIT_FORM");
            intent.putExtra("EVENT_TYPE_ID", eventType.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return allEventTypes.size();
    }

    static class EventTypeViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView eventTypeCard; // Change this to MaterialCardView
        TextView eventTypeTitle;
        TextView eventTypeDescription;
        TextView eventTypeActive;

        public EventTypeViewHolder(@NonNull View itemView) {
            super(itemView);
            eventTypeCard = itemView.findViewById(R.id.event_type_card); // Cast to MaterialCardView
            eventTypeTitle = itemView.findViewById(R.id.event_type_title);
            eventTypeDescription = itemView.findViewById(R.id.event_type_description);
            eventTypeActive = itemView.findViewById(R.id.event_type_active);
        }

        public void bind(EventType eventType) {
            eventTypeTitle.setText(eventType.getTitle());
            eventTypeDescription.setText(eventType.getDescription());
            eventTypeActive.setText(String.valueOf(eventType.getActive()));
        }
    }

}
