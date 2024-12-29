package com.example.EventPlanner.activities;

import static com.example.EventPlanner.clients.ClientUtils.SERVICE_API_PATH;

import android.content.Intent;
import android.media.session.MediaSession;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.EventPlanner.R;
import com.example.EventPlanner.clients.JwtService;
import com.example.EventPlanner.clients.TokenManager;
import com.example.EventPlanner.model.auth.LoginRequest;
import com.example.EventPlanner.model.auth.LoginResponse;
import com.example.EventPlanner.model.merchandise.Category1;
import com.example.EventPlanner.clients.ClientUtils;

import java.util.ArrayList;
import java.util.TooManyListenersException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login_screen), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText username = findViewById(R.id.editTextText);
        EditText password = findViewById(R.id.editTextTextPassword);

        Button loginButton=(Button) findViewById(R.id.loginButton);
        Button registerSelectionButton=(Button) findViewById(R.id.registerSelectionButton);
        Button skipLoginButton=(Button) findViewById(R.id.skipLogin);

        loginButton.setOnClickListener(v -> {

            LoginRequest dto = new LoginRequest("johndoe@gmail.com", "sifra");
            dto.setEmail(username.getText().toString());
            dto.setPassword(password.getText().toString());

            Call<LoginResponse> call1 = ClientUtils.authService.login(dto);
            call1.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        JwtService.setTokens(response.body().getAccessToken(), response.body().getRefreshToken());

                        // Navigate to the home screen
                        Intent intent = new Intent(LoginScreen.this, HomeScreen.class);
                        startActivity(intent);
                    } else {
                        // Handle error cases
                        Log.e("Login Error", "Response not successful: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable throwable) {
                    // Handle network errors
                    Log.e("Login Failure", "Error: " + throwable.getMessage());
                }
            });
        });
        skipLoginButton.setOnClickListener(v -> {
            Intent intent=new Intent(LoginScreen.this, HomeScreen.class);
            startActivity(intent);
        });

        // Set click listener
        registerSelectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRegistrationChoiceDialog();
            }
        });
    }

    private void showRegistrationChoiceDialog() {
        // Inflate the custom dialog layout
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_register_choice, null);

        // Create and show the dialog
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setView(dialogView);
        AlertDialog dialog = dialogBuilder.create();

        // Find buttons in the dialog and set listeners
        Button registerAsSP = dialogView.findViewById(R.id.register_as_sp);
        Button registerAsEO = dialogView.findViewById(R.id.register_as_eo);

        registerAsSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                navigateToRegisterAsSP();
            }
        });

        registerAsEO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                navigateToRegisterAsEO();
            }
        });

        dialog.show();
    }

    private void navigateToRegisterAsSP() {
        // Navigate to SP registration activity
         Intent intent = new Intent(this, RegisterSpScreen.class);
         intent.putExtra("FORM_TYPE", "EDIT_FORM");
         startActivity(intent);
    }

    private void navigateToRegisterAsEO() {
        // Navigate to EO registration activity
         Intent intent = new Intent(this, RegisterEoScreen.class);
         intent.putExtra("FORM_TYPE", "EDIT_FORM");
         startActivity(intent);
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