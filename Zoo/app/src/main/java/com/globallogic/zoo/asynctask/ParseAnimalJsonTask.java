package com.globallogic.zoo.asynctask;

import android.os.AsyncTask;

import com.globallogic.zoo.models.Animal;
import com.globallogic.zoo.helpers.HttpConnectionHelper;
import com.globallogic.zoo.helpers.JsonParserHelper;

import java.util.ArrayList;
import java.util.List;

public class ParseAnimalJsonTask extends AsyncTask<Void, Void, List<Animal>> {

    OnAsyncTaskListener<List<Animal>> onAsyncTaskListener;
    int action;

    public ParseAnimalJsonTask(OnAsyncTaskListener onAsyncTaskListener, int action) {
        this.onAsyncTaskListener = onAsyncTaskListener;
        this.action = action;
    }

    @Override
    protected List<Animal> doInBackground(Void... params) {

        List<Animal> animals = new ArrayList<>();
        String data = excecuteAction();
        if (data != null) {
            animals = JsonParserHelper.parseJson(data);
        }

        return animals;
    }

    private String excecuteAction() {
        switch (action) {
            case HttpConnectionHelper.ALL_ANIMALS:
                return HttpConnectionHelper.getAllAnimals();
            default:
                return null;
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        onAsyncTaskListener.onPreExecute();
    }

    @Override
    protected void onPostExecute(List<Animal> animals) {
        onAsyncTaskListener.onPostExecute(animals);
    }
}