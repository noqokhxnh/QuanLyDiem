package com.example.qld.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.qld.R;


public class NotificationUtil {
    private static final String CHANNEL_ID = "grade_notification_channel";
    private static final int NOTIFICATION_ID = 1;

    /**
     * Creates a notification channel (required for Android O and above)
     * @param context The application context
     */
    public static void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Thông báo điểm";
            String description = "Thông báo về điểm số và cập nhật học tập";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            // Register the channel with the system
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    /**
     * Shows a notification about a grade update
     * @param context The application context
     * @param title Title of the notification
     * @param contentText Content of the notification
     */
    public static void showGradeNotification(Context context, String title, String contentText) {
        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(context, com.example.qld.activities.LoginActivity.class); // Using LoginActivity as entry point
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification) // Using a placeholder icon
                .setContentTitle(title)
                .setContentText(contentText)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(contentText))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true); // Dismiss after being tapped

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        // Issue the notification
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    /**
     * Shows a general system notification
     * @param context The application context
     * @param title Title of the notification
     * @param contentText Content of the notification
     */
    public static void showSystemNotification(Context context, String title, String contentText) {
        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(context, com.example.qld.activities.LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(contentText)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        // Issue the notification
        notificationManager.notify(NOTIFICATION_ID + 1, builder.build()); // Using different ID to avoid overwriting
    }
}