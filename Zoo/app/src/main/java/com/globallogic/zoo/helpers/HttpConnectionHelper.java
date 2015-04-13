package com.globallogic.zoo.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
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

    private static final String LOGIN_URL = "http://rodjacznik.pythonanywhere.com/api/v1.0/login";
    public static final int LOGIN = 1;

    public static final String GET = "GET";

    private static String user = "";
    private static String pass = "";
    private static final int CONNECT_TIMEOUT = 10000;
    private static final int READ_TIMEOUT = 10000;
    private HttpURLConnection connection;



    public HttpConnectionHelper(String anUrl, String method) {
        try {
            URL url = new URL(anUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", getEncodeUserAndPass());
            connection.setConnectTimeout(CONNECT_TIMEOUT);
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setRequestMethod(method);
        } catch (MalformedURLException e) {
            Log.e("HttpConnectionManager", e.getMessage(), e);
        } catch (IOException e) {
            Log.e("HttpConnectionManager", e.getMessage(), e);
        }
    }

    private String getEncodeUserAndPass() {
        return "Basic " + Base64.encodeToString((user + ":" + pass).getBytes(), Base64.DEFAULT);
    }

    public void connect() {
        try {
            connection.connect();
        } catch (SocketException e){
            Log.e("HttpConnectionManager", e.getMessage(), e);
        }catch (SocketTimeoutException e){
            Log.e("HttpConnectionManager", e.getMessage(), e);
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
        InputStream is = getRawData();
        String data = convertStreamToString(is);
        return data;
    }

    public InputStream getRawData() {
        InputStream is = null;
        try {
            is = connection.getInputStream();
        } catch (IOException e) {
            Log.e("HttpConnectionManager", e.getMessage(), e);
        }

        return is;
    }

    public static boolean checkConnection(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        return (networkInfo != null && networkInfo.isConnected());
    }

    public static String getAllAnimals(Context context) {
        HttpConnectionHelper conn = new HttpConnectionHelper(ALL_ANIMALS_URL, GET);
        if (checkConnection(context)) {
            conn.connect();
            int response = conn.getResponseCode();
            if  (response == 200) {
                return conn.getData();
            } else {
                return null;
            }
        } else {
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

    public static boolean login(Context context, String user, String pass) {
        HttpConnectionHelper conn = new HttpConnectionHelper(LOGIN_URL, GET);
        conn.setUser(user);
        conn.setPass(pass);
        conn.connection.setRequestProperty("Authorization", conn.getEncodeUserAndPass());

        if (checkConnection(context)) {
            conn.connect();
            if (conn.getResponseCode() == 200) {
                return true;
            }
        }
        return false;
    }

    public static Bitmap fetchImg(Context context, String url) {
        HttpConnectionHelper conn = new HttpConnectionHelper(url, GET);
        Bitmap img = null;
        if (checkConnection(context)) {
            conn.connect();
            if (conn.getResponseCode() == 200) {
                InputStream is = conn.getRawData();
                img = BitmapFactory.decodeStream(is);
            }
        }

        return img;
    }

    public static void setPass(String pass) {
        HttpConnectionHelper.pass = pass;
    }

    public static void setUser(String user) {
        HttpConnectionHelper.user = user;
    }


}
