package com.example.EventPlanner.activities;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.EventPlanner.R;
import com.example.EventPlanner.clients.ClientUtils;
import com.example.EventPlanner.clients.JwtService;
import com.example.EventPlanner.fragments.eventtype.EventTypeViewModel;
import com.example.EventPlanner.model.common.Address;
import com.example.EventPlanner.databinding.ActivityProductFormBinding;
import com.example.EventPlanner.model.event.EventTypeOverview;
import com.example.EventPlanner.model.merchandise.CategoryOverview;
import com.example.EventPlanner.model.merchandise.product.CreateProductRequest;
import com.example.EventPlanner.fragments.merchandise.product.ProductViewModel;
import com.example.EventPlanner.model.merchandise.product.ProductOverview;
import com.example.EventPlanner.model.merchandise.product.UpdateProductRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductForm extends AppCompatActivity {
    private ActivityProductFormBinding productFormBinding;
    private ProductViewModel productViewModel;
    private List<EventTypeOverview> eventTypesList = new ArrayList<>();
    private boolean[] selectedItems;
    private String[] eventTypeNames;
    private Spinner categorySpinner;

    private static final int REQUEST_CODE_SELECT_PHOTOS = 1;
    private List<Integer> selectedPhotos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productFormBinding = ActivityProductFormBinding.inflate(getLayoutInflater());
        setContentView(productFormBinding.getRoot());

        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);

        EdgeToEdge.enable(this);

        String formType = getIntent().getStringExtra("FORM_TYPE");
        TextView formTitle = productFormBinding.formTitle;

        EventTypeViewModel eventTypeViewModel = new ViewModelProvider(this).get(EventTypeViewModel.class);


        Spinner categorySpinner = productFormBinding.categorySpinner;

        Call<List<CategoryOverview>> call1 = ClientUtils.categoryService.getApproved();
        call1.enqueue(new Callback<List<CategoryOverview>>() {
            @Override
            public void onResponse(Call<List<CategoryOverview>> call, Response<List<CategoryOverview>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ArrayAdapter<CategoryOverview> adapter = new ArrayAdapter<>(
                            ProductForm.this,
                            android.R.layout.simple_spinner_item,
                            response.body()
                    );
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    categorySpinner.setAdapter(adapter);
                } else {
                    // Handle error cases
                    Log.e("Category Getting Error", "Response not successful: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<CategoryOverview>> call, Throwable throwable) {
                // Handle network errors
                Log.e("Category Getting Failure", "Error: " + throwable.getMessage());
            }
        });


        // Multi-select spinner setup
        multiSelectSpinner = findViewById(R.id.multiselect_spinner);
        fetchEventTypesAndSetupSpinner();

        // Setup form title and visibility based on form type
        if ("NEW_FORM".equals(formType)) {
            formTitle.setText(R.string.add_product);
            productFormBinding.categorySpinner.setEnabled(true);
        } else if ("EDIT_FORM".equals(formType)) {
            formTitle.setText(R.string.edit_product);
            productFormBinding.categorySpinner.setEnabled(false);
            int productId = getIntent().getIntExtra("PRODUCT_ID", -1);
            if (productId != -1) {
                productViewModel.getSelectedProduct().observe(this, product -> {
                    if (product != null) {
                        setFields(product);
                    }
                });
                productViewModel.findProductById(productId);
            }
        }


        // Automatic Reservation Radio Buttons
        RadioGroup reservationGroup = productFormBinding.radioGroupReservation;
        RadioButton autoReservation = productFormBinding.radioAutomatic;

        Button addPhotosButton = findViewById(R.id.add_photos);


        productFormBinding.submitProductButton.setOnClickListener(v -> {
            if (isValidInput()) {
                CreateProductRequest product = createProductFromInput();
                if (product != null) {
                    if ("NEW_FORM".equals(formType)) {
                        productViewModel.saveProduct(product);
                    } else if ("EDIT_FORM".equals(formType)) {
                        // If it's an edit form, you can update the event type
                        int productId = getIntent().getIntExtra("PRODUCT_ID", -1);
                        productViewModel.updateProduct(productId, new UpdateProductRequest(product.getTitle(), product.getDescription(),
                                product.getSpecificity(), product.getPrice(), product.getDiscount(), product.getVisible(), product.getAvailable(),
                                product.getMinDuration(), product.getMaxDuration(), product.getReservationDeadline(), product.getCancellationDeadline(),
                                product.getAutomaticReservation(), product.getServiceProviderId(), product.getMerchandisePhotos(), product.getEventTypesIds(), product.getAddress()));
                    }

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

    }

    // Fetch categories and populate the multi-select spinner
    private void fetchEventTypesAndSetupSpinner() {
        Call<List<EventTypeOverview>> call = ClientUtils.eventTypeService.getAllWithoutPagination();
        call.enqueue(new Callback<List<EventTypeOverview>>() {
            @Override
            public void onResponse(Call<List<EventTypeOverview>> call, Response<List<EventTypeOverview>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    eventTypesList = response.body();
                    eventTypeNames = eventTypesList.stream().map(EventTypeOverview::getTitle).toArray(String[]::new);
                    selectedItems = new boolean[eventTypesList.size()];
                    setupMultiSelectSpinner();
                } else {
                    Log.e("EventTypeForm", "Failed to fetch categories: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<EventTypeOverview>> call, Throwable t) {
                Log.e("EventTypeForm", "Error fetching categories", t);
            }
        });
    }

    // Setup the multi-select spinner
    private void setupMultiSelectSpinner() {
        multiSelectSpinner.setOnTouchListener((v, event) -> {
            showMultiSelectDialog();
            return true;
        });

        // Initial adapter to display a placeholder
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{"Select Event Types"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        multiSelectSpinner.setAdapter(adapter);
    }

    // Show the multi-select dialog
    private void showMultiSelectDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Event Types");

        builder.setMultiChoiceItems(eventTypeNames, selectedItems, (dialog, which, isChecked) -> {
            selectedItems[which] = isChecked;
        });

        builder.setPositiveButton("OK", (dialog, id) -> {
            StringBuilder selectedEventTypes = new StringBuilder();
            for (int i = 0; i < selectedItems.length; i++) {
                if (selectedItems[i]) {
                    selectedEventTypes.append(eventTypeNames[i]).append(", ");
                }
            }
            if (selectedEventTypes.length() > 0) {
                selectedEventTypes.setLength(selectedEventTypes.length() - 2); // Remove trailing comma and space
            }

            // Update the spinner with the selected categories
            ArrayAdapter<String> adapter = new ArrayAdapter<>(ProductForm.this, android.R.layout.simple_spinner_item, new String[]{selectedEventTypes.toString()});
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            multiSelectSpinner.setAdapter(adapter);
        });

        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private Spinner multiSelectSpinner;
    // Populate fields when editing a product
    private void setFields(ProductOverview product) {
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
        productFormBinding.radioAutomatic.setChecked(product.getAutomaticReservation());

        // Set category spinner to the correct value
        categorySpinner = productFormBinding.categorySpinner;
        ArrayAdapter<CategoryOverview> categoryAdapter = (ArrayAdapter<CategoryOverview>) categorySpinner.getAdapter();
        if (categoryAdapter != null) {
            for (int i = 0; i < categoryAdapter.getCount(); i++) {
                if (categoryAdapter.getItem(i).getId() == product.getCategory().getId()) {
                    categorySpinner.setSelection(i);
                    break;
                }
            }
        }

        // Set event types multi-select spinner
        if (eventTypesList != null && !eventTypesList.isEmpty()) {
            StringBuilder selectedEventTypes = new StringBuilder();
            for (EventTypeOverview eventType : product.getEventTypes()) {
                for (int i = 0; i < eventTypesList.size(); i++) {
                    if (eventTypesList.get(i).getId() == eventType.getId()) {
                        selectedItems[i] = true;
                        selectedEventTypes.append(eventTypesList.get(i).getTitle()).append(", ");
                    }
                }
            }
            if (selectedEventTypes.length() > 0) {
                selectedEventTypes.setLength(selectedEventTypes.length() - 2); // Remove trailing comma and space
            }
            ArrayAdapter<String> eventTypesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{selectedEventTypes.toString()});
            eventTypesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            multiSelectSpinner.setAdapter(eventTypesAdapter);
        }
    }

    // Validate user input
    private boolean isValidInput() {
        return !TextUtils.isEmpty(productFormBinding.title.getText()) &&
                !TextUtils.isEmpty(productFormBinding.productDescription.getText()) &&
                !TextUtils.isEmpty(productFormBinding.productPrice.getText());
    }

    // Create Product object from user input
    private CreateProductRequest createProductFromInput() {
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
            int minDuration = Integer.parseInt(productFormBinding.min.getText().toString());
            int maxDuration = Integer.parseInt(productFormBinding.max.getText().toString());
            int reservationDeadline = Integer.parseInt(productFormBinding.reservationDeadline.getText().toString());
            int cancelationDeadline = Integer.parseInt(productFormBinding.cancelationDeadline.getText().toString());

            boolean automaticReservation = productFormBinding.radioAutomatic.isChecked();

            CategoryOverview selectedCategory = (CategoryOverview) productFormBinding.categorySpinner.getSelectedItem();

            List<Integer> eventTypeIds = new ArrayList<>();
            for (int i = 0; i < selectedItems.length; i++) {
                if (selectedItems[i]) {
                    eventTypeIds.add(eventTypesList.get(i).getId());
                }
            }

            List<Integer> photos = new ArrayList<>();

            Address address = new Address(city, street, number, latitude, longitude);

            return new CreateProductRequest(title, description, specificity, price, discount, true, true, minDuration, maxDuration, reservationDeadline, cancelationDeadline, automaticReservation, JwtService.getIdFromToken(), photos, eventTypeIds, address, selectedCategory.getId());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }
}
