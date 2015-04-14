package com.globallogic.zoo.models.dao;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.globallogic.zoo.helpers.ZooDatabaseHelper;
import com.globallogic.zoo.models.Show;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GL on 14/04/2015.
 */
public class ShowDB {

    public static final String TABLE_NAME = "Show";

    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_SCHEDULES = "schedules";
    public static final String KEY_DURATION = "duration";

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "" +
            " (" + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_NAME + " TEXT, " + KEY_SCHEDULES + " TEXT, " +
            "" + KEY_DURATION + " TEXT)";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static void insert(ZooDatabaseHelper dbHelper, Show show) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, show.getId());
        values.put(KEY_NAME, show.getName());
        values.put(KEY_SCHEDULES, show.getSchedulesString());
        values.put(KEY_DURATION, show.getDuration());

        db.insertOrThrow(TABLE_NAME, null, values);
        db.close();
    }

    public static Show getOrInsert(ZooDatabaseHelper dbHelper, Show show) {
        Show searchShow = get(dbHelper, show.getId());
        if (searchShow == null) {
            insert(dbHelper, show);
        }

        return show;
    }

    public static Show get(ZooDatabaseHelper dbHelper, long showId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME,
                new String[] {"*"},
                KEY_ID + " = ?",
                new String[] {String.valueOf(showId)}, null, null, null);


        Show show = null;
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            show = createShowFromCursor(cursor);
            cursor.close();
        }

        db.close();
        return show;
    }

    private static Show createShowFromCursor(Cursor cursor) {
        Show show;
        show = new Show(cursor.getLong(0), cursor.getString(1), cursor.getString(2),
                cursor.getString(3));
        cursor.close();
        return show;
    }

    public static List<Show> getAnimalShows(ZooDatabaseHelper dbHelper, long animalId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(ShowDB.TABLE_NAME + "." + ShowDB.KEY_ID + ", ");
        queryBuilder.append(ShowDB.TABLE_NAME + "." + ShowDB.KEY_NAME + ", ");
        queryBuilder.append(ShowDB.TABLE_NAME + "." + ShowDB.KEY_SCHEDULES + ", ");
        queryBuilder.append(ShowDB.TABLE_NAME + "." + ShowDB.KEY_DURATION);

        String args = queryBuilder.toString();
        String tables = ShowDB.TABLE_NAME + ", " + AnimalDB.TABLE_NAME + ", " + AnimalShowDB.TABLE_NAME;

        queryBuilder.setLength(0);
        queryBuilder.append(AnimalDB.TABLE_NAME + "." + AnimalDB.KEY_ID + " = " + String.valueOf(animalId) + " AND ");
        queryBuilder.append(AnimalDB.TABLE_NAME + "." + AnimalDB.KEY_ID + " = ");
        queryBuilder.append(AnimalShowDB.TABLE_NAME + "." + AnimalShowDB.KEY_ANIMAL + " AND ");
        queryBuilder.append(ShowDB.TABLE_NAME + "." + ShowDB.KEY_ID + " = ");
        queryBuilder.append(AnimalShowDB.TABLE_NAME + "." + AnimalShowDB.KEY_SHOW);

        String cond = queryBuilder.toString();
        String rawQuery = "SELECT " + args + " FROM " + tables + " WHERE " + cond;

        Cursor cursor = db.rawQuery(rawQuery, null);

        List<Show> shows = new ArrayList<>();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                shows.add(createShowFromCursor(cursor));
            }
            cursor.close();
        }

        db.close();
        return shows;
    }
}
