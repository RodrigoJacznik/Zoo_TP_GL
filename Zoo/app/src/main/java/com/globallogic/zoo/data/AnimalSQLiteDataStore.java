package com.globallogic.zoo.data;

import android.content.Context;

import com.globallogic.zoo.models.Animal;
import com.globallogic.zoo.network.API;
import com.globallogic.zoo.helpers.ZooDatabaseHelper;
import com.globallogic.zoo.data.dao.AnimalDAO;

import java.util.List;

public class AnimalSQLiteDataStore implements DataStore<Animal, Long> {



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
    public void getById(API.OnRequestObjectListener onRequestObjectListener,
                        Context context, Long animalId) {

        ZooDatabaseHelper db = new ZooDatabaseHelper(context);
        Animal animal = AnimalDAO.get(db, animalId);
        if (animal != null) {
            onRequestObjectListener.onSuccess(animal);
        } else {
            onRequestObjectListener.onFail(API.NOT_FOUND);
        }
    }

    @Override
    public void getAll(API.OnRequestListListener onRequestListListener, Context context) {
        ZooDatabaseHelper db = new ZooDatabaseHelper(context);
        List<Animal> animals = AnimalDAO.getAll(db);

        if (! animals.isEmpty()) {
            onRequestListListener.onSuccess(AnimalDAO.getAll(db));
        } else {
            onRequestListListener.onFail(API.NOT_FOUND);
        }
    }
}
