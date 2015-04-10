package com.globallogic.zoo.activities;

import android.app.AlarmManager;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.globallogic.zoo.R;
import com.globallogic.zoo.broadcastreceivers.AlarmBroadcastReceiver;
import com.globallogic.zoo.broadcastreceivers.LowBatteryBroadcastReceiver;
import com.globallogic.zoo.custom.views.FavoriteView;
import com.globallogic.zoo.custom.views.ShareDialog;
import com.globallogic.zoo.listeners.onTableRowClickListener;
import com.globallogic.zoo.models.Animal;
import com.globallogic.zoo.models.Schedule;
import com.globallogic.zoo.models.Show;
import com.globallogic.zoo.utils.AnimalUtils;

import java.io.File;


public class AnimalDetailsActivity extends ActionBarActivity implements
        FavoriteView.OnFavoriteClickListener,
        ShareDialog.NoticeShareDialogListener {

    public final static String ANIMAL = "ANIMAL";

    private final static String FAVORITE = "FAVORITE";
    private final static String COLOR = "COLOR";

    private static final int REQUEST_CAMERA = 0;

    private TextView name;
    private TextView specie;
    private TextView description;
    private FavoriteView favoriteView;
    private TableLayout schedule;
    private View rootView;
    private ImageView share;
    private ImageView animalThumb;
    private Button btnMoreInfo;
    private LowBatteryBroadcastReceiver lowBatteryBroadcastReceiver;

    private Animal animal;
    private int favoriteViewColor;
    private File animalPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_details);

        bindViews();
        setUpActionBar();

        btnMoreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moreInfo();
            }
        });

        animalThumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareAnimal();
            }
        });

        favoriteView.setOnFavoriteClickListener(this);

        lowBatteryBroadcastReceiver = new LowBatteryBroadcastReceiver();
    }

    @Override
    protected void onStart() {
        super.onStart();
        long animalID = getIntent().getLongExtra(ANIMAL, -1);
        animal = Animal.getById(animalID);

        initAnimalViews();
        populateScheduleTable();
        registerReceiver(lowBatteryBroadcastReceiver, new IntentFilter(Intent.ACTION_BATTERY_LOW));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(lowBatteryBroadcastReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_animal_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuanimal_settings:
                Log.d("SettingsActivityCall", "Llamaaaa");
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.menuanimal_share:
                shareAnimal();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        animal.setFavorite(savedInstanceState.getBoolean(FAVORITE));
        favoriteViewColor = savedInstanceState.getInt(COLOR);
        favoriteView.setBackgroundColor(favoriteViewColor);
        rootView.setBackgroundColor(favoriteViewColor);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(FAVORITE, animal.isFavorite());
        outState.putInt(COLOR, favoriteViewColor);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CAMERA:
                if (resultCode == RESULT_OK) {
                    File f = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
                    if (f.exists()) {
                        animalPhoto = f;
                        createShareDialog();
                    }
                }
                break;
            default:
                break;
        }
    }

    private void setUpActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_HOME_AS_UP);
        actionBar.setIcon(R.drawable.ic_action_logo);
    }

    private void moreInfo() {
        if (checkConnection()) {
            Intent intent = new Intent(this, MoreInfoActivity.class);
            intent.putExtra(MoreInfoActivity.URL, animal.getMoreInfo());
            startActivity(intent);
        } else {
            Toast.makeText(
                    this,
                    getResources().getString(R.string.animaldetailsactivity_without_connection),
                    Toast.LENGTH_LONG).show();
        }
    }

    private void shareAnimal() {
        Intent shareIntent = AnimalUtils.getShareAnimalIntent(animal);
        startActivity(shareIntent);
    }

    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void createShareDialog() {
        ShareDialog dialog = new ShareDialog();
        dialog.show(getFragmentManager(), "TAG");
    }

    private void bindViews() {
        btnMoreInfo = (Button) findViewById(R.id.animaldetailsactivity_more);
        favoriteView = (FavoriteView) findViewById(R.id.animaldetailsactivity_fav);
        name = (TextView) findViewById(R.id.animaldetailsactivity_name);
        specie = (TextView) findViewById(R.id.animaldetailsactivity_specie);
        description = (TextView) findViewById(R.id.animaldetailsactivity_description);
        schedule = (TableLayout) findViewById(R.id.animaldetailsactivity_table);
        rootView = findViewById(R.id.animaldetailsactivity_scrollview);
        share = (ImageView) findViewById(R.id.animaldetailsactivity_share);
        animalThumb = (ImageView) findViewById(R.id.animaldetailsactivity_photo);
    }

    private void initAnimalViews() {
        name.setText(animal.getName());
        specie.setText(animal.getSpecie());
        description.setText(animal.getDescripcion());

        favoriteView.setFavoriteState(animal.isFavorite());
    }

    private boolean checkConnection() {
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        return (cm.getActiveNetworkInfo() != null);
    }

    private void populateScheduleTable() {
        for (Show show : animal.getShow()) {
            for (Schedule schedule : show.getSchedules()) {
                TableRow horarioRow = new TableRow(this);

                TextView name = new TextView(this);
                TextView horaInicio = new TextView(this);
                TextView horaFin = new TextView(this);

                horarioRow.setOnClickListener(new onTableRowClickListener(schedule, this));

                name.setText(show.getName());
                horaInicio.setText(schedule.getInitialHourString());
                horaFin.setText(schedule.getFinalHourString());

                name.setGravity(Gravity.CENTER);
                horaInicio.setGravity(Gravity.CENTER);
                horaFin.setGravity(Gravity.CENTER);

                horarioRow.addView(name);
                horarioRow.addView(horaInicio);
                horarioRow.addView(horaFin);

                this.schedule.addView(horarioRow);
            }
        }
    }

    @Override
    public void onFavoriteClick(boolean favorite, int color) {
        animal.setFavorite(favorite);
        favoriteViewColor = color;
        rootView.setBackgroundColor(color);
        favoriteView.setBackgroundColor(favoriteViewColor);

        sendVibrateBroadcast(20);
    }

    private void sendVibrateBroadcast(int seconds) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmBroadcastReceiver.class);

        intent.putExtra(AlarmBroadcastReceiver.ANIMAL_ID, animal.getId());

        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + seconds * 1000, alarmIntent);
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String[] email) {
        Intent mailIntent = AnimalUtils.getShareMailAnimalIntent(animalPhoto, this, animal);
        mailIntent.putExtra(Intent.EXTRA_EMAIL, email);

        if (mailIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mailIntent);
        } else {
            Toast.makeText(this,
                    getResources().getString(R.string.animaldetailsactivity_without_mailapp),
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        dialog.dismiss();
    }

}
