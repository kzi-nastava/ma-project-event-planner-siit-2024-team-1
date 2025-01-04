package com.example.EventPlanner.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.EventPlanner.BuildConfig;
import com.example.EventPlanner.activities.EventDetails;
import com.example.EventPlanner.activities.HomeScreen;
import com.example.EventPlanner.activities.LoginScreen;
import com.example.EventPlanner.activities.ProductDetailsActivity;
import com.example.EventPlanner.activities.ServiceDetailsActivity;
import com.example.EventPlanner.clients.ClientUtils;
import com.example.EventPlanner.clients.JwtService;
import com.example.EventPlanner.model.common.Notification;
import com.example.EventPlanner.model.common.NotificationType;
import com.google.gson.Gson;

import io.reactivex.disposables.CompositeDisposable;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

public class WebSocketService extends Service {
    private static final String WEBSOCKET_URL = "ws://"+ BuildConfig.IP_ADDR + ":8080/ws";
    private StompClient stompClient;
    private CompositeDisposable compositeDisposable;
    private int userId;
    private static final String CHANNEL_ID = "websocket_notifications";
    private static final String CHANNEL_NAME = "WebSocket Notifications";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            userId = intent.getIntExtra("USER_ID", -1);
            if (userId != -1) {
                connectWebSocket(userId);
            }
        }
        return START_STICKY;
    }

    private void connectWebSocket(int userId) {
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, WEBSOCKET_URL);
        compositeDisposable = new CompositeDisposable();

        stompClient.lifecycle().subscribe(lifecycleEvent -> {
            switch (lifecycleEvent.getType()) {
                case OPENED:
                    Log.d("WebSocket", "Connected");
                    break;
                case CLOSED:
                    Log.d("WebSocket", "Disconnected");
                    break;
                case ERROR:
                    Log.e("WebSocket", "Error", lifecycleEvent.getException());
                    break;
            }
        });

        stompClient.connect();

        compositeDisposable.add(
                stompClient.topic("/user/" + userId + "/notifications")
                        .subscribe(topicMessage -> {
                            // Parse the JSON payload into Notification object
                            Notification notification = parseNotification(topicMessage.getPayload());
                            showNotification(notification);
                        }, throwable -> {
                            Log.e("WebSocket", "Error on subscribe", throwable);
                        })
        );
        compositeDisposable.add(
                stompClient.topic("/user/" + userId + "/suspensions")
                        .subscribe(topicMessage -> {
                            // Parse the JSON payload into Notification object
                            Intent intent = new Intent(this, LoginScreen.class);
                            intent.putExtra("suspension","You have been suspended!");
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            JwtService.logout();
                            startActivity(intent);
                        }, throwable -> {
                            Log.e("WebSocket", "Error on subscribe", throwable);
                        })
        );
    }

    private Notification parseNotification(String payload) {
        try {
            Gson gson = new Gson();
            return gson.fromJson(payload, Notification.class);
        } catch (Exception e) {
            Log.e("WebSocket", "Error parsing notification", e);
            return null;
        }
    }

    private void showNotification(Notification notification) {
        if (notification == null) return;

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        String CHANNEL_ID = "your_channel_id";
        String CHANNEL_NAME = "Your Channel Name";

        // Create notification channel for Android O and above
        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH // High importance for drop-down visibility
        );
        channel.setDescription("Channel description"); // Optional
        notificationManager.createNotificationChannel(channel);

        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info) // Use an appropriate icon
                .setContentTitle("Event Planner")          // Title for the notification
                .setContentText(notification.getContent())      // Brief content for the notification
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(notification.getContent()))    // Expanded content for the notification
                .setPriority(NotificationCompat.PRIORITY_HIGH) // High priority to show in drop-down
                .setAutoCancel(true)                           // Auto-dismiss when tapped
                .setDefaults(NotificationCompat.DEFAULT_ALL);  // Use default vibration, sound, etc.

        // Optionally, add an intent to open an activity when the notification is tapped

        if(notification.getType()== NotificationType.EVENT) {
            Intent intent = new Intent(this, EventDetails.class); // Replace with your activity
            intent.putExtra("EVENT_ID", notification.getEntityId());    // Pass extras if needed
            PendingIntent pendingIntent = PendingIntent.getActivity(
                    this,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );
            builder.setContentIntent(pendingIntent);
        } else if (notification.getType()== NotificationType.PRODUCT) {
            Intent intent = new Intent(this, ProductDetailsActivity.class); // Replace with your activity
            intent.putExtra("MERCHANDISE_ID", notification.getEntityId());    // Pass extras if needed
            PendingIntent pendingIntent = PendingIntent.getActivity(
                    this,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );
            builder.setContentIntent(pendingIntent);
        } else if (notification.getType()== NotificationType.SERVICE) {
            Intent intent = new Intent(this, ServiceDetailsActivity.class); // Replace with your activity
            intent.putExtra("MERCHANDISE_ID", notification.getEntityId());    // Pass extras if needed
            PendingIntent pendingIntent = PendingIntent.getActivity(
                    this,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );
            builder.setContentIntent(pendingIntent);
        }


        // Notify the user
        notificationManager.notify(notification.getId(), builder.build());
    }


    public void disconnect() {
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
        if (stompClient != null) {
            stompClient.disconnect();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disconnect();
    }
}