package com.example.EventPlanner.adapters.merchandise.service;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.EventPlanner.R;
import com.example.EventPlanner.activities.ServiceDetailsActivity;
import com.example.EventPlanner.activities.ServiceForm;
import com.example.EventPlanner.adapters.merchandise.PhotoSliderAdapter;
import com.example.EventPlanner.model.merchandise.service.ServiceOverview;

import java.util.ArrayList;
import java.util.Locale;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder> {
    private ArrayList<ServiceOverview> allServices;
    private Context context;

    public  ServiceAdapter(Context context, ArrayList<ServiceOverview> services) {
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
        ServiceOverview service = allServices.get(position);
        holder.bind(service);

        holder.itemView.findViewById(R.id.edit_service).setOnClickListener(v -> {
            Intent intent = new Intent(context, ServiceForm.class);
            intent.putExtra("FORM_TYPE", "EDIT_FORM");
            intent.putExtra("SERVICE_ID", service.getId());
            context.startActivity(intent);
        });
        holder.itemView.findViewById(R.id.service_details).setOnClickListener(v -> {
            Intent intent = new Intent(context, ServiceDetailsActivity.class);
            intent.putExtra("MERCHANDISE_ID", service.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return allServices.size();
    }

    static class ServiceViewHolder extends RecyclerView.ViewHolder {
        LinearLayout serviceCard;
        TextView serviceTitle;
        TextView serviceCategory;
        TextView serviceLocation;
        TextView servicePrice;
        TextView serviceDescription;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            serviceCard = itemView.findViewById(R.id.service_card_item);
            serviceTitle = itemView.findViewById(R.id.service_title);
            serviceCategory = itemView.findViewById(R.id.service_category);
            serviceLocation = itemView.findViewById(R.id.service_location);
            servicePrice = itemView.findViewById(R.id.service_price);
            serviceDescription = itemView.findViewById(R.id.service_description);
        }

        public void bind(ServiceOverview service) {
            if (service.getMerchandisePhotos() != null && !service.getMerchandisePhotos().isEmpty()) {
                PhotoSliderAdapter photoSliderAdapter = new PhotoSliderAdapter(itemView.getContext(), service.getMerchandisePhotos());
                ViewPager2 photoSlider = itemView.findViewById(R.id.service_image);
                photoSlider.setAdapter(photoSliderAdapter);
            }
            serviceTitle.setText(service.getTitle());
            serviceCategory.setText(String.format(Locale.getDefault(), "%s/%s",
                    service.getCategory().getTitle(),
                    service.getClass().getSimpleName()));
            serviceLocation.setText(String.format("%s, %s %s",
                    service.getAddress().getCity(),
                    service.getAddress().getStreet(),
                    service.getAddress().getNumber()));
            double finalPrice = service.getPrice() - (service.getPrice() * service.getDiscount())/100;
            servicePrice.setText(String.format("%.2f $", finalPrice));
            serviceDescription.setText(service.getDescription());
        }
    }
}
