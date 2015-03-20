package com.globallogic.zoo;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.globallogic.domain.Animal;

import java.util.ArrayList;


public class WelcomeActivity extends ActionBarActivity {
    static final String NOMBRE = "NAME";
    static final String ESPECIE = "ESPECIE";
    static final String DESCRIPCION = "DESCRIPCION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Intent intent = getIntent();
        Boolean isFemm = intent.getBooleanExtra(MainActivity.IS_FEMM, true);
        String name = intent.getStringExtra(MainActivity.USERK);

        TextView tv = (TextView) findViewById(R.id.welcomeactivity_welcome);
        Resources res = getResources();

        String sra = res.getString(R.string.welcomeactivity_sra);
        String sr = res.getString(R.string.welcomeactivity_sr);

        String welcome = String.format(res.getString(R.string.welcomeactivity_welcome),
                isFemm ? sra : sr, name);
        tv.setText(welcome);

        Button signout = (Button) findViewById(R.id.welcomeactivity_signout);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ArrayList<Animal> animals = (ArrayList) Animal.getAnimalList();

        final AnimalAdapter animalAdapter = new AnimalAdapter(this, animals);
        ListView lv = (ListView) findViewById(R.id.welcomeactivity_list);
        lv.setAdapter(animalAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Animal animalSelected = (Animal) parent.getAdapter().getItem(position);
                Intent intent = new Intent(WelcomeActivity.this, AnimalDetailsActivity.class);
                intent.putExtra(NOMBRE, animalSelected.getNombre());
                intent.putExtra(ESPECIE, animalSelected.getEspecie());
                intent.putExtra(DESCRIPCION, animalSelected.getDescripcion());
                startActivity(intent);
            }
        });
    }

    public class AnimalAdapter extends ArrayAdapter<Animal> {

        public AnimalAdapter(Context context, ArrayList<Animal> animals) {
            super(context, 0, animals);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Animal animal = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_animal_list, parent, false);
            }
            ImageView imFoto = (ImageView) convertView.findViewById(R.id.animallistactivity_foto);
            TextView tvName = (TextView) convertView.findViewById(R.id.animallistactivity_name);
            TextView tvEspecie = (TextView) convertView.findViewById(R.id.animallistactivity_especie);

            imFoto.setImageResource(R.drawable.grey_sheep);
            tvName.setText(animal.getNombre());
            tvEspecie.setText(animal.getEspecie());

            return convertView;
        }
    }
}
