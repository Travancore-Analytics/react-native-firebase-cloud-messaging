package com.firebasePushNotification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService     {
    private static final String DATA = "data";
    private static final String IMAGE_NAME = "pushnotification_icon";
    private static final String IMAGE_TYPE = "drawable";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map<String, String> notificationData = remoteMessage.getData();
        HashMap<String, String> hashMapNotificationData = new HashMap<String, String>(notificationData);
        String body = remoteMessage.getNotification().getBody();
        String title = remoteMessage.getNotification().getTitle();

        Intent intent = new Intent(this, getMainActivityClass(this));
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        intent.putExtra(DATA,hashMapNotificationData);

        String notificationId = remoteMessage.getMessageId();
        PendingIntent pendingIntent = PendingIntent.getActivity( this, notificationId.hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT );

        NotificationManager nm = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);

        NotificationChannel channel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            AudioAttributes att = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build();

            channel = new NotificationChannel("222", "channel", NotificationManager.IMPORTANCE_HIGH);
            nm.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(
                        this, "222")
                        .setContentTitle(title)
                        .setAutoCancel(true)
                        .setContentText(body)
                        .setContentIntent(pendingIntent)
                ;
        int smallIconResourceId = getResourceId(this,IMAGE_TYPE,IMAGE_NAME);
        if (smallIconResourceId != 0) {
            builder = builder.setSmallIcon(smallIconResourceId);
        }
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        nm.notify(remoteMessage.getMessageId().hashCode(), builder.build());
    }

    private Class getMainActivityClass(Context context) {
        String packageName = context.getPackageName();
        Intent launchIntent = context
                .getPackageManager()
                .getLaunchIntentForPackage(packageName);

        try {
            return Class.forName(launchIntent.getComponent().getClassName());
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
    private int getResourceId(Context context, String type, String image) {
        return context
                .getResources()
                .getIdentifier(image, type, context.getPackageName());
    }
}

