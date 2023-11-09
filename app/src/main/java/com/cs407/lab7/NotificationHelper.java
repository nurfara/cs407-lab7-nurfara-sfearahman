package com.cs407.lab7;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

// singleton class to send notifs
public class NotificationHelper {
    // constructor set to private so instance of this class can't be created anywhere outside of it.
    // INSTANCE stores only instance of this class.
    private static final NotificationHelper INSTANCE = new NotificationHelper();

    private NotificationHelper() {}

    // returns a ref of the only instance
    public static NotificationHelper getInstance() {
        return INSTANCE;
    }

    public static final String CHANNEL_ID = "channel_chat";

    public void createNotificationChannel(Context context) {
        // check if curr OS ver is greater than Android O (channels aren't available on versions below O)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // get name & desc of channel from strings.xml
            CharSequence name = context.getString(R.string.channel_name);
            String description = context.getString(R.string.channel_description);

            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            // add ID, name, importance, desc to NotificationChannel object
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            // register the new NotificationChannel object with the Android system
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private int notificationId = 0;
    private String sender = null;
    private String message = null;

    public void setNotificationContent(String sender, String message) {
        this.sender = sender;
        this.message = message;
        this.notificationId++;
    }

    public void showNotification(Context context) {
        // check permission to send a notif
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        // Create a basic notif with specified title & text.
        // Priority used as a fallback if the running Android ver doesn't support notif channels.
        // Set visibility level of notif to private (only app name of notif can be seen on lock screen).
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
                .setContentTitle(sender)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(notificationId, builder.build());

    }

}
