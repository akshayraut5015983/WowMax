package com.swaliya.vidmax.configg;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.swaliya.vidmax.R;
import com.swaliya.vidmax.activity.SplashActivity;

public class MyMessagingService extends FirebaseMessagingService {
    String TAG = "TAG";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());

    }

    public void showNotification(String title, String msg) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "MyNo")
                .setContentTitle(title)
                .setSmallIcon(R.drawable.cir_logo)
                .setAutoCancel(true)
                .setContentText(msg);

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

      /*  NotificationManager notificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(R.drawable.cir_logo, msg,999);

        Intent notificationIntent = new Intent(this, SplashActivity.class);

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent intent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        notification.setLatestEventInfo(this, title, msg, intent);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(0, notification);*/

    }
   /* public void createNotification(Context context, String payload) {
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(R.drawable.cir_logo,
                "Message received", System.currentTimeMillis());
        // Hide the notification after its selected
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        //adding LED lights to notification
        notification.defaults |= Notification.DEFAULT_LIGHTS;

        Intent intent = new Intent("android.intent.action.VIEW",
                Uri.parse("http://my.example.com/"));
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                intent, 0);
        notification.setLatestEventInfo(context, "Message",
                "New message received", pendingIntent);
        notificationManager.notify(0, notification);

    }*/

}
