package com.globallogic.zoo.adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.globallogic.zoo.adapters.callbacks.AnimalAdapterCallback;
import com.globallogic.zoo.custom.views.callbacks.ActionModeCallback;
import com.globallogic.zoo.models.Animal;
import com.globallogic.zoo.R;

import java.util.List;

/**
 * Created by GL on 25/03/2015.
 */
public class AnimalAdapter extends RecyclerView.Adapter<AnimalAdapter.ViewHolder> {

    private List<Animal> animals = Animal.getAnimalList();
    private Context context;
    private AnimalAdapterCallback callbackObject;

    private int position;

    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnCreateContextMenuListener, View.OnLongClickListener {

        public View rootView;
        public ImageView photo;
        public TextView name;
        public TextView specie;
        public View color;

        public ActionMode.Callback actionModeCallback = (ActionMode.Callback)
                new ActionModeCallback(AnimalAdapter.this, context);

        public ViewHolder (View v) {
            super(v);
            rootView = v;
            photo = (ImageView) v.findViewById(R.id.animallistactivity_photo);
            name = (TextView) v.findViewById(R.id.animallistactivity_name);
            specie = (TextView) v.findViewById(R.id.animallistactivity_specie);
            color = v.findViewById(R.id.animaldetailsactivity_color);

            v.setOnCreateContextMenuListener(this);
            v.setOnLongClickListener(this);
        }

        public void load(Animal anAnimal) {
            photo.setImageResource(R.drawable.android);
            name.setText(anAnimal.getName());
            specie.setText(anAnimal.getSpecie());
            color.setBackgroundColor(selectBackgroundColor(anAnimal.getSpecieCode()));
            getAdapterPosition();
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

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v,
                                        ContextMenu.ContextMenuInfo menuInfo) {
        }

        @Override
        public boolean onLongClick(View v) {
            ActionModeCallback am = (ActionModeCallback) actionModeCallback;
            if (am.getActionMode() != null) {
                return false;
            }

            setPosition(getAdapterPosition());
            am.setActionMode(v.startActionMode(actionModeCallback));
            am.setCurrentView(v);
            v.setSelected(true);
            return true;
        }
    }

    public AnimalAdapter(Context context) {
        super();
        this.context = context;
    }

    public AnimalAdapter(Context context, AnimalAdapterCallback animalAdapterCallback) {
        super();
        this.context = context;
        this.callbackObject = animalAdapterCallback;
    }

    @Override
    public AnimalAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_animal_list,
                parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final Animal animalSelected = animals.get(i);
        viewHolder.load(animalSelected);
        viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callbackObject != null) {
                    callbackObject.onClick(animalSelected);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return animals.size();
    }

    public Animal getItem() {
        return animals.get(position);
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}