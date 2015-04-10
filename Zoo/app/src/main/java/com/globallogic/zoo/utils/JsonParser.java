package com.globallogic.zoo.utils;

import android.util.Log;

import com.globallogic.zoo.R;
import com.globallogic.zoo.models.Animal;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by GL on 10/04/2015.
 */
public class JsonParser {

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

    public static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }


}
