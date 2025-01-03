package com.example.EventPlanner.adapters.merchandise.product;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.EventPlanner.R;
import com.example.EventPlanner.activities.HomeScreen;
import com.example.EventPlanner.adapters.merchandise.PhotoSliderAdapter;
import com.example.EventPlanner.clients.ClientUtils;
import com.example.EventPlanner.fragments.merchandise.product.ProductCRUD;
import com.example.EventPlanner.model.event.EventTypeOverview;
import com.example.EventPlanner.model.merchandise.MerchandisePhoto;
import com.example.EventPlanner.model.merchandise.product.Product;
import com.example.EventPlanner.activities.ProductForm;
import com.example.EventPlanner.model.merchandise.product.ProductOverview;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private ArrayList<ProductOverview> allProducts;
    private Context context;

    public ProductAdapter(Context context, ArrayList<ProductOverview> products) {
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
        ProductOverview product = allProducts.get(position);
        holder.bind(product);

        holder.itemView.findViewById(R.id.edit_product).setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductForm.class);
            intent.putExtra("FORM_TYPE", "EDIT_FORM");
            intent.putExtra("PRODUCT_ID", product.getId());
            context.startActivity(intent);
        });
        holder.itemView.findViewById(R.id.show_product).setOnClickListener(v -> {
            Call<Boolean> call1 = ClientUtils.productService.show(product.getId());
            call1.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Intent intent = new Intent(context, HomeScreen.class);
                        context.startActivity(intent);
                    } else {
                        // Handle error cases
                        Log.e("Showing Error", "Response not successful: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable throwable) {
                    // Handle network errors
                    Log.e("Showing Failure", "Error: " + throwable.getMessage());
                }
            });
        });
        holder.itemView.findViewById(R.id.avail_product).setOnClickListener(v -> {
            Call<Boolean> call1 = ClientUtils.productService.avail(product.getId());
            call1.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Intent intent = new Intent(context, HomeScreen.class);
                        context.startActivity(intent);
                    } else {
                        // Handle error cases
                        Log.e("Availabling Error", "Response not successful: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable throwable) {
                    // Handle network errors
                    Log.e("Availabling Failure", "Error: " + throwable.getMessage());
                }
            });
        });

        holder.itemView.findViewById(R.id.delete_product).setOnClickListener(v -> {
            Call<Boolean> call1 = ClientUtils.productService.delete(product.getId());
            call1.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Intent intent = new Intent(context, HomeScreen.class);
                        context.startActivity(intent);
                    } else {
                        // Handle error cases
                        Log.e("Availabling Error", "Response not successful: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable throwable) {
                    // Handle network errors
                    Log.e("Availabling Failure", "Error: " + throwable.getMessage());
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return allProducts.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView productCard; // Change this to MaterialCardView
        TextView productTitle;
        TextView productCategory;
        TextView productDiscountedPrice;
        TextView productPrice;
        TextView productDescription;
        TextView specificity;
        TextView visibility;
        TextView availabiliy;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productCard = itemView.findViewById(R.id.product_card); // Cast to MaterialCardView
            productTitle = itemView.findViewById(R.id.product_title);
            productCategory = itemView.findViewById(R.id.product_category);
            productPrice = itemView.findViewById(R.id.product_price);
            productDiscountedPrice = itemView.findViewById(R.id.discounted_price);
            productDescription = itemView.findViewById(R.id.product_description);
            specificity = itemView.findViewById(R.id.product_specificity);
            availabiliy = itemView.findViewById(R.id.available);
            visibility = itemView.findViewById(R.id.visibility);
        }

        public void bind(ProductOverview product) {
            productTitle.setText(product.getTitle());
            productCategory.setText(product.getCategory().getTitle());
            visibility.setText(product.getVisible() ? "Visible" : "Hidden");
            availabiliy.setText(product.getAvailable() ? "Available" : "Unavailable");

            productPrice.setText(String.format("%.2f RSD", product.getPrice()));
            double discountedPrice = product.getPrice() - ((double)(product.getDiscount())/100) * product.getPrice();
            productDiscountedPrice.setText(String.format("%.2f RSD", discountedPrice));
            productDescription.setText(product.getDescription());
            specificity.setText(product.getSpecificity());

            if (product.getMerchandisePhotos() != null && !product.getMerchandisePhotos().isEmpty()) {
                PhotoSliderAdapter photoSliderAdapter = new PhotoSliderAdapter(itemView.getContext(), product.getMerchandisePhotos());
                ViewPager2 photoSlider = itemView.findViewById(R.id.photo_slider);
                photoSlider.setAdapter(photoSliderAdapter);
            }
        }
    }

}
