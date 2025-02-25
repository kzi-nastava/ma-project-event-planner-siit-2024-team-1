package com.example.EventPlanner.activities;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.EventPlanner.adapters.merchandise.MerchandisePhotoAdapter;
import com.example.EventPlanner.clients.ClientUtils;
import com.example.EventPlanner.clients.JwtService;
import com.example.EventPlanner.fragments.common.map.MapFragment;
import com.example.EventPlanner.fragments.eventtype.EventTypeViewModel;
import com.example.EventPlanner.model.common.Address;
import com.example.EventPlanner.databinding.ActivityProductFormBinding;
import com.example.EventPlanner.model.event.EventTypeOverview;
import com.example.EventPlanner.model.merchandise.CategoryOverview;
import com.example.EventPlanner.model.merchandise.CreateCategoryRequest;
import com.example.EventPlanner.model.merchandise.MerchandisePhoto;
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

    private List<String> selectedPhotos = new ArrayList<>();
    private List<Integer> selectedPhotoIds = new ArrayList<>();
    private RecyclerView recyclerViewSelectedPhotos;
    private MerchandisePhotoAdapter photosAdapter;

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

        Button recommendCategoryButton = findViewById(R.id.recommend_category);
        recommendCategoryButton.setOnClickListener(v -> showCreateCategoryDialog());


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
            recommendCategoryButton.setVisibility(View.VISIBLE);
            categorySpinner.setEnabled(true);
        } else if ("EDIT_FORM".equals(formType)) {
            formTitle.setText(R.string.edit_product);
            productFormBinding.categorySpinner.setEnabled(false);
            recommendCategoryButton.setVisibility(View.GONE);
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
        addPhotosButton.setOnClickListener(v -> openPhotoSelectionDialog());

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
        MapFragment mapFragment = MapFragment.newInstance(false, false,true);
        mapFragment.setOnMapAddressSelectedListener(new MapFragment.OnMapAddressSelectedListener() {
            @Override
            public void onAddressSelected(Address address) {
                // Handle the selected address
                productFormBinding.city.setText(address.getCity());
                productFormBinding.street.setText(address.getStreet());
                productFormBinding.number.setText(address.getNumber());
                productFormBinding.latitude.setText(address.getLatitude().toString());
                productFormBinding.longitude.setText(address.getLongitude().toString());
            }
        });
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.eventFormMapViewFragmentView,mapFragment)
                .commit();

    }

    private void showCreateCategoryDialog() {
        // Inflate the dialog layout
        View dialogView = LayoutInflater.from(this).inflate(R.layout.recommend_category_dialog, null);

        // Initialize dialog components
        EditText categoryTitle = dialogView.findViewById(R.id.category_title);
        EditText categoryDescription = dialogView.findViewById(R.id.category_description);
        Button submitCategoryButton = dialogView.findViewById(R.id.submit_category);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setTitle("Create New Category")
                .setCancelable(true)
                .create();

        submitCategoryButton.setOnClickListener(v -> {
            String title = categoryTitle.getText().toString().trim();
            String description = categoryDescription.getText().toString().trim();
            boolean pending = true;

            if (title.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "Title and description are required!", Toast.LENGTH_SHORT).show();
                return;
            }

            CreateCategoryRequest dto = new CreateCategoryRequest(title, description, pending);

            Call<CategoryOverview> call = ClientUtils.categoryService.create(dto);

            call.enqueue(new Callback<CategoryOverview>() {
                @Override
                public void onResponse(Call<CategoryOverview> call, Response<CategoryOverview> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        selectedCategory = response.body();
                    } else {
                        Log.e("Upload", "Failed to create category");
                    }
                }

                @Override
                public void onFailure(Call<CategoryOverview> call, Throwable t) {
                    Log.e("Upload", "Error creating category", t);
                }
            });

            dialog.dismiss();
        });

        dialog.show();
    }
    private CategoryOverview selectedCategory = null;
    private void addPhoto(Uri photoUri) {
        // Get the content resolver and create a RequestBody for the photo
        try {
            InputStream inputStream = getContentResolver().openInputStream(photoUri);
            File file = new File(getCacheDir(), getFileName(photoUri));

            FileOutputStream outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }

            inputStream.close();
            outputStream.close();

            // Create a RequestBody to send to the server
            RequestBody requestBody = RequestBody.create(MediaType.parse(getContentResolver().getType(photoUri)), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestBody);

            // Create the Retrofit instance and make the call
            Call<Integer> call = ClientUtils.photoService.uploadMerchandisePhoto(body);

            call.enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Integer photoId = response.body();
                        Log.d("Upload", "Photo uploaded successfully with ID: " + photoId);

                        // Add the photo ID to the list of selected photos
                        selectedPhotos.add(file.getName());
                        selectedPhotoIds.add(photoId);
                        photosAdapter.notifyDataSetChanged();
                    } else {
                        Log.e("Upload", "Failed to upload photo");
                    }
                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {
                    Log.e("Upload", "Error uploading photo", t);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void openPhotoSelectionDialog() {
        // Create the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(ProductForm.this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_merc_photos, null);
        builder.setView(dialogView);

        // Initialize the RecyclerView and Adapter
        recyclerViewSelectedPhotos = dialogView.findViewById(R.id.recyclerViewSelectedPhotos);
        photosAdapter = new MerchandisePhotoAdapter(selectedPhotos, this::removeSelectedPhoto);
        recyclerViewSelectedPhotos.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSelectedPhotos.setAdapter(photosAdapter);

        // Set up Add and Remove buttons
        Button buttonAddPhoto = dialogView.findViewById(R.id.buttonAddPhoto);
        buttonAddPhoto.setOnClickListener(v -> openGallery());

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, 1);
    }

    // Fetch categories and populate the multi-select spinner
    private void fetchEventTypesAndSetupSpinner() {
        Call<List<EventTypeOverview>> call = ClientUtils.eventTypeService.getAllActiveWithoutPagination();
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

        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (data.getClipData() != null) {
                // Multiple images selected
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    addPhoto(imageUri); // Upload each photo
                }
            } else if (data.getData() != null) {
                // Single image selected
                Uri imageUri = data.getData();
                addPhoto(imageUri); // Upload the single photo
            }
        }
