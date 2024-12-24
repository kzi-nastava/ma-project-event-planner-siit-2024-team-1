package com.example.EventPlanner.product;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.EventPlanner.product.ProductForm;
import com.example.EventPlanner.R;
import com.example.EventPlanner.product.Product;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private ArrayList<Product> allProducts;
    private Context context;

    public ProductAdapter(Context context, ArrayList<Product> products) {
        this.context = context;
        this.allProducts = products;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_product_card, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = allProducts.get(position);
        holder.bind(product);

        holder.itemView.findViewById(R.id.edit_product).setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductForm.class);
            intent.putExtra("FORM_TYPE", "EDIT_FORM");
            intent.putExtra("PRODUCT_ID", product.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return allProducts.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView productCard; // Change this to MaterialCardView
        ImageView productImage;
        TextView productTitle;
        RatingBar productRating;
        TextView productRatingText;
        TextView productCategory;
        TextView productLocation;
        TextView productPrice;
        TextView productDescription;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productCard = itemView.findViewById(R.id.product_card); // Cast to MaterialCardView
            productImage = itemView.findViewById(R.id.product_image);
            productTitle = itemView.findViewById(R.id.product_title);
            productRating = itemView.findViewById(R.id.product_rating);
            productRatingText = itemView.findViewById(R.id.product_rating_text);
            productCategory = itemView.findViewById(R.id.product_category);
            productLocation = itemView.findViewById(R.id.product_location);
            productPrice = itemView.findViewById(R.id.product_price);
            productDescription = itemView.findViewById(R.id.product_description);
        }

        public void bind(Product product) {
            if (product.getPhotos() != null && !product.getPhotos().getPhoto().isEmpty()) {
                productImage.setImageResource(R.drawable.dinja); // Use appropriate image logic
            }
            productTitle.setText(product.getTitle());
            productRating.setRating(product.getRating().floatValue());
            productRatingText.setText(String.format(Locale.getDefault(), "%.1f", product.getRating()));
            productCategory.setText(product.getCategory());
            productLocation.setText(String.format("%s, %s", product.getAddress().getCity(), product.getAddress().getStreet()));
            productPrice.setText(String.format("%.2f RSD", product.getPrice()));
            productDescription.setText(product.getDescription());
        }
    }

}
