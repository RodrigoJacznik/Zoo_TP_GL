package com.globallogic.zoo.data;

import android.content.Context;

import com.globallogic.zoo.network.API;
import com.globallogic.zoo.network.HttpConnectionHelper;

/**
 * Created by GL on 16/04/2015.
 */
public class AnimalRepository {

    private static DataStore initDataStore(Context context) {
        DataStore dataStore;
        if (HttpConnectionHelper.checkConnection(context)) {
            dataStore = new AnimalNetworkDataStore();
        } else {
            dataStore = new AnimalSQLiteDataStore();
        }

        return dataStore;
    }

    public static void getAllAnimals(API.OnRequestListListener onRequestListListener,
                                     Context context) {

        DataStore dataStore = initDataStore(context);
        dataStore.getAll(onRequestListListener, context);
    }

    public static void getAnimalById(API.OnRequestObjectListener onRequestObjectListener,
                                     Context context, long animalId) {

        DataStore dataStore = new AnimalSQLiteDataStore();
        dataStore.getById(onRequestObjectListener, context, animalId);
    }

}
