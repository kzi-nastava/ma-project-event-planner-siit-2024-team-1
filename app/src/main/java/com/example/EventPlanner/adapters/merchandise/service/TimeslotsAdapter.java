package com.example.EventPlanner.adapters.merchandise.service;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.EventPlanner.model.merchandise.service.Timeslot;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;

public class TimeslotsAdapter extends RecyclerView.Adapter<TimeslotsAdapter.ViewHolder> {
    private List<Timeslot> timeslots;

    public TimeslotsAdapter(List<Timeslot> timeslots) {
        this.timeslots = timeslots;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Timeslot slot = timeslots.get(position);

        if (slot.getStartTime() != null && slot.getEndTime() != null) {
            try {
                // Parse the input strings to LocalDateTime
                DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"); // Adjust pattern to match input format
                LocalDateTime startDateTime = LocalDateTime.parse(slot.getStartTime(), inputFormatter);
                LocalDateTime endDateTime = LocalDateTime.parse(slot.getEndTime(), inputFormatter);

                // Define the desired output format
                DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

                // Format the dates to the desired string
                String formattedStartTime = startDateTime.format(outputFormatter);
                String formattedEndTime = endDateTime.format(outputFormatter);

                // Set the formatted text to the TextView
                String text = formattedStartTime + " - " + formattedEndTime;
                holder.textView.setText(text);

            } catch (DateTimeParseException e) {
                e.printStackTrace();
                holder.textView.setText("Invalid date");
            }
        } else {
            holder.textView.setText("Unavailable");
        }
    }


    @Override
    public int getItemCount() {
        return timeslots.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        ViewHolder(View view) {
            super(view);
            textView = view.findViewById(android.R.id.text1);
        }
    }
}

