package com.example.EventPlanner.eventType;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.EventPlanner.R;
import com.example.EventPlanner.merchandise.MerchandisePhoto;
import com.example.EventPlanner.product.PhotoSliderAdapter;
import com.example.EventPlanner.product.Product;
import com.example.EventPlanner.product.ProductForm;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class EventTypeAdapter extends RecyclerView.Adapter<EventTypeAdapter.EventTypeViewHolder> {
    private ArrayList<EventType> allEventTypes;
    private Context context;

    public EventTypeAdapter(Context context, ArrayList<EventType> eventTypes) {
        this.context = context;
        this.allEventTypes = eventTypes;
    }

    @NonNull
    @Override
    public com.example.EventPlanner.eventType.EventTypeAdapter.EventTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_event_type_card, parent, false);
        return new com.example.EventPlanner.eventType.EventTypeAdapter.EventTypeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull com.example.EventPlanner.eventType.EventTypeAdapter.EventTypeViewHolder holder, int position) {
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
