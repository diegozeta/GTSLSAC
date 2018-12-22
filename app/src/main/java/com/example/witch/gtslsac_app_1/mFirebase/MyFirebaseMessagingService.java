package com.example.witch.gtslsac_app_1.mFirebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.witch.gtslsac_app_1.Activities.MainActivity;
import com.example.witch.gtslsac_app_1.Activities.NotificationActivity;
import com.example.witch.gtslsac_app_1.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by witch on 06/07/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        String title = remoteMessage.getNotification().getTitle();
        String message = remoteMessage.getNotification().getBody();
        String click_action = remoteMessage.getNotification().getClickAction();
        String id_alquiler = "HOLA";
        String obj = remoteMessage.getData().get("id");
        if (obj != null) {
            try {
                id_alquiler = obj.toString();
                Log.e("ID DESDE FIREBASE>>>>>", id_alquiler);
            } catch (Exception e) {
                id_alquiler = "";
                e.printStackTrace();
            }
        }
        Intent intent;
        Log.e("ACTIVIDAD FIREBASE>>>>>", click_action);
        if (click_action.equals("NOTIFICATIONACTIVITY")) {
            intent = new Intent(this, NotificationActivity.class);
        }else{
            intent = new Intent(this, MainActivity.class);
        }
        Bundle bundle = new Bundle();
        bundle.putString("id_alquiler", id_alquiler);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setContentTitle(title);
        notificationBuilder.setContentText(message);
        notificationBuilder.setSmallIcon(R.drawable.logo_w);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }
}
