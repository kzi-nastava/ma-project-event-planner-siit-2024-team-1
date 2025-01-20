package com.example.EventPlanner.activities;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.EventPlanner.R;
import com.example.EventPlanner.adapters.merchandise.BusinessPhotoAdapter;
import com.example.EventPlanner.clients.ClientUtils;
import com.example.EventPlanner.clients.JwtService;
import com.example.EventPlanner.databinding.ActivityRegisterSpScreenBinding;
import com.example.EventPlanner.fragments.common.map.MapFragment;
import com.example.EventPlanner.model.auth.RegisterSpRequest;
import com.example.EventPlanner.model.auth.RegisterSpResponse;
import com.example.EventPlanner.model.auth.Role;
import com.example.EventPlanner.model.common.Address;
import com.example.EventPlanner.model.user.BusinessPhoto;
import com.example.EventPlanner.model.user.GetEoById;
import com.example.EventPlanner.model.user.GetSpById;
import com.example.EventPlanner.model.user.UpdateSpRequest;
import com.example.EventPlanner.model.user.UpdateSpResponse;

import java.io.File;
import java.io.FileNotFoundException;
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

public class RegisterSpScreen extends AppCompatActivity {

    EditText name;
    EditText surname;
    EditText city;
    EditText street;
    EditText number;
    EditText latitude;
    EditText longitude;
    EditText phone;
    EditText email;
    EditText password1;
    EditText password2;

    EditText company;
    EditText description;

    Button changePassword;
    TextView photoName;
    private ActivityRegisterSpScreenBinding activityRegisterSpScreenBinding;


    private List<String> selectedPhotos = new ArrayList<>();
    private List<Integer> selectedPhotoIds = new ArrayList<>();
    private RecyclerView recyclerViewSelectedPhotos;
    private BusinessPhotoAdapter photosAdapter;

    private String selectedProfilePhoto = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        activityRegisterSpScreenBinding=ActivityRegisterSpScreenBinding.inflate(getLayoutInflater());
        setContentView(activityRegisterSpScreenBinding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.register_sp_screen), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        name = findViewById(R.id.editTextName);
        surname = findViewById(R.id.editTextSurname);
        city = findViewById(R.id.city);
        street = findViewById(R.id.street);
        number = findViewById(R.id.number);
        latitude = findViewById(R.id.latitude);
        longitude = findViewById(R.id.longitude);
        phone = findViewById(R.id.editTextPhone);
        email = findViewById(R.id.editTextEmail);
        password1 = findViewById(R.id.editTextPassword);
        password2 = findViewById(R.id.editTextConfirmPassword);
        company = findViewById(R.id.editTextCompany);
        description = findViewById(R.id.editTextDescription);
        changePassword = (Button) findViewById(R.id.change_password);
        photoName = findViewById(R.id.photoName);


        String photo = "";

        Button addPhotosButton = findViewById(R.id.choosePhotosButton);
        addPhotosButton.setOnClickListener(v -> openPhotoSelectionDialog());

        Button profilePhotoButton = findViewById(R.id.choosePhottoButton);
        profilePhotoButton.setOnClickListener(v -> openGallery(false));

