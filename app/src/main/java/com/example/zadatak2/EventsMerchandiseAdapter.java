package com.example.zadatak2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zadatak2.event.Event;
import com.example.zadatak2.merchandise.Merchandise;

import java.util.List;
import java.util.Locale;

public class EventsMerchandiseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<EventMerchandise> eventMerchandiseList;

    // public constructor for this class
    public EventsMerchandiseAdapter(List<EventMerchandise> eventMerchandiseList) {
        this.eventMerchandiseList = eventMerchandiseList;
    }

    // Override the getItemViewType method
    @Override
    public int getItemViewType(int position) {
        return eventMerchandiseList.get(position).getViewType();
    }

    // Create classes for each layout ViewHolder
    public class EventViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView description;
        private final TextView address;
        private final TextView date;
        private final TextView type;
        private final LinearLayout eventCard;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.event_title);
            description = itemView.findViewById(R.id.event_description);
            address = itemView.findViewById(R.id.event_location);
            date = itemView.findViewById(R.id.event_date);
            type = itemView.findViewById(R.id.event_type);
            eventCard = itemView.findViewById(R.id.event_card_item);
        }

        private void setViews(String titleText, String descriptionText, String addressText, String dateText, String typeText) {
            title.setText(titleText);
            description.setText(descriptionText);
            address.setText(addressText);
            date.setText(dateText);
            type.setText(typeText);

        }
    }

    static class MerchandiseViewHolder extends RecyclerView.ViewHolder {
        private ImageView photo;
        private TextView title, description,discount,category,location,ratingText,price;
        private LinearLayout linearLayout;
        private RatingBar ratingBar;

        public MerchandiseViewHolder(@NonNull View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.merchandise_image);
            title = itemView.findViewById(R.id.merchandise_title);
            description = itemView.findViewById(R.id.merchandise_description);
            linearLayout = itemView.findViewById(R.id.merchandise_card_item);
            discount=itemView.findViewById(R.id.merchandise_discount);
            category=itemView.findViewById(R.id.merchandise_category);
            location=itemView.findViewById(R.id.merchandise_location);
            ratingText=itemView.findViewById(R.id.merchandise_rating_text);
            price=itemView.findViewById(R.id.merchandise_price);
            ratingBar=itemView.findViewById(R.id.merchandise_rating);
        }

        private void setViews(int image, String textOne, String textTwo, double discountValue, String categoryText, String locationText, double ratingValue, double priceValue,String merchandiseType) {
            photo.setImageResource(image);
            title.setText(textOne);
            description.setText(textTwo);
            if (discountValue > 0) {
                discount.setText("-" + (int)(discountValue * 100) + "%");
                discount.setVisibility(View.VISIBLE);
            } else {
                discount.setVisibility(View.GONE);
            }
            category.setText(String.format(Locale.getDefault(), "%s/%s",categoryText,merchandiseType));
            location.setText(locationText);
            ratingText.setText(String.format("%.1f", ratingValue));
            ratingBar.setRating((float)ratingValue);
            price.setText("$" + String.format("%.2f", priceValue));

        }

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == EventMerchandise.EVENT) {
            View layoutOne = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_event_card, parent, false);
            return new EventViewHolder(layoutOne);
        } else if (viewType == EventMerchandise.MERCHANDISE) {
            View layoutTwo = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_merchandise_card, parent, false);
            return new MerchandiseViewHolder(layoutTwo);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof EventViewHolder) {
            Event event = eventMerchandiseList.get(position).getEvent();
            ((EventViewHolder) holder).setViews(
                    event.getTitle(),
                    event.getDescription(),
                    String.format("%s, %s",
                            event.getAddress().getCity(),
                            event.getAddress().getStreet()),
                    event.getDate().toString(),
                    event.getType().getTitle()
            );
            ((EventViewHolder) holder).eventCard.setOnClickListener(view ->
                    Toast.makeText(view.getContext(), "Hello from merchandise!", Toast.LENGTH_SHORT).show());
        } else if (holder instanceof MerchandiseViewHolder) {
            Merchandise merchandise=eventMerchandiseList.get(position).getMerchandise();
            int image = R.drawable.dinja;
            //int image = eventMerchandiseList.get(position).getMerchandise().getImageResource();
            String title = merchandise.getTitle();
            String description = merchandise.getDescription();
            double discount = merchandise.getDiscount();
            String category = merchandise.getCategory().getTitle();
            String location = String.format("%s, %s",
                    merchandise.getAddress().getCity(),
                    merchandise.getAddress().getStreet());
            double rating = merchandise.getRating();
            double price = merchandise.getPrice();

            ((MerchandiseViewHolder) holder).setViews(image, title, description, discount, category, location, rating, price,merchandise.getClass().getSimpleName());
            ((MerchandiseViewHolder) holder).linearLayout.setOnClickListener(view ->
                    Toast.makeText(view.getContext(), "Hello from merchandise!", Toast.LENGTH_SHORT).show());
        }
    }

    @Override
    public int getItemCount() {
        return eventMerchandiseList.size();
    }
}
