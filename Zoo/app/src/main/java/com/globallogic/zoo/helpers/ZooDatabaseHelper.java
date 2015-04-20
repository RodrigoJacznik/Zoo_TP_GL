package com.globallogic.zoo.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.globallogic.zoo.data.dao.AnimalDAO;
import com.globallogic.zoo.data.dao.AnimalShowDAO;
import com.globallogic.zoo.data.dao.ShowDAO;
import com.globallogic.zoo.data.dao.UserDAO;
import com.globallogic.zoo.models.Animal;
import com.globallogic.zoo.models.Show;

import java.util.List;

/**
 * Created by GL on 14/04/2015.
 */

public class ZooDatabaseHelper extends SQLiteOpenHelper {


    private static final String DB_NAME = "ZooDatabase";
    private static final int VERSION = 3;

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

    public Animal getAnimalById(long animalId) {
        return AnimalDAO.get(this, animalId);
    }

    public void insertOrUpdateUser(String name) {
        UserDAO.insertOrUpdate(this, name);
    }

    public List<Show> getShows() {
        return ShowDAO.getAll(this);
    }
}
