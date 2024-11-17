package com.example.zadatak2.service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zadatak2.R;
import com.example.zadatak2.ServiceForm;

import java.util.ArrayList;
import java.util.Locale;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder> {
    private ArrayList<Service> allServices;
    private Context context;

    public  ServiceAdapter(Context context, ArrayList<Service> services) {
        this.context = context;
        this.allServices = services;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_service_card, parent, false);
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        Service service = allServices.get(position);
        holder.bind(service);

        holder.itemView.findViewById(R.id.edit_service).setOnClickListener(v -> {
            Intent intent = new Intent(context, ServiceForm.class);
            intent.putExtra("FORM_TYPE", "EDIT_FORM");
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return allServices.size();
    }

    static class ServiceViewHolder extends RecyclerView.ViewHolder {
        LinearLayout serviceCard;
        ImageView serviceImage;
        TextView serviceTitle;
        RatingBar serviceRating;
        TextView serviceRatingText;
        TextView serviceCategory;
        TextView serviceLocation;
        TextView servicePrice;
        TextView serviceDescription;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            serviceCard = itemView.findViewById(R.id.service_card_item);
            serviceImage = itemView.findViewById(R.id.service_image);
            serviceTitle = itemView.findViewById(R.id.service_title);
            serviceRating = itemView.findViewById(R.id.service_rating);
            serviceRatingText = itemView.findViewById(R.id.service_rating_text);
            serviceCategory = itemView.findViewById(R.id.service_category);
            serviceLocation = itemView.findViewById(R.id.service_location);
            servicePrice = itemView.findViewById(R.id.service_price);
            serviceDescription = itemView.findViewById(R.id.service_description);
        }

        public void bind(Service service) {
            if (service.getPhotos() != null && !service.getPhotos().isEmpty()) {
                serviceImage.setImageResource(R.drawable.dinja);
            }
            serviceTitle.setText(service.getTitle());
            serviceRating.setRating(service.getRating().floatValue());
            serviceRatingText.setText(String.format(Locale.getDefault(), "%.1f", service.getRating()));
            serviceCategory.setText(String.format(Locale.getDefault(), "%s/%s",
                    service.getCategory().getTitle(),
                    service.getClass().getSimpleName()));
            serviceLocation.setText(String.format("%s, %s",
                    service.getAddress().getCity(),
                    service.getAddress().getStreet()));
            double finalPrice = service.getPrice() * (1 - service.getDiscount());
            servicePrice.setText(String.format("$%.2f", finalPrice));
            serviceDescription.setText(service.getDescription());
        }
    }
}
