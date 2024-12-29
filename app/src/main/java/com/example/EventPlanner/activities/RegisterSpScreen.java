package com.example.EventPlanner.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.example.EventPlanner.model.auth.RegisterSpRequest;
import com.example.EventPlanner.model.auth.RegisterSpResponse;
import com.example.EventPlanner.model.auth.Role;
import com.example.EventPlanner.model.common.Address;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterSpScreen extends AppCompatActivity {

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

        EditText name = findViewById(R.id.editTextName);
        EditText surname = findViewById(R.id.editTextSurname);
        EditText city = findViewById(R.id.city);
        EditText street = findViewById(R.id.street);
        EditText number = findViewById(R.id.number);
        EditText latitude = findViewById(R.id.latitude);
        EditText longitude = findViewById(R.id.longitude);
        EditText phone = findViewById(R.id.editTextPhone);
        EditText email = findViewById(R.id.editTextEmail);
        EditText password1 = findViewById(R.id.editTextPassword);
        EditText password2 = findViewById(R.id.editTextConfirmPassword);
        EditText company = findViewById(R.id.editTextCompany);
        EditText description = findViewById(R.id.editTextDescription);

        String photo = "";
        List<Integer> photos = new ArrayList<>();
        photos.add(1);

        Button submitButton = (Button) findViewById(R.id.invite_btn);

        submitButton.setOnClickListener(v -> {
            if(!password1.getText().toString().equals(password2.getText().toString())){
                Toast.makeText(this, "Password do not match", Toast.LENGTH_SHORT).show();
            }

            RegisterSpRequest dto = new RegisterSpRequest();
            dto.setName(name.getText().toString());
            dto.setSurname(surname.getText().toString());
            dto.setAddress(new Address(street.getText().toString(), city.getText().toString(), number.getText().toString(), Double.parseDouble(longitude.getText().toString()), Double.parseDouble(latitude.getText().toString())));
            dto.setPhoneNumber(phone.getText().toString());
            dto.setEmail(email.getText().toString());
            dto.setPassword(password1.getText().toString());
            dto.setPhoto(photo);
            dto.setRole(Role.SP);
            dto.setCompany(company.getText().toString());
            dto.setDescription(company.getText().toString());
            dto.setPhotos(photos);

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
}