package com.globallogic.zoo;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.globallogic.domain.Animal;

import java.util.List;


public class WelcomeActivity extends ActionBarActivity {
    static final String ANIMAL = "ANIMAL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Button signout = (Button) findViewById(R.id.welcomeactivity_signout);
        TextView tv = (TextView) findViewById(R.id.welcomeactivity_welcome);

        Resources res = getResources();

        Intent intent = getIntent();
        Boolean isFemm = intent.getBooleanExtra(MainActivity.IS_FEMM, true);
        String name = intent.getStringExtra(MainActivity.USERK);

        String sra = res.getString(R.string.welcomeactivity_sra);
        String sr = res.getString(R.string.welcomeactivity_sr);

        String welcome = String.format(res.getString(R.string.welcomeactivity_welcome),
                isFemm ? sra : sr, name);
        tv.setText(welcome);

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.welcomeactivity_recycleview);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        AnimalAdapter mAnimalAdapter = new AnimalAdapter();
        mRecyclerView.setAdapter(mAnimalAdapter);
    }

    public class AnimalAdapter extends RecyclerView.Adapter<AnimalAdapter.ViewHolder> {
        private List<Animal> animals = Animal.getAnimalList();

        public class ViewHolder extends RecyclerView.ViewHolder {
            public View vLayout;
            public ImageView ivFoto;
            public TextView tvNombre;
            public TextView tvEspecie;
            public SurfaceView svColor;

            public ViewHolder (View v) {
                super(v);
                vLayout = v;
                ivFoto = (ImageView) v.findViewById(R.id.animallistactivity_foto);
                tvNombre = (TextView) v.findViewById(R.id.animallistactivity_name);
                tvEspecie = (TextView) v.findViewById(R.id.animallistactivity_especie);
                svColor = (SurfaceView) v.findViewById(R.id.animaldetailsactivity_color);
            }

            public void load(Animal anAnimal) {
                ivFoto.setImageResource(R.drawable.grey_sheep);
                tvNombre.setText(anAnimal.getNombre());
                tvEspecie.setText(anAnimal.getEspecie());
                svColor.setBackgroundColor(selectBackgroundColor(anAnimal.getEspecieCode()));
            }

            public int selectBackgroundColor(int especieCode) {
                Resources resources = getResources();
                switch (especieCode) {
                    case 1:
                        return resources.getColor(android.R.color.holo_blue_bright);
                    case 2:
                        return resources.getColor(android.R.color.holo_green_light);
                    case 3:
                        return resources.getColor(android.R.color.holo_orange_light);
                    case 4:
                        return  resources.getColor(android.R.color.holo_red_light);
                    default:
                        return resources.getColor(android.R.color.background_light);
                }
            }
        }

        public AnimalAdapter() {
            super();
        }

        @Override
        public AnimalAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_animal_list, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            final Animal animalSelected = animals.get(i);
            viewHolder.load(animalSelected);
            viewHolder.vLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(WelcomeActivity.this, AnimalDetailsActivity.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putSerializable(ANIMAL, animalSelected);
                    intent.putExtras(mBundle);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return animals.size();
        }

    }
}
