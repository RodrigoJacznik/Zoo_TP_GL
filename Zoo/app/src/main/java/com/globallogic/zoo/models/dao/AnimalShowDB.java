package com.globallogic.zoo.models.dao;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.globallogic.zoo.helpers.ZooDatabaseHelper;

/**
 * Created by GL on 14/04/2015.
 */
public class AnimalShowDB {

    public static final String TABLE_NAME = AnimalDB.TABLE_NAME + "_" + ShowDB.TABLE_NAME;

    public static final String KEY_ANIMAL = AnimalDB.TABLE_NAME + "_" + AnimalDB.KEY_ID;
    public static final String KEY_SHOW = ShowDB.TABLE_NAME + "_" + ShowDB.KEY_ID;

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" +
            KEY_ANIMAL + " INTEGER NOT NULL, " + KEY_SHOW + " INTEGER NOT NULL, " +
            "PRIMARY KEY (" + KEY_ANIMAL + ", " + KEY_SHOW + " ) " +
            "FOREIGN KEY (" + KEY_ANIMAL + ") REFERENCES " + AnimalDB.TABLE_NAME + "(" + AnimalDB.KEY_ID + ")" +
            "FOREIGN KEY (" + KEY_SHOW + ") REFERENCES " + ShowDB.TABLE_NAME + "(" + ShowDB.KEY_ID + "))";

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
