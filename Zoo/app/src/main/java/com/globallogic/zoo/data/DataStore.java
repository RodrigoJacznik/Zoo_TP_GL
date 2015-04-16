package com.globallogic.zoo.data;

import android.content.Context;

import com.globallogic.zoo.network.API;
import com.globallogic.zoo.network.HttpConnectionHelper;

/**
 * Created by GL on 16/04/2015.
 */
public interface DataStore<T, K> {

    public K create(Context context, T t);

    public K update(Context context, T t);

    public K delete(Context context, T t);

    public void getById(API.OnRequestObjectListener onRequestListListener, Context context, K k);

    public void getAll(API.OnRequestListListener onRequestListListener, Context context);
}
