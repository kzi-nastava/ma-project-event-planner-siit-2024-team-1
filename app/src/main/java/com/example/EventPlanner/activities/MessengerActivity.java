package com.example.EventPlanner.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
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
import com.example.EventPlanner.databinding.ActivityMessengerBinding;
import com.example.EventPlanner.model.common.ErrorResponseDto;
import com.example.EventPlanner.model.user.BlockUserDTO;
import com.example.EventPlanner.model.user.UserReport;
import com.example.EventPlanner.model.user.UserReportResponse;
import com.example.EventPlanner.model.user.UserSuspension;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

public class MessengerActivity extends AppCompatActivity {

    private int userId;
    private ActivityMessengerBinding activityMessengerBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        activityMessengerBinding=ActivityMessengerBinding.inflate(getLayoutInflater());
        setContentView(activityMessengerBinding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent intent=getIntent();
        userId=intent.getIntExtra("USER_ID",-1);
        Button blockBtn=activityMessengerBinding.blockBtn;
        blockBtn.setOnClickListener(v -> {
            new MaterialAlertDialogBuilder(MessengerActivity.this)
                    .setTitle("Confirm Block")
                    .setMessage("Are you sure you want to block this user?")
                    .setNegativeButton("No", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // Call your approve function here
                        blockUser();
                    })
                    .show();
        });
        Button reportBtn=activityMessengerBinding.reportBtn;
        reportBtn.setOnClickListener(v -> showReportDialog());
    }

    private void showReportDialog() {
        // Create EditText for reason input
        EditText reasonInput = new EditText(this);
        reasonInput.setHint("Enter reason for report");
        reasonInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        reasonInput.setMinLines(3);
        reasonInput.setMaxLines(5);

        // Add padding to EditText
        int padding = (int) (16 * getResources().getDisplayMetrics().density); // 16dp converted to pixels
        reasonInput.setPadding(padding, padding, padding, padding);

        new MaterialAlertDialogBuilder(this)
                .setTitle("Report User")
                .setMessage("Please provide a reason for reporting this user")
                .setView(reasonInput)
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .setPositiveButton("Report", (dialog, which) -> {
                    String reason = reasonInput.getText().toString().trim();
                    if (!reason.isEmpty()) {
                        reportUser(reason);
                    } else {
                        Toast.makeText(this, "Please enter a reason", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

    private void reportUser(String reason) {
        UserReport report = new UserReport();
        report.setReportedUserId(userId);
        report.setReporterId(JwtService.getIdFromToken()); // Assuming you have a method to get current user ID
        report.setReason(reason);

        ClientUtils.userReportService.reportUser(report).enqueue(new Callback<UserReportResponse>() {
            @Override
            public void onResponse(Call<UserReportResponse> call, Response<UserReportResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MessengerActivity.this,
                            "User reported successfully",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MessengerActivity.this,
                            "Failed to report user",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserReportResponse> call, Throwable t) {
                Toast.makeText(MessengerActivity.this,
                        "Network error occurred",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void blockUser(){
        ClientUtils.userService.blockUser(JwtService.getIdFromToken(),userId).enqueue(new Callback<BlockUserDTO>() {
            @Override
            public void onResponse(Call<BlockUserDTO> call, Response<BlockUserDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(MessengerActivity.this,
                            "User Blocked",
                            Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    try {
                        Converter<ResponseBody, ErrorResponseDto> converter = ClientUtils.retrofit.responseBodyConverter(
                                ErrorResponseDto.class, new Annotation[0]);
                        ErrorResponseDto errorResponse = converter.convert(response.errorBody());

                        if (errorResponse != null) {
                            Toast.makeText(MessengerActivity.this, errorResponse.getMessage(), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(MessengerActivity.this, "Unknown error occurred", Toast.LENGTH_LONG).show();
                        }
                    } catch (IOException e) {
                        Toast.makeText(MessengerActivity.this, "Error parsing server response", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<BlockUserDTO> call, Throwable t) {
                Toast.makeText(MessengerActivity.this,
                        "Network error",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}