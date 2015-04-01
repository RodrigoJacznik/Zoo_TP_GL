package com.globallogic.zoo.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.globallogic.zoo.R;
import com.globallogic.zoo.adapters.AnimalAdapter;
import com.globallogic.zoo.adapters.callbacks.AnimalAdapterCallback;
import com.globallogic.zoo.models.Animal;


public class WelcomeActivity extends ActionBarActivity implements AnimalAdapterCallback {

    final static String USERK = "USER";
    static final String ANIMAL = "ANIMAL";

    private Button signout;
    private TextView welcome;
    private ImageView maps;
    private RecyclerView recyclerView;
    private AnimalAdapter animalAdapter;
    private ActionMode actionMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        bindViews();
        setUpActionBar();

        Intent intent = getIntent();
        String name = intent.getStringExtra(USERK);

        welcome.setText(makeWelcomeMessage(name));

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPositionInMaps();
            }
        });

        bindRecyclerView();
        registerForContextMenu(recyclerView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menumain_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(Animal animal) {
        Intent intent = new Intent(this, AnimalDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(AnimalDetailsActivity.ANIMAL, animal);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void setUpActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);
    }

    private void viewPositionInMaps() {
        Intent mapsIntent = new Intent(Intent.ACTION_VIEW);
        mapsIntent.setData(Uri.parse("geo:-34.9085327, -57.9378387"));
        if (mapsIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapsIntent);
        } else {
            Toast.makeText(this, getResources().getString(R.string.welcomeactivity_without_maps),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void bindRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.welcomeactivity_recycleview);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        animalAdapter = new AnimalAdapter(this, this);

        recyclerView.setAdapter(animalAdapter);
    }

    private String makeWelcomeMessage(String name) {
        return String.format(getResources().getString(R.string.welcomeactivity_welcome), name);
    }

    private void bindViews() {
        signout = (Button) findViewById(R.id.welcomeactivity_signout);
        welcome = (TextView) findViewById(R.id.welcomeactivity_welcome);
        maps = (ImageView) findViewById(R.id.welcomeactivity_maps);
    }
}
