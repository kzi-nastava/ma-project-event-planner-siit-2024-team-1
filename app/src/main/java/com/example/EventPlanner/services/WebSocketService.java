package com.example.EventPlanner.services;

import static android.app.Notification.VISIBILITY_PUBLIC;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.example.EventPlanner.model.common.PageResponse;
import com.example.EventPlanner.model.event.EventOverview;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

public class WebSocketService extends Service {
    private static final String WEBSOCKET_URL = "ws://"+ BuildConfig.IP_ADDR + ":8080/ws";
    private StompClient stompClient;
    private CompositeDisposable compositeDisposable;
    private int userId;
    public static final String ACTION_MARK_READ = "com.example.EventPlanner.ACTION_MARK_READ";

    private static final String EVENT_CHANNEL_ID = "event_channel_id";
    private static final String PRODUCT_CHANNEL_ID = "product_channel_id";
    private static final String SERVICE_CHANNEL_ID = "service_channel_id";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannels();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            if (ACTION_MARK_READ.equals(action)) {
                int notificationId = intent.getIntExtra("NOTIFICATION_ID", -1);
                if (notificationId != -1) {
                    markNotificationAsRead(notificationId);
                }
            } else {
                // Existing WebSocket connection logic
                userId = intent.getIntExtra("USER_ID", -1);
                if (userId != -1) {
                    connectWebSocket(userId);
                    fetchUnreadNotifications(userId);
                }
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

    private void fetchUnreadNotifications(int userId) {

        ClientUtils.notificationService.getUnreadNotifications(userId).enqueue(new Callback<List<Notification>>() {
            @Override
            public void onResponse(Call<List<Notification>> call, Response<List<Notification>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Notification> unreadNotifications = response.body();
                    for (Notification notification : unreadNotifications) {
                        showNotification(notification);
                    }
                    Log.d("WebSocket", "Fetched " + unreadNotifications.size() + " unread notifications");
                } else {
                    Log.e("WebSocket", "Error fetching unread notifications: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Notification>> call, Throwable t) {
                Log.e("WebSocket", "Failed to fetch unread notifications", t);
            }
        });
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

    private void createNotificationChannels() {
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Event Channel
        NotificationChannel eventChannel = new NotificationChannel(
                EVENT_CHANNEL_ID,
                "Event Notifications",
                NotificationManager.IMPORTANCE_HIGH
        );
        eventChannel.setDescription("Notifications for upcoming and ongoing events");
        // Add these settings
        eventChannel.enableLights(true);
        eventChannel.enableVibration(true);
        eventChannel.setLockscreenVisibility(VISIBILITY_PUBLIC);
        eventChannel.setShowBadge(true);
        notificationManager.createNotificationChannel(eventChannel);

        // Product Channel
        NotificationChannel productChannel = new NotificationChannel(
                PRODUCT_CHANNEL_ID,
                "Product Notifications",
                NotificationManager.IMPORTANCE_HIGH
        );
        productChannel.setDescription("Updates and alerts about products");
        // Add these settings
        productChannel.enableLights(true);
        productChannel.enableVibration(true);
        productChannel.setLockscreenVisibility(VISIBILITY_PUBLIC);
        productChannel.setShowBadge(true);
        notificationManager.createNotificationChannel(productChannel);

        // Service Channel
        NotificationChannel serviceChannel = new NotificationChannel(
                SERVICE_CHANNEL_ID,
                "Service Notifications",
                NotificationManager.IMPORTANCE_HIGH
        );
        serviceChannel.setDescription("Updates and alerts about services");
        // Add these settings
        serviceChannel.enableLights(true);
        serviceChannel.enableVibration(true);
        serviceChannel.setLockscreenVisibility(VISIBILITY_PUBLIC);
        serviceChannel.setShowBadge(true);
        notificationManager.createNotificationChannel(serviceChannel);
    }

    private void showNotification(Notification notification) {
        if (notification == null) return;

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Create delete intent for swipe dismiss
        Intent deleteIntent = new Intent(this, WebSocketService.class);
        deleteIntent.setAction(ACTION_MARK_READ);
        deleteIntent.putExtra("NOTIFICATION_ID", notification.getId());
        PendingIntent deletePendingIntent = PendingIntent.getService(
                this,
                notification.getId(),
                deleteIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Create content intent based on notification type
        Intent contentIntent;
        String channelId;
        String title;

        switch (notification.getType()) {
            case EVENT:
                contentIntent = new Intent(this, EventDetails.class)
                        .putExtra("EVENT_ID", notification.getEntityId());
                channelId = EVENT_CHANNEL_ID;
                title = "Event Update";
                break;
            case PRODUCT:
                contentIntent = new Intent(this, ProductDetailsActivity.class)
                        .putExtra("MERCHANDISE_ID", notification.getEntityId());
                channelId = PRODUCT_CHANNEL_ID;
                title = "Product Update";
                break;
            case SERVICE:
                contentIntent = new Intent(this, ServiceDetailsActivity.class)
                        .putExtra("MERCHANDISE_ID", notification.getEntityId());
                channelId = SERVICE_CHANNEL_ID;
                title = "Service Update";
                break;
            default:
                contentIntent = new Intent(this, HomeScreen.class);
                channelId = EVENT_CHANNEL_ID; // fallback to event channel
                title = "Event Planner";
        }
        contentIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        contentIntent.putExtra("NOTIFICATION_ID", notification.getId());

        // Create pending intent for content click
        PendingIntent contentPendingIntent = PendingIntent.getActivity(
                this,
                notification.getId(),
                contentIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(notification.getContent())
                .setStyle(new NotificationCompat.BigTextStyle().bigText(notification.getContent()))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setDeleteIntent(deletePendingIntent)
                .setContentIntent(contentPendingIntent)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                // Add these settings
                .setFullScreenIntent(contentPendingIntent, true)  // Add this line
                .setVibrate(new long[]{0, 250, 250, 250})
                .setLights(Color.RED, 3000, 3000)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        notificationManager.notify(notification.getId(), builder.build());
    }



    private void markNotificationAsRead(int notificationId) {
        ClientUtils.notificationService.markAsRead(notificationId).enqueue(new Callback<Notification>() {
            @Override
            public void onResponse(Call<Notification> call, Response<Notification> response) {
                if (response.isSuccessful()) {
                    Log.d("WebSocket", "Notification marked as read: " + notificationId);
                    // Cancel the notification from the notification drawer
                    NotificationManager notificationManager =
                            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.cancel(notificationId);
                } else {
                    Log.e("WebSocket", "Error marking notification as read: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Notification> call, Throwable t) {
                Log.e("WebSocket", "Failed to mark notification as read", t);
            }
        });
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