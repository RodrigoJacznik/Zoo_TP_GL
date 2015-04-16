package com.globallogic.zoo.network;

import android.content.Context;
import android.os.AsyncTask;

import com.globallogic.zoo.helpers.JsonParserHelper;
import com.globallogic.zoo.models.Animal;

import java.net.HttpURLConnection;
import java.util.List;

/**
 * Created by GL on 16/04/2015.
 */
public class API {

    public final static int NOT_FOUND = -1;

    public static final String LOGIN_URL = "http://rodjacznik.pythonanywhere.com/api/v1.0/login";
    public static final String ANIMALS_URL = "http://rodjacznik.pythonanywhere.com/api/v1.0/animals";
    public static final String SHOWS_URL = "http://rodjacznik.pythonanywhere.com/api/v1.0/shows";

    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String DELETE = "DELETE";
    public static final String UPDATE = "UPDATE";

    public interface OnRequestListListener<T> {
        public void onSuccess(List<T> list);
        public void onFail(int code);
    }

    public interface OnRequestObjectListener<T> {
        public void onSuccess(T t);
        public void onFail(int code);
    }

    public interface OnLoginRequestListener {
        public void onSuccess();
        public void onFail(int code);
    }

    public static void getAllAnimals(Context context, OnRequestListListener callback) {
        HttpConnectionHelper conn = new HttpConnectionHelper(context, ANIMALS_URL, GET);

        if (HttpConnectionHelper.checkConnection(context)) {
            new GetAnimalList(callback, conn).execute();
        } else {
            callback.onFail(HttpURLConnection.HTTP_CLIENT_TIMEOUT);
        }
    }

    public static void login(Context context, OnLoginRequestListener callback, String user, String pass) {
        HttpConnectionHelper conn = new HttpConnectionHelper(context, LOGIN_URL, GET);
        conn.setUserAndPassToHTTPHeader(user, pass);

        if (HttpConnectionHelper.checkConnection(context)) {
            new LoginTask(callback, conn).execute();
        } else {
            callback.onFail(HttpURLConnection.HTTP_CLIENT_TIMEOUT);
        }
    }

    static final class LoginTask extends AsyncTask<Void, Void, Boolean> {

        private HttpConnectionHelper conn;
        private OnLoginRequestListener callback;

        public LoginTask(OnLoginRequestListener onLoginRequestListener, HttpConnectionHelper conn) {
            this.conn = conn;
            this.callback = onLoginRequestListener;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            conn.connect();
            return conn.getResponseCode() == HttpURLConnection.HTTP_OK;
        }

        @Override
        protected void onPostExecute(Boolean bool) {
            if (bool) {
                callback.onSuccess();
            } else {
                callback.onFail(HttpURLConnection.HTTP_FORBIDDEN);
            }
        }
    }

    static final class GetAnimalList extends AsyncTask<Void, Void, List<Animal>> {

        private OnRequestListListener onRequestListListener;
        private HttpConnectionHelper conn;

        public GetAnimalList(OnRequestListListener callback, HttpConnectionHelper conn) {
            this.onRequestListListener = callback;
            this.conn = conn;
        }

        @Override
        protected List<Animal> doInBackground(Void... params) {
            conn.connect();
            List<Animal> animals = null;
            if (conn.getResponseCode() == 200) {
                String data = conn.getData();
                animals = JsonParserHelper.parseJson(data);
            }
            return animals;
        }

        @Override
        protected void onPostExecute(List<Animal> animals) {
            if (animals != null) {
                onRequestListListener.onSuccess(animals);
            } else {
                onRequestListListener.onFail(1);
            }
        }
    }
}
