package com.globallogic.zoo.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.globallogic.zoo.R;
import com.globallogic.zoo.helpers.SharedPreferencesHelper;
import com.globallogic.zoo.models.Animal;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;


public class AnimalAdapter extends RecyclerView.Adapter<AnimalAdapter.ViewHolder> {

    private List<Animal> animals = new ArrayList<>();

    private OnAnimalClickListener callbackObject;
    private List<ViewHolder> viewHoldersPressed = new ArrayList<>();
    private ActionMode.Callback actionModeCallback;
    private Boolean isActionModeActivate = false;

    public interface OnAnimalClickListener {
        public void onAnimalClick(Animal animal);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener,
            View.OnLongClickListener {

        public View rootView;
        public ImageView photo;
        public TextView name;
        public TextView specie;
        public Animal animal;
        public TextView notificationCounter;

        public ViewHolder(View v) {
            super(v);
            rootView = v;
            photo = (ImageView) v.findViewById(R.id.animallistactivity_photo);
            name = (TextView) v.findViewById(R.id.animallistactivity_name);
            specie = (TextView) v.findViewById(R.id.animallistactivity_specie);
            notificationCounter = (TextView) v.findViewById(R.id.animaldetailsactivity_noti_count);
            v.setOnClickListener(this);
            v.setOnLongClickListener(this);
        }

        public void load(Animal animal) {
            Context context = itemView.getContext();
            Ion.with(photo)
                    .error(android.R.drawable.ic_dialog_alert)
                    .placeholder(R.drawable.android)
                    .load(animal.getImage());

            name.setText(animal.getName());
            specie.setText(animal.getSpecie());

            int count = SharedPreferencesHelper.getAnimalNotificationCount(context, animal.getId());
            if (count > 0) {
                notificationCounter.setText(String.valueOf(count));
            } else {
                notificationCounter.setText("");
            }

            this.animal = animal;
        }

        @Override
        public boolean onLongClick(View v) {
            isActionModeActivate = true;
            ActionModeCallback am = (ActionModeCallback) actionModeCallback;
            if (am.getActionMode() == null) {
                am.setActionMode(v.startActionMode(actionModeCallback));
            }

            if (viewHoldersPressed.contains(this)) {
                v.setSelected(false);
                viewHoldersPressed.remove(this);
                if (viewHoldersPressed.size() == 1) {
                    am.setShareIconInvisible(true);
                }
                if (viewHoldersPressed.isEmpty()) {
                    am.finishActionMode();
                    isActionModeActivate = false;
                }
            } else {
                viewHoldersPressed.add(this);
                v.setSelected(true);
                if (viewHoldersPressed.size() > 1) {
                    am.setShareIconInvisible(false);
                }
            }
            return true;
        }

        @Override
        public void onClick(View v) {
            notificationCounter.setText("");
            if (callbackObject != null && !isActionModeActivate) {
                callbackObject.onAnimalClick(animal);
            }
        }
    }

    public AnimalAdapter(Context context) {
        super();
        actionModeCallback = new ActionModeCallback(AnimalAdapter.this, context);
    }

    public AnimalAdapter(Context context, OnAnimalClickListener animalAdapterCallback) {
        this(context);
        this.callbackObject = animalAdapterCallback;
    }

    @Override
    public AnimalAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.animal_row,
                parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Animal animal = animals.get(i);
        viewHolder.load(animal);
    }

    public void remove(Animal animal) {
        int pos = animals.indexOf(animal);
        animals.remove(pos);
        notifyItemRemoved(pos);
    }

    public void remove() {
        for (ViewHolder vh : viewHoldersPressed) {
            remove(vh.animal);
        }
        setViewHoldersPressedState(false);
        viewHoldersPressed.clear();
    }

    public void setViewHoldersPressedState(Boolean bool) {
        for (ViewHolder vh : viewHoldersPressed) {
            vh.itemView.setSelected(bool);
        }
        viewHoldersPressed.clear();
        isActionModeActivate = false;
    }

    @Override
    public int getItemCount() {
        return animals.size();
    }

    public Animal getItem() {
        return viewHoldersPressed.get(0).animal;
    }

    public void setAnimals(List<Animal> animals) {
        this.animals = animals;
        notifyDataSetChanged();
    }
}