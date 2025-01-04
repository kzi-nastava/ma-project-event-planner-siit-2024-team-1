package com.example.EventPlanner.model.auth;


public class AuthenticationResponse {
    private String access_token;

    private String refresh_token;

    private String message;

    public AuthenticationResponse(String accessToken, String refreshToken, String message) {
        this.access_token = accessToken;
        this.message = message;
        this.refresh_token = refreshToken;
    }

    public String getAccessToken() {
        return access_token;
    }

    public String getRefreshToken() {
        return refresh_token;
    }

    public String getMessage() {
        return message;
    }
}

