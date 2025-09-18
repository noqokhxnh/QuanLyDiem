package com.example.qld.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.qld.R;

/**
 * Utility class để quản lý thông báo
 */
public class NotificationUtil {
    private static final String CHANNEL_ID = "score_notification_channel";
    private static final int NOTIFICATION_ID = 1001;
    
    /**
     * Tạo notification channel (cho Android 8.0 trở lên)
     * @param context context
     */
    public static void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Score Notifications";
            String description = "Notifications for new scores";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    
    /**
     * Hiển thị thông báo điểm mới
     * @param context context
     * @param title tiêu đề thông báo
     * @param message nội dung thông báo
     */
    public static void showScoreNotification(Context context, String title, String message) {
        createNotificationChannel(context);
        
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);
        
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        try {
            notificationManager.notify(NOTIFICATION_ID, builder.build());
        } catch (SecurityException e) {
            // Handle the exception if notification permission is not granted
        }
    }
}