package com.globallogic.zoo.data;

import android.content.Context;

import com.globallogic.zoo.models.Animal;
import com.globallogic.zoo.network.API;
import com.globallogic.zoo.helpers.ZooDatabaseHelper;
import com.globallogic.zoo.data.dao.AnimalDAO;

import java.util.List;

public class AnimalSQLiteDataStore implements DataStore<Animal, Long> {

    private ZooDatabaseHelper db;

    public AnimalSQLiteDataStore(Context context) {
        db = new ZooDatabaseHelper(context);
    }

    @Override
    public void create(Animal animal) {
        AnimalDAO.insertOrUpdate(db, animal);
    }

    @Override
    public Long update(Animal animal) {
        return null;
    }

    @Override
    public Long delete(Animal animal) {
        return null;
    }

    @Override
    public void getById(API.OnRequestObjectListener<Animal> onRequestObjectListener, Long animalId) {

        Animal animal = AnimalDAO.get(db, animalId);
        if (animal != null) {
            onRequestObjectListener.onSuccess(animal);
        } else {
            onRequestObjectListener.onFail(API.NOT_FOUND);
        }
    }

    @Override
    public void getAll(API.OnRequestListListener<Animal> onRequestListListener) {
        List<Animal> animals = AnimalDAO.getAll(db);

        if (! animals.isEmpty()) {
            onRequestListListener.onSuccess(AnimalDAO.getAll(db));
        } else {
            onRequestListListener.onFail(API.NOT_FOUND);
        }
    }

    public void batchCreate(List<Animal> animals) {
        AnimalDAO.insertAnimals(db, animals);
    }
}
