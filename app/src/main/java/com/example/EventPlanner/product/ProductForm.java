package com.example.EventPlanner.product;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.EventPlanner.HomeScreen;
import com.example.EventPlanner.R;
import com.example.EventPlanner.address.Address;
import com.example.EventPlanner.databinding.ActivityProductFormBinding;
import com.example.EventPlanner.eventType.EventType;
import com.example.EventPlanner.merchandise.MerchandisePhoto;

import java.util.ArrayList;
import java.util.List;

public class ProductForm extends AppCompatActivity {
    private ActivityProductFormBinding productFormBinding;
    private ProductViewModel productViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productFormBinding = ActivityProductFormBinding.inflate(getLayoutInflater());
        setContentView(productFormBinding.getRoot());

        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);

        EdgeToEdge.enable(this);

        String formType = getIntent().getStringExtra("FORM_TYPE");
        TextView formTitle = productFormBinding.formTitle;

        // Setup form title and visibility based on form type
        if ("NEW_FORM".equals(formType)) {
            formTitle.setText(R.string.add_product);
        } else if ("EDIT_FORM".equals(formType)) {
            formTitle.setText(R.string.edit_product);
            int productId = getIntent().getIntExtra("PRODUCT_ID", -1);
            if (productId != -1) {
                Product product = productViewModel.findProductById(productId);
                Log.d("Naziv proizvoda", product.getTitle());
                if (product != null) setFields(product);
            }
        }

        // Category Spinner Setup
        String[] categoryOptions = {"Options", "Space", "Food", "Drinks", "Music", "Decorations", "Other"};
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categoryOptions);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Automatic Reservation Radio Buttons
        RadioGroup reservationGroup = productFormBinding.radioGroupReservation;
        RadioButton autoReservation = productFormBinding.radioAutomatic;

        Button addPhotosButton = findViewById(R.id.add_photos);
        addPhotosButton.setOnClickListener(v -> showPhotoOptionsDialog());

        // Submit Button Logic
        productFormBinding.submitProductButton.setOnClickListener(v -> {
            if (isValidInput()) {
                Product product = createProductFromInput();
                if (product != null) {
                     productViewModel.saveProduct(product);

                    Intent intent = new Intent(ProductForm.this, HomeScreen.class);
                    startActivity(intent);
                }
            }
        });

        // Handle insets for edge-to-edge design
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Get reference to the Spinner
        Spinner categorySpinner = findViewById(R.id.category_spinner);

// Create an ArrayAdapter for the Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category_array, android.R.layout.simple_spinner_item);

// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

// Set the adapter to the Spinner
        categorySpinner.setAdapter(adapter);

