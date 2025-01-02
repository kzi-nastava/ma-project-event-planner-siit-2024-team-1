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

import com.example.EventPlanner.R;
import com.example.EventPlanner.clients.ClientUtils;

import java.io.InputStream;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusinessPhotoAdapter extends RecyclerView.Adapter<com.example.EventPlanner.adapters.merchandise.BusinessPhotoAdapter.ViewHolder> {

    private List<String> selectedPhotos;
    private com.example.EventPlanner.adapters.merchandise.BusinessPhotoAdapter.OnItemClickListener onItemClickListener;

    // Constructor accepting the custom OnItemClickListener
    public BusinessPhotoAdapter(List<String> selectedPhotos, com.example.EventPlanner.adapters.merchandise.BusinessPhotoAdapter.OnItemClickListener onItemClickListener) {
        this.selectedPhotos = selectedPhotos;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public com.example.EventPlanner.adapters.merchandise.BusinessPhotoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sp_photo, parent, false); // Inflate the new item layout
        return new BusinessPhotoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(com.example.EventPlanner.adapters.merchandise.BusinessPhotoAdapter.ViewHolder holder, int position) {
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

    // Custom listener interface
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
