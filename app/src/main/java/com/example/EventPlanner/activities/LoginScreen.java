package com.example.EventPlanner.activities;

import static com.example.EventPlanner.clients.ClientUtils.SERVICE_API_PATH;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.EventPlanner.R;
import com.example.EventPlanner.model.merchandise.Category1;
import com.example.EventPlanner.clients.ClientUtils;

import java.util.ArrayList;

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
        Button loginButton=(Button) findViewById(R.id.loginButton);
        Button registerSelectionButton=(Button) findViewById(R.id.registerSelectionButton);
        Button skipLoginButton=(Button) findViewById(R.id.skipLogin);

        loginButton.setOnClickListener(v -> {
            Intent intent=new Intent(LoginScreen.this,HomeScreen.class);
            startActivity(intent);
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

        Call<ArrayList<Category1>> call = ClientUtils.category1Service.getAll();
        call.enqueue(new Callback<ArrayList<Category1>>() {
            @Override
            public void onResponse(Call<ArrayList<Category1>> call, Response<ArrayList<Category1>> response) {
                for ( Category1 cat : response.body())
                {
                    Log.d("Records:Button--", cat.getTitle());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Category1>> call, Throwable throwable) {
                Log.d("Records:Button--", SERVICE_API_PATH);
                Log.d("Records:Button--", throwable.getMessage());
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