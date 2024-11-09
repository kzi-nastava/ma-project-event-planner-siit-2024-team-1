package com.example.zadatak2.event;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.zadatak2.R;

import java.util.ArrayList;
import java.util.Locale;

public class EventsListAdapter extends ArrayAdapter<Event> {
    private ArrayList<Event> aEvents;
    public EventsListAdapter(Context context, ArrayList<Event> events){
        super(context, R.layout.fragment_event_card,events);
        aEvents=events;
    }

    @Override
    public int getCount(){
        return aEvents.size();
    }
    @Nullable
    @Override
    public Event getItem(int position){
        return  aEvents.get(position);
    }
    @Override
    public long getItemId(int position){
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Event event = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_event_card, parent, false);
        }

        LinearLayout eventCard = convertView.findViewById(R.id.event_card_item);
        TextView eventTitle = convertView.findViewById(R.id.event_title);
        TextView eventDescription = convertView.findViewById(R.id.event_description);
        TextView eventLocation = convertView.findViewById(R.id.event_location);
        TextView eventType = convertView.findViewById(R.id.event_type);

        if (event != null) {
            eventTitle.setText(event.getTitle());
            eventDescription.setText(event.getDescription());
            eventLocation.setText(String.format("%s, %s",
                    event.getAddress().getCity(),
                    event.getAddress().getStreet()));
            eventCard.setOnClickListener(v -> {
                Toast.makeText(getContext(), event.getTitle(), Toast.LENGTH_SHORT).show();
            });
            eventType.setText(String.format(Locale.getDefault(), "%s", event.getType().getTitle()));
        }

        return convertView;
    }
}
