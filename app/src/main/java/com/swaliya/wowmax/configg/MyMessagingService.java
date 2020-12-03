package com.swaliya.wowmax.configg;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.swaliya.wowmax.R;
import com.swaliya.wowmax.activity.SplashActivity;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyMessagingService extends FirebaseMessagingService {
    String TAG = "TAG";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String str = String.valueOf(remoteMessage.getNotification().getImageUrl());

        createNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(), str);
        //   showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(),str);

    }

    public void showNotification(String title, String msg, String imageUrl) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "MyNo")
                .setContentTitle(title)
                .setSmallIcon(R.drawable.logo_wow)
                .setAutoCancel(true)

                .setContentText(msg);

        if (imageUrl != null) {
            Bitmap bitmap = getBitmapfromUrl(imageUrl);
            builder.setStyle(
                    new NotificationCompat.BigPictureStyle()
                            .bigPicture(bitmap)
                            .bigLargeIcon(null)
            ).setLargeIcon(bitmap);
        }

        Intent resultIntent = new Intent(this, SplashActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

      /*  Intent appIntent = new Intent(this, SplashActivity.class);
        PendingIntent.getActivity(this,0,appIntent,PendingIntent.FLAG_UPDATE_CURRENT);*/
        builder.setContentIntent(resultPendingIntent);
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(999, builder.build());


    }

    private void createNotification(String notificationTitle, String notificationContent, String imageUrl) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "MyNo");
        // Create a notificationManager object
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
        // If android version is greater than 8.0 then create notification channel
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            // Create a notification channel
            NotificationChannel notificationChannel = new NotificationChannel("MyNo", "MyNo", NotificationManager.IMPORTANCE_DEFAULT);
            // Set properties to notification channel
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300});

            // Pass the notificationChannel object to notificationManager
            notificationManager.createNotificationChannel(notificationChannel);
        }

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
// Set the notification parameters to the notification builder object
        builder.setContentTitle(notificationTitle)
                .setContentText(notificationContent)
                .setSmallIcon(R.drawable.logo_wow)
                .setSound(defaultSoundUri)
                .setAutoCancel(true);
// Set the image for the notification
        if (imageUrl != null) {
            Bitmap bitmap = getBitmapfromUrl(imageUrl);
            builder.setStyle(
                    new NotificationCompat.BigPictureStyle()
                            .bigPicture(bitmap)
                            .bigLargeIcon(null)
            ).setLargeIcon(bitmap);
        }

        notificationManager.notify(1, builder.build());

        Intent resultIntent = new Intent(this, SplashActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

      /*  Intent appIntent = new Intent(this, SplashActivity.class);
        PendingIntent.getActivity(this,0,appIntent,PendingIntent.FLAG_UPDATE_CURRENT);*/
        builder.setContentIntent(resultPendingIntent);
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(999, builder.build());

    }

    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);

        } catch (Exception e) {
            Log.e("awesome", "Error in getting notification image: " + e.getLocalizedMessage());
            return null;
        }
    }
}
