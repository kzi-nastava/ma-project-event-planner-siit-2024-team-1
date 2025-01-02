package com.example.EventPlanner.adapters.merchandise;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MerchandisePhotoAdapter extends RecyclerView.Adapter<MerchandisePhotoAdapter.ViewHolder> {

    private List<String> selectedPhotos;
    private OnItemClickListener onItemClickListener;

    // Constructor accepting the custom OnItemClickListener
    public MerchandisePhotoAdapter(List<String> selectedPhotos, OnItemClickListener onItemClickListener) {
        this.selectedPhotos = selectedPhotos;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView.setText(selectedPhotos.get(position));

        // Use the custom OnItemClickListener
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(position));
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
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }
    }
}