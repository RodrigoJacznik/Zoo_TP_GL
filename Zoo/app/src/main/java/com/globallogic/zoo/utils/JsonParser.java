package com.globallogic.zoo.utils;

import com.globallogic.zoo.R;
import com.globallogic.zoo.models.Animal;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by GL on 10/04/2015.
 */
public class JsonParser {

    public static void parseJson(InputStream inputStream) throws JSONException {
        String json = convertStreamToString(inputStream);
        JSONArray animals = new JSONArray(json);
        for (int i = 0; i < animals.length(); i++) {
            Animal.fromJson(animals.getJSONObject(i).toString());
        }
    }

    private static String convertStreamToString(InputStream is) {
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
