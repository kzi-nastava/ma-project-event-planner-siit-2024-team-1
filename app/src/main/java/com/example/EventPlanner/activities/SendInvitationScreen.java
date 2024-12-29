package com.example.EventPlanner.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.EventPlanner.R;
import com.example.EventPlanner.databinding.ActivitySendInvitationScreenBinding;
import com.example.EventPlanner.fragments.event.EventViewModel;
import com.example.EventPlanner.fragments.eventmerchandise.SearchViewModel;

public class SendInvitationScreen extends AppCompatActivity {

    private ActivitySendInvitationScreenBinding activitySendInvitationScreenBinding;
    private int eventId;
    private EventViewModel eventViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        activitySendInvitationScreenBinding=ActivitySendInvitationScreenBinding.inflate(getLayoutInflater());
        setContentView(activitySendInvitationScreenBinding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.send_invitation_screen), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        eventId = (int) getIntent().getExtras().get("EVENT_ID"); // Default value
        Button inviteBtn=activitySendInvitationScreenBinding.inviteBtn;
        eventViewModel=new ViewModelProvider(this).get(EventViewModel.class);
        inviteBtn.setOnClickListener(v->sendInvitation());
    }

    private void sendInvitation(){
        TextView email=activitySendInvitationScreenBinding.editTextEmail;
        eventViewModel.sendInvitation(email.getText().toString(),eventId);
    }
}