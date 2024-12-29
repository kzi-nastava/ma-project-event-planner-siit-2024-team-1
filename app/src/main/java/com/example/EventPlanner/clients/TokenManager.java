package com.example.EventPlanner.clients;

import android.content.Context;
import android.content.SharedPreferences;

public class TokenManager {
    private static String accessToken;
    private static String refreshToken;
    private static String eventToken;

    public static void setAccessToken(String accessToken) {
        TokenManager.accessToken = accessToken;
    }

    public static String getAccessToken() {
        return accessToken;
    }

    public static String getRefreshToken() {
        return refreshToken;
    }

    public static void setRefreshToken(String refreshToken) {
        TokenManager.refreshToken = refreshToken;
    }

    public static String getEventToken() {
        return eventToken;
    }

    public static void setEventToken(String eventToken) {
        TokenManager.eventToken = eventToken;
    }
}
