package com.example.EventPlanner;

import android.app.Application;

import org.osmdroid.config.Configuration;

import java.io.File;

public class EventPlannerApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Set a proper user agent for tile downloads
        Configuration.getInstance().setUserAgentValue("com.example.EventPlanner.configuration");

        // Optional: use app's private storage for tiles
        Configuration.getInstance().setOsmdroidBasePath(
                new File(getCacheDir(), "osmdroid"));
        Configuration.getInstance().setOsmdroidTileCache(
                new File(getCacheDir(), "osmdroid/tiles"));
    }
}
