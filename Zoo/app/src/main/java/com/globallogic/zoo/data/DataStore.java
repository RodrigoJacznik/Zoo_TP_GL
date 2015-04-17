package com.globallogic.zoo.data;

import android.content.Context;

import com.globallogic.zoo.network.API;
import com.globallogic.zoo.network.HttpConnectionHelper;

import java.util.List;

/**
 * Created by GL on 16/04/2015.
 */
public interface DataStore<T, K> {

    public void create(T t);

    public K update(T t);

    public K delete(T t);

    public void getById(API.OnRequestObjectListener<T> onRequestListListener, K k);

    public void getAll(API.OnRequestListListener<T> onRequestListListener);

    public void batchCreate(List<T> list);
}
