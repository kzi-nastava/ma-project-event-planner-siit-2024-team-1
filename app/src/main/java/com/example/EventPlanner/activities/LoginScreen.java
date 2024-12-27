package com.example.EventPlanner.activities;

import static com.example.EventPlanner.clients.ClientUtils.SERVICE_API_PATH;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
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
        Button registerButton=(Button) findViewById(R.id.registerButton);
        Button skipLoginButton=(Button) findViewById(R.id.skipLogin);
        loginButton.setOnClickListener(v -> {
            Intent intent=new Intent(LoginScreen.this,HomeScreen.class);
            startActivity(intent);
        });
        registerButton.setOnClickListener(v -> {
            Intent intent=new Intent(LoginScreen.this, RegisterPupScreen.class);
            startActivity(intent);
        });
        skipLoginButton.setOnClickListener(v -> {
            Intent intent=new Intent(LoginScreen.this, HomeScreen.class);
            startActivity(intent);
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