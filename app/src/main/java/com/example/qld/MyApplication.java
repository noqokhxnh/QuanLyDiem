package com.example.qld;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.example.qld.utils.NotificationUtil;

public class MyApplication extends Application {
    private static final String TAG = "MyApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        
        // Set up notification channel
        NotificationUtil.createNotificationChannel(this);
        
        // Set up global exception handler
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                Log.e(TAG, "Uncaught exception: " + ex.getMessage(), ex);
                
                // In a real app, you might want to log this to a crash reporting service
                // For now, just log the error
                
                // Don't kill the app, just log the error
                // In a real production app, you might want to gracefully handle the error differently
            }
        });
    }
}