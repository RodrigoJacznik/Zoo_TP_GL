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

    public interface OnRequestListListener<T> {
        public void onFail(int code);
        public void onSuccess(List<T> list);
    }

    private static final String LOG_TAG = "HttpConnectionManager";
    private static final String LOGIN_URL = "http://rodjacznik.pythonanywhere.com/api/v1.0/login";

    public static final String GET = "GET";

    private static final int CONNECT_TIMEOUT = 10000;
    private static final int READ_TIMEOUT = 10000;

    private static HttpURLConnection connection;

    private static void setupConnection(Context context, String anUrl, String method) {
        try {
            URL url = new URL(anUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", getEncodeUserAndPass(context));
            connection.setConnectTimeout(CONNECT_TIMEOUT);
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setRequestMethod(method);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        } catch (IOException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        }
    }

    private static String getEncodeUserAndPass(Context context) {
        String user = SharedPreferencesHelper.getUserName(context);
        String pass = SharedPreferencesHelper.getPass(context);
        return "Basic " + Base64.encodeToString((user + ":" + pass).getBytes(), Base64.DEFAULT);
    }

    private static String getEncodeUserAndPass(String user, String pass) {
        return "Basic " + Base64.encodeToString((user + ":" + pass).getBytes(), Base64.DEFAULT);
    }

    private static void connect() {
        try {
            connection.connect();
        } catch (SocketException e){
            Log.e(LOG_TAG, e.getMessage(), e);
        }catch (SocketTimeoutException e){
            Log.e(LOG_TAG, e.getMessage(), e);
        }
        catch (IOException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        }
    }

    public static int getResponseCode() {
        int code = -1;
        try {
            code = connection.getResponseCode();
        } catch (IOException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        }

        return code;
    }

    private static String getData() {
        InputStream is = getRawData();
        String data = convertStreamToString(is);
        return data;
    }

    private static InputStream getRawData() {
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

    public static void makeRequest(final Context context, final OnRequestListListener onRequestListListener,
                                     String url, String method) {

        setupConnection(context, url, method);
        if (checkConnection(context)) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    connect();
                    int response = getResponseCode();
                    if  (response == HttpURLConnection.HTTP_OK) {
                        onRequestListListener.onSuccess(getData());
                    } else {
                        onRequestListListener.onFail(response);
                    }
                }
            }).start();
        } else {
            onRequestListListener.onFail(HttpURLConnection.HTTP_CLIENT_TIMEOUT);
        }
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

    public static boolean login(Context context, String user, String pass) {
        setupConnection(context, LOGIN_URL, GET);
        connection.setRequestProperty("Authorization", getEncodeUserAndPass(user, pass));

        if (checkConnection(context)) {
            connect();
            if (getResponseCode() == HttpURLConnection.HTTP_OK) {
                return true;
            }
        }
        return false;
    }
}
