package com.example.EventPlanner.activities;

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
import com.example.EventPlanner.databinding.ActivityServiceDetailsBinding;
import com.example.EventPlanner.services.WebSocketService;

public class ServiceDetailsActivity extends AppCompatActivity {
    private int merchandiseId;
    private ActivityServiceDetailsBinding activityServiceDetailsBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        activityServiceDetailsBinding=ActivityServiceDetailsBinding.inflate(getLayoutInflater());
        setContentView(activityServiceDetailsBinding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent intent=getIntent();
        merchandiseId=intent.getIntExtra("MERCHANDISE_ID",-1);
        Button bookReservationBtn=activityServiceDetailsBinding.bookReservationBtn;
        bookReservationBtn.setOnClickListener(v->openBookReservation());
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

    private void openBookReservation(){
        Intent intent = new Intent(ServiceDetailsActivity.this, BookReservationActivity.class);
        intent.putExtra("MERCHANDISE_ID", merchandiseId);
        startActivity(intent);
    }
}