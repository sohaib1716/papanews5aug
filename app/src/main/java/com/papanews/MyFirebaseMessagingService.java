package com.papanews;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private final String TAG = getClass().getSimpleName();

    private int increment = 0;

    //    @Override
//    public void onCreate() {
//        super.onCreate();
//
//        firebaseDatabase = FirebaseDatabase.getInstance();
//        dataRef = firebaseDatabase.getReference();
//    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d("token_update", s);

    }

//    private void token(String token) {
//        HashMap<String, String> obj = new HashMap<String, String>();
//        obj.put("token", token);
//        dataRef.child("FCM_Tokens").push().setValue(obj);
//    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        increment +=1;

        // Receive FCM token
//        Log.e("notiiii1", " something something");
        Log.e("notiiii1", String.valueOf(remoteMessage.getData().get("title")));
//        Log.e("notiiii1", " something something2");
        String title = remoteMessage.getData().get("title");
//            String mes = remoteMessage.getData().get("message");
        Log.e("notiiii1 title = ", title);
//            Log.e("notiiii2",mes);
//            String messageBody = remoteMessage.getNotification().getBody();
//            String imageHost = remoteMessage.getNotification().getImageUrl().getHost();
//            String imagePath = remoteMessage.getNotification().getImageUrl().getPath();
        String imageUrl = "";

            imageUrl = remoteMessage.getData().get("message");
            Log.e("notiiii1 image = ", imageUrl);


        // Display Notification
        sendNotification(title, "1234", imageUrl);

    }

    // Display Notification function

    private void sendNotification(String title, String messageBody, String imageUrl) {
        NotificationManagerCompat notificationManager;
        notificationManager = NotificationManagerCompat.from(this);
        RemoteViews collapsedView = new RemoteViews(getPackageName(),
                R.layout.notification_collapsed);
        @SuppressLint("RemoteViewLayout") RemoteViews expandedView = new RemoteViews(getPackageName(),
                R.layout.notification_expanded);
        Intent clickIntent = new Intent(this, NotificationReciever.class);
        PendingIntent clickPendingIntent = PendingIntent.getBroadcast(this,
                0, clickIntent, 0);
        collapsedView.setTextViewText(R.id.text_view_collapsed_1, title);
        expandedView.setTextViewText(R.id.text_view_expanded,title);
        try {
            URL url = new URL(imageUrl);

            InputStream in;
            //message = params[0] + params[1];

            //URL url = new URL(params[2]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            in = connection.getInputStream();
            Bitmap image = BitmapFactory.decodeStream(in);
//            Drawable d = new BitmapDrawable(getResources(), image);
            expandedView.setImageViewBitmap(R.id.image_view_expanded, image);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e("noti_error", String.valueOf(e));
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("noti_error", String.valueOf(e));
        }


        expandedView.setOnClickPendingIntent(R.id.image_view_expanded, clickPendingIntent);
        Notification notification = new NotificationCompat.Builder(this, "CHANNEL_1")
                .setSmallIcon(R.drawable.main_logo)
                .setCustomContentView(collapsedView)
                .setCustomBigContentView(expandedView)
//                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .build();
        notificationManager.notify(increment, notification);
        Log.e("noti_sended", String.valueOf("noti"));

    }
    @Override
    public void onMessageSent(String s) {
        super.onMessageSent(s);

        Log.i(TAG, "onMessageSent : " + s);
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
        Log.i(TAG, "onDeletedMessages()");
    }
}