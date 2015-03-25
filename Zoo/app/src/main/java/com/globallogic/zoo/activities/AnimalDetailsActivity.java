package com.globallogic.zoo.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.globallogic.zoo.R;
import com.globallogic.zoo.custom.views.FavView;
import com.globallogic.zoo.domain.Animal;



public class AnimalDetailsActivity extends ActionBarActivity {
    final static String URL = "URL";
    private TextView tvName;
    private TextView tvEspecie;
    private TextView tvDescripcion;
    private Animal animal;

    private FavView favView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_details);

        animal = (Animal) getIntent().getSerializableExtra(WelcomeActivity.ANIMAL);

        tvName = (TextView) findViewById(R.id.animaldetailsactivity_name);
        tvEspecie = (TextView) findViewById(R.id.animaldetailsactivity_especie);
        tvDescripcion = (TextView) findViewById(R.id.animaldetailsactivity_descripcion);
        favView = (FavView) findViewById(R.id.animaldetailsactivity_fav);

        tvName.append(" " + animal.getNombre());
        tvEspecie.append(" " + animal.getEspecie());
        tvDescripcion.append(" " + animal.getDescripcion());

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
    }

}
