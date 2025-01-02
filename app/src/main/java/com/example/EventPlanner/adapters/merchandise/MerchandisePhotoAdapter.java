package com.example.EventPlanner.adapters.merchandise;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.EventPlanner.R;
import com.example.EventPlanner.clients.ClientUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MerchandisePhotoAdapter extends RecyclerView.Adapter<MerchandisePhotoAdapter.PhotoViewHolder> {
    private List<Integer> photos;
    private Set<Integer> selectedPhotos = new HashSet<>();
    private Context context;

    public MerchandisePhotoAdapter(List<Integer> photos, Context context) {
        this.photos = photos;
        this.context = context;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_photo, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        String photoFilename = photos.get(position); // Assuming photos contains filenames like "picture.jpg"

        // Load photo using Glide, passing the filename directly
        Glide.with(context)
                .load(ClientUtils.photoService.getPhoto(photoFilename)) // Assuming this method returns the correct path or stream
                .into(holder.photoView);

        // Handle selection
        holder.itemView.setOnClickListener(v -> {
            if (selectedPhotos.contains(photoFilename)) {
                selectedPhotos.remove(photoFilename);
                holder.itemView.setBackgroundColor(Color.TRANSPARENT);
            } else {
                selectedPhotos.add(photoFilename);
                holder.itemView.setBackgroundColor(Color.LTGRAY);
            }
        });

        // Highlight the selected photo
        if (selectedPhotos.contains(photoFilename)) {
            holder.itemView.setBackgroundColor(Color.LTGRAY);
        } else {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    public List<Integer> getSelectedPhotoIds() {
        return new ArrayList<>(selectedPhotos);
    }

    public void updatePhotos(List<Integer> newPhotos) {
        photos = newPhotos;
        notifyDataSetChanged();
    }

    static class PhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView photoView;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            photoView = itemView.findViewById(R.id.photo_view);
        }
    }
}