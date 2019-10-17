package com.integro.eggpro.firebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.integro.eggpro.DeliveryStatusActivity;
import com.integro.eggpro.MyOrdersActivity;
import com.integro.eggpro.R;
import com.integro.eggpro.model.DeliveryNotificationBody;
import com.integro.eggpro.model.RechargeNotificationBody;

import java.util.Calendar;
import java.util.Map;

import static com.integro.eggpro.constants.GenralConstants.FCMTAG;
import static com.integro.eggpro.constants.GenralConstants.ITEM_LIST;
import static com.integro.eggpro.constants.GenralConstants.PREFERENCE;
import static com.integro.eggpro.constants.GenralConstants.PREFERENCE_PRIVATE;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public static final String TAG = "MyFirebaseMsgService";
    public static final String NOTIFICATION_CHANNEL_ID = "1";
    public static final String TYPE = "notificationType";
    public static final String NOTIFICATION_CHANNEL_NAME = "notifications";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        sendNotification(remoteMessage.getData());
        Log.i(TAG, "onMessageReceived: " + remoteMessage.getData());
    }

    private void sendNotification(Map<String, String> data) {
        Log.i(TAG, "sendNotification: " + data);
        String body = data.get("body");
        Intent intent = null;
        String title = "title";
        String description = null;
        String type = data.get(TYPE);

        try {
            if (type.equals("Recharge")) {
                RechargeNotificationBody nItem = (RechargeNotificationBody) new Gson().fromJson(body, RechargeNotificationBody.class);
                description = nItem.getMessage();
                intent = new Intent(getApplicationContext(), MyOrdersActivity.class);
            } else if (type.equals("Delivery")) {
                DeliveryNotificationBody deliveryNotificationBody = new Gson().fromJson(body, DeliveryNotificationBody.class);
                intent = new Intent(getApplicationContext(), DeliveryStatusActivity.class);
                description = deliveryNotificationBody.getMessage();
                Bundle bundle = new Bundle();
                bundle.putSerializable(ITEM_LIST, deliveryNotificationBody);
                intent.putExtra(ITEM_LIST,bundle);
            }
            PendingIntent pendingIntent;
            NotificationCompat.Builder mBuilder;
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                int importanceDefault = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, importanceDefault);
                channel.setDescription("description");
                notificationManager.createNotificationChannel(channel);
            }
            mBuilder = new NotificationCompat.Builder(this);
            if (intent != null) {
                intent.putExtra(TAG, true);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
                mBuilder.setContentIntent(pendingIntent);
            }

            mBuilder.setDefaults(android.app.Notification.DEFAULT_SOUND | android.app.Notification.DEFAULT_LIGHTS | android.app.Notification.DEFAULT_VIBRATE);
            mBuilder.setContentTitle(data.get(title));
            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            mBuilder.setContentText(description);
            mBuilder.setSmallIcon(R.drawable.ic_menu_camera);
            mBuilder.setAutoCancel(true);
            mBuilder.setPriority(android.app.Notification.PRIORITY_HIGH);
            mBuilder.setBadgeIconType(NotificationCompat.BADGE_ICON_LARGE);
            mBuilder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
            notificationManagerCompat.notify((int) (Calendar.getInstance().getTimeInMillis() / 100), mBuilder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        SharedPreferences pref = getApplicationContext().getSharedPreferences(PREFERENCE, PREFERENCE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(FCMTAG, token);
        editor.apply();
    }
}