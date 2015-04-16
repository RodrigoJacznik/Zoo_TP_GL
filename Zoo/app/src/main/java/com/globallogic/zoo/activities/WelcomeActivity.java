package com.globallogic.zoo.activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.globallogic.zoo.R;
import com.globallogic.zoo.fragments.AnimalListFragment;
import com.globallogic.zoo.fragments.ShowListFragment;
import com.globallogic.zoo.helpers.SharedPreferencesHelper;
import com.globallogic.zoo.models.Animal;


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

        if (savedInstanceState == null) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.welcomeactivity_fragment, new AnimalListFragment(), AnimalListFragment.TAG);
            ft.commit();
        }
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
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.welcomeactivity_fragment, new ShowListFragment());
        ft.addToBackStack(null);
        ft.setTransition(ft.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

    @Override
    public void OnAnimalClick(Animal animal) {
        Intent intent = new Intent(this, AnimalDetailsActivity.class);
        intent.putExtra(AnimalDetailsActivity.ANIMAL, animal.getId());
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
