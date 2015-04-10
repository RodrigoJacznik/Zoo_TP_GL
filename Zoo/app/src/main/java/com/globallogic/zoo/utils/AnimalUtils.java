package com.globallogic.zoo.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.globallogic.zoo.R;
import com.globallogic.zoo.models.Animal;

import org.apache.http.protocol.HTTP;

import java.io.File;

/**
 * Created by GL on 01/04/2015.
 */
public class AnimalUtils {

    static public Intent getShareAnimalIntent(Animal animal) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, animal.getName() + ": " + animal.getSpecie());
        intent.setType(HTTP.PLAIN_TEXT_TYPE);

        return intent;
    }

    static public Intent getShareMailAnimalIntent(File file, Context context, Animal animal) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("application/image");

        String subject = String.format(context.getResources().
                getString(R.string.animaldetailsactivity_email_subject), animal.getName());

        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));

        return emailIntent;
    }
}
