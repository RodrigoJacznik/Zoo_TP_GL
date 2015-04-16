package com.globallogic.zoo.activities;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import com.globallogic.zoo.R;
import com.globallogic.zoo.custom.views.ShareDialog;
import com.globallogic.zoo.fragments.AnimalDetailFragment;
import com.globallogic.zoo.helpers.AnimalHelper;
import com.globallogic.zoo.helpers.FileHelper;
import com.globallogic.zoo.models.Animal;

import java.io.File;


public class AnimalDetailsActivity extends BaseActivity implements
        ShareDialog.NoticeShareDialogListener,
        AnimalDetailFragment.AnimalDetailCallback {

    public final static String ANIMAL = "ANIMAL";
    public static final int REQUEST_CAMERA = 0;
    private Animal animal;
    private File animalPhotoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_animal_details);
        setUpActionBar();

        long animalId = getIntent().getLongExtra(ANIMAL, -1);

        if (savedInstanceState == null) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.animaldetailsactivity_frame, AnimalDetailFragment.newInstance(animalId));
            ft.commit();
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

    @Override
    public void onTakePhoto(Animal animal) {
        this.animal = animal;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photo = FileHelper.createFile(animal.getName(), animal.getId());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
        startActivityForResult(intent, AnimalDetailFragment.REQUEST_CAMERA);
    }
}
