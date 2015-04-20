package com.globallogic.zoo.data;

import android.content.Context;

import com.globallogic.zoo.data.dao.ShowDAO;
import com.globallogic.zoo.helpers.ZooDatabaseHelper;
import com.globallogic.zoo.models.Show;
import com.globallogic.zoo.network.API;

import java.util.List;

public class ShowSQLiteDataStore implements DataStore<Show, Long> {

    ZooDatabaseHelper dbHelper;

    public ShowSQLiteDataStore(Context context) {
        dbHelper = new ZooDatabaseHelper(context);
    }

    @Override
    public void create(Show show) {
        ShowDAO.insertOrUpdate(dbHelper, show);
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
        List<Show> shows = ShowDAO.getAll(dbHelper);
        if (shows != null) {
            onRequestListListener.onSuccess(shows);
        } else {
            onRequestListListener.onFail(API.NOT_FOUND);
        }
    }

    @Override
    public void batchCreate(List<Show> shows) {
        for (Show show : shows) {
            create(show);
        }
    }
}
