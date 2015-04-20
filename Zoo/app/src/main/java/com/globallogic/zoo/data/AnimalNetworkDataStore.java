package com.globallogic.zoo.data;

import android.content.Context;

import com.globallogic.zoo.models.Animal;
import com.globallogic.zoo.network.API;

import java.util.List;

/**
 * Created by GL on 16/04/2015.
 */
public class AnimalNetworkDataStore implements DataStore<Animal, Long> {

    private Context context;

    public AnimalNetworkDataStore(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    public void create(Animal animal) {}

    @Override
    public Long update(Animal animal) {
        return null;
    }

    @Override
    public Long delete(Animal animal) {
        return null;
    }

    @Override
    public void getById(API.OnRequestObjectListener onRequestObjectListener, Long animalId) {
        API.getAnimal(context, onRequestObjectListener, animalId);
    }

    @Override
    public void getAll(API.OnRequestListListener onRequestListListener) {
        API.getAllAnimals(context, onRequestListListener);
    }

    @Override
    public void batchCreate(List<Animal> list) {

    }
}
