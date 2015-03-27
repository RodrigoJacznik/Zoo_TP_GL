package com.globallogic.zoo.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.globallogic.zoo.models.Animal;
import com.globallogic.zoo.activities.AnimalDetailsActivity;
import com.globallogic.zoo.R;

import java.util.List;

/**
 * Created by GL on 25/03/2015.
 */
public class AnimalAdapter extends RecyclerView.Adapter<AnimalAdapter.ViewHolder> {
    private List<Animal> animals = Animal.getAnimalList();
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View rootView;
        public ImageView photo;
        public TextView name;
        public TextView specie;
        public SurfaceView color;

        public ViewHolder (View v) {
            super(v);
            rootView = v;
            photo = (ImageView) v.findViewById(R.id.animallistactivity_photo);
            name = (TextView) v.findViewById(R.id.animallistactivity_name);
            specie = (TextView) v.findViewById(R.id.animallistactivity_specie);
            color = (SurfaceView) v.findViewById(R.id.animaldetailsactivity_color);
        }

        public void load(Animal anAnimal) {
            photo.setImageResource(R.drawable.grey_sheep);
            name.setText(anAnimal.getName());
            specie.setText(anAnimal.getSpecie());
            color.setBackgroundColor(selectBackgroundColor(anAnimal.getSpecieCode()));
        }

        public int selectBackgroundColor(int especieCode) {
            Resources resources = context.getResources();
            switch (especieCode) {
                case 1:
                    return resources.getColor(android.R.color.holo_blue_bright);
                case 2:
                    return resources.getColor(android.R.color.holo_green_light);
                case 3:
                    return resources.getColor(android.R.color.holo_orange_light);
                case 4:
                    return  resources.getColor(android.R.color.holo_red_light);
                default:
                    return resources.getColor(android.R.color.background_light);
            }
        }
    }

    public AnimalAdapter(Context context) {
        super();
        this.context = context;
    }

    @Override
    public AnimalAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_animal_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final Animal animalSelected = animals.get(i);
        viewHolder.load(animalSelected);
        viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AnimalAdapter.this.context, AnimalDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(AnimalDetailsActivity.ANIMAL, animalSelected);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return animals.size();
    }

}