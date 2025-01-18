package com.example.EventPlanner.activities;

import android.content.Intent;
import android.net.Uri;
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
import com.example.EventPlanner.databinding.ActivityRegisterAuScreenBinding;
import com.example.EventPlanner.model.auth.LoginRequest;
import com.example.EventPlanner.model.auth.LoginResponse;
import com.example.EventPlanner.model.common.ErrorResponseDto;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

public class RegisterAuScreen extends AppCompatActivity {

    ActivityRegisterAuScreenBinding activityRegisterAuScreenBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        activityRegisterAuScreenBinding=ActivityRegisterAuScreenBinding.inflate(getLayoutInflater());
        setContentView(activityRegisterAuScreenBinding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.register_au_screen), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();
        String inviteToken = null;
        if (appLinkData != null) {
            inviteToken = appLinkData.getQueryParameter("inviteToken");
            JwtService.setEventToken(inviteToken);
        }
        Button registerButton=activityRegisterAuScreenBinding.inviteBtn;
        EditText email=activityRegisterAuScreenBinding.editTextEmail;
        registerButton.setOnClickListener(v -> {
            Call<LoginResponse> call1 = ClientUtils.authService.fastRegister(email.getText().toString());
            call1.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        JwtService.setTokens(response.body().getAccessToken(), response.body().getRefreshToken());
                        // Navigate to the home screen
                        Intent intent = new Intent(RegisterAuScreen.this, LoginScreen.class);
                        startActivity(intent);
                    } else {
                        try {
                            Converter<ResponseBody, ErrorResponseDto> converter = ClientUtils.retrofit.responseBodyConverter(
                                    ErrorResponseDto.class, new Annotation[0]);
                            ErrorResponseDto errorResponse = converter.convert(response.errorBody());

                            if (errorResponse != null) {
                                Toast.makeText(RegisterAuScreen.this, errorResponse.getMessage(), Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(RegisterAuScreen.this, "Unknown error occurred", Toast.LENGTH_LONG).show();
                            }
                        } catch (IOException e) {
                            Toast.makeText(RegisterAuScreen.this, "Error parsing server response", Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable throwable) {
                    // Handle network errors
                    Toast.makeText(RegisterAuScreen.this, "Error parsing server response", Toast.LENGTH_LONG).show();

                }
            });
        });
    }
}