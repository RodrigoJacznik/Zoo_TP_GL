package com.globallogic.zoo.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.globallogic.zoo.activities.AnimalDetailsActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.Permission;

/**
 * Created by GL on 10/04/2015.
 */
public class HttpConnectionManager extends HttpURLConnection {

    public static final String GET = "GET";

    protected HttpConnectionManager(URL url, String method) throws ProtocolException {
        super(url);
        this.setConnectTimeout(10000);
        this.setReadTimeout(10000);
        this.setRequestMethod(method);
    }

    @Override
    public boolean usingProxy() {
        return false;
    }

    @Override
    public void disconnect() {

    }

    @Override
    public void connect() throws IOException {

    }

    public static boolean checkConnection(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        return (networkInfo != null && networkInfo.isConnected());
    }
}