        String formType = getIntent().getStringExtra("FORM_TYPE");
// Setup form title and visibility based on form type
        if ("NEW_FORM".equals(formType)) {
            email.setEnabled(true);
            password1.setVisibility(View.VISIBLE);
            password2.setVisibility(View.VISIBLE);
            changePassword.setVisibility(View.GONE);
            company.setEnabled(true);

            int userId = getIntent().getIntExtra("USER_ID", -1);
            if(JwtService.getRoleFromToken().equals("AU") && userId != -1){
                Call<GetEoById> call1 = ClientUtils.userService.getAuById(userId);
                call1.enqueue(new Callback<GetEoById>() {
                    @Override
                    public void onResponse(Call<GetEoById> call, Response<GetEoById> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            email.setText(response.body().getEmail());
                            email.setEnabled(false);
                        } else {
                            // Handle error cases
                            Log.e("GetEoById Error", "Response not successful: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<GetEoById> call, Throwable throwable) {
                        // Handle network errors
                        Log.e("GetEoById Failure", "Error: " + throwable.getMessage());
                    }
                });
            }
        } else if ("EDIT_FORM".equals(formType)) {
            email.setEnabled(false);
            password1.setVisibility(View.GONE);
            password2.setVisibility(View.GONE);
            changePassword.setVisibility(View.VISIBLE);
            company.setEnabled(false);
            int userId = getIntent().getIntExtra("USER_ID", -1);
            if (userId != -1) {
                Call<GetSpById> call1 = ClientUtils.userService.getSpById(userId);
                call1.enqueue(new Callback<GetSpById>() {
                    @Override
                    public void onResponse(Call<GetSpById> call, Response<GetSpById> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            setFields(response.body());
                        } else {
                            // Handle error cases
                            Log.e("GetEoById Error", "Response not successful: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<GetSpById> call, Throwable throwable) {
                        // Handle network errors
                        Log.e("GetEoById Failure", "Error: " + throwable.getMessage());
                    }
                });
            }
        }

        Button submitButton = (Button) findViewById(R.id.invite_btn);

        submitButton.setOnClickListener(v -> {
            if(!password1.getText().toString().equals(password2.getText().toString())){
                Toast.makeText(this, "Password do not match", Toast.LENGTH_SHORT).show();
            }

            if("NEW_FORM".equals(formType)){
                RegisterSpRequest dto = new RegisterSpRequest();
                dto.setName(name.getText().toString());
                dto.setSurname(surname.getText().toString());
                dto.setAddress(new Address(street.getText().toString(), city.getText().toString(), number.getText().toString(), Double.parseDouble(longitude.getText().toString()), Double.parseDouble(latitude.getText().toString())));
                dto.setPhoneNumber(phone.getText().toString());
                dto.setEmail(email.getText().toString());
                dto.setPassword(password1.getText().toString());
                dto.setPhoto(selectedProfilePhoto);
                dto.setRole(Role.SP);

                dto.setCompany(company.getText().toString());
                dto.setDescription(company.getText().toString());
                dto.setPhotos(selectedPhotoIds);

                boolean promotion = JwtService.getRoleFromToken().equals("AU") ? true : false;
                Call<RegisterSpResponse> call1 = ClientUtils.authService.registerSp(dto, promotion);
                call1.enqueue(new Callback<RegisterSpResponse>() {
                    @Override
                    public void onResponse(Call<RegisterSpResponse> call, Response<RegisterSpResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            JwtService.setTokens(response.body().getAccessToken(), response.body().getRefreshToken());

                            // Navigate to the home screen
                            Intent intent = new Intent(RegisterSpScreen.this, LoginScreen.class);
                            startActivity(intent);
                        } else {
                            // Handle error cases
                            Log.e("Register Error", "Response not successful: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<RegisterSpResponse> call, Throwable throwable) {
                        // Handle network errors
                        Log.e("Registration Failure", "Error: " + throwable.getMessage());
                    }
                });
            }else if("EDIT_FORM".equals(formType)){
                UpdateSpRequest dto = new UpdateSpRequest();
                dto.setName(name.getText().toString());
                dto.setSurname(surname.getText().toString());
                dto.setAddress(new Address(street.getText().toString(), city.getText().toString(), number.getText().toString(), Double.parseDouble(longitude.getText().toString()), Double.parseDouble(latitude.getText().toString())));
                dto.setPhoneNumber(phone.getText().toString());
                dto.setPassword(password1.getText().toString());
                dto.setPhoto(selectedProfilePhoto);
                dto.setRole(Role.EO);

                dto.setDescription(description.getText().toString());
                dto.setPhotos(selectedPhotoIds);

                Call<UpdateSpResponse> call1 = ClientUtils.userService.updateSp(getIntent().getIntExtra("USER_ID", -1), dto);
                call1.enqueue(new Callback<UpdateSpResponse>() {
                    @Override
                    public void onResponse(Call<UpdateSpResponse> call, Response<UpdateSpResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            // Navigate to the home screen
                            Intent intent = new Intent(RegisterSpScreen.this, HomeScreen.class);
                            startActivity(intent);
                        } else {
                            // Handle error cases
                            Log.e("UpdateSpResponse Error", "UpdateSpResponse not successful: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<UpdateSpResponse> call, Throwable throwable) {
                        // Handle network errors
                        Log.e("UpdateSpResponse Failure", "Error: " + throwable.getMessage());
                    }
                });
            }
        });

        Button passwordBtn = (Button) findViewById(R.id.change_password);
        passwordBtn.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterSpScreen.this, ChangePasswordScreen.class);
            startActivity(intent);
        });
        MapFragment mapFragment = MapFragment.newInstance(false, false,true);
        mapFragment.setOnMapAddressSelectedListener(new MapFragment.OnMapAddressSelectedListener() {
            @Override
            public void onAddressSelected(Address address) {
                // Handle the selected address
                activityRegisterSpScreenBinding.city.setText(address.getCity());
                activityRegisterSpScreenBinding.street.setText(address.getStreet());
                activityRegisterSpScreenBinding.number.setText(address.getNumber());
                activityRegisterSpScreenBinding.latitude.setText(address.getLatitude().toString());
                activityRegisterSpScreenBinding.longitude.setText(address.getLongitude().toString());
            }
        });
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.eventFormMapViewFragmentView,mapFragment)
                .commit();
    }

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
            Call<Integer> call = ClientUtils.photoService.uploadBusinessPhoto(body);

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
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterSpScreen.this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_sp_photos, null);
        builder.setView(dialogView);

        // Initialize the RecyclerView and Adapter
        recyclerViewSelectedPhotos = dialogView.findViewById(R.id.recyclerViewSelectedPhotos);
        photosAdapter = new BusinessPhotoAdapter(selectedPhotos, this::removeSelectedPhoto);
        recyclerViewSelectedPhotos.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSelectedPhotos.setAdapter(photosAdapter);

        // Set up Add and Remove buttons
        Button buttonAddPhoto = dialogView.findViewById(R.id.buttonAddPhoto);
        buttonAddPhoto.setOnClickListener(v -> openGallery(true));

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void openGallery(boolean multiple) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, multiple);
        startActivityForResult(intent, multiple ? 1 : 2);
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
        else if (requestCode == 2 && resultCode == RESULT_OK) {
            if (data.getClipData() != null) {
                // Multiple images selected
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(imageUri);
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    File file = new File(getCacheDir(), getFileName(imageUri));
                    selectedProfilePhoto = file.getName(); // Upload each photo
                    photoName.setText(selectedProfilePhoto.toString());

                }
            } else if (data.getData() != null) {
                // Single image selected
                Uri imageUri = data.getData();
                try {
                    InputStream inputStream = getContentResolver().openInputStream(imageUri);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
                File file = new File(getCacheDir(), getFileName(imageUri));
                selectedProfilePhoto = file.getName(); // Upload the single photo
                photoName.setText(selectedProfilePhoto.toString());

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
    // Populate fields when editing an event type
    private void setFields(GetSpById user) {
        name.setText(user.getName());
        surname.setText(user.getSurname());
        city.setText(user.getAddress().getCity());
        street.setText(user.getAddress().getStreet());
        number.setText(user.getAddress().getNumber());
        latitude.setText(user.getAddress().getLatitude().toString());
        longitude.setText(user.getAddress().getLongitude().toString());
        phone.setText(user.getPhoneNumber());
        email.setText(user.getEmail());

        photoName.setText(user.getPhoto());
        selectedProfilePhoto = user.getPhoto();

        company.setText(user.getCompany());
        description.setText(user.getDescription());

        for (BusinessPhoto photo: user.getPhotos()) {
            selectedPhotoIds.add(photo.getId());
            selectedPhotos.add(photo.getPhoto());
        }
    }

    private void removeSelectedPhoto(int position) {
        if (position >= 0 && position < selectedPhotos.size()) {
            // Get the photo ID from the list
            int photoId = selectedPhotoIds.get(position);

            int spId = getIntent().getIntExtra("USER_ID", -1);
            // Call the delete API to remove the photo from the server
            Call<Void> deleteCall = ClientUtils.photoService.deleteBusinessPhoto(photoId, spId, spId != -1); // assuming 'true' for edit flag

            deleteCall.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        // Successfully deleted, now remove it from the list
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
}