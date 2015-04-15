package com.globallogic.zoo.fragments;

import android.animation.Animator;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.globallogic.zoo.R;
import com.globallogic.zoo.activities.AnimalDetailsActivity;
import com.globallogic.zoo.adapters.AnimalAdapter;
import com.globallogic.zoo.asynctask.OnAsyncTaskListener;
import com.globallogic.zoo.asynctask.ParseAnimalJsonTask;
import com.globallogic.zoo.helpers.ZooDatabaseHelper;
import com.globallogic.zoo.models.Animal;

import java.util.List;

/**
 * Created by GL on 15/04/2015.
 */
public class AnimalListFragment extends Fragment implements AnimalAdapter.OnAnimalClickListener,
        OnAsyncTaskListener<List<Animal>> {

    public interface OnAnimalClickListener {
        public void OnAnimalClick(Animal animal);
    }

    private ProgressBar load;
    private RecyclerView recyclerView;
    private AnimalAdapter animalAdapter;
    private ParseAnimalJsonTask parseAnimalJsonTask;
    private OnAnimalClickListener onAnimalClickListener;

    public AnimalListFragment() {
        super();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            onAnimalClickListener = (OnAnimalClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity should implement " +
                    "AnimalListFragment.OnAnimalClickListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parseAnimalJsonTask = new ParseAnimalJsonTask(getActivity(), this);
        parseAnimalJsonTask.execute();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_animal_list, container, false);
        load = (ProgressBar) v.findViewById(R.id.welcomeactivity_load);
        bindRecyclerView(v);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (parseAnimalJsonTask.getStatus().equals(AsyncTask.Status.RUNNING)) {
            load.setVisibility(View.VISIBLE);
        }
    }

    private void bindRecyclerView(View v) {
        Context context = getActivity();

        recyclerView = (RecyclerView) v.findViewById(R.id.welcomeactivity_recycleview);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        animalAdapter = new AnimalAdapter(context, this);
        recyclerView.setAdapter(animalAdapter);
        registerForContextMenu(recyclerView);
    }

    @Override
    public void onAnimalClick(Animal animal) {
        onAnimalClickListener.OnAnimalClick(animal);
    }

    @Override
    public void onPostExecute(List<Animal> animals) {
        if (! animals.isEmpty()) {
            ZooDatabaseHelper db = new ZooDatabaseHelper(getActivity());
            db.insertAnimals(animals);

            animalAdapter.setAnimals(animals);
            animalAdapter.notifyDataSetChanged();
        }
        load.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onPreExecute() {
    }
}
