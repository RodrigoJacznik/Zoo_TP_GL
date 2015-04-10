package com.globallogic.zoo.activities;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.globallogic.zoo.broadcastreceivers.AlarmBroadcastReceiver;
import com.globallogic.zoo.broadcastreceivers.LowBatteryBroadcastReceiver;
import com.globallogic.zoo.models.Animal;
import com.globallogic.zoo.utils.HttpConnectionManager;
import com.globallogic.zoo.utils.JsonParser;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class WelcomeActivity extends ActionBarActivity implements
        AnimalAdapter.OnAnimalClickListener {

    private static final String URL_GET = "http://rodjacznik.pythonanywhere.com/api/v1.0/animals";
    public static final String USERK = "USER";
    private static final String ANIMAL = "ANIMAL";

    private Button signout;
    private TextView welcome;
    private ImageView maps;
    private RecyclerView recyclerView;
    private AnimalAdapter animalAdapter;
    private LowBatteryBroadcastReceiver lowBatteryBroadcastReceiver;

    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        ParseAnimalJson parseAnimalJson = new ParseAnimalJson();
        parseAnimalJson.execute();

        bindViews();
        setUpActionBar();

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

        lowBatteryBroadcastReceiver = new LowBatteryBroadcastReceiver();
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();

        userName = intent.getStringExtra(USERK);
        Boolean bool = intent.getBooleanExtra(AlarmBroadcastReceiver.MULTI_NOTIFICATION, false);
        AlarmBroadcastReceiver.resetNotificationCount(bool);

        registerReceiver(lowBatteryBroadcastReceiver, new IntentFilter(Intent.ACTION_BATTERY_LOW));
        welcome.setText(makeWelcomeMessage(userName));
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (lowBatteryBroadcastReceiver != null) {
            unregisterReceiver(lowBatteryBroadcastReceiver);
        }
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
    public void onAnimalClick(Animal animal) {
        Intent intent = new Intent(this, AnimalDetailsActivity.class);
        intent.putExtra(AnimalDetailsActivity.ANIMAL, animal.getId());
        startActivity(intent);
    }

    private void setUpActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME);
        actionBar.setIcon(R.drawable.ic_action_logo);
    }

    private void viewPositionInMaps() {
        Intent mapsIntent = new Intent(Intent.ACTION_VIEW);
        mapsIntent.setData(Uri.parse("geo:-34.9085327, -57.9378387"));
        if (mapsIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapsIntent);
        } else {
            Toast.makeText(this, getResources().getString(R.string.welcomeactivity_without_maps),
                    Toast.LENGTH_LONG).show();
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

    private class ParseAnimalJson extends AsyncTask<Void, Void, List<Animal>> {

        @Override
        protected List<Animal> doInBackground(Void... params) {
            HttpConnectionManager conn =
                    new HttpConnectionManager(URL_GET, HttpConnectionManager.GET);
            List<Animal> animals = new ArrayList<>();
            conn.connect();
            int response = conn.getResponseCode();
            if (response == 200) {
                String data = conn.getData();
                if (data != null) {
                    animals = JsonParser.parseJson(data);
                }
            }

            return animals;
        }

        @Override
        protected void onPostExecute(List<Animal> animals) {
            Animal.setAnimals(animals);
            bindRecyclerView();
            registerForContextMenu(recyclerView);
        }
    }
}
