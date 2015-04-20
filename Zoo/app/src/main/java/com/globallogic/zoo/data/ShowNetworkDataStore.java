package com.globallogic.zoo.data;

import android.content.Context;

import com.globallogic.zoo.models.Show;
import com.globallogic.zoo.network.API;

import java.util.List;

/**
 * Created by GL on 20/04/2015.
 */
public class ShowNetworkDataStore implements DataStore<Show, Long> {

    private Context appContext;

    public ShowNetworkDataStore(Context context) {
        this.appContext = context.getApplicationContext();
    }

    @Override
    public void create(Show show) {

    }

    @Override
    public Long update(Show show) {
        return null;
    }

    @Override
    public Long delete(Show show) {
        return null;
    }

    @Override
    public void getById(API.OnRequestObjectListener<Show> onRequestListListener, Long aLong) {

    }

    @Override
    public void getAll(API.OnRequestListListener<Show> onRequestListListener) {
        API.getAllShows(appContext, onRequestListListener);
    }

    @Override
    public void batchCreate(List<Show> list) {

    }
}
