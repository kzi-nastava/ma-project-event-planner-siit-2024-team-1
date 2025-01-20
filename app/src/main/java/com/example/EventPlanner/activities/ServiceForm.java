package com.example.EventPlanner.activities;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.EventPlanner.databinding.ActivityServiceFormBinding;
import com.example.EventPlanner.fragments.common.map.MapFragment;
import com.example.EventPlanner.model.event.EventTypeOverview;
import com.example.EventPlanner.model.merchandise.CategoryOverview;
import com.example.EventPlanner.model.merchandise.CreateCategoryRequest;
import com.example.EventPlanner.model.merchandise.MerchandisePhoto;
import com.example.EventPlanner.model.merchandise.service.CreateServiceRequest;
import com.example.EventPlanner.model.merchandise.service.Service;
import com.example.EventPlanner.fragments.merchandise.service.ServiceViewModel;
import com.example.EventPlanner.model.merchandise.service.ServiceOverview;
import com.example.EventPlanner.model.merchandise.service.UpdateServiceRequest;
import com.example.EventPlanner.model.common.Address;

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

public class ServiceForm extends AppCompatActivity {
    private ActivityServiceFormBinding serviceFormBinding;
    private ServiceViewModel serviceViewModel;
    private List<EventTypeOverview> eventTypesList = new ArrayList<>();
    private boolean[] selectedItems;
    private String[] eventTypeNames;
    private Spinner eventTypeSpinner;

    private List<String> selectedPhotos = new ArrayList<>();
    private List<Integer> selectedPhotoIds = new ArrayList<>();
    private RecyclerView recyclerViewSelectedPhotos;
    private MerchandisePhotoAdapter photoAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        serviceFormBinding = ActivityServiceFormBinding.inflate(getLayoutInflater());
        serviceViewModel = new ViewModelProvider(this).get(ServiceViewModel.class);

        EdgeToEdge.enable(this);
        setContentView(serviceFormBinding.getRoot());

        String formType = getIntent().getStringExtra("FORM_TYPE");
        TextView formTitle = serviceFormBinding.formTitle;

        if(formType.equals("NEW_FORM")) {
            formTitle.setText("New Service");
            serviceFormBinding.serviceVisibleCheckbox.setVisibility(View.GONE);
            serviceFormBinding.serviceAvailableCheckbox.setVisibility(View.GONE);
        }else if(formType.equals("EDIT_FORM")) {
            formTitle.setText("Edit Service");
            serviceFormBinding.serviceVisibleCheckbox.setVisibility(View.VISIBLE);
            serviceFormBinding.serviceAvailableCheckbox.setVisibility(View.VISIBLE);
            serviceFormBinding.categorySpinner.setEnabled(false);
            serviceFormBinding.deleteServiceButton.setVisibility(View.VISIBLE);

            int serviceId = getIntent().getIntExtra("SERVICE_ID", -1);
            if(serviceId != -1) {
                serviceViewModel.getSelectedService().observe(this, service -> {
                    if(service != null) {
                        setFields(service);
                    }
                });
                serviceViewModel.findById(serviceId);
            }
        }