// Handle the item selection (optional)
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedCategory = parentView.getItemAtPosition(position).toString();
                Toast.makeText(ProductForm.this, "Selected Category: " + selectedCategory, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle the case when no item is selected (optional)
            }
        });

        multiSelectSpinner = findViewById(R.id.multiselect_spinner);

        // Set the adapter to the Spinner (empty or initial item)
        ArrayAdapter<String> eventTypesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{"Select Event Types"});
        eventTypesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        multiSelectSpinner.setAdapter(eventTypesAdapter);

        // Set an item click listener for the Spinner
        multiSelectSpinner.setOnTouchListener((v, event) -> {
            showMultiSelectDialog();
            return true;
        });
    }

    private void showPhotoOptionsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose photo source")
                .setItems(new String[]{"Take a Photo", "Choose from Gallery"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            // Take a photo
                            openCamera();
                        } else {
                            // Choose from gallery
                            openGallery();
                        }
                    }
                });
        builder.create().show();
    }
    private static final int CAMERA_REQUEST_CODE = 100;

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, CAMERA_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            // Handle the captured photo here
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            // You can add the photo to your photo list or display it in an ImageView
            // Example: imageView.setImageBitmap(photo);
        } else if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
            // Handle the selected image here
            // Example: imageView.setImageURI(selectedImageUri);
        }
    }
    private static final int GALLERY_REQUEST_CODE = 200;

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }
    private Spinner multiSelectSpinner;
    private String[] eventTypes = {"Concert", "Festival", "Sports", "Conference", "Workshop", "Meetup", "Exhibition"};
    private boolean[] selectedItems = new boolean[eventTypes.length];

    private void showMultiSelectDialog() {
        // Create the dialog for multi-select
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Event Types");

        // Create checkboxes dynamically for each event type
        builder.setMultiChoiceItems(eventTypes, selectedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                selectedItems[which] = isChecked;
            }
        });

        // Positive button (OK) to capture selected items
        builder.setPositiveButton("OK", (dialog, id) -> {
            StringBuilder selectedEventTypes = new StringBuilder();
            for (int i = 0; i < selectedItems.length; i++) {
                if (selectedItems[i]) {
                    selectedEventTypes.append(eventTypes[i]).append(", ");
                }
            }
            if (selectedEventTypes.length() > 0) {
                selectedEventTypes.setLength(selectedEventTypes.length() - 2); // Remove trailing comma and space
            }

            // Update the Spinner with the selected event types
            ArrayAdapter<String> adapter = new ArrayAdapter<>(ProductForm.this, android.R.layout.simple_spinner_item, new String[]{selectedEventTypes.toString()});
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            multiSelectSpinner.setAdapter(adapter);
            Toast.makeText(ProductForm.this, "Selected: " + selectedEventTypes.toString(), Toast.LENGTH_SHORT).show();
        });

        // Negative button (Cancel)
        builder.setNegativeButton("Cancel", null);

        // Show the dialog
        builder.create().show();
    }

    // Populate fields when editing a product
    private void setFields(Product product) {
        productFormBinding.title.setText(product.getTitle());
        productFormBinding.productDescription.setText(product.getDescription());
        productFormBinding.specificity.setText(product.getSpecificity());
        productFormBinding.productPrice.setText(String.valueOf(product.getPrice()));
        productFormBinding.productDiscount.setText(String.valueOf(product.getDiscount()));
        productFormBinding.city.setText(product.getAddress().getCity());
        productFormBinding.street.setText(product.getAddress().getStreet());
        productFormBinding.number.setText(product.getAddress().getNumber());
        productFormBinding.latitude.setText(String.valueOf(product.getAddress().getLatitude()));
        productFormBinding.longitude.setText(String.valueOf(product.getAddress().getLongitude()));
        productFormBinding.min.setText(String.valueOf(product.getMinDuration()));
        productFormBinding.max.setText(String.valueOf(product.getMaxDuration()));
        productFormBinding.reservationDeadline.setText(String.valueOf(product.getReservationDeadline()));
        productFormBinding.cancelationDeadline.setText(String.valueOf(product.getCancellationDeadline()));

        if (product.getAutomaticReservation()) {
            productFormBinding.radioAutomatic.setChecked(true);
        } else {
            productFormBinding.radioManual.setChecked(true);
        }
    }

    // Validate user input
    private boolean isValidInput() {
        return !TextUtils.isEmpty(productFormBinding.title.getText()) &&
                !TextUtils.isEmpty(productFormBinding.productDescription.getText()) &&
                !TextUtils.isEmpty(productFormBinding.productPrice.getText());
    }

    // Create Product object from user input
    private Product createProductFromInput() {
        try {
            String title = productFormBinding.title.getText().toString();
            String specificity = productFormBinding.specificity.getText().toString();
            String description = productFormBinding.productDescription.getText().toString();
            double price = Double.parseDouble(productFormBinding.productPrice.getText().toString());
            int discount = Integer.parseInt(productFormBinding.productDiscount.getText().toString());
            String city = productFormBinding.city.getText().toString();
            String street = productFormBinding.street.getText().toString();
            String number = productFormBinding.number.getText().toString();
            double latitude = Double.parseDouble(productFormBinding.latitude.getText().toString());
            double longitude = Double.parseDouble(productFormBinding.longitude.getText().toString());
            int reservationDeadline = Integer.parseInt(productFormBinding.reservationDeadline.getText().toString());
            int cancelationDeadline = Integer.parseInt(productFormBinding.cancelationDeadline.getText().toString());

            boolean automaticReservation = productFormBinding.radioAutomatic.isChecked();

            List<EventType> eventTypes = new ArrayList<>();
            eventTypes.add(new EventType(1, "Tip Eventa", "Opis", true, null));

            List<MerchandisePhoto> photos = new ArrayList<>();
            photos.add(new MerchandisePhoto(1, "Merc Slika"));

            Category category = new Category(1, "Funerality", "Opis", false);

            Address address = new Address(city, street, number, latitude, longitude);

            return new Product(1, title, description, specificity, price, discount, true, true, 1, 1, reservationDeadline, cancelationDeadline, automaticReservation, photos, eventTypes, address, category);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }
}
