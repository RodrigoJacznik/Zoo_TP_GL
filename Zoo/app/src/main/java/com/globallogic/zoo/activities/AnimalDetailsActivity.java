package com.globallogic.zoo.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.globallogic.zoo.FavViewCallback;
import com.globallogic.zoo.R;
import com.globallogic.zoo.custom.views.FavView;
import com.globallogic.zoo.models.Animal;
import com.globallogic.zoo.models.Horario;


public class AnimalDetailsActivity extends ActionBarActivity implements FavViewCallback {
    private final static String FAVORITE = "FAVORITE";
    private final static String COLOR = "COLOR";

    final static String URL = "URL";

    private TextView tvName;
    private TextView tvEspecie;
    private TextView tvDescripcion;
    private FavView favView;
    private TableLayout tbHorarios;

    private Animal animal;
    private int favViewColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_details);

        animal = (Animal) getIntent().getSerializableExtra(WelcomeActivity.ANIMAL);
        favView = (FavView) findViewById(R.id.animaldetailsactivity_fav);
        tvName = (TextView) findViewById(R.id.animaldetailsactivity_name);
        tvEspecie = (TextView) findViewById(R.id.animaldetailsactivity_especie);
        tvDescripcion = (TextView) findViewById(R.id.animaldetailsactivity_descripcion);
        tbHorarios = (TableLayout) findViewById(R.id.animaldetailsactivity_table);

        tvName.append(" " + animal.getNombre());
        tvEspecie.append(" " + animal.getEspecie());
        tvDescripcion.append("\n" + animal.getDescripcion());

        Button btnMoreInfo = (Button) findViewById(R.id.animalsdetailsactivity_more);
        btnMoreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AnimalDetailsActivity.this, MoreInfoActivity.class);
                intent.putExtra(URL, animal.getUrl());
                startActivity(intent);
           }
        });

        favView.setFavoriteState(animal.isFavorite());
        favView.setCallback(this);
        populateHorarioTable();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        animal.setFavorite(savedInstanceState.getBoolean(FAVORITE));
        favViewColor = savedInstanceState.getInt(COLOR);
        favView.setBackgroundColor(favViewColor);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(FAVORITE, animal.isFavorite());
        outState.putInt(COLOR, favViewColor);
    }

    private void populateHorarioTable() {
        for (Horario horario: animal.getHorarios()) {
            TableRow horarioRow = new TableRow(this);
            TextView dia = new TextView(this);
            TextView horaInicio = new TextView(this);
            TextView horaFin = new TextView(this);

            dia.setText(horario.getDia());
            horaInicio.setText(horario.getHoraInicio());
            horaFin.setText(horario.getHoraFin());

            dia.setGravity(Gravity.CENTER);
            horaInicio.setGravity(Gravity.CENTER);
            horaFin.setGravity(Gravity.CENTER);

            horarioRow.addView(dia);
            horarioRow.addView(horaInicio);
            horarioRow.addView(horaFin);

            tbHorarios.addView(horarioRow);
        }
    }

    @Override
    public void callbackCall(boolean favorite, int color) {
        animal.setFavorite(favorite);
        favViewColor = color;
    }
}
