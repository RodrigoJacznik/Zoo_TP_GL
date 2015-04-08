package com.globallogic.zoo.activities;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
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
import com.globallogic.zoo.broadcastreceivers.LowBatteryBroadcastReceiver;
import com.globallogic.zoo.models.Animal;


public class WelcomeActivity extends ActionBarActivity implements
        AnimalAdapter.OnAnimalClickListener {

    final static String USERK = "USER";
    static final String ANIMAL = "ANIMAL";

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
        Log.d("Life cycle", "WelcomeActivity onCreate");
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

        bindRecyclerView();
        registerForContextMenu(recyclerView);

        lowBatteryBroadcastReceiver = new LowBatteryBroadcastReceiver();
    }

    @Override
    protected void onStart() {
        super.onStart();

        userName = getIntent().getStringExtra(USERK);
        registerReceiver(lowBatteryBroadcastReceiver, new IntentFilter(Intent.ACTION_BATTERY_LOW));
        welcome.setText(makeWelcomeMessage(userName));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(lowBatteryBroadcastReceiver);
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
}
