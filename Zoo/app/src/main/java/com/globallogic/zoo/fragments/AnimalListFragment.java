package com.globallogic.zoo.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.globallogic.zoo.R;
import com.globallogic.zoo.adapters.AnimalAdapter;
import com.globallogic.zoo.data.AnimalRepository;
import com.globallogic.zoo.models.Animal;
import com.globallogic.zoo.network.API;

import java.util.List;


public class AnimalListFragment extends Fragment implements
        AnimalAdapter.OnAnimalClickListener,
        API.OnRequestListListener<Animal> {

    public static final String TAG = "AnimalListFragment";

    public interface OnAnimalClickListener {
        public void OnAnimalClick(long animalId);
    }

    private ProgressBar load;
    private RecyclerView recyclerView;
    private AnimalAdapter animalAdapter;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_animal_list, container, false);
        load = (ProgressBar) v.findViewById(R.id.welcomeactivity_load);
        animalAdapter = new AnimalAdapter(getActivity(), this);

        AnimalRepository repository = new AnimalRepository(getActivity(), this);
        repository.getAllAnimals();
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        bindRecyclerView();
    }

    private void bindRecyclerView() {
        Context context = getActivity();

        recyclerView = (RecyclerView) getActivity().findViewById(R.id.welcomeactivity_recycleview);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(animalAdapter);
        registerForContextMenu(recyclerView);
    }

    @Override
    public void onAnimalClick(Animal animal) {
        onAnimalClickListener.OnAnimalClick(animal.getId());
    }

    @Override
    public void onFail(int code) {
        if (code == API.NOT_FOUND) {
            Toast.makeText(getActivity(), "Sin animales. Conectese a internet",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSuccess(List<Animal> animals) {
        if (!animals.isEmpty()) {
            animalAdapter.setAnimals(animals);
        }
        load.setVisibility(View.INVISIBLE);
    }
}
