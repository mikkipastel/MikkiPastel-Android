package com.mikkipastel.blog.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.webkit.WebView;

import com.mikkipastel.blog.MainActivity;
import com.mikkipastel.blog.R;

public class SchemaActivity extends Activity {
    WebView mWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        mWebView = (WebView) findViewById(R.id.web_view);

        Uri uri = getIntent().getData();
        String url = uri.toString();

        setWebView(url);
    }

    private void setWebView(String url) {
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(url);
        mWebView.getTitle();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
