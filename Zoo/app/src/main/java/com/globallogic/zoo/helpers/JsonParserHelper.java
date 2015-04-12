package com.globallogic.zoo.helpers;

import android.util.Log;

import com.globallogic.zoo.models.Animal;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GL on 10/04/2015.
 */
abstract public class JsonParserHelper {

    public static List<Animal> parseJson(String json) {
        List<Animal> lAnimals = new ArrayList<>();
        JSONArray animals;
        Log.d("JsonParser", json);
        try {
            animals = new JSONArray(json);
            for (int i = 0; i < animals.length(); i++) {
                lAnimals.add(Animal.fromJson(animals.getJSONObject(i).toString()));
            }
        } catch (JSONException e) {
            Log.e("JsonParser", e.getMessage(), e);
        }

        return lAnimals;
    }

}
