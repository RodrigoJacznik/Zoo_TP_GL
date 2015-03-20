package com.globallogic.zoo;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class AnimalDetailsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_details);

        Intent intent = getIntent();
        TextView tvName = (TextView) findViewById(R.id.animaldetailsactivity_name);
        TextView tvEspecie = (TextView) findViewById(R.id.animaldetailsactivity_especie);
        TextView tvDescripcion = (TextView) findViewById(R.id.animaldetailsactivity_descripcion);


        tvName.setText(intent.getStringExtra(WelcomeActivity.NOMBRE));
        tvEspecie.setText(intent.getStringExtra(WelcomeActivity.ESPECIE));
        tvDescripcion.setText(intent.getStringExtra(WelcomeActivity.DESCRIPCION));
    }

}
