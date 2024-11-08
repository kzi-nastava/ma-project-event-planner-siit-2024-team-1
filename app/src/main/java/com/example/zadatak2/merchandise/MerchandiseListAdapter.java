package com.example.zadatak2.merchandise;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.zadatak2.R;

import java.util.ArrayList;

public class MerchandiseListAdapter extends ArrayAdapter<Merchandise> {
    private ArrayList<Merchandise> aMerchandises;
    public MerchandiseListAdapter(Context context,ArrayList<Merchandise> merchandises){
        super(context, R.layout.fragment_merchandise_card,merchandises);
        aMerchandises=merchandises;
    }

    @Override
    public int getCount(){
        return aMerchandises.size();
    }
    @Nullable
    @Override
    public Merchandise getItem(int position){
        return  aMerchandises.get(position);
    }
    @Override
    public long getItemId(int position){
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Merchandise merchandise = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_merchandise_card, parent, false);
        }

        LinearLayout merchandiseCard = convertView.findViewById(R.id.merchandise_card_item);
        //ImageView merchandiseImage = convertView.findViewById(R.id.merchandise_image);
        TextView merchandiseTitle = convertView.findViewById(R.id.merchandise_title);
        TextView merchandiseDescription = convertView.findViewById(R.id.merchandise_description);
        TextView merchandisePrice = convertView.findViewById(R.id.merchandise_price);
        TextView merchandiseLocation = convertView.findViewById(R.id.merchandise_location);
        //TextView merchandiseDuration = convertView.findViewById(R.id.merchandise_duration);
        RatingBar merchandiseRating = convertView.findViewById(R.id.merchandise_rating);
//        TextView merchandiseReviewCount = convertView.findViewById(R.id.merchandise_review_count);
//        TextView merchandiseDiscount = convertView.findViewById(R.id.merchandise_discount);
//        TextView merchandiseStatus = convertView.findViewById(R.id.merchandise_status);

        if (merchandise != null) {
            // Set main image if available
            if (merchandise.getPhotos() != null && !merchandise.getPhotos().isEmpty()) {
                // You'll need to implement proper image loading here
                // For example using Picasso or Glide
                // Picasso.get().load(merchandise.getPhotos().get(0)).into(merchandiseImage);
            }

            // Set basic information
            merchandiseTitle.setText(merchandise.getTitle());
            merchandiseDescription.setText(merchandise.getDescription());

            // Format price with discount if applicable
            double finalPrice = merchandise.getPrice() * (1 - merchandise.getDiscount());
            merchandisePrice.setText(String.format("$%.2f", finalPrice));

//            if (merchandise.getDiscount() > 0) {
//                merchandiseDiscount.setVisibility(View.VISIBLE);
//                merchandiseDiscount.setText(String.format("-%d%%", (int)(merchandise.getDiscount() * 100)));
//            } else {
//                merchandiseDiscount.setVisibility(View.GONE);
        }

        // Set location
        merchandiseLocation.setText(String.format("%s, %s",
                merchandise.getAddress().getCity(),
                merchandise.getAddress().getStreet()));

        // Set duration range
//            merchandiseDuration.setText(String.format("%d-%d days",
//                    merchandise.getMinDuration(),
//                    merchandise.getMaxDuration()));

        // Calculate average rating
        merchandiseRating.setRating(merchandise.getRating().floatValue());

        // Set status
//            if (!merchandise.isAvailable()) {
//                merchandiseStatus.setText("Not Available");
//                merchandiseStatus.setTextColor(getContext().getColor(android.R.color.holo_red_dark));
//            } else if (!merchandise.isVisible()) {
//                merchandiseStatus.setText("Hidden");
//                merchandiseStatus.setTextColor(getContext().getColor(android.R.color.darker_gray));
//            } else {
//                merchandiseStatus.setText("Available");
//                merchandiseStatus.setTextColor(getContext().getColor(android.R.color.holo_green_dark));
//            }

                // Set click listener
            merchandiseCard.setOnClickListener(v -> {
                Toast.makeText(getContext(), merchandise.getTitle(), Toast.LENGTH_SHORT).show();
            });


        return convertView;
    }
}
