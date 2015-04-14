package com.globallogic.zoo.models.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.globallogic.zoo.helpers.ZooDatabaseHelper;
import com.globallogic.zoo.models.Animal;
import com.globallogic.zoo.models.Show;

import java.util.List;

/**
 * Created by GL on 14/04/2015.
 */
public class AnimalDB {

    public static final String TABLE_NAME = "Animal";

    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_SPECIE = "specie";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_MORE_INFO = "more_info";

    public static final int KEY_ID_INDEX = 0;
    public static final int KEY_NAME_INDEX = 1;
    public static final int KEY_SPECIE_INDEX = 2;
    public static final int KEY_DESCRIPTION_INDEX = 3;
    public static final int KEY_IMAGE_INDEX = 4;
    public static final int KEY_MORE_INFO_INDEX = 5;

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "" +
            " ( " + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_NAME + " TEXT, " + KEY_SPECIE + " TEXT, " +
            "" + KEY_DESCRIPTION + " TEXT, " + KEY_IMAGE + " TEXT, " + KEY_MORE_INFO + " TEXT)";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS" + TABLE_NAME;

    public static void insert(ZooDatabaseHelper dbHelper, Animal animal) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, animal.getId());
        values.put(KEY_NAME, animal.getName());
        values.put(KEY_SPECIE, animal.getSpecie());
        values.put(KEY_DESCRIPTION, animal.getDescripcion());
        values.put(KEY_IMAGE, animal.getImage());
        values.put(KEY_MORE_INFO, animal.getMoreInfo());
        db.insertOrThrow(TABLE_NAME, null, values);
        db.close();

        for (Show animal_show: animal.getShows()) {
            Show show = ShowDB.getOrInsert(dbHelper, animal_show);
            AnimalShowDB.insertItem(dbHelper, animal.getId(), show.getId());
        }
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
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            animal = new Animal(cursor.getLong(KEY_ID_INDEX), cursor.getString(KEY_NAME_INDEX),
                    cursor.getString(KEY_SPECIE_INDEX), cursor.getString(KEY_DESCRIPTION_INDEX),
                    cursor.getString(KEY_IMAGE_INDEX), cursor.getString(KEY_MORE_INFO_INDEX));
            cursor.close();

            List<Show> shows = ShowDB.getAnimalShows(dbHelper, animalId);
            animal.setShows(shows);
        }

        db.close();
        return animal;
    }
}