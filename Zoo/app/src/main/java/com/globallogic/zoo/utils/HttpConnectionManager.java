package com.globallogic.zoo.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;

/**
 * Created by GL on 10/04/2015.
 */
public class HttpConnectionManager {

    private HttpURLConnection connection;

    public static final String GET = "GET";

    public HttpConnectionManager(String anUrl, String method) {
        try {
            URL url = new URL(anUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            connection.setRequestMethod(method);
        } catch (MalformedURLException e) {
            Log.e("HttpConnectionManager", e.getMessage(), e);
        } catch (IOException e) {
            Log.e("HttpConnectionManager", e.getMessage(), e);
        }
    }

    public void connect() {
        try {
            connection.connect();
        } catch (SocketException e){

        }catch (SocketTimeoutException e){

        }
        catch (IOException e) {
            Log.e("HttpConnectionManager", e.getMessage(), e);
        }
    }

    public int getResponseCode() {
        int code = -1;
        try {
            code = connection.getResponseCode();
        } catch (IOException e) {
            Log.e("HttpConnectionManager", e.getMessage(), e);
        }

        return code;
    }

    public String getData() {
        InputStream is;
        String json = null;
        try {
            is = connection.getInputStream();
            json = JsonParser.convertStreamToString(is);
            is.close();
        } catch (IOException e) {
            Log.e("HttpConnectionManager", e.getMessage(), e);
        }

        return json;
    }

    public static boolean checkConnection(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        return (networkInfo != null && networkInfo.isConnected());
    }
}
