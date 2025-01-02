package com.example.EventPlanner.adapters.merchandise;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.EventPlanner.R;
import com.example.EventPlanner.clients.ClientUtils;

import java.io.InputStream;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MerchandisePhotoAdapter extends RecyclerView.Adapter<MerchandisePhotoAdapter.ViewHolder> {

    private final List<String> selectedPhotos; // URLs of photos
    private final OnItemClickListener onItemClickListener;

    public MerchandisePhotoAdapter(List<String> selectedPhotos, OnItemClickListener onItemClickListener) {
        this.selectedPhotos = selectedPhotos;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_merc_photo, parent, false); // Inflate the new item layout
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String filename = selectedPhotos.get(position);

        ClientUtils.photoService.getPhoto(filename).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        // Convert the InputStream to a Bitmap
                        InputStream inputStream = response.body().byteStream();
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                        // Check if the bitmap is successfully decoded
                        if (bitmap != null) {
                            holder.imageView.setImageBitmap(bitmap);
                            holder.textView.setText(filename);
                        } else {
                            holder.imageView.setImageResource(R.drawable.error);
                            holder.textView.setText("error");// Set error drawable
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        holder.imageView.setImageResource(R.drawable.error); // Set error drawable on exception
                    }
                } else {
                    holder.imageView.setImageResource(R.drawable.error); // Set error drawable
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                holder.imageView.setImageResource(R.drawable.error); // Set error drawable on failure
            }
        });
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(position); // Notify the listener
            }
        });
    }

    @Override
    public int getItemCount() {
        return selectedPhotos.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewPhoto);
            textView = itemView.findViewById(R.id.textViewPhotoName);
        }
    }
}
