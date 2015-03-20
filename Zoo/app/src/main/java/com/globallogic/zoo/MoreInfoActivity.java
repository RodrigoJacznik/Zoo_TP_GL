package com.globallogic.zoo;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;


public class MoreInfoActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info);
        Intent intent = getIntent();

        WebView wv = (WebView) findViewById(R.id.moreinfoactivity_webview);
        wv.setWebViewClient(new MyBrowser());
        wv.loadUrl("http://es.wikipedia.org/wiki/" + intent.getStringExtra(AnimalDetailsActivity.NOMBRE));
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
