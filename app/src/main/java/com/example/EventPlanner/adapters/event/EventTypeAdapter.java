package com.example.EventPlanner.adapters.event;

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
import com.example.EventPlanner.activities.HomeScreen;
import com.example.EventPlanner.activities.LoginScreen;
import com.example.EventPlanner.clients.ClientUtils;
import com.example.EventPlanner.clients.JwtService;
import com.example.EventPlanner.model.auth.LoginResponse;
import com.example.EventPlanner.model.event.EventType;
import com.example.EventPlanner.activities.EventTypeForm;
import com.example.EventPlanner.model.event.EventTypeOverview;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventTypeAdapter extends RecyclerView.Adapter<EventTypeAdapter.EventTypeViewHolder> {
    private ArrayList<EventTypeOverview> allEventTypes;
    private Context context;
    public EventTypeAdapter(Context context, ArrayList<EventTypeOverview> eventTypes) {
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
        EventTypeOverview eventType = allEventTypes.get(position);
        holder.bind(eventType);
        if(eventType.isActive()){
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
        holder.itemView.findViewById(R.id.delete_event_type).setOnClickListener(v -> {
            Call<EventTypeOverview> call1 = ClientUtils.eventTypeService.deactivate(holder.selectedEventTypeId);
            call1.enqueue(new Callback<EventTypeOverview>() {
                @Override
                public void onResponse(Call<EventTypeOverview> call, Response<EventTypeOverview> response) {
                    if (response.isSuccessful() && response.body() != null) {

                    } else {
                        // Handle error cases
                        Log.e("Login Error", "Response not successful: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<EventTypeOverview> call, Throwable throwable) {
                    // Handle network errors
                    Log.e("Deactivating Failure", "Error: " + throwable.getMessage());
                }
            });
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
        Integer selectedEventTypeId;


        public EventTypeViewHolder(@NonNull View itemView) {
            super(itemView);
            eventTypeCard = itemView.findViewById(R.id.event_type_card); // Cast to MaterialCardView
            eventTypeTitle = itemView.findViewById(R.id.event_type_title);
            eventTypeDescription = itemView.findViewById(R.id.event_type_description);
            eventTypeActive = itemView.findViewById(R.id.event_type_active);
        }

        public void bind(EventTypeOverview eventType) {
            eventTypeTitle.setText(eventType.getTitle());
            eventTypeDescription.setText(eventType.getDescription());
            eventTypeActive.setText(String.valueOf(eventType.isActive()));
            selectedEventTypeId = eventType.getId();
        }
    }

}
