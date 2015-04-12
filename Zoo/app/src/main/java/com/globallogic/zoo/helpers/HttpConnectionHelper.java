package com.globallogic.zoo.helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;

/**
 * Created by GL on 10/04/2015.
 */
public class HttpConnectionHelper {

    private static final String ALL_ANIMALS_URL = "http://rodjacznik.pythonanywhere.com/api/v1.0/animals";
    public static final int ALL_ANIMALS = 0;

    private static final int CONNECT_TIMEOUT = 10000;
    private static final int READ_TIMEOUT = 10000;
    private HttpURLConnection connection;

    public static final String GET = "GET";

    public HttpConnectionHelper(String anUrl, String method) {
        try {
            URL url = new URL(anUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(CONNECT_TIMEOUT);
            connection.setReadTimeout(READ_TIMEOUT);
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
            json = convertStreamToString(is);
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

    public static String getAllAnimals() {
        HttpConnectionHelper conn =
                new HttpConnectionHelper(ALL_ANIMALS_URL, HttpConnectionHelper.GET);
        conn.connect();
        int response = conn.getResponseCode();
        switch (response) {
            case 200:
                return conn.getData();
            default:
                return null;
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
}
