package com.globallogic.zoo.data;

import android.content.Context;
import android.util.Log;

import com.globallogic.zoo.models.Animal;
import com.globallogic.zoo.network.API;
import com.globallogic.zoo.network.HttpConnectionHelper;

import java.net.HttpURLConnection;
import java.util.List;

public class AnimalRepository implements
        API.OnRequestListListener<Animal>,
        API.OnRequestObjectListener<Animal> {

    private String TAG = "AnimalRepository";

    public enum Access {DB, NETWORK}

    public enum Request {ALL, ONE, INSERT}

    private API.OnRequestListListener<Animal> onRequestListListener;
    private API.OnRequestObjectListener<Animal> onRequestObjectListener;
    private DataStore<Animal, Long> dataStore;
    private Context context;
    private Request request;
    private long animalId;

    public AnimalRepository(Context context) {
        this.context = context.getApplicationContext();
    }

    public AnimalRepository(Context context, API.OnRequestListListener<Animal> onRequestListListener) {
        this(context);
        this.onRequestListListener = onRequestListListener;
    }

    public AnimalRepository(Context context, API.OnRequestObjectListener<Animal> onRequestObjectListener) {
        this(context);
        this.onRequestObjectListener = onRequestObjectListener;
    }

    private void initDataStore(Context context, Access access) {
        if (access == null) {
            access = Access.NETWORK;
        }
        switch (access) {
            default:
            case NETWORK:
                if (HttpConnectionHelper.checkConnection(context)) {
                    dataStore = new AnimalNetworkDataStore(context);
                    break;
                }
            case DB:
                dataStore = new AnimalSQLiteDataStore(context);
        }
    }

    public void getAllAnimals(Access access) {
        initDataStore(context, access);
        request = Request.ALL;
        dataStore.getAll(this);
    }

    public void getAnimalById(long animalId, Access access) {
        initDataStore(context, access);
        request = Request.ONE;
        this.animalId = animalId;
        dataStore.getById(this, animalId);
    }

    public void insertAnimal(Animal animal, Access access) {
        initDataStore(context, access);
        request = Request.INSERT;
        dataStore.create(animal);
    }

    @Override
    public void onFail(int code) {
        if (code == HttpURLConnection.HTTP_NOT_MODIFIED) {
            resendNetworkRequestToDatabase();
        }
        if (onRequestListListener != null) {
            onRequestListListener.onFail(code);
        } else if (onRequestObjectListener != null) {
            onRequestObjectListener.onFail(code);
        }
    }

    private void resendNetworkRequestToDatabase() {
        Log.d(TAG, "not-modified, resend to db");
        switch (request) {
            case ONE:
                getAnimalById(animalId, Access.DB);
                break;
            case ALL:
                getAllAnimals(Access.DB);
                break;
            default:
                break;
        }
    }

    @Override
    public void onSuccess(Animal animal) {
        if (dataStore instanceof AnimalNetworkDataStore) {
            updateDB(animal);
        }
        onRequestObjectListener.onSuccess(animal);
    }

    @Override
    public void onSuccess(List<Animal> animals) {
        if (dataStore instanceof AnimalNetworkDataStore) {
            updateDB(animals);
        }
        onRequestListListener.onSuccess(animals);
    }

    private void updateDB(Animal animal) {
        DataStore<Animal, Long> sqlDataStore = new AnimalSQLiteDataStore(context);
        sqlDataStore.create(animal);
    }

    private void updateDB(List<Animal> animals) {
        DataStore<Animal, Long> sqlDataStore = new AnimalSQLiteDataStore(context);
        sqlDataStore.batchCreate(animals);
    }
}
