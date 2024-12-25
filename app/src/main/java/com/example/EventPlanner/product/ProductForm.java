package com.example.EventPlanner.product;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.EventPlanner.HomeScreen;
import com.example.EventPlanner.R;
import com.example.EventPlanner.databinding.ActivityProductFormBinding;
import com.example.EventPlanner.product.Product;
import com.example.EventPlanner.product.ProductViewModel;
import com.example.EventPlanner.merchandise.MerchandisePhoto;
import com.example.EventPlanner.address.Address;

import java.util.ArrayList;

public class ProductForm extends AppCompatActivity {
    private ActivityProductFormBinding productFormBinding;
    private ProductViewModel productViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productFormBinding = ActivityProductFormBinding.inflate(getLayoutInflater());
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);

        EdgeToEdge.enable(this);
        setContentView(productFormBinding.getRoot());

        String formType = getIntent().getStringExtra("FORM_TYPE");
        TextView formTitle = productFormBinding.formTitle;

        // Setup form for adding or editing product
        if ("NEW_FORM".equals(formType)) {
            formTitle.setText("New Product");
            productFormBinding.productVisibleCheckbox.setVisibility(View.GONE);
            productFormBinding.productAvailableCheckbox.setVisibility(View.GONE);
        } else if ("EDIT_FORM".equals(formType)) {
            formTitle.setText("Edit Product");
            productFormBinding.productVisibleCheckbox.setVisibility(View.VISIBLE);
            productFormBinding.productAvailableCheckbox.setVisibility(View.VISIBLE);

            int productId = getIntent().getIntExtra("PRODUCT_ID", -1);
            if (productId != -1) {
                Product product = productViewModel.findProductById(productId);
                setFields(product);
            }
        }

        // Setting up Category Spinner
        Spinner categorySpinner = productFormBinding.productCategorySpinner;
        String[] categoryOptions = {"options", "space", "food", "drinks", "music", "decorations", "other"};
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categoryOptions);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        // Handling category selection
        EditText categoryEditText = productFormBinding.categoryInput;
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedOption = categoryOptions[position];
                if ("other".equals(selectedOption)) {
                    categorySpinner.setVisibility(View.GONE);
                    categoryEditText.setVisibility(View.VISIBLE);
                } else {
                    categoryEditText.setVisibility(View.GONE);
                    categorySpinner.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle case when nothing is selected
            }
        });

        // Submit Button Click Listener
        productFormBinding.submitProductButton.setOnClickListener(v -> {
            if (isValidInput()) {
                // Gather input and create Product
                String title = productFormBinding.productName.getText().toString();
                String description = productFormBinding.productDescription.getText().toString();
                String priceText = productFormBinding.productPrice.getText().toString();
                double price = Double.parseDouble(priceText);

                // Assuming you have photo handling logic, here is a simple example
                MerchandisePhoto photos = new MerchandisePhoto(); // Adjust this to add actual photo logic

                Address address = new Address(); // Set the address if required

                // Create Product object
                Product product = new Product(
                        null, // id can be null when creating a new product
                        "Category", // Get category from spinner input
                        "Type", // You may have a type field to handle
                        photos,
                        title,
                        4.5, // Rating, you can change this based on your requirements
                        address,
                        price,
                        description
                );

                productViewModel.saveProduct(product);

                // Navigate back to HomeScreen
                Intent intent = new Intent(ProductForm.this, HomeScreen.class);
                startActivity(intent);
            }
        });

//        // Delete Button Click Listener
//        productFormBinding.deleteProductButton.setOnClickListener(v -> {
//            int productId = getIntent().getIntExtra("PRODUCT_ID", -1);
//            if (productId != -1) {
//                Product product = productViewModel.findProductById(productId);
//                productViewModel.deleteProduct(product);
//            }
//            Intent intent = new Intent(ProductForm.this, HomeScreen.class);
//            startActivity(intent);
//        });

        // Handling system bar padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Set fields with product data if editing
    private void setFields(Product product) {
        productFormBinding.productName.setText(product.getTitle());
        productFormBinding.productDescription.setText(product.getDescription());
        productFormBinding.productPrice.setText(String.format("$%.2f", product.getPrice()));

        if (product.getPhotos() != null) {
            // You can load the photo URLs or handle accordingly
        }

        if (product.getRating() != null) {
            // Set the rating value (e.g., using a RatingBar)
        }

        if (product.getCategory() != null) {
            Spinner categorySpinner = productFormBinding.productCategorySpinner;
            int position = ((ArrayAdapter) categorySpinner.getAdapter()).getPosition(product.getCategory());
            categorySpinner.setSelection(position);
        }
    }

    // Validate the input fields
    private boolean isValidInput() {
        String title = productFormBinding.productName.getText().toString();
        String description = productFormBinding.productDescription.getText().toString();
        String priceText = productFormBinding.productPrice.getText().toString();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(description) || TextUtils.isEmpty(priceText)) {
            // Handle validation errors (e.g., show a toast)
            return false;
        }

        return true;
    }
}
