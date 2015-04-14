package com.globallogic.zoo.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.globallogic.zoo.models.Animal;
import com.globallogic.zoo.models.dao.AnimalDB;
import com.globallogic.zoo.models.dao.AnimalShowDB;
import com.globallogic.zoo.models.dao.ShowDB;

import java.util.List;

/**
 * Created by GL on 14/04/2015.
 */

public class ZooDatabaseHelper extends SQLiteOpenHelper {


    private static final String DB_NAME = "ZooDatabase";
    private static final int VERSION = 1;

    public ZooDatabaseHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(AnimalDB.CREATE_TABLE);
        db.execSQL(ShowDB.CREATE_TABLE);
        db.execSQL(AnimalShowDB.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(AnimalDB.DROP_TABLE);
        db.execSQL(ShowDB.DROP_TABLE);
        db.execSQL(AnimalShowDB.DROP_TABLE);

        db.execSQL(AnimalDB.CREATE_TABLE);
        db.execSQL(ShowDB.CREATE_TABLE);
        db.execSQL(AnimalShowDB.CREATE_TABLE);
    }

    public void insertAnimal(Animal animal) {
        AnimalDB.getOrInsert(this, animal);
    }

    public Animal getAnimalById(long animalId) {
        return AnimalDB.get(this, animalId);
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
}
