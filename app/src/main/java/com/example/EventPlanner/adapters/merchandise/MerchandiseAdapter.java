package com.example.EventPlanner.adapters.merchandise;

import android.content.Context;
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

import com.example.EventPlanner.R;
import com.example.EventPlanner.model.merchandise.Merchandise;

import java.util.ArrayList;
import java.util.Locale;

public class MerchandiseAdapter extends RecyclerView.Adapter<MerchandiseAdapter.MerchandiseViewHolder> {
    private ArrayList<Merchandise> aMerchandises;
    private Context context;

    public MerchandiseAdapter(Context context, ArrayList<Merchandise> merchandises) {
        this.context = context;
        aMerchandises = merchandises;
    }

    @NonNull
    @Override
    public MerchandiseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_merchandise_card, parent, false);
        return new MerchandiseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MerchandiseViewHolder holder, int position) {
        Merchandise merchandise = aMerchandises.get(position);

        if (merchandise.getPhotos() != null && !merchandise.getPhotos().isEmpty()) {
            holder.merchandiseImage.setImageResource(R.drawable.dinja);
        }

        holder.merchandiseTitle.setText(merchandise.getTitle());
        holder.merchandiseDescription.setText(merchandise.getDescription());

        double finalPrice = merchandise.getPrice() * (1 - merchandise.getDiscount());
        holder.merchandisePrice.setText(String.format("%.2f RSD", finalPrice));

        if (merchandise.getDiscount() > 0) {
            holder.merchandiseDiscount.setVisibility(View.VISIBLE);
            holder.merchandiseDiscount.setText(String.format("-%d%%", (int) (merchandise.getDiscount() * 100)));
        } else {
            holder.merchandiseDiscount.setVisibility(View.GONE);
        }

        holder.merchandiseLocation.setText(String.format("%s, %s %s",
                merchandise.getAddress().getCity(),
                merchandise.getAddress().getStreet(),
                merchandise.getAddress().getNumber()));

        holder.merchandiseRating.setRating(merchandise.getRating().floatValue());
        holder.merchandiseRatingText.setText(String.format(Locale.getDefault(), "%.1f", merchandise.getRating()));
        holder.merchandiseCard.setOnClickListener(v -> {
            Toast.makeText(context, merchandise.getTitle(), Toast.LENGTH_SHORT).show();
        });
        holder.merchandiseCategory.setText(String.format(Locale.getDefault(), "%s/%s",
                merchandise.getCategory().getTitle(),
                merchandise.getClass().getSimpleName()));
    }

    @Override
    public int getItemCount() {
        return aMerchandises.size();
    }

    static class MerchandiseViewHolder extends RecyclerView.ViewHolder {
        LinearLayout merchandiseCard;
        ImageView merchandiseImage;
        TextView merchandiseTitle;
        TextView merchandiseDescription;
        TextView merchandisePrice;
        TextView merchandiseLocation;
        RatingBar merchandiseRating;
        TextView merchandiseDiscount;
        TextView merchandiseRatingText;
        TextView merchandiseCategory;

        MerchandiseViewHolder(@NonNull View itemView) {
            super(itemView);
            merchandiseCard = itemView.findViewById(R.id.merchandise_card_item);
            merchandiseImage = itemView.findViewById(R.id.merchandise_image);
            merchandiseTitle = itemView.findViewById(R.id.merchandise_title);
            merchandiseDescription = itemView.findViewById(R.id.merchandise_description);
            merchandisePrice = itemView.findViewById(R.id.merchandise_price);
            merchandiseLocation = itemView.findViewById(R.id.merchandise_location);
            merchandiseRating = itemView.findViewById(R.id.merchandise_rating);
            merchandiseDiscount = itemView.findViewById(R.id.merchandise_discount);
            merchandiseRatingText = itemView.findViewById(R.id.merchandise_rating_text);
            merchandiseCategory = itemView.findViewById(R.id.merchandise_category);
        }
    }
}