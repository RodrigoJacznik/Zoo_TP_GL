package com.globallogic.zoo.data.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.globallogic.zoo.helpers.ZooDatabaseHelper;
import com.globallogic.zoo.models.Animal;
import com.globallogic.zoo.models.Show;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GL on 14/04/2015.
 */
public class AnimalDAO {

    public static final String TABLE_NAME = "Animal";

    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_SPECIE = "specie";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_MORE_INFO = "more_info";
    public static final String KEY_IS_FAVORITE = "is_favorite";

    public static final int KEY_ID_INDEX = 0;
    public static final int KEY_NAME_INDEX = 1;
    public static final int KEY_SPECIE_INDEX = 2;
    public static final int KEY_DESCRIPTION_INDEX = 3;
    public static final int KEY_IMAGE_INDEX = 4;
    public static final int KEY_MORE_INFO_INDEX = 5;
    public static final int KEY_IS_FAVORITE_INDEX = 6;

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "" +
            " ( " + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_NAME + " TEXT, " + KEY_SPECIE + " TEXT, " +
            "" + KEY_DESCRIPTION + " TEXT, " + KEY_IMAGE + " TEXT, " + KEY_MORE_INFO + " TEXT, " +
            "" + KEY_IS_FAVORITE + " INTEGER)";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static void insert(ZooDatabaseHelper dbHelper, Animal animal) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = getAnimalContentValues(animal);
        db.insertOrThrow(TABLE_NAME, null, values);
        db.close();

        for (Show animal_show: animal.getShows()) {
            Show show = ShowDAO.getOrInsert(dbHelper, animal_show);
            AnimalShowDAO.insertItem(dbHelper, animal.getId(), show.getId());
        }
    }

    private static ContentValues getAnimalContentValues(Animal animal) {
        ContentValues values = new ContentValues();
        values.put(KEY_ID, animal.getId());
        values.put(KEY_NAME, animal.getName());
        values.put(KEY_SPECIE, animal.getSpecie());
        values.put(KEY_DESCRIPTION, animal.getDescripcion());
        values.put(KEY_IMAGE, animal.getImage());
        values.put(KEY_MORE_INFO, animal.getMoreInfo());
        values.put(KEY_IS_FAVORITE, animal.isFavorite());
        return values;
    }

    public static Animal getOrInsert(ZooDatabaseHelper dbHelper, Animal animal) {
        if (get(dbHelper, animal.getId()) == null) {
            insert(dbHelper, animal);
        }

        return animal;
    }

    public static Animal get(ZooDatabaseHelper dbHelper, long animalId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,
                new String[] {"*"},
                KEY_ID + " = ?", new String[] {String.valueOf(animalId)}, null, null, null);

        Animal animal = null;
        if (cursor.moveToFirst()) {
            animal = getAnimalFromCursor(dbHelper, cursor);
        }

        cursor.close();
        db.close();
        return animal;
    }

    private static Animal getAnimalFromCursor(ZooDatabaseHelper dbHelper, Cursor cursor) {
        long animalId = cursor.getLong(KEY_ID_INDEX);

        Animal animal = new Animal(animalId, cursor.getString(KEY_NAME_INDEX),
                cursor.getString(KEY_SPECIE_INDEX), cursor.getString(KEY_DESCRIPTION_INDEX),
                cursor.getString(KEY_IMAGE_INDEX), cursor.getString(KEY_MORE_INFO_INDEX),
                cursor.getInt(KEY_IS_FAVORITE_INDEX));

        List<Show> shows = ShowDAO.getAnimalShows(dbHelper, animalId);
        animal.setShows(shows);
        return animal;
    }

    public static void insertOrUpdate(ZooDatabaseHelper dbHelper, Animal animal) {
        if (get(dbHelper, animal.getId()) != null) {
            update(dbHelper, animal);
        } else {
            insert(dbHelper, animal);
        }
    }

    public static void update(ZooDatabaseHelper dbHelper, Animal animal) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = getAnimalContentValues(animal);

        String[] args = new String[] {String.valueOf(animal.getId())};

        db.update(TABLE_NAME, contentValues, KEY_ID + " = ?", args);
        db.close();
    }

    public static List<Animal> getAll(ZooDatabaseHelper dbHelper) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[] {"*"}, null, null, null, null, null);

        List<Animal> animals = new ArrayList<>();

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                animals.add(getAnimalFromCursor(dbHelper, cursor));
            }
            cursor.close();
        }
        db.close();
        return animals;
    }
}