package com.globallogic.zoo.models.dao;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.globallogic.zoo.helpers.ZooDatabaseHelper;

/**
 * Created by GL on 14/04/2015.
 */
public class AnimalShowDAO {

    public static final String TABLE_NAME = AnimalDAO.TABLE_NAME + "_" + ShowDAO.TABLE_NAME;

    public static final String KEY_ANIMAL = AnimalDAO.TABLE_NAME + "_" + AnimalDAO.KEY_ID;
    public static final String KEY_SHOW = ShowDAO.TABLE_NAME + "_" + ShowDAO.KEY_ID;

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" +
            KEY_ANIMAL + " INTEGER NOT NULL, " + KEY_SHOW + " INTEGER NOT NULL, " +
            "PRIMARY KEY (" + KEY_ANIMAL + ", " + KEY_SHOW + " ) " +
            "FOREIGN KEY (" + KEY_ANIMAL + ") REFERENCES " + AnimalDAO.TABLE_NAME + "(" + AnimalDAO.KEY_ID + ")" +
            "FOREIGN KEY (" + KEY_SHOW + ") REFERENCES " + ShowDAO.TABLE_NAME + "(" + ShowDAO.KEY_ID + "))";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;


    public static void insertItem(ZooDatabaseHelper dbHelper, long animal_id, long show_id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ANIMAL, animal_id);
        values.put(KEY_SHOW, show_id);
        db.insertOrThrow(TABLE_NAME, null, values);

        db.close();
    }
}
