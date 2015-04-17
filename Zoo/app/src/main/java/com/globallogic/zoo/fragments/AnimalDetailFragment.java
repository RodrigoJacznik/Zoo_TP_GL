package com.globallogic.zoo.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.globallogic.zoo.R;
import com.globallogic.zoo.activities.MoreInfoActivity;
import com.globallogic.zoo.broadcastreceivers.AlarmBroadcastReceiver;
import com.globallogic.zoo.custom.views.FavoriteView;
import com.globallogic.zoo.data.AnimalRepository;
import com.globallogic.zoo.helpers.AnimalHelper;
import com.globallogic.zoo.helpers.NotificationHelper;
import com.globallogic.zoo.listeners.onTableRowClickListener;
import com.globallogic.zoo.models.Animal;
import com.globallogic.zoo.models.Schedule;
import com.globallogic.zoo.models.Show;
import com.globallogic.zoo.network.API;
import com.globallogic.zoo.network.HttpConnectionHelper;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class AnimalDetailFragment extends Fragment implements
        NotificationHelper.OnNotificationListener,
        FavoriteView.OnFavoriteClickListener,
        API.OnRequestObjectListener<Animal> {

    public interface AnimalDetailCallback {
        public void onTakePhoto(Animal animal);
    }

    public static final String ANIMAL_ID = "ANIMAL_ID";
    public static final int REQUEST_CAMERA = 0;
    private static final String TAG = "AnimalDetailFragment";

    private TextView name;
    private TextView specie;
    private TextView description;
    private FavoriteView favoriteView;
    private TableLayout schedule;
    private ImageView animalThumb;
    private ImageView takePhoto;
    private Button btnMoreInfo;
    private ProgressBar load;
    private AnimalDetailCallback callback;
    private AnimalRepository repository;

    private Animal animal;
    private long animalId;

    public AnimalDetailFragment() {
        super();
    }

    public static AnimalDetailFragment newInstance(long animalId) {
        AnimalDetailFragment fragment = new AnimalDetailFragment();
        Bundle args = new Bundle();
        args.putLong(ANIMAL_ID, animalId);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof AnimalDetailCallback) {
            callback = (AnimalDetailCallback) activity;
        } else {
            throw new ClassCastException("Activity must implement AnimalDetailCallback");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        animalId = getArguments().getLong(ANIMAL_ID);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_animal_detail, container, false);
        bindViews(v);
        btnMoreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moreInfo();
            }
        });
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onTakePhoto(animal);
            }
        });
        favoriteView.setOnFavoriteClickListener(this);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        repository = new AnimalRepository(getActivity(), this);
        repository.getAnimalById(animalId);
    }

    @Override
    public void onResume() {
        super.onResume();
        NotificationHelper.registerListener(this);
        NotificationHelper.cancelNotification(getActivity());
    }

    @Override
    public void onStop() {
        super.onStop();
        NotificationHelper.unregisterListener();
    }

    private void bindViews(View v) {
        btnMoreInfo = (Button) v.findViewById(R.id.animaldetailsactivity_more);
        favoriteView = (FavoriteView) v.findViewById(R.id.animaldetailsactivity_fav);
        name = (TextView) v.findViewById(R.id.animaldetailsactivity_name);
        specie = (TextView) v.findViewById(R.id.animaldetailsactivity_specie);
        description = (TextView) v.findViewById(R.id.animaldetailsactivity_description);
        schedule = (TableLayout) v.findViewById(R.id.animaldetailsactivity_table);

        animalThumb = (ImageView) v.findViewById(R.id.animaldetailsactivity_img);
        takePhoto = (ImageView) v.findViewById(R.id.animaldetailsactivity_photo);
        load = (ProgressBar) v.findViewById(R.id.animaldetailsactivity_load);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_animal_details, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuanimal_share:
                share();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initAnimalViews() {
        name.setText(animal.getName());
        specie.setText(animal.getSpecie());
        description.setText(animal.getDescripcion());
        favoriteView.setFavoriteState(animal.isFavorite());
        Ion.with(this)
                .load(animal.getImage())
                .progressBar(load)
                .intoImageView(animalThumb)
                .setCallback(new FutureCallback<ImageView>() {
                    @Override
                    public void onCompleted(Exception e, ImageView result) {
                        load.setVisibility(View.INVISIBLE);
                        if (result == null) {
                            animalThumb.setImageResource(R.drawable.android);
                        }
                    }
                });
    }

    private void moreInfo() {
        Context context = getActivity();
        if (HttpConnectionHelper.checkConnection(context)) {
            Intent intent = new Intent(context, MoreInfoActivity.class);
            intent.putExtra(MoreInfoActivity.URL, animal.getMoreInfo());
            startActivity(intent);
        } else {
            Toast.makeText(context,
                    getResources().getString(R.string.animaldetailsactivity_without_connection),
                    Toast.LENGTH_LONG).show();
        }
    }

    private void share() {
        Intent shareIntent = AnimalHelper.getShareAnimalIntent(animal);
        startActivity(shareIntent);
    }

    private void populateScheduleTable() {
        Context context = getActivity();
        for (Show show : animal.getShow()) {
            for (Schedule schedule : show.getSchedules()) {
                TableRow hourRow = new TableRow(context);

                TextView name = new TextView(context);
                TextView initialHour = new TextView(context);
                TextView endHour = new TextView(context);

                hourRow.setOnClickListener(new onTableRowClickListener(schedule, context));

                name.setText(show.getName());
                initialHour.setText(schedule.getInitialHourString());
                endHour.setText(schedule.getFinalHourString());

                name.setGravity(Gravity.CENTER);
                initialHour.setGravity(Gravity.CENTER);
                endHour.setGravity(Gravity.CENTER);

                hourRow.addView(name);
                hourRow.addView(initialHour);
                hourRow.addView(endHour);

                this.schedule.addView(hourRow);
            }
        }
    }

    @Override
    public void onNotification() {
        String msg = String.format(getString(R.string.animaldetailsactivity_attractions_start),
                animal.getName());
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public long getAnimalId() {
        return animalId;
    }

    @Override
    public void onFavoriteClick(boolean favorite, int color) {
        animal.setFavorite(favorite);
        repository.insertAnimal(animal);
        AlarmBroadcastReceiver.sendVibrateBroadcast(getActivity(), 20, animal.getId());
    }

    @Override
    public void onFail(int code) {
        Log.d(TAG, "Faaail");
    }

    @Override
    public void onSuccess(Animal animal) {
        this.animal = animal;
        initAnimalViews();
        populateScheduleTable();
    }
}
