package com.example.ezcashapp;

import android.app.PendingIntent;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.RemoteMessage;

/**
 *
 * NAME: FirebaseMessagingService - Messaging Service class for friend requests
 *
 * DESCRIPTION: This class uses FirebaseMessagingService to send a notification to a user's device after they've received a friend request.
 *
 * AUTHOR: Nitesh Parajuli
 *
 * DATE 7/31/2020
 *
 */

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    /**
     *
     * NAME: FirebaseMessagingService:onMessageReceived() - sends a notification to a user's device after the firebase function is triggered
     *
     * SYNOPSIS: public void onMessageReceived(@NonNull RemoteMessage remoteMessage)
     *           remoteMessage -> message received from firebase's messaging service
     *
     * DESCRIPTION: This function sends a notification to a user's device after they receive a friend request.
     *              The firebase function defined in the firebase console send the notification details as a remoteMessage
     *
     * RETURNS: Nothing.
     *
     *  AUTHOR: Nitesh Parajuli
     *
     *  DATE 8/1/2020
     *
     */
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String notification_title = remoteMessage.getNotification().getTitle();
        String notification_message = remoteMessage.getNotification().getBody();

        String click_action = remoteMessage.getNotification().getClickAction();
        String from_user_id = remoteMessage.getData().get("from_user_id");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "CHANNEL_ID")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(notification_title)
                .setContentText(notification_message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Intent intent = new Intent(click_action);
        intent.putExtra("user_id",from_user_id);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        builder.setContentIntent(pendingIntent);

        int mNotificationId = (int) System.currentTimeMillis();


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(mNotificationId, builder.build());

    }
}
