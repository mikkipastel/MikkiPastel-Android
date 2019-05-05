package com.mikkipastel.blog.service;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import com.mikkipastel.blog.R;

/**
 * Created by acer on 2/13/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    String url, title;

    CustomTabsServiceConnection mCustomTabsServiceConnection;
    CustomTabsClient mCustomTabsClient;
    CustomTabsSession mCustomTabsSession;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Handle FCM messages
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            //get key and value
            url = remoteMessage.getData().get("link_url");
            title = remoteMessage.getData().get("link_title");
            Log.d(TAG, title + " " + url);
            chromeCustomContent(title, url);
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    public void chromeCustomContent(String title, String url) {
        //open service
        mCustomTabsServiceConnection = new CustomTabsServiceConnection() {
            @Override
            public void onCustomTabsServiceConnected(ComponentName componentName, CustomTabsClient customTabsClient) {
                mCustomTabsClient = customTabsClient;
                mCustomTabsClient.warmup(0L);
                mCustomTabsSession = mCustomTabsClient.newSession(null);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                //mCustomTabsClient = null;
            }
        };
        mCustomTabsClient.bindCustomTabsService(getApplication(), "com.android.chrome", mCustomTabsServiceConnection);

        //Opening a Chrome Custom Tab
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();

        //set toolbar menu to primary color
        builder.setToolbarColor(getResources().getColor(R.color.colorPrimary));
        //set show title blog
        builder.setShowTitle(true);
        //set back icon (close button)
        builder.setCloseButtonIcon(BitmapFactory.decodeResource(
                getResources(), R.drawable.ic_arrow_back));
        //add share button in menu
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, title + " " + url);
        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, shareIntent, 0);
        builder.addMenuItem("Share", pi);
        //set animation
        builder.setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left);
        builder.setExitAnimations(this, android.R.anim.slide_in_left,
                android.R.anim.slide_out_right);
        //track traffic
        //shareIntent.putExtra(Intent.EXTRA_REFERRER,
        //        Uri.parse(Intent.URI_ANDROID_APP_SCHEME + "//" + this.getPackageName()));

        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(url));
    }
}
