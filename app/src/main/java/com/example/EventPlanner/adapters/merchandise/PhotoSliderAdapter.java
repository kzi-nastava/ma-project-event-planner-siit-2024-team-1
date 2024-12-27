package com.example.EventPlanner.adapters.merchandise;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.EventPlanner.R;
import com.example.EventPlanner.model.merchandise.MerchandisePhoto;

import java.util.List;

public class PhotoSliderAdapter extends RecyclerView.Adapter<PhotoSliderAdapter.PhotoViewHolder> {
    private List<MerchandisePhoto> photos; // List of image URLs
    private Context context;

    public PhotoSliderAdapter(Context context, List<MerchandisePhoto> photos) {
        this.context = context;
        this.photos = photos;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        MerchandisePhoto photo = photos.get(position);

        if (photo.getId() != 0) {
            // Load from drawable
            holder.photoImageView.setImageResource(photo.getId());
        } else if (photo.getPhoto() != null) {
            // Load from URL using Glide
            Glide.with(context)
                    .load(photo.getPhoto())
                    .into(holder.photoImageView);
        }
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView photoImageView;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            photoImageView = itemView.findViewById(R.id.photo_image_view);
        }
    }
}
