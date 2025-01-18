package com.example.EventPlanner.adapters.merchandise;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
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

import com.bumptech.glide.Glide;
import com.example.EventPlanner.R;
import com.example.EventPlanner.activities.ServiceDetailsActivity;
import com.example.EventPlanner.clients.ClientUtils;
import com.example.EventPlanner.fragments.activity.ActivityCRUD;
import com.example.EventPlanner.model.merchandise.Merchandise;
import com.example.EventPlanner.model.merchandise.MerchandiseOverview;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MerchandiseOverviewAdapter extends RecyclerView.Adapter<MerchandiseOverviewAdapter.MerchandiseViewHolder> {
    private ArrayList<MerchandiseOverview> aMerchandises;
    private Context context;

    public MerchandiseOverviewAdapter(Context context, ArrayList<MerchandiseOverview> merchandises) {
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
        MerchandiseOverview merchandise = aMerchandises.get(position);

        if (!merchandise.getPhotos().isEmpty()) {
            ClientUtils.photoService.getPhoto(merchandise.getPhotos().get(0).getPhoto()).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        InputStream inputStream = response.body().byteStream();
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                        // Load bitmap with Glide
                        Glide.with(context)
                                .load(bitmap)
                                .into(holder.merchandiseImage);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    // Handle failure
                    holder.merchandiseImage.setImageResource(R.drawable.error); // Fallback image
                }
            });
        } else {
            holder.merchandiseImage.setImageResource(R.drawable.no_image);
        }

        holder.merchandiseTitle.setText(merchandise.getTitle());
        holder.merchandiseDescription.setText(merchandise.getDescription());

        holder.merchandisePrice.setText(String.format("%.2f RSD", merchandise.getPrice()));
        holder.merchandiseDiscount.setVisibility(View.GONE);


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
                merchandise.getCategory(),
                merchandise.getType()));
        holder.itemView.findViewById(R.id.see_details_button).setOnClickListener(v -> {
            if(merchandise.getType().equals("Service")) {
                Intent intent = new Intent(context, ServiceDetailsActivity.class);
                intent.putExtra("MERCHANDISE_ID", merchandise.getId());
                context.startActivity(intent);
            }
        });
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
