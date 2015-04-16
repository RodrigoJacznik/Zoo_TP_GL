package com.globallogic.zoo.data;

import android.content.Context;

import com.globallogic.zoo.models.Animal;
import com.globallogic.zoo.network.API;

/**
 * Created by GL on 16/04/2015.
 */
public class AnimalNetworkDataStore implements DataStore<Animal, Long> {


    @Override
    public Long create(Context context, Animal animal) {
        return null;
    }

    @Override
    public Long update(Context context, Animal animal) {
        return null;
    }

    @Override
    public Long delete(Context context, Animal animal) {
        return null;
    }

    @Override
    public void getById(API.OnRequestObjectListener onRequestListListener, Context context, Long aLong) {

    }

    @Override
    public void getAll(API.OnRequestListListener onRequestListListener, Context context) {
        API.getAllAnimals(context, onRequestListListener);
    }
}
