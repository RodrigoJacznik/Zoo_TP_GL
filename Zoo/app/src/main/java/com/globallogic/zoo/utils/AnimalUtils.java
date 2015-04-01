package com.globallogic.zoo.utils;

import android.content.Intent;

import com.globallogic.zoo.models.Animal;

import org.apache.http.protocol.HTTP;

/**
 * Created by GL on 01/04/2015.
 */
public class AnimalUtils {

    static public Intent getShareAnimalIntent(Animal animal) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, animal.getName() + ": " + animal.getDescripcion());
        intent.setType(HTTP.PLAIN_TEXT_TYPE);

        return intent;
    }
}