//        if (requestCode == 1 && resultCode == RESULT_OK) {
//            if (data != null) {
//                if (data.getClipData() != null) {
//                    // Multiple images selected
//                    int count = data.getClipData().getItemCount();
//                    for (int i = 0; i < count; i++) {
//                        Uri imageUri = data.getClipData().getItemAt(i).getUri();
//                        String fileName = getFileName(imageUri);
//                        selectedPhotos.add(fileName);
//                    }
//                } else if (data.getData() != null) {
//                    // Single image selected
//                    Uri imageUri = data.getData();
//                    String fileName = getFileName(imageUri);
//                    selectedPhotos.add(fileName);
//                }
//                photosAdapter.notifyDataSetChanged();
//            }
//        }
    }

    private String getFileName(Uri uri) {
        String fileName = null;
        String[] projection = { MediaStore.Images.Media.DISPLAY_NAME };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
            fileName = cursor.getString(columnIndex);
            cursor.close();
        }
        return fileName;
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

        for (MerchandisePhoto photo: product.getMerchandisePhotos()) {
            selectedPhotoIds.add(photo.getId());
            selectedPhotos.add(photo.getPhoto());
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

    private void removeSelectedPhoto(int position) {
        if (position >= 0 && position < selectedPhotos.size()) {
            // Get the photo ID from the list
            int photoId = selectedPhotoIds.get(position);

            // Assuming you pass the merchandise ID via Intent
            int mercId = getIntent().getIntExtra("PRODUCT_ID", -1);

            // Call the delete API
            Call<Void> deleteCall = ClientUtils.photoService.deleteMerchandisePhoto(photoId, mercId, mercId != -1);

            deleteCall.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        // Successfully deleted, remove from list
                        selectedPhotos.remove(position);
                        selectedPhotoIds.remove(position);
                        photosAdapter.notifyItemRemoved(position);
                        Log.d("Delete", "Photo deleted successfully from server");
                    } else {
                        Log.e("Delete", "Failed to delete photo from server");
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e("Delete", "Error deleting photo", t);
                }
            });
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

            CategoryOverview category = selectedCategory != null ? selectedCategory : (CategoryOverview) productFormBinding.categorySpinner.getSelectedItem();

            List<Integer> eventTypeIds = new ArrayList<>();
            for (int i = 0; i < selectedItems.length; i++) {
                if (selectedItems[i]) {
                    eventTypeIds.add(eventTypesList.get(i).getId());
                }
            }

            Address address = new Address(city, street, number, latitude, longitude);

            return new CreateProductRequest(title, description, specificity, price, discount, true, true, minDuration, maxDuration, reservationDeadline, cancelationDeadline, automaticReservation, JwtService.getIdFromToken(), selectedPhotoIds, eventTypeIds, address, category.getId());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }
}
