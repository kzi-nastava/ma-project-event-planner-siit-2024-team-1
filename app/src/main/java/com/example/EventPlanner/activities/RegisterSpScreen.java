package com.example.EventPlanner.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.EventPlanner.R;
import com.example.EventPlanner.clients.ClientUtils;
import com.example.EventPlanner.clients.JwtService;
import com.example.EventPlanner.model.auth.RegisterEoRequest;
import com.example.EventPlanner.model.auth.RegisterEoResponse;
import com.example.EventPlanner.model.auth.RegisterSpRequest;
import com.example.EventPlanner.model.auth.RegisterSpResponse;
import com.example.EventPlanner.model.auth.Role;
import com.example.EventPlanner.model.common.Address;
import com.example.EventPlanner.model.user.GetEoById;
import com.example.EventPlanner.model.user.GetSpById;
import com.example.EventPlanner.model.user.UpdateEoRequest;
import com.example.EventPlanner.model.user.UpdateEoResponse;
import com.example.EventPlanner.model.user.UpdateSpRequest;
import com.example.EventPlanner.model.user.UpdateSpResponse;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_sp_screen);
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

        String photo = "";

        String formType = getIntent().getStringExtra("FORM_TYPE");
// Setup form title and visibility based on form type
        if ("NEW_FORM".equals(formType)) {
            email.setEnabled(true);
            password1.setVisibility(View.VISIBLE);
            password2.setVisibility(View.VISIBLE);
            changePassword.setVisibility(View.GONE);
            company.setEnabled(true);

            int userId = getIntent().getIntExtra("USER_ID", -1);
            if(JwtService.getRoleFromToken() == "AU" && userId != -1){
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
                dto.setPhoto(photo);
                dto.setRole(Role.EO);

                dto.setCompany(company.getText().toString());
                dto.setDescription(company.getText().toString());
                dto.setPhotos(new ArrayList<Integer>());

                Call<RegisterSpResponse> call1 = ClientUtils.authService.registerSp(dto, false);
                call1.enqueue(new Callback<RegisterSpResponse>() {
                    @Override
                    public void onResponse(Call<RegisterSpResponse> call, Response<RegisterSpResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            JwtService.setTokens(response.body().getAccessToken(), response.body().getRefreshToken());

                            // Navigate to the home screen
                            Intent intent = new Intent(RegisterSpScreen.this, HomeScreen.class);
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
                dto.setPhoto(photo);
                dto.setRole(Role.EO);

                dto.setDescription(description.getText().toString());
                dto.setPhotos(new ArrayList<Integer>());

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

        company.setText(user.getCompany());
        description.setText(user.getDescription());
    }
}