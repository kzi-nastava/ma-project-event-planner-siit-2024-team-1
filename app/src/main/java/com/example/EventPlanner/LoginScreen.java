package com.example.EventPlanner;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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