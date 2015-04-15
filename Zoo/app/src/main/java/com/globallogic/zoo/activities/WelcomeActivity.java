package com.globallogic.zoo.activities;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.globallogic.zoo.R;
import com.globallogic.zoo.adapters.AnimalAdapter;
import com.globallogic.zoo.asynctask.OnAsyncTaskListener;
import com.globallogic.zoo.asynctask.ParseAnimalJsonTask;
import com.globallogic.zoo.fragments.AnimalListFragment;
import com.globallogic.zoo.helpers.SharedPreferencesHelper;
import com.globallogic.zoo.helpers.ZooDatabaseHelper;
import com.globallogic.zoo.models.Animal;
import com.globallogic.zoo.helpers.HttpConnectionHelper;
import com.globallogic.zoo.helpers.NotificationHelper;

import java.util.List;


public class WelcomeActivity extends BaseActivity implements AnimalListFragment.OnAnimalClickListener {

    private Button signout;
    private TextView welcome;
    private ImageView maps;

    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        bindViews();
        setUpActionBar();

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPositionInMaps();
            }
        });

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.welcomeactivity_fragment, new AnimalListFragment());
        ft.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();

        userName = SharedPreferencesHelper.getUserName(this);
        welcome.setText(makeWelcomeMessage(userName));
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
    protected void setUpActionBar() {
        super.setUpActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME);
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

    private String makeWelcomeMessage(String name) {
        return String.format(getResources().getString(R.string.welcomeactivity_welcome), name);
    }

    private void bindViews() {
        signout = (Button) findViewById(R.id.welcomeactivity_signout);
        welcome = (TextView) findViewById(R.id.welcomeactivity_welcome);
        maps = (ImageView) findViewById(R.id.welcomeactivity_maps);
    }

    public static Intent getIntent(Context context) {
        return new Intent(context, WelcomeActivity.class);
    }

    private void logout() {
        SharedPreferencesHelper.clearUserName(this);
        finish();
    }

    @Override
    public void OnAnimalClick(Animal animal) {
        Intent intent = new Intent(this, AnimalDetailsActivity.class);
        intent.putExtra(AnimalDetailsActivity.ANIMAL, animal.getId());
        startActivity(intent);
    }
}
