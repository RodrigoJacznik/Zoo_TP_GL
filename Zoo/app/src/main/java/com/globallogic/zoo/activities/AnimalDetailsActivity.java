package com.globallogic.zoo.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.PaintDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.globallogic.zoo.R;
import com.globallogic.zoo.custom.views.FavView;
import com.globallogic.zoo.models.Animal;
import com.globallogic.zoo.models.Horario;


public class AnimalDetailsActivity extends ActionBarActivity {
    final static String URL = "URL";
    private TextView tvName;
    private TextView tvEspecie;
    private TextView tvDescripcion;
    private FavView favView;
    private TableLayout tbHorarios;
    private View rootView;

    private int backgroundcolor;
    private Animal animal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_details);

        animal = (Animal) getIntent().getSerializableExtra(WelcomeActivity.ANIMAL);

        tvName = (TextView) findViewById(R.id.animaldetailsactivity_name);
        tvEspecie = (TextView) findViewById(R.id.animaldetailsactivity_especie);
        tvDescripcion = (TextView) findViewById(R.id.animaldetailsactivity_descripcion);
        favView = (FavView) findViewById(R.id.animaldetailsactivity_fav);
        tbHorarios = (TableLayout) findViewById(R.id.animaldetailsactivity_table);
        rootView = findViewById(android.R.id.content).getRootView();

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
        populateHorarioTable();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
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

}
