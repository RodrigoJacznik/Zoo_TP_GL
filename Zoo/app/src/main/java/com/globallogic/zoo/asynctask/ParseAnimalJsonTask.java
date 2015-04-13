package com.globallogic.zoo.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.globallogic.zoo.models.Animal;
import com.globallogic.zoo.helpers.HttpConnectionHelper;
import com.globallogic.zoo.helpers.JsonParserHelper;

import java.util.ArrayList;
import java.util.List;

public class ParseAnimalJsonTask extends AsyncTask<Void, Void, List<Animal>> {

    private OnAsyncTaskListener<List<Animal>> onAsyncTaskListener;
    private Context context;

    public ParseAnimalJsonTask(Context context, OnAsyncTaskListener onAsyncTaskListener) {
        this.context = context;
        this.onAsyncTaskListener = onAsyncTaskListener;
    }

    @Override
    protected List<Animal> doInBackground(Void... params) {

        List<Animal> animals = new ArrayList<>();
        String data = HttpConnectionHelper.getAllAnimals(context);
        if (data != null) {
            animals = JsonParserHelper.parseJson(data);
        }

        return animals;
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