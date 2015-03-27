package com.globallogic.zoo.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.globallogic.zoo.custom.views.callbacks.FavoriteViewCallback;
import com.globallogic.zoo.R;
import com.globallogic.zoo.custom.views.FavoriteView;
import com.globallogic.zoo.models.Animal;
import com.globallogic.zoo.models.Schudle;


public class AnimalDetailsActivity extends ActionBarActivity implements FavoriteViewCallback {
    public final static String ANIMAL = "ANIMAL";

    private final static String FAVORITE = "FAVORITE";
    private final static String COLOR = "COLOR";

    private TextView name;
    private TextView specie;
    private TextView description;
    private FavoriteView favoriteView;
    private TableLayout schedule;

    private Animal animal;
    private int favoriteViewColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_details);

        bindViews();

        name.append(" " + animal.getName());
        specie.append(" " + animal.getSpecie());
        description.append("\n" + animal.getDescripcion());

        Button btnMoreInfo = (Button) findViewById(R.id.animalsdetailsactivity_more);
        btnMoreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AnimalDetailsActivity.this, MoreInfoActivity.class);
                intent.putExtra(MoreInfoActivity.URL, animal.getUrl());
                startActivity(intent);
           }
        });

        favoriteView.setFavoriteState(animal.isFavorite());
        favoriteView.setCallback(this);
        populateScheduleTable();
    }

    private void bindViews() {
        animal = (Animal) getIntent().getSerializableExtra(WelcomeActivity.ANIMAL);
        favoriteView = (FavoriteView) findViewById(R.id.animaldetailsactivity_fav);
        name = (TextView) findViewById(R.id.animaldetailsactivity_name);
        specie = (TextView) findViewById(R.id.animaldetailsactivity_specie);
        description = (TextView) findViewById(R.id.animaldetailsactivity_description);
        schedule = (TableLayout) findViewById(R.id.animaldetailsactivity_table);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        animal.setFavorite(savedInstanceState.getBoolean(FAVORITE));
        favoriteViewColor = savedInstanceState.getInt(COLOR);
        favoriteView.setBackgroundColor(favoriteViewColor);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(FAVORITE, animal.isFavorite());
        outState.putInt(COLOR, favoriteViewColor);
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
    }
}
