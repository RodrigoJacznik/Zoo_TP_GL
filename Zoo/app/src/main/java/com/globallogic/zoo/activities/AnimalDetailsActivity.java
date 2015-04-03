package com.globallogic.zoo.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
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
import com.globallogic.zoo.custom.views.FavoriteView;
import com.globallogic.zoo.custom.views.callbacks.FavoriteViewCallback;
import com.globallogic.zoo.models.Animal;
import com.globallogic.zoo.models.Schudle;
import com.globallogic.zoo.utils.AnimalUtils;

import java.io.File;


public class AnimalDetailsActivity extends ActionBarActivity implements FavoriteViewCallback {
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
    private ImageView photo;
    private Button btnMoreInfo;

    private Animal animal;
    private static Animal savedAnimal;
    private int favoriteViewColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_details);
        Log.d("Life cycle", "AnimalDetailsActivity onCreate");
        bindViews();
        setUpActionBar();

        btnMoreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moreInfo();
            }
        });

        photo.setOnClickListener(new View.OnClickListener() {
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

        favoriteView.setCallback(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        animal = (Animal) getIntent().getSerializableExtra(WelcomeActivity.ANIMAL);
        if (animal == null) {
            animal = savedAnimal;
        }

        initAnimalViews();
        populateScheduleTable();
    }

    @Override
    protected void onStop() {
        super.onStop();
        savedAnimal = animal;
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
            case R.id.menumain_settings:
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

    /*
    * Se llama solo cuando la activity es destruida (se gira la pantalla o el SO reclama memoria).
    * Si la activity se pone en background, no se asegura que se llame a onSaveInstanceState.
    * */
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
                        sendMail(f);
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
            intent.putExtra(MoreInfoActivity.URL, animal.getUrl());
            startActivity(intent);
        } else {
            Toast.makeText(
                    this,
                    getResources().getString(R.string.animaldetailsactivity_without_connection),
                    Toast.LENGTH_LONG).show();
        }
    }

    private void shareAnimal() {
        Intent intent = AnimalUtils.getShareAnimalIntent(animal);
        startActivity(intent);
    }

    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void sendMail(File file) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("application/image");
        String subject = String.format(getResources().
                getString(R.string.animaldetailsactivity_email_subject), animal.getName());
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        startActivity(emailIntent);
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
        photo = (ImageView) findViewById(R.id.animaldetailsactivity_photo);
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
        for (Schudle schudle : animal.getSchudle()) {
            TableRow horarioRow = new TableRow(this);
            TextView dia = new TextView(this);
            TextView horaInicio = new TextView(this);
            TextView horaFin = new TextView(this);

            dia.setText(schudle.getDay());
            horaInicio.setText(schudle.getInitialHour());
            horaFin.setText(schudle.getFinalHour());

            dia.setGravity(Gravity.CENTER);
            horaInicio.setGravity(Gravity.CENTER);
            horaFin.setGravity(Gravity.CENTER);

            horarioRow.addView(dia);
            horarioRow.addView(horaInicio);
            horarioRow.addView(horaFin);

            schedule.addView(horarioRow);
        }
    }

    @Override
    public void callbackCall(boolean favorite, int color) {
        animal.setFavorite(favorite);
        favoriteViewColor = color;
        rootView.setBackgroundColor(color);
        favoriteView.setBackgroundColor(favoriteViewColor);
    }


}
