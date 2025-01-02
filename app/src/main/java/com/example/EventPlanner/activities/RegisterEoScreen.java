package com.example.EventPlanner.activities;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.EventPlanner.R;
import com.example.EventPlanner.clients.ClientUtils;
import com.example.EventPlanner.clients.JwtService;
import com.example.EventPlanner.model.auth.RegisterEoRequest;
import com.example.EventPlanner.model.auth.RegisterEoResponse;
import com.example.EventPlanner.model.auth.Role;
import com.example.EventPlanner.model.common.Address;
import com.example.EventPlanner.model.event.EventTypeOverview;
import com.example.EventPlanner.model.merchandise.CategoryOverview;
import com.example.EventPlanner.model.user.GetEoById;
import com.example.EventPlanner.model.user.UpdateEoRequest;
import com.example.EventPlanner.model.user.UpdateEoResponse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterEoScreen extends AppCompatActivity {

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
    Button changePassword;

    private String selectedProfilePhoto = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_eo_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.register_eo_screen), (v, insets) -> {
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
        changePassword = (Button) findViewById(R.id.change_password);

        String photo = "";

        Button profilePhotoButton = findViewById(R.id.choosePhottoButton);
        profilePhotoButton.setOnClickListener(v -> openGallery(false));

        String formType = getIntent().getStringExtra("FORM_TYPE");
// Setup form title and visibility based on form type
        if ("NEW_FORM".equals(formType)) {
            email.setEnabled(true);
            password1.setVisibility(View.VISIBLE);
            password2.setVisibility(View.VISIBLE);
            changePassword.setVisibility(View.GONE);

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
                            Log.e("GetAuById Error", "Response not successful: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<GetEoById> call, Throwable throwable) {
                        // Handle network errors
                        Log.e("GetAuById GetAuById", "Error: " + throwable.getMessage());
                    }
                });
            }
        } else if ("EDIT_FORM".equals(formType)) {
            email.setEnabled(false);
            password1.setVisibility(View.GONE);
            password2.setVisibility(View.GONE);
            changePassword.setVisibility(View.VISIBLE);
            int userId = getIntent().getIntExtra("USER_ID", -1);
            if (userId != -1) {
                Call<GetEoById> call1 = ClientUtils.userService.getEoById(userId);
                call1.enqueue(new Callback<GetEoById>() {
                    @Override
                    public void onResponse(Call<GetEoById> call, Response<GetEoById> response) {
                        if (response.isSuccessful() && response.body() != null) {
                                setFields(response.body());
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
        }

        Button submitButton = (Button) findViewById(R.id.invite_btn);

        submitButton.setOnClickListener(v -> {
            if(!password1.getText().toString().equals(password2.getText().toString())){
                Toast.makeText(this, "Password do not match", Toast.LENGTH_SHORT).show();
            }

            if("NEW_FORM".equals(formType)){
                RegisterEoRequest dto = new RegisterEoRequest();
                dto.setName(name.getText().toString());
                dto.setSurname(surname.getText().toString());
                dto.setAddress(new Address(street.getText().toString(), city.getText().toString(), number.getText().toString(), Double.parseDouble(longitude.getText().toString()), Double.parseDouble(latitude.getText().toString())));
                dto.setPhoneNumber(phone.getText().toString());
                dto.setEmail(email.getText().toString());
                dto.setPassword(password1.getText().toString());
                dto.setPhoto(selectedProfilePhoto);
                dto.setRole(Role.EO);

                boolean promotion = JwtService.getRoleFromToken().equals("AU") ? true : false;
                Call<RegisterEoResponse> call1 = ClientUtils.authService.registerEo(dto, promotion);
                call1.enqueue(new Callback<RegisterEoResponse>() {
                    @Override
                    public void onResponse(Call<RegisterEoResponse> call, Response<RegisterEoResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            JwtService.setTokens(response.body().getAccessToken(), response.body().getRefreshToken());

                            // Navigate to the home screen
                            Intent intent = new Intent(RegisterEoScreen.this, HomeScreen.class);
                            startActivity(intent);
                        } else {
                            // Handle error cases
                            Log.e("Register Error", "Response not successful: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<RegisterEoResponse> call, Throwable throwable) {
                        // Handle network errors
                        Log.e("Registration Failure", "Error: " + throwable.getMessage());
                    }
                });
            }else if("EDIT_FORM".equals(formType)){
                UpdateEoRequest dto = new UpdateEoRequest();
                dto.setName(name.getText().toString());
                dto.setSurname(surname.getText().toString());
                dto.setAddress(new Address(street.getText().toString(), city.getText().toString(), number.getText().toString(), Double.parseDouble(longitude.getText().toString()), Double.parseDouble(latitude.getText().toString())));
                dto.setPhoneNumber(phone.getText().toString());
                dto.setPassword(password1.getText().toString());
                dto.setPhoto(selectedProfilePhoto);
                dto.setRole(Role.EO);

                Call<UpdateEoResponse> call1 = ClientUtils.userService.updateEo(getIntent().getIntExtra("USER_ID", -1), dto);
                call1.enqueue(new Callback<UpdateEoResponse>() {
                    @Override
                    public void onResponse(Call<UpdateEoResponse> call, Response<UpdateEoResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            // Navigate to the home screen
                            Intent intent = new Intent(RegisterEoScreen.this, HomeScreen.class);
                            startActivity(intent);
                        } else {
                            // Handle error cases
                            Log.e("Register Error", "Response not successful: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<UpdateEoResponse> call, Throwable throwable) {
                        // Handle network errors
                        Log.e("Registration Failure", "Error: " + throwable.getMessage());
                    }
                });
            }
        });

        Button passwordBtn = (Button) findViewById(R.id.change_password);
        passwordBtn.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterEoScreen.this, ChangePasswordScreen.class);
            startActivity(intent);
        });
    }

    private void openGallery(boolean multiple) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, multiple);
        startActivityForResult(intent, multiple ? 1 : 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == RESULT_OK) {
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
    private void setFields(GetEoById user) {
        name.setText(user.getName());
        surname.setText(user.getSurname());
        city.setText(user.getAddress().getCity());
        street.setText(user.getAddress().getStreet());
        number.setText(user.getAddress().getNumber());
        latitude.setText(user.getAddress().getLatitude().toString());
        longitude.setText(user.getAddress().getLongitude().toString());
        phone.setText(user.getPhoneNumber());
        email.setText(user.getEmail());
        selectedProfilePhoto = user.getPhoto();
    }
}