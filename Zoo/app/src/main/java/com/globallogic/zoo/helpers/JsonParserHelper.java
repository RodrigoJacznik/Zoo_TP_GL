package com.globallogic.zoo.helpers;

import android.util.Log;

import com.globallogic.zoo.models.Animal;
import com.globallogic.zoo.models.Show;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GL on 10/04/2015.
 */
abstract public class JsonParserHelper {
    private static final String TAG = "JsonParserHelper";

    public static List<Animal> parseAnimalListJson(String json) {
        List<Animal> animals = new ArrayList<>();
        JSONArray jAnimals;
        try {
            jAnimals = new JSONArray(json);
            for (int i = 0; i < jAnimals.length(); i++) {
                animals.add(Animal.fromJson(jAnimals.getJSONObject(i).toString()));
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
        }

        return animals;
    }


    public static List<Show> parseShowListJson(String json) {
        List<Show> shows = new ArrayList<>();
        JSONArray jShows;
        try {
            jShows = new JSONArray(json);
            for (int i = 0; i < jShows.length(); i++) {
                shows.add(Show.fromJson(jShows.getJSONObject(i).toString()));
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
        }

        return shows;
    }
}
