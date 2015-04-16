package com.globallogic.zoo.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;
import android.util.Log;

import com.globallogic.zoo.helpers.SharedPreferencesHelper;
import com.globallogic.zoo.models.Animal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.List;

/**
 * Created by GL on 10/04/2015.
 */
public class HttpConnectionHelper {

    private static final String LOG_TAG = "HttpConnectionManager";

    private static final int CONNECT_TIMEOUT = 10000;
    private static final int READ_TIMEOUT = 10000;

    private HttpURLConnection connection;

    public HttpConnectionHelper(Context context, String url, String method) {
        this.connection = setupConnection(context, url, method);
    }

    private HttpURLConnection setupConnection(Context context, String anUrl, String method) {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(anUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", getEncodeUserAndPass(context));
            conn.setConnectTimeout(CONNECT_TIMEOUT);
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setRequestMethod(method);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        } catch (IOException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        }

        return conn;
    }

    private static String getEncodeUserAndPass(Context context) {
        String user = SharedPreferencesHelper.getUserName(context);
        String pass = SharedPreferencesHelper.getPass(context);
        return "Basic " + Base64.encodeToString((user + ":" + pass).getBytes(), Base64.DEFAULT);
    }

    private static String getEncodeUserAndPass(String user, String pass) {
        return "Basic " + Base64.encodeToString((user + ":" + pass).getBytes(), Base64.DEFAULT);
    }

    public void setUserAndPassToHTTPHeader(String user, String pass) {
        connection.setRequestProperty("Authorization", getEncodeUserAndPass(user, pass));
    }

    public void connect() {
        try {
            connection.connect();
        } catch (SocketException | SocketTimeoutException e){
            Log.e(LOG_TAG, e.getMessage(), e);
        } catch (IOException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        }
    }

    public int getResponseCode() {
        int code = -1;
        try {
            code = connection.getResponseCode();
        } catch (IOException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        }

        return code;
    }

    public String getData() {
        InputStream is = getRawData();
        String data = convertStreamToString(is);
        return data;
    }

    private InputStream getRawData() {
        InputStream is = null;
        try {
            is = connection.getInputStream();
        } catch (IOException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        }

        return is;
    }

    public static boolean checkConnection(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        return (networkInfo != null && networkInfo.isConnected());
    }

    public static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
