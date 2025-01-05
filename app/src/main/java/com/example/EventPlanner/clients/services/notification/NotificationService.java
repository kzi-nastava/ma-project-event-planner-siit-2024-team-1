package com.example.EventPlanner.clients.services.notification;

import com.example.EventPlanner.model.common.Notification;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface NotificationService {
    @GET("notifications/unread/{userId}")
    Call<List<Notification>> getUnreadNotifications(@Path("userId") int userId);
    @PUT("notifications/{notificationId}/read")
    Call<Notification> markAsRead(@Path("notificationId") int notificationId);
}
