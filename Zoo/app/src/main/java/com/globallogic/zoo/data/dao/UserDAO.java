package com.globallogic.zoo.data.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.globallogic.zoo.helpers.ZooDatabaseHelper;

import java.util.Date;

/**
 * Created by GL on 15/04/2015.
 */
public class UserDAO {

    public static final String TABLE_NAME = "User";

    public static final String KEY_NAME = "name";
    public static final String KEY_LAST_LOGIN = "last_login";

    public static final int KEY_NAME_INDEX = 0;
    public static final int KEY_LAST_LOGIN_INDEX = 1;

    // datetime unix like
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " " +
            " ( " + KEY_NAME + " TEXT PRIMARY KEY, " + KEY_LAST_LOGIN + " INTEGER)";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static void insertOrUpdate(ZooDatabaseHelper dbHelper, String name) {
        long lastLogin = (new Date()).getTime();

        if (get(dbHelper, name) != null) {
            update(dbHelper, name, lastLogin);
        } else {
            insert(dbHelper, name, lastLogin);
        }
    }

    public static String get(ZooDatabaseHelper dbHelper, String name) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[]{"*"},
                KEY_NAME + " LIKE ?",
                new String[] {name}, null, null, null);

        String nameReturn = null;
        if (cursor.getCount() > 0) {
            nameReturn = name;
        }

        cursor.close();
        db.close();
        return nameReturn;
    }

    public static void insert(ZooDatabaseHelper dbHelper, String name, long lastLogin) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = getContentValues(name, lastLogin);
        db.insertOrThrow(TABLE_NAME, null, contentValues);
        db.close();
    }

    private static ContentValues getContentValues(String name, long lastLogin) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME, name);
        contentValues.put(KEY_LAST_LOGIN, lastLogin);
        return contentValues;
    }

    public static void update(ZooDatabaseHelper dbHelper, String user, long lastLogin) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = getContentValues(user, lastLogin);
        db.update(TABLE_NAME, contentValues, KEY_NAME + " = ?", new String[] {KEY_NAME});
        db.close();
    }
}
