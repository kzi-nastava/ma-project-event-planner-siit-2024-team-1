package com.example.EventPlanner.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.EventPlanner.R;
import com.example.EventPlanner.services.WebSocketService;

public class ProductDetailsActivity extends AppCompatActivity {

    private int merchandiseId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent intent=getIntent();
        merchandiseId=intent.getIntExtra("MERCHANDISE_ID",-1);
        Log.d("merchandise_id", String.valueOf(merchandiseId));
        int notificationId = getIntent().getIntExtra("NOTIFICATION_ID", -1);
        if (notificationId != -1) {
            // Mark notification as read
            Intent markReadIntent = new Intent(this, WebSocketService.class);
            markReadIntent.setAction(WebSocketService.ACTION_MARK_READ);
            markReadIntent.putExtra("NOTIFICATION_ID", notificationId);
            startService(markReadIntent);
        }
    }
}