package com.example.EventPlanner.clients;

import android.util.Base64;
import org.json.JSONObject;

public class JwtService {

    // Set tokens using TokenManager
    public static void setTokens(String accessToken, String refreshToken) {
        TokenManager.setAccessToken(accessToken); // Store the access token
        TokenManager.setRefreshToken(refreshToken); // Store the refresh token
    }

    // Store event token
    public static void setEventToken(String eventToken) {
        TokenManager.setEventToken(eventToken);
    }

    // Remove event token
    public static void removeEventToken() {
        TokenManager.setEventToken("");
    }

    // Get access token from TokenManager
    public static String getAccessToken() {
        return TokenManager.getAccessToken();
    }

    // Get refresh token from TokenManager
    public static String getRefreshToken() {
        return TokenManager.getRefreshToken();
    }

    // Get event token from TokenManager
    public static String getEventToken() {
        return TokenManager.getEventToken();
    }

    // Check if logged in
    public static boolean isLogged() {
        return getAccessToken() != null;
    }

    // Decode JWT token
    public static JSONObject decodeToken(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                throw new IllegalArgumentException("Invalid JWT token");
            }

            // Decode header and payload
            String header = new String(Base64.decode(parts[0], Base64.URL_SAFE));
            String body = new String(Base64.decode(parts[1], Base64.URL_SAFE));

            // Convert the body into a JSONObject
            return new JSONObject(body);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Check if token is valid
    public static boolean isTokenValid(String token) {
        JSONObject decoded = decodeToken(token);
        return decoded != null; // Add expiration checks if needed
    }

    // Check if logged in and the token is valid
    public static boolean isLoggedIn() {
        String token = getAccessToken();
        return token != null && isTokenValid(token);
    }

    // Get role from token
    public static String getRoleFromToken() {
        String token = getAccessToken();
        if (token != null) {
            JSONObject tokenInfo = decodeToken(token);
            if (tokenInfo != null && tokenInfo.has("role")) {
                return tokenInfo.optString("role", "");
            }
        }
        return "";
    }

    public static String getEmailFromToken() {
        String token = getAccessToken();
        if (token != null) {
            JSONObject tokenInfo = decodeToken(token);
            if (tokenInfo != null && tokenInfo.has("sub")) {
                return tokenInfo.optString("sub", "");
            }
        }
        return "";
    }

    // Get ID from token
    public static int getIdFromToken() {
        String token = getAccessToken();
        if (token != null) {
            JSONObject tokenInfo = decodeToken(token);
            if (tokenInfo != null && tokenInfo.has("id")) {
                return tokenInfo.optInt("id", -1);
            }
        }
        return -1;
    }

    // Check if the user is an Admin
    public static boolean isAdmin() {
        return "A".equals(getRoleFromToken());
    }

    // Check if the user is an AU (Authorized User)
    public static boolean isAu() {
        return "AU".equals(getRoleFromToken());
    }

    // Check if the user is an SP (Service Provider)
    public static boolean isSp() {
        return "SP".equals(getRoleFromToken());
    }

    // Check if the user is an EO (Event Organizer)
    public static boolean isEo() {
        return "EO".equals(getRoleFromToken());
    }

    // Logout by removing the tokens
    public static void logout() {
        TokenManager.setAccessToken(null); // Clear the access token
        TokenManager.setRefreshToken(null); // Clear the refresh token
        TokenManager.setEventToken(null); // Clear the event token
    }
}
