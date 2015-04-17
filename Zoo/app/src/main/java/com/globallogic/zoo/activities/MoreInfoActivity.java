package com.globallogic.zoo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.globallogic.zoo.R;


public class MoreInfoActivity extends BaseActivity {
    public final static String URL = "URL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info);

        setUpActionBar();
        callBrowser();
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    private void callBrowser() {
        Intent intent = getIntent();
        WebView wv = (WebView) findViewById(R.id.moreinfoactivity_webview);
        wv.setWebViewClient(new MyBrowser());
        wv.loadUrl(intent.getStringExtra(URL));
    }
}
