package com.globallogic.zoo.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import com.globallogic.zoo.R;
import com.globallogic.zoo.activities.BaseActivity;
import com.globallogic.zoo.activities.SettingsActivity;

/**
 * Created by GL on 13/04/2015.
 */
public class SharedPreferencesHelper {
    private static final String SETTINGS = "SETTINGS";
    private static final String USER_NAME = "USER_NAME";
    private static final String PASS_NAME = "PASS_NAME";
    private static final String THEME = "THEME";

    public static boolean isUserLogin(Context context) {
        return getUserName(context) != null;
    }

    public static String getUserName(Context context) {
        SharedPreferences settings = context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
        return settings.getString(USER_NAME, null);
    }

    public static String getPass(Context context) {
        SharedPreferences settings = context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
        return settings.getString(PASS_NAME, null);
    }

    public static void setUserNameAndPass(Context context, String userName, String pass) {
        SharedPreferences settings = context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(USER_NAME, userName);
        editor.putString(PASS_NAME, pass);
        editor.apply();
    }

    public static void clearUserName(Context context) {
        SharedPreferences settings = context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(USER_NAME, null);
        editor.apply();
    }

    public static int getAnimalNotificationCount(Context context, long animalId) {
        SharedPreferences settings = context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
        return settings.getInt(String.valueOf(animalId), 0);
    }

    public static void incrementAnimalNotificationCount(Context context, long animalId) {
        String id = String.valueOf(animalId);
        SharedPreferences settings = context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
        int count = settings.getInt(id, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(id, ++count);
        editor.apply();
    }

    public static void resetAnimalNotificationCount(Context context, long animalId) {
        SharedPreferences settings = context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(String.valueOf(animalId), 0);
        editor.apply();
    }

    public static int getTheme(Context context) {
        SharedPreferences settings = context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
        return settings.getInt(THEME, R.style.AppTheme);
    }

    public static void setTheme(Context context, int theme) {
        SharedPreferences settings = context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(THEME, theme);
        editor.apply();
    }

    public static boolean isExtraActivate(Context context) {
        SharedPreferences settings = context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
        return settings.getInt(THEME, R.style.AppTheme) == R.style.ExtraTheme;
    }
}
