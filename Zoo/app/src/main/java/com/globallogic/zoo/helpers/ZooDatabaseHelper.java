package com.globallogic.zoo.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.globallogic.zoo.models.Animal;
import com.globallogic.zoo.models.dao.AnimalDAO;
import com.globallogic.zoo.models.dao.AnimalShowDAO;
import com.globallogic.zoo.models.dao.ShowDAO;
import com.globallogic.zoo.models.dao.UserDAO;

import java.util.List;

/**
 * Created by GL on 14/04/2015.
 */

public class ZooDatabaseHelper extends SQLiteOpenHelper {


    private static final String DB_NAME = "ZooDatabase";
    private static final int VERSION = 2;

    public ZooDatabaseHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(AnimalDAO.CREATE_TABLE);
        db.execSQL(ShowDAO.CREATE_TABLE);
        db.execSQL(AnimalShowDAO.CREATE_TABLE);
        db.execSQL(UserDAO.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(AnimalDAO.DROP_TABLE);
        db.execSQL(ShowDAO.DROP_TABLE);
        db.execSQL(AnimalShowDAO.DROP_TABLE);
        db.execSQL(UserDAO.DROP_TABLE);

        db.execSQL(AnimalDAO.CREATE_TABLE);
        db.execSQL(ShowDAO.CREATE_TABLE);
        db.execSQL(AnimalShowDAO.CREATE_TABLE);
        db.execSQL(UserDAO.CREATE_TABLE);
    }

    public void insertAnimal(Animal animal) {
        AnimalDAO.getOrInsert(this, animal);
    }

    public void insertOrUpdateAnimal(Animal animal) { AnimalDAO.insertOrUpdate(this, animal); }

    public Animal getAnimalById(long animalId) {
        return AnimalDAO.get(this, animalId);
    }

    public void insertAnimals(final List<Animal> animals) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (Animal animal : animals) {
                    insertAnimal(animal);
                }
            }
        });

        thread.start();
    }

    public List<Animal> getAnimals() {
        return AnimalDAO.getAll(this);
    }

    public void insertOrUpdateUser(String name) {
        UserDAO.insertOrUpdate(this, name);
    }
}
