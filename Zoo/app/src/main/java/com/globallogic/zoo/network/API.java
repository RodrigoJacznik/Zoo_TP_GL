package com.globallogic.zoo.network;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.globallogic.zoo.helpers.JsonParserHelper;
import com.globallogic.zoo.models.Animal;
import com.globallogic.zoo.models.Show;

import org.json.JSONException;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class API {

    public final static String TAG = "API";
    public final static int NOT_FOUND = -1;

    public static final String LOGIN_URL = "http://rodjacznik.pythonanywhere.com/api/v1.0/login/";
    public static final String ANIMALS_URL = "http://rodjacznik.pythonanywhere.com/api/v1.0/animals/";
    public static final String SHOWS_URL = "https://dl.dropboxusercontent.com/u/48706064/attraction.json";

    public static final String GET = "GET";

    private static final Map<String, String> etags = new HashMap<>();

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

    public static void getAnimal(Context context, OnRequestObjectListener callback, Long animalId) {
        HttpConnectionHelper conn = new HttpConnectionHelper(context, getAnimalURL(animalId), GET);

        if (HttpConnectionHelper.checkConnection(context)) {
            new GetAnimal(callback, conn).execute();
        } else {
            callback.onFail(HttpURLConnection.HTTP_CLIENT_TIMEOUT);
        }
    }

    public static void getAllShows(Context context, OnRequestListListener<Show> callback) {
        HttpConnectionHelper conn = new HttpConnectionHelper(context, SHOWS_URL, GET);

        if (HttpConnectionHelper.checkConnection(context)) {
            new GetShowList(callback, conn).execute();
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

    private static String getAnimalURL(long animalId) {
        return ANIMALS_URL + animalId;
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
        private int responseCode;

        public GetAnimalList(OnRequestListListener callback, HttpConnectionHelper conn) {
            this.onRequestListListener = callback;
            this.conn = conn;
        }

        @Override
        protected List<Animal> doInBackground(Void... params) {
            conn.connect();
            List<Animal> animals = null;
            responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                String data = conn.getData();
                animals = JsonParserHelper.parseAnimalListJson(data);
            }
            return animals;
        }

        @Override
        protected void onPostExecute(List<Animal> animals) {
            if (animals != null) {
                onRequestListListener.onSuccess(animals);
            } else {
                onRequestListListener.onFail(responseCode);
            }
        }
    }

    static final class GetAnimal extends AsyncTask<Void, Void, Animal> {

        private OnRequestObjectListener onRequestObjectListener;
        private HttpConnectionHelper conn;
        private int responseCode;

        public GetAnimal(OnRequestObjectListener callback, HttpConnectionHelper conn) {
            this.onRequestObjectListener = callback;
            this.conn = conn;
        }

        @Override
        protected Animal doInBackground(Void... params) {
            String url = conn.getUrl();
            if (etags.containsKey(url)) {
                String etag = etags.get(url);
                conn.setHeader(HttpConnectionHelper.IF_NONE_MATCH, etag);
            }
            conn.connect();
            Animal animal = null;
            responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                String data = conn.getData();
                try {
                    animal = Animal.fromJson(data);
                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage(), e);
                }
                etags.put(url, conn.getHeader(HttpConnectionHelper.ETAG));
            }
            return animal;
        }

        @Override
        protected void onPostExecute(Animal animal) {
            if (animal != null) {
                onRequestObjectListener.onSuccess(animal);
            } else {
                onRequestObjectListener.onFail(responseCode);
            }
        }
    }

    static final class GetShowList extends AsyncTask<Void, Void, List<Show>> {

        private HttpConnectionHelper conn;
        private OnRequestListListener<Show> callback;
        private int responseCode;

        public GetShowList(OnRequestListListener<Show> callback, HttpConnectionHelper conn) {
            this.conn = conn;
            this.callback = callback;
        }

        @Override
        protected List<Show> doInBackground(Void... params) {
            conn.connect();
            List<Show> shows = null;
            responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                String jShows = conn.getData();
                shows = JsonParserHelper.parseShowListJson(jShows);
            }
            return shows;
        }

        @Override
        protected void onPostExecute(List<Show> shows) {
            if (shows != null) {
                callback.onSuccess(shows);
            } else {
                callback.onFail(API.NOT_FOUND);
            }
        }
    }
}
