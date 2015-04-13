package com.globallogic.zoo.helpers;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.globallogic.zoo.R;
import com.globallogic.zoo.models.Animal;

import org.apache.http.protocol.HTTP;

import java.io.File;

/**
 * Created by GL on 01/04/2015.
 */
abstract public class AnimalHelper {

    static public Intent getShareAnimalIntent(Animal animal) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, animal.getName() + ": " + animal.getSpecie());
        intent.setType(HTTP.PLAIN_TEXT_TYPE);

        return intent;
    }

    static public Intent getShareMailAnimalIntent(Context context, File file, String name) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType(Intent.normalizeMimeType("image/*"));

        String subject = String.format(context.getResources().
                getString(R.string.animaldetailsactivity_email_subject), name);

        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));

        return emailIntent;
    }
}
