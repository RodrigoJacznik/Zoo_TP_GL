package com.globallogic.zoo.data.dao;


import android.app.ActionBar;
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
public class ShowDAO {

    public static final String TABLE_NAME = "Show";

    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_SCHEDULES = "schedules";
    public static final String KEY_DURATION = "duration";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_IMAGE_URL = "image_url";

    public static final int KEY_ID_INDEX = 0;
    public static final int KEY_NAME_INDEX = 1;
    public static final int KEY_SCHEDULES_INDEX = 2;
    public static final int KEY_DURATION_INDEX = 3;
    public static final int KEY_DESCRIPTION_INDEX = 4;
    public static final int KEY_IMAGE_URL_INDEX = 5;


    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "" +
            " (" + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_NAME + " TEXT, " + KEY_SCHEDULES + " TEXT, " +
            "" + KEY_DURATION + " TEXT, " + KEY_DESCRIPTION + " TEXT, " + KEY_IMAGE_URL + " TEXT)";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static final String[] ALL = {"*"};

    public static void insert(ZooDatabaseHelper dbHelper, Show show) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = getShowContentValues(show);

        db.insertOrThrow(TABLE_NAME, null, values);
        db.close();
    }

    private static ContentValues getShowContentValues(Show show) {
        ContentValues values = new ContentValues();
        values.put(KEY_ID, show.getId());
        values.put(KEY_NAME, show.getName());
        values.put(KEY_SCHEDULES, show.getSchedulesString());
        values.put(KEY_DURATION, show.getDuration());
        values.put(KEY_DESCRIPTION, show.getDescription());
        values.put(KEY_IMAGE_URL, show.getImageUrl());
        return values;
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

        Cursor cursor = db.query(TABLE_NAME, ALL, KEY_ID + " = ?",
                new String[] {String.valueOf(showId)}, null, null, null);


        Show show = null;
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            show = createShowFromCursor(cursor);
        }

        cursor.close();
        db.close();
        return show;
    }

    public static void update(ZooDatabaseHelper dbHelper, Show show) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.update(TABLE_NAME, getShowContentValues(show), KEY_ID + " = ?",
                new String[] { String.valueOf(show.getId()) });
    }

    public static void insertOrUpdate(ZooDatabaseHelper dbHelper, Show show) {
        if (get(dbHelper, show.getId()) != null) {
            update(dbHelper, show);
        } else {
            insert(dbHelper, show);
        }
    }

    private static Show createAnimalShowFromCursor(Cursor cursor) {
        Show show;
        show = new Show(cursor.getLong(KEY_ID_INDEX), cursor.getString(KEY_NAME_INDEX),
                cursor.getString(KEY_SCHEDULES_INDEX), cursor.getString(KEY_DURATION_INDEX));
        return show;
    }

    private static Show createShowFromCursor(Cursor cursor) {
        Show show;
        show = new Show(cursor.getLong(KEY_ID_INDEX), cursor.getString(KEY_NAME_INDEX),
                cursor.getString(KEY_SCHEDULES_INDEX), cursor.getString(KEY_DURATION_INDEX),
                cursor.getString(KEY_DESCRIPTION_INDEX), cursor.getString(KEY_IMAGE_URL_INDEX));
        return show;
    }

    public static List<Show> getAnimalShows(ZooDatabaseHelper dbHelper, long animalId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(ShowDAO.TABLE_NAME + "." + ShowDAO.KEY_ID + ", ");
        queryBuilder.append(ShowDAO.TABLE_NAME + "." + ShowDAO.KEY_NAME + ", ");
        queryBuilder.append(ShowDAO.TABLE_NAME + "." + ShowDAO.KEY_SCHEDULES + ", ");
        queryBuilder.append(ShowDAO.TABLE_NAME + "." + ShowDAO.KEY_DURATION);

        String args = queryBuilder.toString();
        String tables = ShowDAO.TABLE_NAME + ", " + AnimalDAO.TABLE_NAME + ", " + AnimalShowDAO.TABLE_NAME;

        queryBuilder.setLength(0);
        queryBuilder.append(AnimalDAO.TABLE_NAME + "." + AnimalDAO.KEY_ID + " = " + String.valueOf(animalId) + " AND ");
        queryBuilder.append(AnimalDAO.TABLE_NAME + "." + AnimalDAO.KEY_ID + " = ");
        queryBuilder.append(AnimalShowDAO.TABLE_NAME + "." + AnimalShowDAO.KEY_ANIMAL + " AND ");
        queryBuilder.append(ShowDAO.TABLE_NAME + "." + ShowDAO.KEY_ID + " = ");
        queryBuilder.append(AnimalShowDAO.TABLE_NAME + "." + AnimalShowDAO.KEY_SHOW);

        String cond = queryBuilder.toString();
        String rawQuery = "SELECT " + args + " FROM " + tables + " WHERE " + cond;

        Cursor cursor = db.rawQuery(rawQuery, null);

        List<Show> shows = new ArrayList<>();

        while (cursor.moveToNext()) {
            shows.add(createAnimalShowFromCursor(cursor));
        }
        cursor.close();

        db.close();
        return shows;
    }

    public static List<Show> getAll(ZooDatabaseHelper dbHelper) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, ALL, null, null, null, null, null);

        List<Show> shows = new ArrayList<>();

        while (cursor.moveToNext()) {
            shows.add(createShowFromCursor(cursor));
        }

        cursor.close();
        db.close();
        return shows;
    }
}
