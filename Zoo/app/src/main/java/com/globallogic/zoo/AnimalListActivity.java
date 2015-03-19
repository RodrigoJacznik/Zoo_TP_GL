package com.globallogic.zoo;

import android.content.res.Resources;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.globallogic.domain.Animal;


public class AnimalListActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_list);

        Resources res = getResources();

        ArrayAdapter<CharSequence> animalAdapter =
                ArrayAdapter.createFromResource(this, R.array.animals, android.R.layout.simple_spinner_dropdown_item);

        ListView listView = (ListView) findViewById(R.id.animallistactivity_list);
        listView.setAdapter(animalAdapter);
    }
}
