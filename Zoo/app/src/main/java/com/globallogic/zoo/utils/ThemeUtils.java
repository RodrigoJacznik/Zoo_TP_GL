package com.globallogic.zoo.utils;

import android.app.Activity;
import android.content.Intent;

import com.globallogic.zoo.R;

/**
 * Created by GL on 31/03/2015.
 */
public class ThemeUtils {
    private static int sTheme;
    public final static int THEME_DEFAULT = 0;
    public final static int THEME_EXTRA = 1;


    /**
     * Set the theme of the Activity, and restart it by creating a new Activity of the same type.
     */
    public static void changeToTheme(Activity activity, int theme) {
        if (sTheme != theme) {
            sTheme = theme;
            // Las siguientes 2 lineas NO deberían estar, sino que debería informarse al usuario que
            // debe reiniciar la aplicación para ver los cambios
            activity.finish();
            activity.startActivity(new Intent(activity, activity.getClass()));
        }
    }


    /** Set the theme of the activity, according to the configuration. */
    public static void onActivityCreateSetTheme(Activity activity) {
        switch (sTheme) {
            default:
            case THEME_DEFAULT:
                activity.setTheme(R.style.AppTheme);
                break;
            case THEME_EXTRA:
                activity.setTheme(R.style.ExtraTheme);
        }
    }

    public static int getsTheme() {
        return sTheme;
    }
}
