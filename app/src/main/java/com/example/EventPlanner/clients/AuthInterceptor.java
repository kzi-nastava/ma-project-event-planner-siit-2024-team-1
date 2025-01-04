package com.example.EventPlanner.clients;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.EventPlanner.activities.HomeScreen;
import com.example.EventPlanner.activities.LoginScreen;
import com.example.EventPlanner.model.auth.AuthenticationResponse;
import com.example.EventPlanner.model.auth.LoginResponse;
import com.example.EventPlanner.model.common.ErrorResponseDto;
import com.example.EventPlanner.model.event.CreatedEventResponse;
import com.example.EventPlanner.services.WebSocketService;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.concurrent.CountDownLatch;

public class AuthInterceptor implements Interceptor {

    private final MutableLiveData<AuthenticationResponse> liveResponse = new MutableLiveData<>();

    @Override
    public Response intercept(Chain chain) throws IOException {
        // Get the current access token
        String accessToken = TokenManager.getAccessToken();
        if (accessToken == null) {
            return chain.proceed(chain.request());  // No token, proceed normally
        }

        // Build the request with the Authorization header
        Request requestWithAuth = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();

        // Proceed with the request
        Response response = chain.proceed(requestWithAuth);

        // If the response is 401 (Unauthorized), refresh the token
        if (response.code() == 401) {
            String refreshToken = TokenManager.getRefreshToken();
            if (refreshToken != null) {
                try {
                    // Attempt to refresh the token
                    refreshAccessToken(refreshToken, chain);
                } catch (Exception e) {
                    Log.e("AuthInterceptor", "Error refreshing token", e);
                    // Handle error or redirect to login page if necessary
                }
            }
        }

        return response;  // Proceed with the original response if no errors
    }

    // Refresh the access token by calling the refresh token endpoint
    private void refreshAccessToken(String refreshToken, Chain chain) {
        // Make the request to refresh token
        Call<AuthenticationResponse> call = ClientUtils.authService.refreshToken("Bearer " + refreshToken,
                "{\"refreshToken\":\"" + refreshToken + "\"}");

        call.enqueue(new Callback<AuthenticationResponse>() {
            @Override
            public void onResponse(Call<AuthenticationResponse> call, retrofit2.Response<AuthenticationResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Post new token data to LiveData
                    liveResponse.postValue(response.body());
                    String newAccessToken = response.body().getAccessToken();
                    String newRefreshToken = response.body().getRefreshToken();

                    // Store new tokens
                    TokenManager.setTokens(newAccessToken, newRefreshToken);

                    // Retry the original request with the new access token
                    retryOriginalRequest(chain, newAccessToken);
                } else {
                    // Handle failure to refresh token (e.g., redirect to login)
                    Log.e("AuthInterceptor", "Failed to refresh token");
                }
            }

            @Override
            public void onFailure(Call<AuthenticationResponse> call, Throwable throwable) {
                // Handle network errors (e.g., no internet connection)
                Log.e("AuthInterceptor", "Error refreshing token", throwable);
            }
        });
    }

    // Retry the original request with the new access token
    private void retryOriginalRequest(Chain chain, String newAccessToken) {
        // Create a new request with the new access token
        Request newRequest = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer " + newAccessToken)
                .build();

        // Proceed with the new request (retry the original request with updated token)
        try {
            Response retryResponse = chain.proceed(newRequest);
        }
        catch (IOException e) {
            Log.e("AuthInterceptor", "Error retrying original request", e);
        }
        catch (Exception e) {
            Log.e("AuthInterceptor", "Error retrying original request", e);
            return;
        }
    }
}
