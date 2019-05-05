package com.mikkipastel.blog;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.mikkipastel.blog.DAO.Item;
import com.mikkipastel.blog.fragment.AboutAppFragment;
import com.mikkipastel.blog.fragment.ContentFragment;
import com.mikkipastel.blog.fragment.MainFragment;
import com.mikkipastel.blog.fragment.OfflineFragment;

public class MainActivity extends AppCompatActivity implements ContentFragment.FragmentListener {
    CustomTabsServiceConnection mCustomTabsServiceConnection;
    CustomTabsClient mCustomTabsClient;
    CustomTabsSession mCustomTabsSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            //1st created
            //place fragment
            if (checkOnline()) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.contentContainer, MainFragment.newInstance())
                        .commit();
            }
            else {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.contentContainer, OfflineFragment.newInstance())
                        .commit();
            }
        }

        SharedPreferences prefs = getSharedPreferences("data_install", MODE_PRIVATE);
        boolean insertBranch = prefs.getBoolean("install_status", false);
        if (insertBranch) {
            addShortcut();
        }

        mCustomTabsServiceConnection = new CustomTabsServiceConnection() {
            @Override
            public void onCustomTabsServiceConnected(ComponentName componentName, CustomTabsClient customTabsClient) {
                mCustomTabsClient = customTabsClient;
                mCustomTabsClient.warmup(0L);
                mCustomTabsSession = mCustomTabsClient.newSession(null);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
               // mCustomTabsClient = null;
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*if (item.getItemId() == R.id.action_settings) {
            Intent goSetting = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(goSetting);
        } */
        if (item.getItemId() == R.id.action_aboutapp) {
            showDialog();
        } else if (item.getItemId() == R.id.action_aboutme) {
            aboutMe();
        }

        return super.onOptionsItemSelected(item);
    }

    public void showDialog() {
        // Create and show the dialog.
        DialogFragment newFragment = AboutAppFragment.newInstance();
        newFragment.show(getSupportFragmentManager().beginTransaction(), "dialog");
    }

    public void aboutMe() {
        String url = "https://mikkipastel.firebaseapp.com/";

        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(getResources().getColor(R.color.colorPrimary));

        builder.setShowTitle(true);
        builder.setCloseButtonIcon(BitmapFactory.decodeResource(
                getResources(), R.drawable.ic_arrow_back));
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "About Mikkipastel " + url);
        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, shareIntent, 0);
        builder.addMenuItem("Share", pi);

        builder.setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left);
        builder.setExitAnimations(this, android.R.anim.slide_in_left,
                android.R.anim.slide_out_right);

        //shareIntent.putExtra(Intent.EXTRA_REFERRER,
        //        Uri.parse(Intent.URI_ANDROID_APP_SCHEME + "//" + this.getPackageName()));

        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(url));
    }

    @Override
    public void onListItemClick(Item dao, int pos) {

        //Opening a Chrome Custom Tab
        String url = dao.getUrl();
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
        shareIntent.putExtra(Intent.EXTRA_TEXT, dao.getTitle() + " " + dao.getUrl());
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

    public boolean checkOnline(){
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    private void addShortcut() {
        //Adding shortcut for MainActivity
        //on Home screen

        Intent shortcutIntent = new Intent(getApplicationContext(),MainActivity.class);

        shortcutIntent.setAction(Intent.ACTION_MAIN);

        Intent addIntent = new Intent();
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "MikkiPastel");
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.mipmap.ic_launcher));

        addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        getApplicationContext().sendBroadcast(addIntent);
    }
}
