package com.globallogic.zoo.data;

import android.content.Context;

import com.globallogic.zoo.models.Show;
import com.globallogic.zoo.network.API;
import com.globallogic.zoo.network.HttpConnectionHelper;

import java.util.List;


public class ShowRepository implements
        API.OnRequestObjectListener<Show>,
        API.OnRequestListListener<Show> {

    public enum Request {ALL, ONE, INSERT}

    public enum Access {DB, NETWORK}

    private Context appContext;
    private API.OnRequestListListener<Show> onRequestListListener;
    private API.OnRequestObjectListener<Show> onRequestObjectListener;
    private DataStore<Show, Long> dataStore;
    private Request request;

    public ShowRepository(Context context, API.OnRequestListListener<Show> onRequestListListener) {
        this.appContext = context.getApplicationContext();
        this.onRequestListListener = onRequestListListener;
    }

    public ShowRepository(Context context, API.OnRequestObjectListener<Show> onRequestObjectListener) {
        this.appContext = context.getApplicationContext();
        this.onRequestObjectListener = onRequestObjectListener;
    }

    private void initDataStore(Access access) {
        if (access == null) {
            access = Access.NETWORK;
        }
        switch (access) {
            default:
            case NETWORK:
                if (HttpConnectionHelper.checkConnection(appContext)) {
                    dataStore = new ShowNetworkDataStore(appContext);
                    break;
                }
            case DB:
                dataStore = new ShowSQLiteDataStore(appContext);
                break;
        }
    }

    public void getAllShows(Access access) {
        initDataStore(access);
        dataStore.getAll(onRequestListListener);
        request = Request.ALL;
    }

    public void getShow(long showId, Access access) {
        initDataStore(Access.DB);
        dataStore.getById(this, showId);
        request = Request.ONE;
    }

    @Override
    public void onSuccess(List<Show> shows) {
        if (dataStore instanceof ShowNetworkDataStore) {
            updateDB(shows);
        } else {
            onRequestListListener.onSuccess(shows);
        }
    }

    @Override
    public void onSuccess(Show show) {
        // TODO: update db
        onRequestObjectListener.onSuccess(show);
    }

    @Override
    public void onFail(int code) {
        onRequestListListener.onFail(code);
    }

    private void updateDB(List<Show> shows) {
        DataStore<Show, Long> sqlDataStore = new ShowSQLiteDataStore(appContext);
        sqlDataStore.batchCreate(shows);
    }
}
