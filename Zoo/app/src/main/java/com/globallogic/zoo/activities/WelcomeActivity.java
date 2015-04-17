package com.globallogic.zoo.activities;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.globallogic.zoo.R;
import com.globallogic.zoo.custom.views.ShareDialog;
import com.globallogic.zoo.fragments.AnimalDetailFragment;
import com.globallogic.zoo.fragments.AnimalListFragment;
import com.globallogic.zoo.fragments.ShowListFragment;
import com.globallogic.zoo.helpers.AnimalHelper;
import com.globallogic.zoo.helpers.FileHelper;
import com.globallogic.zoo.models.Animal;

import java.io.File;


public class WelcomeActivity extends BaseActivity implements
        AnimalListFragment.OnAnimalClickListener,
        ShareDialog.NoticeShareDialogListener,
        AnimalDetailFragment.AnimalDetailCallback {

    public final static String ANIMAL = "ANIMAL";
    public static final int REQUEST_CAMERA = 0;
    private Animal animal;
    private File animalPhotoFile;
    private Fragment firstFragment;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        makeDrawerLayout();

        if (savedInstanceState == null) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.welcomeactivity_fragment, new AnimalListFragment(), AnimalListFragment.TAG);
            ft.commit();
            checkNotification();
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkNotification() {
        Intent intent = getIntent();
        long animalId = intent.getLongExtra(ANIMAL, -1);
        if (animalId != -1) {
            FragmentManager fm = getFragmentManager();
            if (fm.executePendingTransactions()) {
                FragmentTransaction ft = fm.beginTransaction();
                ft.addToBackStack(null);
                ft.replace(R.id.welcomeactivity_fragment, AnimalDetailFragment.newInstance(animalId));
                ft.commit();
            }
        }
    }

    private void makeDrawerLayout() {
        String[] options = getResources().getStringArray(R.array.themes);
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.welcomeactivity_drawer_layout);

        ListView drawerList = (ListView) findViewById(R.id.welcomeactivity_drawer_list);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.app_name, R.string.app_name) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getSupportActionBar().setTitle("settings");
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("settings");
                invalidateOptionsMenu();
            }
        };
        drawerLayout.setDrawerListener(toggle);
        drawerList.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, options));
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


    private void logout() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.welcomeactivity_fragment, new ShowListFragment());
        ft.addToBackStack(null);
        ft.setTransition(ft.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

    @Override
    public void OnAnimalClick(long animalId) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.welcomeactivity_fragment, AnimalDetailFragment.newInstance(animalId));
        ft.addToBackStack(null);
        ft.setTransition(ft.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CAMERA:
                if (resultCode == Activity.RESULT_OK) {
                    animalPhotoFile = FileHelper.getFileByName(animal.getName(), animal.getId());
                    if (animalPhotoFile != null) {
                        createShareDialog();
                    }
                }
                break;
            default:
                break;
        }
    }


    @Override
    public void onTakePhoto(Animal animal) {
        this.animal = animal;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photo = FileHelper.createFile(animal.getName(), animal.getId());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
        startActivityForResult(intent, AnimalDetailFragment.REQUEST_CAMERA);
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String[] email) {
        Intent mailIntent = AnimalHelper.getShareMailAnimalIntent(this,
                animalPhotoFile,
                animal.getName());
        mailIntent.putExtra(Intent.EXTRA_EMAIL, email);

        if (mailIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mailIntent);
        } else {
            Toast.makeText(this,
                    getResources().getString(R.string.animaldetailsactivity_without_mailapp),
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        dialog.dismiss();
    }

    private void createShareDialog() {
        ShareDialog dialog = new ShareDialog();
        dialog.show(getFragmentManager(), ShareDialog.TAG);
    }

    public static Intent getWelcomeIntent(Context context) {
        return new Intent(context, WelcomeActivity.class);
    }
}
