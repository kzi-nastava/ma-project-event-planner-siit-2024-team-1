package com.example.EventPlanner.activities;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.EventPlanner.model.auth.LoginResponse;
import com.example.EventPlanner.model.common.ErrorResponseDto;
import com.example.EventPlanner.model.user.ChangePasswordRequest;
import com.example.EventPlanner.model.user.ChangePasswordResponse;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

public class ChangePasswordScreen extends AppCompatActivity {

    EditText old;
    EditText new1;
    EditText new2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_password_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.change_password_screen), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        old = findViewById(R.id.editTextOld);
        new1 = findViewById(R.id.editTextNew1);
        new2 = findViewById(R.id.editTextNew2);

        Button submit = (Button) findViewById(R.id.invite_btn);
        submit.setOnClickListener(v -> {
            ChangePasswordRequest dto = new ChangePasswordRequest(old.getText().toString(), new1.getText().toString(), new2.getText().toString());

            Call<ChangePasswordResponse> call1 = ClientUtils.userService.changePassword(JwtService.getIdFromToken(), dto);
            call1.enqueue(new Callback<ChangePasswordResponse>() {
                @Override
                public void onResponse(Call<ChangePasswordResponse> call, Response<ChangePasswordResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(ChangePasswordScreen.this, response.body().getMessage(), Toast.LENGTH_SHORT);
                        // Navigate to the home screen
                        Intent intent = new Intent(ChangePasswordScreen.this, HomeScreen.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(ChangePasswordScreen.this, response.body().getMessage(), Toast.LENGTH_SHORT);
                    }
                }

                @Override
                public void onFailure(Call<ChangePasswordResponse> call, Throwable throwable) {
                    // Handle network errors
                    Toast.makeText(ChangePasswordScreen.this, "Network error: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });
    }
}
