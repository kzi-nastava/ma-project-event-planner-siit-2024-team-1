package com.example.EventPlanner.product;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.EventPlanner.R;
import com.example.EventPlanner.merchandise.MerchandisePhoto;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

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
        TextView productTitle;
        TextView productCategory;
        TextView productDiscountedPrice;
        TextView productPrice;
        TextView productDescription;
        TextView specificity;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productCard = itemView.findViewById(R.id.product_card); // Cast to MaterialCardView
            productTitle = itemView.findViewById(R.id.product_title);
            productCategory = itemView.findViewById(R.id.product_category);
            productPrice = itemView.findViewById(R.id.product_price);
            productDiscountedPrice = itemView.findViewById(R.id.discounted_price);
            productDescription = itemView.findViewById(R.id.product_description);
            specificity = itemView.findViewById(R.id.product_specificity);
        }

        public void bind(Product product) {
            productTitle.setText(product.getTitle());
            productCategory.setText(product.getCategory().getTitle());
            productPrice.setText(String.format("%.2f RSD", product.getPrice()));
            productDiscountedPrice.setText(String.format("%.2f RSD", product.getPrice()));
            productDescription.setText(product.getDescription());
            specificity.setText(product.getSpecificity());

            // Example of loading drawable images
            List<MerchandisePhoto> photoResources = new ArrayList<>();
            photoResources.add(new MerchandisePhoto(R.drawable.dinja, null)); // From drawable
            photoResources.add(new MerchandisePhoto(R.drawable.calendar, null)); // From drawable
            photoResources.add(new MerchandisePhoto(0, "https://www.google.com/imgres?q=melon&imgurl=https%3A%2F%2Fwww.agroponiente.com%2Fwp-content%2Fuploads%2F2024%2F06%2Fmelon-amarillo-Agroponiente.png&imgrefurl=https%3A%2F%2Fwww.agroponiente.com%2Fen%2Ffruits-vegetables%2Fmelon-en%2Fyellow-melon%2F&docid=Hoxh5ZO0Z6yKcM&tbnid=GDhNQTy8JTFN9M&vet=12ahUKEwiQ3ezttsOKAxU08LsIHf4VK5MQM3oECHcQAA..i&w=768&h=768&hcb=2&ved=2ahUKEwiQ3ezttsOKAxU08LsIHf4VK5MQM3oECHcQAA")); // Online image
            photoResources.add(new MerchandisePhoto(0, "https://www.google.com/imgres?q=melon&imgurl=https%3A%2F%2Fcdn.britannica.com%2F99%2F143599-050-C3289491%2FWatermelon.jpg&imgrefurl=https%3A%2F%2Fwww.britannica.com%2Fplant%2Fmelon&docid=DslcpmVaaz9dCM&tbnid=Bw4CZ9OZn69IXM&vet=12ahUKEwiQ3ezttsOKAxU08LsIHf4VK5MQM3oECHAQAA..i&w=620&h=600&hcb=2&ved=2ahUKEwiQ3ezttsOKAxU08LsIHf4VK5MQM3oECHAQAA"));

// Set photos in the product and update the adapter
            product.setMerchandisePhotos(photoResources);

            if (product.getMerchandisePhotos() != null && !product.getMerchandisePhotos().isEmpty()) {
                PhotoSliderAdapter photoSliderAdapter = new PhotoSliderAdapter(itemView.getContext(), product.getMerchandisePhotos());
                ViewPager2 photoSlider = itemView.findViewById(R.id.photo_slider);
                photoSlider.setAdapter(photoSliderAdapter);
            }
        }
    }

}
