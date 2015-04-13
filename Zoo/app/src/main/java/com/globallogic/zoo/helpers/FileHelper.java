package com.globallogic.zoo.helpers;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.util.LongSparseArray;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * Created by GL on 13/04/2015.
 */
public class FileHelper {

    private static LongSparseArray<Integer> photo_count = new LongSparseArray();

    private final static String LOG_TAG = "FileHelper";
    private final static String ALBUM_NAME = "ZOO";
    private final static String EXTENSION = ".jpg";

    public static boolean checkExternalStorageStatus() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static File createFile(String name, Long animalId) {
        File photo = null;
        if (checkExternalStorageStatus()) {
            String postfix = getNextPostfix(animalId);
            File dir = getAlbumStorageDir();
            photo = new File(dir, name + postfix + EXTENSION);
            try {
                photo.createNewFile();
            } catch (IOException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
            }
        }
        return photo;
    }

    private static String getNextPostfix(long animalId) {
        int count = photo_count.get(animalId, -1);
        if (count == -1) {
            photo_count.put(animalId, 1);
            return "";
        }
        String postfix = "_" + count++;
        photo_count.put(animalId, count);
        return postfix;
    }

    private static String getPostfix(long animalId) {
        int count = photo_count.get(animalId, -1);
        return count == -1 ? "" : "_" + count;
    }

    private static File getAlbumStorageDir() {
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), ALBUM_NAME);
        if (!file.mkdirs()) {
            Log.e(LOG_TAG, "Directory not created");
        }
        return file;
    }

    public static File getFileByName(String name, Long animalId) {
        File dir = getAlbumStorageDir();
        return new File(dir, name + getPostfix(animalId) + EXTENSION);
    }

}