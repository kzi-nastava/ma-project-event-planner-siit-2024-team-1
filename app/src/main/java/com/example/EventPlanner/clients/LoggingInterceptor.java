package com.example.EventPlanner.clients;

import android.util.Log;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class LoggingInterceptor implements Interceptor {
    private static final String TAG = "HTTP_LOG";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        // Log request details
        Log.d(TAG, "Request URL: " + request.url());
        Log.d(TAG, "Request Method: " + request.method());
        Log.d(TAG, "Request Headers: " + request.headers());

        Response response = chain.proceed(request);

        // Log response details if needed
        Log.d(TAG, "Response Code: " + response.code());
        Log.d(TAG, "Response Message: " + response.message());
        Log.d(TAG, "Response Headers: " + response.headers());

        return response;
    }
}