        //adding items to category spinner
        Spinner categorySpinner = serviceFormBinding.categorySpinner;
        List<CategoryOverview> categoryOptions = new ArrayList<>();
        Call<List<CategoryOverview>> categoryCall = ClientUtils.categoryService.getApproved();
        categoryCall.enqueue(new Callback<List<CategoryOverview>>() {
            @Override
            public void onResponse(Call<List<CategoryOverview>> call, Response<List<CategoryOverview>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    categoryOptions.addAll(response.body());
                    CategoryOverview newCategory = new CategoryOverview(-1, "other", "create new category", false);
                    categoryOptions.add(newCategory);
                    ArrayAdapter<CategoryOverview> adapter = new ArrayAdapter<>(
                            ServiceForm.this, android.R.layout.simple_spinner_item, categoryOptions
                    );
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    categorySpinner.setAdapter(adapter);
                }else {
                    Log.e("Error fetching categories", "Response not successful: " + response.body());
                }
            }

            @Override
            public void onFailure(Call<List<CategoryOverview>> call, Throwable throwable) {
                Log.e("Error fetching categories", "Error: " + throwable.getMessage());
            }
        });
        //adding items to event type spinner
        eventTypeSpinner = findViewById(R.id.event_type_spinner);
        fetchEventTypesAndSetupSpinner();

        Button addPhotosButton = findViewById(R.id.add_service_photos_button);
        addPhotosButton.setOnClickListener(v -> openPhotoSelectionDialog());

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedOption = categoryOptions.get(position).getTitle();
                if (selectedOption.equals("other")) {
                    showCreateCategoryDialog();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        serviceFormBinding.submitServiceButton.setOnClickListener(v -> {
            if(isValidInput()) {
                if("NEW_FORM".equals(formType)) {
                    CreateServiceRequest service = createNewServiceRequest();
                    if(service != null) {
                        serviceViewModel.createService(service);
                    }
                } else if("EDIT_FORM".equals(formType)) {
                    int serviceId = getIntent().getIntExtra("SERVICE_ID", -1);
                    UpdateServiceRequest service = createUpdateServiceRequest();
                    if(service != null && serviceId != -1) {
                        serviceViewModel.updateService(serviceId, service);
                    }
                }

                Intent intent=new Intent(ServiceForm.this, HomeScreen.class);
                startActivity(intent);
            }
        });

        serviceFormBinding.deleteServiceButton.setOnClickListener(v -> {
            int serviceId = getIntent().getIntExtra("SERVICE_ID", -1);
            if(serviceId != -1) {
                serviceViewModel.deleteService(serviceId);
            }
            Intent intent=new Intent(ServiceForm.this, HomeScreen.class);
            startActivity(intent);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        MapFragment mapFragment = MapFragment.newInstance(false, false, true);
        mapFragment.setOnMapAddressSelectedListener(new MapFragment.OnMapAddressSelectedListener() {
            @Override
            public void onAddressSelected(Address address) {
                serviceFormBinding.city.setText(address.getCity());
                serviceFormBinding.street.setText(address.getStreet());
                serviceFormBinding.number.setText(address.getNumber());
                serviceFormBinding.longitude.setText(address.getLongitude().toString());
                serviceFormBinding.latitude.setText(address.getLatitude().toString());
            }
        });
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.eventFormMapViewFragmentView, mapFragment)
                .commit();
    }

    private boolean isValidInput() {
        CategoryOverview category = selectedCategory != null ? selectedCategory : (CategoryOverview) serviceFormBinding.categorySpinner.getSelectedItem();
        if(TextUtils.isEmpty(serviceFormBinding.title.getText()) &&
                TextUtils.isEmpty(serviceFormBinding.description.getText()) &&
                TextUtils.isEmpty(serviceFormBinding.specificity.getText()) &&
                TextUtils.isEmpty(serviceFormBinding.price.getText()) &&
                TextUtils.isEmpty(serviceFormBinding.discount.getText()) &&
                TextUtils.isEmpty(serviceFormBinding.minDuration.getText()) &&
                TextUtils.isEmpty(serviceFormBinding.reservationDeadline.getText()) &&
                TextUtils.isEmpty(serviceFormBinding.cancellationDeadline.getText())
        ) {
            return false;
        }
        else if(Double.parseDouble(serviceFormBinding.price.getText().toString()) < 0) {
            return false;
        }
        else if(Integer.parseInt(serviceFormBinding.discount.getText().toString()) < 0 || Integer.parseInt(serviceFormBinding.discount.getText().toString()) > 100) {
            return false;
        }
        else if(Integer.parseInt(serviceFormBinding.minDuration.getText().toString()) < 0) {
            return false;
        }
        else if(Integer.parseInt(serviceFormBinding.maxDuration.getText().toString()) < 0) {
            return false;
        }
        else if(Integer.parseInt(serviceFormBinding.reservationDeadline.getText().toString()) < 0) {
            return false;
        }
        else if(Integer.parseInt(serviceFormBinding.cancellationDeadline.getText().toString()) < 0) {
            return false;
        }
        else if(!TextUtils.isEmpty(serviceFormBinding.maxDuration.getText())) {
            return Integer.parseInt(serviceFormBinding.maxDuration.getText().toString()) >= Integer.parseInt(serviceFormBinding.minDuration.getText().toString());
        }
        else if(Integer.parseInt(serviceFormBinding.reservationDeadline.getText().toString()) < Integer.parseInt(serviceFormBinding.cancellationDeadline.getText().toString())) {
            return false;
        }
        else if(category.getId() == -1) {
            return false;
        }

        return true;
    }

    private CreateServiceRequest createNewServiceRequest() {
        try{
            String title = serviceFormBinding.title.getText().toString();
            String description = serviceFormBinding.description.getText().toString();
            String specificity = serviceFormBinding.specificity.getText().toString();
            double price = Double.parseDouble(serviceFormBinding.price.getText().toString());
            int discount = Integer.parseInt(serviceFormBinding.discount.getText().toString());
            int minDuration = Integer.parseInt(serviceFormBinding.minDuration.getText().toString());
            int maxDuration = Integer.parseInt(serviceFormBinding.maxDuration.getText().toString());
            int reservationDeadline = Integer.parseInt(serviceFormBinding.reservationDeadline.getText().toString());
            int cancellationDeadline = Integer.parseInt(serviceFormBinding.cancellationDeadline.getText().toString());
            boolean automaticReservation = serviceFormBinding.automaticServiceReservationTypeButton.isChecked();

            CategoryOverview category = selectedCategory != null ? selectedCategory : (CategoryOverview) serviceFormBinding.categorySpinner.getSelectedItem();

            List<Integer> eventTypeIds = new ArrayList<>();
            for(int i = 0; i < selectedItems.length; i++) {
                if(selectedItems[i]) {
                    eventTypeIds.add(eventTypesList.get(i).getId());
                }
            }

            String city = serviceFormBinding.city.getText().toString();
            String street = serviceFormBinding.street.getText().toString();
            String number = serviceFormBinding.number.getText().toString();
            double longitude = Double.parseDouble(serviceFormBinding.longitude.getText().toString());
            double latitude = Double.parseDouble(serviceFormBinding.latitude.getText().toString());
            Address address = new Address(city, street, number, longitude, latitude);

            return new CreateServiceRequest(title, description, specificity, price, discount,
                    category.getId(), eventTypeIds, minDuration, maxDuration, reservationDeadline,
                    cancellationDeadline, automaticReservation, selectedPhotoIds, JwtService.getIdFromToken(), address);
        }catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }

    private UpdateServiceRequest createUpdateServiceRequest() {
        try {
            String title = serviceFormBinding.title.getText().toString();
            String description = serviceFormBinding.description.getText().toString();
            String specificity = serviceFormBinding.specificity.getText().toString();
            double price = Double.parseDouble(serviceFormBinding.price.getText().toString());
            int discount = Integer.parseInt(serviceFormBinding.discount.getText().toString());
            int minDuration = Integer.parseInt(serviceFormBinding.minDuration.getText().toString());
            int maxDuration = Integer.parseInt(serviceFormBinding.maxDuration.getText().toString());
            int reservationDeadline = Integer.parseInt(serviceFormBinding.reservationDeadline.getText().toString());
            int cancellationDeadline = Integer.parseInt(serviceFormBinding.cancellationDeadline.getText().toString());
            boolean automaticReservation = serviceFormBinding.automaticServiceReservationTypeButton.isChecked();
            boolean visible = serviceFormBinding.serviceVisibleCheckbox.isChecked();
            boolean available = serviceFormBinding.serviceAvailableCheckbox.isChecked();

            List<Integer> eventTypeIds = new ArrayList<>();
            for(int i = 0; i < selectedItems.length; i++) {
                if(selectedItems[i]) {
                    eventTypeIds.add(eventTypesList.get(i).getId());
                }
            }

            String city = serviceFormBinding.city.getText().toString();
            String street = serviceFormBinding.street.getText().toString();
            String number = serviceFormBinding.number.getText().toString();
            double longitude = Double.parseDouble(serviceFormBinding.longitude.getText().toString());
            double latitude = Double.parseDouble(serviceFormBinding.latitude.getText().toString());
            Address address = new Address(city, street, number, longitude, latitude);

            return new UpdateServiceRequest(title, description, specificity, price, discount,
                    eventTypeIds, minDuration, maxDuration, reservationDeadline, cancellationDeadline,
                    automaticReservation, visible, available, selectedPhotoIds,
                    JwtService.getIdFromToken(), address);
        }catch(NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void fetchEventTypesAndSetupSpinner() {
        Call<List<EventTypeOverview>> eventTypeCall = ClientUtils.eventTypeService.getAllActiveWithoutPagination();
        eventTypeCall.enqueue(new Callback<List<EventTypeOverview>>() {
            @Override
            public void onResponse(Call<List<EventTypeOverview>> call, Response<List<EventTypeOverview>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    eventTypesList = response.body();
                    eventTypeNames = eventTypesList.stream().map(EventTypeOverview::getTitle).toArray(String[]::new);
                    selectedItems = new boolean[eventTypesList.size()];
                    setupMultiSelectSpinner();
                } else {
                    Log.e("Event Type Form", "Failed to fetch event types: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<EventTypeOverview>> call, Throwable throwable) {
                Log.e("Event Type Form", "Failed to fetch event types: " + throwable.getMessage());
            }
        });
    }

    private void setupMultiSelectSpinner() {
        eventTypeSpinner.setOnTouchListener((v, event) -> {
            showMultiSelectDialog();
            return true;
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[] {"Select Event Types"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eventTypeSpinner.setAdapter(adapter);
    }

    private void showMultiSelectDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Event Types");

        builder.setMultiChoiceItems(eventTypeNames, selectedItems, ((dialog, which, isChecked) -> {
            selectedItems[which] = isChecked;
        }));

        builder.setPositiveButton("OK", (dialog, id) -> {
            StringBuilder selectedEventTypes = new StringBuilder();
            for(int i = 0; i < selectedItems.length; i++) {
                if(selectedItems[i]) {
                    selectedEventTypes.append(eventTypeNames[i]).append(", ");
                }
            }
            if(selectedEventTypes.length() > 0) {
                selectedEventTypes.setLength(selectedEventTypes.length() - 2); //Removing trailing commas and space
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(ServiceForm.this,
                    android.R.layout.simple_spinner_item,
                    new String[]{selectedEventTypes.toString()});
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            eventTypeSpinner.setAdapter(adapter);
        });

        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK) {
            if(data.getClipData() != null) { //upload for multiple photos
                int count = data.getClipData().getItemCount();
                for(int i = 0; i < count; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    addPhoto(imageUri);
                }
            } else if (data.getData() != null) { //upload for single photo
                Uri imageUri = data.getData();
                addPhoto(imageUri);
            }
        }
    }

    private String getFileName(Uri uri) {
        String fileName = null;
        String[] projection = {MediaStore.Images.Media.DISPLAY_NAME };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if(cursor != null && cursor.moveToFirst()) {
            int columnIntex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
            fileName = cursor.getString(columnIntex);
            cursor.close();
        }
        return fileName;
    }

    private void addPhoto(Uri photoUri) {
        try{
            InputStream inputStream = getContentResolver().openInputStream(photoUri);
            File file = new File(getCacheDir(), getFileName(photoUri));

            FileOutputStream outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int len;
            while((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }

            inputStream.close();
            outputStream.close();

            RequestBody requestBody = RequestBody.create(MediaType.parse(getContentResolver().getType(photoUri)), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestBody);

            Call<Integer> call = ClientUtils.photoService.uploadMerchandisePhoto(body);
            call.enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    if(response.isSuccessful() && response.body() != null) {
                        Integer photoId = response.body();

                        selectedPhotos.add(file.getName());
                        selectedPhotoIds.add(photoId);
                        photoAdapter.notifyDataSetChanged();
                    }else {
                        Log.e("Upload Picture", "Failed to upload picture!");
                    }
                }

                @Override
                public void onFailure(Call<Integer> call, Throwable throwable) {
                    Log.e("Upload Picture", "Error uploading photo! " +  throwable.getMessage());
                }
            });
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openPhotoSelectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ServiceForm.this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_merc_photos, null);
        builder.setView(dialogView);

        recyclerViewSelectedPhotos = dialogView.findViewById(R.id.recyclerViewSelectedPhotos);
        photoAdapter = new MerchandisePhotoAdapter(selectedPhotos, this::removeSelectedPhoto);
        recyclerViewSelectedPhotos.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSelectedPhotos.setAdapter(photoAdapter);

        Button buttonAddPhoto = dialogView.findViewById(R.id.buttonAddPhoto);
        buttonAddPhoto.setOnClickListener(v -> openGallery());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, 1);
    }

    private void removeSelectedPhoto(int position) {
        if(position >= 0 && position < selectedPhotos.size()) {
            int photoId = selectedPhotoIds.get(position);
            int merchandiseId = getIntent().getIntExtra("SERVICE_ID", -1);

            Call<Void> deleteCall = ClientUtils.photoService.deleteMerchandisePhoto(photoId, merchandiseId, merchandiseId != -1);
            deleteCall.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if(response.isSuccessful()) {
                        selectedPhotos.remove(position);
                        selectedPhotoIds.remove(position);
                        photoAdapter.notifyItemRemoved(position);
                    }
                    else {
                        Log.e("Delete", "Failed to delete photo from server!");
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable throwable) {
                    Log.e("Delete Photo", "Error deleting photo " + throwable.getMessage());
                }
            });
        }
    }

    private void setFields(ServiceOverview service) {
        serviceFormBinding.title.setText(service.getTitle());
        serviceFormBinding.description.setText(service.getDescription());
        serviceFormBinding.specificity.setText(service.getSpecificity());
        serviceFormBinding.price.setText(String.valueOf(service.getPrice()));
        serviceFormBinding.discount.setText(String.valueOf(service.getDiscount()));
        serviceFormBinding.minDuration.setText(String.valueOf(service.getMinDuration()));
        serviceFormBinding.maxDuration.setText(String.valueOf(service.getMaxDuration()));
        serviceFormBinding.reservationDeadline.setText(String.valueOf(service.getReservationDeadline()));
        serviceFormBinding.cancellationDeadline.setText(String.valueOf(service.getCancellationDeadline()));
        serviceFormBinding.city.setText(service.getAddress().getCity());
        serviceFormBinding.street.setText(service.getAddress().getStreet());
        serviceFormBinding.number.setText(service.getAddress().getNumber());
        serviceFormBinding.longitude.setText(String.valueOf(service.getAddress().getLongitude()));
        serviceFormBinding.latitude.setText(String.valueOf(service.getAddress().getLatitude()));

        if(service.getAutomaticReservation()) {
            serviceFormBinding.automaticServiceReservationTypeButton.setChecked(true);
        }else {
            serviceFormBinding.manualServiceReservationTypeButton.setChecked(true);
        }

        if(service.getAvailable()) {
            serviceFormBinding.serviceAvailableCheckbox.setChecked(true);
        }
        if(service.getVisible()) {
            serviceFormBinding.serviceVisibleCheckbox.setChecked(true);
        }

        for(MerchandisePhoto photo: service.getMerchandisePhotos()) {
            selectedPhotoIds.add(photo.getId());
            selectedPhotos.add(photo.getPhoto());
        }

        if(eventTypesList != null && !eventTypesList.isEmpty()) {
            StringBuilder selectedEventTypes = new StringBuilder();
            for(EventTypeOverview eventType: service.getEventTypes()) {
                for(int i = 0; i < eventTypesList.size(); i++) {
                    if(eventTypesList.get(i).getId() == eventType.getId()) {
                        selectedItems[i] = true;
                        selectedEventTypes.append(eventTypesList.get(i).getTitle()).append(", ");
                    }
                }
            }
            if(selectedEventTypes.length() > 0) {
                selectedEventTypes.setLength(selectedEventTypes.length() - 2);
            }
            ArrayAdapter<String> eventTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{selectedEventTypes.toString()});
            eventTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            eventTypeSpinner.setAdapter(eventTypeAdapter);
        }
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

            Call<List<CategoryOverview>> call = ClientUtils.categoryService.create(dto);

            call.enqueue(new Callback<List<CategoryOverview>>() {
                @Override
                public void onResponse(Call<List<CategoryOverview>> call, Response<List<CategoryOverview>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        for(CategoryOverview cat: response.body()){
                            if(cat.getTitle().equals(dto.getTitle())){
                                selectedCategory = cat;
                            }
                        }
                    } else {
                        Log.e("Upload", "Failed to create category");
                    }
                }

                @Override
                public void onFailure(Call<List<CategoryOverview>> call, Throwable t) {
                    Log.e("Upload", "Error creating category", t);
                }
            });

            dialog.dismiss();
        });

        dialog.show();
    }
    private CategoryOverview selectedCategory = null;
}