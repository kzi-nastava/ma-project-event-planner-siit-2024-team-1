package com.example.EventPlanner.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.EventPlanner.R;
import com.example.EventPlanner.adapters.message.MessageAdapter;
import com.example.EventPlanner.adapters.user.UserOverviewAdapter;
import com.example.EventPlanner.clients.ClientUtils;
import com.example.EventPlanner.clients.JwtService;
import com.example.EventPlanner.databinding.ActivityMessengerBinding;
import com.example.EventPlanner.fragments.user.UserOverviewViewModel;
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
    private MessengerViewModel messengerViewModel;
    private ActivityMessengerBinding activityMessengerBinding;

    private MessageAdapter messageAdapter;
    private int currentUserId; // Get this from your auth system

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        activityMessengerBinding = ActivityMessengerBinding.inflate(getLayoutInflater());
        setContentView(activityMessengerBinding.getRoot());

        setupInsets();
        setupIntentData();
        setupButtons();
        setupRecyclerView();
        setupViewModel();
        setupMessageInput();
    }
    private void setupInsets() {
        // Make sure the content is laid out behind system bars
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        // Use WindowInsetsControllerCompat for handling keyboard
        WindowInsetsControllerCompat controller = WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        controller.setSystemBarsBehavior(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);

        ViewCompat.setOnApplyWindowInsetsListener(activityMessengerBinding.main, (view, windowInsets) -> {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            Insets imeInsets = windowInsets.getInsets(WindowInsetsCompat.Type.ime());

            // Apply padding for the system bars to the main container
            view.setPadding(insets.left, insets.top, insets.right, insets.bottom);

            // Adjust the bottom padding of the RecyclerView when keyboard appears
            int bottomPadding = imeInsets.bottom > 0 ? imeInsets.bottom : insets.bottom;
            activityMessengerBinding.messagesRecyclerView.setPadding(0, 0, 0, bottomPadding);

            // Adjust the message input layout position
            activityMessengerBinding.messageInputLayout.setTranslationY(-imeInsets.bottom);

            return WindowInsetsCompat.CONSUMED;
        });

        // Handle window insets animation
        getWindow().getDecorView().setOnApplyWindowInsetsListener((view, windowInsets) -> {
            return view.onApplyWindowInsets(windowInsets);
        });
    }

//    private void setupSystemInsets() {
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//    }

    private void setupIntentData() {
        Intent intent = getIntent();
        userId = intent.getIntExtra("USER_ID", -1);
        currentUserId = JwtService.getIdFromToken(); // Implement this based on your auth system
    }

    private void setupButtons() {
        activityMessengerBinding.blockBtn.setOnClickListener(v -> {
            new MaterialAlertDialogBuilder(MessengerActivity.this)
                    .setTitle("Confirm Block")
                    .setMessage("Are you sure you want to block this user?")
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                    .setPositiveButton("Yes", (dialog, which) -> blockUser())
                    .show();
        });

        activityMessengerBinding.reportBtn.setOnClickListener(v -> showReportDialog());
    }

    private void setupRecyclerView() {
        messageAdapter = new MessageAdapter(currentUserId);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);

        RecyclerView recyclerView = activityMessengerBinding.messagesRecyclerView;
        recyclerView.setAdapter(messageAdapter);
        recyclerView.setLayoutManager(layoutManager);

        // Add scroll listener to handle keyboard appearance
        recyclerView.addOnLayoutChangeListener((v, left, top, right, bottom,
                                                oldLeft, oldTop, oldRight, oldBottom) -> {
            if (bottom < oldBottom) {
                recyclerView.postDelayed(() -> {
                    int messageCount = messageAdapter.getItemCount();
                    if (messageCount > 0) {
                        recyclerView.smoothScrollToPosition(messageCount - 1);
                    }
                }, 100);
            }
        });
    }

    private void setupViewModel() {
        messengerViewModel = new ViewModelProvider(this).get(MessengerViewModel.class);

        messengerViewModel.getMessagedUser().observe(this, user -> {
            activityMessengerBinding.messagedUser.setText(
                    String.format("%s %s", user.getFirstName(), user.getLastName()));
            activityMessengerBinding.userProfileMessenger.setImageResource(R.drawable.dinja);
        });

        messengerViewModel.getMessages().observe(this, messages -> {
            messageAdapter.setMessages(messages);
            scrollToBottom();
        });

        messengerViewModel.getChatUser(userId);
        messengerViewModel.getMessagesFromSenderAndRecepient(currentUserId, userId);
    }

    private void setupMessageInput() {
        activityMessengerBinding.sendButton.setOnClickListener(v -> {
            String message = activityMessengerBinding.messageInput.getText().toString().trim();
            if (!message.isEmpty()) {
                sendMessage(message);
                activityMessengerBinding.messageInput.setText("");
            }
        });

        // Handle IME action
        activityMessengerBinding.messageInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                String message = v.getText().toString().trim();
                if (!message.isEmpty()) {
                    sendMessage(message);
                    v.setText("");
                }
                return true;
            }
            return false;
        });
    }

    private void scrollToBottom() {
        activityMessengerBinding.messagesRecyclerView.post(() -> {
            int messageCount = messageAdapter.getItemCount();
            if (messageCount > 0) {
                activityMessengerBinding.messagesRecyclerView.smoothScrollToPosition(messageCount - 1);
            }
        });
    }

    private void sendMessage(String content) {
        // Implement your message sending logic here
        // After successful send, refresh messages
        messengerViewModel.getMessagesFromSenderAndRecepient(currentUserId, userId);
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
                    Intent intent = new Intent(MessengerActivity.this, HomeScreen.class);
                    startActivity(intent);
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