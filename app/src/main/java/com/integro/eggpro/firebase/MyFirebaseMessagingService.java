package com.integro.eggpro.firebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.integro.eggpro.DeliveryStatusActivity;
import com.integro.eggpro.MainActivity;
import com.integro.eggpro.MyOrdersActivity;
import com.integro.eggpro.R;
import com.integro.eggpro.model.DeliveryNotificationBody;
import com.integro.eggpro.model.OffersNotificationBody;
import com.integro.eggpro.model.RechargeNotificationBody;
import com.integro.eggpro.model.UpdateNotificationBody;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Map;

import static com.integro.eggpro.constants.GenralConstants.FCMTAG;
import static com.integro.eggpro.constants.GenralConstants.ORDER_ID;
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
        String link = null;
        String img = null;
        String type = data.get(TYPE);
        PendingIntent pendingIntent;
        NotificationCompat.Builder mBuilder;

        try {
            if (type.equals("Recharge")) {
                RechargeNotificationBody nItem = (RechargeNotificationBody) new Gson().fromJson(body, RechargeNotificationBody.class);
                description = nItem.getMessage();
                intent = new Intent(getApplicationContext(), MyOrdersActivity.class);
            } else if (type.equals("Delivery")) {
                DeliveryNotificationBody deliveryNotificationBody = new Gson().fromJson(body, DeliveryNotificationBody.class);
                intent = new Intent(getApplicationContext(), DeliveryStatusActivity.class);
                description = deliveryNotificationBody.getMessage();
                //img=deliveryNotificationBody.
                intent.putExtra(ORDER_ID, deliveryNotificationBody.getOrderId());
            } else if (type.equals("update")) {
                UpdateNotificationBody updateNotificationBody = (UpdateNotificationBody) new Gson().fromJson(body, UpdateNotificationBody.class);
                img = updateNotificationBody.getLink();
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(updateNotificationBody.getLink()));
            } else if (type.equals("offers") || type.equals("others")) {
                OffersNotificationBody offersNotificationBody=new Gson().fromJson(body,OffersNotificationBody.class);
                intent=new Intent(this, MainActivity.class);
                img=offersNotificationBody.getImg();
                link=offersNotificationBody.getLink();
                description=offersNotificationBody.getMessage();
            }

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                int importanceDefault = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, importanceDefault);
                channel.setDescription("description");
                notificationManager.createNotificationChannel(channel);
            }

            intent.putExtra(TAG, true);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

            mBuilder = new NotificationCompat.Builder(this);
            mBuilder.setDefaults(android.app.Notification.DEFAULT_SOUND | android.app.Notification.DEFAULT_LIGHTS | android.app.Notification.DEFAULT_VIBRATE);
            mBuilder.setContentTitle(data.get(title));
            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            mBuilder.setContentText(description);
            mBuilder.setStyle(new NotificationCompat.BigTextStyle()
                    .bigText(description));
            mBuilder.setSmallIcon(R.drawable.ic_launcher);
            mBuilder.setAutoCancel(true);
            mBuilder.setLargeIcon(((BitmapDrawable)getResources().getDrawable(R.drawable.ic_launcher)).getBitmap());
            mBuilder.setPriority(android.app.Notification.PRIORITY_HIGH);
            mBuilder.setBadgeIconType(NotificationCompat.BADGE_ICON_LARGE);
            mBuilder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
            mBuilder.setContentIntent(pendingIntent);

            if (img!=null){
                Bitmap bitmap_image = getBitmapFromURL(img);
                if (bitmap_image == null) {
                    mBuilder.setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(description));
                } else {
                    NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle().bigPicture(bitmap_image);
                    mBuilder.setStyle(style);
                    mBuilder.setLargeIcon(drawableToBitmap(getResources().getDrawable(R.drawable.ic_launcher)));
                }
            }

            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
            notificationManagerCompat.notify((int) (Calendar.getInstance().getTimeInMillis() / 100), mBuilder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap drawableToBitmap (Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }
        int width = drawable.getIntrinsicWidth();
        width = width > 0 ? width : 1;
        int height = drawable.getIntrinsicHeight();
        height = height > 0 ? height : 1;
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
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